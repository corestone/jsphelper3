package chk.jsphelper;

public class Test
{
	public Test()
	{
		
	}

	public static void main (String[] args)
	{
		String ptNo = "1052881";
		for (int i = 9; i >= ptNo.length(); i--)
		{
			ptNo = "0" + ptNo;
		}
		System.out.println(ptNo);
	}

}
