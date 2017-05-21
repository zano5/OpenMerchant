package tut.ac.za.openmerchant.Classes;

import java.io.Serializable;

/**
 * Created by ProJava on 2/17/2017.
 */

public class Card implements Serializable {

    private long cardNo;
    private int expireMonth;
    private int expireYear;
    private int cvc;

    public Card()
    {
        super();
    }

    public long getCardNo() {
        return cardNo;
    }

    public void setCardNo(long cardNo) {
        this.cardNo = cardNo;
    }

    public int getCvc() {
        return cvc;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public int getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(int expireMonth) {
        this.expireMonth = expireMonth;
    }

    public int getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(int expireYear) {
        this.expireYear = expireYear;
    }
}
