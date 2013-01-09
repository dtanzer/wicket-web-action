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
package net.davidtanzer.wicket.webbinding.test.wicket.pages;

import static net.davidtanzer.wicket.webbinding.WebBinding.*;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

public class BindingPage extends WebPage {
	private static final long serialVersionUID = 1L;
	private final Label someLabel;
	private final Form<?> form;
	private final Button button;
	private final AjaxButton ajaxButton;
	private String labelValue = "old value";

	public BindingPage() {
		someLabel = bindable(Label.class, "someLabel", new PropertyModel<String>(this, "labelValue"));
		someLabel.setOutputMarkupId(true);
		add(someLabel);
		
		form = bindable(Form.class, "form");
		add(form);
		
		button = bindable(Button.class, "button");
		form.add(button);
		
		ajaxButton = bindable(AjaxButton.class, "ajaxButton");
		form.add(ajaxButton);
	}
	
	public Label getSomeLabel() {
		return someLabel;
	}
	
	public Form<?> getForm() {
		return form;
	}
	
	public Button getButton() {
		return button;
	}
	
	public AjaxButton getAjaxButton() {
		return ajaxButton;
	}

	public void setLabelValue(final String labelValue) {
		this.labelValue  = labelValue;
	}
}
