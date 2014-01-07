package chk.jsphelper.module.runnable;

import chk.jsphelper.util.CloseUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread
{
	InputStream is;
	String result;

	public StreamGobbler (final InputStream is)
	{
		this.is = is;
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
			String line;
			final StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null)
			{
				sb.append(line).append("\n");
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