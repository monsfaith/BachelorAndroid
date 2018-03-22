package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.R;
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
    private RecyclerView actualRecyclerView;
    private HabitAdapter habitAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        habitMainRepository = new HabitMainRepository(getApplicationContext());
        planMainRepository = new PlanMainRepository(getApplicationContext());
        actualRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewActualHabits);

        if (getIntent().getExtras() != null){
            int id = getIntent().getIntExtra("ID",0);
            Log.i("ID od lets do it","Prislo idcko taketo" + id);
            setAdapter(id);
        }

    }

    public List<PlanHabitAssociation> getHabits(int idPlan){
        List<PlanHabitAssociation> habits = null;
        switch (idPlan){
            case 1:
                habits = habitMainRepository.getDailyPlanHabits();
                break;
            case 2:
                habits =  habitMainRepository.getMorningPlanHabits();
                break;
            case 3:
                habits = habitMainRepository.getLunchPlanHabits();
                break;
            case 4:
                habits = habitMainRepository.getEveningPlanHabits();
        }
        return habits;

    }



    private void setAdapter(int idPlan){
        habitAdapter = new HabitAdapter(getApplicationContext(), getHabits(idPlan));
        actualRecyclerView.setAdapter(habitAdapter);
        actualRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


}
