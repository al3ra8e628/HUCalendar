package com.example.al3ra8e.hucalendar.adminPackage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.eventPackage.CustomEvent;
import com.example.al3ra8e.hucalendar.eventPackage.RecycleEventAdapter;
import com.example.al3ra8e.hucalendar.other.Home;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.Logout;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;
import com.example.al3ra8e.hucalendar.searchPackage.SearchActivity;
import com.example.al3ra8e.hucalendar.servicesPackage.CommentNotificationService;
import com.example.al3ra8e.hucalendar.servicesPackage.EventNotificationService;
import com.example.al3ra8e.hucalendar.servicesPackage.EventRequestNotificationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminMain extends AppCompatActivity {
    private Context context ;
    RecyclerView eventsByCategory;
    ArrayList<CustomEvent> events ;
    SwipeRefreshLayout refreshCategory ;
    String personId ;
    int logInState ;
    FloatingActionButton fab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initial();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminMain.this ,SearchActivity.class) ;
                i.putExtra("person_id" , personId+"");
                i.putExtra(Keys.LOG_IN_STATE_KEY, logInState+"");
                startActivity(i);
            }
        });

        refreshCategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvents();
                refreshCategory.setRefreshing(false);
            }
        });
        getEvents();
        getLastRequestEvent();
    }


    private void setServiceOn(int lastCreatedEvent){


        EventRequestNotificationService.personID = Integer.parseInt(personId) ;
        EventRequestNotificationService.lastCreatedEvent = lastCreatedEvent ;

        if(EventRequestNotificationService.ServiceIsRun == false) {
            EventRequestNotificationService.ServiceIsRun  = true;

            Intent intent = new Intent(this, EventRequestNotificationService.class);

            startService(intent);
        }
    }

    private void getLastRequestEvent(){
        String url = new UrlBuilder(AccessLinks.GET_LAST_REQUEST).setUrlParameter("person_id" , personId).getUrl() ;

        new RequestBuilder()
                .setUrl(url)
                .setMethod(Request.Method.GET)
                .setActivity(this)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int last  = response.getInt("last_created_event") ;
                            setServiceOn(last) ;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .execute();
    }



    public void getEvents(){
        events = new ArrayList<>() ;
        eventsByCategory.removeAllViews();
        String url  = new UrlBuilder(AccessLinks.NOT_ACCEPTED_EVENT).getUrl();
        new RequestBuilder()
                .setUrl(url)
                .setMethod(Request.Method.GET)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new RecycleEventAdapter()
                                .setPersonId(personId)
                                .setLogInState(logInState)
                                .setResponse(response)
                                .setContext(context)
                                .setEvents(events)
                                .setEventList(eventsByCategory)
                                .fillEventsIntoTheList();
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                .execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
             menu.add(0, 0, 0, "add creator");
             menu.add(0, 1, 0, "add admin");
             menu.add(0, 2, 0, "log out");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0 :
                    Toast.makeText(getApplicationContext() , "add new creator " , Toast.LENGTH_LONG).show();
                break;
            case 1 :
                    Toast.makeText(getApplicationContext() , "add new admin" , Toast.LENGTH_LONG).show();
                break;
            case 2 :
                Logout.logOut(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initial(){
         personId  = getIntent().getStringExtra("person_id");
         logInState =  Integer.parseInt(getIntent().getStringExtra(Keys.LOG_IN_STATE_KEY));
         context = this ;
         fab = (FloatingActionButton) findViewById(R.id.fab);
         eventsByCategory = (RecyclerView) findViewById(R.id.smallEventsListAdmin);
         refreshCategory = (SwipeRefreshLayout) findViewById(R.id.refresher);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getEvents();
    }

}
