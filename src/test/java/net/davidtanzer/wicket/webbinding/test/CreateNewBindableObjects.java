package net.davidtanzer.wicket.webbinding.test;

import static org.junit.Assert.*;
import net.davidtanzer.wicket.webbinding.WebBinding;

import org.junit.Test;

public class CreateNewBindableObjects {
	public interface TestInterface {
	}

	public static class TestClass {
		public TestClass(final String id, final TestInterface i) {
			
		}
	}
	
	@Test
	public void newBindableObjectMustNotBeNull() {
		TestClass bindableObject = WebBinding.bindable(TestClass.class, "foo", new TestInterface() {
		});
		
		assertNotNull(bindableObject);
	}
	
	@Test
	public void newBindableObjectMustBeCompatibleWithOriginalClass() {
		TestClass bindableObject = WebBinding.bindable(TestClass.class, "foo", new TestInterface() {
		});
		
		assertTrue(bindableObject instanceof TestClass);
	}
}
