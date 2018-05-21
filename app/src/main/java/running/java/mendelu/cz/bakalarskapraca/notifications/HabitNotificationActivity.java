package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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

import java.util.Calendar;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.MyActivitiesTab2Fragment;
import running.java.mendelu.cz.bakalarskapraca.MyPlanTabsActivity;
import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.HabitAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanHabitAssociation;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.DailyHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

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
    private TextView hiddenText;
    private TextView hiddenText2;

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
        hiddenText = (TextView) findViewById(R.id.hiddenText);
        hiddenText2 = (TextView) findViewById(R.id.hiddenText2);

        if (getIntent().getExtras() != null){
            int id = getIntent().getIntExtra("ID",0);
            Log.i("ID od lets do it","Prislo idcko taketo" + id);
            setAdapter(id);
        }

        actualRecyclerView.setNestedScrollingEnabled(false);

        startStudyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), StartMainNotificationsActivity.class);
                startActivity(i);
            }
        });

        /*YoYo.with(Techniques.Swing)
                .duration(700)
                .repeat(3)
                .playOn(fabAccept);*/

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(300); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "20");
        config.setDismissTextColor(getResources().getColor(R.color.yellow_700));
        sequence.setConfig(config);
        sequence.addSequenceItem(create(hiddenText,
                "Teraz je čas na študijnú prestávku. Označ aktivitu, ktorú ideš vykonať."));
        sequence.addSequenceItem(create(hiddenText2, "Máš k dispozícii vždy celý zoznam pridaných aktivít pre voľnosť realizácie."));
        sequence.start();

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
        infoAbout.setText("Nie sú vybraté žiadne činnosti. ");
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
                        if (idPlan != 1) {
                            setBreak(idPlan);
                        }
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

    private void setBreak(int idPlan){
        Plan plan = planMainRepository.getByType(idPlan);
        long repetition = plan.getRepetition();
        int min = 10;
        if (repetition == 1800000){
            min = 5;
        } else if ((repetition == 3600000) || (repetition == 6000000)){
            min = 10;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,min);

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), DailyHabitNotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 25, i, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private MaterialShowcaseView create(View view, String content){
        MaterialShowcaseView.Builder builder = new MaterialShowcaseView.Builder(this)
                .setTarget(view)
                .setDismissText("Rozumiem!")
                .setDismissTextColor(getResources().getColor(R.color.yellow_700))
                //.setMaskColour(Color.argb(195, 0, 0, 0))
                .setContentText(content)
                .setDelay(300)
                .setDismissOnTouch(true);

        MaterialShowcaseView showcaseView = builder.build();
        return showcaseView;
    }


}
