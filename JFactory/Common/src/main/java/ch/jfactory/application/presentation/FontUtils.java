package ch.jfactory.application.presentation;

import ch.jfactory.reflection.ReflectionUtils;
import java.awt.Font;

/**
 * Todo: document $Author: daniel_frey $ $Revision: 1.1 $
 */
public class FontUtils {
    public static int parseFontStyle(final String name) {
        return parseFontStyle(name, Font.PLAIN);
    }

    public static int parseFontStyle(final String name, final int defaultFontStyle) {
        final Object ret = ReflectionUtils.getField(name, new Integer(defaultFontStyle), Font.class, int.class);
        return ((Integer) ret).intValue();
    }

    public static void main(final String[] args) {
        System.out.println(parseFontStyle("plain"));
        System.out.println(parseFontStyle("BOLD"));
        System.out.println(parseFontStyle("ITALIC"));
    }
}
