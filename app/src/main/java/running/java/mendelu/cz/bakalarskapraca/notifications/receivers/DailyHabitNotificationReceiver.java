package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.notifications.HabitNotificationActivity;

/**
 * Created by Monika on 12.03.2018.
 */

public class DailyHabitNotificationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        //mediaPlayer.start();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent shownIntent = new Intent(context, StudyBreakReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 25, shownIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_lens_black_24dp)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setColor(ContextCompat.getColor(context, R.color.lime_700))
                .setLights(0xf9cc00, 300, 3000)
                .setContentTitle("Koniec prestávky")
                .setAutoCancel(true);

        notificationManager.notify(25, builder.build());


    }
}
