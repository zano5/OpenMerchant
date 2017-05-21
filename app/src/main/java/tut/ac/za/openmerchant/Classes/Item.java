package tut.ac.za.openmerchant.Classes;

import java.io.Serializable;

/**
 * Created by ProJava on 2/15/2017.
 */

public class Item implements Serializable {

    private String name;
    private String description;
    private String url;
    private double price;

    public  Item()
    {
        super();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
