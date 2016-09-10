package com.example.android.inventorytracker;

/**
 * Created by Jaren Lynch on 9/10/2016.
 */
public class InventoryItem {

    //Name of item
    private String mItemName;

    //Price of item
    private Integer mItemPrice;

    //Quantity of stock of item
    private Integer mItemStock;

    /**
     * @param name is the name of the item
     * @param price is the price of the item
     * @param stock is the stock remaining of the item
     */
    public InventoryItem(String name, Integer price, Integer stock) {
        mItemName = name;
        mItemPrice = price;
        mItemStock = stock;
    }

    /**
     * @return the name of the item
     */
    public String getItemName() {
        return mItemName;
    }

    /**
     * @return the price of the item
     */
    public Integer getItemPrice() {
        return mItemPrice;
    }

    /**
     * @return the stock of the item
     */
    public Integer getItemStock() {
        return mItemStock;
    }
}
