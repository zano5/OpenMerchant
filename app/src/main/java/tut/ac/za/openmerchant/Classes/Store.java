package tut.ac.za.openmerchant.Classes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ProJava on 2/15/2017.
 */

public class Store implements Serializable {

    private String userID;
    private String name;
    private String description;
    private Address address;
    private List<Item> items;
    private String storeUrl;

    public Store()
    {
        super();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }
}
