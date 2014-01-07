package chk.jsphelper.service.tag;

import chk.jsphelper.Constant;
import chk.jsphelper.Parameter;
import chk.jsphelper.util.HtmlUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Iterator;

public class ParameterInput extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4710241751628111116L;
	private String keys = null;
	private Parameter param = null;

	@Override
	public int doStartTag () throws JspException
	{
		if (this.keys != null)
		{
			this.param.setFormKey(this.keys);
		}

		final StringBuilder sb = new StringBuilder();
		final String[] formKey = this.param.getFormKey();

		if (formKey == null)
		{
			final String[] currentIndexName = Constant.getValue("KeyName.CurrentIndex", "_currentIndex").split(",");

			for (String key : this.param.keySet())
			{
				if (!"JSESSIONID".equals(key))
				{
					final String[] values = this.param.getValues(key);

					for (int j = 0; j < values.length; j++)
					{
						sb.append("<input type=\"hidden\" id=\"").append(key).append((j == 0 ? "" : Integer.toString(j))).append("\" name=\"").append(key).append("\" value=\"").append(values[j]).append("\" />");
					}
				}
			}

			for (final String key : currentIndexName)
			{
				if (this.param.containsKey(key))
				{
					final String[] values = this.param.getValues(key);

					for (int j = 0; j < values.length; j++)
					{
						sb.append("<input type=\"hidden\" id=\"").append(key).append((j == 0 ? "" : Integer.toString(j))).append("\" name=\"").append(key).append("\" value=\"").append(values[j]).append("\" />");
					}
				}
			}
		}
		else
		{
			for (final String key : formKey)
			{
				final String[] values = this.param.getValues(key);

				if (values != null)
				{
					for (int j = 0; j < values.length; j++)
					{
						sb.append("<input type=\"hidden\" id=\"").append(key).append((j == 0 ? "" : Integer.toString(j))).append("\" name=\"").append(key).append("\" value=\"").append(HtmlUtil.encHTML(values[j])).append("\" />");
					}
				}
				else
				{
					sb.append("<input type=\"hidden\" id=\"").append(key).append("\" name=\"").append(key).append("\" />");
				}
			}
		}

		final JspWriter out = this.pageContext.getOut();
		try
		{
			out.print(sb.toString());
		}
		catch (final IOException ioe)
		{
		}
		return Tag.SKIP_BODY;
	}

	public void setKeys (final String keys)
	{
		this.keys = keys;
	}

	/**
	 * @param param
	 */
	public void setParam (final Parameter param)
	{
		this.param = param;
	}
}