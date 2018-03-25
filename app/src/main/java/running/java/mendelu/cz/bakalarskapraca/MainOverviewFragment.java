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
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.DailyHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.DatabaseRecordReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.DatabaseTimeChangeReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.ExamNotificationReceiver;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainOverviewFragment extends Fragment implements FragmentInterface{

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

        //planMainRepository.deleteAllPlans();

        if (planMainRepository.getAllPlans().size() != 4) {
            init();
            //setDatabasePlanTimeDaily();
            setExamNotification();
        } else {
            dailyPlan = planMainRepository.getByType(1);
            morningPlan = planMainRepository.getByType(2);
            lunchPlan = planMainRepository.getByType(3);
            eveningPlan = planMainRepository.getByType(4);
        }

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            init();
            setDatabasePlanTimeDaily();
            setDatabaseNotification();
            setExamNotification();

            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }*/




        //setDatabaseNotification();
        //setDatabasePlanTimeDaily();

        //setDatabaseNotification();
        //setDatabasePlanTimeDaily();
        //setExamNotification();

        //setEveryDayPlanNotification();
        setDatabaseNotification();
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

        /*if (morningPlan.getNotification() == false) {
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
        }*/



        return view;
    }


    /*
    private void setLunchHabitNotification(long time){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), LunchHabitNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 300, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(3).getRepetition(), pendingIntent);
        Intent cancelIntent = new Intent(getActivity(), CancelMorningHabitNotificationReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), 300, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(3).getToTime().getTime(), cancelPendingIntent);
    }


*/
    //nastavenie notifikacie na upozornovanie aktivit
    private void setHabitNotification(long timeFrom, int idPlan, long timeTo){
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",idPlan);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), idPlan*100, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeFrom, planMainRepository.getByType(idPlan).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",idPlan);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), idPlan*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeTo, cancelPendingIntent);
    }

    //notifikacia, ktora nastavi kazdy den denny plan
    private void setEveryDayPlanNotification(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,7);
        cal.set(Calendar.MINUTE,55);
        if (System.currentTimeMillis() > cal.getTimeInMillis()){
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), DailyHabitNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 10, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    //zakomentovane veci, musim si to objasnit
    private void setExamNotification(){
        Calendar calendar = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
        Toast.makeText(getActivity(), examMainRepository.findNextExams().size() + " size of exams", Toast.LENGTH_SHORT).show();
        //if (examMainRepository.findNextExams().size() != 0){
            calendar.setTimeInMillis(System.currentTimeMillis());
            /*contentValues.put("enabled",false);
            planMainRepository.update2(1, contentValues);*/

            //calendar.add(Calendar.MINUTE, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        //nova cast, podla mna
            calendar.set(Calendar.MINUTE,00);
            calendar.set(Calendar.HOUR_OF_DAY,8);
            if (System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }

            Toast.makeText(getActivity(), "Exam Notification nastavena od " + sdf.format(calendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), ExamNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //} else {
            //calendar.set(Calendar.MINUTE,00);
            //calendar.set(Calendar.HOUR_OF_DAY,8);
            //contentValues.put("enabled",true);
            //planMainRepository.update2(1, contentValues);
            //setHabitNotification(calendar.getTimeInMillis(),1);
        setDailyPlan();

        }

    private void setDatabaseNotification(){
        Intent i = new Intent(getContext(), DatabaseRecordReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 700, i, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
        Calendar calendar = Calendar.getInstance();

        //calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,15);
        long time = calendar.getTimeInMillis();

        if (System.currentTimeMillis() > calendar.getTimeInMillis()){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            time = calendar.getTimeInMillis();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Toast.makeText(getActivity(), "Database Record update o" + sdf.format(calendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();
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
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();

        if (dailyPlan.getEnabled() == false) {
            if ((setPlanDateToCalendar(morningPlan.getType()) > morningPlan.getFromTime().getTime() || setPlanDateToCalendar(morningPlan.getType()) == morningPlan.getFromTime().getTime()) && (setPlanDateToCalendar(morningPlan.getType()) < morningPlan.getToTime().getTime())){
                from.set(Calendar.HOUR_OF_DAY,morningPlan.getFromHour());
                from.set(Calendar.MINUTE,morningPlan.getFromMinute());
                to.set(Calendar.HOUR_OF_DAY,morningPlan.getToHour());
                to.set(Calendar.MINUTE,morningPlan.getToMinute());
                Toast.makeText(getActivity(), "morning " + actualPlan + " aktu", Toast.LENGTH_LONG).show();
                actualPlan = 2;
                actualPlanTextView.setText(R.string.ranny);
                return habitMainRepository.getMorningPlanHabits();

            } else if ((setPlanDateToCalendar(lunchPlan.getType()) > lunchPlan.getFromTime().getTime() || setPlanDateToCalendar(lunchPlan.getType()) == lunchPlan.getFromTime().getTime()) && (setPlanDateToCalendar(lunchPlan.getType()) < lunchPlan.getToTime().getTime())){
                from.set(Calendar.HOUR_OF_DAY,lunchPlan.getFromHour());
                from.set(Calendar.MINUTE,lunchPlan.getFromMinute());
                to.set(Calendar.HOUR_OF_DAY,lunchPlan.getToHour());
                to.set(Calendar.MINUTE,lunchPlan.getToMinute());
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
                from.set(Calendar.HOUR_OF_DAY,eveningPlan.getFromHour());
                from.set(Calendar.MINUTE,eveningPlan.getFromMinute());
                to.set(Calendar.HOUR_OF_DAY,eveningPlan.getToHour());
                to.set(Calendar.MINUTE,eveningPlan.getToMinute());

                Toast.makeText(getActivity(), "evening " + actualPlan + " aktu " + sdf.format(setPlanDateToCalendar(eveningPlan.getType())) + " " + sdf.format(from.getTimeInMillis()), Toast.LENGTH_LONG).show();
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
            from.set(Calendar.HOUR_OF_DAY,dailyPlan.getFromHour());
            from.set(Calendar.MINUTE,dailyPlan.getFromMinute());
            to.set(Calendar.HOUR_OF_DAY,dailyPlan.getToHour());
            to.set(Calendar.MINUTE,dailyPlan.getToMinute());
                Toast.makeText(getActivity(), "daily " + actualPlan + " aktu" + sdf.format(getCurrentTime()) + " " + sdf.format(from.getTimeInMillis()), Toast.LENGTH_LONG).show();
                actualPlan = 1;
                actualPlanTextView.setText(R.string.celodenny);
                return habitMainRepository.getDailyPlanHabits();
            } else {
                actualPlan = 0;
                actualPlanTextView.setText("Je to true, ale nic" + sdf.format(getCurrentTime()) + " " + sdf.format(planMainRepository.getByType(1).getToTime().getTime()));
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
        setPlan();
        dailyPlan = planMainRepository.getByType(1);
        morningPlan = planMainRepository.getByType(2);
        lunchPlan = planMainRepository.getByType(3);
        eveningPlan = planMainRepository.getByType(4);
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

    private long getTodayBeginning(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.HOUR_OF_DAY,0);
        return cal.getTimeInMillis();
    }

    private long getTodayEnd(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.HOUR_OF_DAY,23);
        return cal.getTimeInMillis();
    }


    private Cursor getExamResults(){
        return database.rawQuery("SELECT e.*, s.name, s.shortcut FROM exam e left join subject s on e.subject_id = s._id where e.date > ? AND e.date < ?",new String[]{String.valueOf(getCurrentTime()), String.valueOf(getTodayEnd())});
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

        dailyPlan = new Plan(dailyFromTime, dailyToTime, 1, true, 8, 20, 0, 0, 3600000 );
        planMainRepository.insert(dailyPlan);

        dailyCalendar.set(Calendar.HOUR_OF_DAY,8);
        long morningFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY,13);
        long morningToTime = dailyCalendar.getTimeInMillis();
        morningPlan = new Plan(morningFromTime, morningToTime, 2, true, 8, 12, 0, 15, 1500000 );
        planMainRepository.insert(morningPlan);

        dailyCalendar.set(Calendar.HOUR_OF_DAY,12);
        long lunchFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY,17);
        long lunchToTime = dailyCalendar.getTimeInMillis();
        lunchPlan = new Plan(lunchFromTime, lunchToTime, 3, true, 12, 17, 0, 0,1500000);
        planMainRepository.insert(lunchPlan);

        dailyCalendar.set(Calendar.HOUR_OF_DAY,17);
        long eveningFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY,22);
        long eveningToTime = dailyCalendar.getTimeInMillis();
        eveningPlan = new Plan(eveningFromTime, eveningToTime,4, true, 17, 22, 0, 0, 1500000);
        planMainRepository.insert(eveningPlan);
    }

    private void updateSetNotification(long idPlan){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOTIFICATION",true);
        planMainRepository.update2(idPlan,contentValues);
    }


    /*private void setDailyPlan2(){
        Plan dailyPlan = planMainRepository.getByType(1);
        if (getCurrentTime() < dailyPlan.getFromTime().getTime() || getCurrentTime() == dailyPlan.getFromTime().getTime()) {
            setHabitNotification(dailyPlan.getFromTime().getTime(), 1);
        } else if (getCurrentTime() < dailyPlan.getToTime().getTime() || getCurrentTime() == dailyPlan.getToTime().getTime()) {
            setHabitNotification(getCurrentTime(), 1);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dailyPlan.getFromTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            setHabitNotification(cal.getTimeInMillis(), 1);
        }
    }*/

    private void setDailyPlan(){
        Plan dailyPlan = planMainRepository.getByType(1);
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.set(Calendar.HOUR_OF_DAY, dailyPlan.getFromHour());
        calendarFrom.set(Calendar.MINUTE,dailyPlan.getFromMinute());
        Calendar calendarTo = Calendar.getInstance();
        calendarTo.set(Calendar.HOUR_OF_DAY,dailyPlan.getToHour());
        calendarFrom.set(Calendar.MINUTE,dailyPlan.getToMinute());
        if (System.currentTimeMillis() < calendarFrom.getTimeInMillis()){
            setHabitNotification(calendarFrom.getTimeInMillis(),1, calendarTo.getTimeInMillis());
        } else if (System.currentTimeMillis() < calendarTo.getTimeInMillis()){
            setHabitNotification(System.currentTimeMillis(), 1,calendarTo.getTimeInMillis());
        } else {
            calendarFrom.add(Calendar.DAY_OF_MONTH,1);
            calendarTo.add(Calendar.DAY_OF_MONTH,1);
            setHabitNotification(calendarFrom.getTimeInMillis(),1, calendarTo.getTimeInMillis());
        }
    }


    @Override
    public void fragmentSwitchToVisible() {
        loadListView();
    }
}
