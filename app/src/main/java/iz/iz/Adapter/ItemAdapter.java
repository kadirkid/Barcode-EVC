package iz.iz.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import iz.iz.Model.Item;
import iz.iz.R;

/**
 * Created by abdulahiosoble on 2/20/18.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder>{
    private ArrayList<Item> itemList;
    private Context mContext;

    public ItemAdapter(ArrayList<Item> itemList, Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_adapter, parent, false);
        Log.i("ItemAdapter----------","---------OnCreateViewHolder--------");

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Item item = itemList.get(position);
        Log.i("OnBindViewHolder", "was Called-------------");
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView amount;
        public ItemHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.adapter_name);
            amount = itemView.findViewById(R.id.adapter_amount);
        }

        public void bind(Item item){
            name.setText(item.getName());
            amount.setText(Double.toString(item.getPrice()));
        }
    }
}
