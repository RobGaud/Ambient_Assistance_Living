package map.request;

import android.util.Log;

import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.internal.spdy.Header;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import map.persistence.DBHelper;

/**
 * Created by Andrea on 17/05/2016.
 */
public class Request {

    private static final String TAG_DEBUG_APP = "BLIND_";
    private static final String TAG_DEBUG = "REQUEST";
    private static final String SUCCESS_NO_UPDATE  = "0";
    private static final String SUCCESS_YES_UPDATE = "1";

    private String url;
    private String key;
    private String value;
    private String res;
    private DBHelper dbHelper;

    public Request(String url,String key, String value, DBHelper dbHelper){
        this.url = url;
        this.value=value;
        this.key = key;
        this.dbHelper = dbHelper;
    }

    public void getRequest(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(key, value);

        client.get(url, params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                /** If the local dbVersion is up-to-date, we get a JSON like the following:
                 *  {"success":0,"message":"Required field(s) is missing"}
                 *  Otherwise, we get:
                 *  TODO
                 */
                //TODO here we have to manage the informations about maps, but these informations are in JSON!
                //once we have the information we have to load it into the SQLite database
                /*
                JSONArray sportsArray = root.getJSONArray("sport");
                // now get the first element:
                JSONObject firstSport = sportsArray.getJSONObject(0);
                // and so on
                String name = firstSport.getString("name"); // basketball
                int id = firstSport.getInt("id"); // 40
                JSONArray leaguesArray = firstSport.getJSONArray("leagues");
                 */
                try {
                    String success = response.getString("success");
                    if (success.equals(SUCCESS_NO_UPDATE)) {
                        Log.d(TAG_DEBUG_APP + TAG_DEBUG, "No update is required.");
                        return;
                    }
                    else{
                        //TODO prendi mappe e scrivi in SQLite usando DBHelper
                        JSONArray mapsArray = response.getJSONArray("maps");
                        dbHelper.insertMaps(mapsArray);
                    }
                }
                catch (JSONException e){
                    Log.d(TAG_DEBUG_APP+TAG_DEBUG, "JSONException: bad format for response");
                }
            }

            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //TODO
            }
        });
    }

    public String getRes() {
        return res;
    }

}
