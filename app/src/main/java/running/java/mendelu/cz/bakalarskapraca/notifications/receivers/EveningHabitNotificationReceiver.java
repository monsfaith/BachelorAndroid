package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
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
        int cancelCode = 0;
        boolean cancel = false;
        if (intent.getExtras() != null) {
            id = intent.getIntExtra("REQUESTCODE", 0);
            shownIntent.putExtra("ID", id);

        }
        //shownIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestCode = id * 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, shownIntent, 0);
        String contentTitle = "";
        String contentText = "";
        PlanMainRepository planMainRepository = new PlanMainRepository(context);

        @SuppressLint("ResourceAsColor") NotificationCompat.Builder builder = new NotificationCompat
                .Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setColor(ContextCompat.getColor(context, R.color.lime_700))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setLights(0xf9cc00, 300, 3000)
                .setAutoCancel(true);

        if (requestCode == 200) {
            builder.setSmallIcon(R.drawable.ic_brightness_5_black_24dp);
            contentTitle = "Ranný plán";
            contentText = "Naštartuj sa do nového dňa! ";

        }

        if (requestCode == 300) {
            builder.setSmallIcon(R.drawable.ic_brightness_medium_black_24dp);
            contentTitle = "Obedný plán";
            contentText = "Nezabúdaj na svoje rituály ani v priebehu dňa";
        }

        if (requestCode == 400) {
            builder.setSmallIcon(R.drawable.ic_brightness_3_black_24dp);
            contentTitle = "Večerný plán";
            contentText = "Ostaň bez stresu i ku koncu dňa";
        }

        if (requestCode == 100) {
            builder.setSmallIcon(R.drawable.ic_lens_black_24dp);
            contentTitle = "Denný plán";
            contentText = "Buď bližšie k svojím cieľom!";
        }


        builder.setContentTitle(contentTitle + requestCode);
        builder.setContentText(contentText);

        if (isBadDaily(requestCode, planMainRepository.getByType(1).getEnabled()) == false) {
            if (id != 0) {
                notificationManager.notify(requestCode, builder.build());
            }
        }
    }

    private boolean isBadDaily(int requestCode, boolean enabled){
        return (requestCode == 100 && enabled == false);
    }
}
