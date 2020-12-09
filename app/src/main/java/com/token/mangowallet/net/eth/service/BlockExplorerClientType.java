package com.token.mangowallet.net.eth.service;


import com.token.mangowallet.entity.Transaction;

import io.reactivex.Observable;

public interface BlockExplorerClientType {
    Observable<Transaction[]> fetchTransactions(String forAddress, String forToken);
}
