package teamcool.tradego;


import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kshah97 on 7/6/16.
 */

@ParseClassName("User")

public class User extends ParseObject {

    /*

    private String username;
    private ArrayList<Item> items;
    private int user_id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    */

    public User(){
        super();
    }

    public User(String username, int user_id, ArrayList<Item> items) {
        super();
        setUsername(username);
        setUser_id(user_id);
        setItems(items);
    }


    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put("username",username);
    }


    public int getUser_id() {
        return getInt("user_id");
    }

    public void setUser_id(int user_id) {
        put("user_id",user_id);
    }

    public List<Item> getItems() { //HAD TO MAKE THIS A LIST INSTEAD OF AN ARRAYLIST FOR THIS TO WORK ---
        return getList("items");
    }

    public void setItems(ArrayList<Item> items) {
        put("items",items);
    }

}

