package chk.jsphelper;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

import chk.jsphelper.module.pool.ThreadPool;
import chk.jsphelper.module.runnable.MaintainDBConn;
import chk.jsphelper.object.DataSource;
import chk.jsphelper.object.enums.ObjectType;
import chk.jsphelper.util.ConvertUtil;
import chk.jsphelper.util.StringUtil;
import chk.jsphelper.value.SqlValue;

/**
 * 이 클래스는 JspHelper 프레임웤에서 기본정보를 프로퍼티 등에 담아 전역적인 번수로 값들을 담기 위해 쓰인다. 생성자는 따로 없으며 최초로 값들이 세팅될 때 한번만 세팅된 값은 변하지 않는다. 단 값들의 세팅을 변경하거나 추가하는 메소드를 지원하므로 컨테이너를 재기동하기 힘들 때에는 그 메소드를 써야 한다.
 * 
 * @author Corestone H. Kang
 * @version 1.0
 * @since 1.0
 */
public class Constant
{
	/**
	 * 프로퍼티 파일의 이름으로 변경 불가능하다. 그리고 프로퍼티 파일은 <code>/WEB-INF/classes/</code>에 위치하여야 한다.
	 */
	private static final String CONFING_FILE = "jsphelper.properties";

	/**
	 * 프로퍼티의 정보가 담겨지는 프로퍼티 클래스이다.
	 */
	private static Preferences globalConfig = Preferences.userNodeForPackage(Constant.class);
	/**
	 * 프레임웤에서 쓰이는 로거 클래스이다.
	 */
	private static final Logger LOG = LoggerFactory.getLogger("JspHelper");
	/**
	 * 
	 */
	private static String serverURL = null;
	/**
	 * 프레임웤의 버전
	 */
	private static final String VERSION = "3.0.0";
	static
	{
		try
		{
			// 현재 돌고 있는 스레드의 클래스를 가지고 현재의 위치를 잡아 해당 프로퍼티 파일을 읽어 온다.
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			final InputStream inStream = loader.getResourceAsStream(Constant.CONFING_FILE);
			final Properties temp = new Properties();
			temp.load(inStream);
			final String tempURL = temp.getProperty("Server.URL", "http://localhost/");
			Constant.serverURL = StringUtils.replaceEach(tempURL, new String[] { "http://", "/", ".", ":" }, new String[] { "", "", "", "" }) + temp.getProperty("Server.Context", "");
			Constant.getLogger().info("======= JSP Helper Framework Version : {} =======", Constant.getVersion());

			final Iterator<?> element = temp.keySet().iterator();
			final StringBuilder sb = new StringBuilder("초기에 로딩된 프로퍼티 항목 :\n");

			while (element.hasNext())
			{
				final String key = (String) element.next();
				if ((key.indexOf("KeyName.") == 0) && (temp.getProperty(key).indexOf("_") != 0))
				{
					Constant.getLogger().error("프로퍼티 [{}]의 값이 시스템 변수인 _로 시작하는 변수명이 아닙니다.", key);
				}
				else
				{
					Constant.globalConfig.put(Constant.serverURL + "::" + key, temp.getProperty(key));
					sb.append(key + " = " + temp.getProperty(key) + "\n");
				}
			}
			Constant.getLogger().info(sb.toString());

			if (Constant.getValue("Server.Context", "").equals(""))
			{
				Constant.globalConfig.put(Constant.serverURL + "::Path.Client", Constant.getValue("Path.Client", "/jsphelper/"));
			}
			else
			{
				Constant.globalConfig.put(Constant.serverURL + "::Path.Client", "/" + Constant.getValue("Server.Context", "") + Constant.getValue("Path.Client", "/jsphelper/"));
			}

			if (!ObjectLoader.isSuccess())
			{
				Constant.getLogger().error("XML 데이타를 오브젝트로 변환하는데 문제가 발생하였습니다. 로그를 확인해 보시기 바랍니다.");
			}
			final Iterator<String> ids = ObjectFactory.getObjectIDs(ObjectType._DATASOURCE);
			while (ids.hasNext())
			{
				final String id = ids.next();
				final DataSource ds = ObjectFactory.getDataSource(id);
				if (ds.getSql() != null)
				{
					final MaintainDBConn mdbc = new MaintainDBConn(ds.getInterval(), ds.getSql(), ds.getId());
					ThreadPool.getInstance().assign(mdbc);
				}
			}
			if (!"".equals(Constant.getValue("MessageDB.SQLID", "")))
			{
				final ServiceCaller sf = new ServiceCaller();
				final SqlValue sql = sf.executeSql(Constant.getValue("MessageDB.SQLID", ""), Parameter.getInstance());
				ConvertUtil.dataList2Message(sql.getDataList(), Constant.getValue("MessageDB.Languageis", "").split(","));
				Constant.getLogger().info("메시지 테이블에서 {} 개의 데이타를 메모리에 올림", sql.getDataList().size());
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
		}
	}

	public static Logger getLogger ()
	{
		return Constant.LOG;
	}

	/**
	 * 프레임웤에서 사용되는 로거를 호출하는 메소드이다.
	 * 
	 * @return - Logger 클래스
	 */
	/*
	 * public static LoggerWrapper getLogger ()
	 * {
	 * if (Constant.logger == null)
	 * {
	 * Constant.logger = LoggerWrapper.getLogger(Constant.serverURL + ":" + Constant.getVersion());
	 * Constant.logger.setAppender(Constant.serverURL, Boolean.parseBoolean(Constant.getValue("Location.Print", "false")));
	 * }
	 * return Constant.logger;
	 * }
	 */
	/**
	 * 해당 프로퍼티의 값을 반환하는 메소드이다.
	 * 
	 * @param key
	 *            - 프로퍼티의 키값
	 * @param initValue
	 *            - 프로퍼티의 키가 없을 경우의 초기값
	 * @return - 프로퍼티의 키에 해당하는 문자열 값
	 */
	public static String getValue (final String key, final String initValue)
	{
		return StringUtil.trim(Constant.globalConfig.get(Constant.serverURL + "::" + key, initValue));
	}

	/**
	 * 해당 프로퍼티의 값을 반환하는 메소드이다.<br>
	 * 프로퍼티의 값을 ","로 구분한 것 중에서 해당 인덱스의 값을 가져올 때 사용한다.
	 * 
	 * @param key
	 *            - 프로퍼티의 키값
	 * @param initValue
	 *            - 값이 없을 때 지정할 초기값
	 * @param index
	 *            - 가져올 프로퍼티 값 배열의 인덱스
	 * @return - 프로퍼티의 키에 해당하는 문자열 값
	 */
	public static String getValue (final String key, final String initValue, final int index)
	{
		final String[] value = StringUtil.trim(Constant.globalConfig.get(Constant.serverURL + "::" + key, "")).split(",");
		if (value.length <= index)
		{
			Constant.getLogger().warn("[{}]에 대한 값이 없거나 크기가  {}보다 작은 {}이므로 값을 가져올 수 없습니다.", new Object[] { key, (index + 1), value.length });
			return initValue;
		}
		return value[index];
	}

	/**
	 * 프레임웤의 버전을 반환하는 메소드이다.<br>
	 * 버전 정보는 x.x.xx 형식으로 이루어지며 로그에서도 버전이 표기된다.
	 * 
	 * @return - 프레임웤의 버전
	 */
	public static String getVersion ()
	{
		return Constant.VERSION;
	}

	/**
	 * 로그의 레벨을 세팅하는 메소드로 기본값은 모든 로그가 찍히게 되어 있다.<br>
	 * 그러나 이 메소드 보다는 DEBUG_MODE 를 false로 변경하는 것을 권장한다.
	 * 
	 * @param level
	 *            - ALL, TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF 중의 하나를 준다.
	 */
	public static void setLogLevel (final Level level)
	{
		Constant.logger.setLevel(level);
	}

	private Constant ()
	{

	}
}