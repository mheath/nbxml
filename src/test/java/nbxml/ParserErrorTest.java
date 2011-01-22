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

import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


@Test
public class ParserErrorTest {

	public void emptyElement() throws Exception {
		Parser parser = new Parser(new DefaultHandler());
		try {
			parser.parse("<>");
			Assert.fail("SAXParserException should have been thrown");
		} catch (SAXParseException e) {
			// Pass
		}
	}

	public void missingCloseTag() throws Exception {
		Parser parser = new Parser(new DefaultHandler());
		parser.parse("<hello>");
		try {
			parser.close();
			Assert.fail("SAXParserExceptoin should have been thrown indicating the tag has not been closed.");
		} catch (SAXParseException e) {
			// Pass
		}
	}

}
