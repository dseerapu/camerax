CameraX App with Jetpack Compose and MVVM

This Android app demonstrates capturing images using CameraX, generating zoomed versions of the images in the background, and using Jetpack Compose for the UI. It follows the MVVM design pattern and leverages popular libraries like Hilt, Coil, and WorkManager to provide a clean and modern development experience.

Features

	•	All Images Screen: Displays all the captured images.
	•	Capture Permissions Screen: Requests the user’s permission to capture images. Once captured, GenerateImagesWithZoomLevelsWorker generates multiple zoomed images (1.5x, 2.0x, 2.5x, 3.0x) and sends a notification.
	•	Settings Screen: Contains switches for enabling notifications and toggling between light and dark themes.

Design Patterns

	1.	MVVM : Implements the Model-View-ViewModel architecture.
	2.	Dependency Injection: Uses Hilt to inject dependencies across the app.
	3.	Singleton: Ensures single instances of key classes such as repositories and ViewModels.
	4.	Observer Pattern: Observes changes in data using Flow and StateFlow to reactively update the UI.
	5.	Repository Pattern: Separates data sources (e.g., CameraX, DataStore) from the business logic.

Libraries Used

	1.	Hilt: Dependency Injection framework to manage dependencies.
	2.	Coil: Image loading and caching library for loading and displaying images.
	3.	DataStore: Used for storing user preferences (e.g., theme and notification settings).
	4.	WorkManager: Handles background tasks, such as generating multiple zoom levels of captured images.
	5.	CameraX: Captures images from the camera.
	6.	Navigation: Manages screen navigation in a type-safe way with Jetpack Compose.
	7.	Jetpack Compose: UI toolkit for building the user interface declaratively.

Design Principles

	*	SOLID Principles:


Functionality

	1.	All Images Screen: Displays all the images captured from the Capture Permissions Screen.
	2.	Capture Permissions Screen:
    	•	Requests permission to capture an image.
    	•	Captures an image and generates multiple zoomed versions using GenerateImagesWithZoomLevelsWorker (1.5x, 2.0x, 2.5x, 3.0x).
    	•	Sends a notification to the notification tray:
    	•	Title: “All Images Saved”
    	•	Message: “Zoomed images have been saved successfully.”
	3.	Settings Screen: Contains two switches for enabling notifications and toggling between light and dark themes.

Permissions Needed

	1.	Camera Permission (Mandatory): Required to capture images using CameraX.
	2.	Notification Permission (Optional): Allows the app to send notifications when images are saved.

How to Use

	1.	Clone this repository.
	2.	Open the project in Android Studio.
	3.	Build and run the app on an emulator or a physical device.
	4.	Navigate between the All Images Screen, Capture Screen, and Settings Screen.

License

This project is licensed under the MIT License.

Feel free to update this README.md as needed, such as adding installation instructions, contributing guidelines, or screenshots of the app!
