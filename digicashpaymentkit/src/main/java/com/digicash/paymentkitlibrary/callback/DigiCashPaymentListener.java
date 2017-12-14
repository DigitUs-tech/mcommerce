package com.digicash.paymentkitlibrary.callback;

public interface DigiCashPaymentListener {
    void onError(Throwable th);

    void onPaymentFailed();

    void onPaymentSuccessfullySend();

    void onPaymentPending();

    void onPaymentSuccessfullyConfirmed();
}
