package com.test.a7ara.point_of_sale;

import android.content.ClipData;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PointOfSaleRow implements Parcelable {
    private String ID;
    private String Code;
    private String Name;
    private Double RetailPrice;
    private Double WholesalePrice;
    private Boolean MixedWholesale;
    private Double Count;
    private Double Sum;
    private Integer ByWeight;
    private Double Remaining;
    private ArrayList<InventoryWholesalePrices> Prices;
    private ArrayList<InventoryWholesalePrices> PricesWS;
    private Integer PagePos;
    private Integer ItemPos;

    public PointOfSaleRow(String id,String code,String name,Double price, Double wholesale_price, Boolean mixed,
                          Double count,Integer by_weight , Double inv,ArrayList<InventoryWholesalePrices> prices){
        ID = id;
        Code = code;
        Name = name;
        RetailPrice = price;
        WholesalePrice = wholesale_price;
        MixedWholesale = mixed;
        Count = count;
        Sum = price*count;
        ByWeight = by_weight;
        Remaining = inv;
        Prices = prices;
        PricesWS = new ArrayList<>();
    }

    public void setPrices(ArrayList<InventoryWholesalePrices> prices) {
        Prices = prices;
    }

    public ArrayList<InventoryWholesalePrices> getPrices() {
        return Prices;
    }

    public ArrayList<InventoryWholesalePrices> getWSPrices() {
        return PricesWS;
    }

    public void addprice(ArrayList<ItemInventory> ItInv, Double amount){
        InventoryWholesalePrices price = getavailableprice(ID , ItInv);
        boolean isnew = true;
        for (int i = 0 ; i<PricesWS.size() ; i++){
            if(PricesWS.get(i).getInsertionID().equals(price.getInsertionID())){
                if(!PricesWS.get(i).getRemainingCount().equals(PricesWS.get(i).getCount())){
                    PricesWS.get(i).additem(amount);
                    isnew = false;
                }
            }
        }
        if(isnew){
            PricesWS.add(price);
        }
    }

    private InventoryWholesalePrices getavailableprice(String id, ArrayList<ItemInventory> ItemInvs) {
        for (int i =0 ; i<ItemInvs.size() ; i++){
            if(ItemInvs.get(i).getItemID().equals(id)){
                for(int ii = 0 ; ii<ItemInvs.get(i).getWholesalePrice().size() ; ii++){
                    if(ItemInvs.get(i).getWholesalePrice().get(ii).getRemainingCount()>0){
                        return new InventoryWholesalePrices(id,ItemInvs.get(i).getWholesalePrice().get(ii).getPrice(),ItemInvs.get(i).getWholesalePrice().get(ii).getCount(),1.0,ItemInvs.get(i).getWholesalePrice().get(ii).getInventoryInsertionDate());
                    }
                }
            }
        }
        return null;
    }

    public void removeprice(Double amount){
        Double toberemoved = amount;
        for (int i = PricesWS.size() - 1 ; i>=0 ; i--){
            if(toberemoved>0 && PricesWS.get(i).getRemainingCount() > 0){
                if(PricesWS.get(i).getRemainingCount()>toberemoved){
                    PricesWS.get(i).removeitem(amount);
                    toberemoved = 0.0;
                }else{
                    toberemoved = toberemoved - PricesWS.get(i).getRemainingCount();
                    PricesWS.get(i).setRemainingCount(0.0);
                }
            }
            if(PricesWS.get(i).getRemainingCount()==0){
                PricesWS.remove(i);
            }
        }
    }

    public Double getRemaining() {
        return Remaining;
    }

    public void setRemaining(Double inv) {
        Remaining = inv;
    }

    protected PointOfSaleRow(Parcel in) {
        Code = in.readString();
        Name = in.readString();
        if (in.readByte() == 0) {
            RetailPrice = null;
        } else {
            RetailPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            WholesalePrice = null;
        } else {
            WholesalePrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            Count = null;
        } else {
            Count = in.readDouble();
        }
        if (in.readByte() == 0) {
            Sum = null;
        } else {
            Sum = in.readDouble();
        }
        ByWeight = in.readInt();
    }

    public Integer getByWeight() {
        return ByWeight;
    }

    public static final Creator<PointOfSaleRow> CREATOR = new Creator<PointOfSaleRow>() {
        @Override
        public PointOfSaleRow createFromParcel(Parcel in) {
            return new PointOfSaleRow(in);
        }

        @Override
        public PointOfSaleRow[] newArray(int size) {
            return new PointOfSaleRow[size];
        }
    };

    public String getID() {
        return ID;
    }

    public String getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }



    public void setCount(Double count) { Count = count;}

    public String getCountWithPrefix() {
        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(0);
        if(ByWeight == 1){
            return fr.format(Count) + " غ";
        }
        else{
            return fr.format(Count) ;
        }
    }

    public String getRemainingWithPrefix() {
        NumberFormat fr = NumberFormat.getInstance(Locale.ENGLISH);
        fr.setMaximumFractionDigits(0);
        if(ByWeight == 1){
            return fr.format(Remaining) + " غ";
        }
        else{
            return fr.format(Remaining) ;
        }
    }

    public Double getPrice() {
        return RetailPrice;
    }

    public Double getRowPrice() {
        return RetailPrice * Count;
    }

    public Double getWholesalePrice() {
        return WholesalePrice;
    }

    public Double getCount() {
        return Count;
    }

    public void addtoCount() {
        Count = Count + 1;
    }

    public void removefromCount() {
        if(Count > 1){
            Count = Count - 1;
        }else{
            Count = 1.0;
        }
    }

    public Double getTotalProfit(){
        return getPieceProfit() * Count;
    }

    public Double getPieceProfit(){
        return RetailPrice - WholesalePrice;
    }

    public Double getTotalProfitPercent(){
        return (getPieceProfit() / RetailPrice) * 100;
    }

    public Double getSum() {
        return RetailPrice * Count;
    }

    public Double getWholesaleSum() {
        return WholesalePrice * Count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(Code);
        dest.writeString(Name);
        if (RetailPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(RetailPrice);
        }
        if (WholesalePrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(WholesalePrice);
        }
        if (Count == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Count);
        }
        if (Sum == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Sum);
        }

        dest.writeInt((ByWeight));
    }
}
