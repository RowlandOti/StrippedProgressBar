package com.rowland.strippedprogressbar.attrs

import android.content.Context

import java.io.Serializable

import androidx.annotation.ColorInt

/**
 * A Bootstrap Brand is a color which is used universally across many Bootstrap Views. An example is
 * the 'Info' Brand which colors views light blue.
 */
interface StrippedBrand : Serializable {

    @get:ColorInt
    val color: Int

    /**
     * Retrieves the color that should be used for the default fill state
     *
     * @param context the current context
     * @return the color for the current brand
     */
    @ColorInt
    fun defaultFill(context: Context): Int

    /**
     * Retrieves the color that should be used for the default border state
     *
     * @param context the current context
     * @return the color for the current brand
     */
    @ColorInt
    fun defaultEdge(context: Context): Int

    /**
     * Retrieves the text color that should be used for the default state
     *
     * @param context the current context
     * @return the color for the current brand
     */
    @ColorInt
    fun defaultTextColor(context: Context): Int

    /**
     * Retrieves the color that should be used for the active fill state
     *
     * @param context the current context
     * @return the color for the current brand
     */
    @ColorInt
    fun activeFill(context: Context): Int

    /**
     * Retrieves the color that should be used for the active border state
     *
     * @param context the current context
     * @return the color for the current brand
     */
    @ColorInt
    fun activeEdge(context: Context): Int

    /**
     * Retrieves the text color that should be used for the active state
     *
     * @param context the current context
     * @return the color for the current brand
     */
    @ColorInt
    fun activeTextColor(context: Context): Int

    /**
     * Retrieves the color that should be used for the disabled fill state
     *
     * @param context the current context
     * @return the color for the current brand
     */
    @ColorInt
    fun disabledFill(context: Context): Int

    /**
     * Retrieves the color that should be used for the disabled border state
     *
     * @param context the current context
     * @return the color for the current brand
     */
    @ColorInt
    fun disabledEdge(context: Context): Int

    /**
     * Retrieves the text color that should be used for the disabled state
     *
     * @param context the current context
     * @return the color for the current brand
     */
    @ColorInt
    fun disabledTextColor(context: Context): Int

    companion object {

        val KEY = "BootstrapBrand"
    }
}