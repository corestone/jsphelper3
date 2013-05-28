package chk.jsphelper.service.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import chk.jsphelper.Constant;

/**
 * XML 파서 엔진이다.
 * 
 * @author Corestone H. Kang
 * @since 2.5
 */
public class XML
{
	private final static String unknownAttribute = "";

	/**
	 * XML Document에서 특정 엘리먼트의 속성 값을 검색하여 반환하는 메소드이다.
	 * 
	 * @param document
	 *            - XML Document 객체
	 * @param elementName
	 *            - 엘리먼트 명
	 * @param attrNames
	 *            - 속성 명들의 문자열 배열
	 * @return
	 */
	public static Node[] getAttributes (final Document document, final String elementName, final String[] attrNames)
	{
		final NodeList nodes = document.getElementsByTagName(elementName);

		if (nodes.getLength() < 1)
		{
			return null;
		}

		final Node firstElement = nodes.item(0);
		final NamedNodeMap nnm = firstElement.getAttributes();

		if (nnm != null)
		{
			Node[] matchNodes = new Node[attrNames.length];
			for (int i = 0; i < attrNames.length; i++)
			{
				final boolean all = attrNames[i].equalsIgnoreCase("all");
				if (all)
				{
					final int nnmLength = nnm.getLength();
					matchNodes = new Node[nnmLength];
					for (int j = 0; j < nnmLength; j++)
					{
						matchNodes[j] = nnm.item(j);
					}
					return matchNodes;
				}
				else
				{
					matchNodes[i] = nnm.getNamedItem(attrNames[i]);
					if (matchNodes[i] == null)
					{
						matchNodes[i] = document.createAttribute(attrNames[i]);
						((Attr) matchNodes[i]).setValue(XML.unknownAttribute);
					}
				}
			}
			return matchNodes;
		}
		return null;
	}

	private static Document getDocument (final DocumentBuilder db, final String urlString)
	{
		try
		{
			final URL url = new URL(urlString);

			try
			{
				final HttpURLConnection hurlc = (HttpURLConnection) url.openConnection();

				final int responseCode = hurlc.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK)
				{
					final InputStream in = hurlc.getInputStream();
					try
					{
						final Document doc = db.parse(in);
						doc.setDocumentURI(urlString);
						return doc;
					}
					catch (final SAXException saxe)
					{
						saxe.printStackTrace();
					}
				}
				else
				{
					Constant.getLogger().error("HTTP connection response != HTTP_OK");
				}
			}
			catch (final IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
		catch (final MalformedURLException murle)
		{
			murle.printStackTrace();
		}
		return null;
	}

	private Document document = null;

	private Element element = null;

	public Node[] getNodeList (Node parentNode, final String[] nodeSelector)
	{
		if ((nodeSelector == null) || nodeSelector[0].equals(""))
		{
			return null;
		}
		if (parentNode == null)
		{
			parentNode = this.document.getDocumentElement().getChildNodes().item(0);
		}

		final NodeList nl = this.document.getElementsByTagName(nodeSelector[0]);
		Node[] temp = null;
		for (int j = 0, z = nl.getLength(); j < z; j++)
		{
			final Node node = nl.item(j);
			if (nodeSelector.length > 1)
			{
				final String[] subSelector = new String[nodeSelector.length - 1];
				System.arraycopy(nodeSelector, 1, subSelector, 0, subSelector.length);
				temp = this.getNodeList(node, subSelector);
			}
		}
		return temp;
	}

	public void parsingDocument (final String urlString)
	{
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try
		{
			final DocumentBuilder db = dbf.newDocumentBuilder();
			if ((urlString != null) && urlString.equals(""))
			{
				this.document = XML.getDocument(db, urlString);
				if (this.document != null)
				{
					this.element = this.document.getDocumentElement();
					this.element.normalize();
				}
			}
		}
		catch (final ParserConfigurationException pce)
		{

		}
	}
}