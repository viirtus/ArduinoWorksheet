package ru.gubkin.lk.arduinoworksheet.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;

import java.security.InvalidParameterException;

import ru.gubkin.lk.arduinoworksheet.MainActivity;

/**
 * Created by Андрей on 08.05.2015.
 */
public class Util {
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    public static int valueToColor(float value, float minValue, float maxValue) {

        if (value < minValue || value > maxValue) {
            throw new InvalidParameterException("value mast be > minValue and < maxValue");
        }

        float normal = Math.abs(minValue);
        float normalMax = 0;

        //always 0, shift min to 0
        float normalMin = 0;

        normalMax = maxValue + normal;

        value += normal;

        float middle = normalMax / 2;
        float width = normalMax;

        int r = 0;
        int g = 0;
        int b = 0;

        if (value <= middle) {
            b = (int) (255 * (1 - value / middle));
            g = (int) (255 * (value / middle));
        } else {
            g = (int) (255 * (1 - (value / middle - 1)));
            r = (int) (255 * (value / middle - 1));
        }

        return Color.rgb(r, g, b);
    }

    public static int getActivityHeight(MainActivity activity) {
        return activity.getMainFrame().getHeight();
    }

    public static int getActivityWidth(MainActivity activity) {
        return activity.getWindow().getDecorView().getWidth();
    }
}
