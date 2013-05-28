package chk.jsphelper.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import chk.jsphelper.Constant;

public class FileUtil
{
	public static final int KB = 1024;
	public static final int MB = FileUtil.KB * FileUtil.KB;

	/**
	 * 파일에 문자열을 추가해서 입력하는 메소드이다.
	 * 
	 * @param filePath
	 *            - 파일경로
	 * @param fileContent
	 *            - 추가할 내용
	 * @return - 결과 성공여부
	 */
	public static boolean appendContent (final String filePath, final String fileContent)
	{
		boolean retValue = false;
		RandomAccessFile raf = null;
		FileChannel fc = null;
		try
		{
			final byte[] b = fileContent.getBytes();
			final ByteBuffer bb = ByteBuffer.allocateDirect(b.length);
			bb.put(b);
			bb.flip();
			raf = new RandomAccessFile(filePath, "rw");
			fc = raf.getChannel();
			raf.seek(raf.length());
			fc.write(bb);
			retValue = true;
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
			return retValue;
		}
		finally
		{
			// 자원 해제
			CloseUtil.closeObject(fc);
			CloseUtil.closeObject(raf);
		}
		return retValue;
	}

	/**
	 * 파일Copy. NIO를 이용하여 파일복사 속도 향상
	 * 
	 * @param srcPath
	 *            - 복사할 파일명을 포함한 절대 경로
	 * @param destPath
	 *            - 복사될 파일명을 포함한 절대경로
	 * @return boolean 성공여부
	 */
	public static boolean copyFile (final String srcPath, final String destPath)
	{
		// 복사 대상이 되는 파일 생성
		final File sourceFile = new File(srcPath);

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try
		{
			// 스트림 생성
			fis = new FileInputStream(sourceFile);
			fos = new FileOutputStream(destPath);
			// 채널 생성
			fcin = fis.getChannel();
			fcout = fos.getChannel();
			// 채널을 통한 스트림 전송
			final long size = fcin.size();
			final int maxCount = FileUtil.MB * 128; // NT 시스템에 따라 조절하야 함
			long position = 0;
			while (position < size)
			{
				position += fcin.transferTo(position, maxCount, fcout);
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
			return false;
		}
		finally
		{
			// 자원 해제
			CloseUtil.closeObject(fcout);
			CloseUtil.closeObject(fcin);
			CloseUtil.closeObject(fos);
			CloseUtil.closeObject(fis);
		}
		return true;
	}

	/**
	 * 폴더를 생성하는 메소드이다.
	 * 
	 * @param dirName
	 *            - 생성할 폴더명
	 * @return - 결과 성공여부
	 */
	public static boolean createDir (final String dirName)
	{
		// 생성할 디렉토리 경로를 가져와 인스턴스생성
		final File dir = new File(dirName);
		// 디렉토리가 유효한지 검사
		if (dir.isDirectory())
		{
			return true;
		}
		try
		{
			if (!dir.exists())
			{
				dir.mkdirs();
			}
		}
		catch (final Exception e)
		{
			return false;
		}
		return true;
	}

	/**
	 * 해당 파일을 생성한다.
	 * 
	 * @param path
	 *            - 생성할 파일의 경로
	 * @param fileName
	 *            - 파일명
	 * @param fileContent
	 *            - 파일 안의 내용
	 */
	public static boolean createFile (final String path, final String fileName, final String fileContent)
	{
		boolean rtnValue = false;
		FileOutputStream fos = null;
		FileChannel fc = null;
		try
		{
// FileUtil.createDir(new String(path.getBytes(Constant.getValue("Encoding.FileSystem", "UTF-8"))));
			FileUtil.createDir(new String(path.getBytes("UTF-8")));

			final byte[] b = fileContent.getBytes();
			final ByteBuffer bb = ByteBuffer.allocateDirect(b.length);
			bb.put(b);
			bb.flip();

			fos = new FileOutputStream(path + File.separator + fileName);
			fc = fos.getChannel();
			fc.write(bb);
			rtnValue = true;
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
			return rtnValue;
		}
		finally
		{
			// 자원 해제
			CloseUtil.closeObject(fc);
			CloseUtil.closeObject(fos);
		}
		return rtnValue;
	}

	/**
	 * 이미지 파일로부터 섬네일 이미지를 생성하는 메소드이다.
	 * 
	 * @param filePath
	 *            - 원본 이미지 파일
	 * @param width
	 *            - 섬네일이미지의 가로 폭
	 * @param height
	 *            - 섬네일이미지의 세로 폭
	 * @param thumbFile
	 *            - 네일을 만들 이미지 파일경로
	 */
	public static void createThumbnail (final File filePath, int width, int height, final String thumbFile)
	{
		final File f = new File(thumbFile);
		try
		{
			final ImageOutputStream thPath = new FileImageOutputStream(f);
			final BufferedImage bi = ImageIO.read(filePath);

			if (((float) width / bi.getWidth()) > ((float) height / bi.getHeight()))
			{
				width = (int) (bi.getWidth() * ((float) height / bi.getHeight()));
			}
			else
			{
				height = (int) (bi.getHeight() * ((float) width / bi.getWidth()));
			}

			final BufferedImage thImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g2 = thImg.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2.drawImage(bi, 0, 0, width, height, null);
			g2.dispose();
			ImageIO.write(thImg, "jpg", thPath);
		}
		catch (final IOException ioe)
		{
			Constant.getLogger().error("파일을 읽고 쓰는 데 문제가 발생하였습니다.", ioe);
		}
	}

	/**
	 * 폴더를 삭제하는 메소드로 하위 폴더나 내용까지 모두 삭제가 된다.
	 * 
	 * @param path
	 *            삭제할 폴더
	 * @return -결과 성공여부
	 */
	public static boolean deleteDir (final String path)
	{
		final File file = new File(path);
		if (!file.exists())
		{
			return true;
		}

		final File[] files = file.listFiles();
		for (final File f : files)
		{
			if (f.isDirectory())
			{
				FileUtil.deleteDir(f.getPath());
			}
			else
			{
				FileUtil.deleteFile(f.getPath() + File.separator + f.getName());
			}
		}
		return file.delete();
	}

	/**
	 * 파일을 삭제하는 메소드이다.
	 * 
	 * @param filePath
	 *            - 삭제할 파일경로
	 */
	public static void deleteFile (final String filePath)
	{
		final File f = new File(filePath);
		if (f.exists())
		{
			f.delete();
		}
	}

	/**
	 * 파일명의 확장자를 리턴해주는 메소드이다.
	 * 
	 * @param filename
	 *            : 파일명 문자열로 여기에서 확장자를 찾아 리턴해 준다.
	 * @return 파일명에서의 확장자
	 */
	public static String getExtension (final String filename)
	{
		final int extIndex = filename.lastIndexOf(".");
		String extName = "";
		if (extIndex > 0)
		{
			extName = filename.substring(extIndex + 1);
		}
		return extName;
	}

	/**
	 * 파일 내용을 MD5 checksum으로 체크한 값을 반환하는 메소드이다.
	 * 
	 * @param sFileName
	 * @return
	 */
	public static String getMD5 (final String sFileName)
	{
		String rtnValue = "";
		try
		{
			final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sFileName));
			final DigestInputStream dis = new DigestInputStream(bis, MessageDigest.getInstance("MD5"));
			final byte[] buf = new byte[1024];
			while (dis.read(buf) <= 0)
			{
				break;
			}
			dis.close();
			bis.close();
			final MessageDigest md = dis.getMessageDigest();
			final byte[] digest = md.digest();
			for (final byte element : digest)
			{
				rtnValue += Integer.toHexString(255 & (char) element);
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
		}
		return rtnValue;
	}

	/**
	 * 파일명에서 확장자를 제거한 파일이름 부분만을 반환해주는 메소드
	 * 
	 * @param filename
	 *            : 파일명 문자열로 여기에서 파일명을 반환한다.
	 * @return 파일명에서 확장자를 제외한 파일명
	 */
	public static String getNameWithoutExt (final String filename)
	{
		final int extIndex = filename.lastIndexOf(".");
		if (extIndex == -1)
		{
			return filename;
		}
		else
		{
			return filename.substring(0, extIndex);
		}
	}

	/**
	 * 파일을 이동시키는 메소드이다.
	 * 
	 * @param srcFile
	 *            - 원본 파일경로
	 * @param trgFile
	 *            - 대상 파일경로
	 */
	public static void moveFile (final String srcFile, final String trgFile)
	{
		final File src = new File(srcFile);
		final File trg = new File(trgFile);
		if (!src.exists() || src.renameTo(trg))
		{
			return;
		}
		FileUtil.copyFile(srcFile, trgFile);
		FileUtil.deleteFile(srcFile);
	}

	/**
	 * 파일의 풀 패스를 통해 파일명만을 반환하는 메소드이다.<br />
	 * 실제 파일을 검사하는 것이 아니라 경로 문자열에서 폴더 구분자의 마지막 값을 반환함으로 경로명만 입력하면 마지막 폴더명을 반환한다.
	 * 
	 * @param path
	 *            - 파일 풀 경로명
	 * @return - 경로명에서 추출한 파일명
	 */
	public static String path2FileName (final String path)
	{
		if (StringUtil.isNullOrEmpty(path))
		{
			return path;
		}
		// 원도우 시스템 파일을 유닉스에서 검사할 때 File.separator 가 안되기 때문에 강제로 변환함
		final String convPath = path.replace("\\", "/");
		final int lastIndex = convPath.lastIndexOf("/");
		return convPath.substring(lastIndex + 1);
	}

	/**
	 * 파일의 내용을 읽어서 문자열로 반환하는 메소드이다.
	 * 
	 * @param filePath
	 *            - 읽을 파일경로
	 * @param charSet
	 *            - 파일의 캐릭터셋
	 * @return - 파일의 내용을 담은 문자열
	 */
	@SuppressWarnings ("resource")
	public static String readFile (final String filePath, final String charSet)
	{
		FileChannel fc = null;
		MappedByteBuffer mbb = null;
		Scanner sc = null;
		final StringBuilder sbContents = new StringBuilder();

		try
		{
			fc = new FileInputStream(filePath).getChannel();

			mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			final Charset charset = Charset.forName(StringUtil.trimDefault(charSet, "UTF-8"));
			final CharsetDecoder decoder = charset.newDecoder();
			final CharBuffer buffer = decoder.decode(mbb);

			// 라인 단위로 파일 읽음
			sc = new Scanner(buffer).useDelimiter("\n");

			while (sc.hasNext())
			{
				sbContents.append(sc.next() + "\n");
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error(e.getLocalizedMessage(), e);
		}
		finally
		{
			// 자원 해제
			CloseUtil.closeObject(fc);
		}
		return sbContents.toString();
	}

	private FileUtil ()
	{
	}
}