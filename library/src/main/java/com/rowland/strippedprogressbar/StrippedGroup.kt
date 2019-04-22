package com.rowland.strippedprogressbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * This is a base class that provides methods to get updates when a view is removed or added or rotated and contains abstract methods for the set up of the class.
 * @see StrippedProgressBarGroup
 */
abstract class StrippedGroup : LinearLayout {

	constructor(context: Context) : super(context) {
		initialise(null)
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		initialise(attrs)
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
		context,
		attrs,
		defStyleAttr
	) {
		initialise(attrs)
	}

	protected abstract fun initialise(attrs: AttributeSet?)

	override fun setOrientation(orientation: Int) {
		super.setOrientation(orientation)
		updateBootstrapGroup()
	}

	protected abstract fun updateBootstrapGroup()


	protected abstract fun onBootstrapViewAdded()

	protected abstract fun onBootstrapViewRemoved()

	override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
		super.addView(child, index, params)
		onBootstrapViewAdded()
	}

	override fun removeAllViews() {
		super.removeAllViews()
		onBootstrapViewRemoved()
	}

	override fun removeView(view: View) {
		super.removeView(view)
		onBootstrapViewRemoved()
	}

	override fun removeViewInLayout(view: View) {
		super.removeViewInLayout(view)
		onBootstrapViewRemoved()
	}

	override fun removeViewsInLayout(start: Int, count: Int) {
		super.removeViewsInLayout(start, count)
		onBootstrapViewRemoved()
	}

	override fun removeViewAt(index: Int) {
		val child = getChildAt(index)
		super.removeViewAt(index)
		onBootstrapViewRemoved()
	}

	override fun removeViews(start: Int, count: Int) {
		super.removeViews(start, count)
		onBootstrapViewRemoved()
	}
}