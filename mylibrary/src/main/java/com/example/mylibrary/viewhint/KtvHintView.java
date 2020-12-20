package com.example.mylibrary.viewhint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.mylibrary.R;
import com.example.mylibrary.viewhint.HintInfo.ViewHintInfo;

import java.lang.reflect.Field;

public class KtvHintView extends View {
    private SparseArray<ViewHintInfo> sparseArray;
    private Paint mPaint;
    private static final String TAG = "KtvHintView_test";
    private static final int DRAW_POINT_COUNT = 60;
    private static final int TEXT_MAX_LINE_SIZE = 10;
    public static final int TEXT_BACKGROUND_PADDING = 30;//AndroidUtils.dpToPixel(10);
    private Xfermode xfermode;

    public KtvHintView(@NonNull Context context) {
        this(context, null);
    }

    public KtvHintView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KtvHintView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean flag = true;
        // 屏蔽被遮住的view的点击事件
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (sparseArray != null && sparseArray.size() > 0) {
                    for (int i = 0; i < sparseArray.size(); i++) {
                        ViewHintInfo viewHintInfo = sparseArray.valueAt(i);
                        RectF rectF = new RectF();
                        int[] locationArray = new int[2];
                        viewHintInfo.getHintView().getLocationInWindow(locationArray);
                        rectF.left = locationArray[0];
                        rectF.top = locationArray[1];
                        rectF.right = locationArray[0] + viewHintInfo.getHintView().getWidth();
                        rectF.bottom = locationArray[1] + viewHintInfo.getHintView().getHeight();
                        // 点击位置在提示view上，并且设置为可点，那么不屏蔽点击
                        if (rectF.contains(event.getRawX(), event.getRawY()) && viewHintInfo.isClickEnable()) {
                            flag = false;
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                break;
        }
        return flag;
    }

    public void setHintInfo(SparseArray<ViewHintInfo> sparseArray) {
        this.sparseArray = sparseArray;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sparseArray == null)
            return;
        int count = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        canvas.drawColor(getResources().getColor(R.color.snatchmic_base_txt_black_alpha_50));
        mPaint.setXfermode(xfermode);
        for (int i = 0; i < sparseArray.size(); i++) {
            ViewHintInfo hintInfo = sparseArray.valueAt(i);
            if (hintInfo.getHintView() == null)
                return;
            drawHitView(hintInfo, canvas);
        }
        canvas.restoreToCount(count);
        mPaint.setXfermode(null);
        for (int i = 0; i < sparseArray.size(); i++) {
            ViewHintInfo hintInfo = sparseArray.valueAt(i);
            if (hintInfo.isShowLightCircle()) {
                drawPointCircle(hintInfo, canvas);
            }
            if (hintInfo.getHintText() != null && hintInfo.getHintText().length() > 0) {
                drawHintText(hintInfo, canvas);
            }
        }
    }

    // 绘制镂空区域
    private void drawHitView(ViewHintInfo hintInfo, Canvas canvas) {
        View view = hintInfo.getHintView();
        if (!view.isAttachedToWindow()) {
            view.post(() -> {
                drawHitView(hintInfo, canvas);
            });
            return;
        }
        int[] locationArray = new int[2];
        view.getLocationInWindow(locationArray);
        RectF rectF = new RectF();
        rectF.left = locationArray[0] + view.getPaddingStart();
        rectF.top = locationArray[1] + view.getPaddingTop();
        rectF.right = locationArray[0] + view.getWidth() - view.getPaddingEnd();
        rectF.bottom = locationArray[1] + view.getHeight() -  view.getPaddingBottom();
        // 根据长宽计算计算半径，再加上设定的半径margin
        int radius = calculateRadius(hintInfo, rectF) + hintInfo.getHintMargin();
        float centerCircleX = (rectF.left + rectF.right) / 2;
        float centerCircleY = (rectF.top + rectF.bottom) / 2;
        hintInfo.setCenterX(centerCircleX);
        hintInfo.setCenterY(centerCircleY);
        if (hintInfo.isShowHintCircle()) {
            hintInfo.setRadius(radius);
            canvas.drawCircle(centerCircleX, centerCircleY, radius, mPaint);
        } else {
            // 针对不展示镂空圆圈的case，只在BottomViewHintInfo的代码里做一下适配
            if (hintInfo.getTextGravity() == ViewHintInfo.BOTTOM) {
                hintInfo.setRadius(rectF.bottom - centerCircleY);
            }
        }
    }

    // 绘制点状的圆环
    private void drawPointCircle(ViewHintInfo hintInfo, Canvas canvas) {
        // 绘制镂空区域
        float radius = hintInfo.getRadius();
        float centerCircleX = hintInfo.getCenterX();
        float centerCircleY = hintInfo.getCenterY();

        int perDegree = 360 / DRAW_POINT_COUNT;
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        for (int i = 0; i < DRAW_POINT_COUNT; i++) {
            canvas.drawCircle(centerCircleX, centerCircleY - radius - hintInfo.getLightCircleMargin(), 2, mPaint);
            canvas.rotate(perDegree, centerCircleX, centerCircleY);
        }
    }

    private void drawHintText(ViewHintInfo hintInfo, Canvas canvas) {

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(5);
        textPaint.setTextSize(14 * 3);//AndroidUtils.sp2px(14)
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setAntiAlias(true);
        // 计算一行文字的宽度,最多一行展示10个字符
        int singleLineWidth = (int) textPaint.measureText(hintInfo.getHintText(), 0, Math.min(hintInfo.getHintText().length(), TEXT_MAX_LINE_SIZE));
        StaticLayout layout = new StaticLayout(hintInfo.getHintText(), textPaint, singleLineWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        // 这里的参数300，表示字符串的长度，当满300时，就会换行，也可以使用“\r\n”来实现换行
        canvas.save();

        int layoutCount = layout.getLineCount();
        float singleLineHeight = getFontHeight(textPaint);
        float totalLineHeight = singleLineHeight * layoutCount;

        hintInfo.setSingleLineWidth(singleLineWidth);
        hintInfo.setTotalLineHeight(totalLineHeight);
        // 绘制圆角背景颜色
        drawBackGround(hintInfo, canvas, singleLineWidth, totalLineHeight);
        // 绘制文字
        canvas.translate(hintInfo.getTextStartX(singleLineWidth), hintInfo.getTextStartY(totalLineHeight));
        layout.draw(canvas);
        canvas.restore();//别忘了restore
    }

    private void drawBackGround(ViewHintInfo hintInfo, Canvas canvas, int singleLineWidth, float totalLineHeight) {
        mPaint.setColor(Color.parseColor("#FFFF3348"));
        mPaint.setStyle(Paint.Style.FILL);
        RectF rectF = hintInfo.getBackgroundRectF(singleLineWidth, totalLineHeight);
        canvas.drawRoundRect(rectF, 30, 30, mPaint);
        // 绘制三角形
        canvas.drawPath(hintInfo.getTrianglePath(), mPaint);
    }

    /**
     *得到文字的高度
     */
    private float getFontHeight(TextPaint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();// 得到系统默认字体属性
        return fm.bottom - fm.top;
    }

    // 计算镂空区域半径
    private int calculateRadius(ViewHintInfo viewHintInfo, RectF rectF) {
        if (viewHintInfo != null && viewHintInfo.getHintView() != null && viewHintInfo.getHintView().getBackground() != null) {
            Drawable drawable = viewHintInfo.getHintView().getBackground();
            if (drawable instanceof GradientDrawable) {
                Field fieldGradientState;
                try {
                    fieldGradientState = Class.forName("android.graphics.drawable.GradientDrawable").getDeclaredField("mGradientState");
                    fieldGradientState.setAccessible(true);
                    Object mGradientState = fieldGradientState.get(drawable);
                    Field fieldShape = mGradientState.getClass().getDeclaredField("mShape");
                    fieldShape.setAccessible(true);
                    int shape = (int) fieldShape.get(mGradientState);
                    // 对于有圆形背景的控件，直接使用控件的宽度作为半径
                    if (shape == GradientDrawable.OVAL && viewHintInfo.getHintView().getWidth() == viewHintInfo.getHintView().getHeight()) {
                        return viewHintInfo.getHintView().getWidth() / 2;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // 否则使用勾股定理计算半径
        return (int) Math.ceil(Math.sqrt(Math.pow(rectF.right - rectF.left, 2) + Math.pow(rectF.bottom - rectF.top, 2)) / 2);
    }
}
