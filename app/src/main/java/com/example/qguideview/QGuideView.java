package com.example.qguideview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Queue;

public class QGuideView extends FrameLayout implements View.OnClickListener {
    private static final String SP_NAME = QGuideView.class.getSimpleName();

    private String mOnlyOnceTag; //标识蒙层是否只显示一次
    private ViewGroup mDecorView; //基于decorView全屏显示蒙层
    private View mTargetView; //不为null时，相对targetView显示
    private AppCompatImageView mImageView; //设置蒙层，响应点击
    private Queue<Integer> mResIdQueue; //存放全部蒙层资源id的队列
    private OnDisplayListener mListener; //蒙层显示、是否再次显示、消失的监听器

    public QGuideView(@NonNull Activity context) {
        this(context, null);
    }

    public QGuideView(@NonNull Activity context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QGuideView(@NonNull Activity context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Activity activity) {
        mDecorView = (ViewGroup) activity.getWindow().getDecorView();
        mImageView = new AppCompatImageView(activity);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!mResIdQueue.isEmpty()) {
            mResIdQueue.remove();
        }

        if (mResIdQueue.isEmpty()) {
            putUseRecord();
            mDecorView.removeView(this);

            if (mListener != null) {
                mListener.onDismiss();
            }
            return;
        }
        mImageView.setImageResource(mResIdQueue.peek());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public static class Builder {
        private QGuideView mGuideView;

        public Builder(Activity activity) {
            mGuideView = new QGuideView(activity);
        }

        public Builder setOnlyOnceTag(String tag) {
            mGuideView.setOnlyOnceTag(tag);
            return this;
        }

        public Builder setTargetView(View targetView) {
            mGuideView.setTargetView(targetView);
            return this;
        }

        public Builder addDrawable(@DrawableRes int resId) {
            mGuideView.addDrawable(resId);
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            mGuideView.setBackgroundColor(color);
            return this;
        }

        public Builder setBackgroundResource(@DrawableRes int resId) {
            mGuideView.setBackgroundResource(resId);
            return this;
        }

        public Builder setOnDisplayListener(OnDisplayListener listener) {
            mGuideView.setOnDisplayListener(listener);
            return this;
        }

        public QGuideView build() {
            return mGuideView;
        }
    }

    private void setOnlyOnceTag(String tag) {
        mOnlyOnceTag = tag;
    }

    private void setTargetView(View targetView) {
        this.mTargetView = targetView;
    }

    private void addDrawable(int resId) {
        if (mResIdQueue == null) {
            mResIdQueue = new LinkedList<>();
        }
        mResIdQueue.offer(resId);
    }

    private void setOnDisplayListener(OnDisplayListener listener) {
        mListener = listener;
    }

    private boolean hasUsedOnce() {
        if (TextUtils.isEmpty(mOnlyOnceTag)) {
            return false;
        }
        SharedPreferences sp = getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(mOnlyOnceTag, false);
    }

    private boolean putUseRecord() {
        if (TextUtils.isEmpty(mOnlyOnceTag)) {
            return false;
        }
        SharedPreferences sp = getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(mOnlyOnceTag, true);
        return editor.commit();
    }

    public void show() {
        if (mListener != null) {
            mListener.onReappear(hasUsedOnce());
        }

        if (hasUsedOnce()) {
            return;
        }
        if (mResIdQueue.isEmpty()) {
            return;
        }

        this.removeView(mImageView);
        mDecorView.removeView(this);

        mImageView.setImageResource(mResIdQueue.peek());

        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTargetView != null) {
            int[] location = new int[2];
            mTargetView.getLocationOnScreen(location);
            Drawable drawable = mImageView.getDrawable();
            location[0] = location[0] - drawable.getIntrinsicWidth();
            location[0] = location[0] < 0 ? 0 : location[0];
            location[1] = location[1] - drawable.getIntrinsicHeight();
            location[1] = location[1] < 0 ? 0 : location[1];
            lp.leftMargin = location[0];
            lp.topMargin = location[1];
            lp.gravity = Gravity.TOP | Gravity.RIGHT;
        } else {

            lp.gravity = Gravity.CENTER;
        }
        this.addView(mImageView, lp);
        mDecorView.addView(this);

        if (mListener != null) {
            mListener.onShow(this);
        }
    }

    public void hide() {
        mDecorView.removeView(this);
    }

    public interface OnDisplayListener {
        void onShow(QGuideView guideView);

        void onReappear(boolean isReappear);

        void onDismiss();
    }
}
