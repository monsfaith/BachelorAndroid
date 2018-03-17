package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

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
        updateTimes(1,planMainRepository);
        updateTimes(2,planMainRepository);
        updateTimes(3,planMainRepository);
        updateTimes(4,planMainRepository);

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
