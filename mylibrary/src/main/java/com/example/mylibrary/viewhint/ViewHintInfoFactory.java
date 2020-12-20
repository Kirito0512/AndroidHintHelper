package com.example.mylibrary.viewhint;

import com.example.mylibrary.viewhint.HintInfo.BottomViewHintInfo;
import com.example.mylibrary.viewhint.HintInfo.LowerLeftViewHintInfo;
import com.example.mylibrary.viewhint.HintInfo.LowerRightViewHintInfo;
import com.example.mylibrary.viewhint.HintInfo.TopViewHintInfo;
import com.example.mylibrary.viewhint.HintInfo.UpperLeftViewHintInfo;
import com.example.mylibrary.viewhint.HintInfo.UpperRightViewHintInfo;
import com.example.mylibrary.viewhint.HintInfo.ViewHintInfo;

public class ViewHintInfoFactory {
    public static ViewHintInfo create(int viewId, String hintText, int textGravity, int hintMargin, boolean clickEnable, boolean showLightCircle, boolean showHintCircle) {
        if (hintText == null || hintText.length() == 0) {
            return new ViewHintInfo(viewId);
        }
        switch (textGravity) {
            case ViewHintInfo.TOP:
                return new TopViewHintInfo(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, showHintCircle);
            case ViewHintInfo.LOWER_LEFT:
                return new LowerLeftViewHintInfo(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, showHintCircle);
            case ViewHintInfo.LOWER_RIGHT:
                return new LowerRightViewHintInfo(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, showHintCircle);
            case ViewHintInfo.UPPER_LEFT:
                return new UpperLeftViewHintInfo(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, showHintCircle);
            case ViewHintInfo.UPPER_RIGHT:
                return new UpperRightViewHintInfo(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, showHintCircle);
            case ViewHintInfo.BOTTOM:
            default:
                return new BottomViewHintInfo(viewId, hintText, textGravity, hintMargin, clickEnable, showLightCircle, showHintCircle);
        }
    }
}
