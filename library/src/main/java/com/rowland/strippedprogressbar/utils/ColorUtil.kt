package com.rowland.strippedprogressbar.utils

import android.content.Context
import android.graphics.Color
import android.os.Build

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

object ColorUtil {

    val DISABLED_ALPHA_FILL = 165
    val DISABLED_ALPHA_EDGE = 190
    val ACTIVE_OPACITY_FACTOR_FILL = 0.125f
    val ACTIVE_OPACITY_FACTOR_EDGE = 0.025f

    /**
     * Resolves a color resource.
     *
     * @param color   the color resource
     * @param context the current context
     * @return a color int
     */
    @ColorInt
    fun resolveColor(@ColorRes color: Int, context: Context): Int {
        return if (Build.VERSION.SDK_INT >= 23) {
            context.resources.getColor(color, context.theme)
        } else {
            context.resources.getColor(color)
        }
    }

    /**
     * Darkens a color by reducing its RGB channel values.
     *
     * @param context the current context
     * @param res     the color resource
     * @param percent the percent to decrease
     * @return a color int
     */
    @ColorInt
    fun decreaseRgbChannels(
        context: Context,
        @ColorRes res: Int, percent: Float
    ): Int {
        val c = resolveColor(res, context)

        // reduce rgb channel values to produce box shadow effect
        var red = Color.red(c)
        red -= (red * percent).toInt()
        red = if (red > 0) red else 0

        var green = Color.green(c)
        green -= (green * percent).toInt()
        green = if (green > 0) green else 0

        var blue = Color.blue(c)
        blue -= (blue * percent).toInt()
        blue = if (blue > 0) blue else 0

        return Color.argb(Color.alpha(c), red, green, blue)
    }

    /**
     * Lightens a color by increasing its alpha channel value
     *
     * @param context the current context
     * @param res     the color resource
     * @param alpha   the alpha to set
     * @return a color int
     */
    @ColorInt
    fun increaseOpacity(context: Context, @ColorRes res: Int, alpha: Int): Int {
        val c = resolveColor(res, context)
        return increaseOpacityFromInt(context, resolveColor(res, context), alpha)
    }

    @ColorInt
    fun increaseOpacityFromInt(context: Context, @ColorInt c: Int, alpha: Int): Int {
        return Color.argb(alpha, Color.red(c), Color.green(c), Color.blue(c))
    }

}
