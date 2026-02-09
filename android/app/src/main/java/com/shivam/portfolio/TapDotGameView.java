package com.shivam.portfolio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class TapDotGameView extends View {

    public interface OnScoreChangeListener {
        void onScoreChanged(int score);
    }

    private final Paint dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();

    private float dotX;
    private float dotY;
    private float vx = 8f;
    private float vy = 7f;
    private float radius = 26f;
    private int score = 0;
    private float speedScale = 1f;
    private boolean isGameActive = true;
    private OnScoreChangeListener scoreChangeListener;

    public TapDotGameView(Context context) {
        super(context);
        init();
    }

    public TapDotGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TapDotGameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dotPaint.setColor(Color.parseColor("#2EC4B6"));
        glowPaint.setColor(Color.parseColor("#8838BDF8"));
    }

    public void setOnScoreChangeListener(OnScoreChangeListener listener) {
        this.scoreChangeListener = listener;
    }

    public void resetGameState() {
        score = 0;
        speedScale = 1f;
        vx = random.nextBoolean() ? 8f : -8f;
        vy = random.nextBoolean() ? 7f : -7f;
        isGameActive = true;
        if (getWidth() > 0 && getHeight() > 0) {
            dotX = radius + random.nextFloat() * (getWidth() - 2 * radius);
            dotY = radius + random.nextFloat() * (getHeight() - 2 * radius);
        }
        invalidate();
    }

    public void setGameActive(boolean active) {
        isGameActive = active;
        if (active) {
            invalidate();
        }
    }

    public void increaseDifficultyStep() {
        speedScale = Math.min(2.5f, speedScale + 0.12f);
        vx = (vx >= 0 ? 1 : -1) * Math.max(4f, Math.abs(vx)) * 1.08f;
        vy = (vy >= 0 ? 1 : -1) * Math.max(4f, Math.abs(vy)) * 1.08f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dotX = w * 0.4f;
        dotY = h * 0.5f;
        radius = Math.max(22f, Math.min(w, h) * 0.09f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isGameActive) {
            updateDotPosition();
            canvas.drawCircle(dotX, dotY, radius * 1.4f, glowPaint);
            canvas.drawCircle(dotX, dotY, radius, dotPaint);
            postInvalidateOnAnimation();
        }
    }

    private void updateDotPosition() {
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        dotX += vx;
        dotY += vy;

        if (dotX - radius < 0 || dotX + radius > getWidth()) {
            vx = -vx;
            dotX = clamp(dotX, radius, getWidth() - radius);
        }
        if (dotY - radius < 0 || dotY + radius > getHeight()) {
            vy = -vy;
            dotY = clamp(dotY, radius, getHeight() - radius);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isGameActive) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float dx = event.getX() - dotX;
            float dy = event.getY() - dotY;
            if ((dx * dx) + (dy * dy) <= radius * radius) {
                score++;
                if (scoreChangeListener != null) {
                    scoreChangeListener.onScoreChanged(score);
                }
                boostSpeedAndReposition();
                return true;
            }
        }
        return true;
    }

    private void boostSpeedAndReposition() {
        float speedX = (6f + random.nextFloat() * 3f) * speedScale;
        float speedY = (5f + random.nextFloat() * 3f) * speedScale;
        vx = (random.nextBoolean() ? 1 : -1) * speedX;
        vy = (random.nextBoolean() ? 1 : -1) * speedY;

        if (getWidth() > 2 * radius && getHeight() > 2 * radius) {
            dotX = radius + random.nextFloat() * (getWidth() - 2 * radius);
            dotY = radius + random.nextFloat() * (getHeight() - 2 * radius);
        }
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
