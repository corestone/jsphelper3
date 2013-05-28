package chk.jsphelper.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chk.jsphelper.Constant;
import chk.jsphelper.ObjectFactory;
import chk.jsphelper.module.pool.ConnectionPoolManager;
import chk.jsphelper.module.pool.ThreadPool;
import chk.jsphelper.object.Servlet;

@WebServlet (name = "servlet", urlPatterns = { "/Servlet" })
public class ServletHelper extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2391336976644629157L;

	/**
	 * 
	 */
	public ServletHelper ()
	{
		super();
	}

	@Override
	public void destroy ()
	{
		ThreadPool.getInstance().destroy();
		super.destroy();
	}

	@Override
	public void init ()
	{
		final ConnectionPoolManager cpm = ConnectionPoolManager.getInstance();
		cpm.hashCode();
	}

	/**
	 * 서블릿헬퍼에서 각 지정클래스로 객체를 실행하고 cmd에 따라 메소드를 분기하는 역활을 담당한다. 이 클래스의 모든 컨트롤의 핵심이다.
	 */
	@Override
	protected void service (final HttpServletRequest req, final HttpServletResponse res)
	{
		String cls = null;
		String method = null;
		AbstractServlet servlet = null;
		try
		{
			final Servlet so = ObjectFactory.getServlet(req.getParameter("id"));
			cls = so.getClassname();
			method = so.getMethod();
			Constant.getLogger().debug("ServletID가 [{}]인 클래스 '{}'에서 메소드 '{}()'이 시작되었습니다.", new Object[] { req.getParameter("id"), cls, method });

			req.setCharacterEncoding(Constant.getValue("Encoding.JspPage", "UTF-8"));
			res.setCharacterEncoding(Constant.getValue("Encoding.JspPage", "UTF-8"));

			servlet = (AbstractServlet) Class.forName(cls).newInstance();
			servlet.process(req, res, so, this.getServletContext());
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
			this.errorJSP(e.getLocalizedMessage(), e, req, res);
		}
		finally
		{
			Constant.getLogger().debug("ServletID가 [{}]인 클래스 '{}'에서 메소드 '{}()'이 종료되었습니다.", new Object[] { req.getParameter("id"), cls, method });
		}
	}

	/**
	 * Error가 발생하였을 경우 기본적으로 화면에 디스플레이되는 화면.
	 * 
	 * @param customMsg
	 *            String
	 * @param e
	 *            Exception 예외상황이 발생하였을 경우
	 */
	private void errorJSP (final String customMsg, final Exception e, final HttpServletRequest req, final HttpServletResponse res)
	{
		try
		{
			final StringBuilder sb = new StringBuilder();
			sb.append("JSP/Servlet Error (Catched by ServletHepler) : [");
			sb.append(customMsg);
			sb.append("] Request URI : " + req.getRequestURI() + "?" + req.getQueryString());
			final String user = req.getRemoteUser();
			if (user != null)
			{
				sb.append(", User : " + user);
			}
			sb.append(", User Location : " + req.getRemoteHost() + "(" + req.getRemoteAddr() + ")");

			final PrintWriter out = res.getWriter();
			res.setContentType("text/html;charset=UTF-8");
			out.println("<!DOCTYPE html>");
			out.println("<html><head><title>Error Info</title>");
			out.println("<script>function view() {document.getElementById('errorText').toggle();}</script>");
			out.println("</head><body bgcolor=white>에러 발생. 관리자에게 문의 바랍니다<span onclick='view()'>.</span></br>");
			out.println("<xmp id='errorText'>");
			out.println(sb.toString());
			e.printStackTrace(out);
			out.println("</xmp></body></html>");
			out.flush();
			out.close();
		}
		catch (final IOException ioe)
		{
			Constant.getLogger().error(ioe.getLocalizedMessage(), ioe);
		}
	}
}