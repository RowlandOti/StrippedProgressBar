package com.rowland.strippedprogressbar.api.traits

import com.rowland.strippedprogressbar.api.defaults.DefaultStrippedSize

/**
 * Classes which implement this interface allow aspects of their view to be scaled by a float factor.
 * For example, a Button may double its padding from the baseline if a factor of 2.0 is provided.
 */
interface StrippedSizeView {

	/**
	 * Retrieves the scale factor that should be used to scale a view from its baseline size.
	 * For example, specifying that a Button should use a scale factor of 2.0 may increase its
	 * padding and font size by that factor.
	 *
	 * @return the scale factor
	 */
	/**
	 * Sets the scale factor that should be used to scale a view from its baseline size.
	 * For example, specifying that a Button should use a scale factor of 2.0 may increase its
	 * padding and font size by that factor.
	 *
	 * @param bootstrapSize the scale factor
	 */
	var strippedSize: Float
	
	fun setStrippedSize(bootstrapSize: DefaultStrippedSize)
	
	companion object {
		val KEY = "com.rowland.strippedprogressbar.api.view.StrippedSizeView"
	
	}
	
}
