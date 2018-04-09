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
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.Calendar;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
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
        //skusam dat prec
        /*if (intent.getExtras() != null){
            exams = intent.getIntExtra("NOEXAMS",0);
        }*/

        Intent shownIntent = new Intent(context, StartMainNotificationsActivity.class);

        //pripomenutie
        //Intent postponeIntent = new Intent(context, ExamPostponeNotificationActivity.class);
        //postponeIntent.putExtra("NOTIFICATIONID",500);

        //zacatie planu
        Intent doExamIntent = new Intent(context, StartMainNotificationsActivity.class);

        //nie dnes
        Intent notToday = new Intent(context, ExamTomorrowReceiver.class);
        //Intent notToday = new Intent(context, ExamTomorrowNotificationActivity.class);
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

        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_done_black_24dp, "Začať", doIntent);
        NotificationCompat.Action actionNot = new NotificationCompat.Action(R.drawable.ic_menu_share, "Odložiť na zajtra", notTodayIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        String subjectString = "";
        int days = 0;
        Exam exam = examMainRepository.getClosestExam();
        if (exam != null){
            subjectString = (subjectMainRepository.getById(examMainRepository.getById(exam.getId()).getSubjectId())).getName();
            days = exam.getDays();
        }
        String contentTitle = "";
        String contentText = "";

        exams = examMainRepository.findNextExams().size();
        if (exams == 0){
            contentTitle = "Uži si deň voľna";
            contentText = "Nečakajú ťa žiadne skúšky";
        } else {
            contentTitle = "V blízkej dobe ťa čakajú skušky ";
            contentText = "Priprav sa na skúšky efektívne a bez stresov ";
            builder.addAction(action).addAction(actionNot);
        }


        //NotificationCompat.Action actionDelay = new NotificationCompat.Action(R.drawable.ic_menu_send, "odlozit", delayIntent);

        builder.setContentIntent(pendingShowIntent)
        .setSmallIcon(R.drawable.ic_class_black_24dp)
                .setColor(ContextCompat.getColor(context, R.color.lime_700))
        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        .setContentTitle(contentTitle)
        .setLights(0xf9cc00, 300, 3000)
        .setPriority(Notification.PRIORITY_DEFAULT)
        //.addAction(actionDelay)
        .setContentText(contentText)
        .setAutoCancel(true);

        PlanMainRepository planMainRepository = new PlanMainRepository(context);
        if (planMainRepository.getByType(1).getEnabled() == true) {
            notificationManager.notify(500, builder.build());
        }
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
