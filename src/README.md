# Piano Controller

## Information

### Author

1. Name: Aadarsh Devi

### Project Basic

1. Name: Piano Controller
2. Group: 2
3. Build: 1
4. Release Version: 0.1.0
5. Internal Version: 2.1.0.1.0

### Project Technologies
1. Programming Language: Java 23.0.1
2. GUI Library: JavaFX 23.0.1
3. Logging Library: Log4J 2.25.2

### Description

Someone I know told me how they used their piano to control their device (I think it was his PC, IDK). I got really
interested in it, so with my java skills I built this project to be able to play video games using my piano that has
been sitting in my room collecting dust.

## User Manual

### Download App

**_Note:_** _The app is **Windows** only since I was unable to run my app on macOS._

1. Go to releases and get the **latest release that does not have "-console"**.
2. Download the app and place it in a folder somewhere.
3. Create a shortcut of the **_.exe_** to desktop for easier use.
4. Connect your **_Piano's MIDI Output_** to you PC.
5. Run the app by double-clicking the shortcut or .exe.
6. If windows defender throws the blue warning box, click more info
7. Then click run anyway which is hidden in the right-side (scroll to the right).
8. The app should be running.
9. Create a save file to be able to use your piano. [Create A Save](#create-a-save)

### Create A Save

_**Note:** User must know the numbers of the keys on their piano. **Middle C has a value of 60**. Keys to the left will
decrease in values and increase to the right. Black keys are between white keys will have a value that is between the
white keys. They are always integers / whole numbers._

1. Click **_"Create"_** to create a new file.
   > A GUI will open up showing some of the Keyboard Keys like letters, numbers, numpad numbers, and Mouse Input like
   > mouse buttons, and mouse movement.
2. Go to the input fields and enter numbers. You do not have to fill in all the input fields.
3. Then click **_"Save"_** and close the Save GUI Window (It might happen automatically).

### Select a Device

1. Click **_"Select Midi Device"_** and find your piano.
2. If you are successfully able to connect to your piano, then when re-clicking **_"Select Midi Device"_**, the selected
   device must have the green light instead of the red light.

### Running App

1. Before clicking **_"Run"_**, There must be the filepath/filename of your save file.
2. And there must also be the name of the device above **_"Select Midi Device"_**
3. Click **_"Run"_** and it should show a gui for the piano.
   > If there was en error, it will be either the config file was **_not found / created_** or the selected device
   **_cannot_** transmit data.

### Different Versions

There might be 2 version of the same app.

1. PianoController_releaseVersion_platform-console
2. PianoController_releaseVersion_platform

The app with **_"-console"_** has the Terminal/CommandPrompt open when running the app. it will contain

