package chk.jsphelper.service.tag;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import chk.jsphelper.Constant;
import chk.jsphelper.Parameter;
import chk.jsphelper.util.HtmlUtil;

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

			final Iterator<String> element = this.param.keySet().iterator();
			while (element.hasNext())
			{
				final String key = element.next();

				if (!"JSESSIONID".equals(key))
				{
					final String[] values = this.param.getValues(key);

					for (int j = 0; j < values.length; j++)
					{
						sb.append("<input type=\"hidden\" id=\"" + key + (j == 0 ? "" : Integer.toString(j)) + "\" name=\"" + key + "\" value=\"" + values[j] + "\" />");
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
						sb.append("<input type=\"hidden\" id=\"" + key + (j == 0 ? "" : Integer.toString(j)) + "\" name=\"" + key + "\" value=\"" + values[j] + "\" />");
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
						sb.append("<input type=\"hidden\" id=\"" + key + (j == 0 ? "" : Integer.toString(j)) + "\" name=\"" + key + "\" value=\"" + HtmlUtil.encHTML(values[j]) + "\" />");
					}
				}
				else
				{
					sb.append("<input type=\"hidden\" id=\"" + key + "\" name=\"" + key + "\" />");
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