package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.ExamNotificationReceiver;

/**
 * Created by Monika on 17.03.2018.
 */

public class ExamPostponeNotificationActivity extends AppCompatActivity {

    private Button delay15;
    private Button delay30;
    private Button delay60;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_postpone_exam_notification);
        this.setFinishOnTouchOutside(false);
        delay15 = (Button) findViewById(R.id.delay15);
        delay30 = (Button) findViewById(R.id.delay30);
        delay60 = (Button) findViewById(R.id.delay60);

        delay15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExamNotification(15);
                finish();

            }
        });

        delay30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExamNotification(30);
                finish();
            }
        });

        delay60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExamNotification(60);
                finish();
            }
        });

    }

    private void setExamNotification(int time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, time);
        Intent i = new Intent(getApplicationContext(), ExamNotificationReceiver.class);

        Intent delayIntent = new Intent();
        delayIntent.setAction("odlozit");
        PendingIntent pendingIntentDelay = PendingIntent.getBroadcast(getApplicationContext(), 500, delayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent notTodayIntent = new Intent();
        delayIntent.setAction("dnes nie");
        PendingIntent pendingIntentNotToday = PendingIntent.getBroadcast(getApplicationContext(), 500, notTodayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent doIntent = new Intent();
        doIntent.setAction("odlozit");
        PendingIntent pendingIntentDo = PendingIntent.getBroadcast(getApplicationContext(), 500, doIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        i.putExtra("DELAY",pendingIntentDelay);
        i.putExtra("DO",pendingIntentDo);
        i.putExtra("NOTTODAY",pendingIntentNotToday);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        //Maybe intent
        /*Intent maybeReceive = new Intent();
        maybeReceive.setAction(MAYBE_ACTION);
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(this, 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.calendar_question, "Partly", pendingIntentMaybe);*/

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 300000, pendingIntent);

    }

}
