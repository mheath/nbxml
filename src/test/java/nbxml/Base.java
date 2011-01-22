//*********************************************************************
//Copyright 2010 Intellectual Reserve, Inc. All rights reserved.
//This notice may not be removed.
//*********************************************************************
package nbxml;

import org.apache.commons.io.IOUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mike Heath <heathma@ldschurch.org>
 */
public abstract class Base {

	public void validate(String resource) throws Exception {
		ContentHandlerVerifier handlerVerifier = new ContentHandlerVerifier();

		parseXml(resource, handlerVerifier);

		handlerVerifier.reset();

		Parser parser = new Parser(handlerVerifier);
		parser.parse(IOUtils.toString(getResourceAsStream(resource)));
		parser.close();

		handlerVerifier.done();
	}

	public void parseXml(String resource, ContentHandler contentHandler) throws SAXException, IOException {
		InputStream inputStream = getResourceAsStream(resource);
		InputSource inputSource = new InputSource(inputStream);
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();

		xmlReader.setContentHandler(contentHandler);
		xmlReader.parse(inputSource);
	}

	private InputStream getResourceAsStream(String resource) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
	}

}
