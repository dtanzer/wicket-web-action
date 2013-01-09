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
package net.davidtanzer.wicket.webaction;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.ResourceModel;

/**
 * @deprecated
 * Use "webbinding" (the static methods from net.davidtanzer.wicket.webbinding.WebBinding) instead.
 */
@Deprecated
public class ActionButton<ActionContextType extends ActionContext> extends Button {
	private static final long serialVersionUID = 1L;
	private final AbstractWebAction<ActionContextType> action;

	private final ActionContextType actionContext;

	public ActionButton(final String id, final AbstractWebAction<ActionContextType> action) {
		super(id);
		assert action != null : "Parameter \"action\" can not be null.";

		this.action = action;
		this.actionContext = (ActionContextType) ActionContextFactory.createActionContext(getSession(), this);

		String captionResourceKey = action.getCaptionKey();
		if (captionResourceKey == null) {
			captionResourceKey = id;
		}
		add(new AttributeModifier("value", new ResourceModel(captionResourceKey)));
	}

	@Override
	public void onSubmit() {
		action.actionPerformed(actionContext);
		if (action.getRedirectPage() != null) {
			setResponsePage(action.getRedirectPage(), action.getRedirectParameters());
		}
	}

	@Override
	public boolean isEnabled() {
		return action.isEnabled();
	}
}
