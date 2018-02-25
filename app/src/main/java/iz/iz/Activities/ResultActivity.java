package iz.iz.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import iz.iz.Adapter.ItemAdapter;
import iz.iz.Model.Item;
import iz.iz.R;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Item> itemList;
    private RecyclerView recyclerView;
    private Button cash, evc;
    private DividerItemDecoration mDividerItemDecoration;
    private ItemAdapter mAdapter;
    private Double sum = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        recyclerView = findViewById(R.id.recyclerView);

        try{
            Bundle bundle = getIntent().getBundleExtra("bundle");
            itemList = bundle.getParcelableArrayList("list");
            Log.i(Integer.toString(itemList.size()), "---------------------");
            Log.i("List RECEIVED", "------------------------");
        }catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(this, "No List received", Toast.LENGTH_SHORT).show();
            Log.i("THERE IS NO LIST", "----------------------");
        }
        //Getting the sum
        for(Item item: itemList){
            sum += item.getPrice();
        }

        cash = findViewById(R.id.btn_cash);
        evc = findViewById(R.id.btn_evc);

        cash.setOnClickListener(this);
        evc.setOnClickListener(this);

        mDividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), LinearLayout.HORIZONTAL);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_line));

        mAdapter = new ItemAdapter(itemList, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(mDividerItemDecoration);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cash:
                showTotal(sum);
                break;

            case R.id.btn_evc:
                Intent intent = new Intent(this, EVCActivity.class);
                intent.putExtra("Sum", sum);
                startActivity(intent);
                break;
        }
    }

    private void showTotal(final Double sum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Total");
        builder.setMessage(Double.toString(sum));
        final EditText amount_received = new EditText(this);
        amount_received.setInputType(InputType.TYPE_CLASS_NUMBER);
        amount_received.setHint("Amount Received");

        builder.setView(amount_received);
        builder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Double change = Double.parseDouble(amount_received.getText().toString());
                change -= sum;
                showRemaining(change);
            }
        });

        AlertDialog alert1 = builder.create();
        alert1.show();
    }

    private void showRemaining(double v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Change");
        builder.setMessage(Double.toString(v));

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
}
