package chk.jsphelper.value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import chk.jsphelper.Constant;
import chk.jsphelper.ObjectFactory;
import chk.jsphelper.Parameter;
import chk.jsphelper.module.mapper.ParameterMapper;
import chk.jsphelper.object.Upload;
import chk.jsphelper.util.StringUtil;

public class UploadValue extends AbstractValue
{
	/**
	 * 업로드 아이디에 대한 업로드 절대경로를 반환하는 메소드이다.
	 * 
	 * @param uplaodID
	 *            - 업로드 오브젝트 아이디
	 * @return - 해당 업로드 오브젝트의 저장 경로
	 */
	public static String getUploadPath (final String uplaodID)
	{
		if (ObjectFactory.getUpload(uplaodID) == null)
		{
			Constant.getLogger().error("[{}]에 해당하는 업로드 오브젝트가 없습니다. id를 확인해 보시기 바랍니다.", uplaodID);
			return null;
		}
		return ObjectFactory.getUpload(uplaodID).getDir();
	}

	/**
	 * 업로드 아이디에 대한 업로드 절대경로를 반환하는 메소드이다. 파라미터를 통해 값을 매핑하고 결과값을 전달한다.
	 * 
	 * @param uplaodID
	 *            - 업로드 오브젝트 아이디
	 * @param param
	 *            - 변환할 값이 있는 파라미터
	 * @return - 해당 업로드 오브젝트의 저장 경로
	 */
	public static String getUploadPath (final String uplaodID, final Parameter param)
	{
		String path = UploadValue.getUploadPath(uplaodID);
		if (path != null)
		{
			final ParameterMapper pm = new ParameterMapper(param);
			path = pm.convertMappingText(path);
		}
		return path;
	}

	/**
	 * 업로드 아이디에 대한 저장 경로를 웹에서 접근할 수 있도록 웹 절대경로로 변환하여 반환하는 메소드
	 * 
	 * @param uplaodID
	 * @return
	 */
	public static String getWebPath (final String uplaodID)
	{
		if (ObjectFactory.getUpload(uplaodID) == null)
		{
			Constant.getLogger().error("[{}]에 해당하는 업로드 오브젝트가 없습니다. id를 확인해 보시기 바랍니다.", uplaodID);
			return null;
		}
		String path = ObjectFactory.getUpload(uplaodID).getDir();
		if (path.startsWith(Constant.getValue("Path.WebRoot", "/home")))
		{
			path = StringUtils.replace(path, Constant.getValue("Path.WebRoot", "/home"), (Constant.getValue("Server.Context", "").equals("") ? "" : "/" + Constant.getValue("Server.Context", "")));
		}
		else
		{
			Constant.getLogger().error("[{}]에 해당하는 업로드 경로는 웹루트의 하위 디렉토리가 아닙니다.", uplaodID);
			path = null;
		}
		return path;
	}

	/**
	 * 업로드 아이디에 대한 저장 경로를 웹에서 접근할 수 있도록 웹 절대경로로 변환하여 반환하는 메소드. 파라미터를 통해 값을 매핑하고 결과값을 전달한다.
	 * 
	 * @param uplaodID
	 *            - 업로드 오브젝트 아이디
	 * @param param
	 *            - 변환할 값이 있는 파라미터
	 * @return - 해당 업로드 오브젝트의 저장 웹경로
	 */
	public static String getWebPath (final String uplaodID, final Parameter param)
	{
		String path = UploadValue.getWebPath(uplaodID);
		if (path != null)
		{
			final ParameterMapper pm = new ParameterMapper(param);
			path = pm.convertMappingText(path);
		}
		return path;
	}

	protected final Map<String, List<String>> fileInfo = new HashMap<String, List<String>>();

	protected String uploadPath = null;

	public UploadValue (final String functionID)
	{
		super(functionID);
		this.fileInfo.put("NAME", new ArrayList<String>());
		this.fileInfo.put("TYPE", new ArrayList<String>());
		this.fileInfo.put("OLD", new ArrayList<String>());
		this.fileInfo.put("NEW", new ArrayList<String>());
		this.fileInfo.put("SIZE", new ArrayList<String>());
	}

	public boolean existsField (final String fieldName)
	{
		return this.fileInfo.get("NAME").contains(fieldName);
	}

	public String getContentType (final String name)
	{
		if (this.existsField(name))
		{
			return this.getInfoValue(name, "TYPE").get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * 업로드 폼을 통해 업로드된 파일들의 MIME타입을 반환하는 메소드이다.
	 * 
	 * @return - MIME타입을 저장한 List객체
	 */
	public List<String> getContentTypeList ()
	{
		return this.fileInfo.get("TYPE");
	}

	/**
	 * 업로드 폼을 통해 업로드된 특정 파일의 MIME타입을 반환하는 메소드이다.
	 * 
	 * @param name
	 *            - 폼 이름
	 * @return - 파일의 MIME 타입
	 */
	public String[] getContentTypes (final String name)
	{
		if (this.existsField(name))
		{
			return this.getInfoValue(name, "TYPE").toArray(new String[] {});
		}
		else
		{
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		// final List<String> values = this.getInfoValue(name, "TYPE");
		// final String[] returnValue = new String[values.size()];
		// int i = 0;
		// for (final String val : values)
		// {
		// returnValue[i] = val;
		// i++;
		// }
		// return returnValue;
	}

	/**
	 * 업로드된 파일의 갯수를 반환하는 메소드이다.
	 * 
	 * @return - 업로드된 파일의 갯수
	 */
	public int getFileCount ()
	{
		int rtnValue = 0;
		final Iterator<String> iter = this.fileInfo.get("NEW").iterator();
		while (iter.hasNext())
		{
			if (!iter.next().equals(""))
			{
				rtnValue++;
			}
		}
		return rtnValue;
	}

	/**
	 * 업로드 파일폼의 갯수를 반환하는 메소드이다.
	 * 
	 * @return - 파일 폼의 갯수
	 */
	public int getFileFormCount ()
	{
		return this.fileInfo.get("NAME").size();
	}

	/**
	 * 업로드한 파일폼의 name값의 리스트를 반환하는 메소드이다.
	 * 
	 * @return - name값 리스트
	 */
	public List<String> getFileFormNameList ()
	{
		return this.fileInfo.get("NAME");
	}

	public String getLocalName (final String name)
	{
		if (this.existsField(name))
		{
			return this.getInfoValue(name, "OLD").get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * 업로드 폼에서 입력된 원래 파일명 리스트를 반환하는 메소드이다.
	 * 
	 * @return - 입력한 원래 파일명 리스트
	 */
	public List<String> getLocalNameList ()
	{
		return this.fileInfo.get("OLD");
	}

	/**
	 * 업로드 폼에서 입력된 원래 파일명을 반환하는 메소드이다.
	 * 
	 * @param name
	 *            - 파일폼 이름
	 * @return - 입력한 원래 파일명
	 */
	public String[] getLocalNames (final String name)
	{
		if (this.existsField(name))
		{
			return this.getInfoValue(name, "OLD").toArray(new String[] {});
		}
		else
		{
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		// final List<String> values = this.getInfoValue(name, "OLD");
		// final String[] returnValue = new String[values.size()];
		// int i = 0;
		// for (final String val : values)
		// {
		// returnValue[i] = val;
		// i++;
		// }
		// return returnValue;
	}

	@Override
	public Upload getObject ()
	{
		return ObjectFactory.getUpload(this.functionID);
	}

	/**
	 * 업로드 된 경로를 리턴하는 메소드이다.
	 * 
	 * @return - 업로드 된 경로
	 */
	public String getSaveDir ()
	{
		return this.uploadPath;
	}

	public String getSavedName (final String name)
	{
		if (this.existsField(name))
		{
			return this.getInfoValue(name, "NEW").get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * 서버에 저장된 파일명 리스트를 반환하는 메소드이다.
	 * 
	 * @return - 서버에 저장된 파일명 리스트
	 */
	public List<String> getSavedNameList ()
	{
		return this.fileInfo.get("NEW");
	}

	/**
	 * 서버에 저장된 파일명을 반환하는 메소드이다.
	 * 
	 * @param name
	 *            - 업로드 파일폼 이름
	 * @return - 서버에 저장된 파일명
	 */
	public String[] getSavedNames (final String name)
	{
		if (this.existsField(name))
		{
			return this.getInfoValue(name, "NEW").toArray(new String[] {});
		}
		else
		{
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		// final List<String> values = this.getInfoValue(name, "NEW");
		// final String[] returnValue = new String[values.size()];
		// int i = 0;
		// for (final String val : values)
		// {
		// returnValue[i] = val;
		// i++;
		// }
		// return returnValue;
	}

	public long getSizeInByte (final String name)
	{
		if (this.existsField(name))
		{
			return Long.parseLong(this.getInfoValue(name, "SIZE").get(0));
		}
		else
		{
			return 0L;
		}
	}

	/**
	 * 업로드 된 파일의 크기 리스트를 반환하는 메소드이다.
	 * 
	 * @return - 파일의 바이트 크기 리스트
	 */
	public List<String> getSizeInByteList ()
	{
		return this.fileInfo.get("NEW");
	}

	/**
	 * 업로드 된 파일의 크기를 반환하는 메소드이다.
	 * 
	 * @param name
	 *            - 업로드 파일 폼
	 * @return - 해당 파일의 바이트 크기
	 */
	public long[] getSizeInBytes (final String name)
	{
		if (this.existsField(name))
		{
			final List<String> values = this.getInfoValue(name, "SIZE");
			final long[] returnValue = new long[values.size()];
			int i = 0;
			for (final String val : values)
			{
				returnValue[i] = Long.parseLong(StringUtil.trimDefault(val, "0"));
				i++;
			}
			return returnValue;
		}
		else
		{
			return ArrayUtils.EMPTY_LONG_ARRAY;
		}
	}

	@Override
	public void loggingObject ()
	{
		Constant.getLogger().info("Upload ID : {}에 쓰인 값들\n{}", new Object[] { this.functionID, ObjectFactory.getUpload(this.functionID).toString() });
	}

	@Override
	public void setRequest (final HttpServletRequest req)
	{
		req.setAttribute("upload." + this.functionID + ".uploadPath", this.uploadPath);
		req.setAttribute("upload." + this.functionID + ".fileInfo", this.fileInfo);
	}

	private List<String> getInfoValue (final String name, final String code)
	{
		final List<String> fieldName = this.fileInfo.get("NAME");
		final List<String> returnValue = new ArrayList<String>();
		if (fieldName.contains(name))
		{
			for (final String key : fieldName)
			{
				if (name.equals(key))
				{
					returnValue.add(this.fileInfo.get(code).get(fieldName.indexOf(key)));
				}
			}
			return returnValue;
		}
		else
		{
			Constant.getLogger().error("{}에 해당하는 파일폼이 없습니다. 그래서 그냥 빈 리스트를 리턴합니다.", name);
			return returnValue;
		}
	}
}