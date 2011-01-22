/*
 *   Copyright (c) 2011 Mike Heath.  All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
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
