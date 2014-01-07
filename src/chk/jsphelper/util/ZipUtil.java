package chk.jsphelper.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ZipUtil
{
	private static final int BUFFER_SIZE = 1024 * 4;
	private static final int COMPRESSION_LEVEL = 8;

	/**
	 * Zip 파일의 압축을 푼다.
	 * 
	 * @param zipFile
	 *            - 압축 풀 Zip 파일
	 * @param targetDir
	 *            - 압축 푼 파일이 들어간 디렉토리
	 * @throws Exception
	 */
	public static void unzip (final File zipFile, final File targetDir) throws Exception
	{
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry zentry = null;
		try
		{
			fis = new FileInputStream(zipFile); // FileInputStream
			zis = new ZipInputStream(fis); // ZipInputStream
			while ((zentry = zis.getNextEntry()) != null)
			{
				final String fileNameToUnzip = zentry.getName();
				final File targetFile = new File(targetDir, fileNameToUnzip);
				if (zentry.isDirectory())
				{// Directory 인 경우
					FileUtil.createDir(targetFile.getAbsolutePath()); // 디렉토리 생성
				}
				else
				{ // File 인 경우
					// parent Directory 생성
					FileUtil.createDir(targetFile.getParent());
					ZipUtil.unzipEntry(zis, targetFile);
				}
			}
		}
		finally
		{
			CloseUtil.closeObject(zis);
			CloseUtil.closeObject(fis);
		}
	}

	/**
	 * 지정된 폴더를 Zip 파일로 압축한다.
	 * 
	 * @param sourcePath
	 *            - 압축 대상 디렉토리
	 * @param output
	 *            - 저장 zip 파일 이름
	 * @throws Exception
	 */
	public static void zip (final String sourcePath, final String output) throws Exception
	{
		// 압축 대상(sourcePath)이 디렉토리나 파일이 아니면 리턴한다.
		final File sourceFile = new File(sourcePath);
		if (!sourceFile.isFile() && !sourceFile.isDirectory())
		{
			throw new Exception("압축 대상의 파일을 찾을 수가 없습니다.");
		}
		// output 의 확장자가 zip이 아니면 리턴한다.
		if (!(StringUtils.substringAfterLast(output, ".")).equalsIgnoreCase("zip"))
		{
			throw new Exception("압축 후 저장 파일명의 확장자를 확인하세요");
		}
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;
		try
		{
			fos = new FileOutputStream(output); // FileOutputStream
			bos = new BufferedOutputStream(fos); // BufferedStream
			zos = new ZipOutputStream(bos); // ZipOutputStream
			zos.setLevel(ZipUtil.COMPRESSION_LEVEL); // 압축 레벨 - 최대 압축률은 9, 디폴트 8
			ZipUtil.zipEntry(sourceFile, sourcePath, zos); // Zip 파일 생성
			zos.finish(); // ZipOutputStream finish
		}
		finally
		{
			CloseUtil.closeObject(zos);
			CloseUtil.closeObject(bos);
			CloseUtil.closeObject(fos);
		}
	}

	/**
	 * Zip 파일의 한 개 엔트리의 압축을 푼다.
	 * 
	 * @param zis
	 *            - Zip Input Stream
	 * @param targetFile
	 *            - 압축 풀린 파일의 경로
	 * @return
	 * @throws Exception
	 */
	private static File unzipEntry (final ZipInputStream zis, final File targetFile) throws Exception
	{
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(targetFile);
			final byte[] buffer = new byte[ZipUtil.BUFFER_SIZE];
			int len;
			while ((len = zis.read(buffer)) != -1)
			{
				fos.write(buffer, 0, len);
			}
		}
		finally
		{
			CloseUtil.closeObject(fos);
		}
		return targetFile;
	}

	/**
	 * 압축
	 * 
	 * @param sourceFile
	 * @param sourcePath
	 * @param zos
	 * @throws Exception
	 */
	private static void zipEntry (final File sourceFile, final String sourcePath, final ZipOutputStream zos) throws Exception
	{
		// sourceFile 이 디렉토리인 경우 하위 파일 리스트 가져와 재귀호출
		if (sourceFile.isDirectory())
		{
			if (sourceFile.getName().equalsIgnoreCase(".metadata"))
			{ // .metadata 디렉토리 return
				return;
			}
			final File[] fileArray = sourceFile.listFiles(); // sourceFile 의 하위 파일 리스트
			for (final File element : fileArray)
			{
				ZipUtil.zipEntry(element, sourcePath, zos); // 재귀 호출
			}
		}
		else
		{ // sourcehFile 이 디렉토리가 아닌 경우
			BufferedInputStream bis = null;
			try
			{
				final String sFilePath = sourceFile.getPath();
				final String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());
				bis = new BufferedInputStream(new FileInputStream(sourceFile));
				final ZipEntry zentry = new ZipEntry(zipEntryName);
				zentry.setTime(sourceFile.lastModified());
				zos.putNextEntry(zentry);
				final byte[] buffer = new byte[ZipUtil.BUFFER_SIZE];
				int cnt;
				while ((cnt = bis.read(buffer, 0, ZipUtil.BUFFER_SIZE)) != -1)
				{
					zos.write(buffer, 0, cnt);
				}
				zos.closeEntry();
			}
			finally
			{
				CloseUtil.closeObject(bis);
			}
		}
	}

	private ZipUtil ()
	{

	}
}