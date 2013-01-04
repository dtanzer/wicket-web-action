package net.davidtanzer.wicket.webbinding;

import org.apache.wicket.ajax.AjaxRequestTarget;

public abstract class BindableAction<T> {
	public void onAction(final T targetComponent) {
		onAction(targetComponent, null);
	}

	public void onAction(final T targetComponent, final AjaxRequestTarget target) {
	}
}
