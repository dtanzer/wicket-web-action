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
package net.davidtanzer.wicket.webaction;

import java.io.Serializable;

import org.apache.wicket.Session;

/**
 * @deprecated
 * Use "webbinding" (the static methods from net.davidtanzer.wicket.webbinding.WebBinding) instead.
 */
@Deprecated
public interface ActionContext extends Serializable {
	void error(String messageKey, Object... messageParams);

	void info(String messageKey, Object... messageParams);

	Session getSession();
}
