package teamcool.tradego.Models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import teamcool.tradego.Clients.ParseClient;

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
    public static Acquaintance fromJSON(JSONObject object){

        List<String> acquaintancesID;
        List<Acquaintance> acquaintances1;
        ParseClient parseClient = new ParseClient();
        //ParseClient retrieves the names of the of AcquaintUser
        acquaintancesID = parseClient.queryAcquaintanceIDofUser(ParseUser.getCurrentUser());
        //ParseClient retrieves the Acquaintances of the User.
        acquaintances1 = parseClient.queryAcquaintancesofUser(ParseUser.getCurrentUser());

        Acquaintance acquaintance = null;
        try {
            if (!acquaintancesID.contains(object.getString("id"))) {
                acquaintance = new Acquaintance(object.getString("name"), object.getJSONObject("picture").getJSONObject("data").getString("url"),
                        object.getString("id"));
                acquaintance.setOwner(ParseUser.getCurrentUser());
                acquaintance.saveInBackground();
            }
            else {
                //At this point going to delete the ParseObjects associated with the user so I can instantiate again without doing any harm
                // Should delete every acquaintance inside the database
                for (int i = 0; i < acquaintances1.size(); i ++){
                    acquaintances1.get(i).deleteInBackground();
                }
                acquaintance = new Acquaintance(object.getString("name"), object.getJSONObject("picture").getJSONObject("data").getString("url"),
                        object.getString("id"));
                acquaintance.setOwner(ParseUser.getCurrentUser());
                acquaintance.saveInBackground();
            }
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
