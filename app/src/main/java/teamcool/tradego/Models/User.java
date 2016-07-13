package teamcool.tradego.Models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class User {
    private ParseUser extended_user;
    private String username;
    private String user_id;
    private String location;
    private String timezone;
    private String profilePicUrl;

    public User () {
    }

    //Updates member variables + aliasing
    public User(String username, String user_id, String location, String timezone, String profilePicUrl, ParseUser obj) {
        this.username = username;
        this.user_id = user_id;
        this.location = location;
        this.timezone = timezone;
        this.profilePicUrl = profilePicUrl;
        extended_user = obj;
        setSomeField(username, user_id, location, timezone, profilePicUrl);

    }

    //Adds the updated/extended fields to the ParseUser
    public void setSomeField(String username, String user_id, String location, String timezone, String profilePicUrl) {
        extended_user.put("username", username);
        extended_user.put("user_id", user_id);
        extended_user.put("location", location);
        extended_user.put("timezone", timezone);
        extended_user.put("profilePicUrl", profilePicUrl);
        extended_user.saveInBackground();
    }

    //Deserializes JSONObjects
    public static User fromJSON(JSONObject object, ParseUser obj) {
        User user = null;
        try {

            user = new User(object.getString("name"),
                    object.getString("id"),
                    object.getJSONObject("location").getString("name"),
                    object.getString("timezone"),
                    object.getJSONObject("picture").getJSONObject("data").getString("url"),
                    obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }


    public static User fromJSON(JSONObject object) {
        User user = new User();
        try {
            user.username = object.getString("name");
            user.user_id = object.getString("id");
            user.location = object.getJSONObject("location").getString("name");
            user.timezone = object.getString("timezone");
            user.profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

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

