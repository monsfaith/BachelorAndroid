package running.java.mendelu.cz.bakalarskapraca.notifications;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.scheme.HostNameResolver;

import java.util.Calendar;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.ExamNotificationAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.MainOpenHelper;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Subject;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelEveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelExamReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.ExamNotificationReceiver;

/**
 * Created by Monika on 17.03.2018.
 */


public class StartMainNotificationsActivity extends AppCompatActivity {

    private Button letsDoIt;
    private Button cancel;
    private TextView info;
    private PlanMainRepository planMainRepository;
    private ExamNotificationAdapter examNotificationAdapter;
    private MainOpenHelper mainOpenHelper;
    private SQLiteDatabase database;
    private RecyclerView recyclerView;
    private ExamMainRepository examMainRepository;
    private ImageView noExamsImage;
    private TextView enjoyDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_start_exam_plan);
        this.setFinishOnTouchOutside(false);

        mainOpenHelper = new MainOpenHelper(getApplicationContext());
        database = mainOpenHelper.getWritableDatabase();



        letsDoIt = (Button) findViewById(R.id.letsDoIt);

        cancel = (Button) findViewById(R.id.letsCancelit);
        info = (TextView) findViewById(R.id.infoAboutStartingPlans);
        noExamsImage = (ImageView) findViewById(R.id.noExamsImage);
        enjoyDay = (TextView) findViewById(R.id.enjoyYourDay);

        planMainRepository = new PlanMainRepository(this);
        examMainRepository = new ExamMainRepository(this);
        recyclerView = (RecyclerView) findViewById(R.id.availableExamsRecyclerVie);
        examNotificationAdapter = new ExamNotificationAdapter(this, examMainRepository.getExamResultsListNotification());




        //examNotificationAdapter = new ExamNotificationAdapter(getApplicationContext(), getExamResults());

        //if (examMainRepository.getExamResultsListNotification().size() == 0){
          if (getIntent().getExtras() != null) {

              if (getIntent().getBooleanExtra("ISZERO", false) == true) {
                  enjoyDay.setVisibility(View.VISIBLE);
                  noExamsImage.setVisibility(View.VISIBLE);
                  letsDoIt.setText("Ok");
                  cancel.setVisibility(View.GONE);
                  info.setVisibility(View.GONE);
                  recyclerView.setVisibility(View.GONE);
                  letsDoIt.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      finish();
                                                  }
                                              }
                  );
              } else {
                  setNoZero();
                  /*recyclerView.setAdapter(examNotificationAdapter);
                  recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                  info.setText("Zvoľ si skúšku, ktorej príprave sa chceš dnes venovať, a spustí sa Ti intenzívnejší učebný plán");

                  letsDoIt.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          if (examNotificationAdapter.numberOfSelectedExams() > 0) {
                              //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                              //notificationManager.cancel(500);
                              cancelDailyNotifications();
                              setAllNotifications();
                              cancelExam();
                              setExamNotificationTomorrow();
                              finish();
                          } else {
                              Toast.makeText(getApplicationContext(), "Nutné zvoliť skúšku, na ktorú sa ideš pripravovať", Toast.LENGTH_SHORT).show();
                          }
                      }
                  });

                  cancel.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          if (examNotificationAdapter.numberOfSelectedExams() > 0) {
                              examNotificationAdapter.setZero(true);
                          }
                          finish();
                      }
                  });*/
              }
          } else {
              setNoZero();
          }






    }

    private void setNoZero(){
        recyclerView.setAdapter(examNotificationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        info.setText("Zvoľ si skúšku, ktorej príprave sa chceš dnes venovať, a spustí sa Ti intenzívnejší učebný plán.");

        letsDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (examNotificationAdapter.numberOfSelectedExams() > 0) {
                    //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    //notificationManager.cancel(500);
                    updateCheckedExams(examNotificationAdapter.getCheckedExams());
                    cancelDailyNotifications();
                    setAllNotifications();
                    cancelExam();
                    setExamNotificationTomorrow();
                    examNotificationAdapter.deleteCheckedExams();
                    examNotificationAdapter.clearCount();
                    finish();
                } else {
                    examNotificationAdapter.deleteCheckedExams();
                    Toast.makeText(getApplicationContext(), "Nutné zvoliť skúšku, na ktorú sa ideš pripravovať", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (examNotificationAdapter.numberOfSelectedExams() > 0) {
                    examNotificationAdapter.setZero(true);
                    //dala som to prec
                    //examNotificationAdapter.setNoExams();
                }
                examNotificationAdapter.deleteCheckedExams();
                examNotificationAdapter.clearCount();
                finish();
            }
        });
    }

    //private nastavenie notifikacii
    private void setHabitNotification(long time, int idPlan, long toTime){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",idPlan);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idPlan*100, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(idPlan).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getApplicationContext(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",idPlan);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), idPlan*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, toTime, cancelPendingIntent);
    }

    //nastavenie jednotlivych notifikacii k delenemu planu
    private void setAllNotifications() {
        Calendar calendar = Calendar.getInstance();
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        setNotification(2);
        setNotification(3);
        setNotification(4);
        }

    private void setNotification(int idPlan){
        Calendar calendar = Calendar.getInstance();
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        Plan plan = planMainRepository.getByType(idPlan);
        from.set(Calendar.HOUR_OF_DAY, plan.getFromHour());
        from.set(Calendar.MINUTE, plan.getFromMinute());
        to.set(Calendar.HOUR_OF_DAY,plan.getToHour());
        to.set(Calendar.MINUTE,plan.getToMinute());
        if (System.currentTimeMillis() < from.getTimeInMillis()) {
            setHabitNotification(from.getTimeInMillis(), idPlan, to.getTimeInMillis());
        } else if (System.currentTimeMillis() < to.getTimeInMillis()) {
            calendar.add(Calendar.MINUTE, 1);
            setHabitNotification(calendar.getTimeInMillis(), idPlan, to.getTimeInMillis());
        } else {
            from.add(Calendar.DAY_OF_MONTH,1);
            to.add(Calendar.DAY_OF_MONTH,1);
            setHabitNotification(from.getTimeInMillis(), 2, to.getTimeInMillis());
        }
    }


    //zrusenie denneho planu, ak boli notifiakcie spustene
    private void cancelDailyNotifications(){
        cancelDailyPlan();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent cancelIntent = new Intent(getApplicationContext(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",1);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), cancelPendingIntent);
    }

    //update v databazi, ze dailyplan nie je spusteny
    private void cancelDailyPlan(){
        PlanMainRepository planMainRepository = new PlanMainRepository(getApplicationContext());
        ContentValues contentValues = new ContentValues();
        contentValues.put("enabled",false);
        planMainRepository.update2(1,contentValues);
    }

    /*private Cursor getExamResults(){
        return database.rawQuery("SELECT e.*, s.name, s.color FROM exam e left join subject s on e.subject_id = s._id where e.date > ? order by e.date",new String[]{String.valueOf(System.currentTimeMillis())});
    }*/



    //ak zrusim tu exam notifikaciu, musim ju znova nastavit
    private void setExamNotificationTomorrow(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.set(Calendar.MINUTE,00);
        //calendar.set(Calendar.HOUR_OF_DAY,8);
        SubjectMainRepository subjectMainRepository = new SubjectMainRepository(getApplicationContext());
        calendar.set(Calendar.MINUTE,subjectMainRepository.getProjectById(1).getMinute());
        calendar.set(Calendar.HOUR_OF_DAY,subjectMainRepository.getProjectById(1).getHour());
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Intent i = new Intent(getApplicationContext(), ExamNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void cancelExam(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent cancelIntent = new Intent(getApplicationContext(), CancelExamReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 500, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), cancelPendingIntent);
    }

    private boolean sameDay(long time){
        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarMy = Calendar.getInstance();
        calendarMy.setTimeInMillis(time);
        boolean sameDay = calendarCurrent.get(Calendar.YEAR) == calendarMy.get(Calendar.YEAR) &&
                calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMy.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

    private void updateCheckedExams(List<Exam> exams){
        if (exams.size() != 0) {
            for (int i = 0; i < exams.size(); i++) {
                Exam e = exams.get(i);
                ContentValues contentValues = new ContentValues();
                int studying = e.getStudying();
                if (sameDay(e.getStudyDate()) == false) {
                    contentValues.put("studying", studying + 1);
                }
                contentValues.put("study_date", System.currentTimeMillis());
                examMainRepository.update2(e.getId(), contentValues);
                //Toast.makeText(, " id exam " + e.getId(), Toast.LENGTH_SHORT).show();

            }
        }
    }
}
