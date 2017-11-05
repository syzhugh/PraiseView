package com.syzhugh.praiseview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by syzhu on 2017/11/5.
 */

public class PraiseView extends View {

  public static final int PRAISESTATE_NO = 0;
  public static final int PRAISESTATE_YES = 1;

  private final float c = 0.551915024494F;

  private final Paint mPaint;

  private float mRadius;
  private float mCurrentRadius;
  private float mOffset;
  private PointF mCenterPoint;

  private int mPraiseState;

  private PointF mTopPointA;
  private PointF mTopPointB;
  private PointF mTopPointC;

  private PointF mBottomPointA;
  private PointF mBottomPointB;
  private PointF mBottomPointC;

  private PointF mLeftPointA;
  private PointF mLeftPointB;
  private PointF mLeftPointC;

  private PointF mRightPointA;
  private PointF mRightPointB;
  private PointF mRightPointC;

  private ValueAnimator mAnimator;
  private int mDuration;

  public PraiseView(Context context) {
    this(context, null);
  }

  public PraiseView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PraiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mPaint = new Paint();
    mRadius = 100;
    mCurrentRadius = mRadius;
    mCenterPoint = new PointF(540, 960);
    mAnimator = new ValueAnimator();
    mDuration = 300;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.translate(mCenterPoint.x, mCenterPoint.y);
    drawHeart(canvas, mCurrentRadius);
//    showControllPoint(canvas);
  }

  private void drawHeart(Canvas canvas, float radius) {
    initControlPoint(radius);

    mPaint.setColor(mPraiseState == PRAISESTATE_NO ? Color.BLACK : Color.RED);
    mPaint.setStyle(mPraiseState == PRAISESTATE_NO ? Paint.Style.STROKE : Paint.Style.FILL);
    mPaint.setStrokeWidth(15);
    mPaint.setStrokeJoin(Paint.Join.ROUND);
    mPaint.setAntiAlias(true);
    Path path = new Path();
    path.moveTo(mTopPointB.x, mTopPointB.y);
    path.cubicTo(mTopPointC.x, mTopPointC.y, mRightPointA.x, mRightPointA.y, mRightPointB.x, mRightPointB.y);
    path.cubicTo(mRightPointC.x, mRightPointC.y, mBottomPointC.x, mBottomPointC.y, mBottomPointB.x, mBottomPointB.y);
    path.cubicTo(mBottomPointA.x, mBottomPointA.y, mLeftPointC.x, mLeftPointC.y, mLeftPointB.x, mLeftPointB.y);
    path.cubicTo(mLeftPointA.x, mLeftPointA.y, mTopPointA.x, mTopPointA.y, mTopPointB.x, mTopPointB.y);
    path.close();
    canvas.drawPath(path, mPaint);
  }

  private void showControllPoint(Canvas canvas) {
    mPaint.setColor(Color.BLACK);
    mPaint.setStrokeWidth(3);
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.FILL);
    canvas.drawPoint(mTopPointA.x, mTopPointA.y, mPaint);
    canvas.drawPoint(mTopPointB.x, mTopPointB.y, mPaint);
    canvas.drawPoint(mTopPointC.x, mTopPointC.y, mPaint);
    canvas.drawPoint(mRightPointA.x, mRightPointA.y, mPaint);
    canvas.drawPoint(mRightPointB.x, mRightPointB.y, mPaint);
    canvas.drawPoint(mRightPointC.x, mRightPointC.y, mPaint);
    canvas.drawPoint(mBottomPointA.x, mBottomPointA.y, mPaint);
    canvas.drawPoint(mBottomPointB.x, mBottomPointB.y, mPaint);
    canvas.drawPoint(mBottomPointC.x, mBottomPointC.y, mPaint);
    canvas.drawPoint(mLeftPointA.x, mLeftPointA.y, mPaint);
    canvas.drawPoint(mLeftPointB.x, mLeftPointB.y, mPaint);
    canvas.drawPoint(mLeftPointC.x, mLeftPointC.y, mPaint);
  }

  private void initControlPoint(float radius) {
    mOffset = c * radius;

    mTopPointA = new PointF(-mOffset, -radius);
    mTopPointB = new PointF(0, -radius * 0.5f);
    mTopPointC = new PointF(mOffset, -radius);

    mRightPointA = new PointF(radius, -mOffset);
    mRightPointB = new PointF(radius, 0);
    mRightPointC = new PointF(radius * 0.9f, mOffset);

    mBottomPointA = new PointF(-mOffset, radius * 0.7f);
    mBottomPointB = new PointF(0, radius);
    mBottomPointC = new PointF(mOffset, radius * 0.7f);

    mLeftPointA = new PointF(-radius, -mOffset);
    mLeftPointB = new PointF(-radius, 0);
    mLeftPointC = new PointF(-radius * 0.9f, mOffset);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mCurrentRadius = mRadius * 0.8F;
        invalidate();
        return true;
      case MotionEvent.ACTION_UP:
        mPraiseState = (mPraiseState == PRAISESTATE_NO) ? PRAISESTATE_YES : PRAISESTATE_NO;
        startViewAnimation();
        break;
      case MotionEvent.ACTION_CANCEL:
        startViewAnimation();
        break;
    }
    return super.onTouchEvent(event);
  }

  private void startViewAnimation() {
    if (mAnimator == null || mAnimator.isRunning()) {
      return;
    }
    mAnimator = ValueAnimator.ofFloat(mRadius * 0.8F, mRadius);
    mAnimator.setDuration(mDuration);
    mAnimator.setInterpolator(new OvershootInterpolator());//需要随时间匀速变化
    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        mCurrentRadius = (float) animation.getAnimatedValue();
        invalidate();
      }
    });
    mAnimator.start();
  }
}