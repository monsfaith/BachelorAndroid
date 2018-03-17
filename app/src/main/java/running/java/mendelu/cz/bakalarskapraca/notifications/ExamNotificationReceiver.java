package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;

/**
 * Created by Monika on 12.03.2018.
 */

public class ExamNotificationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent shownIntent = new Intent(context, ExamNotificationStartActivity.class);
        //shownIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 500, shownIntent,0);

        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 200, shownIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        ExamMainRepository examMainRepository = new ExamMainRepository(context);
        int size = examMainRepository.findAllExams().size();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_menu_camera).setSound(Settings.System.DEFAULT_NOTIFICATION_URI).setContentTitle("Treba sa ucit " + size).setContentText("blalblal").setAutoCancel(true);

        notificationManager.notify(500, builder.build());
    }
}
