package chk.jsphelper.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chk.jsphelper.Constant;
import chk.jsphelper.engine.ext.upload.FileUploadListener;

@WebServlet (name = "upload", urlPatterns = { "/Progress" })
public class UploadProgress extends HttpServlet
{
	private static final long serialVersionUID = 6173900234730850545L;

	public UploadProgress ()
	{
		super();
	}

	@Override
	public void service (final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException
	{
		final PrintWriter out = res.getWriter();
		final HttpSession session = req.getSession();
		final StringBuilder sbJson = new StringBuilder();
		FileUploadListener listener = null;
		long bytesRead = 0, contentLength = 0;

		if (session == null)
		{
			return;
		}
		else if (session != null)
		{
			listener = (FileUploadListener) session.getAttribute("_UploadListener");

			if (listener == null)
			{
				return;
			}
			else
			{
				bytesRead = listener.getBytesRead();
				contentLength = listener.getContentLength();
			}
		}

		res.setContentType("text/plain");

		sbJson.append("{\"readBytes\" : " + bytesRead + ", ");
		sbJson.append("\"contentBytes\" : " + contentLength + ", ");

		if (listener.getErrMsg() != null)
		{
			sbJson.append("\"errorMsg\" : \"" + listener.getErrMsg() + "\", ");
			sbJson.append("\"uploading\" : false");
			session.setAttribute("_UploadListener", null);
		}
		else if (bytesRead == contentLength)
		{
			sbJson.append("\"inputName\" : " + listener.getInputName2JSON() + ", ");
			sbJson.append("\"savedName\" : " + listener.getSavedName2JSON() + ", ");
			sbJson.append("\"completePercent\" : 100, ");
			sbJson.append("\"uploading\" : false");
			session.setAttribute("_UploadListener", null);
		}
		else
		{
			final long completePercent = ((100 * bytesRead) / contentLength);
			sbJson.append("\"completePercent\" : " + completePercent + ", ");
			sbJson.append("\"uploading\" : true");
		}
		sbJson.append("}");
		Constant.getLogger().debug(sbJson.toString());

		out.println(sbJson.toString());
		out.flush();
		out.close();
	}
}