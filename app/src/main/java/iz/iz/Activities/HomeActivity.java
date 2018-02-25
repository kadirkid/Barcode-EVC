package iz.iz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import iz.iz.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ZXingScannerView mScannerView;
    private Button mScan, mAdd;
    private TextView format, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mScan = findViewById(R.id.scan_button);
        mAdd = findViewById(R.id.add_button);

        format = findViewById(R.id.scan_format);
        content = findViewById(R.id.scan_content);

        mScan.setOnClickListener(this);
        mAdd.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scan_button:
                startActivity(new Intent(HomeActivity.this, ScanActivity.class));
                break;
            case R.id.add_button:
                startActivity(new Intent(HomeActivity.this, AddActivity.class));
                break;
        }
    }
}
