package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Monika on 06.04.2018.
 */

public class ExamTomorrowReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        cancelExam(context);
        setExamNotificationTomorrow(context);

    }

    private void cancelExam(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent cancelIntent = new Intent(context, CancelExamReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 500, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), cancelPendingIntent);
    }

    private void setExamNotificationTomorrow(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Intent i = new Intent(context, ExamNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
