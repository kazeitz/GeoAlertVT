# Push Library for Android

This library is made available to build other applications that utilize the alerts push notification system.

## Available Services

The library provides two service objects that can be used to interact with the registration and alerting systems.

* **RegistrationService** - provides a service to perform, verify, and cancel the device's registration.
* **AlertService** - provides a service to fetch alert summaries and details.

## Architecture

Since all networking in Android needs to be done on background threads, the library makes heavy use of AsyncTasks.  As such, calls to the service methods do not return their results directly, but communicate using provided callbacks.  These callbacks will be notified upon completion of the task or upon error.  Examples of their use is in the example application.

## Requirements

Your implementing application needs to provide the following details:

- InputStream for the installer certificate.  This certificate must be in pkcs12 (p12) format.
- Google Cloud Messaging Sender ID - in order to receive push notifications through the Google Cloud Messaging framework, the library must now the sender ID to register the device.  This can be obtained using the Google Cloud Console and registering a project and enabling the **Google Cloud Messaging for Android** api.

