//*********************************************************************
//Copyright 2010 Intellectual Reserve, Inc. All rights reserved.
//This notice may not be removed.
//*********************************************************************
package nbxml;

import org.testng.Assert;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static nbxml.Event.Method.*;

/**
 * @author Mike Heath <heathma@ldschurch.org>
 */
public class ContentHandlerVerifier implements ContentHandler {

	private List<Event> eventQueue = new LinkedList<Event>();
	private Iterator<Event> iterator;

	private boolean verifying = false;

	public void reset() {
		verifying = true;
		iterator = eventQueue.iterator();
	}

	public void done() {
		if (!verifying) {
			Assert.fail("Not in verifying mode");
		}
		if (iterator.hasNext()) {
			Assert.fail("Not all methods were invoked");
		}
	}

	private void handleEvent(Event event) {
		if (verifying) {
			Assert.assertEquals(event, iterator.next());
		} else {
			System.out.println(event);
			eventQueue.add(event);
		}
	}

	public void setDocumentLocator(Locator locator) {
		// Ignore
	}

	public void startDocument() throws SAXException {
		handleEvent(new Event(START_DOCUMENT));
	}

	public void endDocument() throws SAXException {
		handleEvent(new Event(END_DOCUMENT));
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		handleEvent(new Event(START_PREFIX_MAPPING, prefix, uri));
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		handleEvent(new Event(END_PREFIX_MAPPING, prefix));
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		handleEvent(new Event(START_ELEMENT, uri, localName, qName, atts));
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		handleEvent(new Event(END_ELEMENT, uri, localName, qName));
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		handleEvent(new Event(CHARACTERS, ch, start, length));
	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		handleEvent(new Event(IGNORABLE_WHITESPACE, ch, start, length));
	}

	public void processingInstruction(String target, String data) throws SAXException {
		handleEvent(new Event(PROCESSING_INSTRUCTION, null, null, null, null, null, null, -1, -1, target, data, null));
	}

	public void skippedEntity(String name) throws SAXException {
		handleEvent(new Event(SKIPPED_ENTITY, null, null, null, null, null, null, -1, -1, null ,null, name));
	}
}
