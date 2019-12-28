package com.example.al3ra8e.hucalendar.searchPackage;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.eventPackage.CustomEvent;
import com.example.al3ra8e.hucalendar.eventPackage.EventActivity;
import com.example.al3ra8e.hucalendar.other.DateFormat;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;




public class SearchByCalendar extends Fragment {
    private CompactCalendarView compactCalendarView;
    private TextView year;
    private ListView listView;
    private LayoutInflater inflater;
    private AppCompatActivity context ;
    String personId ;
    int loginState ;
    SwipeRefreshLayout swipeRefreshLayout ;

    public SearchByCalendar(String personId , int loginState ,  AppCompatActivity context){
        this.context = context ;
        this.personId = personId ;
        this.loginState = loginState ;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_search_by_calendar,null,false);
        this.inflater = inflater ;

        initial(layout);

        updateCalendar();
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                int day =  new DateFormat(dateClicked).getDay() ;
                listView.setSelection(day - 1);

            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                year.setText(new DateFormat(firstDayOfNewMonth).getMonthAndYear().toUpperCase());
                listView.setAdapter(new MyAdapter(compactCalendarView));

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DateFormat currentMonth = new DateFormat(compactCalendarView.getFirstDayOfCurrentMonth()) ;
                try {

                    Date selectedDate = DateFormat.makeDate((position+1),currentMonth.getMonthAsNumber(),currentMonth.getYear());
                    compactCalendarView.setCurrentDate(selectedDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateCalendar();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return layout;
    }


    public void interpretTheResponse(JSONObject response){
        try {


            compactCalendarView.removeAllEvents();
            JSONArray arr = response.getJSONArray("Events") ;
                for (int i = 0 ; i<arr.length() ; i++) {
                    JSONObject eventJs = new JSONObject(arr.get(i).toString());
                    Date date = DateFormat.makeDate("yyyy-MM-dd", eventJs.getString("event_date"));
                    int listViewIndex = new DateFormat(date).getDay()-1 ;
                    CustomEvent temp = new CustomEvent(date , eventJs.getString("event_title"),listViewIndex);
                    temp.setId(eventJs.getInt("event_id"));
                    compactCalendarView.addEvent(temp);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat current = new DateFormat(new Date());
        compactCalendarView.setCurrentDate(current.getDate());
        listView.setAdapter(new MyAdapter(compactCalendarView));
        listView.setSelection(current.getDay()-1);
        year.setText(current.getMonthAndYear());

    }

    public void updateCalendar(){
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading Events ...");
        pDialog.setCancelable(false);
        pDialog.show();

        String url = new UrlBuilder(AccessLinks.EVENTS_4_CALENDAR).getUrl() ;

        new RequestBuilder()
                .setUrl(url)
                .setMethod(Request.Method.GET)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        interpretTheResponse(response);
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(context , "no internet connection" , Toast.LENGTH_LONG).show();
                    }
                })
                .execute();

    }

    class MyAdapter extends BaseAdapter {

        int count;
        Date selectedDate;
        CompactCalendarView compactCalendarView;
        List<CustomEvent> events;

    public MyAdapter(CompactCalendarView compactCalendarView) {

        this.compactCalendarView = compactCalendarView;
        this.selectedDate = compactCalendarView.getFirstDayOfCurrentMonth();
        this.events = castEvents(compactCalendarView.getEventsForMonth(selectedDate));
        int month = new DateFormat(selectedDate).getMonthAsNumber();
        int days[] = new int[getNumberOfElement(month)];

        count = days.length;
            for (int i = 0; i < days.length; i++) {
                days[i] = i + 1;
            }

        }

        public ArrayList<CustomEvent> castEvents(List<Event> events) {
            ArrayList<CustomEvent> customEvents = new ArrayList<>();
            for(Event e : events){
                customEvents.add((CustomEvent) e);
            }
            return customEvents;
        }

        private int getNumberOfElement(int month) {
            switch (month) {
                case 12 : case 10: case 8: case 7: case 5  : case 3 : case 1:
                    return 31;

                case 11: case 9: case 6: case 4:
                    return 30;

                case 2:
                    int currentYear = new DateFormat(selectedDate).getYear();
                    if ((currentYear - 2012) % 4 == 0) {
                        return 29;
                    }
                    return 28;
            }
            return 0;
        }

        @Override
        public int getCount() {
            return count;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            DateFormat current = new DateFormat(selectedDate);

            View view = inflater.inflate(R.layout.event_in_list, parent , false);

            TextView dayAsNum = (TextView) view.findViewById(R.id.dayAsNum);
            TextView eventDesc = (TextView) view.findViewById(R.id.eventDesc);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.US);

            LinearLayout eventBackground = (LinearLayout) view.findViewById(R.id.eventBackground);
            dayAsNum.setText(""+(position + 1));

            try {

                Date temp =  DateFormat.makeDate((position + 1),current.getMonthAsNumber(),current.getYear()) ;

                String asWeek = dateFormat.format(temp);
                TextView dayAsName = (TextView) view.findViewById(R.id.dayAsName);
                dayAsName.setText(asWeek);

                CustomEvent e = new CustomEvent(temp);
                if (events.contains(e)) {
                    CustomEvent customEvent = events.get(events.indexOf(e));
                    eventDesc.setText(customEvent.getDesc());
                    eventBackground.setBackground(getResources().getDrawable(R.color.event_indicator_color));
                    eventDesc.setTextColor(getResources().getColor(R.color.white));
                } else {
                    eventBackground.setVisibility(View.INVISIBLE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            eventBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateFormat current = new DateFormat(selectedDate);

                    try { //position +1 = the day  ;
                        Date temp = DateFormat.makeDate((position + 1),current.getMonthAsNumber(),current.getYear()) ;

                        CustomEvent e = new CustomEvent(temp);
                        if(events.contains(e)) {
                         CustomEvent c = events.get(events.indexOf(e));

                            Intent i = new Intent(context , EventActivity.class) ;
                            i.putExtra("person_id" , personId+"");
                            i.putExtra(Keys.LOG_IN_STATE_KEY, loginState+"");

                            i.putExtra("eventId" , c.getId()+"") ;
                            startActivity(i);

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            return view;
        }
    }

    private void initial(View layout) {
        listView = (ListView) layout.findViewById(R.id.listView);
        year = (TextView) layout.findViewById(R.id.year);
       swipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.calendarRefresher);
        compactCalendarView = (CompactCalendarView) layout.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);


    }

}
