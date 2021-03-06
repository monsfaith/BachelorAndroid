package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;

/**
 * Created by Monika on 06.04.2018.
 */

public class ExamTomorrowReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "rusim exam receiver", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //notificationManager.cancel(500);

        Toast.makeText(context,"Odložené na zajtra", Toast.LENGTH_SHORT).show();
        cancelExam(context);

        //cancelIntent(context);
        setExamNotificationTomorrow(context);

    }

    private void cancelExam(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent cancelIntent = new Intent(context, CancelExamReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 500, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), cancelPendingIntent);
    }

    private void cancelIntent(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent cancelIntent = new Intent(context, ExamTomorrowReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 500, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        cancelPendingIntent.cancel();
        alarmManager.cancel(cancelPendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), cancelPendingIntent);
    }

    private void setExamNotificationTomorrow(Context context){
        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.MINUTE,00);
        //calendar.set(Calendar.HOUR_OF_DAY,8);
        SubjectMainRepository subjectMainRepository = new SubjectMainRepository(context);
        calendar.set(Calendar.MINUTE,subjectMainRepository.getProjectById(1).getMinute());
        calendar.set(Calendar.HOUR_OF_DAY,subjectMainRepository.getProjectById(1).getHour());

        calendar.add(Calendar.DAY_OF_MONTH,1);
        Intent i = new Intent(context, ExamNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
