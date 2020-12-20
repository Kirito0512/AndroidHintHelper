package com.example.mylibrary.viewhint.HintInfo;

import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;


import com.example.mylibrary.viewhint.AndroidUtils;
import com.example.mylibrary.viewhint.KtvHintView;
import com.example.mylibrary.viewhint.ViewHintSizeInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ViewHintInfo implements ViewHintSizeInfo {
    private @IdRes
    int viewId;
    private View hintView;
    private String hintText;
    private int hintMargin;
    private boolean showLightCircle;
    private boolean clickEnable;
    private int singleLineWidth;
    private float totalLineHeight;
    private boolean showHintCircle;
    private int lightCircleMargin;

    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    public static final int LOWER_RIGHT = 4;
    public static final int LOWER_LEFT = 5;
    public static final int UPPER_LEFT = 6;
    public static final int UPPER_RIGHT = 7;
    @IntDef({LEFT, TOP, RIGHT, BOTTOM, LOWER_RIGHT, LOWER_LEFT, UPPER_LEFT, UPPER_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HintTextGravity {}
    private @HintTextGravity int textGravity;
    protected int triangleWidth = 18;//AndroidUtils.dpToPixel(6);
    protected int triangleHeight = 15;//AndroidUtils.dpToPixel(5);

    @Override
    public float getTextStartX(int singleLineWidth) {
        return 0;
    }

    @Override
    public float getTextStartY(float totalLineHeight) {
        return 0;
    }

    @Override
    public Path getTrianglePath() {
        return null;
    }

    @Override
    public RectF getBackgroundRectF(int singleLineWidth, float totalLineHeight) {
        float left = getTextStartX(singleLineWidth) - KtvHintView.TEXT_BACKGROUND_PADDING;
        float top = getTextStartY(totalLineHeight) - KtvHintView.TEXT_BACKGROUND_PADDING;
        float right = left + singleLineWidth + 2 * KtvHintView.TEXT_BACKGROUND_PADDING;
        float bottom = top + totalLineHeight + 2 * KtvHintView.TEXT_BACKGROUND_PADDING;
        // 绘制圆角背景颜色
        return new RectF(left, top, right, bottom);
    }

    private float centerX, centerY, radius;

    public ViewHintInfo(int viewId, String hintText, @HintTextGravity int textGravity, int hintMargin, boolean clickEnable, boolean showLightCircle) {
        this(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, true);
    }

    public ViewHintInfo(int viewId, String hintText, @HintTextGravity int textGravity, int hintMargin, boolean clickEnable, boolean showLightCircle, boolean showHintCircle) {
        this.viewId = viewId;
        this.hintText = hintText;
        this.textGravity = textGravity;
        this.hintMargin = hintMargin;
        this.clickEnable = clickEnable;
        this.showLightCircle = showLightCircle;
        this.showHintCircle = showHintCircle;
    }

    public ViewHintInfo(int viewId, String hintText, @HintTextGravity int textGravity) {
        this(viewId, hintText, ViewHintInfo.BOTTOM, 15, true, true);
    }

    public ViewHintInfo(int viewId) {
        this(viewId, null, ViewHintInfo.BOTTOM, 15, true, false);
    }


    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public View getHintView() {
        return hintView;
    }

    public void setHintView(View hintView) {
        this.hintView = hintView;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public int getHintMargin() {
        return hintMargin;
    }

    public void setHintMargin(int hintMargin) {
        this.hintMargin = hintMargin;
    }

    public boolean isShowLightCircle() {
        return showLightCircle;
    }

    public void setShowLightCircle(boolean showLightCircle) {
        this.showLightCircle = showLightCircle;
    }

    public boolean isClickEnable() {
        return clickEnable;
    }

    public void setClickEnable(boolean clickEnable) {
        this.clickEnable = clickEnable;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getRadius() {
        return radius;
    }

    public int getTextGravity() {
        return textGravity;
    }

    public void setTextGravity(int textGravity) {
        this.textGravity = textGravity;
    }


    public void setSingleLineWidth(int singleLineWidth) {
        this.singleLineWidth = singleLineWidth;
    }


    public void setTotalLineHeight(float totalLineHeight) {
        this.totalLineHeight = totalLineHeight;
    }

    public int getSingleLineWidth() {
        return singleLineWidth;
    }

    public float getTotalLineHeight() {
        return totalLineHeight;
    }

    public boolean isShowHintCircle() {
        return showHintCircle;
    }

    public void setShowHintCircle(boolean showHintCircle) {
        this.showHintCircle = showHintCircle;
    }

    public int getLightCircleMargin() {
        return lightCircleMargin;
    }

    public void setLightCircleMargin(int lightCircleMargin) {
        this.lightCircleMargin = lightCircleMargin;
    }
}
