package net.davidtanzer.wicket.webbinding.test;

import static org.junit.Assert.*;
import net.davidtanzer.wicket.webbinding.WebBinding;

import org.junit.Test;

public class CreateNewBindableObjects {
	public static class TestClass {
		public TestClass(final String id) {
			
		}
	}
	
	@Test
	public void newBindableObjectMustNotBeNull() {
		TestClass bindableObject = WebBinding.bindable(TestClass.class, "foo");
		
		assertNotNull(bindableObject);
	}
	
	@Test
	public void newBindableObjectMustBeCompatibleWithOriginalClass() {
		TestClass bindableObject = WebBinding.bindable(TestClass.class, "foo");
		
		assertTrue(bindableObject instanceof TestClass);
	}
}
