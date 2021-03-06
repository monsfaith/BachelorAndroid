package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.SleepNotificationActivity;

/**
 * Created by Monika on 01.04.2018.
 */

public class SleepReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i = new Intent(context, SleepNotificationActivity.class);
        long id = -25;
        if (intent.getExtras() != null){
            id = intent.getLongExtra("examSleepID",0);
            i.putExtra("examSleepID",id);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 50, i, PendingIntent.FLAG_UPDATE_CURRENT);





        String content = "";


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
        Calendar threeHours = Calendar.getInstance();
        ExamMainRepository examMainRepository = new ExamMainRepository(context);

        if (examMainRepository.getClosestExam() != null) {
            long date = examMainRepository.getClosestExam().getDate().getTime();
            Exam exam = examMainRepository.getNextClosestExam(date);

            if (exam != null){
                long dateNext = exam.getDate().getTime();
                calendar.setTimeInMillis(dateNext);
                calendar.add(Calendar.HOUR_OF_DAY, -8);
                threeHours.setTimeInMillis(dateNext);
                threeHours.set(Calendar.HOUR, 2);
                threeHours.set(Calendar.MINUTE, 0);
                if (threeHours.after(calendar.getTimeInMillis())) {


                    if (alarmManager != null) {
                        if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                            } else {
                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                            }





                            //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        }
                    }
                }
            }
        }
    }

    private boolean sameDay(long date){
        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarMy = Calendar.getInstance();
        calendarMy.setTimeInMillis(date);
        boolean sameDay = calendarCurrent.get(Calendar.YEAR) == calendarMy.get(Calendar.YEAR) &&
                calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMy.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }
}
