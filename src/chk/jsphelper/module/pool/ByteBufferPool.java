package chk.jsphelper.module.pool;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ByteBufferPool
{
	// 두 블럭의 크기는 같으면 안된다. 이것은 버퍼의 구분값으로도 쓰이기 때문이다.
	private static final int FILE_BLOCKSIZE = 8192; // 파일 블럭 크기 : 8Kb
	private static final int MEMORY_BLOCKSIZE = 2048; // 메모리 블럭 크기 : 2Kb
	private final ArrayList<ByteBuffer> fileQueue = new ArrayList<ByteBuffer>();
	private final ArrayList<ByteBuffer> memoryQueue = new ArrayList<ByteBuffer>();
	// 버퍼를 가지고 올때 다 차 있으면 기본적으로 기다릴 것인지 아님 그냥 생성해서 가지고올 것인지 결정하는 변수
	private boolean wait = false;

	/**
	 * ByteBuffer Pool를 생성한다.
	 * 
	 * @param memorySize
	 *            - 메모리 버퍼의 갯수
	 * @param fileSize
	 *            - 파일버퍼의 갯수
	 * @param file
	 *            - 파일버퍼에 사용되는 파일오브젝트
	 * @throws IOException
	 *             - 파일버퍼를 만들때 생길 수 있는 IO예외
	 */
	public ByteBufferPool (final int memorySize, final int fileSize, final File file) throws IOException
	{
		if (memorySize > 0)
		{
			this.initMemoryBuffer(memorySize);
		}
		if (fileSize > 0)
		{
			this.initFileBuffer(fileSize, file);
		}
	}

	/**
	 * 파일 버퍼에서 있으면 파일 버퍼에서 가지고 오고 없으면 메모리 버퍼에서 가지고 오는 메소드
	 * 
	 * @return - 바이트버퍼
	 */
	public ByteBuffer getFileBuffer ()
	{
		return this.getBuffer(this.fileQueue, this.memoryQueue);
	}

	/**
	 * 메모리 버퍼에서 있으면 메모리 버퍼에서 가지고 오고 없으면 파일 버퍼에서 가지고 오는 메소드
	 * 
	 * @return - 바이트버퍼
	 */
	public ByteBuffer getMemoryBuffer ()
	{
		return this.getBuffer(this.memoryQueue, this.fileQueue);
	}

	/**
	 * 버퍼를 가지고 올때 버퍼의 여유가 없으면 기다릴지를 알아보는 메소드
	 * 
	 * @return - true : 반환될 때까지 기다린다. / false : 생성해서 바로 가져온다.
	 */
	public synchronized boolean getWait ()
	{
		return this.wait;
	}

	/**
	 * 버퍼를 풀에 다시 담는 메소드이다.
	 * 
	 * @param buffer
	 *            - 담을 버퍼
	 */
	public void putBuffer (final ByteBuffer buffer)
	{
		if (buffer.isDirect())
		{
			switch (buffer.capacity())
			{
				case MEMORY_BLOCKSIZE :
					this.putBuffer(buffer, this.memoryQueue);
					break;
				case FILE_BLOCKSIZE :
					this.putBuffer(buffer, this.fileQueue);
					break;
			}
		}
	}

	/**
	 * 버퍼를 가지고 올때 버퍼의 여유가 없으면 기다릴지를 설정하는 메소드
	 * 
	 * @param wait
	 *            - true : 반환될 때까지 기다린다. / false : 생성해서 바로 가져온다.
	 */
	public synchronized void setWait (final boolean wait)
	{
		this.wait = wait;
	}

	private void divideBuffer (final ByteBuffer buf, final int blockSize, final ArrayList<ByteBuffer> list)
	{
		final int bufferCount = buf.capacity() / blockSize;
		int position = 0;
		for (int i = 0; i < bufferCount; i++)
		{
			final int max = position + blockSize;
			buf.limit(max);
			list.add(buf.slice());
			position = max;
			buf.position(position);
		}
	}

	private ByteBuffer getBuffer (final ArrayList<ByteBuffer> firstQueue, final ArrayList<ByteBuffer> secondQueue)
	{
		ByteBuffer buffer = this.getBuffer(firstQueue, false);
		if (buffer == null)
		{
			buffer = this.getBuffer(secondQueue, false);
			if (buffer == null)
			{
				buffer = (this.wait ? this.getBuffer(firstQueue, true) : ByteBuffer.allocate(ByteBufferPool.MEMORY_BLOCKSIZE));
			}
		}
		return buffer;
	}

	private ByteBuffer getBuffer (final ArrayList<ByteBuffer> queue, final boolean wait)
	{
		synchronized (queue)
		{
			if (queue.isEmpty())
			{
				if (wait)
				{
					try
					{
						queue.wait();
					}
					catch (final InterruptedException e)
					{
						return null;
					}
				}
				else
				{
					return null;
				}
			}
			return queue.remove(0);
		}
	}

	private void initFileBuffer (int size, final File f) throws IOException
	{
		final int bufferCount = size / ByteBufferPool.FILE_BLOCKSIZE;
		size = bufferCount * ByteBufferPool.FILE_BLOCKSIZE;
		final RandomAccessFile file = new RandomAccessFile(f, "rw");
		try
		{
			file.setLength(size);
			final ByteBuffer fileBuffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0L, size);
			this.divideBuffer(fileBuffer, ByteBufferPool.FILE_BLOCKSIZE, this.fileQueue);
		}
		finally
		{
			file.close();
		}
	}

	private void initMemoryBuffer (int size)
	{
		final int bufferCount = size / ByteBufferPool.MEMORY_BLOCKSIZE;
		size = bufferCount * ByteBufferPool.MEMORY_BLOCKSIZE;
		final ByteBuffer directBuf = ByteBuffer.allocateDirect(size);
		this.divideBuffer(directBuf, ByteBufferPool.MEMORY_BLOCKSIZE, this.memoryQueue);
	}

	private void putBuffer (final ByteBuffer buffer, final ArrayList<ByteBuffer> queue)
	{
		buffer.clear();
		synchronized (queue)
		{
			queue.add(buffer);
			queue.notify();
		}
	}
}