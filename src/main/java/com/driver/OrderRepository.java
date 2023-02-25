package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    Map<String,Order> orderMap=new HashMap<>();

    Map<String,DeliveryPartner>deliveryPartnerMap=new HashMap<>();

    Map<DeliveryPartner,List<Order>>deliveryPartnerListMap=new HashMap<>();



    Map<String,String>partnerOrderIdMap=new HashMap<>();

    public void addOrder(Order order) {
        String orderId= order.getId();
        orderMap.put(orderId,order);
    }

    public void addPartner(String partnerId) {

        DeliveryPartner partner=new DeliveryPartner(partnerId);

        deliveryPartnerMap.put(partnerId,partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        Order order=orderMap.get(orderId);
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);

        partnerOrderIdMap.put(orderId,partnerId);

        if(deliveryPartnerListMap.containsKey(deliveryPartner)){
            List<Order>orderList=deliveryPartnerListMap.get(deliveryPartner);
            orderList.add(order);
            deliveryPartnerListMap.put(deliveryPartner,orderList);
            deliveryPartner.setNumberOfOrders(deliveryPartnerListMap.get(deliveryPartner).size());
        }
        else{
            deliveryPartnerListMap.put(deliveryPartner,new ArrayList<>());
            List<Order>orderList=deliveryPartnerListMap.get(deliveryPartner);
            orderList.add(order);
            deliveryPartnerListMap.put(deliveryPartner,orderList);
            deliveryPartner.setNumberOfOrders(deliveryPartnerListMap.get(deliveryPartner).size());
        }

    }

    public Order getOrderById(String orderId) {

        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);

        return deliveryPartnerListMap.get(deliveryPartner).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);

        List<Order>orderById=deliveryPartnerListMap.get(deliveryPartner);

        List<String>required=new ArrayList<>();
        for(Order order:orderById){
            required.add(order.getId());
        }
        return required;
    }

    public List<String> getAllOrders() {
        List<String>allOrders=new ArrayList<>();
        for(String orderId:orderMap.keySet()){
            allOrders.add(orderId);
        }
        return allOrders;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderMap.size()-partnerOrderIdMap.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        String arr[]=time.split(":");
        int hrs=Integer.parseInt(arr[0]);
        int mins=Integer.parseInt(arr[1]);

        int currTime=hrs*60+mins;

        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        List<Order>orderList=deliveryPartnerListMap.get(deliveryPartner);

        int count=0;
        for(Order order:orderList){
            if(order.getDeliveryTime()>currTime){
                count++;
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {

        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);

        int maxTime=0;

        List<Order>orderList=deliveryPartnerListMap.get(deliveryPartner);

        for(Order order:orderList){
            maxTime=Math.max(maxTime,order.getDeliveryTime());
        }
        int hrs=maxTime/60;
        int mins=maxTime%60;

        String deliveryTime="";
        if(hrs<10){
           if(mins<10){
               deliveryTime="0"+hrs+":"+"0"+mins;
           }
           else{
               deliveryTime="0"+hrs+":"+mins;
           }
        }
        else{
            if(mins<10){
                deliveryTime=hrs+":"+"0"+mins;
            }
            else deliveryTime=hrs+":"+mins;
        }

        return deliveryTime;
    }

    public void deletePartner(String partnerId) {
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(partnerId);
        deliveryPartnerListMap.remove(deliveryPartner);
        deliveryPartnerMap.remove(partnerId);

        for(String orderId:partnerOrderIdMap.keySet()){
            if(partnerOrderIdMap.get(orderId).equals(partnerId)){
                partnerOrderIdMap.remove(orderId);
            }
        }
    }

    public void deleteOrder(String orderId) {

        String deliveryPartenerId=partnerOrderIdMap.get(orderId);
        Order order=orderMap.get(orderId);
        DeliveryPartner deliveryPartner=deliveryPartnerMap.get(deliveryPartenerId);

        orderMap.remove(orderId);
        partnerOrderIdMap.remove(orderId);

        List<Order>orderList=deliveryPartnerListMap.get(deliveryPartner);
        for(Order order1:orderList){
            if(Objects.equals(order,order1)){
                orderList.remove(order1);
            }
        }
        deliveryPartnerListMap.put(deliveryPartner,orderList);
        deliveryPartner.setNumberOfOrders(orderList.size());
    }
}
