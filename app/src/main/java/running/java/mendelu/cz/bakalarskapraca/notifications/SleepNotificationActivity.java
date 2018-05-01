package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;

/**
 * Created by Monika on 10.04.2018.
 */

public class SleepNotificationActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ExamAdapter examAdapter;
    private Button gotIt;
    private long id;
    private ExamMainRepository examMainRepository;
    private List<Exam> exams = Collections.emptyList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sleep);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSleep);
        gotIt = (Button) findViewById(R.id.gotItButton);
        examMainRepository = new ExamMainRepository(getApplicationContext());



        if (getIntent().getExtras() != null){
            id = getIntent().getLongExtra("examSleepID",-25);
            Exam exam = examMainRepository.getById(id);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(exam.getDate().getTime());
            calendar.set(Calendar.HOUR_OF_DAY,0);
            exams = examMainRepository.getExamResultsList(calendar.getTimeInMillis());

        }

        //Toast.makeText(getApplicationContext(),exams.size() + " id" + id  + "velkost",Toast.LENGTH_SHORT).show();

        if (exams.size() !=  0){
            examAdapter = new ExamAdapter(getApplicationContext(), exams);
            recyclerView.setAdapter(examAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
