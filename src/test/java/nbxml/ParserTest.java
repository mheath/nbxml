//*********************************************************************
//Copyright 2010 Intellectual Reserve, Inc. All rights reserved.
//This notice may not be removed.
//*********************************************************************
package nbxml;

import org.testng.annotations.Test;

/**
 * @author Mike Heath <heathma@ldschurch.org>
 */
@Test
public class ParserTest extends Base {

	public void simple() throws Exception {
		validate("simple.xml");
	}

	public void singleElement() throws Exception {
		validate("single-element.xml");
	}

	public void singleAttribute() throws Exception {
		validate("single-attribute.xml");
	}

	public void multipleAttribute() throws Exception {
		validate("multiple-attributes.xml");
	}

	public void simpleText() throws Exception {
		validate("simple-text.xml");
	}

	public void formatted() throws Exception {
		validate("formatted.xml");
	}

	public void elementWhiteSpace() throws Exception {
		validate("element-whitespace.xml");
	}

	public void xmlNameSpace() throws Exception {
		validate("xml-namespace.xml");
	}

}
