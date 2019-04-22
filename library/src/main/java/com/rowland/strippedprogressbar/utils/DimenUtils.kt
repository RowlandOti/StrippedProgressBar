package com.rowland.strippedprogressbar.utils

import android.content.Context
import android.content.res.Resources

import androidx.annotation.DimenRes

/**
 * Utils class for resolving color resource values.
 */
class DimenUtils {
    
    companion object  {
        
        /**
         * Resolves a dimension resource that uses scaled pixels
         *
         * @param context the current context
         * @param sizeRes the dimension resource holding an SP value
         * @return the text size in pixels
         */
        fun pixelsFromSpResource(context: Context, @DimenRes sizeRes: Int): Float {
            val res = context.resources
            return res.getDimension(sizeRes) / res.displayMetrics.density
        }
        
        /**
         * Resolves a dimension resource that uses density-independent pixels
         *
         * @param context the current context
         * @param res     the dimension resource holding a DP value
         * @return the size in pixels
         */
        fun pixelsFromDpResource(context: Context, @DimenRes res: Int): Float {
            return context.resources.getDimension(res)
        }
        
        /**
         * Converts density-independent pixels to pixels
         *
         * @param dip the dips
         * @return size in pixels
         */
        fun dpToPixels(dip: Float): Int {
            return (dip * Resources.getSystem().displayMetrics.density).toInt()
        }
        
        /**
         * Converts pixels to density-independent pixels
         *
         * @param pixels the pixels
         * @return size in dp
         */
        fun pixelsToDp(pixels: Float): Int {
            return (pixels / Resources.getSystem().displayMetrics.density).toInt()
        }
    }
}