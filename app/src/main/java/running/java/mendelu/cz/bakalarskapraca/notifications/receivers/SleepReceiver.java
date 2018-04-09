package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.Calendar;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.SleepNotificationActivity;

/**
 * Created by Monika on 01.04.2018.
 */

public class SleepReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent(context, SleepNotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 50, i, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_class_black_24dp)
                .setColor(ContextCompat.getColor(context, R.color.lime_700))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle("Je čas ísť spať")
                .setLights(0xf9cc00, 300, 3000)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText("Spánok je dôležitým faktorom pre zvládnutie skúšky")
                .setAutoCancel(true);

        notificationManager.notify(50, builder.build());
        setSleepNotification(context);


    }

    public void setSleepNotification(Context context){

        Intent i = new Intent(context, SleepReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 50, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        ExamMainRepository examMainRepository = new ExamMainRepository(context);

        if (examMainRepository.findNextExams().size() > 1) {
            long date = examMainRepository.findNextExams().get(1).getDate().getTime();
            calendar.setTimeInMillis(date);
            calendar.add(Calendar.HOUR_OF_DAY, -8);
            if (alarmManager != null){
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent );
            }
        }

    }
}
