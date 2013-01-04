package net.davidtanzer.wicket.webbinding.test;

import static org.junit.Assert.*;
import net.davidtanzer.wicket.webbinding.WebBinding;

import org.junit.Test;

public class BindReturnValue {
	public static class TestClass {
		public boolean isBound() {
			return false;
		}
	}
	
	@Test
	public void unboundMethodShouldReturnOriginalValue() {
		TestClass bindable = WebBinding.bindable(new TestClass());
		
		assertFalse(bindable.isBound());
	}
	
	@Test
	public void boundMethodShouldReturnNewValue() {
		TestClass bindable = WebBinding.bindable(new TestClass());
		BindReturnValue target = WebBinding.target(this);
		WebBinding.bind(bindable.isBound()).to(target.overrideIsBound());
		
		assertTrue(bindable.isBound());
	}
	
	public boolean overrideIsBound() {
		return true;
	}
}
