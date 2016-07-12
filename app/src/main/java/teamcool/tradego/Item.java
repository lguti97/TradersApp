package teamcool.tradego;

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

    public Item(String item_name, String category, String description, String status, double price) {
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
    public void setOwner(User user) {
        put("owner", user);
    }

    public static Item fromJSON(JSONObject object) {
        Item item = null;
        try {
            item = new Item(object.getString("item_name"),
                    object.getString("category"),
                    object.getString("description"),
                    object.getString("status"),
                    object.getDouble("price"));
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



