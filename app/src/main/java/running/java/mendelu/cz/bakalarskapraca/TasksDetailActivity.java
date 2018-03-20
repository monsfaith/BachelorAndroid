package running.java.mendelu.cz.bakalarskapraca;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.ExamAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.MainOpenHelper;
import running.java.mendelu.cz.bakalarskapraca.db.ResultAdapter;

public class TasksDetailActivity extends AppCompatActivity {

    private ExamMainRepository examMainRepository;
    private ExamAdapter examAdapter;
    private ExamAdapter examAdapterTomorrow;
    private ExamAdapter examAdapterOther;

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewTomorrow;
    private RecyclerView recyclerViewOther;

    private TextView todayView;
    private TextView tomorrowView;
    private TextView otherView;


    //private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_detail);

        //docasna nahrada za ListView
        //detailedTasks = (ListView) findViewById(R.id.listViewDetailedTasks);
        //examMainRepository = new ExamMainRepository(this);
        //examAdapter = new ExamAdapter(this, new Date());
        //detailedTasks.setAdapter(examAdapter);

        recyclerView = (RecyclerView) findViewById(R.id.tasksDetailRecyclerView);
        recyclerViewTomorrow = (RecyclerView) findViewById(R.id.tasksDetailSecondRecyclerView);
        recyclerViewOther = (RecyclerView) findViewById(R.id.tasksDetailThirdRecyclerView);
        todayView = (TextView) findViewById(R.id.textDate);
        tomorrowView = (TextView) findViewById(R.id.textDateTomorrow);
        otherView = (TextView) findViewById(R.id.textDateOtherDays);

        examMainRepository = new ExamMainRepository(this);

        setViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    Toast.makeText(view.getContext(), "Year=" + year + " Month=" + month + " Day=" + dayOfMonth, Toast.LENGTH_LONG).show();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR,year);
                    cal.set(Calendar.MONTH,month);
                    cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    Date date = zeroTime(cal.getTime());
                    setExamAdaper(date);
                Toast.makeText(view.getContext(), date.getTime() + "", Toast.LENGTH_LONG).show();

            }
        });*/

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

    private void calendarToZeroes(Calendar cal){
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private void setViews(){
        Calendar cal = Calendar.getInstance();
        calendarToZeroes(cal);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        todayView.setText(sdf.format(cal.getTime()));

        examAdapter = new ExamAdapter(this, examMainRepository.getExamResults(cal.getTime()));
        recyclerView.setAdapter(examAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        cal.add(Calendar.DATE,1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,1);
        tomorrowView.setText(sdf.format(cal.getTime()));

        examAdapterTomorrow = new ExamAdapter(this, examMainRepository.getExamResults(cal.getTime()));
        recyclerViewTomorrow.setAdapter(examAdapterTomorrow);
        recyclerViewTomorrow.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        cal.add(Calendar.DATE,1);
        examAdapterOther = new ExamAdapter(this, examMainRepository.getOtherExamResults(cal.getTime()));
        recyclerViewOther.setAdapter(examAdapterOther);
        recyclerViewOther.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        otherView.setText("od " + sdf.format(cal.getTime()));
    }

    public void onResume(){
        super.onResume();
        setViews();
    }


}
