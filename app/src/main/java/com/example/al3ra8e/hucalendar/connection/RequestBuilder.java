package com.example.al3ra8e.hucalendar.connection;

import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RequestBuilder {

    private String  url ;
    private HashMap<String , String> params ;
    private int method ;
    private  Response.Listener<JSONObject> responseListener  ;
    private  Response.Listener<String> stringResponseListener  ;
    private  Response.ErrorListener errorListener  ;

    private AppCompatActivity activity ;

    public RequestBuilder setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this ;
    }

    public RequestBuilder() {

    }

    public RequestBuilder(String url, HashMap<String, String> params, int method) {
        this.url = url;
        this.params = params;
        this.method = method;
    }

    public RequestBuilder(String url, int method) {
        this.url = url;
        this.method = method;
    }

    public RequestBuilder setUrl(String url) {
        this.url = url;
        return this ;
    }

    public RequestBuilder setParams(HashMap<String, String> params) {
        this.params = params;
        return this ;
    }


    public RequestBuilder setMethod(int method) {
        this.method = method;
        return this ;
    }

    public RequestBuilder onResponse(Response.Listener<JSONObject> responseListener) {
        this.responseListener = responseListener;
        return this ;
    }

    public RequestBuilder onStringResponse(Response.Listener<String> stringResponseListener) {
        this.stringResponseListener = stringResponseListener;
        return this ;
    }

    public RequestBuilder onError(Response.ErrorListener onConnectionError) {
        this.errorListener = onConnectionError;
        return this ;
    }

    public void execute(){
        if(method == Request.Method.POST)
            executePost();
        else
            executeGet();
    }

    private void executeGet(){
            JsonObjectRequest jsonObject = new JsonObjectRequest(method , url ,responseListener,errorListener) ;
            AppController.getInstance().addToRequestQueue(jsonObject);
    }

    private void executePost(){
        JsonObjectRequest jsonObject = new JsonObjectRequest(method , url ,responseListener,errorListener){
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        } ;
        AppController.getInstance().addToRequestQueue(jsonObject);
    }

    public void executeStringRequest(){
        StringRequest stringRequest = new StringRequest(method , url ,stringResponseListener,errorListener) {
            @Override
            protected Map<String,String> getParams(){
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
