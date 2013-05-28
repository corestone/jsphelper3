package chk.jsphelper.engine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import chk.jsphelper.Constant;
import chk.jsphelper.ObjectFactory;
import chk.jsphelper.Parameter;
import chk.jsphelper.engine.ext.sql.CallableStatementManager;
import chk.jsphelper.engine.ext.sql.DataUtil;
import chk.jsphelper.engine.ext.sql.DmlType;
import chk.jsphelper.engine.ext.sql.PreparedStatementManager;
import chk.jsphelper.module.mapper.DataMapper;
import chk.jsphelper.module.mapper.ParameterMapper;
import chk.jsphelper.module.wrapper.ConnWrapper;
import chk.jsphelper.object.DataSource;
import chk.jsphelper.object.Sql;
import chk.jsphelper.util.StringUtil;

/**
 * @author Corestone
 */
public class SqlEngine implements InterfaceEngine
{
	private final ConnWrapper conn;
	private DataMapper dataMapper;
	private boolean isClobUpdate = false;
	private final String jdbcID;
	private final String logID;
	private Map<String, String> outParameter;
	private int pagingTotalSize = 0;
	private final Parameter param;
	private final Sql sql;
	private int updateCount = 0;

	/**
	 * @param conn
	 * @param object
	 * @param parameters
	 * @param jdbcid
	 */
	public SqlEngine (final ConnWrapper conn, final Sql object, final Parameter parameters, final String jdbcid)
	{
		this.sql = object;
		this.param = parameters;
		this.conn = conn;
		this.jdbcID = jdbcid;
		this.logID = (this.jdbcID == null) ? ("SQL:'" + this.sql.getId() + "'") : ("JDBC:'" + this.jdbcID + "'의 SQL:'" + this.sql.getId() + "'");
	}

	public void execute () throws Exception
	{
		boolean isPaging = false;

		try
		{
			Constant.getLogger().debug("바인드 갯수 : {}", this.sql.getBinds().size());

			isPaging = this.sql.isPaging();

			// sql문에서 쿼리 타입을 찾아내고 XML문에 정의 되어 있으면 그것으로 강제세팅을 한다.
			final DmlType dmlType = this.checkDMLType();

			final ParameterMapper pm = new ParameterMapper(this.param);
			pm.checkParamQuery(this.sql.getQuery(), this.sql.getBinds());
			final String[] queries = pm.convertMappingTexts(this.sql.getQuery());

			if (isPaging)
			{
				if (dmlType != DmlType.SELECT)
				{
					throw new Exception(this.logID + "는 SELECT 절이 아닌데도 페이징처리가 정의되어 있습니다.");
				}
				if (!this.param.containsKey(Constant.getValue("KeyName.LinePerPage", "_linePerPage")))
				{
					throw new Exception(this.logID + "에서 페이징 처리를 위한 파라미터가 정의되지 않았습니다.");
				}
				if (this.param.getCurrentIndex() == -1)
				{
					throw new Exception(this.logID + "에서 페이징 처리를 위한 파라미터의 설정인덱스가 세팅되지 않았습니다.");
				}
			}

			synchronized (this.conn)
			{
				if (dmlType == DmlType.SELECT)
				{
					this.executeQuery(queries[0], isPaging);
				}
				else if (dmlType == DmlType.PROCEDURE)
				{
					this.executeProc(queries[0], pm);
				}
				else
				{
					this.updateCount = this.executeUpdate(queries, pm);
				}
			}
		}
		catch (final Exception e)
		{
			throw e;
		}
		finally
		{
		}
	}

	public Map<String, Object> getValueObject ()
	{
		final Map<String, Object> hm = new HashMap<String, Object>();
		hm.put("pagingTotalSize", this.pagingTotalSize);
		hm.put("updateCount", this.updateCount);
		hm.put("DataMapper", this.dataMapper);
		hm.put("OutParameter", this.outParameter);
		return hm;
	}

	private DmlType checkDMLType ()
	{
		String chkquery = "";
		final String temp = StringUtil.compressWhitespace(this.sql.getQuery());
		chkquery = temp.substring(0, temp.indexOf(" "));

		if (chkquery.equalsIgnoreCase("SELECT"))
		{
			return DmlType.SELECT;
		}
		else if (chkquery.equalsIgnoreCase("INSERT") || chkquery.equalsIgnoreCase("UPDATE") || chkquery.equalsIgnoreCase("DELETE"))
		{
			return DmlType.EXECUTE;
		}
		else if (chkquery.equalsIgnoreCase("CALL"))
		{
			return DmlType.PROCEDURE;
		}
		else
		{
			return DmlType.ETC;
		}
	}

	private boolean executeProc (final String currentQuery, final ParameterMapper paramMapper) throws SQLException, Exception
	{
		final CallableStatementManager csm = new CallableStatementManager(this.sql.getBinds(), this.param);
		boolean isSuccess = true;

		try
		{
			Constant.getLogger().info("QUERY ({}) :\n{}", new String[] { this.logID, currentQuery });
			for (int i = 0, z = paramMapper.getMappingSize(); i < z; i++)
			{
				csm.bindingCS(this.conn, currentQuery, i);
				isSuccess = csm.execute();
				final String[] op = csm.getOutParameter();
				final List<String> keys = new ArrayList<String>();
				final Map<String, String> outParameter = new HashMap<String, String>();
				for (int j = 1, y = op.length; j < y; j++)
				{
					if (op[j] != null)
					{
						final String key = this.sql.getBinds().get(j - 1).getValue();
						keys.add(key);
						outParameter.put(key, csm.getString(key));
					}
				}
			}
		}
		catch (final SQLException sqle)
		{
			isSuccess = false;
			Constant.getLogger().error(" Invalid Query ({}) : {}", new String[] { this.logID, currentQuery });
			throw sqle;
		}
		catch (final Exception e)
		{
			isSuccess = false;
			throw e;
		}
		return isSuccess;
	}

	private void executeQuery (final String currentQuery, final boolean isPaging) throws SQLException, Exception
	{
		ResultSet rtnRS = null;
		final StringBuilder countQuery = new StringBuilder();
		final StringBuilder pagingQuery = new StringBuilder();
		final PreparedStatementManager psm = new PreparedStatementManager(this.sql.getBinds(), this.param);

		try
		{
			// 페이징 처리를 위해 쿼리를 파싱하여 해당 페이지 만큼만 레코드를 가지고 오게 한다.
			if (isPaging)
			{
				// 쿼리에서 ORDER 절까지의 위치.
				int lastIndex = currentQuery.toUpperCase().indexOf("ORDER");
				if (lastIndex == -1)
				{
					lastIndex = currentQuery.length();
				}
				// 쿼리에서 FROM 절까지의 위치 (인라인 쿼리를 위해 "#FROM"으로 강제로 FROM 위치를 세팅할 수 있다.)
				int startIndex = currentQuery.lastIndexOf("#") + 1;
				if (startIndex < 1)
				{
					startIndex = currentQuery.toUpperCase().indexOf("FROM");
				}
				else
				{
					lastIndex--;
				}
				// ORDER BY 절 다음에 있는 ')'부터 끝까지는 삭제하지 않는다.
				int braceIndex = StringUtils.indexOf(currentQuery.toUpperCase(), ")", lastIndex);
				if (braceIndex <= lastIndex)
				{
					braceIndex = currentQuery.length();
				}

				countQuery.append("SELECT COUNT(*) CNT FROM");
				countQuery.append(currentQuery.substring(startIndex + 4, lastIndex));
				countQuery.append(" ");
				countQuery.append(currentQuery.substring(braceIndex));

				Constant.getLogger().info("Counter Query ({}):\n{}", new String[] { this.logID, countQuery.toString() });
				psm.bindingPS(this.conn, countQuery.toString());
				rtnRS = psm.executeQuery();
				Constant.getLogger().info("{}의 실행시간 : {}", new String[] { this.logID, psm.getExecuteTime() });

				if (rtnRS.next())
				{
					this.pagingTotalSize = rtnRS.getInt(1);
				}
				final String query = currentQuery.replaceFirst("#", "");
				final int selectIndex = currentQuery.toUpperCase().indexOf("SELECT");

				// 각각의 RDMS 에 맞게 쿼리를 만들어서 페이징에 필요한 만큼만 레코드를 가지고 온다.
				final int iIndexCurrent = this.param.getCurrentIndex();
				final int iLinePerPage = this.param.getLinePerPage();

				int pagingVar1 = 0;
				int pagingVar2 = 0;
				final DataSource dataSource = ObjectFactory.getDataSource(this.sql.getDatasourceName());
				switch (dataSource.getVendorType())
				{
					case ORACLE :
						pagingQuery.append("SELECT * FROM ( SELECT ROWNUM ROWNUM_COUNT, PAGING_QUERY.* FROM (\n");
						pagingQuery.append(query);
						pagingQuery.append("\n) PAGING_QUERY WHERE ROWNUM <= ? ) WHERE ROWNUM_COUNT >= ?");
						pagingVar1 = iLinePerPage * iIndexCurrent;
						pagingVar2 = (iLinePerPage * (iIndexCurrent - 1)) + 1;
						break;
					case MSSQL :
					case DB2 :
						pagingQuery.append("SELECT * FROM ( SELECT ROW_NUMBER() OVER(");
						pagingQuery.append(query.substring(lastIndex));
						pagingQuery.append(") AS ROWNUM_COUNT, ");
						pagingQuery.append(query.substring(selectIndex + 6, lastIndex));
						pagingQuery.append(" ) PAGING_QUERY WHERE ROWNUM_COUNT BETWEEN ? AND ?");
						pagingVar1 = (iLinePerPage * (iIndexCurrent - 1)) + 1;
						pagingVar2 = iLinePerPage * iIndexCurrent;
						break;
					case MYSQL :
						pagingQuery.append(query);
						pagingQuery.append("\nLIMIT ?, ?");
						pagingVar1 = iLinePerPage * (iIndexCurrent - 1);
						pagingVar2 = iLinePerPage;
						break;
				}
				Constant.getLogger().info("Paging QUERY ({}):\n{}", new String[] { this.logID, pagingQuery.toString() });
				psm.bindingPS4Paging(this.conn, pagingQuery.toString(), pagingVar1, pagingVar2);
				rtnRS = psm.executeQuery();
				Constant.getLogger().info("{}의 실행시간 : {}", new String[] { this.logID, psm.getExecuteTime() });
			}
			else
			{
				Constant.getLogger().info("QUERY ({}) :\n{}", new String[] { this.logID, currentQuery });
				if (currentQuery.toUpperCase().indexOf("FOR UPDATE") > 1)
				{
					this.isClobUpdate = true;
				}
				psm.bindingPS(this.conn, currentQuery);
				rtnRS = psm.executeQuery();
				Constant.getLogger().info("{}의 실행시간 : {}", new String[] { this.logID, psm.getExecuteTime() });
			}
			if (rtnRS == null)
			{
			}
			if (this.isClobUpdate)
			{
				this.updateCLOB(rtnRS);
			}
			else
			{
				this.dataMapper = new DataMapper(rtnRS);
			}
		}
		catch (final SQLException sqle)
		{
			Constant.getLogger().error(" Invalid Query ({}) :\n{}", new String[] { this.logID, psm.getQueryString() });
			throw sqle;
		}
		catch (final Exception e)
		{
			throw e;
		}
	}

	private int executeUpdate (final String[] queries, final ParameterMapper paramMapper) throws SQLException, Exception
	{
		final PreparedStatementManager psm = new PreparedStatementManager(this.sql.getBinds(), this.param);
		int applyCount = 0;
		try
		{
			// 쿼리에 바인드 되는 파라미터의 갯수가 하나라서 쿼리가 하나인 경우
			if (queries.length == 1)
			{
				Constant.getLogger().info("QUERY ({}) :\n{}", new String[] { this.logID, queries[0] });
				// 파라미터의 갯수가 1개일 경우 (바인드가 한번만 될 때)
				if (paramMapper.getMappingSize() == 1)
				{
					psm.bindingPS(this.conn, queries[0]);
					applyCount = psm.executeUpdate();
				}
				// 파라미터의 갯수가 2개 이상일 경우 (바인드가 여러번 될 때)
				else
				{
					psm.bindingPS(this.conn, queries[0]);
					for (int i = 0, z = paramMapper.getMappingSize(); i < z; i++)
					{
						psm.setBinding(i);
						psm.addBatch();
					}
					final int[] applyCounts = psm.executeBatch();
					for (final int applyCount2 : applyCounts)
					{
						applyCount += applyCount2;
					}
				}
				Constant.getLogger().info("{}의 실행시간 : {}", new String[] { this.logID, psm.getExecuteTime() });
			}
			// 쿼리에 바인드 되는 파라미터의 갯수가 여러개라서 쿼리와 파라미터 배열의 크기가 같은 경우
			else if (queries.length == paramMapper.getMappingSize())
			{
				final int[] applyCounts = new int[queries.length];
				for (int i = 0, z = paramMapper.getMappingSize(); i < z; i++)
				{
					Constant.getLogger().info("QUERY ({}) :\n{}", new String[] { this.logID, queries[i] });
					psm.bindingPS(this.conn, queries[i], i);
					applyCounts[i] = psm.executeUpdate();
				}
				for (final int applyCount2 : applyCounts)
				{
					applyCount += applyCount2;
				}
				Constant.getLogger().info("{}의 실행시간 : {} (마지막 건)", new String[] { this.logID, psm.getExecuteTime() });
			}
			// 쿼리의 갯수와 바인드 되는 배열의 크기가 다른 경우 에러임
			else
			{
				throw new Exception("쿼리 배열의 크기와 바인드 배열의 크기가 서로 다릅니다.");
			}
			if (applyCount == 0)
			{
				Constant.getLogger().warn("{} 쿼리의 테이블 반영이 한건도 안되었습니다. 쿼리를 참고해 주시기 바랍니다.", this.logID);
			}
			return applyCount;
		}
		catch (final SQLException sqle)
		{
			Constant.getLogger().error("Invalid Query ({}) :\n{}", new String[] { this.logID, psm.getQueryString() });
			throw sqle;
		}
		catch (final Exception e)
		{
			throw e;
		}
	}

	private void updateCLOB (final ResultSet rs) throws Exception
	{
		try
		{
			if (rs.next())
			{
				for (int i = 0; i < this.sql.getClobs().size(); i++)
				{
					final Map<String, String> clob = this.sql.getClobs().get(i);
					DataUtil.setClobData(rs, clob.get("FIELD"), this.param.getValue(clob.get("VALUE")));
					Constant.getLogger().debug("{}필드에 CLOB으로 {}값 기록", new String[] { clob.get("FIELD"), clob.get("VALUE") });
				}
			}
		}
		catch (final Exception e)
		{
			throw e;
		}
	}
}