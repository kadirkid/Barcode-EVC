package iz.iz.Model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by abdulahiosoble on 2/27/18.
 */

public class Transaction {
    private String CashierID;
    private String TransactionID;
    private String Method;
    private ArrayList<Item> Items;

    public Transaction() {
    }

    public Transaction(String mCashierID, String mUUID, String method, ArrayList<Item> items) {
        this.CashierID = mCashierID;
        this.TransactionID = mUUID;
        this.Method = method;
        this.Items = items;
    }

    public String getCashierID() {
        return CashierID;
    }

    public void setCashierID(String cashierID) {
        CashierID = cashierID;
    }

    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID() {
        this.TransactionID = UUID.randomUUID().toString();
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        this.Method = method;
    }

    public ArrayList<Item> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Item> items) {
        this.Items = items;
    }

}
