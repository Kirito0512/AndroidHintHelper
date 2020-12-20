package com.example.mylibrary.viewhint.HintInfo;

import android.graphics.Path;
import android.graphics.RectF;

public class UpperRightViewHintInfo extends ViewHintInfo {

    public UpperRightViewHintInfo(int viewId, String hintText, int textGravity, int hintMargin, boolean clickEnable, boolean showLightCircle, boolean showHintCircle) {
        super(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, showHintCircle);
    }

    @Override
    public float getTextStartX(int singleLineWidth) {
        return getCenterX() - 50;
    }

    @Override
    public float getTextStartY(float totalLineHeight) {
        return getCenterY() - getRadius() - 100 - totalLineHeight;
    }

    @Override
    public Path getTrianglePath() {
        RectF rectF = getBackgroundRectF(getSingleLineWidth(), getTotalLineHeight());
        Path path = new Path();
        // 防止出现圆角矩形和三角形之间出现不连接的部分
        float bottom = (float) Math.floor(rectF.bottom);
        path.moveTo(getCenterX() - triangleWidth, bottom);
        path.lineTo(getCenterX(), bottom + triangleHeight);
        path.lineTo(getCenterX() + triangleWidth, bottom);
        path.close();
        return path;
    }
}
