package com.token.mangowallet.bus;

import com.qmuiteam.qmui.arch.effect.Effect;
import com.token.mangowallet.bean.ShippingAddressBean;

public class AddGoodsSucEffect extends Effect {
    public int type = 0;

    public AddGoodsSucEffect(int type) {
        this.type = type;
    }
}
