package net.davidtanzer.wicket.webbinding;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TargetTypeInterceptor<T> implements MethodInterceptor {
	private final T object;

	TargetTypeInterceptor(final T object) {
		this.object = object;
	}

	@Override
	public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy) throws Throwable {
		WebBinding.currentBindingTarget(this, method);
		return proxy.invoke(object, args);
	}

	public Object getObject() {
		return object;
	}
}
