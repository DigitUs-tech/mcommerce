package tech.digitus.fun.snake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.digicash.paymentkitlibrary.builder.PaymentBuilder;
import com.digicash.paymentkitlibrary.builder.PaymentRequest;
import com.digicash.paymentkitlibrary.callback.DigiCashPaymentListener;
import com.digicash.paymentkitlibrary.util.CurrencyFormatter;

public class BillRequestActivity extends AppCompatActivity implements DigiCashPaymentListener {
    private PaymentBuilder myPaymentBuilder;

    private TextView billamount;
    private Button billconfirm, btnStartPlaying;
    private PaymentRequest paymentRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_request);

        billamount = (TextView) findViewById(R.id.billamount);

        billconfirm = (Button) findViewById(R.id.billconfirm);
        btnStartPlaying = (Button) findViewById(R.id.btnStartPlaying);

        btnStartPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent game_intent = new Intent(BillRequestActivity.this, GameActivity.class);
                startActivity(game_intent);
            }
        });
    }

    public void onPayClick(View view) {
        long amount = 100; // 0.100 TND

        myPaymentBuilder = PaymentBuilder.createWith(this, this)
                .addMerchantName("Snake Game")
                .addApiKey("a893fa05-0026-4498-ba18-fc471011529d")
                .addDigiCode("diz4LjvVvS5gyAWt")
                .addMerchantId("6QDyoSy9AShWbvkbmMSXdJmoVT8vvtdgDVuXZVKMPEXp"); // salah hechmi heron

        try {
            paymentRequest = myPaymentBuilder.preparePayment(amount, null);
            paymentRequest.pay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myPaymentBuilder.onPaymentResult(requestCode, resultCode, data);
    }

    public void onPaymentSuccessfullySend() {
        billamount.setText("Votre paiement en cours de validation");
        //start the payment verification

        try {
            this.paymentRequest.verifyPayment();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onPaymentFailed() {
        btnStartPlaying.setVisibility(View.GONE);
        billconfirm.setVisibility(View.VISIBLE);
        billamount.setText("Paiement non reçu!");
    }

    public void onError(Throwable throwable) {
    }

    public void onPaymentSuccessfullyConfirmed() {
        billamount.setText("Paiement effectué! vous pouvez commencer la partie");
        btnStartPlaying.setVisibility(View.VISIBLE);
        billconfirm.setVisibility(View.GONE);
    }

    public void onPaymentPending() {

    }
}

