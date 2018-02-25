package iz.iz.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import iz.iz.R;
import iz.iz.Utils.SmsListener;
import iz.iz.Utils.SmsReceiver;

public class EVCActivity extends AppCompatActivity {
    private double sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evc);

        try{
            sum = getIntent().getDoubleExtra("Sum", 0.0);
        }catch (Exception e){
            e.printStackTrace();
        }

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                String[] s = messageText.split("$");
                String q = s[1];
                s = q.split(" ");
                q = s[0];


            }
        });

    }
}
