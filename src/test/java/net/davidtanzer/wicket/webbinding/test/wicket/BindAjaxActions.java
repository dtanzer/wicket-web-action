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
import net.davidtanzer.wicket.webbinding.BindableAction;
import net.davidtanzer.wicket.webbinding.test.wicket.pages.BindingPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class BindAjaxActions {
	private BindingPage page;

	@Test
	public void ajaxLinkShouldReplaceLabel() {
		WicketTester tester = new WicketTester();
		page = new BindingPage();
		bindAction(submitAction()).whenProtected(page.getAjaxButton(), "onSubmit", AjaxRequestTarget.class, Form.class);
		
		tester.startPage(page);
		tester.assertModelValue("someLabel", "old value");
		tester.executeAjaxEvent("form:ajaxButton", "click");
		tester.assertModelValue("someLabel", "new value");
	}
	
	public BindableAction<AjaxButton> submitAction() {
		return new BindableAction<AjaxButton>() {
			@Override
			public void onAction(final AjaxButton targetComponent, final AjaxRequestTarget target) {
				page.setLabelValue("new value");
				target.add(page.getSomeLabel());
			}
		};
	}
}
