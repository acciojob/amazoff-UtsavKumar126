package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    Set<Order> orders;
    Set<DeliveryPartner>deliveryPartners;

    Map<DeliveryPartner,ArrayList<Order>>orderSet;
    Map<DeliveryPartner,Order>orderDeliveryPartnerMap;
    public OrderRepository() {
        this.orders = new HashSet<>();
        this.deliveryPartners = new HashSet<>();
        this.orderSet = new HashMap<>();
        this.orderDeliveryPartnerMap = new HashMap<>();
    }

    public void addOrder(Order order) {

        orders.add(order);
        //return "New order added successfully";
    }

    public void addPartner(String partnerId) {

        deliveryPartners.add(new DeliveryPartner(partnerId));
        //return "New delivery partner added successfully";
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        Order order=null;
        DeliveryPartner partner=null;
        for(Order o:orders){
            if(o.getId().equals(orderId)){
                order=o;
                break;
            }
        }
        for(DeliveryPartner d:deliveryPartners){
            if(d.getId().equals(partnerId)){
                partner=d;
                break;
            }
        }
        if(orderSet.containsKey(partner)){
            List<Order>current=orderSet.get(partner);
            current.add(order);
            orderSet.put(partner, (ArrayList<Order>) current);
        }
        else{
            orderSet.put(partner,new ArrayList<>());
            orderSet.get(partner).add(order);
        }
        //orderDeliveryPartnerMap.put(partner,add);
        //return "New order-partner pair added successfully";
    }

    public Order getOrderById(String orderId) {
        for(Order o:orders){
            if(o.getId().equals(orderId)) {
                return o;
            }
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        DeliveryPartner curr=null;
        for(DeliveryPartner d:deliveryPartners){
            if(d.getId().equals(partnerId)) {
                return d;
            }
        }
        return null;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        for(DeliveryPartner d:orderSet.keySet()){
            if(d.getId().equals(partnerId)){
                return orderSet.get(d).size();
            }
        }
        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String>ordersbyseller=new ArrayList<>();
        List<Order>ordersbySeller=new ArrayList<>();
        for(DeliveryPartner d:orderSet.keySet()){
            if(d.getId().equals(partnerId)){
                ordersbySeller=orderSet.get(d);
            }
        }
        for(Order o:ordersbySeller){
            ordersbyseller.add(o.getId());
        }
        return ordersbyseller;
    }

    public List<String> getAllOrders() {
        List<String>allOrders=new ArrayList<>();
        for(Order o:orders){
            allOrders.add(o.getId());
        }
        return allOrders;
    }

    public Integer getCountOfUnassignedOrders() {

        int count=0;
        for(DeliveryPartner d:orderSet.keySet()){
            List<Order>orderList=orderSet.get(d);
            count+=orderList.size();
        }

        return orders.size()-count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int count=0;
        String arr[]=time.split(":");
        int hrs=Integer.parseInt(arr[0]);
        int min=Integer.parseInt(arr[1]);
        int orderTime=hrs*60+min;
        List<Order>orderList=new ArrayList<>();
        for(DeliveryPartner d:orderSet.keySet()){
            if(d.getId().equals(partnerId)){
                orderList=orderSet.get(d);
            }
        }
        for(Order order:orderList){
            if(order.getDeliveryTime()>orderTime){
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int lastdeliverytime=0;
        List<Order>orderList=new ArrayList<>();
        for(DeliveryPartner d:orderSet.keySet()){
            if(d.getId().equals(partnerId)){
                orderList=orderSet.get(d);
            }
        }
        for(Order order:orderList){
            lastdeliverytime=Math.max(lastdeliverytime,order.getDeliveryTime());
        }
        int hrs=lastdeliverytime/60;
        int min=lastdeliverytime%60;

        if(hrs<10){
            if(min<10)
            return "0"+hrs+":"+"0"+min;
            else
            return "0"+hrs+":"+min;
        }
        else{
            if(min<10)
            return hrs+":"+"0"+min;
            else
            return hrs+":"+min;
        }
    }
    public void  deletePartnerById(String partnerId) {
        DeliveryPartner partner=null;
        for(DeliveryPartner d:orderSet.keySet()){
            if(d.getId().equals(partnerId)){
                partner=d;
                orderSet.remove(d);
            }
        }
        deliveryPartners.remove(partner);
       // return "removed successfully";
    }
    public void deleteOrderById(String orderId) {

        Order removed=null;
        for(Order o:orders){
            if(o.getId().equals(orderId)){
                removed=o;
            }
        }
        orders.remove(removed);
        for(DeliveryPartner d:orderSet.keySet()){
            if(orderSet.get(d).contains(removed)){
                orderSet.get(d).remove(removed);
            }
        }
        //return "removed successfully";
    }
}
