

package teamcool.tradego.Clients;

//Created by selinabing on 7/11/16.


import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import teamcool.tradego.Models.Item;

public class ParseClient {

    public ParseClient () {

    }
    public List<ParseUser> queryFriendsInDatabaseOnName(String name) {
        List<ParseUser> friends = new ArrayList<>();
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereContains("username",name);
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

    public List<Item> queryAvailableItemsInDatabaseOnUser(ParseUser user) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        query.whereEqualTo("status","Available");
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> querySoldItemsInDatabaseOnUser(ParseUser user) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        query.whereEqualTo("status","Sold");
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> queryOnholdItemsInDatabaseOnUser(ParseUser user) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        query.whereEqualTo("status","On hold");
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public int countNumFriendsOfUser(ParseUser user) {
        int count = -15251;
        /*
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        try {
            count = query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */
        return count;
    }

    public int countNumItemsSold(ParseUser user) {
        int count = -15251;
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        query.whereEqualTo("status","Sold");
        try {
            count = query.count();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countNumsItemsOnhold(ParseUser user) {
        int count = -15251;
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        query.whereEqualTo("status","On hold");
        try {
            count = query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int countNumsItemsAvailable(ParseUser user) {
        int count = -15251;
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        query.whereEqualTo("status","Available");
        try {
            count = query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return count;
    }

    public Item queryItemBasedonObjectID(String itemId) {

        Item item = new Item();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
// First try to find from the cache and only then go to network
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
// Execute the query to find the object with ID

        query.whereEqualTo("objectId", itemId);
        try {
            item = query.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return item;
    }

 }

