package teamcool.tradego.Models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by lguti on 7/25/16.
 */
@ParseClassName("WishItem")
public class WishItem extends ParseObject {

    public WishItem(){
        super();
    }

    public WishItem(String item_name, String image) {
        super();
        setItemName(item_name);
        setItemPicture(image);
    }

    public String getItemName() {
        return getString("itemName");
    }

    public String getPicture(){
        return getString("itemPicture");
    }

    public void setItemName(String item_name){
        put("itemName", item_name);
    }

    public void setItemPicture(String image){
        put ("itemPicture", image);
    }

    public ParseUser getUser(){
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user){
        put("owner", user);
    }

    //Retrieve information from an add item to wish list activity/fragment

}
