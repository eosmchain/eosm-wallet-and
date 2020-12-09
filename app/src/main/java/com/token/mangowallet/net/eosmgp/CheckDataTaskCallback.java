package com.token.mangowallet.net.eosmgp;

public interface CheckDataTaskCallback {

    void update(Object object);

    void finish(boolean success, Object o1, Object o2);
}
