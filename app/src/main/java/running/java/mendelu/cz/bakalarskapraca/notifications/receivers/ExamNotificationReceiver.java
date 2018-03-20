package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.ExamPostponeNotificationActivity;
import running.java.mendelu.cz.bakalarskapraca.notifications.ExamTomorrowNotificationActivity;
import running.java.mendelu.cz.bakalarskapraca.notifications.StartMainNotificationsActivity;

/**
 * Created by Monika on 12.03.2018.
 */

public class ExamNotificationReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int exams = 0;
        if (intent.getExtras() != null){
            exams = intent.getIntExtra("NOEXAMS",0);
        }

        Intent shownIntent = new Intent(context, StartMainNotificationsActivity.class);

        //pripomenutie
        //Intent postponeIntent = new Intent(context, ExamPostponeNotificationActivity.class);
        //postponeIntent.putExtra("NOTIFICATIONID",500);

        //zacatie planu
        Intent doExamIntent = new Intent(context, StartMainNotificationsActivity.class);

        //nie dnes
        Intent notToday = new Intent(context, ExamTomorrowNotificationActivity.class);
        notToday.putExtra("AFTER",500);

        ExamMainRepository examMainRepository = new ExamMainRepository(context);
        SubjectMainRepository subjectMainRepository = new SubjectMainRepository(context);
        int size = examMainRepository.findAllExams().size();

        PendingIntent pendingShowIntent = PendingIntent.getActivity(context, 500, shownIntent,0);
        PendingIntent doIntent = PendingIntent.getActivity(context, 500, doExamIntent, 0);

        //zrusit a odlozit, teda vymenit za novu
        //PendingIntent delayIntent = PendingIntent.getActivity(context,500, postponeIntent ,PendingIntent.FLAG_CANCEL_CURRENT);

        /*AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(delayIntent);*/

        //zrusit na dnes a vytvorit novu na dalsi den
        PendingIntent notTodayIntent = PendingIntent.getActivity(context, 500, notToday,
                PendingIntent.FLAG_CANCEL_CURRENT);
        //alarmManager.cancel(notTodayIntent);

        //long closestExamId = intent.getIntExtra("EXAMID",0);
        String subjectString = "";
        int days = 0;
        Exam exam = examMainRepository.getClosestExam();
        if (exam != null){
            subjectString = (subjectMainRepository.getById(examMainRepository.getById(exam.getId()).getSubjectId())).getName();
            days = exam.getDays();
        }
        String contentTitle = "";
        String contentText = "";
        if (exams == 1){
            contentTitle = "Uzi si den volna.";
            contentText = "Neboli pridane ziadne skusky.";
        } else {
            contentTitle = "Treba sa ucit na predmet " + subjectString;
            contentText = "Na ucenie zostava " + days + " dni";
        }


        //NotificationCompat.Action actionDelay = new NotificationCompat.Action(R.drawable.ic_menu_send, "odlozit", delayIntent);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_menu_share, "robit", doIntent);
        NotificationCompat.Action actionNot = new NotificationCompat.Action(R.drawable.ic_menu_share, "dnes nie", notTodayIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingShowIntent)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle(contentTitle)
                .setLights(0xf9cc00, 300, 3000)
                .addAction(action)
                .addAction(actionNot)
                .setPriority(Notification.PRIORITY_HIGH)
                //.addAction(actionDelay)
                .setContentText(contentText)
                .setAutoCancel(false);

        notificationManager.notify(500, builder.build());
    }

    private void setExamNotification(Context context){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);
        Intent i = new Intent(context, ExamNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

    }
}
