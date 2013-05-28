package chk.jsphelper.module.runnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import chk.jsphelper.util.CloseUtil;

public class StreamGobbler extends Thread
{
	InputStream is;
	String result;
	String type;

	public StreamGobbler (final InputStream is)
	{
		this.is = is;
		this.type = ">> ";
	}

	public StreamGobbler (final InputStream is, final String type)
	{
		this.is = is;
		this.type = type;
	}

	public String getResult ()
	{
		return this.result;
	}

	@Override
	public void run ()
	{
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(this.is));
			String line = "";
			final StringBuffer sb = new StringBuffer();

			while ((line = br.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			br.close();
			this.result = sb.toString();
		}
		catch (final IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			CloseUtil.closeObject(br);
		}
	}
}