package map.request;

import android.util.Log;

import cz.msebera.android.httpclient.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import map.persistence.DBHelper;
import ui.SplashScreen;
import utils.AppConstants;

/**
 * Created by Andrea on 17/05/2016.
 *
 * This class is used to check whether the local database is up-to-date.
 */
public class Request {

    private static final String TAG_DEBUG = "REQUEST";

    private String url;
    private String key;
    private String res;
    private String value;
    private DBHelper dbHelper;
    private SplashScreen splashScreen;

    public Request(String url,String key, String value, DBHelper dbHelper, SplashScreen splashScreen){
        this.url = url;
        this.value=value;
        this.key = key;
        this.dbHelper = dbHelper;
        this.splashScreen = splashScreen;
    }

    public void getRequest(){
        Log.d(AppConstants.TAG_DEBUG_APP + TAG_DEBUG, "getRequest started.");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(key, value);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Once we have the information we have to load it into the SQLite database
                Log.d(AppConstants.TAG_DEBUG_APP + TAG_DEBUG, "onSuccess called. statusCode="+statusCode);

                try {
                    String success = response.getString("success");
                    if (success.equals(AppConstants.SUCCESS_MISSING)) {
                        Log.d(AppConstants.TAG_DEBUG_APP + TAG_DEBUG, "An error occurred.");
                        res = success;
                    }
                    else{
                        res = success;
                        int dbVersion = response.getInt("dbVersion");
                        int dbVersionLocal = Integer.parseInt(value);
                        if( dbVersionLocal < dbVersion){
                            //In this case, we need to update the map
                            // If the request succeeded, then we store the data into the database.
                            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                                    "The local database isn't up-to-date. " +
                                    "local version="+dbVersionLocal+", server version="+dbVersion+".");

                            JSONArray mapsArray = response.getJSONArray("maps");
                            dbHelper.insertMaps(mapsArray);
                            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                                    "insertMaps ended. updating dbVersion");

                            splashScreen.updateVersion(dbVersion);
                        }
                        else{
                            // Otherwise, there is no need to change the local database: we can just end.
                            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                                    "The local database is up-to-date." +
                                    "local version="+dbVersionLocal);
                        }
                    }
                }
                catch (JSONException e){
                    Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                            "JSONException: bad format for response");
                }
            }

            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "onFailure called. " +
                        "StatusCode="+statusCode+", res="+res);
            }
        });
    }

    public String getRes() {
        return res;
    }
}
