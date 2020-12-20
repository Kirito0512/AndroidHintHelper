package com.example.mylibrary.viewhint.HintInfo;

import android.graphics.Path;
import android.graphics.RectF;

public class TopViewHintInfo extends ViewHintInfo {

    public TopViewHintInfo(int viewId, String hintText, int textGravity, int hintMargin, boolean clickEnable, boolean showLightCircle, boolean showHintCircle) {
        super(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, showHintCircle);
    }

    @Override
    public float getTextStartX(int singleLineWidth) {
        return getCenterX() - (singleLineWidth >> 1);
    }

    @Override
    public float getTextStartY(float totalLineHeight) {
        return getCenterY() - getRadius() - 100 - totalLineHeight;
    }

    @Override
    public Path getTrianglePath() {
        RectF rectF = getBackgroundRectF(getSingleLineWidth(), getTotalLineHeight());
        Path path = new Path();
        path.moveTo(getCenterX() - triangleWidth, rectF.bottom);
        path.lineTo(getCenterX(), rectF.bottom + triangleHeight);
        path.lineTo(getCenterX() + triangleWidth, rectF.bottom);
        path.close();
        return path;
    }
}
