package running.java.mendelu.cz.bakalarskapraca;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import io.fabric.sdk.android.Fabric;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import running.java.mendelu.cz.bakalarskapraca.db.ExamAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.ResultAdapter;


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
    private String author;
    private FloatingActionButton fab;
    private ResultsFragment resultsFragment;
    private MainOverviewFragment mainOverviewFragment;
    private Dialog quoteDialog;

    //https://www.raywenderlich.com/127544/android-gridview-getting-started

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main_view);
        quoteDialog = new Dialog(this);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        /*butt = (Button) findViewById(R.id.buttonMoreee);
        butt = (Button) findViewById(R.id.planShowMore);
        floatingButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonAdd);
        listView = (ListView) findViewById(R.id.examListView);*/

        mainOverviewFragment = new MainOverviewFragment();
        resultsFragment = new ResultsFragment();

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.replace(R.id.mainFrameLayout,new MainOverviewFragment());
        ft.replace(R.id.mainFrameLayout,mainOverviewFragment);
        ft.commit();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quote = "";
        author = "";
        getJsonQuotes();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        YoYo.with(Techniques.Swing)
                .duration(500)
                .repeat(2)
                .playOn(fab);
        fab.setVisibility(View.GONE);
        //setQuoteVisible();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);


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
            int n = rand.nextInt(quotes.size());
            quote = quotes.get(n).getText();
            author = quotes.get(n).getAuthor();
            /*fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view,quote, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });*/
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton closeDialog;
                    TextView quoteText;
                    TextView quoteAuthor;
                    quoteDialog.setContentView(R.layout.quote_popup);
                    closeDialog = (ImageButton) quoteDialog.findViewById(R.id.closeQuoteDialog);
                    quoteText = (TextView) quoteDialog.findViewById(R.id.quote);
                    quoteAuthor = (TextView) quoteDialog.findViewById(R.id.quoteAuthor);
                    quoteText.setText(quote);
                    quoteAuthor.setText(author);
                    closeDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quoteDialog.dismiss();
                        }
                    });
                    quoteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    quoteDialog.show();
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_main, menu);
        return true;
    }*/

    /*@Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            /*if (getApplicationContext() != MainViewActivity.this){
                Intent i = new Intent(this, MainViewActivity.class);
                startActivity(i);
            }*/

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrameLayout,mainOverviewFragment);
            ft.commit();

        } else if (id == R.id.nav_exams) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrameLayout,resultsFragment);
            ft.commit();
        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_icons) {

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


