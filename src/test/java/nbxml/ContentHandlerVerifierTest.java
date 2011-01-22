//*********************************************************************
//Copyright 2010 Intellectual Reserve, Inc. All rights reserved.
//This notice may not be removed.
//*********************************************************************
package nbxml;

import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author Mike Heath <heathma@ldschurch.org>
 */
@Test
public class ContentHandlerVerifierTest {

	public void callEachMethod() throws Exception {
		ContentHandlerVerifier handlerVerifier = new ContentHandlerVerifier();

		callAllMethods(handlerVerifier);

		handlerVerifier.reset();

		callAllMethods(handlerVerifier);

		handlerVerifier.done();
	}

	public void simpleError() throws Exception {
		ContentHandlerVerifier handlerVerifier = new ContentHandlerVerifier();
		handlerVerifier.startDocument();
		handlerVerifier.reset();
		try {
			handlerVerifier.endDocument();
			throw new Error("Should have thrown AssertionError");
		} catch (AssertionError e) {
			// Pass
		}
	}

	public void notDone() throws Exception {
		ContentHandlerVerifier handlerVerifier = new ContentHandlerVerifier();
		handlerVerifier.startDocument();
		handlerVerifier.reset();

		try {
			handlerVerifier.done();
			throw new Error("Should have thrown AssertionError");
		} catch (AssertionError e) {
			// Pass
		}
	}

	private void callAllMethods(ContentHandlerVerifier handlerVerifier) throws SAXException {
		handlerVerifier.startDocument();
		handlerVerifier.endDocument();
		handlerVerifier.startPrefixMapping("prefix", "uri");
		handlerVerifier.endPrefixMapping("prefix");
		handlerVerifier.startElement("uri", "localName", "qName", new AttributesImpl());
		handlerVerifier.endElement("uri", "localName", "qName");
		handlerVerifier.characters("ch".toCharArray(), 0, 2);
		handlerVerifier.ignorableWhitespace("whitespace".toCharArray(), 0, 10);
		handlerVerifier.processingInstruction("target", "data");
		handlerVerifier.skippedEntity("name");
	}

}
