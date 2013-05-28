package chk.jsphelper.module.pool;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import chk.jsphelper.Constant;
import chk.jsphelper.ObjectFactory;
import chk.jsphelper.module.wrapper.ConnWrapper;
import chk.jsphelper.object.enums.ObjectType;

public class ConnectionPoolManager
{
	private static ConnectionPoolManager poolManager;

	/**
	 * @return
	 */
	public static ConnectionPoolManager getInstance ()
	{
		if (ConnectionPoolManager.poolManager == null)
		{
			ConnectionPoolManager.poolManager = new ConnectionPoolManager();
		}
		return ConnectionPoolManager.poolManager;
	}

	private final Map<String, ConnectionPool> poolContainer = new HashMap<String, ConnectionPool>();

	private ConnectionPoolManager ()
	{
		try
		{
			final Iterator<String> element = ObjectFactory.getObjectIDs(ObjectType._DATASOURCE);
			while (element.hasNext())
			{
				final String key = element.next();
				this.poolContainer.put(key, new ConnectionPool(ObjectFactory.getDataSource(key)));
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @param datasourceID
	 * @return
	 * @throws Exception
	 */
	public ConnWrapper createConnection (final String datasourceID) throws Exception
	{
		return this.getPool(datasourceID).createConnection();
	}

	/**
	 * @param datasourceID
	 * @param serviceID
	 * @return
	 * @throws Exception
	 */
	public ConnWrapper getConnection (final String datasourceID, final String serviceID) throws Exception
	{
		final ConnWrapper conn = this.getPool(datasourceID).getConnection(serviceID);
		if (conn.getTransactionIsolation() != Connection.TRANSACTION_NONE)
		{
			conn.setAutoCommit(false);
		}
		return conn;
	}

	/**
	 * @param datasourceID
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public ConnWrapper recreate (final String datasourceID, final ConnWrapper conn) throws Exception
	{
		return this.getPool(datasourceID).recreate(conn);
	}

	/**
	 * @throws Exception
	 */
	public void releaseAll () throws Exception
	{
		final Iterator<String> keys = this.poolContainer.keySet().iterator();
		while (keys.hasNext())
		{
			final String key = keys.next();
			this.getPool(key).releaseAll();
		}
	}

	/**
	 * @param datasourceID
	 * @param conn
	 * @param serviceID
	 * @param isSucess
	 * @throws Exception
	 */
	public void releaseConnection (final String datasourceID, final ConnWrapper conn, final String serviceID, final boolean isSucess)
	{
		this.getPool(datasourceID).releaseConnection(conn, serviceID, isSucess);
	}

	/**
	 * @return
	 */
	public int size (final String poolName)
	{
		return this.getPool(poolName).size();
	}

	private ConnectionPool getPool (final String name)
	{
		if (!this.poolContainer.containsKey(name))
		{
			Constant.getLogger().error("[{}]에 해당되는 Datasource 오브젝트의 id가 없습니다.", name);
			return null;
		}
		return this.poolContainer.get(name);
	}
}