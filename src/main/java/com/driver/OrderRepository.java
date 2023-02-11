package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    Set<Order> orders=new HashSet<>();
    Set<DeliveryPartner>deliveryPartners=new HashSet<>();

    Map<DeliveryPartner, ArrayList<Order>>orderSet=new HashMap<>();
    Map<DeliveryPartner,Order>orderDeliveryPartnerMap=new HashMap<>();

    public String addOrder(Order order) {

        orders.add(order);
        return "New order added successfully";
    }

    public String addPartner(String partnerId) {

        deliveryPartners.add(new DeliveryPartner(partnerId));
        return "New delivery partner added successfully";
    }

    public String addOrderPartnerPair(String orderId, String partnerId) {
        Order add=null;
        DeliveryPartner partner=null;
        for(Order o:orders){
            if(o.getId().equals(orderId)){
                add=o;
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
            current.add(add);
            orderSet.put(partner, (ArrayList<Order>) current);
        }
        else{
            orderSet.put(partner,new ArrayList<>());
            orderSet.get(partner).add(add);
        }
        orderDeliveryPartnerMap.put(partner,add);
        return "New order-partner pair added successfully";
    }

    public Order getOrderById(String orderId) {
        Order curr=null;
        for(Order o:orders){
            if(o.getId().equals(orderId)) {
                curr = o;
                break;
            }
        }
        return curr;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        DeliveryPartner curr=null;
        for(DeliveryPartner d:deliveryPartners){
            if(d.getId().equals(partnerId)) {
                curr = d;
                break;
            }
        }
        return curr;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        int orderSize=0;
        for(DeliveryPartner d:orderSet.keySet()){
            if(d.getId().equals(partnerId)){
                orderSize=orderSet.get(d).size();
            }
        }
        return orderSize;
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
            for(Order o:orderList){
                count++;
            }
        }
        return orders.size()-count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int count=0;
        String arr[]=time.split(":");
        int hrs=Integer.valueOf(arr[0]);
        int min=Integer.valueOf(arr[1]);
        int orderTime=hrs*60+min;
        for (DeliveryPartner d:orderDeliveryPartnerMap.keySet()){
            if(d.getId().equals(partnerId)){
               if(orderDeliveryPartnerMap.get(d).getDeliveryTime()>orderTime){
                    count++;
                }
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int lastdeliverytime=0;
        for(DeliveryPartner d:orderDeliveryPartnerMap.keySet()){
            if(d.getId().equals(partnerId)){
                lastdeliverytime=Math.max(orderDeliveryPartnerMap.get(d).getDeliveryTime(),lastdeliverytime);
            }
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
    public String deletePartnerById(String partnerId) {
        DeliveryPartner partner=null;
        for(DeliveryPartner d:orderSet.keySet()){
            if(d.getId().equals(partnerId)){
                partner=d;
                break;
            }
        }
        for(DeliveryPartner d:orderDeliveryPartnerMap.keySet()){
            if(d.getId().equals(partnerId)){
                orderDeliveryPartnerMap.remove(d);
            }
        }
        orderSet.remove(partner);
        deliveryPartners.remove(partner);
        return "removed successfully";
    }

    public String deleteOrderById(String orderId) {

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
        for(DeliveryPartner d:orderDeliveryPartnerMap.keySet()){
            if(orderDeliveryPartnerMap.get(d).equals(removed)){
                orderDeliveryPartnerMap.remove(removed);
            }
        }
        return "removed successfully";
    }
}
