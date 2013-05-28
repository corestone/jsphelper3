package chk.jsphelper.module.pool;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import chk.jsphelper.Constant;
import chk.jsphelper.ObjectFactory;
import chk.jsphelper.module.wrapper.ConnWrapper;
import chk.jsphelper.object.DataSource;

public class ConnectionPool
{
	private final List<ConnWrapper> connPool = new ArrayList<ConnWrapper>();
	private final String driver;
	private final int idleSize;
	private int maxSize;
	private final String poolName;
	private final Map<String, String> property;
	private final String url;
	private int useCount = 0;

	/**
	 * @param datasource
	 */
	ConnectionPool (final DataSource datasource)
	{
		this.property = datasource.getProperty();
		this.poolName = datasource.getId();
		this.idleSize = datasource.getIdlesize();
		this.maxSize = datasource.getMaxsize();
		this.driver = datasource.getDriver();
		this.url = datasource.getUrl();

		if (this.idleSize > this.maxSize)
		{
			this.maxSize = this.idleSize;
		}
		this.init();
	}

	/**
	 * @return
	 * @throws Exception
	 */
	ConnWrapper createConnection ()
	{
		final Properties prop = new Properties();
		final Iterator<String> element = this.property.keySet().iterator();
		while (element.hasNext())
		{
			final String key = element.next();
			prop.put(key, this.property.get(key));
		}
		Connection conn = null;
		try
		{
			Class.forName(this.driver).newInstance();
			conn = DriverManager.getConnection(this.url, prop);
		}
		catch (final Exception e)
		{
			Constant.getLogger().error("[{}]커넥션을 생성할 때 {} 예외가 발생하였습니다.", new Object[] { this.poolName, e.getClass().getName() });
		}
		return new ConnWrapper(conn);
	}

	/**
	 * @return
	 */
	ConnWrapper getConnection (final String serviceID) throws Exception
	{
		// idleSize과 maxSize가 둘 다 0 이면 커넥션풀을 사용하지 않고 매번 커넥션을 만들어서 사용한다.
		if ((this.idleSize == 0) && (this.maxSize == 0))
		{
			ConnWrapper conn = null;
			conn = this.createConnection();
			return conn;
		}
		else
		{
			synchronized (this.connPool)
			{
				try
				{
					while ((this.connPool.size() == 0) && (this.useCount >= this.maxSize))
					{
						this.connPool.wait();
					}
				}
				catch (final Exception e)
				{
					Constant.getLogger().error("[{}]커넥션풀에서 객체를 기다릴 때 {} 예외가 발생하였습니다.", new Object[] { this.poolName, e.getClass().getName() });
					throw e;
				}
				if ((this.connPool.size() == 0) && (this.useCount < this.maxSize))
				{
					this.connPool.add(this.createConnection());
				}
				ConnWrapper conn = this.connPool.remove(0);
				Statement stmt = null;
				try
				{
					if (Constant.getValue("Connection.Check", "false").equals("true"))
					{
						final String query = ObjectFactory.getDataSource(this.poolName).getSql();
						if ((query == null) || query.equals(""))
						{
							Constant.getLogger().error("커넥션을 체크하기 위한 옵션을 프로퍼티에 세팅하였다면 [{}] 데이타소스 XML에도 반드시 테스트쿼리를 설정해야 합니다.", new Object[] { this.poolName });
						}
						stmt = conn.createStatement();
						stmt.execute(query);
					}
				}
				catch (final Exception e)
				{
					conn = this.recreate(conn);
					Constant.getLogger().warn("커넥션에 문제가 있어서 다시 생성합니다.");
				}
				finally
				{
					if (stmt != null)
					{
						stmt.close();
						stmt = null;
					}
				}
				this.useCount++;
				if (!serviceID.endsWith(" Repeat"))
				{
					Constant.getLogger().debug("{}로부터 DB 커넥션 반출 - 가용량 : {} ({})", new Object[] { serviceID, this.connPool.size(), (this.maxSize - this.useCount) });
				}
				return conn;
			}
		}
	}

	/**
	 * @param conn
	 * @return
	 */
	ConnWrapper recreate (ConnWrapper conn) throws Exception
	{
		this.closeConnection(conn, null);
		conn = this.createConnection();
		return conn;
	}

	/**
	 * @throws Exception
	 */
	void releaseAll () throws Exception
	{
		synchronized (this.connPool)
		{
			while (this.connPool.size() > 0)
			{
				final ConnWrapper conn = this.connPool.remove(this.connPool.size() - 1);
				this.closeConnection(conn, null);
				this.connPool.notifyAll();
			}
		}
	}

	/**
	 * @param conn
	 */
	void releaseConnection (final ConnWrapper conn, final String serviceID, final boolean isSucess)
	{
		synchronized (this.connPool)
		{
			if (this.idleSize > this.connPool.size())
			{
				try
				{
					if (conn.getTransactionIsolation() != Connection.TRANSACTION_NONE)
					{
						if (isSucess)
						{
							conn.commit();
						}
						else
						{
							conn.rollback();
						}
						conn.setAutoCommit(true);
					}
					conn.closeAll();
					this.connPool.add(conn);
				}
				catch (final Exception e)
				{
					this.connPool.add(this.createConnection());
				}
				this.useCount--;
				if (!serviceID.endsWith(" Repeat"))
				{
					Constant.getLogger().debug("{}로부터 DB 커넥션 반입 - 가용량 : {} ({})", new Object[] { serviceID, this.connPool.size(), (this.maxSize - this.useCount) });
				}
			}
			else
			{
				this.closeConnection(conn, "여분의 [" + this.poolName + "]커넥션을 닫고 폐기합니다.");
			}
			this.connPool.notifyAll();
		}
	}

	/**
	 * @return
	 */
	int size ()
	{
		return this.connPool.size();
	}

	private void closeConnection (ConnWrapper conn, final String logMsg)
	{
		if (conn != null)
		{
			try
			{
				conn.close();
				conn = null;
			}
			catch (final Exception e)
			{
				Constant.getLogger().error("[{}]커넥션을 닫을 때 {} 예외가 발생하였습니다.", new Object[] { this.poolName, e.getClass().getName() });
			}
			if (logMsg != null)
			{
				Constant.getLogger().debug(logMsg);
			}
		}
	}

	/**
	 * 
	 */
	private void init ()
	{
		try
		{
			Constant.getLogger().info("[{}] 커넥션을 만듭니다.", new Object[] { this.poolName });
			for (int i = 0; i < this.idleSize; i++)
			{
				this.connPool.add(this.createConnection());
			}
			if (this.idleSize > 0)
			{
				final ConnWrapper conn = this.connPool.get(0);
				final DatabaseMetaData meta = conn.getMetaData();
				Constant.getLogger().info("Database :  {} ({})" + new Object[] { meta.getDatabaseProductName(), meta.getDatabaseProductVersion() });
				Constant.getLogger().info("JDBC Driver : {} ({})" + new Object[] { meta.getDriverName(), meta.getDriverVersion() });
			}
			else
			{
				Constant.getLogger().info("[{}]에 대한 커넥션 초기갯수가 0개이므로 커넥션을 만들지 않았습니다.", new Object[] { this.poolName });
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
		}
	}
}