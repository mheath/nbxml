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

import org.xml.sax.Attributes;

import java.util.Arrays;

/**
 * @author Mike Heath <heathma@ldschurch.org>
 */
public class Event {

	public enum Method {
		START_DOCUMENT, END_DOCUMENT, START_PREFIX_MAPPING, END_PREFIX_MAPPING, START_ELEMENT, END_ELEMENT, CHARACTERS,
		IGNORABLE_WHITESPACE, PROCESSING_INSTRUCTION, SKIPPED_ENTITY
	}

	private final Method method;
	private final String prefix;
	private final String uri;
	private final String localName;
	private final String qName;
	private final Attributes attributes;
	private final String text;
	private final String target;
	private final String data;
	private final String name;

	public Event(Method method) {
		this(method, null, null, null, null, null, null, -1, -1, null, null, null);
	}

	public Event(Method method, String prefix, String uri) {
		this(method, prefix, uri, null, null, null, null, -1, -1, null, null, null);
	}

	public Event(Method method, String prefix) {
		this(method, prefix, null, null, null, null, null, -1, -1, null, null, null);
	}

	public Event(Method method, String uri, String localName, String qName, Attributes attributes) {
		this(method, null, uri, localName, qName, attributes, null, -1, -1, null, null, null);
	}

	public Event(Method method, String uri, String localName, String qName) {
		this(method, null, uri, localName, qName, null, null, -1, -1, null, null, null);
	}

	public Event(Method method, char[] ch, int start, int length) {
		this(method, null, null, null, null, null, ch, start, length, null, null, null);
	}

	public Event(Method method, String prefix, String uri, String localName, String qName, Attributes attributes, char[] ch, int start, int length, String target, String data, String name) {
		this.method = method;
		this.prefix = prefix;
		this.uri = uri;
		this.localName = localName;
		this.qName = qName;
		this.attributes = attributes;
		this.text = ch == null ? null : new String(ch, start, length);
		this.target = target;
		this.data = data;
		this.name = name;
	}

	public Method getMethod() {
		return method;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Event)) return false;

		Event event = (Event) o;

		if (text != null ? !text.equals(event.text) : event.text != null) return false;
		if (!attributesEquals(attributes, event.attributes)) return false;
		if (data != null ? !data.equals(event.data) : event.data != null) return false;
		if (localName != null ? !localName.equals(event.localName) : event.localName != null) return false;
		if (method != event.method) return false;
		if (name != null ? !name.equals(event.name) : event.name != null) return false;
		if (prefix != null ? !prefix.equals(event.prefix) : event.prefix != null) return false;
		if (qName != null ? !qName.equals(event.qName) : event.qName != null) return false;
		if (target != null ? !target.equals(event.target) : event.target != null) return false;
		if (uri != null ? !uri.equals(event.uri) : event.uri != null) return false;

		return true;
	}

	private boolean attributesEquals(Attributes a, Attributes b) {
		if (a == b) return true;
		if (a == null || b == null) return false;
		if (a.getLength() != b.getLength()) return false;
		int length = a.getLength();
		for (int i = 0; i < length; i++) {
			if (!equals(a.getLocalName(i), b.getLocalName(i))) return false;
			if (!equals(a.getQName(i), b.getQName(i))) return false;
			if (!equals(a.getValue(i), b.getValue(i))) return false;
		}
		return true;
	}

	private boolean equals(String a, String b) {
		if (a == b) return true;
		if (a == null || b == null) return false;
		return a.equals(b);
	}

	@Override
	public int hashCode() {
		int result = method != null ? method.hashCode() : 0;
		result = 31 * result + (prefix != null ? prefix.hashCode() : 0);
		result = 31 * result + (uri != null ? uri.hashCode() : 0);
		result = 31 * result + (localName != null ? localName.hashCode() : 0);
		result = 31 * result + (qName != null ? qName.hashCode() : 0);
		result = 31 * result + (text != null ? text.hashCode() : 0);
		result = 31 * result + (target != null ? target.hashCode() : 0);
		result = 31 * result + (data != null ? data.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Event{" +
				"method=" + method +
				", prefix='" + prefix + '\'' +
				", uri='" + uri + '\'' +
				", localName='" + localName + '\'' +
				", qName='" + qName + '\'' +
				", attributes=" + toString(attributes) +
				", text=" + text +
				", target='" + target + '\'' +
				", data='" + data + '\'' +
				", name='" + name + '\'' +
				'}';
	}

	private String toString(Attributes attributes) {
		StringBuilder builder = new StringBuilder();
		if (attributes != null) {
			for (int i = 0; i < attributes.getLength(); i++) {
				builder.append("(")
				.append("uri: ").append(attributes.getURI(i))
				.append(", localName: ").append(attributes.getLocalName(i))
				.append(", qName: ").append(attributes.getQName(i))
				.append(", type: ").append(attributes.getType(i))
				.append(", value: ").append(attributes.getValue(i))
				.append(")");
			}
		}
		return builder.toString();
	}
}
