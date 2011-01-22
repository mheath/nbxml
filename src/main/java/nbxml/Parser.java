//*********************************************************************
//Copyright 2010 Intellectual Reserve, Inc. All rights reserved.
//This notice may not be removed.
//*********************************************************************
package nbxml;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.XMLConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Mike Heath <heathma@ldschurch.org>
 */
public class Parser {

	private enum State {
		START, IN_ELEMENT, IN_ATTRIBUTES, IN_ATTRIBUTE_NAME, IN_ATTRIBUTE_VALUE, IN_ATTRIBUTE_VALUE_SINGLE_QUOTE, IN_ATTRIBUTE_VALUE_DOUBLE_QUOTE, IN_TEXT, IN_EMPTY_ELEMENT, CLOSED
	}

	// TODO Implement a locator
	private final Locator locator = null;
	private final ContentHandler contentHandler;
	private State state = State.START;
	private StringBuilder builder = new StringBuilder();

	private String element;
	private boolean emptyElement;
	private String attributeName;
	private AttributesImpl attributes;

	private final List<Element> elementStack = new LinkedList<Element>();

	public Parser(ContentHandler contentHandler) throws SAXException {
		this.contentHandler = contentHandler;
		contentHandler.startDocument();
	}

	public Parser parse(CharSequence csq) throws SAXException {
		for (int i = 0; i < csq.length(); i++) {
			parse(csq.charAt(i));
		}
		return this;
	}

	public Parser parse(CharSequence csq, int start, int end) throws SAXException {
		for (int i = start; i < end; i++) {
			parse(csq.charAt(i));
		}
		return this;
	}

	public Parser parse(char c) throws SAXException {
		switch (state) {
			case START:
				if (c == '<') {
				    state = State.IN_ELEMENT;
					attributes = new AttributesImpl();
				} else {
				    state = State.IN_TEXT;
					builder.append(c);
				}
				break;
			case IN_ELEMENT:
				if (c == '>') {
					setElement();
					processElement();
					setStartState();
				} else if (Character.isWhitespace(c)) {
					if (builder.length() > 0) {
						state = State.IN_ATTRIBUTES;
						setElement();
						clearBuilder();
					} else {
						throw new SAXParseException("The markup in the document preceding the root element must be well-formed.", locator);
					}
				} else {
					builder.append(c);
				}
				break;
			case IN_EMPTY_ELEMENT:
				if (c != '>') {
					throw new SAXParseException("Expected a '>' immediately following the '/'", locator);
				}
				processElement();
				setStartState();
				break;
			case IN_ATTRIBUTES:
				if (c == '>') {
					processElement();
					setStartState();
				} else if (c == '/') {
					emptyElement = true;
					state = State.IN_EMPTY_ELEMENT;
				} else if (!Character.isWhitespace(c)) {
					builder.append(c);
					state = State.IN_ATTRIBUTE_NAME;
				}
				break;
			case IN_ATTRIBUTE_NAME:
				if (c == '=') {
					state = State.IN_ATTRIBUTE_VALUE;
					attributeName = builder.toString().trim();
					clearBuilder();
				} else if (!Character.isWhitespace(c)) {
					builder.append(c);
				}
				break;
			case IN_ATTRIBUTE_VALUE:
				if (c == '"') {
					state = State.IN_ATTRIBUTE_VALUE_DOUBLE_QUOTE;
				} else if (c == '\'') {
					state = State.IN_ATTRIBUTE_VALUE_SINGLE_QUOTE;
				} else if (!Character.isWhitespace(c)) {
					throw new RuntimeException("SAX Parse exception, unexpected character '" + c + "'");
				}
				break;
			case IN_ATTRIBUTE_VALUE_DOUBLE_QUOTE:
				if (c == '"') {
					addAttribute();
				} else {
					builder.append(c);
				}
				break;
			case IN_ATTRIBUTE_VALUE_SINGLE_QUOTE:
				if (c == '\'') {
					addAttribute();
				} else {
					builder.append(c);
				}
				break;
			case IN_TEXT:
				if (c == '<') {
				    state = State.IN_ELEMENT;
					if (builder.length() > 0) {
						text(builder.toString().toCharArray());
						clearBuilder();
					}
				} else {
					builder.append(c);
				}
				break;
			case CLOSED:
				throw new SAXParseException("The parser has been closed.", locator);
		}
		return this;
	}

	private void addAttribute() throws SAXParseException {
		QName qName = new QName(attributeName);
		String uri = lookupUri(qName);
		attributes.addAttribute(uri, qName.localName, attributeName, "CDATA", builder.toString());
		clearBuilder();
		state = Parser.State.IN_ATTRIBUTES;
	}

	private String lookupUri(QName qName) throws SAXParseException {
		if (qName.prefix != null) {
			for (Element e : elementStack) {
				if (e.prefixMappings != null) {
					String uri = e.prefixMappings.get(qName.prefix);
					if (uri != null) {
						return uri;
					}
				}
			}
		}
		return "";
	}

	private void setStartState() {
		state = State.START;
		clearBuilder();
		element = null;
		emptyElement = false;
		attributes = new AttributesImpl();
	}

	private void setElement() throws SAXParseException {
		element = builder.toString().trim();
		if (element.endsWith("/")) {
			emptyElement = true;
			element = element.substring(0, element.length() - 1).trim();
		}
		if (element.length() == 0) {
			throw new SAXParseException("Element must have a name", locator);
		}
	}

	private void processElement() throws SAXException {
		if (element.startsWith("/")) {
			element = element.substring(1).trim();
			endElement();
		} else if (emptyElement) {
			startElement();
			endElement();
		} else {
			startElement();
		}
	}

	private void clearBuilder() {
		builder.delete(0, builder.length());
	}

	private void startElement() throws SAXException {
		List<QName> prefixQnames = parsePrefixMappings();
		List<String> prefixes = null;
		Map<String, String> prefixMappings = null;
		if (prefixQnames != null) {
			prefixes = new ArrayList<String>(prefixQnames.size());
			prefixMappings = new HashMap<String, String>();
			for (QName qName : prefixQnames) {
				int idx = attributes.getIndex(qName.toString());
				String uri = attributes.getValue(idx);
				String prefix = "";
				if (qName.prefix.length() > 0) {
					prefix = qName.localName;
				}
				prefixes.add(prefix);
				prefixMappings.put(prefix, uri);
				contentHandler.startPrefixMapping(prefix, uri);
				attributes.removeAttribute(idx);
			}
		}
		elementStack.add(0, new Element(element, prefixes, prefixMappings));

		QName qName = new QName(element);
		contentHandler.startElement(lookupUri(qName), qName.localName, element, attributes);
	}

	private List<QName> parsePrefixMappings() {
		List<QName> prefixMappings = null;
		for (int i = 0; i < attributes.getLength(); i++) {
			String qName = attributes.getQName(i);
			if (qName.startsWith(XMLConstants.XMLNS_ATTRIBUTE)) {
				if (prefixMappings == null) {
					prefixMappings = new LinkedList<QName>();
				}
				prefixMappings.add(new QName(qName));
			}
		}
		return prefixMappings;
	}

	private void endElement() throws SAXException {
		if (elementStack.size() == 0) {
			// Too many closing elements
			throw new SAXParseException("Closing element '" + element + "' doesn't have a starting element", locator);
		}
		Element e = elementStack.get(0);
		if (!e.element.equals(element)) {
			throw new SAXParseException("Expected closing element for '" + e.element + "' but found '" + element + "'", locator);
		}

		QName qName = new QName(element);
		contentHandler.endElement(lookupUri(qName), qName.localName, element);
		if (e.prefixes != null) {
			for (String prefix : e.prefixes) {
				try {
					contentHandler.endPrefixMapping(prefix);
				} catch (SAXException se) {
					se.printStackTrace();
				}
			}
		}
		elementStack.remove(0);
	}

	private void text(char[] chars) throws SAXException {
		contentHandler.characters(chars, 0, chars.length);
	}

	public void close() throws SAXException {
		if (elementStack.size() != 0) {
			throw new SAXParseException("No closing tag for element \"" + elementStack.get(0).element + "\"", locator);
		}
		state = State.CLOSED;
		contentHandler.endDocument();
	}

	private class Element {
		private final String element;
		private final List<String> prefixes;
		private final Map<String, String> prefixMappings;

		private Element(String element, List<String> prefixes, Map<String, String> prefixMappings) {
			this.element = element;
			this.prefixes = prefixes;
			this.prefixMappings = prefixMappings;
		}
	}

	private class QName {
		private final String prefix;
		private final String localName;

		private QName(String qName) {
			int separatorIndex = qName.indexOf(':');
			if (separatorIndex >= 0) {
				prefix = qName.substring(0, separatorIndex);
				localName = qName.substring(separatorIndex + 1);
			} else {
				prefix = "";
				localName = qName;
			}
		}

		@Override
		public String toString() {
			if (prefix == null || prefix.length() == 0) {
				return localName;
			} else {
				return prefix + ":" + localName;
			}
		}
	}
}
