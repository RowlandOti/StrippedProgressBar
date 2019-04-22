package com.rowland.strippedprogressbar;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.rowland.strippedprogressbar.api.attrs.StrippedBrand;
import com.rowland.strippedprogressbar.api.defaults.DefaultStrippedBrand;
import com.rowland.strippedprogressbar.api.defaults.DefaultStrippedSize;
import com.rowland.strippedprogressbar.api.traits.ProgressView;
import com.rowland.strippedprogressbar.api.traits.RoundableView;
import com.rowland.strippedprogressbar.api.traits.StrippedBrandView;
import com.rowland.strippedprogressbar.api.traits.StrippedSizeView;
import com.rowland.strippedprogressbar.utils.ColorUtils;
import com.rowland.strippedprogressbar.utils.DimenUtils;

import java.io.Serializable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


import static android.graphics.Bitmap.Config.ARGB_8888;

public class StrippedProgressBar extends View implements ProgressView, StrippedBrandView, StrippedSizeView, RoundableView, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private static final String TAG = StrippedProgressBar.class.getSimpleName();

    private static final long UPDATE_ANIM_MS = 300;
    private static final int STRIPE_ALPHA = 150;
    private static final long STRIPE_CYCLE_MS = 1500;

    private Paint progressPaint;
    private Paint stripePaint;
    private Paint bgPaint;
    private Paint textPaint;

    private int userProgress;
    private int drawnProgress;

    private int maxProgress;
    private int lineSpacing;

    private boolean striped;
    private boolean animated;
    private boolean rounded;

    //used for progressbarGroup so that only the currect corners will be rounded
    private boolean canRoundLeft = true;
    private boolean canRoundRight = true;

    private ValueAnimator progressAnimator;
    private Paint tilePaint;
    private final float baselineHeight = DimenUtils.Companion.pixelsFromDpResource(getContext(), R.dimen.stripped_progress_bar_height);

    private StrippedBrand bootstrapBrand;

    private Canvas progressCanvas;
    private Bitmap progressBitmap;
    //private Bitmap stripeTile;

    private float bootstrapSize;
    private boolean showPercentage;

    public StrippedProgressBar(Context context) {
        super(context);
        initialise(null);
    }

    public StrippedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(attrs);
    }

    public StrippedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(attrs);
    }

    private void initialise(AttributeSet attrs) {
        ValueAnimator.setFrameDelay(15); // attempt 60fps
        tilePaint = new Paint();

        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setAntiAlias(true);

        stripePaint = new Paint();
        stripePaint.setStyle(Paint.Style.FILL);
        stripePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setColor(ColorUtils.Companion.resolveColor(android.R.color.black, getContext()));
        textPaint.setTextSize(DimenUtils.Companion.pixelsFromSpResource(getContext(), R.dimen.stripped_progress_bar_default_font_size));

        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(ColorUtils.Companion.resolveColor(R.color.stripped_gray_light, getContext()));

        // get attributes
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StrippedProgressBar);

        try {
            this.animated = a.getBoolean(R.styleable.StrippedProgressBar_pAnimated, false);
            this.rounded = a.getBoolean(R.styleable.StrippedProgressBar_pRoundedCorners, false);
            this.striped = a.getBoolean(R.styleable.StrippedProgressBar_pStriped, false);
            this.showPercentage = a.getBoolean(R.styleable.StrippedProgressBar_pShowPercentage, false);
            this.userProgress = a.getInt(R.styleable.StrippedProgressBar_pProgress, 0);
            this.maxProgress = a.getInt(R.styleable.StrippedProgressBar_pMaxProgress, 100);
            this.lineSpacing = a.getDimensionPixelSize(R.styleable.StrippedProgressBar_pLineSpacing, 80);

            int typeOrdinal = a.getInt(R.styleable.StrippedProgressBar_pBrand, -1);
            int sizeOrdinal = a.getInt(R.styleable.StrippedProgressBar_pSize, -1);

            this.bootstrapSize = DefaultStrippedSize.Companion.fromAttributeValue(sizeOrdinal).scaleFactor();
            this.bootstrapBrand = DefaultStrippedBrand.Companion.fromAttributeValue(typeOrdinal);
            this.drawnProgress = userProgress;
        } finally {
            a.recycle();
        }

        textPaint.setColor(bootstrapBrand.defaultTextColor(getContext()));
        textPaint.setTextSize((DimenUtils.Companion.pixelsFromSpResource(getContext(), R.dimen.stripped_button_default_font_size)) * this.bootstrapSize);
        updateStrippedState();
        setProgress(this.userProgress);
        setMaxProgress(this.maxProgress);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, super.onSaveInstanceState());

        bundle.putInt(ProgressView.Companion.getKEY_USER_PROGRESS(), userProgress);
        bundle.putInt(ProgressView.Companion.getKEY_DRAWN_PROGRESS(), drawnProgress);
        bundle.putBoolean(ProgressView.Companion.getKEY_STRIPED(), striped);
        bundle.putBoolean(ProgressView.Companion.getKEY_ANIMATED(), animated);
        bundle.putBoolean(RoundableView.Companion.getKEY(), rounded);
        bundle.putFloat(StrippedSizeView.Companion.getKEY(), bootstrapSize);
        bundle.putSerializable(StrippedBrand.Companion.getKEY(), bootstrapBrand);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            Serializable brand = bundle.getSerializable(StrippedBrand.Companion.getKEY());

            if (brand instanceof StrippedBrand) {
                bootstrapBrand = (StrippedBrand) brand;
            }

            this.userProgress = bundle.getInt(ProgressView.Companion.getKEY_USER_PROGRESS());
            this.drawnProgress = bundle.getInt(ProgressView.Companion.getKEY_DRAWN_PROGRESS());
            this.striped = bundle.getBoolean(ProgressView.Companion.getKEY_STRIPED());
            this.animated = bundle.getBoolean(ProgressView.Companion.getKEY_ANIMATED());
            this.rounded = bundle.getBoolean(RoundableView.Companion.getKEY());
            this.bootstrapSize = bundle.getFloat(StrippedSizeView.Companion.getKEY());

            state = bundle.getParcelable(TAG);
        }
        super.onRestoreInstanceState(state);
        updateStrippedState();
        setProgress(userProgress);
    }

    private int getStripeColor(@ColorInt int color) {
        return Color.argb(STRIPE_ALPHA, Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * Starts an animation which moves the progress bar from one value to another, in response to
     * a call to setProgress(). Animation update callbacks allow the interpolator value to be used
     * to calculate the current progress value, which is stored in a temporary variable. The view is
     * then invalidated.
     * <p/>
     * If this method is called when a progress update animation is already running, the previous
     * animation will be cancelled, and the currently drawn progress recorded. A new animation will
     * then be started from the last drawn point.
     */
    private void startProgressUpdateAnimation() {
        clearAnimation();

        progressAnimator = ValueAnimator.ofFloat(drawnProgress, userProgress);
        progressAnimator.setDuration(UPDATE_ANIM_MS);
        progressAnimator.setRepeatCount(0);
        progressAnimator.setRepeatMode(ValueAnimator.RESTART);
        progressAnimator.setInterpolator(new DecelerateInterpolator());

        progressAnimator.addUpdateListener(this);

        // start striped animation after progress update if needed
        progressAnimator.addListener(this);
        progressAnimator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        drawnProgress = (int) ((float) animation.getAnimatedValue());
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        startStripedAnimationIfNeeded(); // start striped animation after progress update
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    /**
     * Starts an infinite animation cycle which provides the visual effect of stripes moving
     * backwards. The current system time is used to offset tiled bitmaps of the progress background,
     * producing the effect that the stripes are moving backwards.
     */
    private void startStripedAnimationIfNeeded() {
        if (!striped || !animated) {
            return;
        }

        clearAnimation();

        progressAnimator = ValueAnimator.ofFloat(0, 0);
        progressAnimator.setDuration(UPDATE_ANIM_MS);
        progressAnimator.setRepeatCount(ValueAnimator.INFINITE);
        progressAnimator.setRepeatMode(ValueAnimator.RESTART);

        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        progressAnimator.start();
    }

    /*
     * Custom Measuring/Drawing
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // restrict view to default progressbar height

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST: // prefer default height, if not all available use as much as possible
                float desiredHeight = (baselineHeight * bootstrapSize);
                height = (height > desiredHeight) ? (int) desiredHeight : height;
                break;
            default:
                height = (int) (baselineHeight * bootstrapSize);
                break;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = getWidth();
        float h = getHeight();

        if (w <= 0 || h <= 0) {
            return;
        }

        if (progressBitmap == null) {
            progressBitmap = Bitmap.createBitmap((int) w, (int) h, ARGB_8888);
        }
        if (progressCanvas == null) {
            progressCanvas = new Canvas(progressBitmap);
        }
        progressCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        float ratio = (drawnProgress / (float) maxProgress);
        int lineEnd = (int) (w * ratio);

        // draw a filled bar
        progressCanvas.drawRect(0, 0, lineEnd, h, progressPaint);

        if (striped) {
            // draw a regular striped bar
            int start = - lineEnd/2;
            while (start < lineEnd) {
                progressCanvas.drawLine(start, 0, start + lineSpacing, h, tilePaint);
                start += lineSpacing / 4;
            }
        }

        progressCanvas.drawRect(lineEnd, 0, w, h, bgPaint); // draw bg

        float corners = rounded ? h / 2 : 0;
        Bitmap round = createRoundedBitmap(progressBitmap, corners, canRoundRight, canRoundLeft);
        canvas.drawBitmap(round, 0, 0, tilePaint);

        if (showPercentage) {
            String percent = getProgress() + "%";
            int xPos = (lineEnd / 2);
            xPos = xPos - (int) (textPaint.measureText(percent) / 2);
            int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

            canvas.drawText(percent, xPos, yPos, textPaint);
        }
    }

    /**
     * Creates a rounded bitmap with transparent corners, from a square bitmap.
     * See <a href="http://stackoverflow.com/questions/4028270">StackOverflow</a>
     *
     * @param bitmap       the original bitmap
     * @param cornerRadius the radius of the corners
     * @param roundRight   if you should round the corners on the right, note that if set to true and cornerRadius == 0 it will create a square
     * @param roundLeft    if you should round the corners on the right, note that if set to true and cornerRadius == 0 it will create a square
     * @return a rounded bitmap
     */
    private static Bitmap createRoundedBitmap(Bitmap bitmap, float cornerRadius, boolean roundRight, boolean roundLeft) {
        Bitmap roundedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);

        final Paint paint = new Paint();
        final Rect frame = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final Rect leftRect = new Rect(0, 0, bitmap.getWidth() / 2, bitmap.getHeight());
        final Rect rightRect = new Rect(bitmap.getWidth() / 2, 0, bitmap.getWidth(), bitmap.getHeight());

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);

        canvas.drawRoundRect(new RectF(frame), cornerRadius, cornerRadius, paint);

        if (!roundLeft) {
            canvas.drawRect(leftRect, paint);
        }

        if (!roundRight) {
            canvas.drawRect(rightRect, paint);
        }
        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, frame, frame, paint);

        return roundedBitmap;
    }

    private void updateStrippedState() {
        int color = bootstrapBrand.defaultFill(getContext());
        progressPaint.setColor(color);
        stripePaint.setColor(getStripeColor(color));
        invalidateDrawCache();
        invalidate();
    }

    private void invalidateDrawCache() {
        progressBitmap = null;
        progressCanvas = null;
    }

    /*
     * Getters/Setters
     */


    @SuppressLint("DefaultLocale")
    @Override
    public void setProgress(int progress) {
        if (getParent() instanceof StrippedProgressBarGroup) {
            this.userProgress = 0;
            setMaxProgress(progress);
        } else {
            if (progress < 0 || progress > maxProgress) {
                throw new IllegalArgumentException(
                        String.format("Invalid value '%d' - progress must be an integer in the range 0-%d", progress, maxProgress));
            }
        }

        this.userProgress = progress;

        if (animated) {
            startProgressUpdateAnimation();
        } else {
            this.drawnProgress = progress;
            invalidate();
        }

        ViewParent parent = getParent();
        if (parent != null) {
            if (parent instanceof StrippedProgressBarGroup) {
                StrippedProgressBarGroup parentGroup = (StrippedProgressBarGroup) parent;
                parentGroup.onProgressChanged(this);
            }
        }
    }

    @Override
    public int getProgress() {
        return userProgress;
    }

    @Override
    public void setStriped(boolean striped) {
        this.striped = striped;
        invalidate();
        startStripedAnimationIfNeeded();
    }

    @Override
    public boolean isStriped() {
        return striped;
    }

    @Override
    public void setAnimated(boolean animated) {
        this.animated = animated;
        invalidate();
        startStripedAnimationIfNeeded();
    }

    @Override
    public boolean isAnimated() {
        return animated;
    }

    @Override
    public void setStrippedBrand(@NonNull StrippedBrand bootstrapBrand) {
        this.bootstrapBrand = bootstrapBrand;
        textPaint.setColor(bootstrapBrand.defaultTextColor(getContext()));
        updateStrippedState();
    }

    @NonNull
    @Override
    public StrippedBrand getStrippedBrand() {
        return bootstrapBrand;
    }

    @Override
    public void setRounded(boolean rounded) {
        this.rounded = rounded;
        updateStrippedState();
    }

    @Override
    public boolean isRounded() {
        return rounded;
    }

    @Override
    public float getStrippedSize() {
        return bootstrapSize;
    }

    @Override
    public void setStrippedSize(float bootstrapSize) {
        this.bootstrapSize = bootstrapSize;
        textPaint.setTextSize((DimenUtils.Companion.pixelsFromSpResource(getContext(), R.dimen.stripped_progress_bar_default_font_size)) * this.bootstrapSize);
        requestLayout();
        updateStrippedState();
    }

    @Override
    public void setStrippedSize(DefaultStrippedSize bootstrapSize) {
        setStrippedSize(bootstrapSize.scaleFactor());
    }

    @Override
    public int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public void setMaxProgress(int newMaxProgress) {
        if (getProgress() <= newMaxProgress) {
            maxProgress = newMaxProgress;
        } else {
            throw new IllegalArgumentException(
                    String.format("MaxProgress cant be smaller than the current progress %d<%d", getProgress(), newMaxProgress));
        }
        invalidate();
        StrippedProgressBarGroup parent = (StrippedProgressBarGroup) getParent();
    }

    void setCornerRounding(boolean left, boolean right) {
        canRoundLeft = left;
        canRoundRight = right;
    }

    boolean getCornerRoundingLeft() {
        return canRoundLeft;
    }

    boolean getCornerRoundingRight() {
        return canRoundRight;
    }
}
