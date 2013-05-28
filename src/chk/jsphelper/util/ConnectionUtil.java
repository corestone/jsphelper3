package chk.jsphelper.util;

import java.sql.Connection;

import chk.jsphelper.Constant;
import chk.jsphelper.module.pool.ConnectionPoolManager;
import chk.jsphelper.module.wrapper.ConnWrapper;

public class ConnectionUtil
{
	/**
	 * 커넥션풀에서 커넥션을 가지고 오는 메소드이다.<br>
	 * 이 메소드를 통해 Connection을 가지고 오는 경우는 Transaction 프레임워크를 이용하여 한번에 처리할 수 없는 경우로 한정하여 가지고 와야 하며<br>
	 * 일반적인 경우에는 사용하지 않은 것을 권장한다.
	 * jdbc.xml의 datasource 속성을 통해 해당 값에 맞는 커넥션을 가지고 오기 때문에 서비스함수 아이디가 필요하다.<br>
	 * 여기에서 얻은 커넥션은 기본적으로 AutoCommit 이 false 인 상태이며 이 상태를 변경하지 말고 사용해야 한다.<br>
	 * 
	 * @param jdbcID
	 *            - 커넥션을 가지고 올 datasource를 알기 위한 jdbc 서비스함수 아이디
	 * @return - 해당 jdbc에 등록된 datasource의 커넥션
	 */
	public static ConnWrapper getConnection (final String datasourceID)
	{
		Constant.getLogger().warn("[{}]에 해당되는 커넥션을 ConnectionUtil을 통해 가지고 나왔습니다.", datasourceID);
		ConnWrapper conn = null;
		try
		{
			conn = ConnectionPoolManager.getInstance().getConnection(datasourceID, "ConnectionUtil.getConnection");
			if (conn.getTransactionIsolation() != Connection.TRANSACTION_NONE)
			{
				conn.setAutoCommit(false);
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error("[{}]에 해당되는 데이타소스를 설정하는데 문제가 발생하여 해당 커넥션을 가져오지 못했습니다.", datasourceID, e);
			return null;
		}
		return conn;
	}

	/**
	 * 커넥션을 풀로 반환하는 메소드이다. 풀로 반환하면서 성공여부에 따라 자동으로 commit이나 rollback를 한다.
	 * 
	 * @param conn
	 *            - 커넥션
	 * @param succ
	 *            - 트랜젝션의 성공여부
	 * @return - 커넥션의 릴리즈가 성공했는지 여부
	 */
	public static boolean relConnection (ConnWrapper conn, final String datasourceID, final boolean succ)
	{
		Constant.getLogger().warn("[{}]에 해당되는 커넥션을 ConnectionUtil을 통해 집어 넣었습니다.", datasourceID);
		boolean rtnB = false;
		ConnWrapper chkConn = null;
		try
		{
			chkConn = ConnectionPoolManager.getInstance().createConnection(datasourceID);
			final String chkURL = chkConn.getMetaData().getURL();
			final String chkUser = chkConn.getMetaData().getUserName();
			if (chkURL.equals(conn.getMetaData().getURL()) && chkUser.equals(conn.getMetaData().getUserName()))
			{
				ConnectionPoolManager.getInstance().releaseConnection(datasourceID, conn, "ServiceFactory.relConnection", succ);
				rtnB = true;
			}
			else
			{
				Constant.getLogger().error("{}에 해당되는 데이타소스와 반환하려는 데이타소스가 서로 달라서 쿼리를 롤백시켰습니다.", datasourceID);
				if (conn.getTransactionIsolation() != Connection.TRANSACTION_NONE)
				{
					conn.rollback();
					conn.close();
					conn = null;
				}
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error("[{}]에 해당되는 데이타소스를 커밋/반한하는데 문제가 발생하였습니다.", datasourceID, e);
			rtnB = false;
			CloseUtil.closeObject(conn, false);
		}
		finally
		{
			CloseUtil.closeObject(chkConn, false);
		}
		return rtnB;
	}

	private ConnectionUtil ()
	{
	}
}