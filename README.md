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

## Inputs

Piano Controller can mimic keyboard and mouse keys, mouse ~~scrolling and~~ movement.

## Future Updates (Alpha/Beta/Release)

_**Note:** Some of the updates might not happen. Some updates might happen in alpha or beta. And other might happen in
the release version._

1. UI Overhaul: making the ui of the app easier. Useful to make custom themes.
2. UI for creating/editing save files.
3. Remove Midi Devices without valid transmitters.
4. Add variety of PianoUI (UI Type in app)
    1. LetterKey: Square Key, a letter in a square
    2. PianoKey: Piano Key, a key that changes size depending on the key color (Black/White)
    3. GrandPiano: Piano 88 Key, A full sized PianoUI with 88 keys
5. Key History: holds the previous pressed keys in some sort of listview.
6. Create a way to save files.
7. Understand and Improve file logging with log4j (xml config file)
8. Add menubar:
    1. theme testing / set theme.
    2. Quit app
    3. create config
    4. open config
    5. edit config
    6. save config
    7. connect to midi device
    8. select ui type
    9. font sizes (can be multiplier "float fontMultiplier" )
        1. Tiny ()
        2. Small ()
        3. Normal (x1)
        4. Medium ()
        5. Large ()
        6. Extra Large ()
   > Changes the size of the ui to accommodate the new font size. It
   > can only increase in size. maybe in future it can reduce in size.
9. Rename many variables using appropriate casing and names.
10. Create a UIConstant/ThemeUI to hold all ui related constant values.
11. Create a music sheet that holds all the key presses and can be put into an app if possible to get key and its
    sound (converting it into music sheets).




