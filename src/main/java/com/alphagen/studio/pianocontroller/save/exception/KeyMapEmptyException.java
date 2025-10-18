package com.alphagen.studio.pianocontroller.save.exception;

public class KeyMapEmptyException extends Exception {
    public KeyMapEmptyException() {
        super("The KeyMap has no piano key ids");
    }
}
