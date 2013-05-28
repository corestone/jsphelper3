package chk.jsphelper.object.enums;

/**
 * @author Corestone
 */
public enum ObjectType implements Symbolic
{
	/**
	 * 데이타베이스 커넥션 정보 오브젝트
	 */
	_DATASOURCE ("DataSource"),
	/**
	 * 엑셀 변환 정보 오브젝트
	 */
	EXCEL ("Excel"),
	/**
	 * 메일 발송 정보 오브젝트
	 */
	MAIL ("Mail"),
	/**
	 * 다국어 메시지 정보 오브젝트
	 */
	MESSAGE ("Message"),
	/**
	 * 서블릿 호출 정보 오브젝트
	 */
	SERVLET ("Servlet"),
	/**
	 * 쿼리 정보 오브젝트
	 */
	SQL ("SQL"),
	/**
	 * 쿼리 트랜잭션 정보 오브젝트
	 */
	TRANSACTION ("Transaction"),
	/**
	 * 파일 업로드 정보 오브젝트
	 */
	UPLOAD ("Upload");

	private String typeValue = "";

	private ObjectType (final String type)
	{
		this.typeValue = type;
	}

	public String getSymbol ()
	{
		return this.typeValue;
	}
}