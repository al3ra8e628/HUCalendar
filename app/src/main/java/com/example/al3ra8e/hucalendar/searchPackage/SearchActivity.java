package com.example.al3ra8e.hucalendar.searchPackage;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.other.Keys;

public class SearchActivity extends AppCompatActivity {
    ViewPager mViewPager;
    MyViewPagerAdapter adapter;
    TabLayout tabLayout ;
    SearchActivity me ;
    SearchByCalendar searchByCalendar ;
    SearchByCategory searchByCategory ;
    SearchByFaculty searchByFaculty ;
    String personId ;
    int logInState ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initial(this);

    }




    public class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                      return searchByCategory = new SearchByCategory(SearchActivity.this , personId , logInState);
                case 1:
                      //return new BlankFragment() ;
                      return searchByCalendar = new SearchByCalendar( personId , logInState , SearchActivity.this) ;
                case 2:
                      return searchByFaculty = new SearchByFaculty(SearchActivity.this , personId , logInState);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(logInState == Keys.CREATOR_LOG_IN) {
           // MenuItem t1 = menu.add(0, 0, 0, "");
           // t1.setIcon(getResources().getDrawable(R.drawable.create_event_icon)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        else if(logInState == Keys.ADMIN_LOG_IN) {
            //MenuItem t1 = menu.add(0, 0, 0, "");
            //t1.setIcon(getResources().getDrawable(R.drawable.update1)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            // add admin menu icons

        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case 0 :
                 if(logInState == Keys.CREATOR_LOG_IN){
                     Toast.makeText(getApplicationContext() , " go to create event activity " , Toast.LENGTH_LONG).show(); ;
                 }else if(logInState == Keys.ADMIN_LOG_IN){
                     Toast.makeText(getApplicationContext() , " admin action menu item " , Toast.LENGTH_LONG).show(); ;
                 }
            }
        return super.onOptionsItemSelected(item);
    }


    private void initial(SearchActivity context){
        personId = getIntent().getStringExtra("person_id") ;
        logInState = Integer.parseInt(getIntent().getStringExtra(Keys.LOG_IN_STATE_KEY)) ;
        me = context ;
        mViewPager = (ViewPager) findViewById(R.id.myViewPager);
        adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("search by \ncategory") ;
        tabLayout.getTabAt(1).setText("calendar") ;
        tabLayout.getTabAt(2).setText("search by \nfaculty") ;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initial(this);
    }
}
