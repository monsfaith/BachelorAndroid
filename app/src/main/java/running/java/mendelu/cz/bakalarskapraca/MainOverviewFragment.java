package running.java.mendelu.cz.bakalarskapraca;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.ExamAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.IconHabitAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.MainOpenHelper;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanHabitAssociation;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelEveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.DatabaseRecordReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.DatabaseTimeChangeReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.ExamNotificationReceiver;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainOverviewFragment extends Fragment {

    private Button butt;
    private Button buttShow;
    private FloatingActionButton floatingButton;

    private ExamMainRepository examMainRepository;
    private ExamAdapter examAdapter;
    private IconHabitAdapter iconHabitAdapter;
    private ListView listView;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewHabits;
    private MainOpenHelper mainOpenHelper;
    private SQLiteDatabase database;
    private PlanMainRepository planMainRepository;
    private HabitMainRepository habitMainRepository;
    private CardView cardViewTwo;
    private int actualPlan;
    private Plan dailyPlan;
    private Plan morningPlan;
    private Plan lunchPlan;
    private Plan eveningPlan;
    private TextView actualPlanTextView;


    public MainOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main_overview, container, false);

        butt = (Button) view.findViewById(R.id.buttonMoreee);
        buttShow = (Button) view.findViewById(R.id.planShowMore);
        floatingButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonAdd);
        recyclerView = (RecyclerView) view.findViewById(R.id.mainViewExamRecyclerView);
        recyclerViewHabits = (RecyclerView) view.findViewById(R.id.mainOverviewRecyclerViewPlan);
        cardViewTwo = (CardView) view.findViewById(R.id.card_view_two);
        mainOpenHelper = new MainOpenHelper(getActivity());
        database = mainOpenHelper.getWritableDatabase();
        planMainRepository = new PlanMainRepository(getActivity());
        habitMainRepository = new HabitMainRepository(getActivity());
        examMainRepository = new ExamMainRepository(getActivity());
        actualPlanTextView = (TextView) view.findViewById(R.id.actualPlanTextView);
        
        if (planMainRepository.getAllPlans().size() != 4) {
            init();
            setDatabaseNotification();
            setDatabasePlanTimeDaily();
        } else {
            dailyPlan = planMainRepository.getByType(1);
            morningPlan = planMainRepository.getByType(2);
            lunchPlan = planMainRepository.getByType(3);
            eveningPlan = planMainRepository.getByType(4);
        }

        setDatabaseNotification();
        setDatabasePlanTimeDaily();
        loadListView();

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), TasksDetailActivity.class);
                startActivity(i);
            }
        });

        buttShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MyPlanTabsActivity.class);
                startActivity(i);
            }
        });

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),CreateExamActivity.class);
                startActivity(i);
            }
        });

        if (morningPlan.getNotification() == false) {
            int id = morningPlan.getType();
            Calendar calendar = Calendar.getInstance();
            if (getCurrentTime() < morningPlan.getFromTime().getTime() || getCurrentTime() == morningPlan.getFromTime().getTime()) {
                setEveningHabitNotification(morningPlan.getFromTime().getTime(), id);
                updateSetNotification(2);
            } else if (getCurrentTime() < morningPlan.getToTime().getTime()) {
                calendar.setTimeInMillis(getCurrentTime());
                calendar.add(Calendar.MINUTE,1);
                setEveningHabitNotification(calendar.getTimeInMillis(), id);
                updateSetNotification(2);
            } else {
                calendar.setTime(morningPlan.getFromTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                setEveningHabitNotification(calendar.getTimeInMillis(), id);
                updateSetNotification(2);
            }
        }

        if (lunchPlan.getNotification() == false) {
            int id = lunchPlan.getType();
            Calendar calendar = Calendar.getInstance();
            if (getCurrentTime() < lunchPlan.getFromTime().getTime() || getCurrentTime() == lunchPlan.getFromTime().getTime()) {
                setEveningHabitNotification(lunchPlan.getFromTime().getTime(), id);
                updateSetNotification(3);
            } else if (getCurrentTime() < lunchPlan.getToTime().getTime()) {
                calendar.setTimeInMillis(getCurrentTime());
                calendar.add(Calendar.MINUTE,1);
                setEveningHabitNotification(getCurrentTime(), id);
                updateSetNotification(3);
            } else {
                calendar.setTime(lunchPlan.getFromTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                setEveningHabitNotification(calendar.getTimeInMillis(), id);
                updateSetNotification(3);
            }
        }

        if (eveningPlan.getNotification() == false) {
            int id = eveningPlan.getType();
            Calendar calendar = Calendar.getInstance();
            if (getCurrentTime() < eveningPlan.getFromTime().getTime() || getCurrentTime() == eveningPlan.getFromTime().getTime()) {
                setEveningHabitNotification(eveningPlan.getFromTime().getTime(), id);
                updateSetNotification(4);
            } else if (getCurrentTime() < eveningPlan.getToTime().getTime()) {
                calendar.setTimeInMillis(getCurrentTime());
                calendar.add(Calendar.MINUTE,1);
                setEveningHabitNotification(calendar.getTimeInMillis(), id);
                updateSetNotification(4);
            } else {
                calendar.setTime(eveningPlan.getFromTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                setEveningHabitNotification(calendar.getTimeInMillis(), id);
                updateSetNotification(4);
            }
        }



        setExamNotification();
        return view;
    }

    //nastavenie notifikacie na upozornovanie aktivit

    /*private void setMorningHabitNotification(){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), MorningHabitNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 200, i, 0);
        //setExactPlanNotification(alarmManager, pendingIntent, 2,200);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(2).getFromTime().getTime(), planMainRepository.getByType(2).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), CancelMorningHabitNotificationReceiver.class);
        //cancelIntent.putExtra("CANCELINTENT", pendingIntent);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), 200, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(2).getToTime().getTime(), cancelPendingIntent);


    }

    private void setMorningHabitNotification(long time){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), MorningHabitNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 200, i, 0);
        //setExactPlanNotification(alarmManager, pendingIntent, 2,200);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(2).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), CancelMorningHabitNotificationReceiver.class);
        //cancelIntent.putExtra("CANCELINTENT", pendingIntent);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), 200, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(2).getToTime().getTime(), cancelPendingIntent);
    }


    private void setLunchHabitNotification(){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), LunchHabitNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 300, i, 0);
        //setExactPlanNotification(alarmManager, pendingIntent, 3,300);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(3).getFromTime().getTime(), planMainRepository.getByType(3).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), CancelMorningHabitNotificationReceiver.class);
        //cancelIntent.putExtra("CANCELINTENT", pendingIntent);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), 300, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(3).getToTime().getTime(), cancelPendingIntent);
    }

    private void setLunchHabitNotification(long time){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), LunchHabitNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 300, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(3).getRepetition(), pendingIntent);
        Intent cancelIntent = new Intent(getActivity(), CancelMorningHabitNotificationReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), 300, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(3).getToTime().getTime(), cancelPendingIntent);
    }

    private void setEveningHabitNotification(){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), EveningHabitNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 400, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(4).getFromTime().getTime(), planMainRepository.getByType(4).getRepetition(), pendingIntent);
        Intent cancelIntent = new Intent(getActivity(), CancelMorningHabitNotificationReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), 400, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(4).getToTime().getTime(), cancelPendingIntent);
    }
*/
    private void setEveningHabitNotification(long time, int idPlan){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",idPlan);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), idPlan*100, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(idPlan).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",idPlan);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), idPlan*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(idPlan).getToTime().getTime(), cancelPendingIntent);
    }


    private void setExamNotification(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);
        Intent i = new Intent(getActivity(), ExamNotificationReceiver.class);

        /*Intent delayIntent = new Intent();
        delayIntent.setAction("odlozit");
        PendingIntent pendingIntentDelay = PendingIntent.getBroadcast(getActivity(), 500, delayIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        long id = examMainRepository.getClosestExam().getId();
        i.putExtra("EXAMID", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        //Maybe intent
        /*Intent maybeReceive = new Intent();
        maybeReceive.setAction(MAYBE_ACTION);
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(this, 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.calendar_question, "Partly", pendingIntentMaybe);*/

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

    }

    private void setDatabaseNotification(){
        Intent i = new Intent(getContext(), DatabaseRecordReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 700, i, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
        long time = planMainRepository.getLastTime();
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    //ulozit kazdy den novy datum k casom planom v databazi
    private void setDatabasePlanTimeDaily(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,30);
        long time = calendar.getTimeInMillis();
        Intent i = new Intent(getContext(), DatabaseTimeChangeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 800, i, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);

    }


    public void loadListView(){
        examAdapter = new ExamAdapter(getActivity(), getExamResults());
        recyclerView.setAdapter(examAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (setPlan() != null){
            iconHabitAdapter = new IconHabitAdapter(getActivity(), setPlan());
            recyclerViewHabits.setAdapter(iconHabitAdapter);
            recyclerViewHabits.setLayoutManager(new GridLayoutManager(getActivity(),4));
        } else {
            TextView tx = new TextView(getActivity());
            tx.setText("Nie je momentálne zvolený žiadny plán. ");
            tx.setPadding(50, 210, 0, 50);
            tx.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            cardViewTwo.addView(tx);
        }




    }

    //urcenie View na zaklade prebiehajuceho planu
    private List<PlanHabitAssociation> setPlan() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");


        if (dailyPlan.getEnabled() == false) {
            if ((setPlanDateToCalendar(morningPlan.getType()) > morningPlan.getFromTime().getTime() || setPlanDateToCalendar(morningPlan.getType()) == morningPlan.getFromTime().getTime()) && (setPlanDateToCalendar(morningPlan.getType()) < morningPlan.getToTime().getTime())){
                Toast.makeText(getActivity(), "morning " + actualPlan + " aktu", Toast.LENGTH_LONG).show();
                actualPlan = 2;
                actualPlanTextView.setText(R.string.ranny);
                return habitMainRepository.getMorningPlanHabits();

            } else if ((setPlanDateToCalendar(lunchPlan.getType()) > lunchPlan.getFromTime().getTime() || setPlanDateToCalendar(lunchPlan.getType()) == lunchPlan.getFromTime().getTime()) && (setPlanDateToCalendar(lunchPlan.getType()) < lunchPlan.getToTime().getTime())){
                Toast.makeText(getActivity(), "lunch " + actualPlan + " aktu", Toast.LENGTH_LONG).show();
                actualPlan = 3;
                actualPlanTextView.setText(R.string.obedny);
                return habitMainRepository.getLunchPlanHabits();

           /* } else if ((time > eveningPlan.getFromTime().getTime() || time == eveningPlan.getFromTime().getTime()) && (time < eveningPlan.getToTime().getTime() || time == eveningPlan.getToTime().getTime())) {
                Toast.makeText(getActivity(), "evening " + actualPlan + " aktu", Toast.LENGTH_LONG).show();
                actualPlan = 4;
                actualPlanTextView.setText(R.string.vecerny);
                return habitMainRepository.getEveningPlanHabits();*/

            /*/*else if ((getOnlyTime(calendar.getTime()) > getOnlyTime(eveningPlan.getFromTime()) || getOnlyTime(calendar.getTime()) == getOnlyTime(eveningPlan.getFromTime())) && (getOnlyTime(calendar.getTime()) < getOnlyTime(eveningPlan.getToTime()))){*/


            } else if ((setPlanDateToCalendar(eveningPlan.getType()) > eveningPlan.getFromTime().getTime() || setPlanDateToCalendar(eveningPlan.getType()) == eveningPlan.getFromTime().getTime()) && (setPlanDateToCalendar(eveningPlan.getType()) < eveningPlan.getToTime().getTime() || setPlanDateToCalendar(eveningPlan.getType()) == eveningPlan.getToTime().getTime())){


                Toast.makeText(getActivity(), "evening " + actualPlan + " aktu " + sdf.format(setPlanDateToCalendar(eveningPlan.getType())) + " " + sdf.format(eveningPlan.getFromTime().getTime()), Toast.LENGTH_LONG).show();
                actualPlan = 4;
                actualPlanTextView.setText(R.string.vecerny);
                return habitMainRepository.getEveningPlanHabits();
            } else {
                actualPlan = 0;
                actualPlanTextView.setText("Je to false, ale nic");
                Toast.makeText(getActivity(), "nic" + sdf.format(setPlanDateToCalendar(dailyPlan.getType())) + "", Toast.LENGTH_LONG).show();
                return null;
            }
        } else if ((setPlanDateToCalendar(dailyPlan.getType()) > dailyPlan.getFromTime().getTime() || setPlanDateToCalendar(dailyPlan.getType()) == dailyPlan.getFromTime().getTime()) && (setPlanDateToCalendar(dailyPlan.getType()) < dailyPlan.getToTime().getTime() || setPlanDateToCalendar(dailyPlan.getType()) == dailyPlan.getToTime().getTime())){
                Toast.makeText(getActivity(), "daily " + actualPlan + " aktu", Toast.LENGTH_LONG).show();
                actualPlan = 1;
                actualPlanTextView.setText(R.string.celodenny);
                return habitMainRepository.getDailyPlanHabits();
            } else {
                actualPlan = 0;
                actualPlanTextView.setText("Je to true, ale nic");
                Toast.makeText(getActivity(), "nic " + sdf.format(setPlanDateToCalendar(dailyPlan.getType())) + sdf.format(dailyPlan.getFromTime().getTime()) + " " + sdf.format(dailyPlan.getToTime().getTime()), Toast.LENGTH_LONG).show();
                return null;
            }
    }

    private int compareTime(Date date1, Date date2){
        int time1;
        int time2;

        time1 = (int) (date1.getTime() % (24*60*60*1000L));
        time2 = (int) (date2.getTime() % (24*60*60*1000L));
        return (time1 - time2);

    }

    private int getOnlyTime(Date date){
        int time;

        time = (int) (date.getTime() % (24*60*60*1000L));
        return time;
    }

    private long setPlanDateToCalendar(int idPlan){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        calendar.setTime(planMainRepository.getByType(idPlan).getFromTime());
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        return calendar.getTimeInMillis();


    }

    public void onResume(){
        super.onResume();
        loadListView();

    }

    public void onPause(){
        super.onPause();
        loadListView();
    }

    private long getCurrentDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTimeInMillis();
    }

    private long getCurrentTime(){
        long time = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTimeInMillis();
    }


    private Cursor getExamResults(){
        return database.rawQuery("SELECT e.*, s.name, s.shortcut FROM exam e left join subject s on e.subject_id = s._id where e.date = ?",new String[]{String.valueOf(getCurrentDate())});
    }

    private void init(){
        Calendar dailyCalendar = Calendar.getInstance();
        dailyCalendar.set(Calendar.MINUTE, 0);
        dailyCalendar.set(Calendar.MILLISECOND,0);
        dailyCalendar.set(Calendar.SECOND,0);

        dailyCalendar.set(Calendar.HOUR_OF_DAY, 8);
        long dailyFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY, 20);
        long dailyToTime = dailyCalendar.getTimeInMillis();

        dailyPlan = new Plan(dailyFromTime, dailyToTime, 1, false);
        planMainRepository.insert(dailyPlan);

        dailyCalendar.set(Calendar.HOUR_OF_DAY,8);
        long morningFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY,12);
        long morningToTime = dailyCalendar.getTimeInMillis();
        morningPlan = new Plan(morningFromTime, morningToTime, 2, true);
        planMainRepository.insert(morningPlan);

        dailyCalendar.set(Calendar.HOUR_OF_DAY,12);
        long lunchFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY,17);
        long lunchToTime = dailyCalendar.getTimeInMillis();
        lunchPlan = new Plan(lunchFromTime, lunchToTime, 3, true);
        planMainRepository.insert(lunchPlan);

        dailyCalendar.set(Calendar.HOUR_OF_DAY,17);
        long eveningFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY,22);
        long eveningToTime = dailyCalendar.getTimeInMillis();
        eveningPlan = new Plan(eveningFromTime, eveningToTime,4, true);
        planMainRepository.insert(eveningPlan);
    }

    private void updateSetNotification(long idPlan){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOTIFICATION",true);
        planMainRepository.update2(idPlan,contentValues);
    }






}
