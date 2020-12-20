package com.example.mylibrary.viewhint;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.mylibrary.R;
import com.example.mylibrary.viewhint.HintInfo.ViewHintInfo;


public class AndroidViewHintHelper {
    private KtvHintView hintView;
    private SparseArray<ViewHintInfo> sparseArray = new SparseArray<>();
    private FragmentActivity activity;
    private View externalView;
    private TextView skipTv;

    public AndroidViewHintHelper(Fragment fragment) {
        this(fragment.getActivity());
    }

    public AndroidViewHintHelper(FragmentActivity activity) {
        this.activity = activity;
        this.hintView = new KtvHintView(activity);
    }

    public AndroidViewHintHelper addHintView(@IdRes int viewId, String hintText, @ViewHintInfo.HintTextGravity int textGravity) {
        return addHintView(viewId, null, hintText, textGravity, 0, true, false, 0);
    }

    public AndroidViewHintHelper addHintView(@IdRes int viewId, String hintText, @ViewHintInfo.HintTextGravity int textGravity, int margin, boolean clickEnable) {
        return addHintView(viewId, null, hintText, textGravity, margin, clickEnable, false, 0);
    }

    public AndroidViewHintHelper addHintView(@IdRes int viewId, String hintText, @ViewHintInfo.HintTextGravity int textGravity, int margin) {
        return addHintView(viewId, null, hintText, textGravity, margin, false, false, 0);
    }

    public AndroidViewHintHelper addHintView(ViewHintInfo viewHintInfo) {
        if (viewHintInfo == null || viewHintInfo.getHintView() == null) {
            return this;
        }
        sparseArray.put(viewHintInfo.getHintView().hashCode(), viewHintInfo);
        return this;
    }

    public AndroidViewHintHelper addHintView(View hintView, String hintText, @ViewHintInfo.HintTextGravity int textGravity, int hintMargin) {
        if (hintView == null)
            return this;
        return addHintView(hintView.getId(), hintView, hintText, textGravity, hintMargin, true, false, 0);
    }

    public AndroidViewHintHelper addExternalCenterView(View externalView, String hintText, @ViewHintInfo.HintTextGravity int textGravity, boolean showHintCircle, boolean clickEnable) {
        this.externalView = externalView;
        ViewHintInfo hintInfo = ViewHintInfoFactory.create(0, hintText, textGravity, 0, clickEnable, false, false);
        hintInfo.setHintView(externalView);
        sparseArray.put(externalView.hashCode(), hintInfo);
        FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
        decorView.addView(externalView);
        return this;
    }

    public AndroidViewHintHelper addHintView(@IdRes int viewId, View hintView, String hintText, @ViewHintInfo.HintTextGravity int textGravity, int hintMargin, boolean clickEnable, boolean showLightCircle, int lightCircleMargin) {
        ViewHintInfo hintInfo = ViewHintInfoFactory.create(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, true);
        if (hintView == null) {
            hintView = activity.findViewById(viewId);
        }
        hintInfo.setHintView(hintView);
        hintInfo.setLightCircleMargin(lightCircleMargin);
        sparseArray.put(hintView.hashCode(), hintInfo);
        return this;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public AndroidViewHintHelper addSkipView(final View.OnClickListener onClickListener) {
        skipTv = new TextView(activity);
        skipTv.setText("跳过");
        skipTv.setTextColor(Color.parseColor("#FF121212"));
        skipTv.setTextSize(12);
        skipTv.setGravity(Gravity.CENTER);
        skipTv.setBackground(skipTv.getResources().getDrawable(R.drawable.skip_textview_background, null));
        skipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
        skipTv.setTag("HintView");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(AndroidUtils.dpToPixel(skipTv.getContext(),48), AndroidUtils.dpToPixel(skipTv.getContext(), 24));
        lp.gravity = Gravity.BOTTOM | Gravity.START;
        lp.leftMargin = AndroidUtils.dpToPixel(skipTv.getContext(),15);
        lp.bottomMargin = AndroidUtils.dpToPixel(skipTv.getContext(),15) + AndroidUtils.getDeviceNavigationBarHeight(skipTv.getContext());
        skipTv.setLayoutParams(lp);
        return this;
    }

    public AndroidViewHintHelper clearHint() {
        sparseArray.clear();
        FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
        decorView.removeView(skipTv);
        return this;
    }

    public void updateContent() {
        if (hintView != null && hintView.getParent() != null) {
            hintView.invalidate();
        }
    }

    public AndroidViewHintHelper setHintViewClickListener(View.OnClickListener onClickListener) {
        if (hintView != null) {
            hintView.setOnClickListener(onClickListener);
        }
        return this;
    }

    public void show() {
        if (activity == null)
            return;
        final FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
        View topView = decorView.getChildAt(decorView.getChildCount() - 1);
        if (topView.getTag() != null && topView.getTag().equals("HintView")) {
            hintView = null;
            skipTv = null;
            sparseArray.clear();
            return;
        }
        decorView.post(new Runnable() {
            @Override
            public void run() {
                hintView.setHintInfo(sparseArray);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                hintView.setLayoutParams(layoutParams);
                hintView.setTag("HintView");
                decorView.addView(hintView);
                if (externalView != null) {
                    decorView.addView(externalView);
                }
                if (skipTv != null) {
                    decorView.addView(skipTv);
                }
            }
        });
    }

    public void dismiss() {
        if (activity == null)
            return;
        FrameLayout decorView = (FrameLayout) activity.getWindow().getDecorView();
        decorView.removeView(hintView);
        decorView.removeView(externalView);
        decorView.removeView(skipTv);
        hintView = null;
        skipTv = null;
        externalView = null;
        sparseArray.clear();
    }
}
