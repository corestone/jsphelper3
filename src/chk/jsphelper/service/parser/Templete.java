package chk.jsphelper.service.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.StrBuilder;

import chk.jsphelper.Constant;
import chk.jsphelper.util.FileUtil;

/**
 * HTML 간이 템플릿 파서 엔진이다. 템플릿 파일을 읽어와서 각종 작업을 할 수 있다.<br>
 * 값을 입력할 곳에는 @(#) 형식으로 작성된다.<br>
 * 출력 여부 블럭인 IF 블럭은 <!-- **:IF # START:** --> 로 시작해서 <!-- **:IF # END:** --> 로 끝난다.<br>
 * 반복 블럭인 LOOP 블럭은 <!-- **:LOOP # START:** --> 로 시작해서 <!-- **:LOOP # END:** --> 로 끝난다.<br>
 * 사용예)
 * Templete t = Templete.createTemplete(File 템플릿 파일 경로); // 템플릿 생성
 * t.showIfBlock (List<String> IF 블럭 코드 리스트); // 템플릿에서 보여줄 IF블럭을 선택
 * t.parseLoopBlock (String LOOP 블럭 코드, List<Map<String, String>> 변환할 텍스트 리스트); // LOOP블럭을 파싱하여 List의 갯수만큼 만들어 냄
 * t.replaceText (Map<String, String> 변환할 텍스트 리스트); // LOOP블럭을 제외한 부분에서의 단어 변환 작업
 * out.print(t.parseTemplete()); // 템플릿을 파싱하여 결과값 리턴
 * 
 * @author Corestone H. Kang
 * @version 1.0
 * @since 3.0
 */
public class Templete
{
	private static final String END_BLOCK = ":** -->";
	private static final String END_VALUE = ")";
	private static final String START_BLOCK = "<!-- **:";
	private static final String START_VALUE = "@(";

	/**
	 * 템플릿 파일로 부터 템플릿 객체를 생성하는 메소드이다.
	 * 
	 * @param fileName
	 *            - 템플릿 파일경로
	 * @return - Templete 객체
	 */
	public static Templete createTemplete (final String fileName)
	{
		final Templete p = new Templete();
		p.TempleteString = new StrBuilder(FileUtil.readFile(fileName, Constant.getValue("Encoding.JspPage", "UTF-8")));

		p.splitIfBlock();
		p.splitLoopBlock();
		return p;
	}

	private final Map<String, StrBuilder> IfTemplete = new HashMap<String, StrBuilder>();
	private final Map<String, StrBuilder> LoopTemplete = new HashMap<String, StrBuilder>();

	private StrBuilder TempleteString = null;

	private Templete ()
	{

	}

	public void parseLoopBlock (final String code, final int size, final Map<String, List<String>> mappingData)
	{
		final StrBuilder sb = this.LoopTemplete.get(code);
		final StrBuilder text = new StrBuilder();

		if (sb == null)
		{
			return;
		}
		for (int i = 0; i < size; i++)
		{
			String value = sb.toString();
			final Iterator<String> keys = mappingData.keySet().iterator();
			while (keys.hasNext())
			{
				final String key = keys.next();
				value = value.replace(Templete.START_VALUE + key + Templete.END_VALUE, mappingData.get(key).get(i));
			}
			text.append(value);
		}
		this.LoopTemplete.put(code, text);
	}

	public void parseLoopBlock (final String code, final List<Map<String, String>> mappingLoopObject)
	{
		final StrBuilder sb = this.LoopTemplete.get(code);
		final StrBuilder text = new StrBuilder();

		if (sb == null)
		{
			return;
		}
		for (int i = 0, z = mappingLoopObject.size(); i < z; i++)
		{
			final Map<String, String> mappingObject = mappingLoopObject.get(i);
			if (mappingObject == null)
			{
				continue;
			}
			String value = sb.toString();
			final Iterator<String> keys = mappingObject.keySet().iterator();
			while (keys.hasNext())
			{
				final String key = keys.next();
				value = value.replace(Templete.START_VALUE + key + Templete.END_VALUE, mappingObject.get(key));
			}
			text.append(value);
		}
		this.LoopTemplete.put(code, text);
	}

	public String parseTemplete ()
	{
		final Iterator<String> ifkeys = this.IfTemplete.keySet().iterator();
		final Iterator<String> loopkeys = this.LoopTemplete.keySet().iterator();
		boolean isParsed = false;

		while (ifkeys.hasNext())
		{
			isParsed = false;
			final String code = ifkeys.next();
			final String block = Templete.START_BLOCK + "BLOCK " + ifkeys.next() + Templete.END_BLOCK;

			if (this.TempleteString.indexOf(block) > -1)
			{
				this.TempleteString.replaceAll(block, this.IfTemplete.get(code).toString());
				isParsed = true;
			}
			isParsed = isParsed || this.parseBlock(this.IfTemplete, this.IfTemplete, code, block);
			isParsed = isParsed || this.parseBlock(this.LoopTemplete, this.IfTemplete, code, block);
		}

		while (loopkeys.hasNext())
		{
			isParsed = false;
			final String code = loopkeys.next();
			final String block = Templete.START_BLOCK + "BLOCK " + loopkeys.next() + Templete.END_BLOCK;

			if (this.TempleteString.indexOf(block) > -1)
			{
				this.TempleteString.replaceAll(block, this.LoopTemplete.get(code).toString());
				isParsed = true;
			}
			isParsed = isParsed || this.parseBlock(this.IfTemplete, this.LoopTemplete, code, block);
			isParsed = isParsed || this.parseBlock(this.LoopTemplete, this.LoopTemplete, code, block);
		}
		return this.TempleteString.toString();
	}

	public void replaceText (final Map<String, String> mappingObject)
	{
		final Iterator<String> keys = mappingObject.keySet().iterator();
		final Iterator<String> ifkeys = this.IfTemplete.keySet().iterator();
		final Iterator<String> loopkeys = this.LoopTemplete.keySet().iterator();
		while (keys.hasNext())
		{
			final String key = keys.next();
			final String code = Templete.START_VALUE + key + Templete.END_VALUE;

			this.TempleteString.replaceAll(code, mappingObject.get(key));
			while (ifkeys.hasNext())
			{
				final String ifkey = ifkeys.next();
				this.IfTemplete.get(ifkey).replaceAll(code, mappingObject.get(key));
			}
			while (loopkeys.hasNext())
			{
				final String loopkey = loopkeys.next();
				this.LoopTemplete.get(loopkey).replaceAll(code, mappingObject.get(key));
			}
		}
	}

	public void showIfBlock (final List<String> showIfBlockList)
	{
		final Iterator<String> ifkey = this.IfTemplete.keySet().iterator();
		while (ifkey.hasNext())
		{
			final String key = ifkey.next();
			if (!showIfBlockList.contains(key))
			{
				this.IfTemplete.get(key).setLength(0);
			}
		}
	}

	@Override
	public String toString ()
	{
		final StrBuilder sb = new StrBuilder();
		sb.appendln(this.TempleteString);
		final Iterator<String> ifkey = this.IfTemplete.keySet().iterator();
		while (ifkey.hasNext())
		{
			final String key = ifkey.next();
			sb.appendln(this.IfTemplete.get(key));
		}
		final Iterator<String> loopkey = this.LoopTemplete.keySet().iterator();
		while (loopkey.hasNext())
		{
			final String key = loopkey.next();
			sb.appendln(this.LoopTemplete.get(key));
		}
		return sb.toString();
	}

	/**
	 * 템플릿 소스에서 블럭을 잘라내어 반환하는 메소드이다
	 * 
	 * @param source
	 *            - 템플릿 소스로 잘라낸 블럭 부분은 블럭코드로 변환되어진다.
	 * @param type
	 *            - "IF" 또는 "LOOP"
	 * @param code
	 *            - 블럭 명칭 코드
	 * @return - 잘려진 블럭
	 */
	private StrBuilder cutBlock (final StrBuilder source, final String type, final String code)
	{
		final String startCode = Templete.START_BLOCK + type + " " + code + " START" + Templete.END_BLOCK;
		final String endCode = Templete.START_BLOCK + type + " " + code + " END" + Templete.END_BLOCK;
		final String blockCode = Templete.START_BLOCK + "BLOCK " + code + Templete.END_BLOCK;
		final int startIndex = source.indexOf(startCode);
		final int endIndex = source.indexOf(endCode);
		final String block = source.substring(startIndex + startCode.length(), endIndex);
		source.replace(startIndex, endIndex + endCode.length(), blockCode);
		return new StrBuilder(block);
	}

	private boolean parseBlock (final Map<String, StrBuilder> sourceTemplete, final Map<String, StrBuilder> targetValue, final String code, final String block)
	{
		final Iterator<String> keys = sourceTemplete.keySet().iterator();
		while (keys.hasNext())
		{
			final String key = keys.next();
			if (sourceTemplete.get(key).indexOf(block) > -1)
			{
				sourceTemplete.get(key).replaceAll(block, targetValue.get(code).toString());
				return true;
			}
		}
		return false;
	}

	/**
	 * 템플릿의 IF 블럭들을 모두 쪼개는 작업을 하는 메소드이다
	 */
	private void splitIfBlock ()
	{
		int startIndex = this.TempleteString.length();
		int endIndex = 0;
		String code = "";

		while (startIndex > -1)
		{
			startIndex = this.TempleteString.lastIndexOf("START_CODE" + "IF", startIndex);
			endIndex = this.TempleteString.indexOf(Templete.END_BLOCK, startIndex);
			code = this.TempleteString.substring(startIndex + Templete.START_BLOCK.length() + 3, endIndex - 1);
			this.IfTemplete.put(code, this.cutBlock(this.TempleteString, "IF", code));
		}
	}

	/**
	 * 템플릿의 LOOP 블럭들을 모두 쪼개는 작업을 하는 메소드이다 IF 블럭을 잘라낸 다음 이루어진 작업으로 여기에서는 IF블럭에서도
	 * LOOP블럭을 찾아 쪼개는 작업을 한다.
	 */
	private void splitLoopBlock ()
	{
		int startIndex = this.TempleteString.length();
		int endIndex = 0;
		String code = "";

		while (startIndex > -1)
		{
			startIndex = this.TempleteString.lastIndexOf("START_CODE" + "LOOP", startIndex);
			endIndex = this.TempleteString.indexOf(Templete.END_BLOCK, startIndex);
			code = this.TempleteString.substring(startIndex + Templete.START_BLOCK.length() + 5, endIndex - 1);
			this.LoopTemplete.put(code, this.cutBlock(this.TempleteString, "LOOP", code));
		}

		final Iterator<String> keys = this.IfTemplete.keySet().iterator();
		while (keys.hasNext())
		{
			final String key = keys.next();
			final StrBuilder sb = this.IfTemplete.get(key);
			startIndex = sb.length();
			while (startIndex > -1)
			{
				startIndex = sb.lastIndexOf("START_CODE" + "LOOP", startIndex);
				endIndex = sb.indexOf(Templete.END_BLOCK, startIndex);
				code = sb.substring(startIndex + Templete.START_BLOCK.length() + 5, endIndex - 1);
				this.LoopTemplete.put(code, this.cutBlock(sb, "LOOP", code));
			}
		}
	}
}