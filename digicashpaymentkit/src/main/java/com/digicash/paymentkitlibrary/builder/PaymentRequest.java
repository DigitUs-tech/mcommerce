package com.digicash.paymentkitlibrary.builder;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.digicash.paymentkitlibrary.constant.PaymentConfig;
import com.digicash.paymentkitlibrary.model.TransactionStatus;
import com.digicash.paymentkitlibrary.util.CurrencyFormatter;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hechmi on 12/13/17.
 */

public class PaymentRequest implements Serializable {

    private PaymentBuilder paymentBuilder;

    private long amount;
    private String transactionId;


    private Response response;
    private OkHttpClient client;
    private Request request;

    // count the verification attempt if the transaction status pending
    private int verificationAttempt = 0;
    private Runnable verifyRunnable;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = null;
            try {
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("response", result);
            Gson gson = new Gson();

            TransactionStatus[] transactionStatus = gson.fromJson(result, TransactionStatus[].class);
            if (transactionStatus != null)
                switch (transactionStatus[0].getState()) {
                    case SUCCESS:
                        paymentBuilder.paymentListener.onPaymentSuccessfullyConfirmed();
                        break;
                    case PENDING:
                        if (verificationAttempt < 3)
                            postDelayed(new Thread(verifyRunnable), PaymentConfig.VERIFICATION_ATTEMPT_DELAY);
                        else
                            paymentBuilder.paymentListener.onPaymentPending();
                        break;
                    case ERROR:
                        paymentBuilder.paymentListener.onError(new Throwable("an error has occurred!"));
                        break;
                }
        }
    };

    private PaymentRequest() {

    }

    public PaymentRequest(PaymentBuilder builder) {
        this.paymentBuilder = builder;
    }


    public void setPaymentBuilder(PaymentBuilder paymentBuilder) {
        this.paymentBuilder = paymentBuilder;
    }

    public long getAmount() {
        return amount;
    }

    public String getAmountAsString() {
        return new CurrencyFormatter().formatAmount(amount);
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String generateTransactionId() {
        this.transactionId = UUID.randomUUID().toString();
        return this.transactionId;
    }

    public void verifyPayment() throws Exception {
        client = new OkHttpClient();
        request = new Request.Builder()
                .url(prepareVerificationUrl())
                .addHeader(PaymentConfig.DIGI_CODE, paymentBuilder.getDigicode())
                .addHeader(PaymentConfig.API_KEY, paymentBuilder.getApikey())
                .build();

        verify();
    }

    private void verify() throws Exception {
        verifyRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    verificationAttempt++;
                    response = client.newCall(request).execute();
                    handler.sendEmptyMessage(verificationAttempt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(verifyRunnable).start();
    }

    private String prepareVerificationUrl() {
        return PaymentConfig.WALLET_API_URL
                + ":" + PaymentConfig.WALLET_API_PORT
                + "/" + PaymentConfig.CLOUD_WALLET_VERSION
                + "/" + paymentBuilder.getWalletNym()
                + "/" + PaymentConfig.REQUEST_CHARGE_PAY_PATH
                + "/" + transactionId /*"0ef4f9e0-de82-11e7-b29d-add2527cbf9a"*/;
    }

    public void pay() {
        Uri paymentUri = Uri.parse(PaymentConfig.PAYMENT_URI_PREFIX).buildUpon()
                .appendQueryParameter(PaymentConfig.KEY_PAYMENT_AMOUNT, getAmountAsString())
                .appendQueryParameter(PaymentConfig.KEY_MERCHANT_TRANSACTION_ID, this.transactionId)
                .appendQueryParameter(PaymentConfig.KEY_PAYMENT_MERCHANT_ID, paymentBuilder.getWalletNym())
                .appendQueryParameter(PaymentConfig.KEY_PAYMENT_MERCHANT_NAME, paymentBuilder.getWalletName())
                .build();
        Intent paymentIntent = new Intent(PaymentConfig.PAYMENT_ACTION_CALL);
        paymentIntent.setData(paymentUri);
        paymentBuilder.getActivity().startActivityForResult(paymentIntent, PaymentConfig.KEY_PAYMENT_CALL_REQUEST_CODE);
    }
}
