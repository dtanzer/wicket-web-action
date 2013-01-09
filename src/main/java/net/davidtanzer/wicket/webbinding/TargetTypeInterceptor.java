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

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TargetTypeInterceptor<T> implements MethodInterceptor {
	private final T object;
	private final Class<?> expectedReturnType;

	TargetTypeInterceptor(final T object, final Class<?> expectedReturnType) {
		this.expectedReturnType = expectedReturnType;
		this.object = object;
	}

	@Override
	public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
		if(!(expectedReturnType.isAssignableFrom(method.getReturnType()) || isUnboxableTo(expectedReturnType, method.getReturnType()))) {
			throw new IllegalStateException("Return type of bound method "+method.getName()+" is not compatible. " +
					"Expected: "+expectedReturnType+", found: "+method.getReturnType());
		}
		WebBinding.currentBindingTarget(this, method);
		return null;
	}

	private boolean isUnboxableTo(final Class<?> expected, final Class<?> returnType) {
		return (expected.equals(Boolean.class) && returnType.equals(boolean.class))
				|| (expected.equals(Character.class) && returnType.equals(char.class))
				|| (expected.equals(Byte.class) && returnType.equals(byte.class))
				|| (expected.equals(Short.class) && returnType.equals(short.class))
				|| (expected.equals(Integer.class) && returnType.equals(int.class))
				|| (expected.equals(Long.class) && returnType.equals(long.class))
				|| (expected.equals(Float.class) && returnType.equals(float.class))
				|| (expected.equals(Double.class) && returnType.equals(double.class))
				;
	}

	public Object getObject() {
		return object;
	}
}
