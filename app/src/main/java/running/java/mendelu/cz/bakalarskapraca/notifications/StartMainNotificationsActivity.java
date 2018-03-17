package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;

/**
 * Created by Monika on 17.03.2018.
 */

public class StartMainNotificationsActivity extends AppCompatActivity {

    private Button letsDoIt;
    private TextView info;
    private PlanMainRepository planMainRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_start_exam_plan);
        this.setFinishOnTouchOutside(false);

        letsDoIt = (Button) findViewById(R.id.letsDoIt);
        info = (TextView) findViewById(R.id.infoAboutStartingPlans);
        planMainRepository = new PlanMainRepository(getApplicationContext());

        letsDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllNotifications();
                finish();

            }
        });
    }

    private void setHabitNotification(long time, int idPlan){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",idPlan);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idPlan*100, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(idPlan).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getApplicationContext(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",idPlan);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idPlan*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(idPlan).getToTime().getTime(), cancelPendingIntent);
    }


    private void setAllNotifications() {
        Calendar calendar = Calendar.getInstance();
        Plan morningPlan = planMainRepository.getByType(2);
        if (getCurrentTime() < morningPlan.getFromTime().getTime() || getCurrentTime() == morningPlan.getFromTime().getTime()) {
            setHabitNotification(morningPlan.getFromTime().getTime(), 2);
        } else if (getCurrentTime() < morningPlan.getToTime().getTime()) {
                calendar.setTimeInMillis(getCurrentTime());
                calendar.add(Calendar.MINUTE, 1);
                setHabitNotification(calendar.getTimeInMillis(), 2);
            } else {
                calendar.setTime(morningPlan.getFromTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                setHabitNotification(calendar.getTimeInMillis(), 2);
            }

            Plan lunchPlan = planMainRepository.getByType(3);
            if (getCurrentTime() < lunchPlan.getFromTime().getTime() || getCurrentTime() == lunchPlan.getFromTime().getTime()) {
                setHabitNotification(lunchPlan.getFromTime().getTime(), 3);
            } else if (getCurrentTime() < lunchPlan.getToTime().getTime()) {
                calendar.setTimeInMillis(getCurrentTime());
                calendar.add(Calendar.MINUTE, 1);
                setHabitNotification(getCurrentTime(), 3);
            } else {
                calendar.setTime(lunchPlan.getFromTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                setHabitNotification(calendar.getTimeInMillis(), 3);
            }


            Plan eveningPlan = planMainRepository.getByType(4);
            if (getCurrentTime() < eveningPlan.getFromTime().getTime() || getCurrentTime() == eveningPlan.getFromTime().getTime()) {
                setHabitNotification(eveningPlan.getFromTime().getTime(), 4);
            } else if (getCurrentTime() < eveningPlan.getToTime().getTime()) {
                calendar.setTimeInMillis(getCurrentTime());
                calendar.add(Calendar.MINUTE, 1);
                setHabitNotification(calendar.getTimeInMillis(), 4);
            } else {
                calendar.setTime(eveningPlan.getFromTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                setHabitNotification(calendar.getTimeInMillis(), 4);
            }
        }

    private long getCurrentTime(){
        long time = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTimeInMillis();
    }
}
