package com.rowland.strippedprogressbar.api.defaults


import android.content.Context

import com.rowland.strippedprogressbar.R
import com.rowland.strippedprogressbar.api.attrs.StrippedBrand

import androidx.annotation.ColorInt

import com.rowland.strippedprogressbar.utils.ColorUtils


enum class DefaultStrippedBrand : StrippedBrand {

	PRIMARY(R.color.stripped_brand_primary),
	SECONDARY(R.color.stripped_brand_secondary_fill, R.color.stripped_brand_secondary_text),
	REGULAR(R.color.stripped_gray_light);

	private val textColor: Int
	override val color: Int

	private constructor(color: Int) {
		this.color = color
		this.textColor = android.R.color.white
	}

	private constructor(color: Int, textColor: Int) {
		this.color = color
		this.textColor = textColor
	}

	override fun defaultTextColor(context: Context): Int {
		return ColorUtils.resolveColor(textColor, context)
	}

	override fun activeTextColor(context: Context): Int {
		return ColorUtils.resolveColor(textColor, context)
	}

	override fun disabledTextColor(context: Context): Int {
		return ColorUtils.resolveColor(textColor, context)
	}


	@ColorInt
	override fun defaultFill(context: Context): Int {
		return ColorUtils.resolveColor(color!!, context)
	}

	@ColorInt
	override fun defaultEdge(context: Context): Int {
		return ColorUtils.decreaseRgbChannels(context, color!!, ColorUtils.ACTIVE_OPACITY_FACTOR_EDGE)
	}

	@ColorInt
	override fun activeFill(context: Context): Int {
		return ColorUtils.decreaseRgbChannels(context, color!!, ColorUtils.ACTIVE_OPACITY_FACTOR_FILL)
	}

	@ColorInt
	override fun activeEdge(context: Context): Int {
		return ColorUtils.decreaseRgbChannels(
			context,
			color!!,
			ColorUtils.ACTIVE_OPACITY_FACTOR_FILL + ColorUtils.ACTIVE_OPACITY_FACTOR_EDGE
		)
	}

	@ColorInt
	override fun disabledFill(context: Context): Int {
		return ColorUtils.increaseOpacity(context, color!!, ColorUtils.DISABLED_ALPHA_FILL)
	}

	@ColorInt
	override fun disabledEdge(context: Context): Int {
		return ColorUtils.increaseOpacity(
			context,
			color!!,
			ColorUtils.DISABLED_ALPHA_FILL - ColorUtils.DISABLED_ALPHA_EDGE
		)
	}

	companion object {

		fun fromAttributeValue(attrValue: Int): DefaultStrippedBrand {
			when (attrValue) {
				0 -> return PRIMARY
				1 -> return REGULAR
				2 -> return SECONDARY
				else -> return REGULAR
			}
		}
	}
}
