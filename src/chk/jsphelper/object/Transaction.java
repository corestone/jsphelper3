package chk.jsphelper.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import chk.jsphelper.ObjectFactory;
import chk.jsphelper.object.enums.ObjectType;
import chk.jsphelper.object.sub.TransactionSqls;

public class Transaction implements ServiceObject
{
	public static final ObjectType TYPE = ObjectType.TRANSACTION;
	protected String description;
	protected String id;
	protected List<TransactionSqls> sqls = new ArrayList<TransactionSqls>();
	protected String srcenc = "UTF-8";
	protected String trgenc = "UTF-8";

	public void addSqls (final TransactionSqls sqls)
	{
		this.sqls.add(sqls);
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

	public ObjectType getObjectType ()
	{
		return Transaction.TYPE;
	}

	public int getPageIndex (final int index)
	{
		return this.sqls.get(index).getPageindex();
	}

	public List<String> getParamKeys (final int index)
	{
		return this.sqls.get(index).getParamKeys();
	}

	public List<String> getParamValues (final int index)
	{
		return this.sqls.get(index).getParamValues();
	}

	public List<String> getReturnFields (final int index)
	{
		return this.sqls.get(index).getReturnFields();
	}

	public List<String> getReturnKeys (final int index)
	{
		return this.sqls.get(index).getReturnKeys();
	}

	public int getSize ()
	{
		return this.sqls.size();
	}

	public Sql getSql (final int index)
	{
		return ObjectFactory.getSql(this.sqls.get(index).getSql());
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
	 * 오브젝트의 타겟 인코딩을 가지고 오는 메소드이다.
	 * 
	 * @return - 오브젝트의 타겟 인코딩
	 */
	public String getTrgenc ()
	{
		return this.trgenc;
	}

	public void setDescription (final String description)
	{
		this.description = description;
	}

	public void setId (final String id)
	{
		this.id = id;
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