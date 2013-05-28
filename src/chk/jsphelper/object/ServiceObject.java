package chk.jsphelper.object;

import chk.jsphelper.object.enums.ObjectType;

public interface ServiceObject
{
	public String getDescription ();

	public String getId ();

	public ObjectType getObjectType ();
}