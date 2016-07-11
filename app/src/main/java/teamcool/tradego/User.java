package teamcool.tradego;


import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kshah97 on 7/6/16.
 */

@ParseClassName("User")

public class User extends ParseObject {

    private String timezone;

    public User(){
        super();
    }

    public User(String username, String user_id, String location, String timezone, List<Item> items) {
        super();
        setUsername(username);
        setUser_id(user_id);
        setItems(items);
        setLocation(location);
        setTimezone(timezone);

    }


    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put("username",username);
    }

    public String getUser_id() {
        return getString("user_id");
    }

    public void setUser_id(String user_id) {
        put("user_id",user_id);
    }

    public List<Item> getItems() { //HAD TO MAKE THIS A LIST INSTEAD OF AN ARRAYLIST FOR THIS TO WORK ---
        return getList("items");
    }

    public void setItems(List<Item> items) {
        put("items",items);
    }

    public String getLocation() {
        return getString("location");
    }

    public void setLocation(String location) {
        put("location",location);
    }

    public String getTimezone() {
        return getString("timezone");
    }

    public void setTimezone(String timezone) {
        put("timezone",timezone);
    }

    public static User fromJSON(JSONObject object) {
        User user = null;
        try {
            user = new User(object.getString("name"),
                    object.getString("id"),
                    object.getString("location"),
                    object.getString("timezone"),
                    Item.fromJSONArray(object.getJSONArray("items")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static ArrayList<User> fromJSONArray(JSONArray jsonArray){
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject userJson = jsonArray.getJSONObject(i);
                User user = User.fromJSON(userJson);
                if (user != null) {
                    users.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return users;
    }
}

