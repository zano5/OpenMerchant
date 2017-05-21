package tut.ac.za.openmerchant.Classes;

import java.io.Serializable;

/**
 * Created by ProJava on 2/15/2017.
 */

public class Payment implements Serializable {


    private String paymentType;
    private double amntDue;
    private double amntPaid;

    public Payment()
    {
        super();
    }

    public double getAmntDue() {
        return amntDue;
    }

    public void setAmntDue(double amntDue) {
        this.amntDue = amntDue;
    }

    public double getAmntPaid() {
        return amntPaid;
    }

    public void setAmntPaid(double amntPaid) {
        this.amntPaid = amntPaid;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
