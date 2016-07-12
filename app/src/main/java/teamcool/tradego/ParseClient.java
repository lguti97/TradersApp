package teamcool.tradego;

import android.util.Log;

import com.facebook.AccessToken;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by selinabing on 7/11/16.
 */
public class ParseClient {

    public ParseClient () {

    }

    public List<User> queryFriendsInDatabaseOnName(final String name) {
        final ArrayList<User> friends = new ArrayList<>();
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereContains("name",name);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        //no user matching exists
                        //Toast.makeText(getApplicationContext(), name + " is not found", Toast.LENGTH_SHORT).show();
                    } else {
                        friends.addAll(objects);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        return friends;
    }

    public List<Item> queryItemsInDatabaseOnName(final String name) {
        final ArrayList<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereContains("item_name",name);
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        //no matching item exists
                        //Toast.makeText(getApplicationContext(), name + " is not found", Toast.LENGTH_SHORT).show();
                    } else {
                        items.addAll(objects);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        return items;
    }

    public List<Item> queryItemsInDatabaseOnCategory(final String category) {
        final ArrayList<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("category",category);
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        //no matching item exists
                    } else {
                        items.addAll(objects);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        return items;
    }

    public List<Item> queryItemsInDatabaseOnUser(final User user) {
        final ArrayList<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 0) {
                        //no matching item exists
                    } else {
                        items.addAll(objects);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        return items;
    }

    public User getCurrentParseUser() {
        String currentUserFbId = AccessToken.getCurrentAccessToken().getUserId();
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        final User user = null;
        query.whereEqualTo("user_id",currentUserFbId);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    // if no users of this ID exist, return null
                    // otherwise, return it
                    if (objects.size() != 0) {
                        user.setCurrentUser(objects.get(0));
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        return user;
    }

    //when updateUserData is called, new params come from fb graph api call
    // therefore the Items and Friends shouldn't be updated
    public void updateUserDataFromFBAPI(String objectID, final String username, final String location, final String timezone, final String profilePicURL) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.getInBackground(objectID, new GetCallback<User>() {
            @Override
            public void done(User object, ParseException e) {
                if (e == null) {
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
                } else {
                    Log.d("DEBUG","updateUserData, ParseException code: "+e.getCode());
                    e.printStackTrace();
                }
            }
        });
    }



}
