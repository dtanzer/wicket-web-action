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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;

/**
 * @deprecated
 * Use "webbinding" (the static methods from net.davidtanzer.wicket.webbinding.WebBinding) instead.
 */
@Deprecated
public class AjaxActionLink<ActionContextType extends ActionContext> extends AjaxLink<Void> {
	private static final long serialVersionUID = 1L;
	private final AbstractWebAction<ActionContextType> action;

	private final ActionContextType actionContext;
	private final Component[] updateComponents;

	public AjaxActionLink(final String id, final AbstractWebAction<ActionContextType> action, final Component... updateComponents) {
		super(id);
		assert action != null : "Parameter \"action\" can not be null.";

		this.action = action;
		this.actionContext = (ActionContextType) ActionContextFactory.createActionContext(getSession(), this);

		this.updateComponents = updateComponents;
		setEnabled(action.isEnabled());
	}

	@Override
	public void onClick(final AjaxRequestTarget target) {
		action.actionPerformed(actionContext);
		target.add(updateComponents);
		if (action.getRedirectPage() != null) {
			setResponsePage(action.getRedirectPage(), action.getRedirectParameters());
		}
	}

	@Override
	public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
		String captionKey = action.getCaptionKey();
		if (captionKey != null) {
			replaceComponentTagBody(markupStream, openTag, getString(captionKey));
		} else {
			super.onComponentTagBody(markupStream, openTag);
		}
	}

}
