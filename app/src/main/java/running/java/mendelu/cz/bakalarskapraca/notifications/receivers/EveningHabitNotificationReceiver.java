package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.notifications.HabitNotificationActivity;

/**
 * Created by Monika on 13.03.2018.
 */

public class EveningHabitNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent shownIntent = new Intent(context, HabitNotificationActivity.class);
        int id = 0;
        if (intent.getExtras() != null){
            id = intent.getIntExtra("REQUESTCODE", 0);
        }
        //shownIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestCode = id*100;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, shownIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_menu_gallery).setSound(Settings.System.DEFAULT_NOTIFICATION_URI).setContentTitle("Vecerny plan super" + requestCode).setContentText("Je cas na splnenie tvojich ritualov. Zo svojich vybranych aktivit si zrealizuj lubovolne mnozstvo. Je vsak dolezite zrealizovat aspon jednu. Nastartuj sa!").setAutoCancel(true);

        notificationManager.notify(requestCode, builder.build());
    }
}
