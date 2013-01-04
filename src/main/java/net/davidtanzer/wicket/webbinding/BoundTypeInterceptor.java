package net.davidtanzer.wicket.webbinding;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class BoundTypeInterceptor<T> implements MethodInterceptor {
	private class BindingInformation {
		private final Object target;
		private final String targetMethodName;
		
		public BindingInformation(final Object target, final String targetMethodName) {
			super();
			this.target = target;
			this.targetMethodName = targetMethodName;
		}
	}
	
	private final T object;
	private final Map<String, BindingInformation> bindings = new HashMap<String, BindingInformation>();
	private final Map<String, ActionBindingTarget<T>> actionBindings = new HashMap<String, ActionBindingTarget<T>>();

	BoundTypeInterceptor(final T object) {
		this.object = object;
	}

	@Override
	public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
		WebBinding.currentBoundType(this, method.getName());
		
		ActionBindingTarget<T> actionBindingTarget = (ActionBindingTarget<T>) WebBinding.removeCurrentActionBindingTarget();
		if(actionBindingTarget != null) {
			if(args.length == 0 || (args.length == 1 && AjaxRequestTarget.class.isAssignableFrom(method.getParameterTypes()[0]))) {
				actionBindings.put(method.getName(), actionBindingTarget);
				return null;
			} else {
				throw new IllegalStateException("The method \""+method.getName()+" is not suitable for action binding.");
			}
		}
		
		String methodName = method.getName();
		BindingInformation bindingInfo = bindings.get(methodName);
		actionBindingTarget = actionBindings.get(methodName);
		if(bindingInfo != null) {
			if(args.length != 0) {
				throw new IllegalStateException("binding of methods with arguments is not yet supported.");
			}
			Method targetMethod = bindingInfo.target.getClass().getMethod(bindingInfo.targetMethodName, new Class<?> [] {});
			return targetMethod.invoke(bindingInfo.target);
		} else if(actionBindingTarget != null) {
			if(args.length == 0 || (args.length == 1 && AjaxRequestTarget.class.isInstance(args[0]))) {
				if(args.length == 0) {
					actionBindingTarget.getAction().onAction(object);
				} else {
					actionBindingTarget.getAction().onAction(object, (AjaxRequestTarget) args[0]);
				}
				return null;
			}
		}
		return proxy.invoke(object, args);
	}

	public void setTarget(final String boundMethodName, final Object target, final String targetMethodName) {
		bindings.put(boundMethodName, new BindingInformation(target, targetMethodName));
	}
}