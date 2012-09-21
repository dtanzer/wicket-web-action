/*
Copyright 2012 David Tanzer (david@davidtanzer.net)

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
package net.davidtanzer.wicket.webaction;

import org.apache.wicket.Component;
import org.apache.wicket.Session;

public abstract class ActionContextFactory {
	private static ActionContextFactory factory;

	public static void configureFactory(final ActionContextFactory factory) {
		if (ActionContextFactory.factory != null) {
			throw new IllegalStateException("Configure called twice! The currently configured action context factory is " + ActionContextFactory.factory +
					", you tried to configure " + factory);
		}
		ActionContextFactory.factory = factory;
	}

	public static ActionContext createActionContext(final Session session, final Component parent) {
		if (factory == null) {
			throw new IllegalStateException("This factory has not yet been configured!");
		}
		return factory.create(session, parent);
	}

	protected abstract ActionContext create(final Session session, final Component parent);
}
