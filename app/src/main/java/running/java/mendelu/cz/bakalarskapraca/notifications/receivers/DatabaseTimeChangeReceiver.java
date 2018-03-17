package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        planMainRepository = new PlanMainRepository(context);

        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarMy = Calendar.getInstance();
        calendarMy.setTime(planMainRepository.getByType(1).getFromTime());
        boolean sameDay = calendarCurrent.get(Calendar.YEAR) == calendarMy.get(Calendar.YEAR) &&
                calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMy.get(Calendar.DAY_OF_YEAR);
        if (!sameDay) {
            updateTimes(1, planMainRepository);
            updateTimes(2, planMainRepository);
            updateTimes(3, planMainRepository);
            updateTimes(4, planMainRepository);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Toast.makeText(context, "aktualizace na cas " + sdf.format(planMainRepository.getByType(2).getToTime().getTime()), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTimes(long idPlan, PlanMainRepository planMainRepository){
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


    }
}
