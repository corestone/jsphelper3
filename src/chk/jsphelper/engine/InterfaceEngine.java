package chk.jsphelper.engine;

import java.util.Map;

/**
 * @author Corestone
 */
abstract public interface InterfaceEngine
{
	/**
	 * @throws Exception
	 */
	public void execute () throws Exception;

	/**
	 * @return - 실행엔진에서 나온 결과값 Map 오브젝트
	 */
	public Map<String, Object> getValueObject ();
}