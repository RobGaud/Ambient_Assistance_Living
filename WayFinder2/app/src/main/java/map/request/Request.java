package map.request;

import com.estimote.sdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.internal.spdy.Header;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * Created by Andrea on 17/05/2016.
 */
public class Request {

    private String url;
    private String key;
    private String value;
    private String res;

    public Request(String url,String key, String value){
        this.url = url;
        this.value=value;
        this.key = key;
    }

    public void getRequest(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(key, value);

        client.get(url, params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                //TODO here we have to manage the informations about maps, but these informations are in JSON!

                //once we have te informations we have to load in graph


            }

            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }
        });
    }

    public String getRes() {
        return res;
    }

}
