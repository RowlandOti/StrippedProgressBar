package com.rowland.strippedprogressbar

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.rowland.strippedprogressbar.api.traits.ProgressView
import com.rowland.strippedprogressbar.api.traits.RoundableView

/**
 * StrippedProgressBarGroups are a LinearLayout which exclusively holds StrippedProgressBars in a horizontal orientation.
 * This can be used to create the effect of stacked progress bars
 *
 * Each child will have there weight and max progress set to there progress. An empty progressbar emptyProgressBar will then be added to the end of layout if the bar is not full.
 */
class StrippedProgressBarGroup : StrippedGroup, ProgressView, RoundableView {
	
	private var cumulativeProgress: Int = 0
	private val emptyProgressBar = StrippedProgressBar(context)
	private var sizeOrdinal: Int = 0
	
	/**
	 * This will only be true if setStriped(true) was called
	 * @return striped true for a striped pattern, false for a plain pattern
	 */
	/**
	 * This will set all children to striped.
	 * @param striped true for a striped pattern, false for a plain pattern
	 */
	override var isStriped = false
		set(striped) {
			field = striped
			for (i in 0 until childCount) {
				retrieveChild(i).isStriped = striped
			}
		}
	
	
	/**
	 *
	 * @return int maxProgress. Returns the maxProgress value
	 */
	/**
	 * Used for settings the maxprogress. Also check if Cumulative progress is smaller than the max
	 * before asigning, see [.checkCumulativeSmallerThanMax].
	 * @param maxProgress the maxProgress value
	 */
	override var maxProgress: Int
		get() = maxProgress
		set(maxProgress) {
			checkCumulativeSmallerThanMax(maxProgress, cumulativeProgress)
			this.maxProgress = maxProgress
		}
	
	
	private var isEmptyBeingAdded = false
	private var rounded: Boolean = false
	/**
	 * This will only be true if setAnimated(true) was called
	 * @return animated if all children have been set to be animated (through the Group)
	 */
	/**
	 *
	 * @param animated whether the view should animate its updates or not.
	 */
	override var isAnimated: Boolean = false
		set(animated) {
			field = animated
			for (i in 0 until childCount) {
				retrieveChild(i).isAnimated = animated
			}
		}
	
	private val defultlayoutParams: LinearLayout.LayoutParams
		get() {
			val height =
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics)
					.toInt()
			
			return LayoutParams(height, ViewGroup.LayoutParams.WRAP_CONTENT)
		}
	
	/**
	 *
	 * @return a boolean weather the progressbarGroup will have rounded edges
	 */
	/**
	 *
	 * @param rounded if it should display rounded corners. true will round the corners, false wont
	 */
	override var isRounded: Boolean
		get() = rounded
		set(rounded) {
			this.rounded = rounded
			updateBootstrapGroup()
		}
	
	override var progress: Int
		get() = throw IllegalStateException("This method not applicable for type StrippedProgressBarGroup")
		set(progress) = throw IllegalStateException("This method not applicable for type StrippedProgressBarGroup")
	
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
	
	override fun initialise(attrs: AttributeSet?) {
		val a = context.obtainStyledAttributes(attrs, R.styleable.StrippedProgressBarGroup)
		
		try {
			this.maxProgress =
				a.getInt(R.styleable.StrippedProgressBarGroup_pMaxProgress, 100)
			this.sizeOrdinal = a.getInt(R.styleable.StrippedProgressBarGroup_pSize, 2)
			this.rounded = a.getBoolean(R.styleable.StrippedProgressBarGroup_pRoundedCorners, false)
		} finally {
			a.recycle()
		}
		
		orientation = LinearLayout.HORIZONTAL
		updateBootstrapGroup()
	}
	
	override fun onBootstrapViewAdded() {
		addEmptyProgressBar()
		
		updateBootstrapGroup()
	}
	
	override fun onBootstrapViewRemoved() {
		addEmptyProgressBar()
		
		updateBootstrapGroup()
	}
	
	/**
	 * This looks for instances of emptyProgressBar and removes them if they are not at the end and then adds one at the end if its needed.
	 */
	private fun addEmptyProgressBar() {
		var whereIsEmpty = -1
		for (i in 0 until childCount) {
			if (retrieveChild(i) != null && retrieveChild(i) == emptyProgressBar) {
				whereIsEmpty = i
			}
		}
		
		if (whereIsEmpty != childCount - 1) {
			if (whereIsEmpty != -1) {
				//the flowing true/false is to stop empty progressbar being added more than once as removeView and addView indirectly call this method
				isEmptyBeingAdded = true
				removeView(emptyProgressBar)
				isEmptyBeingAdded = false
			}
			if (!isEmptyBeingAdded) {
				addView(emptyProgressBar)
			}
		}
	}
	
	override fun updateBootstrapGroup() {
		if (childCount == 0) {
			return
		}
		
		cumulativeProgress = getCumulativeProgress()
		
		val numChildren = childCount
		for (i in 0 until numChildren) {
			val layoutParams = defultlayoutParams
			layoutParams.weight = retrieveChild(i).progress.toFloat()
			retrieveChild(i).layoutParams = layoutParams
			
			retrieveChild(i).maxProgress = retrieveChild(i).progress
			retrieveChild(i).strippedSize = sizeOrdinal.toFloat()
			
			retrieveChild(i).isRounded = rounded
			retrieveChild(i).setCornerRounding(false, false)
		}
		//this means that rounded corners will display correctly by telling only the first child to draw the left edge as rounded and only the last to draw right edge as rounded
		retrieveChild(0).setCornerRounding(true, false)
		retrieveChild(numChildren - 1).setCornerRounding(false, true)
		
		//update empty progressbar attributes
		val layoutParams = defultlayoutParams
		layoutParams.weight = maxProgress.toFloat() - cumulativeProgress
		emptyProgressBar.layoutParams = layoutParams
		emptyProgressBar.maxProgress = maxProgress - cumulativeProgress
		emptyProgressBar.strippedSize = sizeOrdinal.toFloat()
		
		weightSum = maxProgress.toFloat()
	}
	
	/**
	 * This get the total progress of all the children
	 * @return the CumulativeProgress i.e. the total progress of all children
	 */
	fun getCumulativeProgress(): Int {
		val numChildren = childCount
		var total = 0
		for (i in 0 until numChildren) {
			total += getChildProgress(i)
		}
		checkCumulativeSmallerThanMax(maxProgress, total)
		return total
	}
	
	private fun checkCumulativeSmallerThanMax(max: Int, cumulative: Int) {
		val builder = StringBuilder()
		builder.append("Max Progress Cant be smaller than cumulative progress. Max = ")
		builder.append(max)
		builder.append(", cumlative = ")
		builder.append(cumulative)
		builder.append(". \n")
		for (i in 0 until childCount) {
			builder.append("Child ").append(i).append(" has progress ").append(getChildProgress(i))
		}
		if (max < cumulative) {
			throw IllegalStateException(builder.toString())
			
		}
		
	}
	
	private fun getChildProgress(i: Int): Int {
		return retrieveChild(i).progress
	}
	
	private fun retrieveChild(i: Int): StrippedProgressBar {
		val view = getChildAt(i)
		
		return view as? StrippedProgressBar
			?: throw IllegalStateException("All child view of BootstrapProgressBarGroup must be BootstrapProgressBar")
	}
	
	/**
	 * this should be called by all children to notify the BootstrapProgressBarGroup that there progress has changed
	 * @param bootstrapProgressBar the child View
	 */
	fun onProgressChanged(bootstrapProgressBar: StrippedProgressBar) {
		updateBootstrapGroup()
	}
}
