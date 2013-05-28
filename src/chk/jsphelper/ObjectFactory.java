package chk.jsphelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import chk.jsphelper.object.DataSource;
import chk.jsphelper.object.Excel;
import chk.jsphelper.object.Mail;
import chk.jsphelper.object.Message;
import chk.jsphelper.object.ServiceObject;
import chk.jsphelper.object.Servlet;
import chk.jsphelper.object.Sql;
import chk.jsphelper.object.Transaction;
import chk.jsphelper.object.Upload;
import chk.jsphelper.object.enums.ObjectType;
import chk.jsphelper.util.StringUtil;

/**
 * @author Corestone
 */
public class ObjectFactory
{
	private static final Map<String, DataSource> dataSource = new HashMap<String, DataSource>();
	private static final Map<String, Excel> excel = new HashMap<String, Excel>();
	private static final Map<String, Mail> mail = new HashMap<String, Mail>();
	private static final Map<String, Message> message = new HashMap<String, Message>();
	private static final Map<String, Servlet> servlet = new HashMap<String, Servlet>();
	private static final Map<String, Sql> sql = new HashMap<String, Sql>();
	private static final Map<String, Transaction> transaction = new HashMap<String, Transaction>();
	private static final Map<String, Upload> upload = new HashMap<String, Upload>();

	/**
	 * 현재 해당 타입의 오브젝트에 특정키에 해당하는 오브젝트가 존재하는지에 대한 결과를 반환하는 메소드이다.
	 * 
	 * @param type
	 *            - 존재여부를 판단할 오브젝트의 타입
	 * @param id
	 *            - 찾을 오브젝트의 ID값
	 * @return - 존재하면 true 없으면 false를 반환함
	 */
	public static boolean existsID (final ObjectType type, final String id)
	{
		if ((type == null) || StringUtil.isNullOrEmpty(id))
		{
			return false;
		}
		return ObjectFactory.mappingObjectTypes(type).containsKey(id);
	}

	/**
	 * @param objectID
	 * @return - 해당 아이디에 해당하는 DataSource 인터페이스
	 */
	public static DataSource getDataSource (final String objectID)
	{
		if (!ObjectFactory.dataSource.containsKey(objectID))
		{
			return null;
		}
		return ObjectFactory.dataSource.get(objectID);
	}

	/**
	 * @param objectID
	 * @return - 해당 아이디에 해당하는 Excel 인터페이스
	 */
	public static Excel getExcel (final String objectID)
	{
		if (!ObjectFactory.excel.containsKey(objectID))
		{
			return null;
		}
		return ObjectFactory.excel.get(objectID);
	}

	/**
	 * @param objectID
	 * @return - 해당 아이디에 해당하는 Mail 인터페이스
	 */
	public static Mail getMail (final String objectID)
	{
		if (!ObjectFactory.mail.containsKey(objectID))
		{
			return null;
		}
		return ObjectFactory.mail.get(objectID);
	}

	/**
	 * @param objectID
	 * @return - 해당 아이디에 해당하는 Message 인터페이스
	 */
	public static Message getMessage (final String objectID)
	{
		if (!ObjectFactory.message.containsKey(objectID))
		{
			return null;
		}
		return ObjectFactory.message.get(objectID);
	}

	/**
	 * @param type
	 *            - 오브젝트의 Type
	 * @param objectID
	 *            - 해당 아이디에 해당하는 DataSource 인터페이스
	 * @return - 해당 오브젝트타입에서 objectID 아이디에 해당하는 오브젝트의 Super 인터페이스
	 */
	public static ServiceObject getObject (final ObjectType type, final String objectID)
	{
		if (!ObjectFactory.mappingObjectTypes(type).containsKey(objectID))
		{
			return null;
		}
		return ObjectFactory.mappingObjectTypes(type).get(objectID);
	}

	/**
	 * 특정 오브젝트의 키의 묶음을 반환하는 메소드이다.
	 * 
	 * @param type
	 *            - 오브젝트 타입값 ObjectList의 해당 퍼블릭 변수를 사용하면 된다.
	 * @return - 키들의 값이 저장된 객체
	 */
	public static Iterator<String> getObjectIDs (final ObjectType type)
	{
		return ObjectFactory.mappingObjectTypes(type).keySet().iterator();
	}

	/**
	 * @param objectID
	 * @return - 해당 아이디에 해당하는 Servlet 인터페이스
	 */
	public static Servlet getServlet (final String objectID)
	{
		if (!ObjectFactory.servlet.containsKey(objectID))
		{
			return null;
		}
		return ObjectFactory.servlet.get(objectID);
	}

	/**
	 * @param objectID
	 * @return - 해당 아이디에 해당하는 Sql 인터페이스
	 */
	public static Sql getSql (final String objectID)
	{
		if (!ObjectFactory.sql.containsKey(objectID))
		{
			return null;
		}
		return ObjectFactory.sql.get(objectID);
	}

	/**
	 * @param objectID
	 * @return - 해당 아이디에 해당하는 Transaction 인터페이스
	 */
	public static Transaction getTransaction (final String objectID)
	{
		if (!ObjectFactory.transaction.containsKey(objectID))
		{
			return null;
		}
		return ObjectFactory.transaction.get(objectID);
	}

	/**
	 * @param objectID
	 * @return - 해당 아이디에 해당하는 Upload 인터페이스
	 */
	public static Upload getUpload (final String objectID)
	{
		if (!ObjectFactory.upload.containsKey(objectID))
		{

		}
		return ObjectFactory.upload.get(objectID);
	}

	/**
	 * 해당 타입에 대한 모든 오브젝트들을 로그에 출력한다.
	 * 
	 * @param type
	 *            - ObjectList의 타입 변수들로 오브젝트의 해당 타입
	 */
	public static void printDump (final ObjectType type)
	{
		final StringBuilder sb = new StringBuilder("\n");
		final Iterator<String> element = ObjectFactory.getObjectIDs(type);
		sb.append("************************** DUMP ").append(type.getSymbol()).append(" Objects ********************************\n");
		while (element.hasNext())
		{
			final String key = element.next();
			sb.append(key).append(" : ").append(ObjectFactory.mappingObjectTypes(type).get(key)).append("\n");
		}
		sb.append("***************************************************************************\n");
		Constant.getLogger().info(sb.toString());
	}

	/**
	 * 해당 타입의 오브젝트의 키만을 로그에 출력하는 메소드이다.
	 * 
	 * @param type
	 *            - ObjectList의 타입 변수들로 오브젝트의 해당 타입
	 */
	public static void printIDs (final ObjectType type)
	{
		final List<String> key = new ArrayList<String>();
		final StringBuilder sb = new StringBuilder();
		final Iterator<String> element = ObjectFactory.getObjectIDs(type);
		sb.append(type.getSymbol()).append(" Object Key is\n");

		while (element.hasNext())
		{
			key.add(element.next());
		}
		Collections.sort(key);
		for (int i = 0, z = key.size(); i < z; i++)
		{
			sb.append("[" + key.get(i) + "]\n");
		}
		Constant.getLogger().info(sb.toString());
	}

	/**
	 * 특정 오브젝트의 갯수를 반환하는 메소드이다.
	 * 
	 * @param type
	 *            - ObjectList의 타입 변수들로 오브젝트의 해당 타입
	 * @return - 등록된 오브젝트의 갯수
	 */
	public static int size (final ObjectType type)
	{
		return ObjectFactory.mappingObjectTypes(type).size();
	}

	private static Map<String, ? extends ServiceObject> mappingObjectTypes (final ObjectType type)
	{
		switch (type)
		{
			case _DATASOURCE :
				return ObjectFactory.dataSource;
			case EXCEL :
				return ObjectFactory.excel;
			case MAIL :
				return ObjectFactory.mail;
			case MESSAGE :
				return ObjectFactory.message;
			case SERVLET :
				return ObjectFactory.servlet;
			case SQL :
				return ObjectFactory.sql;
			case TRANSACTION :
				return ObjectFactory.transaction;
			case UPLOAD :
				return ObjectFactory.upload;
			default :
				return null;
		}
	}

	/**
	 * 특정 오브젝트들을 모두 지우는 메소드이다.
	 * 
	 * @param type
	 *            - ObjectList의 타입 변수들로 오브젝트의 해당 타입
	 */
	public void clearObject (final ObjectType type)
	{
		ObjectFactory.mappingObjectTypes(type).clear();
	}

	/**
	 * 오브젝트를 등록할 때 쓰인다.
	 * 
	 * @param object
	 *            - 등록할 오브젝트
	 */
	public void putObject (final ServiceObject object)
	{
		if (object.getId() == null)
		{
			return;
		}
		switch (object.getObjectType())
		{
			case _DATASOURCE :
				ObjectFactory.dataSource.put(object.getId(), (DataSource) object);
				break;
			case EXCEL :
				ObjectFactory.excel.put(object.getId(), (Excel) object);
				break;
			case MAIL :
				ObjectFactory.mail.put(object.getId(), (Mail) object);
				break;
			case MESSAGE :
				ObjectFactory.message.put(object.getId(), (Message) object);
				break;
			case SERVLET :
				ObjectFactory.servlet.put(object.getId(), (Servlet) object);
				break;
			case SQL :
				ObjectFactory.sql.put(object.getId(), (Sql) object);
				break;
			case TRANSACTION :
				ObjectFactory.transaction.put(object.getId(), (Transaction) object);
				break;
			case UPLOAD :
				ObjectFactory.upload.put(object.getId(), (Upload) object);
				break;
		}
	}

	/**
	 * 특정 오브젝트에서 해당 키를 가지고 있는 하나의 오브젝트를 삭제하는 메소드이다.
	 * 
	 * @param type
	 *            - ObjectList의 타입 변수들로 오브젝트의 해당 타입
	 * @param id
	 *            - 삭제할 오브젝트의 id
	 * @return - 삭제되는 오브젝트
	 */
	public Object removeObject (final ObjectType type, final String id)
	{
		return ObjectFactory.mappingObjectTypes(type).remove(id);
	}
}