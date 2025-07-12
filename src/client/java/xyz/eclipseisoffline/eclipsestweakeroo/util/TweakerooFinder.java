package xyz.eclipseisoffline.eclipsestweakeroo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TweakerooFinder {

    // Not using main logger and mod ID constant here because this class should use as little classes as possible
    public static final Logger LOGGER = LoggerFactory.getLogger("eclipsestweakeroo");

    private static Boolean tweakeroo = null;

    public static boolean hasTweakeroo() {
        if (tweakeroo == null) {
            try {
                Class.forName("fi.dy.masa.tweakeroo.Tweakeroo");
                tweakeroo = true;
                LOGGER.info("Tweakeroo compat enabled");
            } catch (ClassNotFoundException exception) {
                tweakeroo = false;
                LOGGER.info("Tweakeroo not found");
            }
        }
        return tweakeroo;
    }
}
