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
		TestClass bindable = WebBinding.bindable(TestClass.class);
		
		assertFalse(bindable.isBound());
	}
	
	@Test
	public void boundMethodShouldReturnNewValue() {
		TestClass bindable = WebBinding.bindable(TestClass.class);
		WebBinding.bind(bindable.isBound()).to(this).overrideIsBound();
		
		assertTrue(bindable.isBound());
	}
	
	@Test
	public void boundMethodWithBoxedObjectShouldReturnNewValue() {
		TestClass bindable = WebBinding.bindable(TestClass.class);
		WebBinding.bind(bindable.isBound()).to(this).overridenWithBoxedObject();
		
		assertTrue(bindable.isBound());
	}
	
	@Test(expected=IllegalStateException.class)
	public void incompatibleTypeCanNotBeBound() {
		TestClass bindable = WebBinding.bindable(TestClass.class);
		WebBinding.bind(bindable.isBound()).to(this).size();
	}
	
	public boolean overrideIsBound() {
		return true;
	}
	
	public Boolean overridenWithBoxedObject() {
		return true;
	}
	
	public int size() {
		return 0;
	}
}
