/**
 * 
 */
package chk.jsphelper.module.wrapper;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Corestone H. Kang
 */
public class ConnWrapper implements Closeable
{
	private final Connection conn;
	private final List<Statement> stmts = new ArrayList<Statement>();

	public ConnWrapper (final Connection conn)
	{
		this.conn = conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#clearWarnings()
	 */
	public void clearWarnings () throws SQLException
	{
		this.conn.clearWarnings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#close()
	 */
	public void close () throws IOException
	{
		this.closeAll();
		try
		{
			this.conn.close();
		}
		catch (final Exception e)
		{
		}
	}

	public void closeAll ()
	{
		for (final Statement stmt : this.stmts)
		{
			try
			{
				this.stmts.remove(stmt);
				stmt.close();
			}
			catch (final Exception e)
			{
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#commit()
	 */
	public void commit () throws SQLException
	{
		this.conn.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
	 */
	public Array createArrayOf (final String typeName, final Object[] elements) throws SQLException
	{
		return this.conn.createArrayOf(typeName, elements);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createBlob()
	 */
	public Blob createBlob () throws SQLException
	{
		return this.conn.createBlob();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createClob()
	 */
	public Clob createClob () throws SQLException
	{
		return this.conn.createClob();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createNClob()
	 */
	public NClob createNClob () throws SQLException
	{
		return this.conn.createNClob();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStatement()
	 */
	public Statement createStatement () throws SQLException
	{
		final Statement s = this.conn.createStatement();
		this.stmts.add(s);
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	public Statement createStatement (final int resultSetType, final int resultSetConcurrency) throws SQLException
	{
		final Statement s = this.conn.createStatement(resultSetType, resultSetConcurrency);
		this.stmts.add(s);
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	public Statement createStatement (final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException
	{
		final Statement s = this.conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		this.stmts.add(s);
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getAutoCommit()
	 */
	public boolean getAutoCommit () throws SQLException
	{
		return this.conn.getAutoCommit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getMetaData()
	 */
	public DatabaseMetaData getMetaData () throws SQLException
	{
		return this.conn.getMetaData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	public int getTransactionIsolation () throws SQLException
	{
		return this.conn.getTransactionIsolation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#getWarnings()
	 */
	public SQLWarning getWarnings () throws SQLException
	{
		return this.conn.getWarnings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#isClosed()
	 */
	public boolean isClosed () throws SQLException
	{
		return this.conn.isClosed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#isReadOnly()
	 */
	public boolean isReadOnly () throws SQLException
	{
		return this.conn.isReadOnly();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	public CallableStatement prepareCall (final String sql) throws SQLException
	{
		final CallableStatement cs = this.conn.prepareCall(sql);
		this.stmts.add(cs);
		return cs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	public CallableStatement prepareCall (final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException
	{
		final CallableStatement cs = this.conn.prepareCall(sql, resultSetType, resultSetConcurrency);
		this.stmts.add(cs);
		return cs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	public CallableStatement prepareCall (final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException
	{
		final CallableStatement cs = this.conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		this.stmts.add(cs);
		return cs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	public PreparedStatement prepareStatement (final String sql) throws SQLException
	{
		final PreparedStatement ps = this.conn.prepareStatement(sql);
		this.stmts.add(ps);
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	public PreparedStatement prepareStatement (final String sql, final int autoGeneratedKeys) throws SQLException
	{
		final PreparedStatement ps = this.conn.prepareStatement(sql, autoGeneratedKeys);
		this.stmts.add(ps);
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	public PreparedStatement prepareStatement (final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException
	{
		final PreparedStatement ps = this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
		this.stmts.add(ps);
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	public PreparedStatement prepareStatement (final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException
	{
		final PreparedStatement ps = this.conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		this.stmts.add(ps);
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	public PreparedStatement prepareStatement (final String sql, final int[] columnIndexes) throws SQLException
	{
		final PreparedStatement ps = this.conn.prepareStatement(sql, columnIndexes);
		this.stmts.add(ps);
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
	 */
	public PreparedStatement prepareStatement (final String sql, final String[] columnNames) throws SQLException
	{
		final PreparedStatement ps = this.conn.prepareStatement(sql, columnNames);
		this.stmts.add(ps);
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#rollback()
	 */
	public void rollback () throws SQLException
	{
		this.conn.rollback();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	public void setAutoCommit (final boolean autoCommit) throws SQLException
	{
		this.conn.setAutoCommit(autoCommit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	public void setTransactionIsolation (final int level) throws SQLException
	{
		this.conn.setTransactionIsolation(level);
	}
}