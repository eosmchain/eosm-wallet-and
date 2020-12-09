package com.token.mangowallet.bean;

import com.token.mangowallet.entity.TokenInfo;

public class EthplorerResponse {
    Token[] tokens;
    EthplorerError error;

    private static class EthplorerError {
        int code;
        String message;
    }

    private static class Token {
        TokenInfo tokenInfo;
    }
}
