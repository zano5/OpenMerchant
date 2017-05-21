package tut.ac.za.openmerchant.Classes;

import java.io.Serializable;

/**
 * Created by ProJava on 12/5/2016.
 */

public class Ads implements Serializable {

    private String name;
    private String url;


    public Ads()
    {
        super();
    }


    public Ads(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ads{" +
                "url='" + url + '\'' +
                '}';
    }
}
