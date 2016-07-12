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

    public User(String username, String user_id, String location, String timezone, String profilePicUrl, List<Item> items, List<User> friends) {
        super();
        setUsername(username);
        setUser_id(user_id);
        setItems(items);
        setLocation(location);
        setTimezone(timezone);
        setFriends(friends);
        setProfilePicURL(profilePicUrl);

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

    public List<User> getFriends() {
        return getList("friends");
    }

    public void setFriends(List<User> friends) {
        put("friends",friends);
    }

    public String getProfilePicURL () {
        return getString("profile_pic_url");
    }

    public void setProfilePicURL(String profilePicUrl) {
        put("profile_pic_url",profilePicUrl);
    }

    public void setCurrentUser (User user) {
        if (user != null) {
            setUsername(user.getUsername());
            setUser_id(user.getUser_id());
            setItems(user.getItems());
            setLocation(user.getLocation());
            setTimezone(user.getTimezone());
            setFriends(user.getFriends());
            setProfilePicURL(user.getProfilePicURL());
        }
    }

    public static User fromJSON(JSONObject object) {
        User user = null;
        try {
            user = new User(object.getString("name"),
                    object.getString("id"),
                    object.getJSONObject("location").getString("name"),
                    object.getString("timezone"),
                    object.getJSONObject("picture").getJSONObject("data").getString("url"),
                    new ArrayList<Item>(),
                    new ArrayList<User>());

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

