package com.token.mangowallet.repository;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.token.mangowallet.MyApplication;
import com.token.mangowallet.bean.AccountInfo;
import com.token.mangowallet.bean.AccountNames;
import com.token.mangowallet.bean.TableRowsBean;
import com.token.mangowallet.bean.TransactionBean;
import com.token.mangowallet.bean.TransactionDetailsBean;
import com.token.mangowallet.bean.TransactionRecordBean;
import com.token.mangowallet.entity.Token;
import com.token.mangowallet.entity.TokenInfo;
import com.token.mangowallet.net.eosmgp.CustomEosioJavaRpcProviderImpl;
import com.token.mangowallet.net.eosmgp.EOSParams;
import com.token.mangowallet.net.eosmgp.ErrorUtils;
import com.token.mangowallet.utils.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import one.block.eosiojava.error.rpcProvider.RpcProviderError;
import one.block.eosiojava.error.serializationProvider.SerializationProviderError;
import one.block.eosiojava.error.session.TransactionPrepareError;
import one.block.eosiojava.error.session.TransactionSignAndBroadCastError;
import one.block.eosiojava.implementations.ABIProviderImpl;
import one.block.eosiojava.interfaces.IABIProvider;
import one.block.eosiojava.interfaces.IRPCProvider;
import one.block.eosiojava.interfaces.ISerializationProvider;
import one.block.eosiojava.interfaces.ISignatureProvider;
import one.block.eosiojava.models.rpcProvider.Action;
import one.block.eosiojava.models.rpcProvider.Authorization;
import one.block.eosiojava.models.rpcProvider.response.GetInfoResponse;
import one.block.eosiojava.models.rpcProvider.response.PushTransactionResponse;
import one.block.eosiojava.models.rpcProvider.response.RPCResponseError;
import one.block.eosiojava.session.TransactionProcessor;
import one.block.eosiojava.session.TransactionSession;
import one.block.eosiojavaabieosserializationprovider.AbiEosSerializationProviderImpl;
import one.block.eosiosoftkeysignatureprovider.SoftKeySignatureProviderImpl;
import one.block.eosiosoftkeysignatureprovider.error.ImportKeyError;
import party.loveit.eosforandroidlibrary.Ecc;

import static com.token.mangowallet.utils.Constants.EOSIO_TOKEN_CONTRACT_CODE;

public class EMWalletRepository {
    public CustomEosioJavaRpcProviderImpl customEosioJavaRpcProvider = null;


    public EMWalletRepository() {
        RepositoryFactory rf = MyApplication.repositoryFactory();
        customEosioJavaRpcProvider = rf.customEosioJavaRpcProvider;
    }

    public Observable<List<Token>> fetchBalance(String walletAddress, List<Token> tokens) {
        return Observable.fromCallable(() -> getToken(walletAddress, tokens)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BigDecimal> fetchBalance(String walletAddress, Constants.WalletType walletType) {
        return Observable.fromCallable(() -> getEOSMGPBalance(walletAddress, walletType)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<String>> fetchAccount(String privatekey, Constants.WalletType walletType) {
        return Observable.fromCallable(() ->
                getAccount(privatekey, walletType)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<AccountInfo> fetchAccountInfo(String walletAddress, Constants.WalletType walletType) {
        return Observable.fromCallable(() -> getAccountInfo(walletAddress, walletType)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TransactionBean> sendTransaction(String action, String privateKey, String fromAccount, String code,
                                                       String jsonData, Constants.WalletType walletType) {
        return Observable.fromCallable(() -> getTransaction(action, privateKey, fromAccount, code, jsonData, walletType)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TransactionRecordBean> fetchActions(Map<String, Object> pamars, Constants.WalletType walletType) {
        return Observable.fromCallable(() -> getActions(pamars, walletType)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TransactionDetailsBean> fetchTransactionDetails(String id, Constants.WalletType walletType) {
        return Observable.fromCallable(() -> getTransactionDetails(id, walletType)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<GetInfoResponse> fetchInfo() {
        return Observable.fromCallable(() -> customEosioJavaRpcProvider.getInfo()
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TableRowsBean> fetchTableRows(Map<String, Object> pamars, Constants.WalletType walletType) {
        return Observable.fromCallable(() -> getTableRows(pamars, walletType)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> fetchTableRowsStr(Map<String, Object> pamars, Constants.WalletType walletType) {
        return Observable.fromCallable(() -> getTableRowsStr(pamars, walletType)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TransactionBean> sendTransaction(List<Action> actionList, String privateKey, Constants.WalletType walletType) {
        return Observable.fromCallable(() -> getTransaction(actionList, privateKey, walletType)
        ).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public List<Token> getToken(String walletAddress, List<Token> tokens) throws Exception {
        for (Token token : tokens) {
            BigDecimal balance = null;
            String balanceStr = null;
            TokenInfo tokenInfo = token.tokenInfo;
            Constants.WalletType walletType = tokenInfo.walletType;
            if (tokenInfo.address.isEmpty()) {
                balance = getEOSMGPBalance(walletAddress, walletType);
            }
            LogUtils.d("balance:" + balance);
            if (balance == null || balance.compareTo(BigDecimal.ZERO) == 0) {
                token.balance = "0";
            } else {
                token.balance = balance.setScale(tokenInfo.decimals, RoundingMode.CEILING).toPlainString();
            }
        }
        return tokens;
    }

    public BigDecimal getEOSMGPBalance(String walletAddress, Constants.WalletType walletType) throws Exception {
        Map<String, Object> map = EOSParams.getBalancePamars(walletAddress, EOSIO_TOKEN_CONTRACT_CODE, walletType + "");
        RequestBody requestBody = EOSParams.getRequestBody(map);
        String balance = "";
        balance = customEosioJavaRpcProvider.getCurrencyBalance(requestBody);
        //["161.4881 MGP"]
        if (!ObjectUtils.isEmpty(balance)) {
            if (balance.contains(walletType + "")) {
                balance = balance.split(" ")[0].replace("[\"", "");
                return new BigDecimal(balance);
            }
        }
        return BigDecimal.ZERO;
    }

    public List<String> getAccount(String privatekey, Constants.WalletType walletType) throws RpcProviderError {
        String publicKey = Ecc.privateToPublic(privatekey);
        Map<String, Object> map = EOSParams.getKeyAccountsPamars(publicKey);
        String accounts = "";
        accounts = customEosioJavaRpcProvider.getKeyAccounts(EOSParams.getRequestBody(map));
        if (ObjectUtils.isEmpty(accounts)) {
            return null;
        }
        AccountNames accountNames = GsonUtils.fromJson(accounts, AccountNames.class);
        List<String> accountList = accountNames.getAccount_names();
        return accountList;
    }

    public AccountInfo getAccountInfo(String walletAddress, Constants.WalletType walletType) throws RpcProviderError {
        Map<String, Object> map = EOSParams.getAccountPamars(walletAddress);
        RequestBody requestBody = EOSParams.getRequestBody(map);
        String accountInfoStr = null;
        accountInfoStr = customEosioJavaRpcProvider.getAccount(requestBody);
        //["161.4881 MGP"]
        if (!ObjectUtils.isEmpty(accountInfoStr)) {
            return GsonUtils.fromJson(accountInfoStr, AccountInfo.class);
        }
        return null;
    }

    public TransactionBean getTransaction(String action, String privateKey, String fromAccount, String code,
                                          String jsonData, Constants.WalletType walletType) {
        TransactionBean transactionBean = new TransactionBean();
        ISerializationProvider serializationProvider;
        try {
            serializationProvider = new AbiEosSerializationProviderImpl();
        } catch (SerializationProviderError serializationProviderError) {
            serializationProviderError.printStackTrace();
            return null;
        }
        // Creating RPC Provider
        IRPCProvider rpcProvider = customEosioJavaRpcProvider;
        IABIProvider abiProvider = new ABIProviderImpl(rpcProvider, serializationProvider);
        // Creating Signature provider
        ISignatureProvider signatureProvider = new SoftKeySignatureProviderImpl();
        try {
            ((SoftKeySignatureProviderImpl) signatureProvider).importKey(privateKey);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask importKeyError = " + importKeyError.getMessage());
//            this.publishProgress(Boolean.toString(false), importKeyError.getMessage());
            transactionBean.setData(false, importKeyError.getMessage());
            return transactionBean;
        }
        // Creating TransactionProcess
        TransactionSession session = new TransactionSession(serializationProvider, rpcProvider, abiProvider, signatureProvider);
        TransactionProcessor processor = session.getTransactionProcessor();
        // Apply transaction data to Action's data
        // Creating action with action's data, eosio.token contract and transfer action.
        Action mAction = new Action(code, action, Collections.singletonList(new Authorization(fromAccount, "active")), jsonData);
        try {
            // Prepare transaction with above action. A transaction can be executed with multiple action.
            processor.prepare(Collections.singletonList(mAction));
            // Sign and broadcast the transaction.
            PushTransactionResponse response = processor.signAndBroadcast();
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask getTransactionId = " + response.getTransactionId());
            transactionBean.setData(true, response.getTransactionId());
            return transactionBean;
        } catch (TransactionPrepareError transactionPrepareError) {
            // Happens if preparing transaction unsuccessful
            transactionPrepareError.printStackTrace();
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask TransactionPrepareError = " + transactionPrepareError.getMessage());
            transactionBean.setData(false, transactionPrepareError.getLocalizedMessage());
            return transactionBean;
        } catch (TransactionSignAndBroadCastError transactionSignAndBroadCastError) {
            // Happens if Sign transaction or broadcast transaction unsuccessful.
            transactionSignAndBroadCastError.printStackTrace();
            // try to get backend error if the error come from backend
            RPCResponseError rpcResponseError = ErrorUtils.getBackendError(transactionSignAndBroadCastError);
            if (rpcResponseError != null) {
                String errorDetail = ErrorUtils.getErrorDetail(rpcResponseError);
                LogUtils.dTag(Constants.LOG_TAG, "TransactionTask backendErrorMessage = " + errorDetail);
                transactionBean.setData(false, errorDetail);
                return transactionBean;
            }
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask TransactionSignAndBroadCastError asJsonString= " + transactionSignAndBroadCastError.asJsonString());
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask TransactionSignAndBroadCastError = " + transactionSignAndBroadCastError.getMessage());
            transactionBean.setData(false, transactionSignAndBroadCastError.getMessage());
            return transactionBean;
        }
    }

    public TransactionBean getTransaction(List<Action> actionList, String privateKey, Constants.WalletType walletType) {
        TransactionBean transactionBean = new TransactionBean();
        ISerializationProvider serializationProvider;
        try {
            serializationProvider = new AbiEosSerializationProviderImpl();
        } catch (SerializationProviderError serializationProviderError) {
            serializationProviderError.printStackTrace();
            return null;
        }
        // Creating RPC Provider
        IRPCProvider rpcProvider = customEosioJavaRpcProvider;
        IABIProvider abiProvider = new ABIProviderImpl(rpcProvider, serializationProvider);
        // Creating Signature provider
        ISignatureProvider signatureProvider = new SoftKeySignatureProviderImpl();
        try {
            ((SoftKeySignatureProviderImpl) signatureProvider).importKey(privateKey);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask importKeyError = " + importKeyError.getMessage());
//            this.publishProgress(Boolean.toString(false), importKeyError.getMessage());
            transactionBean.setData(false, importKeyError.getMessage());
            return transactionBean;
        }
        // Creating TransactionProcess
        TransactionSession session = new TransactionSession(serializationProvider, rpcProvider, abiProvider, signatureProvider);
        TransactionProcessor processor = session.getTransactionProcessor();
        // Apply transaction data to Action's data
        // Creating action with action's data, eosio.token contract and transfer action.
//        Action mAction = new Action(code, action, Collections.singletonList(new Authorization(fromAccount, "active")), jsonData);
        try {
            // Prepare transaction with above action. A transaction can be executed with multiple action.
            processor.prepare(actionList);
            // Sign and broadcast the transaction.
            PushTransactionResponse response = processor.signAndBroadcast();
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask getTransactionId = " + response.getTransactionId());
            transactionBean.setData(true, response.getTransactionId());
            return transactionBean;
        } catch (TransactionPrepareError transactionPrepareError) {
            // Happens if preparing transaction unsuccessful
            transactionPrepareError.printStackTrace();
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask TransactionPrepareError = " + transactionPrepareError.getMessage());
            transactionBean.setData(false, transactionPrepareError.getLocalizedMessage());
            return transactionBean;
        } catch (TransactionSignAndBroadCastError transactionSignAndBroadCastError) {
            // Happens if Sign transaction or broadcast transaction unsuccessful.
            transactionSignAndBroadCastError.printStackTrace();
            // try to get backend error if the error come from backend
            RPCResponseError rpcResponseError = ErrorUtils.getBackendError(transactionSignAndBroadCastError);
            if (rpcResponseError != null) {
                String errorDetail = ErrorUtils.getErrorDetail(rpcResponseError);
                LogUtils.dTag(Constants.LOG_TAG, "TransactionTask backendErrorMessage = " + errorDetail);
                transactionBean.setData(false, errorDetail);
                return transactionBean;
            }
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask TransactionSignAndBroadCastError asJsonString= " + transactionSignAndBroadCastError.asJsonString());
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask TransactionSignAndBroadCastError = " + transactionSignAndBroadCastError.getMessage());
            transactionBean.setData(false, transactionSignAndBroadCastError.getMessage());
            return transactionBean;
        }
    }

    public TransactionRecordBean getActions(Map<String, Object> pamars, Constants.WalletType walletType) throws RpcProviderError {
        RequestBody requestBody = EOSParams.getRequestBody(pamars);
        String accountInfoStr = null;
        accountInfoStr = customEosioJavaRpcProvider.getActions(requestBody);
        //["161.4881 MGP"]
        if (!ObjectUtils.isEmpty(accountInfoStr)) {
            return GsonUtils.fromJson(accountInfoStr, TransactionRecordBean.class);
        }
        return null;
    }

    public TransactionDetailsBean getTransactionDetails(String id, Constants.WalletType walletType) throws RpcProviderError {
        Map pamars = EOSParams.getTransactionPamars(id);
        RequestBody requestBody = EOSParams.getRequestBody(pamars);
        String accountInfoStr = null;
        accountInfoStr = customEosioJavaRpcProvider.getTransaction(requestBody);
        //["161.4881 MGP"]
        if (!ObjectUtils.isEmpty(accountInfoStr)) {
            return GsonUtils.fromJson(accountInfoStr, TransactionDetailsBean.class);
        }
        return null;
    }

    public TableRowsBean getTableRows(Map<String, Object> pamars, Constants.WalletType walletType) throws RpcProviderError {
        RequestBody requestBody = EOSParams.getRequestBody(pamars);
        String accountInfoStr = null;
        accountInfoStr = customEosioJavaRpcProvider.getTableRows(requestBody);
        TableRowsBean tableRowsBean = null;
        if (!ObjectUtils.isEmpty(accountInfoStr)) {
            tableRowsBean = GsonUtils.fromJson(accountInfoStr, TableRowsBean.class);
        }
        return tableRowsBean;
    }

    public String getTableRowsStr(Map<String, Object> pamars, Constants.WalletType walletType) throws RpcProviderError {
        RequestBody requestBody = EOSParams.getRequestBody(pamars);
        return customEosioJavaRpcProvider.getTableRows(requestBody);
    }
}
