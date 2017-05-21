package tut.ac.za.openmerchant.Classes;

import java.io.Serializable;

/**
 * Created by ProJava on 2/15/2017.
 */

public class Address implements Serializable {

    private String city;
    private String street;
    private String province;
    private String town;
    private String code;
    private String country;

    public Address()
    {
        super();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
