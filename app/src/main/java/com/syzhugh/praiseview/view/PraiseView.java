package com.syzhugh.praiseview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
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
import com.syzhugh.praiseview.R;

/**
 * Created by syzhu on 2017/11/5.
 */

public class PraiseView extends View {

  private final String TAG = "Test";

  private final float c = 0.551915024494F;

  public static final int PRAISESTATE_NO = 0;
  public static final int PRAISESTATE_YES = 1;

  private int mPraiseState;


  private final Paint mPaint;
  private float mRadius;
  private float mCurrentRadius;
  private float mOffset;
  private Path mHeartPath;
  private ValueAnimator mAnimator;

  private int mDuration;
  private int mColorPraised;
  private int mColorUnPraised;

  private PointF mTopPointA;
  private PointF mTopPointB;
  private PointF mTopPoint;
  private PointF mBottomPointA;
  private PointF mBottomPointB;
  private PointF mBottomPoint;
  private PointF mLeftPointA;
  private PointF mLeftPointB;
  private PointF mLeftPoint;
  private PointF mRightPointA;
  private PointF mRightPointB;
  private PointF mRightPoint;

  public PraiseView(Context context) {
    this(context, null);
  }

  public PraiseView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PraiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.PraiseView);
    mColorUnPraised = typedArray
        .getColor(R.styleable.PraiseView_praiseview_color_unpraised, Color.BLACK);
    mColorPraised = typedArray.getColor(R.styleable.PraiseView_praiseview_color_praised, Color.RED);
    mDuration = typedArray.getInt(R.styleable.PraiseView_praiseview_duration, 300);
    typedArray.recycle();

    mPaint = new Paint();
    mAnimator = new ValueAnimator();

    mHeartPath = new Path();
    mTopPointA = new PointF();
    mTopPointB = new PointF();
    mTopPoint = new PointF();
    mRightPointA = new PointF();
    mRightPointB = new PointF();
    mRightPoint = new PointF();
    mBottomPointA = new PointF();
    mBottomPointB = new PointF();
    mBottomPoint = new PointF();
    mLeftPointA = new PointF();
    mLeftPointB = new PointF();
    mLeftPoint = new PointF();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    int realHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    mRadius = Math.min(realWidth, realHeight) / 2;
    mCurrentRadius = mRadius;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    //全局居中 绘制
    canvas.translate(getWidth() / 2, getHeight() / 2);
    //画心
    drawHeart(canvas, mCurrentRadius);
//    //显示控制点
//    showControllPoint(canvas);
  }

  private void drawHeart(Canvas canvas, float radius) {
    initControlPoint(radius);
    mPaint.setColor(mPraiseState == PRAISESTATE_NO ? mColorUnPraised : mColorPraised);
    mPaint.setStyle(mPraiseState == PRAISESTATE_NO ? Paint.Style.STROKE : Paint.Style.FILL);
    mPaint.setStrokeWidth(mRadius / 10);
    mPaint.setStrokeJoin(Paint.Join.ROUND);
    mPaint.setAntiAlias(true);

    mHeartPath.rewind();
    mHeartPath.moveTo(mTopPoint.x, mTopPoint.y);
    mHeartPath.cubicTo(mTopPointB.x, mTopPointB.y, mRightPointA.x, mRightPointA.y, mRightPoint.x,
        mRightPoint.y);
    mHeartPath
        .cubicTo(mRightPointB.x, mRightPointB.y, mBottomPointB.x, mBottomPointB.y, mBottomPoint.x,
            mBottomPoint.y);
    mHeartPath.cubicTo(mBottomPointA.x, mBottomPointA.y, mLeftPointB.x, mLeftPointB.y, mLeftPoint.x,
        mLeftPoint.y);
    mHeartPath.cubicTo(mLeftPointA.x, mLeftPointA.y, mTopPointA.x, mTopPointA.y, mTopPoint.x,
        mTopPoint.y);
    mHeartPath.close();
    canvas.drawPath(mHeartPath, mPaint);
  }

  private void showControllPoint(Canvas canvas) {
    mPaint.setColor(Color.BLACK);
    mPaint.setStrokeWidth(10);
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.FILL);
    canvas.drawPoint(mTopPointA.x, mTopPointA.y, mPaint);
    canvas.drawPoint(mTopPoint.x, mTopPoint.y, mPaint);
    canvas.drawPoint(mTopPointB.x, mTopPointB.y, mPaint);
    canvas.drawPoint(mRightPointA.x, mRightPointA.y, mPaint);
    canvas.drawPoint(mRightPoint.x, mRightPoint.y, mPaint);
    canvas.drawPoint(mRightPointB.x, mRightPointB.y, mPaint);
    canvas.drawPoint(mBottomPointA.x, mBottomPointA.y, mPaint);
    canvas.drawPoint(mBottomPoint.x, mBottomPoint.y, mPaint);
    canvas.drawPoint(mBottomPointB.x, mBottomPointB.y, mPaint);
    canvas.drawPoint(mLeftPointA.x, mLeftPointA.y, mPaint);
    canvas.drawPoint(mLeftPoint.x, mLeftPoint.y, mPaint);
    canvas.drawPoint(mLeftPointB.x, mLeftPointB.y, mPaint);
  }

  private void initControlPoint(float radius) {
    mOffset = c * radius;
    mTopPointA.set(-mOffset, -radius);
    mTopPointB.set(mOffset, -radius);
    mTopPoint.set(0, -radius * 0.5f);
    mRightPointA.set(radius, -mOffset);
    mRightPointB.set(radius * 0.9f, mOffset);
    mRightPoint.set(radius, 0);
    mBottomPointA.set(-mOffset, radius * 0.7f);
    mBottomPointB.set(mOffset, radius * 0.7f);
    mBottomPoint.set(0, radius);
    mLeftPointA.set(-radius, -mOffset);
    mLeftPointB.set(-radius * 0.9f, mOffset);
    mLeftPoint.set(-radius, 0);
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
