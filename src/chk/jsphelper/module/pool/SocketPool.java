package chk.jsphelper.module.pool;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SocketPool
{
	private static boolean BLOCK;
	// Configuration variable
	private static String HOST;
	private static int INITIAL_COUNT;
	// Singleton
	private static SocketPool instance;
	private static int MAX_COUNT;
	private static int PORT;
	private static int TIMEOUT;
	static
	{
		SocketPool.loadConf();
		try
		{
			SocketPool.instance = new SocketPool();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	public static SocketPool getInstance () throws IOException
	{
		if (SocketPool.instance == null)
		{
			synchronized (SocketPool.class)
			{
				if (SocketPool.instance == null)
				{
					SocketPool.instance = new SocketPool();
				}
			}
		}

		return SocketPool.instance;
	}

	// Loading the configuration from pool.properties file.
	private static void loadConf ()
	{
		final ResourceBundle rb = ResourceBundle.getBundle("pool");
		SocketPool.HOST = rb.getString("host");
		SocketPool.PORT = Integer.parseInt(rb.getString("port"));
		SocketPool.MAX_COUNT = Integer.parseInt(rb.getString("maxCons"));
		SocketPool.INITIAL_COUNT = Integer.parseInt(rb.getString("initialCons"));
		final String timeout = rb.getString("timeout");
		final String block = rb.getString("block");
		if (block != null)
		{
			SocketPool.BLOCK = Boolean.getBoolean(block);
			SocketPool.TIMEOUT = Integer.parseInt(timeout);
		}
		System.out.println("Socket Pooling Configuration ****************");
		System.out.println("Host : " + SocketPool.HOST);
		System.out.println("Port : " + SocketPool.PORT);
		System.out.println("Max_Count : " + SocketPool.MAX_COUNT);
		System.out.println("Min_Count : " + SocketPool.INITIAL_COUNT);
		System.out.println("BLOCK : " + SocketPool.BLOCK);
		System.out.println("TIMEOUT : " + SocketPool.TIMEOUT);
		System.out.println("---------------------------------------------");
	}

	private int count;

	private final List<Socket> free;

	private final List<Socket> used;

	// Constructor
	private SocketPool () throws IOException
	{
		this.free = new ArrayList<Socket>();
		this.used = new ArrayList<Socket>();
		while (this.count < SocketPool.INITIAL_COUNT)
		{
			this.addSocket();
		}
	}

	public synchronized void closeAll () throws IOException
	{
		for (int i = 0; i < this.free.size(); i++)
		{
			final Socket sck = this.free.remove(i);
			try
			{
				sck.close();
			}
			catch (final IOException e)
			{
				System.out.println(e.toString());
			}
		}

		for (int i = 0; i < this.used.size(); i++)
		{
			this.used.remove(i);
		}
	}

	public Socket getSocket () throws IOException
	{
		return this.getSocket(SocketPool.BLOCK, SocketPool.TIMEOUT);
	}

	public synchronized Socket getSocket (final boolean block, final long timeout) throws IOException
	{
		if (this.free.isEmpty())
		{
			if (this.count < SocketPool.MAX_COUNT)
			{
				this.addSocket();
			}
			else if (block)
			{
				try
				{
					synchronized (this)
					{
						this.wait(timeout);
					}
				}
				catch (final InterruptedException e)
				{
					e.printStackTrace();
				}
				if (this.free.isEmpty())
				{
					if (this.count < SocketPool.MAX_COUNT)
					{
						this.addSocket();
					}
					else
					{
						throw new IOException("Timeout waiting for a socket to be released.");
					}
				}
			}
			else
			{
				throw new IOException("Maximum number of allowed Sockets reached");
			}
		}
		Socket sck = null;
		synchronized (this.used)
		{
			sck = this.free.remove(this.free.size() - 1);
			this.used.add(sck);
		}
		return sck;
	}

	public synchronized void release (final Socket sck) throws IOException
	{
		if (this.used.contains(sck))
		{
			final int idx = this.used.indexOf(sck);
			this.used.remove(idx);
			this.free.add(sck);
		}
		else
		{
			throw new IOException("Socket " + sck + " did not come from this SocketPool");
		}
		this.notify();
	}

	private void addSocket () throws IOException
	{
		final Socket sck = new Socket(SocketPool.HOST, SocketPool.PORT);
		this.free.add(sck);
		this.count++;
	}
}