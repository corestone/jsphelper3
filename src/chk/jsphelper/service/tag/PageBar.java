package chk.jsphelper.service.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import chk.jsphelper.Constant;
import chk.jsphelper.Parameter;

public class PageBar extends TagSupport
{
	private static final long serialVersionUID = -5889751199080835131L;
	private boolean comboBox = false;
	private int configIndex = -1;
	private String imageSuffix = "";
	private boolean minusPlus = true;
	private boolean noLinkShow = true;
	private Parameter param;
	private boolean pervNextBlock = true;
	private boolean startLast = true;
	private int total;

	@Override
	public int doStartTag () throws JspException
	{
		if (this.configIndex == -1)
		{
			this.configIndex = this.param.getPagingConfigIndex();
		}
		int index = this.param.getCurrentIndex();
		final int size = this.param.getPagebarSize();
		final StringBuilder sb = new StringBuilder("");

		if ((size > 0) && (this.total > 0))
		{
			// 첫 페이지 링크
			if (index > this.total)
			{
				index = this.total;
			}
			if (index < 1)
			{
				index = 1;
			}
			int start = ((index / size) * size) + 1;
			if ((index % size) == 0)
			{
				start = ((index / size) * size) - (size - 1);
			}
			int end = (start + size) - 1;
			if (end > this.total)
			{
				end = this.total;
			}

			final String indexCurrentParam = Constant.getValue("KeyName.CurrentIndex", "_currentIndex", this.configIndex);
			String valueStart = "";
			String valueEnd = "";
			if (this.comboBox)
			{
				valueStart = "jQuery('#" + indexCurrentParam + " > option[@value='";
				valueEnd = "']').attr('selected', 'true');";
			}
			else
			{
				valueStart = "jQuery('#" + indexCurrentParam + "').val('";
				valueEnd = "');";
				sb.append("<input type=\"hidden\" id=\"" + indexCurrentParam + "\" name=\"" + indexCurrentParam + "\" value=\"" + index + "\" />");
			}

			// 첫 페이지 링크
			if (this.startLast)
			{
				if (index > 1)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_first_page" + this.imageSuffix + ".gif\" alt=\"First Page\" align=\"absmiddle\" onclick=\"" + valueStart + "1" + valueEnd + "_goPage('" + this.configIndex + "');\" class=\"pageBarImg hand\" /> ");
				}
				else if (this.noLinkShow)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_first_page_" + this.imageSuffix + ".gif\" alt=\"First Page\" align=\"absmiddle\" class=\"pageBarImg\" /> ");
				}
			}
			// 이전블력 페이지 링크
			if (this.pervNextBlock)
			{
				if ((index - size) > 0)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_prev_block" + this.imageSuffix + ".gif\" alt=\"Prev Block\" align=\"absmiddle\" onclick=\"" + valueStart + (start - 1) + valueEnd + "_goPage('" + this.configIndex + "');\" class=\"pageBarImg hand\" /> ");
				}
				else if (this.noLinkShow)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_prev_block_" + this.imageSuffix + ".gif\" alt=\"Prev Block\" align=\"absmiddle\" class=\"pageBarImg\" /> ");
				}
			}
			// 이전 페이지 링크
			if (this.minusPlus)
			{
				if (index > 1)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_prev_page" + this.imageSuffix + ".gif\" alt=\"Prev Page\" align=\"absmiddle\" onclick=\"" + valueStart + (index - 1) + valueEnd + "_goPage('" + this.configIndex + "');\" class=\"pageBarImg hand\" /> ");
				}
				else if (this.noLinkShow)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_prev_page_" + this.imageSuffix + ".gif\" alt=\"Prev Page\" align=\"absmiddle\" class=\"pageBarImg\" /> ");
				}
			}
			// 페이지 리스트 링크
			for (int i = start; i <= end; i++)
			{
				if (i < 10)
				{
					sb.append(i != index ? " <span onclick=\"" + valueStart + i + valueEnd + "_goPage('" + this.configIndex + "');\" class=\"pageBarNum" + this.imageSuffix + " hand\">[ " + i + " ]</span> " : " <span class=\"pageBarThis" + this.imageSuffix + "\" style=\"font-weight:bold;\"> " + i + " </span> ");
				}
				else
				{
					sb.append(i != index ? " <span onclick=\"" + valueStart + i + valueEnd + "_goPage('" + this.configIndex + "');\" class=\"pageBarNum" + this.imageSuffix + " hand\">[" + i + "]</span> " : " <span class=\"pageBarThis" + this.imageSuffix + "\" style=\"font-weight:bold;\">" + i + "</span> ");
				}
			}
			// 다음 페이지 링크
			if (this.minusPlus)
			{
				if (index < this.total)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_next_page" + this.imageSuffix + ".gif\" alt=\"Next Page\" align=\"absmiddle\" onclick=\"" + valueStart + (index + 1) + valueEnd + "_goPage('" + this.configIndex + "');\" class=\"pageBarImg hand\" /> ");
				}
				else if (this.noLinkShow)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_next_page_" + this.imageSuffix + ".gif\" alt=\"Next Page\" align=\"absmiddle\" class=\"pageBarImg\" /> ");
				}
			}
			// 다음블력 페이지 링크
			if (this.pervNextBlock)
			{
				if ((start + size) <= this.total)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_next_block" + this.imageSuffix + ".gif\" alt=\"Next Block\" align=\"absmiddle\" onclick=\"" + valueStart + (start + size) + valueEnd + "_goPage('" + this.configIndex + "');\" class=\"pageBarImg hand\" /> ");
				}
				else if (this.noLinkShow)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_next_block_" + this.imageSuffix + ".gif\" alt=\"Next Block\" align=\"absmiddle\" class=\"pageBarImg\" /> ");
				}
			}
			// 끝 페이지 페이지 링크
			if (this.startLast)
			{
				if (index < this.total)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_last_page" + this.imageSuffix + ".gif\" alt=\"Last Page\" align=\"absmiddle\" onclick=\"" + valueStart + this.total + valueEnd + "_goPage('" + this.configIndex + "');\" class=\"pageBarImg hand\" /> ");
				}
				else if (this.noLinkShow)
				{
					sb.append(" <img src=\"" + Constant.getValue("Path.Client", "/jsphelper/") + "img/_btn_last_page_" + this.imageSuffix + ".gif\" alt=\"Last Page\" align=\"absmiddle\" class=\"pageBarImg\" /> ");
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

	public void setCombobox (final boolean combobox)
	{
		this.comboBox = combobox;
	}

	/**
	 * @param configIndex
	 */
	public void setConfigIndex (final int configIndex)
	{
		this.configIndex = configIndex;
	}

	/**
	 * @param imageSuffix
	 */
	public void setImageSuffix (final String imageSuffix)
	{
		if (imageSuffix != null)
		{
			this.imageSuffix = imageSuffix;
		}
	}

	/**
	 * @param minusPlus
	 */
	public void setMinusPlus (final boolean minusPlus)
	{
		this.minusPlus = minusPlus;
	}

	/**
	 * @param noLinkShow
	 */
	public void setNoLinkShow (final boolean noLinkShow)
	{
		this.noLinkShow = noLinkShow;
	}

	/**
	 * @param param
	 */
	public void setParam (final Parameter param)
	{
		this.param = param;
	}

	/**
	 * @param pervNextBlock
	 */
	public void setPervNextBlock (final boolean pervNextBlock)
	{
		this.pervNextBlock = pervNextBlock;
	}

	/**
	 * @param startLast
	 */
	public void setStartLast (final boolean startLast)
	{
		this.startLast = startLast;
	}

	/**
	 * @param total
	 */
	public void setTotal (final int total)
	{
		this.total = total;
	}
}