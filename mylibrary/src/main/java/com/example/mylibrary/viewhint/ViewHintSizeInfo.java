package com.example.mylibrary.viewhint;

import android.graphics.Path;
import android.graphics.RectF;

public interface ViewHintSizeInfo {

    public float getTextStartX(int singleLineWidth);
    public float getTextStartY(float totalLineHeight);
    public Path getTrianglePath();
    public RectF getBackgroundRectF(int singleLineWidth, float totalLineHeight);
}
