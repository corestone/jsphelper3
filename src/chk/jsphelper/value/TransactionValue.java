package chk.jsphelper.value;

import javax.servlet.http.HttpServletRequest;

import chk.jsphelper.Constant;
import chk.jsphelper.DataList;
import chk.jsphelper.ObjectFactory;
import chk.jsphelper.Parameter;
import chk.jsphelper.object.Transaction;

public class TransactionValue extends AbstractValue
{
	protected DataList[] dl;
	protected int size = 0;
	protected int[] totalSize;
	protected int[] updateCount;

	public TransactionValue (final String functionID, final int size)
	{
		super(functionID);
		this.size = size;
	}

	/**
	 * 해당 레코드 셋을 반환하는 메소드이다.
	 * 
	 * @param index
	 *            - jdbc오브젝트의 sqls태그 인덱스
	 * @return - 쿼리를 통해 얻은 레코드셋 객체
	 */
	public DataList getDataList (final int index)
	{
		return this.dl[index];
	}

	@Override
	public Transaction getObject ()
	{
		return ObjectFactory.getTransaction(this.functionID);
	}

	/**
	 * 페이징 처리되는 리스트에서 글 번호 제일 처음 값을 구해오는 메소드이다.
	 * 
	 * @param param
	 *            - 페이징 정보가 담긴 파라미터 객체
	 * @param index
	 *            - jdbc오브젝트의 sqls태그 인덱스
	 * @param isASC
	 *            - true : 1부터 오름 차순 / false : 글 갯수에서 내림차순
	 * @return - for 문의 처음에 ++나 --연산자를 사용하면 바로 쓸 수 있도록 값을 조정한 처음 글번호
	 */
	public int getStartNum (final Parameter param, final boolean isASC, final int index)
	{
		int iCurrent = param.getCurrentIndex();
		final int iTotal = this.getTotalPageSize(param, index);
		if (iTotal < iCurrent)
		{
			iCurrent = iTotal;
		}
		if (iCurrent <= 0)
		{
			iCurrent = 1;
		}

		if (isASC)
		{
			return (iCurrent - 1) * param.getLinePerPage();
		}
		else
		{
			return (this.getTotalRowSize(index) - ((iCurrent - 1) * param.getLinePerPage())) + 1;
		}
	}

	/**
	 * 페이징 처리되는 쿼리에서 해당 쿼리의 페이지 수를 반환하는 메소드이다.
	 * 
	 * @param param
	 *            - 해당 쿼리를 날린 파라미터 객체
	 * @param index
	 *            - jdbc오브젝트의 sqls태그 인덱스
	 * @return - 해당 쿼리에 대한 총 페이지 수
	 */
	public int getTotalPageSize (final Parameter param, final int index)
	{
		int pagingSize = this.totalSize[index] / param.getLinePerPage();
		if ((this.totalSize[index] % param.getLinePerPage()) > 0)
		{
			pagingSize++;
		}
		return pagingSize;
	}

	/**
	 * 페이징 처리 전의 쿼리를 통해 나온 전체 레코드 수를 반환하는 메소드이다.
	 * 
	 * @param index
	 *            - jdbc오브젝트의 sqls태그 인덱스
	 * @return - 페이징 전의 전체 레코드 수
	 */
	public int getTotalRowSize (final int index)
	{
		return this.totalSize[index];
	}

	/**
	 * INSERT, UPDATE, DELETE 문이었을 경우 그 쿼리가 적용된 DB의 레코드 수를 반환하는 메소드이다.
	 * 
	 * @param index
	 *            - jdbc오브젝트의 sqls태그 인덱스
	 * @return - DB에 반영된 레코드 수
	 */
	public int getUpdateCount (final int index)
	{
		return this.updateCount[index];
	}

	@Override
	public void loggingObject ()
	{
		Constant.getLogger().info("Jdbc ID : {}에 쓰인 값들\n{}", new Object[] { this.functionID, ObjectFactory.getTransaction(this.functionID).toString() });
	}

	/**
	 * jdbc오브젝트의 sqls태그의 갯수를 반환하는 메소드이다.
	 * 
	 * @return - 해당 jdbc태그 내의 sqls태그의 갯수
	 */
	public int objectSize ()
	{
		return this.size;
	}

	@Override
	public void setRequest (final HttpServletRequest req)
	{
		req.setAttribute("transaction." + this.functionID + ".size", this.size);
		req.setAttribute("transaction." + this.functionID + ".totalSize", this.totalSize);
		req.setAttribute("transaction." + this.functionID + ".updateCount", this.updateCount);
		req.setAttribute("transaction." + this.functionID + ".dataList", this.dl);
	}
}