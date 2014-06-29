# Demo Application of the Android Library

## Required Setup

You will need to place your installer certificate (in pkcs12/p12 format) into the res/raw directory.

## Application Architecture

This application tries to stay very simple by having only two activities.

### MainActivity

The MainActivity displays a UI that has buttons to register, verify, and cancel the device's registration.  For each interaction, it passes itself as the callback, as the MainActivity implements all callback interfaces.  This allows us to update the UI as needed.

In addition, the MainActivity displays the titles of the alert summaries, once fetched.  Clicking on an alert title will open more details related to that alert.

### AlertActivity

The AlertActivity receives the alert summary passed to it by the MainActivity, fetches the full details for the alert, and then updates the UI.  Again, the activity registers itself as the callback when fetching details.

## Troubleshooting

### Can't find library classes

1. Be sure you have the library included in your workspace.
2. Be sure you have the library listed as a dependent library on your application (right-click the demo app's project, go to Properties, in the Android tab, verify that the push-library is in the Library section and there is a green check-mark next to it).

### Views can't be resolved when running the application

To reduce the amount of boilerplate in fetching views and storing references, the ButterKnife library was used.  This does annotation processing to make your code cleaner, without a runtime penalty hit.  To enable that in Eclipse, do the following:

1. Right-click on your project and go to **Properties**.
2. Go to the **Java Compiler** -> **Annotation Processing** section.
3. Check **Enable project specific settings** and **Enable annotation processing**.  The **Generated source directory** should have the value __.apt_generated__ in it.
4. Now go to the **Annotation Processing** -> **Factory Path** section.
5. Click **Add JARS...** and navigate to the app's lib folder.  Select the **butterknife-[VERSION].jar** jar file.
6. Be sure to let Eclipse build again.  Should be good to go now!


