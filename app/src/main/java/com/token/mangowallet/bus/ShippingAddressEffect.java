package com.token.mangowallet.bus;

import com.qmuiteam.qmui.arch.effect.Effect;
import com.token.mangowallet.bean.ShippingAddressBean;

public class ShippingAddressEffect extends Effect {
    public final ShippingAddressBean.DataBean addressDataBean;

    public ShippingAddressEffect(ShippingAddressBean.DataBean addressDataBean) {
        this.addressDataBean = addressDataBean;
    }
}
