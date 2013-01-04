package net.davidtanzer.wicket.webaction;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;

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
