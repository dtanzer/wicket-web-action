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

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.link.Link;

/**
 * @deprecated
 * Use "webbinding" (the static methods from net.davidtanzer.wicket.webbinding.WebBinding) instead.
 */
@Deprecated
public class ActionLink<ActionContextType extends ActionContext> extends Link<Void> {
	private static final long serialVersionUID = 1L;
	private final AbstractWebAction<ActionContextType> action;
	private final ActionContextType actionContext;

	public ActionLink(final String id, final AbstractWebAction<ActionContextType> action) {
		super(id);
		this.action = action;

		this.actionContext = (ActionContextType) ActionContextFactory.createActionContext(getSession(), this);

		setEnabled(action.isEnabled());
	}

	@Override
	public void onClick() {
		action.actionPerformed(actionContext);
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
