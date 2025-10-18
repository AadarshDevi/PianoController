package com.alphagen.studio.pianocontroller.util;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FontFactory {
    public static Font getFont(FontWeight fontWeight) {
        return switch (fontWeight) {
            case THIN -> null;
            case EXTRA_LIGHT -> null;
            case LIGHT -> null;
            case NORMAL -> null;
            case MEDIUM -> null;
            case SEMI_BOLD ->
                    Font.loadFont(FontFactory.class.getResourceAsStream("fonts/CascadiaMono/static/CascadiaMono-SemiBold.ttf"), 15);
            case BOLD ->
                    Font.loadFont(FontFactory.class.getResourceAsStream("fonts/CascadiaMono/static/CascadiaMono-Bold.ttf"), 15);
            case EXTRA_BOLD -> null;
            case BLACK -> null;
        };
//        Font font =
//        return font;
    }
}
