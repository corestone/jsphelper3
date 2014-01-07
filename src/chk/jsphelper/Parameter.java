package chk.jsphelper;

import chk.jsphelper.module.wrapper.MapStringsAdapter;
import chk.jsphelper.util.EncryptUtil;
import chk.jsphelper.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;

/**
 * 이 클래스는 HashMap<String, String[]>을 상속받은 클래스로 프레임웤에서 변수를 주고 받을 수 있는 벨류오브젝트이다. <br>
 * 기본적으로 jsp나 servlet에서 자동으로 리퀘스트와 세션의 값을 담게 세팅되어 있으며 나머지 필요한 값들을 담아 프레임웤의 기능을 사용할 수 있도록 설계되었다. <br>
 * 기본적으로 키는 String형이며 Value는 String 또는 String[]이다. <br>
 * 또한 키값으로 '_'로 시작되는 것은 프레임웤에서 특수한 목적으로 쓰이는 키값들이다.
 * 
 * @author Corestone H. Kang
 * @version 1.0
 * @since 1.0
 */
public class Parameter extends MapStringsAdapter
{
	/**
	 * 
	 */
	private static String KEY_NAME_LINE_PAGE;
	/**
	 * 
	 */
	private static String KEY_NAME_PAGEBAR_SIZE;

	static
	{
		Parameter.KEY_NAME_LINE_PAGE = Constant.getValue("KeyName.LinePerPage", "_linePerPage");
		Parameter.KEY_NAME_PAGEBAR_SIZE = Constant.getValue("KeyName.PagebarSize", "_pagebarSize");
	}

	/**
	 * 기본적인 파라미터 클래스를 반환하는 팩토리 메소드이다.
	 * 
	 * @return 기본적인 파라미터 클래스
	 */
	public static Parameter getInstance ()
	{
		return new Parameter();
	}

	/**
	 * 
	 */
	private String[] formKey;
	/**
	 * 
	 */
	private String keyNameCurrentIndex;
	/**
	 * 
	 */
	private int pagingConfigIndex = -1;
	/**
	 * 
	 */
	private HttpServletRequest request;

	/**
	 * 인자로 Request를 받는 생성자이다. 프레임웍에서 기본적으로 사용하는 방법이다. Request의 파라미터들과 쿠키, 세션들을 자동으로 파라미터에 세팅시켜 준다.
	 * 
	 * @param req
	 *            - 파라미터에 세팅할 값이 들어있는 Request
	 */
	public Parameter (final HttpServletRequest req)
	{
		super();
		this.request = req;
		// 파라미터 입력
		final String[] sessionKey = Constant.getValue("Mapping.Session", "").split(",");
		final Enumeration<?> element = req.getParameterNames();
		while (element.hasMoreElements())
		{
			final String key = (String) element.nextElement();
			if (ArrayUtils.contains(sessionKey, key))
			{
				Constant.getLogger().warn("HttpServletRequest의 파라미터의 키가 세션의 키 '{}가 중복됨으로 보안을 위해 Parameter 에 담지 않습니다.", key);
				continue;
			}
			if (key.indexOf("_") == 0)
			{
				this.underbarPut(key, req.getParameterValues(key)[0]);
			}
			else
			{
				this.put(key, req.getParameterValues(key));
			}
		}
		this.underbarPut("_jspURI", req.getRequestURI());
		// 쿠키 입력
		if (req.getCookies() != null)
		{
			try
			{
				for (final Cookie cookie : req.getCookies())
				{
					if (!cookie.getName().equals("JSESSIONID"))
					{
						if (cookie.getName().indexOf("_") == 0)
						{
							this.underbarPut(cookie.getName(), URLDecoder.decode(cookie.getValue(), Constant.getValue("Encoding.JspPage", "UTF-8")));
						}
						else
						{
							this.put(cookie.getName(), URLDecoder.decode(cookie.getValue(), Constant.getValue("Encoding.JspPage", "UTF-8")));
						}
					}
				}
			}
			catch (final UnsupportedEncodingException uee)
			{
				Constant.getLogger().error("쿠키의 값을 파라미터에 저장하는데 UnsupportedEncodingException 예외가 발생하였습니다.", uee);
			}
		}
		// 암호화된 퀴리스트링 입력
		if (Constant.getValue("QueryString.Encrypt", "false").equals("true"))
		{
			try
			{
				this.decQueryString(req.getQueryString());
			}
			catch (final Exception e)
			{
				Constant.getLogger().error(e.getLocalizedMessage(), e);
			}
		}
		// 세션 입력
		for (final String key : sessionKey)
		{
			if (req.getSession().getAttribute(key) != null)
			{
				this.put(key, (String) req.getSession().getAttribute(key));
			}
		}
		this.setPaging(0);
	}

	/**
	 * 
	 */
	private Parameter ()
	{
		super();
	}

	/**
	 * 페이징 처리을 완료한 후 현재 페이지 값을 가지고 오는 메소드이다.
	 * 
	 * @return - 현재 페이지
	 */
	public final int getCurrentIndex ()
	{
		if (this.m.containsKey(this.keyNameCurrentIndex))
		{
			return Integer.parseInt(this.getValue(this.keyNameCurrentIndex));
		}
		return -1;
	}

	/**
	 * 현재 페이지를 담고 있는 input태그의 name속성을 당당하는 값을 반환하는 메소드이다.
	 * 
	 * @return 현재페이지병수를 담고 있는 폼의 명칭
	 */
	public final String getCurrentIndexName ()
	{
		return this.keyNameCurrentIndex;
	}

	/**
	 * 파라미터의 setFormKey메소드를 통해 특정 폼의 키만 세팅시킨 폼키의 키 문자열 배열을 반환하는 메소드이다.
	 * 
	 * @return - 파라미터 키 문자열 배열
	 */
	public final String[] getFormKey ()
	{
		return this.formKey;
	}

	/**
	 * 한페이지에서 보여지는 데이타의 라인 수를 반환하는 메소드이다.
	 * 
	 * @return 페이지당 라인수
	 */
	public final int getLinePerPage ()
	{
		final String lineValue = Constant.getValue("Value.LinePerPage", "20", this.pagingConfigIndex);
		if (this.m.containsKey(Parameter.KEY_NAME_LINE_PAGE))
		{
			return Integer.parseInt(this.getValue(Parameter.KEY_NAME_LINE_PAGE));
		}
		return Integer.parseInt(this.getValue(lineValue));
	}

	/**
	 * 페이지바에서 한번에 보여질 페이지 단위의 묶음크기를 반환하여 주는 메소드이다.
	 * 
	 * @return 페이지바의 크기
	 */
	public final int getPagebarSize ()
	{
		final String barValue = Constant.getValue("Value.PagebarSize", "10", this.pagingConfigIndex);
		if (this.m.containsKey(Parameter.KEY_NAME_PAGEBAR_SIZE))
		{
			return Integer.parseInt(this.getValue(Parameter.KEY_NAME_PAGEBAR_SIZE));
		}
		return Integer.parseInt(this.getValue(barValue));
	}

	/**
	 * 파라미터에서 사용되는 페이지 값의 인덱스 번호(0부터 시작)를 반환하는 메소드이다.
	 * 
	 * @return 페이지 인덱스 번호
	 */
	public final int getPagingConfigIndex ()
	{
		return this.pagingConfigIndex;
	}

	/**
	 * 파라미터을 Request을 가지고 생성할 때 해당 Request를 반환하는 메소드로 반환할 수 있다.
	 * 
	 * @return 파라미터를 생성할 때 사용된 Request
	 */
	public final HttpServletRequest getRequest ()
	{
		return this.request;
	}

	/**
	 * 파라미터를 Get방식으로 풀어서 사용할 때 쓰이는 메소드이다. 단 시스템에서 사용되는 '_'로 시작되는 키는 세팅되지 않는다.<br>
	 * key1=value1&key2=value2형식으로 풀어주어 파라미터의 값을 다른 페이지로 넘길 수 있게 해 준다.<br>
	 * 또한 넘길 폼키값이 세팅되어 있다면 그 값만을 넘길 수 있다.
	 * 
	 * @return get방식으로 파라미터를 넘길 수 있는 문자열
	 */
	public final String getString ()
	{
		final StringBuilder sb = new StringBuilder();
		String retValue;
		if (this.formKey == null)
		{
			for (Map.Entry<String, String[]> entry : this.m.entrySet())
			{
				if (!entry.getKey().contains("_"))
				{
					for (final String value : entry.getValue())
					{
						try
						{
							sb.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(value, "UTF-8"));
						}
						catch (final UnsupportedEncodingException e)
						{
							e.printStackTrace();
						}
					}
				}
			}

			final String[] currentIndexName = Constant.getValue("KeyName.CurrentIndex", "_currentIndex").split(",");
			for (final String indexName : currentIndexName)
			{
				if (this.m.containsKey(indexName))
				{
					for (final String value : this.m.get(indexName))
					{
						try
						{
							sb.append("&").append(indexName).append("=").append(URLEncoder.encode(value, "UTF-8"));
						}
						catch (final UnsupportedEncodingException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
		else
		{
			for (final String key : this.formKey)
			{
				final String[] values = this.m.get(key);

				if (values != null)
				{
					for (final String val : values)
					{
						try
						{
							sb.append("&").append(key).append("=").append(URLEncoder.encode(val, "UTF-8"));
						}
						catch (final UnsupportedEncodingException e)
						{
							e.printStackTrace();
						}
					}
				}
				else
				{
					sb.append("&").append(key).append("=");
				}
			}
		}
		retValue = sb.substring(1);
		if (Constant.getValue("QueryString.Encrypt", "false").equals("true"))
		{
			try
			{
				retValue = EncryptUtil.encrypt(retValue);
			}
			catch (final Exception e)
			{
				Constant.getLogger().error("쿼리스트링 암호화를 복호화하는 중 에레가 발생하였습니다");
			}
		}
		return retValue;
	}

	/**
	 * 파라미터를 Get방식으로 풀어서 사용할 때 쓰이는 메소드이다. 단 시스템에서 사용되는 '_'로 시작되는 키는 세팅되지 않는다.<br>
	 * key1=value1&key2=value2형식으로 풀어주어 파라미터의 값을 다른 페이지로 넘길 수 있게 해 준다.<br>
	 * 폼의 키값을 같이 세팅하면서 해당키값으로 문자열을 만드는 setFormKey() 메소드도 같이 실행하는 효과를 지닌다.
	 * 
	 * @param key
	 *            - 키값 리스트를 ","로 구분한 문자열
	 * @return get방식으로 파라미터를 넘길 수 있는 문자열
	 */
	public final String getString (final String key)
	{
		this.setFormKey(key);
		return this.getString();
	}

	/**
	 * 키를 가지고 해당 값을 가지고 오는 메소드이다. 만약 키에 배열로 된 여러 값들이 있다면 첫번째 값을 가지고 온다.
	 * 
	 * @param key
	 *            - 값을 가지고 올 키값
	 * @return 해당 키의 값(만약 여러값이 있는 배열이면 첫번째 값)
	 */
	public final String getValue (final String key)
	{
		final String[] value = this.m.get(key);
		if (value == null)
		{
			Constant.getLogger().warn("[{}]에 해당되는 Parameter의 키가 존재하지 않아서 null을 반환합니다.", key);
			return null;
		}
		else if (value.length >= 1)
		{
			return value[0];
		}
		else
		{
			return null;
		}
	}

	/**
	 * 키를 가지고 해당 값의 배열을 가지고 오는 메소드이다. 만약 키에 하나의 값만이 들어 있어도 크가가 1인 배열을 가지고 온다. 만약 값이 없는 키이면 String[0]를 반환한다.
	 * 
	 * @param key
	 *            - 값을 가지고 올 키값
	 * @return 해당 키의 배열
	 */
	public final String[] getValues (final String key)
	{
		if (this.m.containsKey(key))
		{
			return this.m.get(key);
		}
		Constant.getLogger().warn("[{}]에 해당되는 Parameter의 키가 존재하지 않아서 String[0]을 반환합니다.", key);
		return new String[0];
	}

	/**
	 * 파라미터의 값을 배열의 형태라면 콤마(',')로 구분된 문자열로 넘겨 받는다.
	 * 
	 * @param key
	 *            - 파라미터의 키값
	 * @return ','로 구분된 문자열 값
	 */
	public final String getValuesByComma (final String key)
	{
		return StringUtils.join(this.m.get(key), ",");
	}

	/**
	 * 파라미터의 모든 값들을 로그에 출력해 주는 메소드이다. 주로 디버깅용으로 사용된다.
	 */
	public final void printLog ()
	{
		final StringBuilder sb = new StringBuilder("\n");
		sb.append("**************************** DUMP PARAMETERS ****************************\n");
		sb.append(this.toString()).append("\n");
		sb.append("*************************************************************************\n");
		Constant.getLogger().debug(sb.toString());
	}

	/**
	 * 파라미터에 값을 넣어주는 메소드이다. '_' 로 시작되는 키는 시스템에서 예약된 키이므로 키 이름으로 사용하지 말아야 한다.
	 * 
	 * @param key
	 *            - 키 이름 ('_'로 시작되는 이름은 시스템용이므로 사용하지 말 것)
	 * @param value
	 *            - 키에 해당하는 문자열 값
	 * @return - 입력된 키에 해당되는 문자열배열 객체
	 */
	public final String[] put (final String key, final String value)
	{
		if ((key.indexOf("_") == 0))
		{
			Constant.getLogger().error("{}={}는 _로 시작되는 파라미터 키로 시스템에서 사용되는 키이기 때문에 입력이 되지 않았습니다.", new Object[] { key, value });
			return new String[] { value };
		}
		final String[] arr = { value };
		return this.m.put(key, arr);
	}

	/**
	 * 파라미터에 입력되어 있는 값이 없을 때 기본 값을 넣어주는 메소드이다.
	 * 
	 * @param key
	 *            - 파라미터의 키값
	 * @param defaultString
	 *            - 해당 키의 값이 없을 때 기본적으로 입력될 값
	 * @return - 입력된 값
	 */
	public final String[] putDefault (final String key, final String defaultString)
	{
		if (!this.m.containsKey(key))
		{
			return this.put(key, defaultString);
		}
		else
		{
			return this.m.get(key);
		}
	}

	/**
	 * 특정키의 값들에서 어떤 문자열을 변경하고 싶을 때 사용된다.
	 * 
	 * @param key
	 *            - 변경할 파라미터의 키값
	 * @param srcStr
	 *            - 변경하기 원하는 대상 문자열
	 * @param rplStr
	 *            - 변경되는 결과 문자열
	 */
	public final void replaceValue (final String key, final String srcStr, final String rplStr)
	{
		final String[] arrParam = this.m.get(key);
		for (int i = 0, z = arrParam.length; i < z; i++)
		{
			String strParam = arrParam[i];
			strParam = strParam.replace(srcStr, rplStr);
			arrParam[i] = strParam;
		}
		this.m.put(key, arrParam);
	}

	/**
	 * getString() 메소드에서 특정 키만을 가지고 get방식으로 만들 때 그 특정 키값을 세팅하는 메소드이다. ','를 구분자로 하여 키값을 입력해 놓으면 해당 키를 배열로 저장한다.
	 * 
	 * @param key
	 *            - ','를 구분자로 파라미터의 키값을 나열한 문자열
	 */
	public final void setFormKey (final String key)
	{
		final String[] keys = key.split(",");
		if ((keys == null) || (keys[0].equals("")))
		{
			return;
		}
		this.formKey = keys;
	}

	/**
	 * 페이징 처리를 위한 세팅을 하는 메소드이다. 해당 클래스에 각각의 설정을 함으로써 페이징 처리를 자동으로 해주게 된다.
	 * 
	 * @param configIndex
	 *            - 페이징 처리의 설정인덱스 값
	 */
	public final void setPaging (final int configIndex)
	{
		this.pagingConfigIndex = configIndex;
		this.keyNameCurrentIndex = Constant.getValue("KeyName.CurrentIndex", "_currentIndex", this.pagingConfigIndex);
		final String indexValue = StringUtil.trimDefault(this.request.getParameter(this.keyNameCurrentIndex), "1");
		final String lineValue = Constant.getValue("Value.LinePerPage", "20", this.pagingConfigIndex);
		final String barValue = Constant.getValue("Value.PagebarSize", "10", this.pagingConfigIndex);

		this.underbarPut(this.keyNameCurrentIndex, indexValue);
		this.underbarPut(Parameter.KEY_NAME_LINE_PAGE, lineValue);
		this.underbarPut(Parameter.KEY_NAME_PAGEBAR_SIZE, barValue);
	}

	/**
	 * 이 프레임웤 시스템에서 사용되는 '_'로 시작되는 파라미터 키 값의 값들을 집어 넣는 메소드이다. 이는 put과는 다르게 '_'로 시작되지 않는 키값은 사용하면 안된다.
	 * 
	 * @param key
	 *            - '_'로 시작되는 키 이름
	 * @param value
	 *            - 키에 해당되는 값 문자열
	 */
	public final void underbarPut (final String key, final String value)
	{
		if ((key.indexOf("_") != 0))
		{
			Constant.getLogger().error("{}={}는 _로 시작되지 않는 일반 파라미터 키이기 때문에 입력이 되지 않았습니다.", new Object[] { key, value });
			return;
		}
		final String[] arr = { value };
		this.m.put(key, arr);
	}

	/**
	 * 암호화된 get방식의 request 문자열을 복호화하여 파라미터에 담아내는 메소드이다.
	 * 
	 * @param sValue
	 *            - 암호화된 request 문자열
	 * @throws Exception
	 *             - 문자열을 복호화하고 파라미터에 담을 때 발생하는 예외
	 */
	private void decQueryString (final String sValue) throws Exception
	{
		final String queryString = EncryptUtil.decrypt(URLDecoder.decode(sValue, Constant.getValue("Charset.JspPage", "UTF-8")));
		final String[] strings = queryString.split("&");
		for (final String keyvalue : strings)
		{
			final String[] kv = keyvalue.split("=");
			this.put(kv[0], kv[1]);
		}
	}
}