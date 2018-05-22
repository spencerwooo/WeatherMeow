package com.spencerwoo.android.catinthebox.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.facebook.drawee.drawable.ProgressBarDrawable;

public class CircleProgressBarDrawable extends ProgressBarDrawable {
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mLevel = 0;
    private int maxLevel = 10000;
    private final RectF progressOval = new RectF();
    private final int radius = 100;

    @Override
    protected boolean onLevelChange(int level) {
        mLevel = level;
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (getHideWhenZero() && mLevel == 0) {
            return;
        }
        drawBar(canvas, maxLevel, Color.DKGRAY);
        drawBar(canvas, mLevel, Color.GRAY);
    }

    private void drawBar(Canvas canvas, int level, int color) {
        progressOval.set(canvas.getWidth() / 2 - radius, canvas.getHeight() / 2 - radius,
                canvas.getWidth() / 2 + radius, canvas.getHeight() / 2 + radius);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        if (level != 0)
            canvas.drawArc(progressOval, 0, (float) (level * 360 / maxLevel), false, mPaint);
    }
}