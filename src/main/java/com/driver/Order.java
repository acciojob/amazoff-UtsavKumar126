package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        this.id=id;
        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        String arr[]=deliveryTime.split(":");
        int hrs=Integer.valueOf(arr[0]);
        int min=Integer.valueOf(arr[1]);

        this.deliveryTime=hrs*60+min;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
