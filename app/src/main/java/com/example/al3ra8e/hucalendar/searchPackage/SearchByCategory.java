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

public class SearchByCategory extends Fragment {
    private Context context ;
    LayoutInflater inflater ;
    AppCompatSpinner categoriesView ;
    RecyclerView eventsByCategory;
    ArrayList<CustomEvent> events ;
    int currentCategory ;
    SwipeRefreshLayout refreshCategory ;
    String personId ;
    int logInState ;

    public SearchByCategory(Context context , String personId , int logInState) {
        this.context = context ;
        this.personId = personId ;
        this.logInState = logInState ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater ;
        View view = inflater.inflate(R.layout.fragment_search_by_category, container, false) ;

        initial(view);

        categoriesView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = position+1 ;
                getEvents();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        refreshCategory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoriesView.setSelection(categoriesView.getSelectedItemPosition());
                currentCategory = categoriesView.getSelectedItemPosition()+1 ;
                getEvents();
                refreshCategory.setRefreshing(false);
            }
        });
        return view ;
    }


    public void getEvents(){
        events = new ArrayList<>() ;
        eventsByCategory.removeAllViews();

        String url  = new UrlBuilder(AccessLinks.EVENTS_4_CATEGORY).setUrlParameter("category" , currentCategory+"").getUrl();

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



    private class CategoryAdapter extends BaseAdapter{
        String  [] categories ;
        public CategoryAdapter (String [] categories){
            this.categories = categories ;
        }

        @Override
        public int getCount() {
            return categories.length;
        }

        @Override
        public Object getItem(int position) {
            return categories[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
         View view = inflater.inflate(R.layout.category_view , null ) ;
            TextView temp = (TextView) view.findViewById(R.id.categoryText);
            temp.setText(categories[position]);
        return view ;
        }
    }

    private void initial(View view) {
        currentCategory = 1 ;

        categoriesView = (AppCompatSpinner) view.findViewById(R.id.categoriesView);
        refreshCategory = (SwipeRefreshLayout) view.findViewById(R.id.refreshCategory);

        events = new ArrayList<>() ;
        eventsByCategory = (RecyclerView) view.findViewById(R.id.eventsByCategory);

        categoriesView.setAdapter(new CategoryAdapter(getResources().getStringArray(R.array.categories)));

    }


}
