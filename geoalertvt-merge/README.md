push-android-library
====================

This repository contains both the Android library, as well as a demo application that implements the library.

## Importing into Eclipse

Once cloned on to your local filesystem, do the following:

1. In Eclipse, go to **File** -> **Import**.
2. Under **Android**, select **Existing Android Code into Workspace**.
3. For the **Root Directory** value, navigate to the top-level of this repository.  You should see both the library and demo app show up in the list of projects.
4. Click **Finish**.

**NOTE:** Once the project is imported, you WILL have a build error, as the installer certificate cannot be located (it's purposely left out of the repository).  You will need to deposit your p12 installer certificate into the res/raw folder of the demo app.  Then, everything will be happy.

## Running the Application

Should be as simple as plugging in your phone, installing the app, and running it (assuming you have the installer certificate installed in your app's directory).

