package net.davidtanzer.wicket.webbinding;


public class ActionBindingTarget<T> {
	private final BindableAction<T> action;

	public ActionBindingTarget(final BindableAction<T> action) {
		this.action = action;
	}

	public <U> U when(final U bindingTarget) {
		return bindingTarget;
	}

	public BindableAction<T> getAction() {
		return action;
	}
}
