package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;

/**
 * Created by Monika on 17.03.2018.
 */

public class DatabaseTimeChangeReceiver extends BroadcastReceiver {

    PlanMainRepository planMainRepository;
    Context myContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        planMainRepository = new PlanMainRepository(context);
        myContext = context;

        /*Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTimeInMillis(System.currentTimeMillis());
        Calendar calendarMy = Calendar.getInstance();
        calendarMy.setTime(planMainRepository.getByType(1).getFromTime());
        boolean sameDay = calendarCurrent.get(Calendar.YEAR) == calendarMy.get(Calendar.YEAR) &&
                calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMy.get(Calendar.DAY_OF_YEAR);
        if (!sameDay) {
            updateTimes(1, planMainRepository);
            updateTimes(2, planMainRepository);
            updateTimes(3, planMainRepository);
            updateTimes(4, planMainRepository);*/
        planMainRepository.updatePlanTime(1);
        planMainRepository.updatePlanTime(2);
        planMainRepository.updatePlanTime(3);
        planMainRepository.updatePlanTime(4);
        ContentValues contentValues = new ContentValues();
        contentValues.put("enabled",true);
        planMainRepository.update2(1,contentValues);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Toast.makeText(context, "aktualizace na cas " + sdf.format(planMainRepository.getByType(2).getToTime().getTime()), Toast.LENGTH_SHORT).show();

            setDailyPlan();

        }




    private void setDailyPlan(){
        Plan dailyPlan = planMainRepository.getByType(1);
        if (getCurrentTime() < dailyPlan.getFromTime().getTime() || getCurrentTime() == dailyPlan.getFromTime().getTime()) {
            setHabitNotification(dailyPlan.getFromTime().getTime());
        } else if (getCurrentTime() < dailyPlan.getToTime().getTime() || getCurrentTime() == dailyPlan.getToTime().getTime()) {
            setHabitNotification(getCurrentTime());
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dailyPlan.getFromTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            setHabitNotification(cal.getTimeInMillis());
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

    private void setHabitNotification(long time){
        AlarmManager alarmManager = (AlarmManager) myContext.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(myContext, EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(myContext, 100, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(1).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(myContext, CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",1);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(myContext, 100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(1).getToTime().getTime(), cancelPendingIntent);
    }

}




    /*private void updateTimes(long idPlan, PlanMainRepository planMainRepository){
        Calendar calendar = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
        Plan plan = planMainRepository.getByType(idPlan);
        calendar.setTime(plan.getFromTime());
        calendar.add(Calendar.DAY_OF_MONTH,1);
        contentValues.put("from_time", calendar.getTimeInMillis());
        calendar.setTime(plan.getToTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        contentValues.put("to_time",calendar.getTimeInMillis());
        planMainRepository.update2(idPlan,contentValues);


    }*/
