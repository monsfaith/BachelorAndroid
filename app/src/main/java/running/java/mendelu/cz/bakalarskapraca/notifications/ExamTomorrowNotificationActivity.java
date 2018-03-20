package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.ExamNotificationReceiver;

/**
 * Created by Monika on 17.03.2018.
 */

public class ExamTomorrowNotificationActivity extends AppCompatActivity {

    private Button delay15;
    private Button delay30;
    private Button delay60;

    private Button startDailyPlan;
    private Button cancelDialog;
    private PlanMainRepository planMainRepository;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_postpone_tomorrow);
        this.setFinishOnTouchOutside(false);
        planMainRepository = new PlanMainRepository(getApplicationContext());
        startDailyPlan = (Button) findViewById(R.id.startDailyPlan);
        cancelDialog = (Button) findViewById(R.id.cancelTomorrowDialog);

        /*if (getIntent().getExtras() != null) {
            int id = getIntent().getIntExtra("AFTER", 0);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(id);
        }*/

        startDailyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("enabled", true);
                planMainRepository.update2(1,contentValues);
                setTimeToNotification();
                setExamNotificationTomorrow();
                finish();
            }
        });

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExamNotificationTomorrow();
                finish();
            }
        });

    }

    private void setExamNotificationTomorrow(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Intent i = new Intent(getApplicationContext(), ExamNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    private void setDailyHabitNotification(long time){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(1).getRepetition(), pendingIntent);
    }

    private void setTimeToNotification(){
        Calendar calendar = Calendar.getInstance();
        Plan plan = planMainRepository.getByType(1);
        if (getCurrentTime() < plan.getFromTime().getTime() || getCurrentTime() == plan.getFromTime().getTime()) {
            setDailyHabitNotification(plan.getFromTime().getTime());
        } else if (getCurrentTime() < plan.getToTime().getTime()) {
            calendar.setTimeInMillis(getCurrentTime());
            calendar.add(Calendar.MINUTE,1);
            setDailyHabitNotification(calendar.getTimeInMillis());
        } else {
            calendar.setTime(plan.getFromTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            setDailyHabitNotification(calendar.getTimeInMillis());
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
