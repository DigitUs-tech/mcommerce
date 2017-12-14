package com.digicash.paymentkitlibrary.builder;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.digicash.paymentkitlibrary.callback.DigiCashPaymentListener;
import com.digicash.paymentkitlibrary.constant.PaymentConfig;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PaymentBuilder {
    private static PaymentBuilder mPaymentBuilder;

    private PaymentRequest paymentRequest;

    private String apikey;
    private String digicode;
    private String walletName;
    private Activity mActivity;
    private String walletNym;
    public DigiCashPaymentListener paymentListener;

    private Response response;
    private OkHttpClient client;
    private Request request;

    private PaymentBuilder() {
    }

    public static PaymentBuilder createWith(Activity context, DigiCashPaymentListener paymentListener) {
        mPaymentBuilder = new PaymentBuilder();
        mPaymentBuilder.mActivity = context;
        mPaymentBuilder.paymentListener = paymentListener;
        mPaymentBuilder.client = new OkHttpClient();

        return mPaymentBuilder;
    }

    public PaymentBuilder addMerchantId(String walletNym) {
        mPaymentBuilder.walletNym = walletNym;
        return mPaymentBuilder;
    }

    public PaymentBuilder addDigiCode(String digiCode) {
        mPaymentBuilder.digicode = digiCode;
        return mPaymentBuilder;
    }

    public PaymentBuilder addApiKey(String apiKey) {
        mPaymentBuilder.apikey = apiKey;
        return mPaymentBuilder;
    }

    public PaymentBuilder addMerchantName(String name) {
        mPaymentBuilder.walletName = name;
        return mPaymentBuilder;
    }

    public PaymentRequest preparePayment(@NonNull long amount, @Nullable String transactionId) throws Exception {

        if (walletNym == null)
            throw new Exception("Wallet nym cannot be empty");
        if (apikey == null)
            throw new Exception("apiKey cannot be empty");
        if (digicode == null)
            throw new Exception("digiCode cannot be empty");

        if (amount == 0)
            throw new Exception("amount cannot be null");

        paymentRequest = new PaymentRequest(this);
        paymentRequest.setAmount(amount);
        if (transactionId != null)
            paymentRequest.setTransactionId(transactionId);
        else
            paymentRequest.setTransactionId(paymentRequest.generateTransactionId());

        request = new Request.Builder()
                .url(preparePaymentUrl())
                .addHeader(PaymentConfig.DIGI_CODE, mPaymentBuilder.getDigicode())
                .addHeader(PaymentConfig.API_KEY, mPaymentBuilder.getApikey())
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = client.newCall(request).execute();
                    Log.d("post mtid", ": " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return paymentRequest;
    }

    public PaymentBuilder addPaymentListener(DigiCashPaymentListener paymentListener) {
        mPaymentBuilder.paymentListener = paymentListener;
        return mPaymentBuilder;
    }

    public String preparePaymentUrl() {
        //"heron.digitus.me:32000/wallet1.0/958FHemu1si63hPShvy2ygrRSGJgTudTArG1PzfdKrhz/requestmoney?mtid='xxxxxxx'&amount='xxxxx'&optype=charge"
        return PaymentConfig.WALLET_API_URL
                + ":" + PaymentConfig.WALLET_API_PORT
                + "/" + PaymentConfig.CLOUD_WALLET_VERSION
                + "/" + getWalletNym()
                + "/" + PaymentConfig.REQUEST_REQUEST_PAYMENT_PATH
                + "?" + PaymentConfig.KEY_MERCHANT_TRANSACTION_ID + "=" + paymentRequest.getTransactionId()
                + "&" + PaymentConfig.AMOUNT_KEY + "=" + paymentRequest.getAmount()
                + "&" + PaymentConfig.OPERATION_TYPE_KEY + "=" + PaymentConfig.OPERATION_TYPE_CHARGE;
    }


    public void onPaymentResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PaymentConfig.KEY_PAYMENT_CALL_REQUEST_CODE /*11204*/:
                if (resultCode == -1 && data != null && data.getExtras().getBoolean(PaymentConfig.KEY_PAYMENT_RESULT)) {
                    this.paymentListener.onPaymentSuccessfullySend();
                    return;
                }
            default:
                this.paymentListener.onError(new Throwable("It's not a valid payment request!"));
                break;
        }
        this.paymentListener.onPaymentFailed();
    }

    public Activity getActivity() {
        return mActivity;
    }

    public String getWalletName() {
        return walletName;
    }

    public String getWalletNym() {
        return walletNym;
    }

    public String getApikey() {
        return apikey;
    }

    public String getDigicode() {
        return digicode;
    }
}
