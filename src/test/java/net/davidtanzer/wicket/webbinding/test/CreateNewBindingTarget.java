package net.davidtanzer.wicket.webbinding.test;

import static org.junit.Assert.*;
import net.davidtanzer.wicket.webbinding.WebBinding;

import org.junit.Test;

public class CreateNewBindingTarget {
	public static class TestClass {
	}
	
	@Test
	public void newBindableObjectMustNotBeNull() {
		TestClass target = WebBinding.target(new TestClass());
		
		assertNotNull(target);
	}
	
	@Test
	public void newBindableObjectMustBeCompatibleWithOriginalClass() {
		TestClass target = WebBinding.target(new TestClass());
		
		assertTrue(target instanceof TestClass);
	}

}
