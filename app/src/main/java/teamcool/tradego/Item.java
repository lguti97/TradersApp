package teamcool.tradego;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by kshah97 on 7/6/16.
 */

@ParseClassName("Item")

public class Item extends ParseObject {

    /*

    private String item_name;
    private String category;
    private float price;
    private String status;
    private String description;


    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    */

    public Item(){
        super();
    }

    public Item(String item_name, String category, String description, String status, float price) {
        super();
        setItem_name(item_name);
        setCategory(category);
        setPrice(price);
        setStatus(status);
        setDescription(description);
    }


    public String getItem_name() {
        return getString("item_name");
    }

    public void setItem_name(String item_name) {
        put("item_name",item_name);
    }

    public String getCategory() {
        return getString("category");
    }

    public void setCategory(String category) {
        put("category",category);
    }

    public float getPrice() {
        return (float) getDouble("price");
    }

    public void setPrice(float price) {
        put("price",price);
    }

    public String getStatus() {
        return getString("status");
    }

    public void setStatus(String status) {
        put("status",status);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description",description);
    }

    public ParseUser getUser()  {
        return getParseUser("owner");
    }

    // Associate each item with a user
    public void setOwner(ParseUser user) {
        put("owner", user);
    }

}



