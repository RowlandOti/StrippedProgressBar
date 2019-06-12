package com.rowland.strippedprogressbar.api.traits

/*Views which implement this interface visually display ongoing progress to users*/
interface ProgressView {

    /**
     * @return the amount of progress displayed to the user
     */
    /**
     * Updates the amount of progress displayed to the user.
     *
     * @param progress a positive integer
     */
    var progress: Int

    /**
     * @return true if the view is displaying a striped pattern, otherwise false
     */
    /**
     * Sets whether the view should display a striped pattern.
     *
     * @param striped true for a striped pattern, false for a plain pattern
     */
    var isStriped: Boolean

    /**
     * @return true if the view should animate itself
     */
    /**
     * Sets whether the view should animate itself. If the view is striped, the animation will run
     * in an infinite loop; if the view is not striped, the animation will only be visible when
     * setProgress() is called.
     *
     * @param animated whether the view should animate its updates or not.
     */
    var isAnimated: Boolean

    /**
     * @return int maxProgress. Returns the maxProgress value
     */
    /**
     * Used for settings the maxprogress. Also check if Cumulative progress is smaller than the
     * max before asigning.
     *
     * @param maxProgress the maxProgress value
     */
    var maxProgress: Int

    /**
     * @return int progressBgColor. Returns the progressBgColor value
     */
    /**
     * Used for settings the progressBgColor.
     *
     * @param progressBgColor the progressBgColor value
     */
    var progressBgColor: Int

    companion object {

        val KEY_USER_PROGRESS = " com.rowland.strippedprogressbar.api.view.KEY_USER_PROGRESS"
        val KEY_DRAWN_PROGRESS = " com.rowland.strippedprogressbar.api.view.KEY_DRAWN_PROGRESS"
        val KEY_STRIPED = " com.rowland.strippedprogressbar.api.view.KEY_STRIPED"
        val KEY_ANIMATED = " com.rowland.strippedprogressbar.api.view.KEY_ANIMATED"
        val KEY_PROGRESS_BG = " com.rowland.strippedprogressbar.api.view.KEY_PROGRESS_BG"
    }
}
