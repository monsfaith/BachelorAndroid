package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

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


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isChecked = settings.getBoolean("turn_notif", true);
        String name = settings.getString("full_name","");

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
            if (name.trim().length() != 0){
                contentText = name + ", naštartuj sa do nového dňa! ";
                builder.setStyle(new NotificationCompat.InboxStyle().setSummaryText("Študijná prestávka").addLine(contentText));
                builder.setContentText("");


            } else {
                contentText = "Naštartuj sa do nového dňa! ";
                builder.setStyle(new NotificationCompat.InboxStyle().setSummaryText("Študijná prestávka").addLine(contentText));
                builder.setContentText("");


            }

        }

        if (requestCode == 300) {
            builder.setSmallIcon(R.drawable.ic_brightness_medium_black_24dp);
            contentTitle = "Obedný plán";
            if (name.trim().length() != 0){
                contentText = name + ", nezabúdaj si oddýchnuť ani v priebehu dňa";
                builder.setStyle(new NotificationCompat.InboxStyle().setSummaryText("Študijná prestávka").addLine(contentText));
                builder.setContentText("");


            } else {
                contentText = "Nezabúdaj si oddýchnuť ani v priebehu dňa";
                builder.setStyle(new NotificationCompat.InboxStyle().setSummaryText("Študijná prestávka").addLine(contentText));
                builder.setContentText("");

            }
        }

        if (requestCode == 400) {
            builder.setSmallIcon(R.drawable.ic_brightness_3_black_24dp);
            contentTitle = "Večerný plán";
            if (name.trim().length() != 0){
                contentText = name + ", ostaň bez stresu i ku koncu dňa";
                builder.setStyle(new NotificationCompat.InboxStyle().setSummaryText("Študijná prestávka").addLine(contentText));
                builder.setContentText("");

            } else {
                contentText = "Ostaň bez stresu i ku koncu dňa";
                builder.setStyle(new NotificationCompat.InboxStyle().setSummaryText("Študijná prestávka").addLine(contentText));
                builder.setContentText("");

            }
        }

        if (requestCode == 100) {
            builder.setSmallIcon(R.drawable.ic_lens_black_24dp);
            contentTitle = "Denný plán";
            if (name.trim().length() != 0){
                contentText = name + ", buď bližšie k svojím cieľom!";
                builder.setContentText(contentText);

            } else {
                contentText = "Buď bližšie k svojím cieľom!";
                builder.setContentText(contentText);


            }
        }


        builder.setContentTitle(contentTitle);

        if (isBadDaily(requestCode, planMainRepository.getByType(1).getEnabled()) == false) {
            if (id != 0) {
                if (!(isChecked == false && planMainRepository.getByType(1).getEnabled() == true))
                notificationManager.notify(requestCode, builder.build());
            }
        }
    }

    private boolean isBadDaily(int requestCode, boolean enabled){
        return (requestCode == 100 && enabled == false);
    }
}
