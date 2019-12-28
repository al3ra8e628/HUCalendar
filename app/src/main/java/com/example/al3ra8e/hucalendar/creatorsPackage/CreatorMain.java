package com.example.al3ra8e.hucalendar.creatorsPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.eventPackage.CustomEvent;
import com.example.al3ra8e.hucalendar.eventPackage.RecycleEventAdapter;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.Logout;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;
import com.example.al3ra8e.hucalendar.searchPackage.SearchActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class CreatorMain extends AppCompatActivity {
    private Context context ;
    RecyclerView eventsByCreator;
    ArrayList<CustomEvent> events ;
    SwipeRefreshLayout refreshCategory ;
    String personId ;
    int logInState ;
    FloatingActionButton fab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initial();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreatorMain.this ,SearchActivity.class) ;
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
    }

    public void getEvents(){
        events = new ArrayList<>();
        eventsByCreator.removeAllViews();
        String url  = new UrlBuilder(AccessLinks.EVENT_BY_CREATOR).setUrlParameter("creator_id" , personId).getUrl();
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
                                .setEventList(eventsByCreator)
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
        menu.add(0, 0, 0, "")
        .setIcon(getResources().getDrawable(R.drawable.create_event_icon)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(0, 1, 0, "")
        .setIcon(getResources().getDrawable(android.R.drawable.ic_lock_idle_lock)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0 :
                Intent i = new Intent(CreatorMain.this , AddEventActivity.class) ;
                i.putExtra("creator_id" , personId+"") ;
                startActivity(i);
                break;
            case 1 :
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
        eventsByCreator = (RecyclerView) findViewById(R.id.smallEventsListCreator);
        refreshCategory = (SwipeRefreshLayout) findViewById(R.id.refresher);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getEvents();
    }
}
