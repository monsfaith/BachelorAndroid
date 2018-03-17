package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.ExamPostponeNotificationActivity;
import running.java.mendelu.cz.bakalarskapraca.notifications.StartMainNotificationsActivity;

/**
 * Created by Monika on 12.03.2018.
 */

public class ExamNotificationReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent shownIntent = new Intent(context, StartMainNotificationsActivity.class);

        //pripomenutie
        Intent postponeIntent = new Intent(context, ExamPostponeNotificationActivity.class);

        //zacatie planu
        Intent doExamIntent = new Intent(context, StartMainNotificationsActivity.class);


        ExamMainRepository examMainRepository = new ExamMainRepository(context);
        SubjectMainRepository subjectMainRepository = new SubjectMainRepository(context);
        int size = examMainRepository.findAllExams().size();

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 500, shownIntent,0);
        PendingIntent doIntent = PendingIntent.getActivity(context, 500, doExamIntent, 0);
        PendingIntent delayIntent = PendingIntent.getActivity(context,500, postponeIntent ,0);
        PendingIntent notTodayIntent = intent.getParcelableExtra("NOTTODAY");

        //long closestExamId = intent.getIntExtra("EXAMID",0);
        Exam exam = examMainRepository.getClosestExam();
        String subjectString = (subjectMainRepository.getById(examMainRepository.getById(exam.getId()).getSubjectId())).getName();



        NotificationCompat.Action actionDelay = new NotificationCompat.Action(R.drawable.ic_menu_send, "odlozit", delayIntent);
        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_menu_share, "robit", doIntent);
        NotificationCompat.Action actionNot = new NotificationCompat.Action(R.drawable.ic_menu_share, "dnes nie", notTodayIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_menu_camera).setSound(Settings.System.DEFAULT_NOTIFICATION_URI).setContentTitle("Treba sa ucit na predmet " + subjectString).addAction(actionDelay).addAction(action).addAction(actionNot).setContentText("Na ucenie zostava " + exam.getDays() + " dni").setAutoCancel(true);

        notificationManager.notify(500, builder.build());
    }
}
