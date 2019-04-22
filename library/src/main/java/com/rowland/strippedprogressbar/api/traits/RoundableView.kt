package com.rowland.strippedprogressbar.api.traits

/**
 * Views which implement this interface allow the user to specify whether the view should have
 * rounded corners or not. The interpretation of what a 'rounded corner' is will differ between views.
 */
interface RoundableView {

	/**
	 * @return true if the view is displaying rounded corners, otherwise false
	 */
	/**
	 * Sets whether the view should display rounded corners or not
	 *
	 * @param rounded whether the view should be rounded
	 */
	var isRounded: Boolean

	companion object {

		val KEY = "com.rowland.strippedprogressbar.api.view.Roundable"
	}

}
