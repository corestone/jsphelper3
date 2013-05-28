package chk.jsphelper.service.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import chk.jsphelper.Constant;

@WebFilter (filterName = "Set Character Encoding", urlPatterns = "/*", initParams = { @WebInitParam (name = "encoding", value = "UTF-8") })
public class CharsetFilter implements Filter
{
	protected String encoding = null;
	protected FilterConfig filterConfig = null;
	protected boolean ignore = true;

	public void destroy ()
	{
		this.encoding = null;
		this.filterConfig = null;
	}

	public void doFilter (final ServletRequest req, final ServletResponse res, final FilterChain fc) throws IOException, ServletException
	{
		if (this.ignore || (req.getCharacterEncoding() == null))
		{
			final String encoding = this.selectEncoding(req);
			if (encoding != null)
			{
				req.setCharacterEncoding(encoding);
				res.setCharacterEncoding(encoding);
			}
		}
		fc.doFilter(req, res);
	}

	public void init (final FilterConfig fc) throws ServletException
	{
		this.filterConfig = fc;
		this.encoding = fc.getInitParameter("encoding");
		if (this.encoding == null)
		{
			Constant.getLogger().info("필터의 인코딩이 지정되어 있지 않아 기본 인코딩으로 세팅합니다.");
			this.encoding = Constant.getValue("Encoding.JspPage", "UTF-8");
		}

		final String value = fc.getInitParameter("ignore");
		if ((value == null) || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"))
		{
			this.ignore = true;
		}
		else
		{
			this.ignore = false;
		}
	}

	protected String selectEncoding (final ServletRequest req)
	{
		return (this.encoding);
	}
}