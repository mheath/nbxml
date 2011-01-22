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
