#Wicket WebActions#

##Include Wicket WebBinding in your project##

Using gradle, you can get WebActions and WebBinding by adding a repository and a compile time dependency:

	repositories {
		...
		maven {
			url('https://raw.github.com/dtanzer/dtanzer-mvn-repo/master/releases')
		}
	}

	dependencies {
		...
		compile group: 'net.davidtanzer.wicket', name: 'wicket-web-action', version: 'v01dr04'
	}

##How to use WebBinding##

Wicket web actions allows you to to move code that reacts to user interactions out of your page classes. Consider the following code from a page class:

	public class SomePage extends WebPage {
		public SomePage() {
			/* ... */
			Label someLabel = new Label("grandTotal", "Not Applicable");
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

The presentation model should contain all the logic that this page requires. So somehow it has to provide a mechanism for the button to call back into the presentation model. But: The button must not know about the presentation model. In MVVM this is done using so called "Commands". We can use the "BindableAction" from this library (I called it "Action" instead of "Command" because in Swing there is an Action class too). To be able to bind stuff to our components, we have to create them using the "bindable" method from the class "WebBinding":

	import static net.davidtanzer.wicket.webbinding.WebBinding.*;
	/* ... */
	
	public class SomePage extends WebPage {
		@Inject SomePagePresentationModel presentationModel;

		public SomePage {
			/* ... */
			Label someLabel = bindable(Label.class, "grandTotal", new PropertyModel<String>(presentationModel, "grandTotal"));
			bind(someLabel.isVisible()).to(presentationModel).isGrandTotalApplicable()
			add(someLabel);
	
			Button submitButton = bindable(Button.class, "submitButton");
			bindAction(presentationModel.submitAction()).when(submitButton).onSubmit();
			someForm.add(submitButton);
		}
	}

Here is a part of the presentation model, where the action is implemented:

	public class SomePagePresentationModel {
		/* ... */
		public boolean isGrandTotalApplicable() {
			return someService.shouldShowGrandTotal(...);
		}

		public String getGrandTotal() {
			return String.valueOf(someEntity.getGrandTotal());
		}

		public BindableAction<Component> submitAction() {
			return new BindableAction<Component>() {
				@Override
				public void onAction(final Component targetComponent) {
					someService.createAndSave(...)
					targetComponent.getSession().info("Your input has been saved");
					targetComponent.setResponsePage(DashboardPage.class);
				}
			};
		}
	}

And that's it. Now we have completely decoupled the page class (which creates all the components in the page) from the presentation model (which bridges the gap to the business logic).
