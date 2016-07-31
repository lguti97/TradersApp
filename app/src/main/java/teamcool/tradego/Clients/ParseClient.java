package teamcool.tradego.Clients;

//Created by selinabing on 7/11/16.


import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import teamcool.tradego.Models.Acquaintance;
import teamcool.tradego.Models.Friend;
import teamcool.tradego.Models.Item;
import teamcool.tradego.Models.User;


public class ParseClient {

    ArrayList<ParseUser> friends;
    int countItems = 0;

    public ParseClient () {
        friends = convertFriendsToParseUsers(queryFriendsOnName(null));
    }

    public ArrayList<ParseUser> convertFriendsToParseUsers(List<Friend> friends) {
        ArrayList<ParseUser> userFriends = new ArrayList<>();
        for (ParseObject f : friends) {
            userFriends.add(queryUserBasedonFBid(((Friend)f).getUserID()));
        }
        return userFriends;
    }

    public List<Friend> queryFriendsOnName(String name) {
        List<Friend> friends = new ArrayList<>();
        ParseQuery<Friend> query = ParseQuery.getQuery(Friend.class);
        query.whereEqualTo("owner",ParseUser.getCurrentUser());
        if (name != null)
            query.whereContains("username",name);
        try {
            friends = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public List<Item> queryItemsOnName(String name,boolean self) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereContains("item_name",name);
        query.whereEqualTo("status","Available");
        query.orderByDescending("createdAt");
        if(!self) {
            query.whereNotEqualTo("owner", ParseUser.getCurrentUser());
            query.whereContainedIn("owner", friends); //only query current user's friends
        } else {
            query.whereEqualTo("owner", ParseUser.getCurrentUser());
        }
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> queryItemsOnCategory(String category) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereNotEqualTo("owner",ParseUser.getCurrentUser());
        query.whereContainedIn("owner", friends);
        query.orderByDescending("createdAt");
        query.whereEqualTo("category",category);
        query.whereEqualTo("status","Available");
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    //REQUIRES: user must be a friend of currentUser or is currentUser itself
    // this method is only called in UserCatalogFragment,
    // where targetedUser is queried based on userID
    // depends on queryUserOnObjId method
    public List<Item> queryItemsOnUserAndStatus(ParseUser user, String status) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        if (user != null) {
            query.whereEqualTo("owner", user);
        }
        if (status != null) {
            query.whereEqualTo("status", status);
        }
        query.orderByDescending("createdAt");
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    //REQUIRES: user must be a friend of currentUser or is currentUser itself
    // this method is only called in UserCatalogFragment,
    // where targetedUser is queried based on userID
    // depends on queryUserOnObjId method
    public List<Item> queryItemsOnUser(ParseUser user) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        if (user != null) {
            query.whereEqualTo("owner", user);
        }
        query.orderByDescending("createdAt");
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> queryBoughtItemsOnUser(ParseUser user) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("buyer",user);
        query.whereEqualTo("status","Sold");
        query.orderByDescending("createdAt");
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> queryItemsOnOtherUserAndStatus(ParseUser user, String status) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereNotEqualTo("owner",user);
        query.whereContainedIn("owner",friends);
        query.whereEqualTo("status",status);
        query.orderByDescending("createdAt");
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> queryItemsOnFilteredQuery(String name, String category, String sort, String owner) {
        List<Item> items = new ArrayList<>();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereNotEqualTo("owner",ParseUser.getCurrentUser());
        query.whereContainedIn("owner",friends);
        query.whereContains("item_name",name);
        query.whereEqualTo("status","Available");
        query.whereEqualTo("category",category);
        if (!owner.equalsIgnoreCase("")) {
            query.whereContainedIn("owner",queryFriendsOnName(owner));
        }
        if (sort.equalsIgnoreCase("Oldest")) {
            query.orderByAscending("createdAt");
        }
        query.orderByDescending("createdAt");
        try {
            items = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void countNumFriendsOfUser(ParseUser user, final TextView textView, final String strAppend) {
        int count = -15251; //placeholder value
        ParseQuery<Friend> query = ParseQuery.getQuery(Friend.class);
        query.whereEqualTo("owner", user);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                String txt = count + strAppend;
                textView.setText(txt);
            }
        });
    }

    //REQUIRES: user must be a friend of currentUser
    public void countNumItemsOnStatus(ParseUser user, String status, final TextView textView, final String strPre, final String strAppend) {
        int count = -15251; //placeholder value
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        query.whereEqualTo("status",status);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                String txt = strPre + count + strAppend;
                textView.setText(txt);
            }
        });
    }


    public int countNumItemsBought(ParseUser user) {
        int count = -15210; //placeholder value
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("buyer", user);
        query.whereEqualTo("status","Sold");
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
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY

        query.whereEqualTo("objectId", itemId);
        try {
            item = query.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return item;
    }

    public ParseUser queryUserBasedonObjectID(String userId) {

        ParseUser user = new ParseUser();
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY

        query.whereEqualTo("objectId", userId);
        try {
            user = query.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    public ParseUser queryUserBasedonFBid(String userId) {

        ParseUser user = new ParseUser();
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY

        query.whereEqualTo("fb_id", userId);
        try {
            user = query.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<String> queryAcquaintanceIDofUser(ParseUser user){
        List<Acquaintance> acquaintances;
        List<String> acquaintancesID = new ArrayList<>();
        ParseQuery<Acquaintance> query = ParseQuery.getQuery(Acquaintance.class);
        query.whereEqualTo("owner", user.getCurrentUser());
        try {
            acquaintances = query.find();
            for (int i = 0; i < acquaintances.size(); i++){
                acquaintancesID.add(acquaintances.get(i).getUserID());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return acquaintancesID;
    }


    public List<Acquaintance> queryAcquaintancesofUser(ParseUser user){
        List<Acquaintance> acquaintances = new ArrayList<>();
        ParseQuery<Acquaintance> query = ParseQuery.getQuery(Acquaintance.class);
        query.whereEqualTo("owner", user.getCurrentUser());
        try {
            acquaintances = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return acquaintances;
    }

    public Acquaintance queryAcquaintanceOfUser(ParseUser user, String ID){
        Acquaintance acquaintance = new Acquaintance();
        ParseQuery<Acquaintance> query = ParseQuery.getQuery(Acquaintance.class);
        query.whereEqualTo("owner", user.getCurrentUser());
        query.whereEqualTo("userID", ID);
        try {
            acquaintance = query.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return acquaintance;
    }

    public Friend queryFriendOfUser(ParseUser user, String ID){
        Friend friend = new Friend();
        ParseQuery<Friend> query = ParseQuery.getQuery(Friend.class);
        query.whereEqualTo("owner", user.getCurrentUser());
        query.whereEqualTo("userID", ID);
        try {
            if (query.find().size() == 0){
                friend.setUserID("");
            }
            else{
                friend = query.find().get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return friend;
    }


    public void updateItem(String objectId, Item newItem) {
        ParseObject point = ParseObject.createWithoutData(Item.class,objectId);
        point.put("item_name",newItem.getItem_name());
        point.put("category",newItem.getCategory());
        point.put("price",newItem.getPrice());
        point.put("status",newItem.getStatus());
        point.put("negotiable",newItem.getNegotiable());
        point.put("description",newItem.getDescription());
        point.put("image_1",newItem.getImage1());
        point.put("image_2",newItem.getImage2());
        if (newItem.getStatus().equalsIgnoreCase("sold"))
            point.put("transaction_time", DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
        point.saveInBackground();
    }

    public void deleteItem(String itemId) {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("objectId",itemId);
        /*
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> objects, ParseException e) {
                objects.get(0).deleteInBackground();
            }
        });
        */
        try {
            Item i = query.find().get(0);
            i.delete();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void deleteFriend(String objId) {
        ParseQuery<Friend> query = ParseQuery.getQuery(Friend.class);
        Log.d("DEBUG",objId);
        query.whereEqualTo("userID",objId);
        try {
            Friend f = query.find().get(0);
            Log.d("DEBUG",f.getName()+"--");
            f.delete();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int countNumItemsOnStatus(ParseUser user, String status) {
        int count = -15251;
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner",user);
        query.whereEqualTo("status",status);
        try {
            count = query.count();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return count;
    }

}
