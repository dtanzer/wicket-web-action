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
