package chk.jsphelper;

import chk.jsphelper.module.wrapper.ConnWrapper;
import chk.jsphelper.object.enums.ObjectType;
import chk.jsphelper.service.controller.ExcelController;
import chk.jsphelper.service.controller.MailController;
import chk.jsphelper.service.controller.MessageController;
import chk.jsphelper.service.controller.SqlController;
import chk.jsphelper.service.controller.TransactionController;
import chk.jsphelper.service.controller.UploadController;
import chk.jsphelper.value.ExcelValue;
import chk.jsphelper.value.SqlValue;
import chk.jsphelper.value.TransactionValue;
import chk.jsphelper.value.UploadValue;

/**
 * @author Corestone
 */
public class ServiceCaller
{
	/**
	 * 
	 */
	public ServiceCaller ()
	{

	}

	/**
	 * 엑셀을 디비에 인서트내지 RecordSet으로 받던지, 디비의 자료를 엑셀로 받는 작업을 수행하는 메소드이다.<br>
	 * 
	 * @param functionID
	 *            - 엑셀오브젝트 아이디
	 * @param param
	 *            - 마라미터 객체
	 * @return - 엑셀의 정보를 가지고 있는 ExcelValue 오브젝트
	 */
	public final ExcelValue executeExcel (final String functionID, final Parameter param)
	{
		if (this.checkFunctionID(ObjectType.EXCEL, functionID) && this.checkParameter(param))
		{
			return null;
		}
		final ExcelController ec = new ExcelController();
		return ec.executeExcel(functionID, param);
	}

	/**
	 * SQL 개별 단위로 실행하는 메소드이다.<br>
	 * 
	 * @param functionID
	 *            - SQL 오브젝트 아이디
	 * @param param
	 *            - 파라미터
	 * @return - SQL수행 결과값을 가지고 있는 SqlValue 오브젝트
	 */
	public final SqlValue executeSql (final String functionID, final Parameter param)
	{
		if (this.checkFunctionID(ObjectType.SQL, functionID) && this.checkParameter(param))
		{
			return null;
		}
		final SqlController sc = new SqlController();
		return sc.executeSql(functionID, param);
	}

	/**
	 * SQL 개별 단위로 실행하는 메소드이다.<br>
	 * 
	 * @param functionID
	 *            - SQL 오브젝트 아이디
	 * @param param
	 *            - 파라미터
	 * @param conn
	 *            - 커넥션
	 * @return - SQL수행 결과값을 가지고 있는 SqlValue 오브젝트
	 */
	public final SqlValue executeSql (final String functionID, final Parameter param, final ConnWrapper conn)
	{
		if (this.checkFunctionID(ObjectType.SQL, functionID) && this.checkParameter(param))
		{
			return null;
		}
		final SqlController sc = new SqlController();
		return sc.executeSql(functionID, param, conn);
	}

	/**
	 * Jdbc를 통해 SQL의 묶음을 실행하는 메소드이다.<br>
	 * 자체적인 jdbc태그의 datasource를 통해 커넥션풀의 커넥션을 이용해서 SQL를 실행한다.<br>
	 * 
	 * @param functionID
	 *            - Jdbc오브젝트 아이디
	 * @param param
	 *            - 파라미터
	 * @return - SQL수행 결과값을 가지고 있는 JdbcValue 오브젝트
	 */
	public final TransactionValue executeTransaction (final String functionID, final Parameter param)
	{
		if (this.checkFunctionID(ObjectType.TRANSACTION, functionID) && this.checkParameter(param))
		{
			return null;
		}
		final TransactionController tc = new TransactionController();
		return tc.executeTransaction(functionID, param);
	}

	/**
	 * Jdbc를 통해 SQL의 묶음을 실행하는 메소드이다.<br>
	 * 커넥션을 외부에서 미리 생성하여 작업한다. 보통 ProcessFactory.getConnection() 메소드로 커넥션을 만든다.<br>
	 * 
	 * @param functionID
	 *            - Jdbc오브젝트 아이디
	 * @param param
	 *            - 파라미터
	 * @param conn
	 *            - SQL을 실행할 커넥션 객체
	 * @return - SQL수행 결과값을 가지고 있는 JdbcValue 오브젝트
	 */
	public final TransactionValue executeTransaction (final String functionID, final Parameter param, final ConnWrapper conn)
	{
		if (this.checkFunctionID(ObjectType.TRANSACTION, functionID) && this.checkParameter(param))
		{
			return null;
		}
		final TransactionController tc = new TransactionController();
		return tc.executeTransaction(functionID, param, conn);
	}

	/**
	 * 파일 업로드을 실행하는 메소드이다.<br>
	 * form의 encording이 multipart/form-data이기 때문에 request에 대해서 param이 세팅되지 않기 때문에 반드시 이 메소드를 통해 먼저 업로드를 하면 자동적으로 Parameter에 값들이 세팅이 된다.
	 * 
	 * @param functionID
	 *            - 업로드 오브젝트 아이디
	 * @param param
	 *            - 파라미터
	 * @return - 업로드된 결과를 파악할 수 있는 UploadValue 오브젝트
	 */
	public final UploadValue executeUpload (final String functionID, final Parameter param)
	{
		if (this.checkFunctionID(ObjectType.MESSAGE, functionID) && this.checkParameter(param))
		{
			return null;
		}
		final UploadController uc = new UploadController();
		return uc.executeUpload(functionID, param);
	}

	/**
	 * 메시지를 브라우저의 프로퍼티의 언어설정에 따라 리턴해 주는 메소드이다.
	 * 
	 * @param functionID
	 *            - 메시지 오브젝트 아이디
	 * @return - 메시지 값의 문자열
	 */
	public final String getMessage (final String functionID)
	{
		if (this.checkFunctionID(ObjectType.MESSAGE, functionID))
		{
			return null;
		}
		final MessageController mc = new MessageController();
		return mc.getMessage(functionID);
	}

	/**
	 * 메시지를 브라우저의 언어에 따라 리턴해 주는 메소드이다.<br>
	 * 
	 * @param functionID
	 *            - 메시지 오브젝트 아이디
	 * @param param
	 *            - 파라미터
	 * @return - 메시지 값의 문자열
	 */
	public final String getMessage (final String functionID, final Parameter param)
	{
		if (this.checkFunctionID(ObjectType.MESSAGE, functionID) && this.checkParameter(param))
		{
			return null;
		}
		final MessageController mc = new MessageController();
		return mc.getMessage(functionID, param);
	}

	/**
	 * 메시지를 언어코드에 따라 반환하는 메소드이다.<br>
	 * 
	 * @param functionID
	 *            - 메시지 오브젝트 아이디
	 * @param param
	 *            - 파라미터
	 * @param language
	 *            - 언어 코드
	 * @return - 메시지 값의 문자열
	 */
	public final String getMessage (final String functionID, final Parameter param, final String language)
	{
		if (this.checkFunctionID(ObjectType.MESSAGE, functionID) && this.checkParameter(param))
		{
			return null;
		}
		final MessageController mc = new MessageController();
		return mc.getMessage(functionID, param, language);
	}

	/**
	 * 메일을 발송하는 메소드이다.<br>
	 * 
	 * @param functionID
	 *            - 메일 오브젝트 아이디
	 * @param param
	 *            - 파라미터
	 * @return - 메일 발송 성공 여부
	 */
	public final boolean sendMail (final String functionID, final Parameter param)
	{
		if (this.checkFunctionID(ObjectType.MAIL, functionID) && this.checkParameter(param))
		{
			return false;
		}
		final MailController mc = new MailController();
		return mc.sendMail(functionID, param);
	}

	/**
	 * 본 클래스에 있는 호출 메소드의 인자값 중에서 functionID을 체크하는 내부메소드이다.
	 * 
	 * @param type
	 *            - 오브젝트 Type 종류
	 * @param functionID
	 *            - 서비스함수 아이디
	 * @return - 인자값 중에 문제가 있다면 true 아니면 false;
	 */
	private boolean checkFunctionID (final ObjectType type, final String functionID)
	{
		if (ObjectFactory.existsID(type, functionID))
		{
			return false;
		}
		Constant.getLogger().error("[{}] 오브젝트에서 id='{}'에 해당되는 오브젝트가 존재하지 않습니다.", new Object[] { type.getSymbol(), functionID });
		return true;
	}

	/**
	 * 본 클래스에 있는 호출 메소드의 인자값 중에서 param을 체크하는 내부메소드이다.
	 * 
	 * @param param
	 *            - 파라미터 클래스
	 * @return - 인자값 중에 문제가 있다면 true 아니면 false;
	 */
	private boolean checkParameter (final Parameter param)
	{
		if (param == null)
		{
			Constant.getLogger().error("파라미터는 null이 될 수 없습니다.");
			return true;
		}
		return false;
	}
}