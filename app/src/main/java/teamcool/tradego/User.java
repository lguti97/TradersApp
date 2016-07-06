package teamcool.tradego;


import java.util.ArrayList;

/**
 * Created by kshah97 on 7/6/16.
 */
public class User {

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
}

