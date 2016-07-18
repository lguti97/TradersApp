package teamcool.tradego.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kshah97 on 7/6/16.
 */

@ParseClassName("Item")
public class Item extends ParseObject {


    public Item(){
        super();
    }

    public Item(String item_name, String category, String description, String status, double price, String negotiable, String image1, String image2, String image3) {
        super();
        setItem_name(item_name);
        setCategory(category);
        setPrice(price);
        setStatus(status);
        setDescription(description);
        setNegotiable(negotiable);
        setImage1(image1);
        setImage2(image2);
        setImage3(image3);
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

    public double getPrice() {
        return getDouble("price");
    }

    public void setPrice(double price) {
        put("price",price);
    }

    public String getStatus() {
        return getString("status");
    }

    public void setStatus(String status) {
        put("status",status);
    }

    public String getNegotiable() {
        return getString("negotiable");
    }

    public void setNegotiable(String negotiable) {
        put("negotiable",negotiable);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description",description);
    }

    public String getTransactionTime() { return getString("transaction_time"); }

    public void setTransactionTime(String time) { put("transaction_time",time); }

    public String getImage1() { return getString("image_1"); }

    public void setImage1(String image1) { put("image_1",image1); }

    public String getImage2() { return getString("image_2"); }

    public void setImage2(String image2) { put("image_2",image2); }

    public String getImage3() { return getString("image_3"); }

    public void setImage3(String image3) { put("image_3",image3); }

    // Associate each item with a user
    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    public void setBuyer(ParseUser user) { put("buyer", user); }

    public static Item fromJSON(JSONObject object) {
        Item item = null;
        try {
            item = new Item(object.getString("item_name"),
                    object.getString("category"),
                    object.getString("description"),
                    object.getString("status"),
                    object.getDouble("price"),
                    object.getString("negotiable"),
                    object.getString("image_1"),
                    object.getString("image_2"),
                    object.getString("image_3"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static ArrayList<Item> fromJSONArray(JSONArray jsonArray){
        ArrayList<Item> items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject itemJson = jsonArray.getJSONObject(i);
                Item item = Item.fromJSON(itemJson);
                if (item != null) {
                    items.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return items;
    }

}



