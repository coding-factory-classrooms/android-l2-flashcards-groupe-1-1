package com.gwesaro.mycheeseornothing;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class ProgressBarAnimation extends Animation {
    private final ProgressBar progressBar;
    private final float from;
    private final float  to;

    /**
     * ProgressBarAnimation constructor
     * @param progressBar The progressBar to anim
     * @param from Start value
     * @param to End value
     */
    public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
    }

    /**
     * Apply the transform to the progress bar
     * @param interpolatedTime Duration
     * @param t Time mapped
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
    }
}
