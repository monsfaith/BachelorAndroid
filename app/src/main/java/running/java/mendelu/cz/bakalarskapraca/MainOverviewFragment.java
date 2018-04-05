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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.ExamAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Habit;
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
public class MainOverviewFragment extends Fragment{

    private Button butt;
    private Button buttShow;
    private FloatingActionButton floatingButton;
    private ProgressBar progressBarReview;

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
    private TextView textDate;


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
        //cardViewTwo = (CardView) view.findViewById(R.id.card_view_two);
        mainOpenHelper = new MainOpenHelper(getActivity());
        database = mainOpenHelper.getWritableDatabase();
        planMainRepository = new PlanMainRepository(getActivity());
        habitMainRepository = new HabitMainRepository(getActivity());
        examMainRepository = new ExamMainRepository(getActivity());
        actualPlanTextView = (TextView) view.findViewById(R.id.actualPlanTextView);
        progressBarReview = (ProgressBar) view.findViewById(R.id.progressBarReview);
        textDate = (TextView) view.findViewById(R.id.textDateToday);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        textDate.setText(sdf.format(System.currentTimeMillis()));

        //planMainRepository.deleteAllPlans();

        if (planMainRepository.getAllPlans().size() != 4) {
            init();
            createMorningHabits();
            //setDatabasePlanTimeDaily();

            //DAVAM len docasne prec
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

        //setExamNotification();
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

        return view;
    }


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

    //zakomentovane veci, musim si to objasnit
    private void setExamNotification(){
        Calendar calendar = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
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
            actualPlanTextView.setText("Plán neprebieha");
            /*TextView tx = new TextView(getActivity());
            tx.setText("Nie je momentálne zvolený žiadny plán. ");
            tx.setPadding(50, 210, 0, 50);
            tx.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            cardViewTwo.addView(tx);*/
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
                //Toast.makeText(getActivity(), "morning " + actualPlan + " aktu", Toast.LENGTH_LONG).show();
                progressBarReview.setMax(habitMainRepository.getMorningPlanHabits().size());
                progressBarReview.setProgress(habitMainRepository.getDoneMorningPlanHabits());
                actualPlan = 2;
                actualPlanTextView.setText(R.string.ranny);
                return habitMainRepository.getMorningPlanHabits();

            } else if ((setPlanDateToCalendar(lunchPlan.getType()) > lunchPlan.getFromTime().getTime() || setPlanDateToCalendar(lunchPlan.getType()) == lunchPlan.getFromTime().getTime()) && (setPlanDateToCalendar(lunchPlan.getType()) < lunchPlan.getToTime().getTime())){
                from.set(Calendar.HOUR_OF_DAY,lunchPlan.getFromHour());
                from.set(Calendar.MINUTE,lunchPlan.getFromMinute());
                to.set(Calendar.HOUR_OF_DAY,lunchPlan.getToHour());
                to.set(Calendar.MINUTE,lunchPlan.getToMinute());
                //Toast.makeText(getActivity(), "lunch " + actualPlan + " aktu", Toast.LENGTH_LONG).show();
                actualPlan = 3;
                actualPlanTextView.setText(R.string.obedny);
                progressBarReview.setMax(habitMainRepository.getLunchPlanHabits().size());
                progressBarReview.setProgress(habitMainRepository.getDoneLunchPlanHabits());
                return habitMainRepository.getLunchPlanHabits();

            } else if ((setPlanDateToCalendar(eveningPlan.getType()) > eveningPlan.getFromTime().getTime() || setPlanDateToCalendar(eveningPlan.getType()) == eveningPlan.getFromTime().getTime()) && (setPlanDateToCalendar(eveningPlan.getType()) < eveningPlan.getToTime().getTime() || setPlanDateToCalendar(eveningPlan.getType()) == eveningPlan.getToTime().getTime())){
                from.set(Calendar.HOUR_OF_DAY,eveningPlan.getFromHour());
                from.set(Calendar.MINUTE,eveningPlan.getFromMinute());
                to.set(Calendar.HOUR_OF_DAY,eveningPlan.getToHour());
                to.set(Calendar.MINUTE,eveningPlan.getToMinute());

                //Toast.makeText(getActivity(), "evening " + actualPlan + " aktu " + sdf.format(setPlanDateToCalendar(eveningPlan.getType())) + " " + sdf.format(from.getTimeInMillis()), Toast.LENGTH_LONG).show();
                actualPlan = 4;
                actualPlanTextView.setText(R.string.vecerny);
                progressBarReview.setMax(habitMainRepository.getEveningPlanHabits().size());
                progressBarReview.setProgress(habitMainRepository.getDoneEveningPlanHabits());
                return habitMainRepository.getEveningPlanHabits();
            } else {
                actualPlan = 0;
                actualPlanTextView.setText("Neprebieha plán");
                //Toast.makeText(getActivity(), "nic" + sdf.format(setPlanDateToCalendar(dailyPlan.getType())) + "", Toast.LENGTH_LONG).show();
                return null;
            }
        } else if ((setPlanDateToCalendar(dailyPlan.getType()) > dailyPlan.getFromTime().getTime() || setPlanDateToCalendar(dailyPlan.getType()) == dailyPlan.getFromTime().getTime()) && (setPlanDateToCalendar(dailyPlan.getType()) < dailyPlan.getToTime().getTime() || setPlanDateToCalendar(dailyPlan.getType()) == dailyPlan.getToTime().getTime())){
            from.set(Calendar.HOUR_OF_DAY,dailyPlan.getFromHour());
            from.set(Calendar.MINUTE,dailyPlan.getFromMinute());
            to.set(Calendar.HOUR_OF_DAY,dailyPlan.getToHour());
            to.set(Calendar.MINUTE,dailyPlan.getToMinute());
                //Toast.makeText(getActivity(), "daily " + actualPlan + " aktu" + sdf.format(getCurrentTime()) + " " + sdf.format(from.getTimeInMillis()), Toast.LENGTH_LONG).show();
                actualPlan = 1;
                actualPlanTextView.setText(R.string.celodenny);
            progressBarReview.setMax(habitMainRepository.getDailyPlanHabits().size());
            progressBarReview.setProgress(habitMainRepository.getDoneDailyPlanHabits());
                return habitMainRepository.getDailyPlanHabits();
            } else {
                actualPlan = 0;
                actualPlanTextView.setText("Neprebieha plán");
                //Toast.makeText(getActivity(), "nic " + sdf.format(setPlanDateToCalendar(dailyPlan.getType())) + sdf.format(dailyPlan.getFromTime().getTime()) + " " + sdf.format(dailyPlan.getToTime().getTime()), Toast.LENGTH_LONG).show();
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
        //setPlan();
        dailyPlan = planMainRepository.getByType(1);
        morningPlan = planMainRepository.getByType(2);
        lunchPlan = planMainRepository.getByType(3);
        eveningPlan = planMainRepository.getByType(4);
        }


    private long getTodayEnd(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.HOUR_OF_DAY,23);
        return cal.getTimeInMillis();
    }


    private Cursor getExamResults(){
        return database.rawQuery("SELECT e.*, s.name, s.shortcut FROM exam e left join subject s on e.subject_id = s._id where e.date > ? AND e.date < ?",new String[]{String.valueOf(System.currentTimeMillis()), String.valueOf(getTodayEnd())});
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
        dailyCalendar.set(Calendar.HOUR_OF_DAY,12);
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

    private void createMorningHabits(){
        Habit believe = new Habit("Veriť si", "Je dôležité, aby si sa hneď zrána uistil, že zvládneš povinnosti, ktoré ťa cez deň čakajú. Povedz si, že to zvládneš. ", "believe", 2);
        habitMainRepository.insert(believe);
        Habit affection = new Habit("Prejaviť náklonnosť", "Ak s niekým bývaš, je dôležité prejaviť svoje city hneď z rána. Napríklad túlenie uvoľňuje veľa serotonínu, ktorý ťa dostatočne nabudí do nového dňa. ","love",2 );
        habitMainRepository.insert(affection);
        Habit tidyUp = new Habit("Upratať stôl", "Pokiaľ si si nestihol upratať svoj stôl alebo pracovné prostredie včera večer, urob tak teraz. Je lepšie tomu obetovať čas ráno než v priebehu dňa, nakoľko tvoj rozhodovací proces je už neskôr viac vyťažený.", "cleandesk",2);
        habitMainRepository.insert(tidyUp);
        Habit morningGratefulness = new Habit("Byť vďačný", "Keď naplníš svoju myseľ vďačnosťou a pozitívnosťou, chemické látky v mozgu ti umožnia, aby sa z týchto pocitov stal tvoj návyk. Vyhraď si čas na definovanie vecí, za ktoré si vďačný. Či už tvoja rodina, zdravie, tie nohavice, ktoré tak rád nosíš. Dôvodov je veľa, stačí ich len nájsť. Skús nájsť aspoň 3 ", "pray",2);
        habitMainRepository.insert(morningGratefulness);
        Habit morningShower = new Habit("Ranná sprcha", "Studená sprcha ťa osvieži, prebudí a naštartuje tvoj metabolizmus správnym smerom. Studená sprcha rovnako podnecuje i chudnutie, zvyšuje ostražitosť, eliminuje stres a vytvára pocit pevnej vôle.", "coldshower", 2);
        habitMainRepository.insert(morningShower);
        Habit makeYourBed = new Habit("Ustlať posteľ", "Zaberie ti to menej než minútu, a zrána hneď splníš prvú úlohu dňa. Dodá ti to drobný pocit hrdosti, nabudí ťa to do ďalších povinností. Ustlaná posteľ pomáha dotvárať duševnú disciplínu. A ak tvoj deň nebol podľa predstáv, posledná skúsenosť tvojho dňa bude práve to niečo, čo si zvládol.", "bed", 2);
        habitMainRepository.insert(makeYourBed);

        Habit visualization = new Habit("Vizualizácia", "Predstav si, ako bude vyzerať tvoj deň, čo más všetko na pláne. Stačia to byť veci, ktoré potrebuješ spraviť, nemusí to byť detailný plán. Ak si totiž mozog dokáže niečo predstaviť, vizualizovať, samotná realizácia je jednoduchšia.", "vision", 2);
        habitMainRepository.insert(visualization);

        Habit breakfast = new Habit("Raňajky", "Najdôležitejšie jedlo dňa. Energia, ktorú získať naraňajkovaním sa je energia, ktorú si nesieš počas dňa. Snaž sa, aby boli tvoje raňajky zdravé. ", "breakfast",2);
        habitMainRepository.insert(breakfast);

        Habit timeForMe = new Habit("Chvíľa pre seba", "Často je nemožné nájsť si čas počas dňa, kedy nerobíš absolútne nič. Preto je najlepšou možnosťou ráno. Obetuj tomuto času 10-15 min, uži si ráno, slnko, vypočuj si obľúbenú pieseň. Budeš zrelaxovaný pred odštartovaním dňa a povinností.", "stones",2);
        habitMainRepository.insert(timeForMe);
    }

}
