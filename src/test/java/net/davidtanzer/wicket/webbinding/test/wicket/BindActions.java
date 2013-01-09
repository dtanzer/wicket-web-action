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
import static org.junit.Assert.*;
import net.davidtanzer.wicket.webbinding.BindableAction;
import net.davidtanzer.wicket.webbinding.test.wicket.pages.BindingPage;

import org.apache.wicket.Component;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class BindActions {
	private boolean formSubmitted = false;
	
	@Test
	public void boundActionToProtectedMethodShouldChangeField() throws Exception {
		WicketTester tester = new WicketTester();
		BindingPage page = new BindingPage();
		bindAction(submitAction()).whenProtected(page.getForm(), "onSubmit");

		tester.startPage(page);
		FormTester formTester = tester.newFormTester("form");
		formTester.submit();
		assertTrue(formSubmitted);
	}
	
	@Test
	public void boundActionShouldChangeField() throws Exception {
		WicketTester tester = new WicketTester();
		BindingPage page = new BindingPage();
		bindAction(submitAction()).when(page.getButton()).onSubmit();

		tester.startPage(page);
		FormTester formTester = tester.newFormTester("form");
		formTester.submit("button");
		assertTrue(formSubmitted);
	}
	
	public BindableAction<Component> submitAction() {
		return new BindableAction<Component>() {
			@Override
			public void onAction(final Component targetComponent) {
				System.out.println("submitted");
				formSubmitted = true;
			}
		};
	}
}
