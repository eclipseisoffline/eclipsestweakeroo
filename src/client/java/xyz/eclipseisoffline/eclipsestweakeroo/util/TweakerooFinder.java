package xyz.eclipseisoffline.eclipsestweakeroo.util;

import xyz.eclipseisoffline.eclipsestweakeroo.EclipsesTweakeroo;

public class TweakerooFinder {

    private static Boolean tweakeroo = null;

    public static boolean hasTweakeroo() {
        if (tweakeroo == null) {
            try {
                Class.forName("fi.dy.masa.tweakeroo.Tweakeroo");
                tweakeroo = true;
                EclipsesTweakeroo.LOGGER.info("Tweakeroo compat enabled");
            } catch (ClassNotFoundException exception) {
                tweakeroo = false;
                EclipsesTweakeroo.LOGGER.info("Tweakeroo not found");
            }
        }
        return tweakeroo;
    }
}
