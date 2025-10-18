package com.alphagen.studio.pianocontroller.log;

public class Logger {

    private final Object obj;

    public Logger(Object obj) {
        this.obj = obj;
    }

    public static Logger getLogger(Object object) {
        return new Logger(object);
    }

    public void error() {

    }

    public void log() {
        System.out.println();
    }


}
