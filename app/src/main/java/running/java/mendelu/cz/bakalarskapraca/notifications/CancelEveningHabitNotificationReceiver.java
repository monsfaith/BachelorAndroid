package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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

        if (in.getExtras() != null){
            id = in.getIntExtra("CANCEL",0);
        }

        int requestCode = id*100;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, in,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Toast.makeText(context, "request code zrusenie " + requestCode, Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);


    }
}
