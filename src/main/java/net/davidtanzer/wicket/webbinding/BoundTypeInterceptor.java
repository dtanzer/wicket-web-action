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
	
	private final Map<String, BindingInformation> bindings = new HashMap<String, BindingInformation>();
	private final Map<String, ActionBindingTarget<T>> actionBindings = new HashMap<String, ActionBindingTarget<T>>();

	BoundTypeInterceptor() {
	}

	@Override
	public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
		WebBinding.currentBoundType(this, method.getName());
		
		ActionBindingTarget<T> actionBindingTarget = (ActionBindingTarget<T>) WebBinding.removeCurrentActionBindingTarget();
		if(actionBindingTarget != null) {
			if(args.length == 0 || (AjaxRequestTarget.class.isAssignableFrom(method.getParameterTypes()[0]))) {
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
			if(args.length == 0 || (AjaxRequestTarget.class.isInstance(args[0]))) {
				if(args.length == 0) {
					actionBindingTarget.getAction().onAction((T) obj);
				} else {
					actionBindingTarget.getAction().onAction((T) obj, (AjaxRequestTarget) args[0]);
				}
				return null;
			}
		}
		
		return proxy.invokeSuper(obj, args);
	}

	public void setTarget(final String boundMethodName, final Object target, final String targetMethodName) {
		bindings.put(boundMethodName, new BindingInformation(target, targetMethodName));
	}
}