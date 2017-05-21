package tut.ac.za.openmerchant.Classes;

import java.io.Serializable;
/**
 * Created by ProJava on 2/17/2017.
 */

public class Order implements Serializable {

    private Store store;
    private User user;
    private DistanceCounter counter;
    private double totItemPrice;
    private Payment payment;
    private double deliveryFee;
    private String storeArea;
    private String deliveryAddress;
    private double totAmnt;
    private String date;


    public Order() {
        super();
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public DistanceCounter getCounter() {
        return counter;
    }

    public void setCounter(DistanceCounter counter) {
        this.counter = counter;
    }


    public double getTotItemPrice() {
        return totItemPrice;
    }

    public void setTotItemPrice(double totItemPrice) {
        this.totItemPrice = totItemPrice;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }


    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getStoreArea() {
        return storeArea;
    }

    public void setStoreArea(String storeArea) {
        this.storeArea = storeArea;
    }


    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public double getTotAmnt() {
        return totAmnt;
    }

    public void setTotAmnt(double totAmnt) {
        this.totAmnt = totAmnt;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
