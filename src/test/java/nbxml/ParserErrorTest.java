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
