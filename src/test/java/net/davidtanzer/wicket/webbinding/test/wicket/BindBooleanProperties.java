/*
Copyright 2012-2013 David Tanzer (david@davidtanzer.net)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.davidtanzer.wicket.webbinding.test.wicket;

import static net.davidtanzer.wicket.webbinding.WebBinding.*;
import net.davidtanzer.wicket.webbinding.test.wicket.pages.BindingPage;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class BindBooleanProperties {
	@Test
	public void unboundComponentShouldBeVisible() {
		WicketTester tester = new WicketTester();
		BindingPage page = new BindingPage();
		
		tester.startPage(page);
		tester.assertVisible("someLabel");
	}
	
	@Test
	public void componentWithVisibilityBoundToFalseShouldBeInvisible() {
		WicketTester tester = new WicketTester();
		
		BindingPage page = new BindingPage();
		bind(page.getSomeLabel().isVisible()).to(this).isAlternateReality();
		
		tester.startPage(page);
		tester.assertInvisible("someLabel");
	}
	
	public boolean isAlternateReality() {
		return false;
	}
}
