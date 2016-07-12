package teamcool.tradego.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lguti on 7/11/16.
 */
@ParseClassName("Acquaintance")
public class Acquaintance extends ParseObject {
    private String profile_url ;
    private String name;

    //Going to use a One - to - Many Relationship for this
    //Will make a parse object out of this.

    public Acquaintance () {
        super();
    }

    public Acquaintance(String name, String profile_url){
        super();

    }

    // Will be used to access fields
    public String getName (){
        return getString("name");
    }

    public String getProfile_url() {
        return getString("profile_url");
    }

    // To modify field values

    public void setName(){
        put("name", name);
    }

    public void setProfile_url(){
        put("picture", profile_url);
    }

    /*
    TO CONNECT WITH USER OBJECT
     */

    //Get User for this acquaintance
    public ParseObject getUser() {
        return getParseObject("owner");
    }

    //Associate each acquaintance with the user
    public void setOwner(ParseObject user) {
        put("owner", user);
    }

    //Deserializes JSON objects into the Acquaintance model
    public static Acquaintance fromJSON(JSONObject object){
        Acquaintance acquaintance = null;
        try {
            acquaintance = new Acquaintance(object.getString("name"), object.getString("picture"));
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
