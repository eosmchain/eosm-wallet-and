package com.token.mangowallet.view.basepopup;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.token.mangowallet.R;


public abstract class BasePopup extends EasyPopup {
    private static final String TAG = "BaseCustomPopup";

    protected BasePopup(Context context) {
        super(context);
    }

    @Override
    public void onPopupWindowCreated() {
        super.onPopupWindowCreated();
        //执行设置PopupWindow属性也可以通过Builder中设置
        //setContentView(x,x,x);
        //...
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusAndOutsideEnable(true);
        setAnimationStyle(R.style.dialogSlideAnim);
        setBackgroundDimEnable(true);
        initAttributes();
    }

    @Override
    public void onPopupWindowViewCreated(View contentView) {
        initViews(contentView);
    }

    @Override
    public void onPopupWindowDismiss() {

    }

    /**
     * 可以在此方法中设置PopupWindow需要的属性
     */
    protected abstract void initAttributes();

    /**
     * 初始化view {@see getView()}
     *
     * @param view
     */
    protected abstract void initViews(View view);


}
