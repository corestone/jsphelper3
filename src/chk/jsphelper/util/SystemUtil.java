package chk.jsphelper.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import chk.jsphelper.Constant;
import chk.jsphelper.module.runnable.StreamGobbler;

public class SystemUtil
{
	/**
	 * 메일주소 @ 뒤의 도메인의 유효성검사하기<br>
	 * nslookup 명령어를 통해 레코드가 MX인 것을 검사하여 메일 도메인의 유효성을 체크한다.
	 */
	public static boolean checkMailServer (final String email) throws IOException
	{
		boolean result = false;
		if ((email != null) && !"".equals(email))
		{
			final String domain = email.substring(email.indexOf("@") + 1);
			final String[] cmd = { "nslookup", "-type=MX", domain };
			final String print = SystemUtil.getCmd(cmd);
			if (print.indexOf("mail exchanger") != -1)
			{
				result = true;
			}
			else
			{
				result = false;
			}
		}
		return result;
	}

	/**
	 * 시스템 콘솔에서 실행된 명령의 결과를 String 으로 받아 온다
	 * 
	 * @param cmd
	 *            실행한 콘솔 명령
	 * @return 실행 결과
	 */
	public static String getCmd (final String... cmd)
	{
		try
		{
			String[] runCmd = null;
			if (System.getProperty("os.name").startsWith("Win"))
			{
				runCmd = new String[cmd.length + 2];

				System.arraycopy(cmd, 0, runCmd, 2, cmd.length);
				runCmd[0] = "cmd";
				runCmd[1] = "/c";
				final Process p = new ProcessBuilder(runCmd).start();

				final StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "Error");
				final StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "Output");
				errorGobbler.start();
				outputGobbler.start();
				p.waitFor();
				while ((outputGobbler.getResult() == null) && (errorGobbler.getResult() == null))
				{
				}
				Constant.getLogger().info("output : {}", new Object[] { outputGobbler.getResult() });
				Constant.getLogger().info("error : {}" + new Object[] { errorGobbler.getResult() });
				p.destroy();
				return StringUtil.trimDefault(outputGobbler.getResult(), errorGobbler.getResult());
			}
			else
			{
				runCmd = cmd;
				final Process p = new ProcessBuilder(runCmd).start();
				final InputStream in = p.getInputStream();
				final BufferedReader br = new BufferedReader(new InputStreamReader(in));
				final StringBuilder sb = new StringBuilder();
				String temp = "";
				while ((temp = br.readLine()) != null)
				{
					sb.append(temp + "\n");
				}
				br.close();
				in.close();
				return sb.toString();
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
		}
		return null;
	}

	/**
	 * 시스템 환경설정 파일을 읽어와서 이름에 대한 값을 반환하는 메소드<br>
	 * 예) SystemUtil.getEnv("JAVA_HOME") = 시스템의 JAVA_HOME 경로
	 * 
	 * @param envname
	 *            - 환경변수 명
	 * @return - 해당하는 환경변수에 따른 값
	 * @throws Exception
	 */
	public static String getEnv (final String envname) throws Exception
	{
		String envval = System.getProperty(envname);

		if (envval != null)
		{
			return envval;
		}

		final String osName = System.getProperties().getProperty("os.name");

		if (osName.startsWith("Win"))
		{
			envval = SystemUtil.getCmd("echo %" + envname + "%");
		}
		else
		{
			final Process ps = Runtime.getRuntime().exec("/usr/bin/env");
			ps.waitFor();
			final Properties prop = new Properties();
			prop.load(ps.getInputStream());
			envval = prop.getProperty(envname);
		}

		if (envval == null)
		{
			return null;
		}
		return envval;
	}

	/**
	 * 특정 값이 ${ 와 } 로 둘려쌓일 때 안의 값을 환경변수의 값으로 치환하여 반환하는 메소드 예) replaceEnv2Value("${JAVA_HOME}/lib/") = 자바홈 경로 + "/lib/"
	 * 
	 * @param value
	 *            - 원래 값
	 * @return - 변환된 값
	 */
	public static String replaceEnv2Value (final String value)
	{
		final StringBuilder sb = new StringBuilder();
		int offset = 0;
		int fromIndex = 0, toIndex = 0;
		while ((fromIndex = value.indexOf("${", offset)) != -1)
		{
			if (fromIndex > offset)
			{
				sb.append(value.substring(offset, fromIndex));
			}
			toIndex = value.indexOf("}", fromIndex);
			if (toIndex == -1)
			{
				break;
			}
			final String key = value.substring(fromIndex + 2, toIndex);
			String temp = System.getProperty(key, "");
			if (temp.equals(""))
			{
				try
				{
					temp = SystemUtil.getEnv(key);
				}
				catch (final Exception e)
				{
					temp = "";
				}
			}
			sb.append(temp);
			offset = toIndex + 1;
		}
		if (offset < value.length())
		{
			sb.append(value.substring(offset));
		}
		return sb.toString();
	}

	/**
	 * 한글의 인코딩이 어떻게 변하는지 테스트를 하는 메소드이다
	 * 한글 인코딩을 변화하면서 인코딩을 한 결과를 로그에 남긴다.
	 * 
	 * @param hanText
	 *            - 테스트할 한글문자열
	 */
	public static void testHanEncoding (final String hanText)
	{
		Constant.getLogger().debug("[TEST] 변환전 : {}", new Object[] { hanText });
		final String charset[] = { "8859_1", "ascii", "UTF-8", "KSC5601", "EUC-KR", "MS949" };
		try
		{
			for (int i = 0; i < charset.length; i++)
			{
				for (int j = 0; j < charset.length; j++)
				{
					if (i == j)
					{
						continue;
					}
					else
					{
						Constant.getLogger().debug("[TEST] {} -> {} : {}", new Object[] { charset[i], charset[j], new String(hanText.getBytes(charset[i]), charset[j]) });
					}
				}
			}
		}
		catch (final UnsupportedEncodingException uee)
		{
			Constant.getLogger().error(uee.getLocalizedMessage(), uee);
		}
	}

	private SystemUtil ()
	{
	}
}