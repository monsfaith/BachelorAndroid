package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanHabitAssociation;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;

/**
 * Created by Monika on 12.03.2018.
 */

public class DatabaseRecordReceiver extends BroadcastReceiver{

    PlanMainRepository planMainRepository;


    @Override
    public void onReceive(Context context, Intent intent) {

        //planMainRepository = new PlanMainRepository(context);
        //long idUpdate = planMainRepository.updateAssociation();
        cancelDividedNotification(2, context);
        cancelDividedNotification(3, context);
        cancelDividedNotification(4, context);
        setDailyPlan(context);
        setExamNotificationTomorrow(context);
        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.start();
        Toast.makeText(context, "shit dabase", Toast.LENGTH_LONG).show();


    }

    private void setDailyPlan(Context context){
        ContentValues contentValues = new ContentValues();
        contentValues.put("enabled",true);
        planMainRepository = new PlanMainRepository(context);
        planMainRepository.update2(1,contentValues);
        Plan dailyPlan = planMainRepository.getByType(1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,dailyPlan.getFromHour());
        calendar.set(Calendar.MINUTE,dailyPlan.getFromMinute());

        Calendar to = Calendar.getInstance();
        to.set(Calendar.HOUR_OF_DAY,dailyPlan.getToHour());
        to.set(Calendar.MINUTE,dailyPlan.getToMinute());
        if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
            setHabitNotification(calendar.getTimeInMillis(), context, to.getTimeInMillis());
        } else if (System.currentTimeMillis() < to.getTimeInMillis()) {
            setHabitNotification(System.currentTimeMillis(), context, to.getTimeInMillis());
        } else {
            to.add(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            setHabitNotification(calendar.getTimeInMillis(), context, to.getTimeInMillis());
        }
    }


    private void setHabitNotification(long time, Context context, long toTime){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(1).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(context, CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",1);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 100, cancelIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, toTime, cancelPendingIntent);
    }

    private void setExamNotificationTomorrow(Context context){
        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.MINUTE,00);
        //calendar.set(Calendar.HOUR_OF_DAY,8);
        SubjectMainRepository subjectMainRepository = new SubjectMainRepository(context);
        calendar.set(Calendar.MINUTE,subjectMainRepository.getProjectById(1).getMinute());
        calendar.set(Calendar.HOUR_OF_DAY,subjectMainRepository.getProjectById(1).getHour());
        //calendar.add(Calendar.DAY_OF_MONTH,1);
        Intent i = new Intent(context, ExamNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //odstranenie notifikacii pre jednotlive plany
    private void cancelDividedNotification(int type, Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent cancelIntent = new Intent(context, CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",type);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, type*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), cancelPendingIntent);

    }
}
