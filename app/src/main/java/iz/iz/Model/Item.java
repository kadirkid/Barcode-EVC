package iz.iz.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abdulahiosoble on 2/17/18.
 */

public class Item implements Parcelable{
    private String barcode;
    private String name;
    private double price;

    public Item(Parcel in) {
        barcode = in.readString();
        name = in.readString();
        price = in.readDouble();
    }

    public Item() {
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(barcode);
        parcel.writeString(name);
        parcel.writeDouble(price);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>()
    {
        public Item createFromParcel(Parcel in)
        {
            return new Item(in);
        }
        public Item[] newArray(int size)
        {
            return new Item[size];
        }
    };
}
