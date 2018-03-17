package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 13.03.2018.
 */

public class LunchHabitNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent shownIntent = new Intent(context, HabitNotificationActivity.class);
        //shownIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 300, shownIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_menu_send).setSound(Settings.System.DEFAULT_NOTIFICATION_URI).setContentTitle("Obedny plan").setContentText("Je cas na splnenie tvojich ritualov. Zo svojich vybranych aktivit si zrealizuj lubovolne mnozstvo. Je vsak dolezite zrealizovat aspon jednu. Nastartuj sa!").setAutoCancel(true);

        notificationManager.notify(300, builder.build());

    }
}
