package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.w3c.dom.Text;

import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.MyActivitiesTab2Fragment;
import running.java.mendelu.cz.bakalarskapraca.MyPlanTabsActivity;
import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.HabitAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.PlanHabitAssociation;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;

/**
 * Created by Monika on 12.03.2018.
 */

public class HabitNotificationActivity extends AppCompatActivity{

    private HabitMainRepository habitMainRepository;
    private PlanMainRepository planMainRepository;
    private ExamMainRepository examMainRepository;
    private RecyclerView actualRecyclerView;
    private HabitAdapter habitAdapter;
    private TextView actualPlanText;
    private FloatingActionButton fabAccept;
    private TextView infoAbout;
    private ImageButton startStudyButton;
    private TextView startStudy;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        habitMainRepository = new HabitMainRepository(getApplicationContext());
        planMainRepository = new PlanMainRepository(getApplicationContext());
        examMainRepository = new ExamMainRepository(getApplicationContext());
        actualRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewActualHabits);
        actualPlanText = (TextView) findViewById(R.id.actualPlanText);
        fabAccept = (FloatingActionButton) findViewById(R.id.fabOk);
        infoAbout = (TextView) findViewById(R.id.infoAboutWhatToDo);
        startStudyButton = (ImageButton) findViewById(R.id.startStudyButton);
        startStudy = (TextView) findViewById(R.id.startStudy);

        if (getIntent().getExtras() != null){
            int id = getIntent().getIntExtra("ID",0);
            Log.i("ID od lets do it","Prislo idcko taketo" + id);
            setAdapter(id);
        }

        startStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StartMainNotificationsActivity.class);
                startActivity(i);
            }
        });

        YoYo.with(Techniques.Swing)
                .duration(700)
                .repeat(3)
                .playOn(fabAccept);

        /*fabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

    }

    public List<PlanHabitAssociation> getHabits(int idPlan){
        List<PlanHabitAssociation> habits = null;
        switch (idPlan){
            case 1:
                habits = habitMainRepository.getDailyPlanHabits();
                actualPlanText.setText(R.string.celodenny);

                if (examMainRepository.findNextExams().size() > 0) {
                    startStudyButton.setVisibility(View.VISIBLE);
                    startStudy.setVisibility(View.VISIBLE);
                }
                if (habits.size() == 0){
                    noRituals();
                }
                break;
            case 2:
                habits =  habitMainRepository.getMorningPlanHabits();
                actualPlanText.setText(R.string.ranny);
                if (habits.size() == 0){
                    noRituals();
                }
                break;
            case 3:
                habits = habitMainRepository.getLunchPlanHabits();
                actualPlanText.setText(R.string.obedny);
                if (habits.size() == 0){
                    noRituals();
                }
                break;
            case 4:
                habits = habitMainRepository.getEveningPlanHabits();
                actualPlanText.setText(R.string.vecerny);
                if (habits.size() == 0){
                    noRituals();
                }
        }
        return habits;

    }

    private void noRituals(){
        infoAbout.setText("Nie sú vybraté žiadne rituály. ");
        fabAccept.setBackgroundResource(R.drawable.ic_add_circle_black_24dp);
        fabAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MyPlanTabsActivity.class);
                startActivity(i);
            }
        });
    }



    private void setAdapter(final int idPlan){
        habitAdapter = new HabitAdapter(getApplicationContext(), getHabits(idPlan));
            fabAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getHabits(idPlan).size() != 0){
                        finish();
                    } else {
                        noRituals();
                        finish();
                    }
                }
            });
        actualRecyclerView.setAdapter(habitAdapter);
        actualRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


}
