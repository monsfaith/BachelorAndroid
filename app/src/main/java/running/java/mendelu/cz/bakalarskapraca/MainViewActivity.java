package running.java.mendelu.cz.bakalarskapraca;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import running.java.mendelu.cz.bakalarskapraca.db.ExamAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;

public class MainViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CharSequence mTitle;
    private Integer[] integers = {1,2,3,4,5,7,8,9};
    private Button butt;
    private Button buttShow;
    private FloatingActionButton floatingButton;

    private ExamMainRepository examMainRepository;
    private ExamAdapter examAdapter;
    private ListView listView;
    ArrayList<Quote> quotes = new ArrayList<>();
    private String quote;
    private FloatingActionButton fab;


    //https://www.raywenderlich.com/127544/android-gridview-getting-started

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        /*butt = (Button) findViewById(R.id.buttonMoreee);
        butt = (Button) findViewById(R.id.planShowMore);
        floatingButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonAdd);
        listView = (ListView) findViewById(R.id.examListView);*/


        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrameLayout,new MainOverviewFragment());
        ft.commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quote = "";
        getJsonQuotes();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        setQuoteVisible();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_camera);
    }

    public void getJsonQuotes(){
        String json;
        try {
            InputStream inputStream = getAssets().open("quotesjson.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i=0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                quotes.add(new Quote(jsonObject.getString("quote"), jsonObject.getString("author")));
                //Toast.makeText(getApplicationContext(), quotes.size() + " size of quotes", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /*public void loadListView(){
        examMainRepository = new ExamMainRepository(getApplicationContext());
        listView = (ListView) findViewById(R.id.examListView);
        examAdapter = new ExamAdapter(this);
        listView.setAdapter(examAdapter);
    }

    public void onResume(){
        super.onResume();
        loadListView();

    }*/

    private void setQuoteVisible(){
        if (quotes.size() > 40){
            fab.setVisibility(View.VISIBLE);
            Random rand = new Random();
            int n = rand.nextInt(quotes.size()) + 1;
            quote = quotes.get(n).getText() + " - " + quotes.get(n).getAuthor();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view,quote, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

    }

    public void onResume(){
        super.onResume();
        setQuoteVisible();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            /*if (getApplicationContext() != MainViewActivity.this){
                Intent i = new Intent(this, MainViewActivity.class);
                startActivity(i);
            }*/

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrameLayout,new MainOverviewFragment());
            ft.commit();

        } else if (id == R.id.nav_gallery) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrameLayout,new ResultsFragment());
            ft.commit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*public void detailShow(View view){
        Intent i = new Intent(MainViewActivity.this, TasksDetailActivity.class);
        startActivity(i);
    }

    public void showMore(View view){
        Intent i = new Intent(this, MyPlanTabsActivity.class);
        startActivity(i);
    }

    public void addExamShow(View view){
        Intent i = new Intent(this, CreateExamActivity.class);
        startActivity(i);
    }

    public void addExamMainButton(View view){
        Intent i = new Intent(getApplicationContext(),CreateExamActivity.class);
        startActivity(i);
    }*/


    }


