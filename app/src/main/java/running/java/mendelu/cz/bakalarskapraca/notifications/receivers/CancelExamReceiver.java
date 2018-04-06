package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Monika on 04.04.2018.
 */

public class CancelExamReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(context, CancelExamReceiver.class);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        /*if (in.getExtras() != null){
            id = in.getIntExtra("CANCEL",0);
        }*/

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 500, in,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Toast.makeText(context, "Odložené na zajtra ", Toast.LENGTH_SHORT).show();
        notificationManager.cancel(500);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }
}
