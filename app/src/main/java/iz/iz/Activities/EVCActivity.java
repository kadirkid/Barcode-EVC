package iz.iz.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import iz.iz.Model.Item;
import iz.iz.Model.Transaction;
import iz.iz.R;
import iz.iz.Utils.SmsListener;
import iz.iz.Utils.SmsReceiver;

public class EVCActivity extends AppCompatActivity {
    private double sum;
    private ArrayList<Item> itemList;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private DatabaseReference childref;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evc);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        childref = databaseReference.child("Transaction");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting for the Money to be sent...");
        progressDialog.show();

        try{
            
            sum = getIntent().getDoubleExtra("Sum", 0.0);
            Bundle bundle = getIntent().getBundleExtra("bundle");
            itemList = bundle.getParcelableArrayList("list");
            
        }catch (Exception e){
            e.printStackTrace();
        }

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                progressDialog.dismiss();

                //Eliminate characters to the left of the value
                String[] s = messageText.split("$");    
                String q = s[1];
                
                //Eliminate characters to the right of the value
                s = q.split(" ");
                q = s[0];
                
                double sentAmount = Double.parseDouble(q);
                if(sentAmount == sum){
                    addTransactionToDatabase();
                    buildSuccessfulDialog();
                }
            }
        });

    }

    private void addTransactionToDatabase() {
        progressDialog.setMessage("Finishing up...");
        progressDialog.show();

        Transaction transaction = new Transaction();

        transaction.setTransactionID();
        transaction.setCashierID(auth.getCurrentUser().getUid());
        transaction.setItems(itemList);
        transaction.setMethod("EVC");

        childref.child(transaction.getTransactionID()).child("EVC").setValue(transaction)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()) {
                            Snackbar.make(getCurrentFocus(), "Transaction has been completed",
                                    Snackbar.LENGTH_SHORT);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Failed to add transaction" +
                                            " to the database",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void buildSuccessfulDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Done");
        builder.setMessage("The total amount has been received.");

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(EVCActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
}
