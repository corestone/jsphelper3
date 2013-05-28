package chk.jsphelper.value;

import javax.servlet.http.HttpServletRequest;

import chk.jsphelper.object.ServiceObject;

/**
 * 기능별 Value클래스들(ChartValue, ExcelValue, JdbcValue, SqlValue, UploadValue)의 상위 클래스이다.
 * 
 * @author Corestone H. Kang
 */
abstract public class AbstractValue
{
	// 기능오브젝트 이름
	protected String functionID;
	// 해당 기능에 의한 ValueObject 를 생성하는 ServiceFactory의 수행 성공 여부
	protected boolean success = false;

	/**
	 * 생성자
	 * 
	 * @param functionID
	 *            - 함수 오브젝트 아이디
	 */
	public AbstractValue (final String functionID)
	{
		this.functionID = functionID;
	}

	/**
	 * 해당 프로세스가 작동되는데 사용된 오브젝트를 반환하는 메소드이다.<br>
	 * 디버깅 용으로 사용될 수 있다.
	 * 
	 * @return - 오브젝트
	 */
	abstract public ServiceObject getObject ();

	/**
	 * 해당 프로세스가 성공을 했는지 여부를 체크하는 메소드이다.
	 * 
	 * @return - true : 성공 / false : 실패
	 */
	public final boolean isSuccess ()
	{
		return this.success;
	}

	/**
	 * 해당 서비스 함수의 오브젝트 내용을 로그에 찍는 메소드이다.
	 */
	abstract public void loggingObject ();

	/**
	 * 해당 프로세스에서 나온 값을 Request의 Attribute에 입력하는 메소드이다.
	 * 
	 * @param req
	 *            - 값을 입력할 Request
	 */
	abstract public void setRequest (HttpServletRequest req);
}