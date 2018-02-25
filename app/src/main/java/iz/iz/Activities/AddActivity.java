package iz.iz.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import iz.iz.Model.Item;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AddActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private DatabaseReference childref;
    private ProgressDialog progressDialog;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childref = databaseReference.child("Item");
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void handleResult(Result result) {
        // Do something with the result here
        Log.v("TAG", result.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("TAG", result.getBarcodeFormat().toString());

        item = new Item();
        item.setBarcode(result.getText());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Give name");
        builder.setMessage(result.getText());
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText name = new EditText(this);
        name.setHint("Name");
        layout.addView(name); // Notice this is an add method

        final EditText price = new EditText(this);
        price.setInputType(InputType.TYPE_CLASS_NUMBER);
        price.setHint("Price");
        layout.addView(price); // Another add method

        builder.setView(layout); // Again this is a set method, not add
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setName(name.getText().toString());
                item.setPrice(Double.parseDouble(price.getText().toString()));
                addItem(item);
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();

        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
    }

    public void addItem(Item item){

        progressDialog.setMessage("Adding Item...");
        progressDialog.show();

        childref.child(item.getBarcode()).setValue(item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            Snackbar.make(mScannerView, "Item has been added",
                                    Snackbar.LENGTH_SHORT);
                            startActivity(new Intent(AddActivity.this, HomeActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Failed to add Item to " +
                                            "the database",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
