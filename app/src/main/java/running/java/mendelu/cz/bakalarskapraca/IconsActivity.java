package running.java.mendelu.cz.bakalarskapraca;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.Habit;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.IconAuthorAdapter;


public class IconsActivity extends AppCompatActivity {

    private RecyclerView author1;
    private RecyclerView author2;
    private RecyclerView author3;
    private RecyclerView author4;
    private RecyclerView author5;
    private RecyclerView author6;

    private TextView author1Text;
    private TextView author2Text;
    private TextView author3Text;
    private TextView author4Text;
    private TextView author5Text;
    private TextView author6Text;

    private IconAuthorAdapter iconAuthorAdapter1;
    private IconAuthorAdapter iconAuthorAdapter2;
    private IconAuthorAdapter iconAuthorAdapter3;
    private IconAuthorAdapter iconAuthorAdapter4;
    private IconAuthorAdapter iconAuthorAdapter5;
    private IconAuthorAdapter iconAuthorAdapter6;


    private HabitMainRepository habitMainRepository;




    public IconsActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons);

        habitMainRepository = new HabitMainRepository(getApplicationContext());
        /*((MainViewActivity) getActivity()).setActionBarTitle("Použité ikony");



*/
        author1Text = (TextView) findViewById(R.id.firstAuthor);
        author2Text = (TextView) findViewById(R.id.secondAuthor);
        author3Text = (TextView) findViewById(R.id.thirdAuthor);
        author4Text = (TextView) findViewById(R.id.forthAuthor);
        author5Text = (TextView) findViewById(R.id.fifthAuthor);
        author6Text = (TextView) findViewById(R.id.sixAuthor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



//        loadRecyclerViews();


        //return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
