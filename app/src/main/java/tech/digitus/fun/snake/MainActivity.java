package tech.digitus.fun.snake;

import android.content.Intent;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PAY_SDK_REQUEST_CODE=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Start_New_Btn=(Button)findViewById(R.id.button);


        Start_New_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Snake is coming",Toast.LENGTH_SHORT).show();
                Intent game_intent= new Intent(MainActivity.this,GameActivity.class);
                startActivity(game_intent);
            }
        });

        Button pay_Btn=(Button)findViewById(R.id.payRequestButton);


        pay_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Snake is coming",Toast.LENGTH_SHORT).show();
                Intent pay_intent= new Intent(MainActivity.this,BillRequestActivity.class);
                startActivityForResult(pay_intent, PAY_SDK_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PAY_SDK_REQUEST_CODE) {
            //Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            Intent game_intent= new Intent(MainActivity.this,GameActivity.class);
            startActivity(game_intent);
        }
    }

}
