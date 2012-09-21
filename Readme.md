#Wicket WebActions#

Wicket web actions allows you to to move code that reacts to user interactions out of your page classes. Consider the following code from a page class:

	public class SomePage extends WebPage {
		public SomePage {
			/* ... */
			Label someLabel = new Label("someLabel", "someText");
			someLabel.setVisible(false);
			add(someLabel);

			Button submitButton = new Button("submitButton") {
				@Override
				public void onSubmit() {
					someService.doSomeBusinessLogic();
				}
			};
			someForm.add(submitButton);
		}
	}

This class has 2 responsibilities: Creating all the components of the page and performing some logic on user interaction. Wouldn't it be nice if we could move the second responsibility to a different class? Well, we can. Let's add a "Presentation Model" to this page. I was inspired to use this approach by a .NET project I worked on. A common pattern for user interfaces to interact with the rest of the system in .NET is MVVM - "Model-View-ViewModel". This is Microsoft's way of implementing the "Model View Presenter" pattern.

The presentation model should contain all the logic that this page requires. So somehow it has to provide a mechanism for the button to call back into the presentation model. But: The button must not know about the presentation model. In MVVM this is done using so called "Commands". We can use the "AbstractWebAction" from this library (I called it "Action" instead of "Command" because in Swing there is an Action class too):

	public class SomePage extends WebPage {
		@Inject SomePagePresentationModel presentationModel;

		public SomePage {
			/* ... */
			Label someLabel = new Label("someLabel", new PropertyModel<String>(presentationModel, "someText")) {
				@Override public boolean isVisible() {
					return presentationModel.isSomeTextVisible();
				}
			};
			add(someLabel);
			
			ActionButton<OurActionContext> submitButton = new ActionButton<OurActionContext>("submitButton", presentationModel.getSubmitAction());
			someForm.add(submitButton);
		}
	}

The action button will call the "actionPerformed" method of its action when the button is pressed ("onSubmit"). When the action has a redirect page set, the button will redirect to this page. And the button does even more: It looks up its wicket:id in the resources and sets "value" attribute of the HTML element to the result (you can override this behavior by overriding the method "getCaptionKey" in the action class). This means that all action buttons automatically have a internationalized caption, just add the wicket:id of the button to all properties files.

Here is a part of the presentation model, where the action is implemented:

	public class SomePagePresentationModel {
		/* ... */
		public boolean isSomeTextVisible() {
			return someService.shouldShowSomeText(...);
		}

		public String getSomeText() {
			return someEntity.getSomeText();
		}

		public AbstractWebAction<OurActionContext> getSubmitAction() {
			return new AbstractWebAction<OurActionContext>() {
				@Override public void actionPerformed(final OurActionContext context) {
					if(hasErrors) {
						context.error("error.i18n.key", errorParameters);
					}

					someService.doSomeBusinessLogic();
					redirect(MainPage.class);
				}
			}
		}
	}

What's left for us now is to provide an action context for the abstract web actions:

	public class OurActionContext implements ActionContext {
		private static final long serialVersionUID = 1L;

		private final OurSession session;
		private final Component parent;

		public OurActionContext(final Session session, final Component parent) {
			this.session = (OurSession) session;
			this.parent = parent;
		}

		@Override
		public void info(final String messageKey, final Object... messageParams) {
			session.info(formatMessage(messageKey, messageParams));
		}

		@Override
		public void error(final String messageKey, final Object... messageParams) {
			session.error(formatMessage(messageKey, messageParams));
		}

		private String formatMessage(final String messageKey, final Object[] messageParams) {
			String message = parent.getString(messageKey);
			return MessageFormat.format(message, messageParams);
		}

		@Override
		public OurSession getSession() {
			return session;
		}
	}

	public class OurApplication extends WebApplication {
		static {
			ActionContextFactory.configureFactory(new ActionContextFactory() {
				@Override
				protected ActionContext create(final Session session, final Component parent) {
					return new OurActionContext(session, parent);
				}
			});
		}
	}

And that's it. Now we have completely decoupled the page class (which creates all the components in the page) from the presentation model (which bridges the gap to the business logic). So far there are three components that use this "AbstractWebAction" concept: ActionButton, ActionLink and AjaxActionButton.
