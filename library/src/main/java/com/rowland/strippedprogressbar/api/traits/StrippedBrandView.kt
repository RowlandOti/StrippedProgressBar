package com.rowland.strippedprogressbar.api.traits

import com.rowland.strippedprogressbar.api.attrs.StrippedBrand

/**
 * Views which implement this interface change their color according to the given Bootstrap Brand
 */
interface StrippedBrandView {

	/**
	 * @return the current Bootstrap Brand
	 */
	/**
	 * Changes the color of the view to match the given Bootstrap Brand
	 *
	 * @param bootstrapBrand the Bootstrap Brand
	 */
	var strippedBrand: StrippedBrand

	companion object {

		val KEY = "com.rowland.strippedprogressbar.api.view.BootstrapBrandView"
	}
	
}
