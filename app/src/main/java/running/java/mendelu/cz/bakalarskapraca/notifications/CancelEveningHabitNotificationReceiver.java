package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Monika on 13.03.2018.
 */

public class CancelEveningHabitNotificationReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(context, CancelEveningHabitNotificationReceiver.class);
        int id = 0;
        if (intent.getExtras() != null){
            id = intent.getIntExtra("CANCEL",0);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id*100, in,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);


    }
}
