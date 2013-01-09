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

	public void whenProtected(final Object bindingTarget, final String methodName, final Class<?>... parameterTypes) {
		Class<? extends Object> targetClass = bindingTarget.getClass();
		Method method = findMethod(targetClass, methodName, parameterTypes);
		if(method == null) {
			method = findMethod(targetClass.getSuperclass(), methodName, parameterTypes);
			if(method == null) {
				throw new IllegalStateException("Method \""+methodName+"\" could not be found in object of class \""+targetClass.getName()+"\".");
			}
		}
		method.setAccessible(true);
		Object[] args = new Object[method.getParameterTypes().length];
		try {
			method.invoke(bindingTarget, args);
		} catch (Exception e) {
			throw new IllegalStateException("Bind action error: Could not invoke method \""+methodName+"\" on target class \""+targetClass.getName()+"\"", e);
		}
	}

	private Method findMethod(final Class<? extends Object> targetClass, final String methodName, final Class<?>... parameterTypes) {
		Method result = null;
		try {
			try {
				result = targetClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				//Do nothing: Result will be null in this case.
			}
		} catch (SecurityException e) {
			throw new IllegalStateException("Could not access method "+methodName+" in class "+targetClass.getName(), e);
		}
		return result;
	}
}
