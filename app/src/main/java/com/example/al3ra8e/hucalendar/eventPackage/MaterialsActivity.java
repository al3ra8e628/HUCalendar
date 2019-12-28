package com.example.al3ra8e.hucalendar.eventPackage;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.al3ra8e.hucalendar.other.TextFormat;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MaterialsActivity extends AppCompatActivity {
    String eventTitle ;

    ListView materialsView ;
    SwipeRefreshLayout refresher ;
    ArrayList<Material> materials ;
    DownloadManager downloadManager ;
    int eventId  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        eventTitle = TextFormat.firstCharUpper(getIntent().getStringExtra("event_title")) ;
        eventId = Integer.parseInt(getIntent().getStringExtra("event_id"));
        toolbar.setTitle(eventTitle+" - "+"Materials ");
        setSupportActionBar(toolbar);


        Toast.makeText(getApplicationContext() , "@_@" , Toast.LENGTH_LONG).show();

        materials = new ArrayList<>() ;
        materialsView = (ListView) findViewById(R.id.materialsListView);
        getMaterials(eventId);
        refresher = (SwipeRefreshLayout) findViewById(R.id.refresher);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMaterials(eventId);
                refresher.setRefreshing(false);
            }
        });

    }


    private void getMaterials(int eventId){
        materials = new ArrayList<>() ;
        String url = new UrlBuilder(AccessLinks.GET_EVENT_MATERIALS).setUrlParameter("event_id" , eventId+"").getUrl() ;
        new RequestBuilder()
                .setMethod(Request.Method.GET)
                .setUrl(url)
                .setActivity(this)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Materials") ;
                            for (int i = jsonArray.length()-1; i>=0 ; i--){
                                JSONObject jso = jsonArray.getJSONObject(i) ;
                                Material temp = new Material() ;
                                temp.setEventId(jso.getInt("event_id")) ;
                                temp.setFileName(jso.getString("file_name"));
                                temp.setMaterialId(jso.getInt("material_id")) ;
                                temp.setMaterialTitle(jso.getString("material_title")) ;
                                materials.add(temp);
                            }
                         materialsView.setAdapter(new MaterialsAdapter(materials));
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

    class MaterialsAdapter extends BaseAdapter {

        ArrayList<Material> materials ;

        private MaterialsAdapter(ArrayList<Material> materials){
            this.materials = materials ;
        }

        @Override
        public int getCount() {
            return materials.size();
        }

        @Override
        public Object getItem(int i) {
            return materials.get(i).getMaterialTitle();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.material_view, null);

            TextView materialTitle = (TextView) view.findViewById(R.id.materialText);
            materialTitle.setText(materials.get(i).getMaterialTitle()) ;

            LinearLayout materialView = (LinearLayout) view.findViewById(R.id.materialView);

            materialView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE) ;
                    String fileName = materials.get(i).getFileName() ;
                    String url = new UrlBuilder(AccessLinks.MATERIALS_DIRECTORY).setUrlPath(fileName).getUrl() ;
                    Log.e("url = " , url) ;
                    Uri uri = Uri.parse(url);
                    Toast.makeText(getApplicationContext() , uri.toString() , Toast.LENGTH_LONG ).show();
                    DownloadManager.Request request = new DownloadManager.Request(uri) ;
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) ;
                    long refrence = downloadManager.enqueue(request) ;
                }
            });
            return view;
        }
    }





}
