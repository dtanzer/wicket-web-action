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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;

public class WebBinding {
	private static class BindingInformation {
		private final BoundTypeInterceptor<?> interceptor;
		private final String methodName;
		
		public BindingInformation(final BoundTypeInterceptor<?> interceptor, final String methodName) {
			this.interceptor = interceptor;
			this.methodName = methodName;
		}
	}
	
	private static ThreadLocal<BindingInformation> currentBinding = new ThreadLocal<BindingInformation>();
	private static ThreadLocal<ActionBindingTarget<?>> currentActionBindingTarget = new ThreadLocal<ActionBindingTarget<?>>();
	private static ThreadLocal<Boolean> bindingActive = new ThreadLocal<Boolean>();
	
	public static <T> T bindable(final Class<T> superClass, final Object... constructorParameters) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(superClass);
		enhancer.setCallback(new BoundTypeInterceptor<T>());
		
		Class<?>[] argumentTypes = new Class<?>[constructorParameters.length];
		for(int i=0; i<constructorParameters.length; i++) {
			argumentTypes[i] = constructorParameters[i].getClass();
		}
		Constructor<?>[] constructors = superClass.getConstructors();
		for(Constructor<?> c : constructors) {
			if(argumentTypesCompatible(constructorParameters, c.getParameterTypes())) {
				argumentTypes = c.getParameterTypes();
				break;
			}
		}
		return (T) enhancer.create(argumentTypes, constructorParameters);
	}

	private static boolean argumentTypesCompatible(final Object[] constructorParameters, final Class<?>[] parameterTypes) {
		if(constructorParameters.length == parameterTypes.length) {
			for(int i=0; i<constructorParameters.length; i++) {
				if(!parameterTypes[i].isInstance(constructorParameters[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static BindingTarget bind(final Object bindableReturnValue) {
		return new BindingTarget(bindableReturnValue.getClass());
	}
	
	public static <T> ActionBindingTarget<T> bindAction(final BindableAction<T> action) {
		ActionBindingTarget<T> actionBindingTarget = new ActionBindingTarget<T>(action);
		currentActionBindingTarget.set(actionBindingTarget);
		return actionBindingTarget;
	}
	
	static ActionBindingTarget<?> removeCurrentActionBindingTarget() {
		ActionBindingTarget<?> actionBindingTarget = currentActionBindingTarget.get();
		currentActionBindingTarget.set(null);
		return actionBindingTarget;
	}
	
	static void currentBoundType(final BoundTypeInterceptor<?> interceptor, final String methodName) {
		currentBinding.set(new BindingInformation(interceptor, methodName));
	}
	
	static void currentBindingTarget(final TargetTypeInterceptor<?> targetInterceptor, final Method method) {
		if(bindingActive.get() != null && bindingActive.get()) {
			if(currentBinding.get() == null) {
				throw new IllegalStateException("binding activ, but no current binding found!");
			}
			currentBinding.get().interceptor.setTarget(currentBinding.get().methodName, targetInterceptor.getObject(), method.getName());
			currentBinding.set(null);
		}
		
		bindingActive.set(false);
	}

	static void activateBinding() {
		bindingActive.set(true);
	}
	
}

