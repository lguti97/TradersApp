package teamcool.tradego.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lguti on 7/11/16.
 */

@ParseClassName("Acquaintance")
public class Acquaintance extends ParseObject {
    //Going to use a One - to - Many Relationship for this
    //Will make a parse object out of this.

    public Acquaintance () {
        super();
    }

    public Acquaintance(String name, String profile_url, String id){
        super();
        setName(name);
        setProfile_url(profile_url);
        setUserID(id);
    }

    // Will be used to access fields from Parse Database
    public String getName (){
        return getString("name");
    }

    public String getProfile_url() {
        return getString("picture");
    }

    public String getUserID(){
        return getString("userID");
    }

    // To modify field values in Parse Database

    public void setName(String name){
        put("name", name);
    }

    public void setProfile_url(String profile_url){
        put("picture", profile_url);
    }

    public void setUserID(String id){
        put("userID", id);
    }


    //TO CONNECT WITH USER OBJECT


    //Get User for this acquaintance
    public ParseUser getUser() {
        return getParseUser("owner");
    }

    //Associate each acquaintance with the user
    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    //Deserializes JSON objects into the Acquaintance model
    //How do we do this though?
    //TODO. Make a condition to not make the same Acquaintance Object.
    public static Acquaintance fromJSON(JSONObject object){
        Acquaintance acquaintance = null;
        try {
            acquaintance = new Acquaintance(object.getString("name"), object.getJSONObject("picture").getJSONObject("data").getString("url"),
                                            object.getString("id"));
            acquaintance.setOwner(ParseUser.getCurrentUser());
            acquaintance.saveInBackground();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return acquaintance;
    }

    //populates ArrayList of type Acquaintance
    //Also goes into the JSONArrays to get JSONObjects. Uses previous method
    public static ArrayList<Acquaintance> fromJSONArray(JSONArray jsonArray){
        ArrayList<Acquaintance> acquaintances = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonAcquaintance = jsonArray.getJSONObject(i);
                Acquaintance acquaintance = Acquaintance.fromJSON(jsonAcquaintance);
                if (acquaintance != null) {
                    acquaintances.add(acquaintance);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return acquaintances;
    }

}
