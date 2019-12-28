package com.example.al3ra8e.hucalendar.searchPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.eventPackage.CustomEvent;
import com.example.al3ra8e.hucalendar.eventPackage.RecycleEventAdapter;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchByFaculty extends Fragment {
    private Context context ;
    LayoutInflater inflater ;
    AppCompatSpinner facultiesView ;
    RecyclerView eventsByFaculty;
    ArrayList<CustomEvent> events ;
    int currentFaculty ;
    SwipeRefreshLayout refreshFaculty ;
    String personId ;
    int logInState ;

    public SearchByFaculty(Context context , String personId ,int logInState) {
        this.context = context ;
        this.personId = personId ;
        this.logInState = logInState ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater ;
        View view = inflater.inflate(R.layout.fragment_search_by_faculty, container, false) ;

        initial(view) ;

        facultiesView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFaculty = position+1 ;
                getEventForStudent();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        refreshFaculty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                facultiesView.setSelection(facultiesView.getSelectedItemPosition());
                currentFaculty = facultiesView.getSelectedItemPosition()+1 ;
                getEventForStudent();
                refreshFaculty.setRefreshing(false);
            }
        });
        return view ;
    }

    public void getEventForStudent(){
        events = new ArrayList<>() ;
        eventsByFaculty.removeAllViews();
        String url = new UrlBuilder(AccessLinks.EVENTS_4_FACULTY).setUrlParameter("faculty" , currentFaculty+"").getUrl() ;

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
                                .setEventList(eventsByFaculty)
                                .setEvents(events)
                                .setContext(context)
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

    private class FacultiesAdapter extends BaseAdapter{
        String  [] faculties ;
        public FacultiesAdapter (String [] faculties){
            this.faculties =faculties ;
        }

        @Override
        public int getCount() {
            return faculties.length;
        }

        @Override
        public Object getItem(int position) {
            return faculties[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.category_view , null ) ;
            TextView temp = (TextView) view.findViewById(R.id.categoryText);
            temp.setText(faculties[position]);
            return view ;
        }
    }

    private void initial(View view) {

        events = new ArrayList<>() ;
        currentFaculty = 1 ;
        facultiesView = (AppCompatSpinner) view.findViewById(R.id.facultiesView);
        eventsByFaculty = (RecyclerView) view.findViewById(R.id.eventsByFaculty);
        refreshFaculty = (SwipeRefreshLayout) view.findViewById(R.id.refreshFaculty);
        facultiesView.setAdapter(new FacultiesAdapter(getResources().getStringArray(R.array.faculties)));

    }

}
