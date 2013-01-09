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

import net.sf.cglib.proxy.Enhancer;

public class BindingTarget {

	private final Class<?> expectedReturnType;

	public BindingTarget(final Class<?> expectedReturnType) {
		this.expectedReturnType = expectedReturnType;
	}
	
	public <T> T to(final T object) {
		WebBinding.activateBinding();

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(object.getClass());
		enhancer.setCallback(new TargetTypeInterceptor<T>(object, expectedReturnType));
		
		return createEnhancedObject(object, enhancer);
	}
	
	private static <T> T createEnhancedObject(final T object, final Enhancer enhancer) {
		Constructor<?>[] constructors = object.getClass().getConstructors();
		Constructor<?> bestConstructor = null;
		for(Constructor<?> constructor : constructors) {
			if(bestConstructor == null) {
				bestConstructor = constructor;
			} else if(constructor.getParameterTypes().length < bestConstructor.getParameterTypes().length) {
				bestConstructor = constructor;
			}
		}
		Class<?>[] parameterTypes = bestConstructor.getParameterTypes();
		Object[] parameters = new Object[parameterTypes.length];
		for(int i=0; i<parameterTypes.length; i++) {
			Class<?> type = parameterTypes[i];
			if(String.class.isAssignableFrom(type)) {
				parameters[i] = "dummy";
			} else if(int.class.isAssignableFrom(type)) {
				parameters[i] = 0;
			} else if(long.class.isAssignableFrom(type)) {
				parameters[i] = 0L;
			} else if(double.class.isAssignableFrom(type)) {
				parameters[i] = 0.0;
			} else if(boolean.class.isAssignableFrom(type)) {
				parameters[i] = false;
			} else {
				parameters[i] = null;
			}
		}
		return (T) enhancer.create(parameterTypes, parameters);
	}
}
