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

import java.util.Collection;

import net.davidtanzer.wicket.webaction.ActionContext;
import net.davidtanzer.wicket.webbinding.BindableAction;
import net.davidtanzer.wicket.webbinding.WebBinding;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.ILogData;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

public class BindVoidMethodToAction {
	public class MyActionContext implements ActionContext {
		@Override
		public void error(final String messageKey, final Object... messageParams) {
		}

		@Override
		public void info(final String messageKey, final Object... messageParams) {
		}

		@Override
		public Session getSession() {
			return null;
		}
	}

	public static class TestClass {
		public void onSubmit() {
			
		}
		
		public void onAjax(final AjaxRequestTarget target) {
			
		}
	}

	protected TestClass context;
	protected AjaxRequestTarget target;
	
	@Test
	public void boundActionWithNoParameterShouldExecuteMethodWithoutAjaxContext() {
		TestClass bindable = WebBinding.bindable(TestClass.class);
		
		WebBinding.bindAction(testAction()).when(bindable).onSubmit();
		context = null;
		target = null;
		bindable.onSubmit();
		
		assertNotNull(context);
		assertNull(target);
	}
	
	@Test
	public void boundActionWithAjaxParameterShouldExecuteMethodWithAjaxContext() {
		TestClass bindable = WebBinding.bindable(TestClass.class);
		
		WebBinding.bindAction(testAction()).when(bindable).onAjax(null);
		context = null;
		target = null;
		bindable.onAjax(new AjaxRequestTarget() {
			@Override
			public Integer getPageId() {
				return null;
			}

			@Override
			public boolean isPageInstanceCreated() {
				return false;
			}

			@Override
			public Integer getRenderCount() {
				return null;
			}

			@Override
			public Class<? extends IRequestablePage> getPageClass() {
				return null;
			}

			@Override
			public PageParameters getPageParameters() {
				return null;
			}

			@Override
			public void respond(final IRequestCycle requestCycle) {
			}

			@Override
			public void detach(final IRequestCycle requestCycle) {
			}

			@Override
			public ILogData getLogData() {
				return null;
			}

			@Override
			public void add(final Component component, final String markupId) {
			}

			@Override
			public void add(final Component... components) {
			}

			@Override
			public void addChildren(final MarkupContainer parent, final Class<?> childCriteria) {
			}

			@Override
			public void addListener(final IListener listener) {
			}

			@Override
			public void appendJavaScript(final CharSequence javascript) {
			}

			@Override
			public void prependJavaScript(final CharSequence javascript) {
			}

			@Override
			public void registerRespondListener(final ITargetRespondListener listener) {
			}

			@Override
			public Collection<? extends Component> getComponents() {
				return null;
			}

			@Override
			public void focusComponent(final Component component) {
			}

			@Override
			public IHeaderResponse getHeaderResponse() {
				return null;
			}

			@Override
			public String getLastFocusedElementId() {
				return null;
			}

			@Override
			public Page getPage() {
				return null;
			}
		});
		
		assertNotNull(context);
		assertNotNull(target);
	}
	
	public BindableAction<TestClass> testAction() {
		return new BindableAction<TestClass>() {
			@Override
			public void onAction(final TestClass context) {
				BindVoidMethodToAction.this.context = context;
			}
			@Override
			public void onAction(final TestClass context, final AjaxRequestTarget target) {
				BindVoidMethodToAction.this.context = context;
				BindVoidMethodToAction.this.target = target;
			}
		};
	}
}
