package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        this.id=id;
        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        int hrs= Integer.parseInt(deliveryTime.substring(0,2));
        int min= Integer.parseInt(deliveryTime.substring(3,deliveryTime.length()));

        this.deliveryTime=hrs*60+min;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
