package chk.jsphelper.util;

import chk.jsphelper.Constant;
import chk.jsphelper.object.enums.Symbolic;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public final class JavascriptUtil
{
	public enum Control implements Symbolic
	{
		BACK ("history.back();"),
		CLOSE ("close();"),
		RELOAD ("location.reload();");

		private String typeValue = "";

		private Control (final String value)
		{
			this.typeValue = value;
		}

		public String getSymbol ()
		{
			return this.typeValue;
		}
	}

	public enum Document implements Symbolic
	{
		OPENER ("opener"),
		PARENT ("parent"),
		THIS ("self"),
		TOP ("top");

		private String typeValue = "";

		private Document (final String value)
		{
			this.typeValue = value;
		}

		public String getSymbol ()
		{
			return this.typeValue;
		}
	}

	/**
	 * 자바스크립의 alert 메시지를 출력하는 메소드이다.
	 * 
	 * @param msg
	 *            - alert을 띄울 메시지
	 * @param res
	 *            - 자바스크립트를 실행할 response객체
	 */
	public static void alert (final String msg, final HttpServletResponse res)
	{
		final String script = " alert(\"" + msg + "\"); ";
		JavascriptUtil.executeJS(script, res);
	}

	/**
	 * 자바스크립의 alert을 실행하고 다음 액션을 지정하여 실행시킬수 있는 메소드이다.<br>
	 * 메시지가 null 이면 alert는 실행이 되지 않는다.
	 * 
	 * @param msg
	 *            - alert을 띄울 메시지
	 * @param object
	 *            - alert을 띄우고 작업을 지정할 오브젝트 (THIS, PARENT, OPENER, TOP 중의 하나)
	 * @param control
	 *            - alert을 띄우고 경로를 제어할 실행 코드 (BACK, CLOSE, RELOAD)
	 * @param res
	 *            - 자바스크립트를 실행할 response객체
	 */
	public static void alertAction (final String msg, final Document object, final Control control, final HttpServletResponse res)
	{
		String script = "";
		if (msg != null)
		{
			script += " alert(\"" + msg + "\"); ";
		}
		script += object.getSymbol() + "." + control.getSymbol();
		JavascriptUtil.executeJS(script, res);
	}

	/**
	 * 자바스크립의 alert을 실행하고 다음 스크립트 함수를 지정하여 실행시킬수 있는 메소드이다.<br>
	 * 메시지가 null 이면 alert는 실행이 되지 않는다.
	 * 
	 * @param msg
	 *            - alert을 띄울 메시지
	 * @param object
	 *            - alert을 띄우고 스크립트를 실행할 오브젝트 (THIS, PARENT, OPENER, TOP 중의 하나)
	 * @param code
	 *            - 실행할 스크립트 함수코드
	 * @param res
	 *            - 자바스크립트를 실행할 response객체
	 */
	public static void alertScript (final String msg, final Document object, final String code, final HttpServletResponse res)
	{
		String script = "";
		if (msg != null)
		{
			script += " alert('" + msg + "'); ";
		}
		script += object.getSymbol() + "." + code + "; ";
		JavascriptUtil.executeJS(script, res);
	}

	/**
	 * 자바스크립의 alert을 실행하고 다음 액션을 지정하여 실행시킬수 있는 메소드이다.<br>
	 * 메시지가 null 이면 alert는 실행이 되지 않는다.
	 * 
	 * @param msg
	 *            - alert을 띄울 메시지
	 * @param object
	 *            - alert을 띄우고 경로를 변경할 오브젝트 (THIS, PARENT, OPENER, TOP 중의 하나)
	 * @param url
	 *            - 변경될 URL 경로
	 * @param res
	 *            - 자바스크립트를 실행할 response객체
	 */
	public static void alertURL (final String msg, final Document object, final String url, final HttpServletResponse res)
	{
		StringBuilder sb = new StringBuilder();
		if (msg != null)
		{
			sb.append(" alert('").append(msg).append("');\n");
		}
		if (url.startsWith("http"))
		{
			sb.append(object.getSymbol()).append(".location.href = '").append(url).append("';\n");
			Constant.getLogger().debug("자바스크립트를 통한 경로변경 : {}", new Object[] { url });
		}
		else if (url.startsWith("/"))
		{
			sb.append(object.getSymbol()).append(".location.href = '").append(url.substring(1)).append("';\n")
			Constant.getLogger().debug("자바스크립트를 통한 경로변경 : /{}{}", new Object[] { Constant.getValue("Server.Context", ""), url });
		}
		else
		{
//			sb.append(object.getSymbol()).append(".location.href = '/").append(Constant.getValue("Server.Context", "")).append(Constant.getValue("Path.JspRoot", "/WEB-INF/jsp/")).append(url).append("';\n");
			sb.append(object.getSymbol()).append(".location.href = '/WEB-INF/jsp/").append(url).append("';\n")
			Constant.getLogger().debug("자바스크립트를 통한 경로변경 : /{}{}{}", new Object[] { Constant.getValue("Server.Context", ""), Constant.getValue("Path.JspRoot", "/WEB-INF/jsp/"), url });
		}
		JavascriptUtil.executeJS(sb.toString(), res);
	}

	/**
	 * 스크립트만을 순수하게 실행시킬 수 있는 메소드이다.
	 * 
	 * @param script
	 *            - 스크립트 문자열
	 * @param res
	 *            - 자바스크립트를 실행할 response객체
	 */
	public static void executeJS (final String script, final HttpServletResponse res)
	{
		PrintWriter printWriter = null;
		try
		{
			if (res != null)
			{
				res.setContentType("text/html;charset=UTF-8");
				res.setHeader("Cache-Control", "no-cache");
				printWriter = res.getWriter();
				printWriter.println("<script type=\"text/javascript\" language=\"javascript\">\n<!--\n");
				printWriter.println(script);
				printWriter.println("//-->\n</script>");
			}
		}
		catch (final IOException ioe)
		{
			Constant.getLogger().error(ioe.getLocalizedMessage(), ioe);
		}
		finally
		{
			CloseUtil.closeObject(printWriter);
		}
	}

	private JavascriptUtil ()
	{
	}
}