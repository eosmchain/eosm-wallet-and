package com.token.mangowallet.net.eosmgp;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.token.mangowallet.utils.Constants;

import java.util.Collections;
import java.util.List;

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
import one.block.eosiojava.models.rpcProvider.Transaction;
import one.block.eosiojava.models.rpcProvider.response.PushTransactionResponse;
import one.block.eosiojava.models.rpcProvider.response.RPCResponseError;
import one.block.eosiojava.session.TransactionProcessor;
import one.block.eosiojava.session.TransactionSession;
import one.block.eosiojavaabieosserializationprovider.AbiEosSerializationProviderImpl;
import one.block.eosiojavarpcprovider.error.EosioJavaRpcProviderInitializerError;
import one.block.eosiojavarpcprovider.implementations.EosioJavaRpcProviderImpl;
import one.block.eosiosoftkeysignatureprovider.SoftKeySignatureProviderImpl;
import one.block.eosiosoftkeysignatureprovider.error.ImportKeyError;

/**
 * This class is an example about the most basic/easy way to use eosio-java to send a transaction.
 * <p>
 * Basic steps:
 * <p>
 * - Create serialization provider as an instant of {@link AbiEosSerializationProviderImpl} from [eosiojavaandroidabieosserializationprovider] library
 * <p>
 * - Create RPC provider as an instant of {@link EosioJavaRpcProviderImpl} with an input string point to a node backend.
 * <p>
 * - Create ABI provider as an instant of {@link ABIProviderImpl} with instants of Rpc provider and serialization provider.
 * <p>
 * - Create Signature provider as an instant of {@link SoftKeySignatureProviderImpl} which is not recommended for production because of its simple key management.
 * <p>
 * - Import an EOS private key which associate with sender's account which will be used to sign the transaction.
 * <p>
 * - Create an instant of {@link TransactionSession} which is used for spawning/factory {@link TransactionProcessor}
 * <p>
 * - Create an instant of {@link TransactionProcessor} from the instant of {@link TransactionSession} above by calling {@link TransactionSession#getTransactionProcessor()} or {@link TransactionSession#getTransactionProcessor(Transaction)} if desire to use a preset {@link Transaction} object.
 * <p>
 * - Call {@link TransactionProcessor#prepare(List)} with a list of Actions which is desired to be sent to backend. The method will serialize the list of action to list of hex and keep them inside
 * the list of {@link Transaction#getActions()}. The transaction now is ready to be signed and broadcast.
 * <p>
 * - Call {@link TransactionProcessor#signAndBroadcast()} to sign the transaction inside {@link TransactionProcessor} and broadcast it to backend.
 */
public class TransactionTask extends AsyncTask<String, String, Void> {

    /**
     * Whether the network logs will be enabled for RPC provider
     */
    private static final boolean ENABLE_NETWORK_LOG = true;

    private CheckDataTaskCallback callback;
    private final EosioJavaRpcProviderImpl eosioJavaRpcProvider;

    public TransactionTask(@NonNull EosioJavaRpcProviderImpl rpcProvider, CheckDataTaskCallback callback) {
        this.callback = callback;
        this.eosioJavaRpcProvider = rpcProvider;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values.length == 1) {
            String message = values[0];
            this.callback.update(message);
        } else if (values.length == 2) {
            boolean isSuccess = Boolean.parseBoolean(values[0]);
            String message = values[1];
            this.callback.finish(isSuccess, message, null);
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        String action = params[0];
        String privateKey = params[1];
        String fromAccount = params[2];
        String code = params[3];
        String jsonData = params[4];
        LogUtils.e(Constants.LOG_TAG, "TransactionTask name = " + action + " privateKey = " + privateKey + " fromAccount = " + fromAccount + " jsonData = " + jsonData);
        this.publishProgress("TransactionTask " + action);
        // Creating serialization provider
        ISerializationProvider serializationProvider;
        try {
            serializationProvider = new AbiEosSerializationProviderImpl();
        } catch (SerializationProviderError serializationProviderError) {
            serializationProviderError.printStackTrace();
            return null;
        }
        // Creating RPC Provider
        IRPCProvider rpcProvider = eosioJavaRpcProvider;
        // Creating ABI provider
        IABIProvider abiProvider = new ABIProviderImpl(rpcProvider, serializationProvider);
        // Creating Signature provider
        ISignatureProvider signatureProvider = new SoftKeySignatureProviderImpl();
        try {
            ((SoftKeySignatureProviderImpl) signatureProvider).importKey(privateKey);
        } catch (ImportKeyError importKeyError) {
            importKeyError.printStackTrace();
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask importKeyError = " + importKeyError.getMessage());
            this.publishProgress(Boolean.toString(false), importKeyError.getMessage());
            return null;
        }
        // Creating TransactionProcess
        TransactionSession session = new TransactionSession(serializationProvider, rpcProvider, abiProvider, signatureProvider);
        TransactionProcessor processor = session.getTransactionProcessor();
        // Apply transaction data to Action's data

        // Creating action with action's data, eosio.token contract and transfer action.
        Action mAction = new Action(code, action, Collections.singletonList(new Authorization(fromAccount, "active")), jsonData);
        try {
            // Prepare transaction with above action. A transaction can be executed with multiple action.
            this.publishProgress("Preparing Transaction...");
            processor.prepare(Collections.singletonList(mAction));
            // Sign and broadcast the transaction.
            this.publishProgress("Signing and Broadcasting Transaction...");
            PushTransactionResponse response = processor.signAndBroadcast();
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask getTransactionId = " + response.getTransactionId());
            this.publishProgress(Boolean.toString(true), response.getTransactionId());
        } catch (TransactionPrepareError transactionPrepareError) {
            // Happens if preparing transaction unsuccessful
            transactionPrepareError.printStackTrace();
            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask TransactionPrepareError = " + transactionPrepareError.getMessage());
            this.publishProgress(Boolean.toString(false), transactionPrepareError.getLocalizedMessage());
        } catch (TransactionSignAndBroadCastError transactionSignAndBroadCastError) {
            // Happens if Sign transaction or broadcast transaction unsuccessful.
            transactionSignAndBroadCastError.printStackTrace();
            // try to get backend error if the error come from backend
            RPCResponseError rpcResponseError = ErrorUtils.getBackendError(transactionSignAndBroadCastError);
            if (rpcResponseError != null) {
                String errorDetail = ErrorUtils.getErrorDetail(rpcResponseError);
                LogUtils.dTag(Constants.LOG_TAG, "TransactionTask backendErrorMessage = " + errorDetail);
                this.publishProgress(Boolean.toString(false), errorDetail);
                return null;
            }

            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask TransactionSignAndBroadCastError asJsonString= " + transactionSignAndBroadCastError.asJsonString());

            LogUtils.dTag(Constants.LOG_TAG, "TransactionTask TransactionSignAndBroadCastError = " + transactionSignAndBroadCastError.getMessage());
            this.publishProgress(Boolean.toString(false), transactionSignAndBroadCastError.getMessage());
        }
        return null;
    }
}
