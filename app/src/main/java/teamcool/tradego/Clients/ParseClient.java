package teamcool.tradego.Clients;

import com.facebook.AccessToken;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import teamcool.tradego.Models.Item;
import teamcool.tradego.Models.User;

/**
 * Created by selinabing on 7/11/16.
 */
public class ParseClient {

    public ParseClient () {

    }

    public List<User> queryFriendsInDatabaseOnName(String name) {
        List<User> friends = new ArrayList<>();
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereContains("name",name);
        try {
            friends = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public List<Item> queryItemsInDatabaseOnName(String name) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereContains("item_name",name);
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> queryItemsInDatabaseOnCategory(String category) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("category",category);
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> queryItemsInDatabaseOnUser(User user) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public User getCurrentParseUser() {
        String currentUserFbId = AccessToken.getCurrentAccessToken().getUserId();
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        User user = User.constructBlankUser(); //to be changed after User class restructured
        query.whereEqualTo("user_id",currentUserFbId);
        List<User> objects = null;
        try {
            objects = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (objects.size() != 0) {
            user.setCurrentUser(objects.get(0));
        }
        return user;
    }

    //when updateUserData is called, new params come from fb graph api call
    // therefore the Items and Friends shouldn't be updated
    public void updateUserDataFromFBAPI(String objectID, final String username, final String location, final String timezone, final String profilePicURL) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        User object = null;
        try {
            object = query.get(objectID);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (username != null) {
            object.setUsername(username);
        }
        if(location != null) {
            object.setLocation(location);
        }
        if (timezone != null) {
            object.setTimezone(timezone);
        }
        if (profilePicURL != null) {
            object.setProfilePicURL(profilePicURL);
        }
        object.saveInBackground();
    }



}
