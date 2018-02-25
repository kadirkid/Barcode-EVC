package iz.iz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;

import iz.iz.Model.Item;
import iz.iz.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private DatabaseReference databaseReference;
    private DatabaseReference childref;
    private Item item;
    private FirebaseAuth auth;
    private Button next;
    private ArrayList<Item> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
//        mScannerView = new ZXingScannerView(this);
        mScannerView = findViewById(R.id.zxscan);
        mScannerView.setResultHandler(this);

        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childref = databaseReference.child("Item");

        next = findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list != null){
                    Intent intent = new Intent(ScanActivity.this,
                            ResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", list);
                    intent.putExtra("bundle", bundle);

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void handleResult(Result result) {
        // Do something with the result here
        String barcode = result.getText();
        childref.child(barcode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    if(dataSnapshot != null){
                        try{
                            item = dataSnapshot.getValue(Item.class);
                            Toast.makeText(getApplicationContext(), item.getName(),
                                    Toast.LENGTH_SHORT).show();
                            list.add(item);
                            Log.i("----------------------",Integer.toString(list.size()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Does not exist!!",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.v("TAG", result.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("TAG", result.getBarcodeFormat().toString());
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Scan Result");
        builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
*/
        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }
}
