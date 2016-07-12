package teamcool.tradego;

import com.parse.FindCallback;
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

}
