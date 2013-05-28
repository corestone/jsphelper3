package chk.jsphelper.engine;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.ArrayUtils;

import chk.jsphelper.Constant;
import chk.jsphelper.Parameter;
import chk.jsphelper.engine.ext.upload.FileUploadListener;
import chk.jsphelper.module.mapper.ParameterMapper;
import chk.jsphelper.object.Upload;
import chk.jsphelper.util.FileUtil;
import chk.jsphelper.util.StringUtil;

/**
 * @author Corestone
 */
public class UploadEngine implements InterfaceEngine
{
	private final List<String> contentType = new ArrayList<String>();
	private final List<String> fieldName = new ArrayList<String>();
	private final List<String> fileSize = new ArrayList<String>();
	private final FileUploadListener listener = new FileUploadListener();
	private final List<String> newFileName = new ArrayList<String>();
	private final List<String> oldFileName = new ArrayList<String>();
	private final Parameter rtnParam;
	private final Upload upload;
	private String uploadPath = null;

	/**
	 * @param object
	 * @param param
	 */
	public UploadEngine (final Upload object, final Parameter param)
	{
		this.upload = object;
		this.rtnParam = param;
	}

	public void execute () throws Exception
	{
		try
		{
			final ParameterMapper pm = new ParameterMapper(this.rtnParam);
			pm.checkParam();
			final boolean isMultipart = ServletFileUpload.isMultipartContent(this.rtnParam.getRequest());
			if (!isMultipart)
			{
				throw new Exception("form의 enctype을 'multipart/form-data'로 해야 업로드가 됩니다.");
			}
			// iter 를 모두 list로 변경해서 작업 내부적으로 계속 next를 사용하므로 업로드 안됨
			this.uploadPath = pm.convertMappingText(this.upload.getDir());
			FileUtil.createDir(this.uploadPath);
			final List<FileItem> items = this.initUpload(pm);
			this.setParameter(items);
			this.uploadFile(pm, items);
		}
		catch (final Exception e)
		{
			throw e;
		}
	}

	public Map<String, Object> getValueObject ()
	{
		final Map<String, Object> hm = new HashMap<String, Object>();
		hm.put("Parameter", this.rtnParam);
		hm.put("NAME", this.fieldName);
		hm.put("TYPE", this.contentType);
		hm.put("OLD", this.oldFileName);
		hm.put("NEW", this.newFileName);
		hm.put("SIZE", this.fileSize);
		hm.put("DIR", this.uploadPath);
		return hm;
	}

	private String getNewFileName (final String fileName)
	{
		final String[] dirFiles = new File(this.uploadPath).list();
		int fileIndex = 0;
		String newName = fileName.toString();
		boolean isChange = true;

		while (isChange)
		{
			isChange = false;
			for (final String element : dirFiles)
			{
				if (newName.toUpperCase().equals(element.toUpperCase()))
				{
					isChange = true;
					fileIndex++;
					newName = FileUtil.getNameWithoutExt(fileName) + "_" + fileIndex + "." + FileUtil.getExtension(fileName);
					Constant.getLogger().info("업로드 파일 중에서 {}를 {}로 변경하여 업로드합니다.", new String[] { fileName, newName });
				}
			}
		}
		return newName;
	}

	private List<FileItem> initUpload (final ParameterMapper pm) throws FileUploadException
	{
		final List<FileItem> items = new ArrayList<FileItem>();
		try
		{
			final int maxMemorySize = 1024 * 64; // threshold 값 설정
			long maxRequestSize = 0;
			final String maxSize = this.upload.getLimit();
			if (maxSize.substring(maxSize.length() - 1).toUpperCase().equals("M"))
			{
				maxRequestSize = 1024 * 1024 * Integer.parseInt(maxSize.substring(0, maxSize.length() - 1));
			}
			else if (maxSize.substring(maxSize.length() - 1).toUpperCase().equals("K"))
			{
				maxRequestSize = 1024 * Integer.parseInt(maxSize.substring(0, maxSize.length() - 1));
			}
			else
			{
				maxRequestSize = Integer.parseInt(maxSize.substring(0, maxSize.length() - 1));
			}

			final File tempDirectory = new File(this.uploadPath);
			final DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemorySize);
			factory.setRepository(tempDirectory);
			final ServletFileUpload uploader = new ServletFileUpload(factory);

			// 진행상태바 설정
			final HttpSession session = this.rtnParam.getRequest().getSession();
			if ("yes".equals(this.rtnParam.getValue("progress")))
			{
				session.setAttribute("_UploadListener", this.listener);
			}

			uploader.setProgressListener(this.listener);
			uploader.setFileSizeMax(maxRequestSize); // 업로드 최대 용량 설정
			uploader.setHeaderEncoding(Constant.getValue("Encoding.JspPage", "UTF-8")); // 인코딩

			if (this.rtnParam.getRequest().getContentLength() > maxRequestSize)
			{
				Constant.getLogger().error("[{}]에서 업로드의 용량({})을 초과하는 {} byte가 들어왔습니다.", new String[] { this.upload.getId(), this.upload.getLimit(), String.valueOf(this.rtnParam.getRequest().getContentLength()) });
				this.listener.setErrMsg("업로드 용량 " + this.upload.getLimit() + "을 초과해서 올릴 수 없습니다.");
				return items;
			}
			final List<?> temp = uploader.parseRequest(this.rtnParam.getRequest());
			for (int i = 0; i < temp.size(); i++)
			{
				items.add((FileItem) temp.get(i));
			}
			return items;
		}
		catch (final FileUploadException fue)
		{
			Constant.getLogger().error("[{}]에서 업로드 작업을 초기화 하는데 FileUploadException 예외가 발생하였습니다.", this.upload.getId());
			throw fue;
		}
	}

	private void setParameter (final List<FileItem> items) throws UnsupportedEncodingException
	{
		final HashMap<String, List<String>> hm = new HashMap<String, List<String>>();
		final Iterator<FileItem> iter = items.iterator();
		try
		{
			while (iter.hasNext())
			{
				final FileItem item = iter.next();
				// 정상적인 폼값 출력 및 처리
				if (item.isFormField())
				{
					final String key = item.getFieldName();
					final String value = item.getString(Constant.getValue("Encoding.JspPage", "UTF-8"));

					if (hm.containsKey(key))
					{
						hm.get(key).add(value);
					}
					else
					{
						final List<String> values = new ArrayList<String>();
						values.add(value);
						hm.put(key, values);
					}
				}
			}

			final Iterator<String> iterator = hm.keySet().iterator();
			while (iterator.hasNext())
			{
				final String key = iterator.next();
				final String[] temp = new String[hm.get(key).size()];
				for (int i = 0, z = temp.length; i < z; i++)
				{
					temp[i] = hm.get(key).get(i);
				}
				if (key.indexOf("_") == 0)
				{
					this.rtnParam.underbarPut(key, temp[0]);
				}
				else
				{
					this.rtnParam.put(key, temp);
				}
			}
		}
		catch (final UnsupportedEncodingException uee)
		{
			Constant.getLogger().error("[{}]에서 업로드를 데이타를 파라미터에 입력할 때UnsupportedEncodingException 예외가 발행하였습니다.", this.upload.getId());
			throw uee;
		}
	}

	private void uploadFile (final ParameterMapper pm, final List<FileItem> items) throws Exception
	{
		String[] denyExts = ArrayUtils.EMPTY_STRING_ARRAY;
		if (this.upload.getDeny() != null)
		{
			denyExts = this.upload.getDeny().toUpperCase().split(",");
		}
		String[] acceptExts = ArrayUtils.EMPTY_STRING_ARRAY;
		if (this.upload.getAccept() != null)
		{
			acceptExts = this.upload.getAccept().toUpperCase().split(",");
		}
		final Iterator<FileItem> iter = items.iterator();
		try
		{
			while (iter.hasNext())
			{
				final FileItem item = iter.next();
				// 업로드 파일 처리
				if (!item.isFormField())
				{
					final String filePath = item.getName();
					// 파일폼에 파일이 입력되지 않았을 경우 기본적인 빈 값들을 정보에 담는다.
					if ((filePath == null) || filePath.equals(""))
					{
						this.fieldName.add(item.getFieldName());
						this.contentType.add("");
						this.fileSize.add("0");
						this.oldFileName.add("");
						this.newFileName.add("");
					}
					else
					{
						final String oriFile = FileUtil.path2FileName(filePath);
						// 파일 확장자 체크해서 업로드 막음
						final String fileExt = FileUtil.getExtension(StringUtil.trim(oriFile).toUpperCase());
						if (ArrayUtils.contains(denyExts, fileExt))
						{
							Constant.getLogger().warn("파일업로드 중 {}의 확장자 '{}'는 업로드 할 수 없는 확장자 중의 하나입니다.", new String[] { oriFile, fileExt });
							this.listener.setErrMsg("파일업로드 중 " + oriFile + "의 확장자 '" + fileExt + "'는 업로드 할 수 없는 확장자 중의 하나입니다.");
							continue;
						}
						else if (!(acceptExts.equals(ArrayUtils.EMPTY_STRING_ARRAY) || ArrayUtils.contains(acceptExts, fileExt)))
						{
							Constant.getLogger().warn("파일업로드 중 {}의 확장자 '{}'는 업로드 할 수 있는 확장자가 아닙니다.", new String[] { oriFile, fileExt });
							this.listener.setErrMsg("파일업로드 중 " + oriFile + "의 확장자 '" + fileExt + "'는 업로드 할 수 있는 확장자가 아닙니다.");
							continue;
						}
						final String ext = FileUtil.getExtension(oriFile);
						String newFile = String.valueOf(System.nanoTime()) + "." + ext;
						newFile = this.getNewFileName(newFile);
						this.listener.addInputName(item.getFieldName(), oriFile);
						this.listener.addSavedName(item.getFieldName(), newFile);
						this.fieldName.add(item.getFieldName());
						this.contentType.add(item.getContentType());
						this.fileSize.add(String.valueOf((item.getSize())));
						this.oldFileName.add(oriFile);
						this.newFileName.add(newFile);
						final File uploadedFile = new File(this.uploadPath + newFile);
						item.write(uploadedFile);
						Constant.getLogger().info("[{}]에서 파일 {}이 업로드 되었습니다.", new String[] { this.upload.getId(), this.uploadPath + newFile });
					}
				}
			}
		}
		catch (final Exception e)
		{
			Constant.getLogger().error("[{}]에서 파일을 업로드하는데 Exception 예외가 발생하였습니다.", this.upload.getId());
			this.listener.setErrMsg("파일을 업로드하는데 Exception 예외가 발생하였습니다.");
			throw e;
		}
	}
}