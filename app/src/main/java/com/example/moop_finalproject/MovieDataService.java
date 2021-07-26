package com.example.moop_finalproject;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDataService {

    Context context;
    List<Movies> moviesList;

    public MovieDataService(Context context){
        this.context = context;
    }

    public interface VolleyResponseListenerShort {
        void onError (String message);

        void onResponse (List<Movies> moviesList);
    }

    public void getMovieDataShort(String userInput, VolleyResponseListenerShort volleyResponseListener){
        moviesList = new ArrayList<>();

//        Transform input into url parameter
        String[] inputArr = userInput.split(" ");
        String param = "";
        for (int i=0; i<inputArr.length; i++){
            param += inputArr[i] + "+";
        }
        param = param.substring(0, param.length()-1);

        String url = "https://www.omdbapi.com/?apikey=c39f3d49&s=" + param;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray container = response.getJSONArray("Search");
                            for (int i = 0; i < container.length(); i++) {
                                JSONObject data = container.getJSONObject(i);
                                Movies m = new Movies(data.getString("imdbID"), data.getString("Title"), data.getString("Year"), data.getString("Type"), data.getString("Poster"));
                                moviesList.add(m);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        volleyResponseListener.onResponse(moviesList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Something went wrong");
            }
        });

        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface VolleyResponseListenerDetail {
        void onError (String message);

        void onResponse (JSONObject response) throws JSONException;
    }

    public void getMovieDataDetail(String imdbID, VolleyResponseListenerDetail volleyResponseListenerDetail){
        String url = "https://www.omdbapi.com/?apikey=c39f3d49&plot=full&i=" + imdbID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            volleyResponseListenerDetail.onResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListenerDetail.onError("Something went wrong");
            }
        });

        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(context).addToRequestQueue(request);
    }
}
