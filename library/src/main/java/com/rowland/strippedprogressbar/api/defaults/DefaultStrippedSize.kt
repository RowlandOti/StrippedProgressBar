package com.rowland.strippedprogressbar.api.defaults

/**
 * Bootstrap provides 5 sizes - XS, SM, MD, LG, and XL. In the Android implementation the scale
 * factors used are 0.70, 0.85, 1.00, 1.30, and 1.60 respectively.
 */
enum class DefaultStrippedSize {

	XS,
	SM,
	MD,
	LG,
	XL;

	fun scaleFactor(): Float {
		when (this) {
			XS -> return 0.70f
			SM -> return 0.85f
			MD -> return 1.00f
			LG -> return 1.30f
			XL -> return 1.60f
			else -> return 1.00f
		}
	}

	companion object {

		fun fromAttributeValue(attrValue: Int): DefaultStrippedSize {
			when (attrValue) {
				0 -> return XS
				1 -> return SM
				2 -> return MD
				3 -> return LG
				4 -> return XL
				else -> return MD
			}
		}
	}

}