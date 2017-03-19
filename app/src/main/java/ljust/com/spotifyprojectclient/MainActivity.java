package ljust.com.spotifyprojectclient;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 * Activity : Executes all of the RESTful calls to web server and displays the output
 */
public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    String baseUrl;
    TextView jsonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();
        baseUrl = "https://quiet-mountain-88418.herokuapp.com";
        jsonText = (TextView)findViewById(R.id.json_text);
        jsonText.setMovementMethod(new ScrollingMovementMethod());

        new GETAsyncTask(0).execute();
        new POSTAsyncTask().execute(new ArrayList<>(Arrays.asList("Sean", "New York")));
        new GETAsyncTask(0).execute();
        new PUTAsyncTask(1).execute(new ArrayList<>(Arrays.asList("Brooklyn")));
        new GETAsyncTask(0).execute();
        new DELETEAsyncTask(1).execute();
        new GETAsyncTask(0).execute();
    }

    /*
     * AsyncTask : Performs a GET request to the server if given a person's id
     *
     * @param person_id : person's id on the server
     */
    private class GETAsyncTask extends AsyncTask<Void, Void, String> {

        int personId;

        public GETAsyncTask(int person_id) {
            this.personId = person_id;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url;

            if(personId != 0) {
                url = baseUrl + "/people/" + personId;
            } else {
                url = baseUrl + "/people";
            }

            final Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException ioe) {
                return ioe.toString();
            }
        }

        @Override
        protected void onPostExecute(String res){
            if(personId != 0) {
                jsonText.append("--> GET Request /people/" + personId + "\n\n");
            } else {
                jsonText.append("--> GET Request /people\n\n");
            }

            jsonText.append(res+"\n");
        }
    }

    /*
     * AsyncTask : Performs a POST request to the server if given the list of arguments
     *
     * @params ArrayList<String> : The list of arguments needed to create JSON
     */
    private class POSTAsyncTask extends AsyncTask<ArrayList<String>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<String>... params) {
            String url = baseUrl + "/people";
            MediaType JSON = MediaType.parse("application/json");
            ArrayList<String> args = params[0];
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("name", args.get(0));
                jsonObject.put("favoriteCity", args.get(1));
            } catch (JSONException je) {
                return je.toString();
            }

            RequestBody body = RequestBody.create(JSON, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch(IOException ioe) {
                return ioe.toString();
            }
        }

        @Override
        protected void onPostExecute(String res) {
            jsonText.append("--> POST Request /people\n\n");
            jsonText.append(res + "\n");
        }
    }

    /*
     * AsyncTask : Performs a PUT request to the server if given the list of arguments
     *
     * @params ArrayList<String> : The list of arguments needed to create JSON Object
     */
    private class PUTAsyncTask extends AsyncTask<ArrayList<String>, Void, String> {

        int personId;

        public PUTAsyncTask(int person_id) {
            this.personId = person_id;
        }

        @Override
        protected String doInBackground(ArrayList<String>... params) {
            String url = baseUrl + "/people/" + personId;
            MediaType JSON = MediaType.parse("application/json");
            JSONObject jsonObject = new JSONObject();
            ArrayList<String> args = params[0];

            try {
                jsonObject.put("favoriteCity", args.get(0));
            } catch (JSONException je) {
                return je.toString();
            }

            RequestBody body = RequestBody.create(JSON, jsonObject.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException ioe){
                return ioe.toString();
            }
        }

        @Override
        protected void onPostExecute(String res){
            jsonText.append("--> PUT Request /people\n\n");
            jsonText.append(res + "\n");
        }
    }

    /*
     * AsyncTask : Performs a DELETE request to the web server if given the person's id
     *
     * @params person_id : The person's id on the server.
     */
    private class DELETEAsyncTask extends AsyncTask<Void, Void, String> {

        int personId;

        public DELETEAsyncTask(int person_id){
            this.personId = person_id;
        }

        @Override
        protected String doInBackground(Void... params) {
            String url = baseUrl + "/people/" + personId;

            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException ioe){
                return ioe.toString();
            }
        }

        @Override
        protected void onPostExecute(String res) {
            jsonText.append("--> DELETE Request /people/" + personId + "\n\n");
            jsonText.append(res + "\n");
        }
    }

}
