package chk.jsphelper.service.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import chk.jsphelper.Constant;
import chk.jsphelper.Parameter;
import chk.jsphelper.util.FileUtil;
import chk.jsphelper.util.StringUtil;
import chk.jsphelper.value.UploadValue;

@WebServlet (name = "download", urlPatterns = { "/Download" })
public class DownloadServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4029171873586471479L;

	/**
	 * 
	 */
	public DownloadServlet ()
	{
		super();
	}

	@Override
	public void service (final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException
	{
		try
		{
			File file;
			BufferedInputStream bin;
			BufferedOutputStream bos;
			final String jspEnc = Constant.getValue("Encoding.JspPage", "UTF-8");
			req.setCharacterEncoding(jspEnc);
			// 업로드 오브젝트의 경로에서 가지고 올때는 그 오브젝트 아이디를 적는다.
			final String uploadID = StringUtil.trim(req.getParameter("id"));
			// 업로드 아이디가 있을 때에는 파일명만 없을 때에는 전체 경로까지 입력해 준다.
			String fileName = req.getParameter("file");
			if (fileName.indexOf("WEB-INF") > 0)
			{
				Constant.getLogger().error("파일 경로 '{}'는 다운로드가 금지된 WEB-INF 경로를 포함하므로 다운로드가 취소됩니다.", new Object[] { fileName });
				return;
			}
			final String ext = FileUtil.getExtension(fileName);
			if (StringUtils.containsIgnoreCase(ext, "jsp"))
			{
				Constant.getLogger().error("파일명 '{}'는 다운로드가 금지된 확장자(jsp)이므로 다운로드가 취소됩니다.", new Object[] { fileName });
				return;
			}
			// 다운로드 되는 파일명을 변경하고 싶을 때에는 이 값을 세팅하면 된다.
			final String saveName = StringUtil.trimDefault(req.getParameter("name"), fileName);
			Constant.getLogger().info("{}파일을 파일명 {}로 다운로드 하려고 합니다.", new Object[] { fileName, saveName });
			if (!uploadID.equals(""))
			{
				fileName = StringUtil.trim(UploadValue.getUploadPath(uploadID, new Parameter(req))) + fileName;
			}
			file = new File(fileName);
			if (file.exists())
			{
				res.reset();
				res.setContentType("application/octet-stream");
				res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(saveName, "UTF-8"));
				res.setHeader("Content-Length", file.length() + ";");
				res.setHeader("Pragma", "no-cache;");
				res.setHeader("Expires", "-1;");
				bin = new BufferedInputStream(new FileInputStream(file));
				bos = new BufferedOutputStream(res.getOutputStream());
				final byte buf[] = new byte[2048];
				for (int read = 0; (read = bin.read(buf)) != -1;)
				{
					bos.write(buf, 0, read);
				}
				bos.close();
				bin.close();
			}
			else
			{
				Constant.getLogger().warn("{} 파일이 존재하지 않습니다", fileName);
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error("파일 다운로드에서 {} 예외가 발생 : {}", new Object[] { e.getClass().getName(), e.getLocalizedMessage() }, e);
		}
	}
}