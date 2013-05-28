package chk.jsphelper.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import chk.jsphelper.object.enums.ExcelDataType;
import chk.jsphelper.object.enums.ObjectType;
import chk.jsphelper.object.sub.ExcelColtype;

public class Excel implements ServiceObject
{
	public static final ObjectType TYPE = ObjectType.EXCEL;
	private final List<String> colType = new ArrayList<String>();
	private String description;
	private String id;
	private int index = -1;
	private String path;
	private ExcelDataType sourceEnum;
	private String srcenc = "UTF-8";
	private int start = 1;
	private ExcelDataType targetEnum;
	private String transaction;
	private String trgenc = "UTF-8";

	/**
	 * @param ec
	 */
	public void addColType (final ExcelColtype ec)
	{
		this.colType.add(ec.getValue());
	}

	/**
	 * @return - 각 컬럼의 자료 타입
	 */
	public List<String> getColType ()
	{
		return this.colType;
	}

	/**
	 * 오브젝트의 설명을 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 설명인 description
	 */
	public final String getDescription ()
	{
		return this.description;
	}

	/**
	 * 오브젝트의 id를 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 id
	 */
	public final String getId ()
	{
		return this.id;
	}

	/**
	 * @return - 작업하는 Transaction 객체의 SQL 인덱스
	 */
	public int getIndex ()
	{
		return this.index;
	}

	public ObjectType getObjectType ()
	{
		return Excel.TYPE;
	}

	/**
	 * @return - 엑셀 파일 경로
	 */
	public String getPath ()
	{
		return this.path;
	}

	/**
	 * @return - 소스 타입
	 */
	public ExcelDataType getSourceEnum ()
	{
		return this.sourceEnum;
	}

	/**
	 * 오브젝트의 소스 인코딩을 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 소스 인코딩
	 */
	public String getSrcenc ()
	{
		return this.srcenc;
	}

	/**
	 * @return - 엑셀에서 데이타를 읾을 때 시작 행번호
	 */
	public int getStart ()
	{
		return this.start;
	}

	/**
	 * @return - 대상 타입
	 */
	public ExcelDataType getTargetEnum ()
	{
		return this.targetEnum;
	}

	/**
	 * @return - 사용될 Transaction ID
	 */
	public String getTransaction ()
	{
		return this.transaction;
	}

	/**
	 * 오브젝트의 타겟 인코딩을 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 타겟 인코딩
	 */
	public String getTrgenc ()
	{
		return this.trgenc;
	}

	/**
	 * @param description
	 */
	public void setDescription (final String description)
	{
		this.description = description;
	}

	/**
	 * @param id
	 */
	public void setId (final String id)
	{
		this.id = id;
	}

	/**
	 * @param index
	 */
	public void setIndex (final int index)
	{
		this.index = index;
	}

	/**
	 * @param path
	 */
	public void setPath (final String path)
	{
		this.path = path;
	}

	/**
	 * @param source
	 */
	public void setSource (final String source)
	{
		if (source.equals("EXCEL"))
		{
			this.sourceEnum = ExcelDataType.EXCEL;
		}
		else if (source.equals("DATALIST"))
		{
			this.sourceEnum = ExcelDataType.DATALIST;
		}
		else if (source.equals("DB"))
		{
			this.sourceEnum = ExcelDataType.DB;
		}
	}

	/**
	 * 오브젝트의 소스 인코딩을 지정하는 메소드이다.
	 * 
	 * @param srcenc
	 *            - 소스 인코딩
	 */
	public void setSrcenc (final String srcenc)
	{
		this.srcenc = srcenc;
	}

	/**
	 * @param start
	 */
	public void setStart (final int start)
	{
		this.start = start;
	}

	/**
	 * @param target
	 */
	public void setTarget (final String target)
	{
		if (target.equals("EXCEL"))
		{
			this.targetEnum = ExcelDataType.EXCEL;
		}
		else if (target.equals("DATALIST"))
		{
			this.targetEnum = ExcelDataType.DATALIST;
		}
		else if (target.equals("DB"))
		{
			this.targetEnum = ExcelDataType.DB;
		}
	}

	/**
	 * @param transaction
	 */
	public void setTransaction (final String transaction)
	{
		this.transaction = transaction;
	}

	/**
	 * 오브젝트의 타겟 인코딩을 지정하는 메소드이다.
	 * 
	 * @param trgenc
	 *            - 타겟 인코딩
	 */
	public void setTrgenc (final String trgenc)
	{
		this.trgenc = trgenc;
	}

	@Override
	public final String toString ()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}