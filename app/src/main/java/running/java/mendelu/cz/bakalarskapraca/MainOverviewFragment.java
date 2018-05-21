package running.java.mendelu.cz.bakalarskapraca;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.services.concurrency.Task;
import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Habit;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.IconHabitAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.MainOpenHelper;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanHabitAssociation;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Project;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelEveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.DatabaseRecordReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.DatabaseTimeChangeReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.ExamNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.SleepReceiver;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


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
    private TextView noExamsToday;
    private SubjectMainRepository subjectMainRepository;
    private ImageButton buttonApp;
    private TextView altbutton;

    private Button examsButtonClick;
    private Button plansButtonClick;

    private static final String SHOWCASE_ID = "7";

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
        progressBarReview = (ProgressBar) view.findViewById(R.id.progressBarReview);
        textDate = (TextView) view.findViewById(R.id.textDateToday);
        noExamsToday = (TextView) view.findViewById(R.id.noExamsToday);
        subjectMainRepository = new SubjectMainRepository(getActivity());
        buttonApp = (ImageButton) view.findViewById(R.id.buttonApp);
        altbutton = (TextView) view.findViewById(R.id.showButtonAlternative);
        examsButtonClick = (Button) view.findViewById(R.id.examsButtonClick);
        plansButtonClick = (Button) view.findViewById(R.id.plansButtonClick);


        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        textDate.setText(sdf.format(System.currentTimeMillis()));

        Log.i("Bakalarka", "onCreate");
        Project project = new Project(8,0,"",1);

        ContentValues contentValues = new ContentValues();

        ShowcaseConfig config = new ShowcaseConfig();
        //config.setDelay(300); // half second between each showcase view
        //config.setDismissTextColor(getResources().getColor(R.color.yellow_700));
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);
        sequence.setConfig(config);
        sequence.addSequenceItem(create(floatingButton,"Toto tlačidlo slúži na vytvorenie skúšky. Na prípravu ťa aplikácia upozorní."));
        sequence.addSequenceItem(create(examsButtonClick,"Kliknutím na toto tlačidlo sa ti zobrazí prehľad skúšok."));
        sequence.addSequenceItem(create(plansButtonClick,"Týmto tlačidlom sa dostaneš k informáciám a nastaveniam tvojich plánov."));
        sequence.start();


        /*sequence.addSequenceItem(floatingButton,
                "Toto tlačidlo slúži na vytvorenie novej skúšky (klikni na Rozumiem).", "Rozumiem!");
        sequence.addSequenceItem(examsButtonClick,
                "Kliknutím na toto tlačidlo sa ti zobrazí prehľad skúšok.", "Rozumiem!");
        sequence.addSequenceItem(plansButtonClick,
                "Týmto tlačidlom sa dostaneš k informáciám a nastaveniam tvojich plánov.", "Rozumiem!");
        sequence.start();*/

        if (planMainRepository.getAllPlans().size() != 4) {
            init();
            subjectMainRepository.insertProject(project);
            createMorningHabits();
            createLunchHabits();
            createEveningHabits();
            createOtherHabits();
            //setDatabasePlanTimeDaily();

            setExamNotification();
            setDatabaseNotification();
            setDailyPlan();
        } else {
            dailyPlan = planMainRepository.getByType(1);
            morningPlan = planMainRepository.getByType(2);
            lunchPlan = planMainRepository.getByType(3);
            eveningPlan = planMainRepository.getByType(4);
        }

        if (examMainRepository.getExamResultsList(System.currentTimeMillis()).size() == 0){
            noExamsToday.setVisibility(View.VISIBLE);
        }


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int time = settings.getInt("edit", 0);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time*60000);
        cal.add(Calendar.HOUR_OF_DAY,-1);

        //subjectMainRepository.insertProject(project);
        if (cal.get(Calendar.HOUR_OF_DAY) != subjectMainRepository.getProjectById(1).getHour()){
            if (cal.get(Calendar.HOUR_OF_DAY) != 0) {
                contentValues.put("hour", cal.get(Calendar.HOUR_OF_DAY));
                subjectMainRepository.updateProject(1, contentValues);
            }
        }

        if (cal.get(Calendar.MINUTE) != subjectMainRepository.getProjectById(1).getMinute()){
            if (cal.get(Calendar.MINUTE) != 0) {
                contentValues.put("minute", cal.get(Calendar.MINUTE));
                subjectMainRepository.updateProject(1, contentValues);
            }
        }

        setExamNotification();
        setDatabaseNotification();
        setSleepNotification();
        loadListView();

        recyclerViewHabits.setNestedScrollingEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TasksDetailActivity.class);
                startActivity(i);
            }
        });

        buttShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyPlanTabsActivity.class);
                startActivity(i);
            }
        });

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CreateExamActivity.class);
                startActivity(i);
            }
        });

        buttonApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),IntroActivity.class);
                startActivity(i);
            }
        });

        examsButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TasksDetailActivity.class);
                startActivity(i);
            }
        });

        plansButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyPlanTabsActivity.class);
                startActivity(i);
            }
        });

        sharedPreferencesControl();


        return view;
    }


    //nastavenie notifikacie na upozornovanie aktivit
    private void setHabitNotification(long timeFrom, int idPlan, long timeTo){



        AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
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

            //po upraveni settings
            calendar.set(Calendar.MINUTE,subjectMainRepository.getProjectById(1).getMinute());
            calendar.set(Calendar.HOUR_OF_DAY,subjectMainRepository.getProjectById(1).getHour());
            if (System.currentTimeMillis() > calendar.getTimeInMillis()){
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }

            //Toast.makeText(getActivity(), "Exam Notification nastavena od " + sdf.format(calendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), ExamNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        //setDailyPlan();

        }


    private void setDatabaseNotification(){
        Intent i = new Intent(getActivity().getApplicationContext(), DatabaseRecordReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 700, i, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE,00);
        calendar.set(Calendar.SECOND,15);
        long time = calendar.getTimeInMillis();

        if (System.currentTimeMillis() > calendar.getTimeInMillis()){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            time = calendar.getTimeInMillis();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        //Toast.makeText(getActivity(), "Database Record update o" + sdf.format(calendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,pendingIntent);
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);
        }


    }

    private void setSleepNotification(){
        if (examMainRepository.getClosestExam() != null) {
            Intent i = new Intent(getActivity().getApplicationContext(), SleepReceiver.class);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            Calendar threeHours = Calendar.getInstance();

            long date = examMainRepository.getClosestExam().getDate().getTime();
            long id = examMainRepository.getClosestExam().getId();
            i.putExtra("examSleepID",id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 50, i, PendingIntent.FLAG_UPDATE_CURRENT);

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            calendar.setTimeInMillis(date);
            calendar.add(Calendar.HOUR_OF_DAY, -8);

            threeHours.setTimeInMillis(date);
            threeHours.set(Calendar.HOUR_OF_DAY, 2);
            threeHours.set(Calendar.MINUTE, 0);

            if (calendar.before(threeHours)) {
                Log.d("Bakalarka",sdf.format(threeHours.getTimeInMillis()));
                    Log.d("Bakalarka","manazer");

                    if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    }
                }
        }

    }

    public void loadListView(){
        //examAdapter = new ExamAdapter(getActivity(), getExamResults());
        examAdapter = new ExamAdapter(getActivity(), getExamResults(System.currentTimeMillis()));
        recyclerView.setAdapter(examAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<PlanHabitAssociation> pha = setPlan();

        if (pha != null){
            iconHabitAdapter = new IconHabitAdapter(getActivity(), pha, this);
            recyclerViewHabits.setAdapter(iconHabitAdapter);
            recyclerViewHabits.setLayoutManager(new GridLayoutManager(getActivity(),4));
        } else {
            actualPlanTextView.setText("Plán neprebieha");
            recyclerViewHabits.setAdapter(null);
            progressBarReview.setProgress(0);
            
        }




    }

    private List<Exam> getExamResults(long date){
        return examMainRepository.getExamResultsList(date);
    }

    private MaterialShowcaseView create(View view, String content){
        MaterialShowcaseView.Builder builder = new MaterialShowcaseView.Builder(getActivity())
                .setTarget(view)
                .setDismissText("Rozumiem!")
                .setDismissTextColor(getResources().getColor(R.color.yellow_700))
                //.setMaskColour(Color.argb(195, 0, 0, 0))
                .setContentText(content)
                .setDelay(300)
                .setDismissOnTouch(true);

        MaterialShowcaseView showcaseView = builder.build();
        return showcaseView;
    }


    //urcenie View na zaklade prebiehajuceho planu
    private List<PlanHabitAssociation> setPlan() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        Calendar eveningCalFrom = Calendar.getInstance();
        Calendar eveningCalTo = Calendar.getInstance();

        eveningCalFrom.set(Calendar.HOUR_OF_DAY,eveningPlan.getFromHour());
        eveningCalFrom.set(Calendar.MINUTE,eveningPlan.getFromMinute());
        eveningCalTo.set(Calendar.HOUR_OF_DAY,eveningPlan.getToHour());
        eveningCalTo.set(Calendar.MINUTE,eveningPlan.getToMinute());

        //tento if povodne nebol
        //tooto som zakomentovala 20.05.2018
        //if (eveningCalFrom.get(Calendar.HOUR_OF_DAY) > eveningCalTo.get(Calendar.HOUR_OF_DAY)){
         //   eveningCalTo.add(Calendar.DATE,1);

        //}

            if (eveningPlan.getFromHour() > eveningPlan.getToHour()) {
                eveningCalTo.add(Calendar.DATE, 1);
                if (System.currentTimeMillis() < eveningCalFrom.getTimeInMillis()){
                    eveningCalFrom.add(Calendar.DATE,-1);
                    eveningCalTo.add(Calendar.DATE,-1);
                }
            }






//        eveningCalTo.add(Calendar.DATE,1);

        long evFrom1 = eveningCalFrom.getTimeInMillis();
        long evTo1 = eveningCalTo.getTimeInMillis();


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
            } else if ((System.currentTimeMillis() > evFrom1) && (System.currentTimeMillis() < evTo1)) {
                from.set(Calendar.HOUR_OF_DAY,eveningPlan.getFromHour());
                from.set(Calendar.MINUTE,eveningPlan.getFromMinute());
                to.set(Calendar.HOUR_OF_DAY,eveningPlan.getToHour());
                to.set(Calendar.MINUTE,eveningPlan.getToMinute());

                //Toast.makeText(getActivity(), "evening " + actualPlan + " aktu " + sdf.format(evFrom1) + " " + sdf.format(evTo1), Toast.LENGTH_LONG).show();
                actualPlan = 4;
                actualPlanTextView.setText(R.string.vecerny);
                progressBarReview.setMax(habitMainRepository.getEveningPlanHabits().size());
                progressBarReview.setProgress(habitMainRepository.getDoneEveningPlanHabits());
                return habitMainRepository.getEveningPlanHabits();
            }
            //toto povodne zakomentovane nebolo
            /*else if ((System.currentTimeMillis() > evFrom2) && (System.currentTimeMillis() < evTo2)) {
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
            }*/
            else {
                actualPlan = 0;
                actualPlanTextView.setText("Neprebieha plán");
                //Toast.makeText(getActivity(), "evening " + actualPlan + " aktu " + sdf.format(evFrom1) + " " + sdf.format(evTo1), Toast.LENGTH_LONG).show();
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

    private void sharedPreferencesControl(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            setDailyPlan();
        }
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
        Log.i("Bakalarka","onResume");
        //setPlan();
        dailyPlan = planMainRepository.getByType(1);
        morningPlan = planMainRepository.getByType(2);
        lunchPlan = planMainRepository.getByType(3);
        eveningPlan = planMainRepository.getByType(4);
        loadListView();
        if (examMainRepository.getExamResultsList(System.currentTimeMillis()).size() != 0){
            noExamsToday.setVisibility(View.GONE);
        }
        setSleepNotification();

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

        dailyPlan = new Plan(dailyFromTime, dailyToTime, 1, true, 8, 20, 0, 0, 6000000 );
        planMainRepository.insert(dailyPlan);

        dailyCalendar.set(Calendar.HOUR_OF_DAY,8);
        long morningFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY,11);
        long morningToTime = dailyCalendar.getTimeInMillis();
        morningPlan = new Plan(morningFromTime, morningToTime, 2, true, 8, 11, 0, 0, 3600000 );
        planMainRepository.insert(morningPlan);

        dailyCalendar.set(Calendar.HOUR_OF_DAY,12);
        long lunchFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY,17);
        long lunchToTime = dailyCalendar.getTimeInMillis();
        lunchPlan = new Plan(lunchFromTime, lunchToTime, 3, true, 12, 17, 0, 0,3600000);
        planMainRepository.insert(lunchPlan);

        dailyCalendar.set(Calendar.HOUR_OF_DAY,18);
        long eveningFromTime = dailyCalendar.getTimeInMillis();
        dailyCalendar.set(Calendar.HOUR_OF_DAY,22);
        long eveningToTime = dailyCalendar.getTimeInMillis();
        eveningPlan = new Plan(eveningFromTime, eveningToTime,4, true, 18, 22, 0, 0, 3600000);
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
        Habit believe = new Habit("Veriť si", "Je dôležité, aby si sa hneď zrána uistil, že zvládneš povinnosti, ktoré ťa cez deň čakajú. Povedz si, že to zvládneš. ","Povedz si, že všetko zvládneš." ,"believe", 2);
        long idBelieve = habitMainRepository.insert(believe);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idBelieve,2));

        Habit affection = new Habit("Prejaviť náklonnosť", "Ak s niekým bývaš, je dôležité prejaviť svoje city hneď z rána. Napríklad túlenie uvoľňuje veľa serotonínu, ktorý ťa dostatočne nabudí do nového dňa. ", "Ak s niekým bývaš, je dôležité prejaviť svoje city hneď z rána.", "love",2);
        long idAff = habitMainRepository.insert(affection);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idAff,2));


        Habit tidyUp = new Habit("Upratať stôl", "Pokiaľ si si nestihol upratať svoj stôl alebo pracovné prostredie včera večer, urob tak teraz. Je lepšie tomu obetovať čas ráno než v priebehu dňa, nakoľko tvoj rozhodovací proces je už neskôr viac vyťažený.","V priebehu dňa bude tvoj rozhodovací proces viac vyťažený.", "cleandesk",2);
        long idTidy = habitMainRepository.insert(tidyUp);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idTidy,2));


        Habit morningGratefulness = new Habit("Byť vďačný", "Keď naplníš svoju myseľ vďačnosťou a pozitívnosťou, chemické látky v mozgu ti umožnia, aby sa z týchto pocitov stal tvoj návyk. Vyhraď si čas na definovanie vecí, za ktoré si vďačný. Či už tvoja rodina, zdravie, tie nohavice, ktoré tak rád nosíš. Dôvodov je veľa, stačí ich len nájsť. Skús nájsť aspoň 3.", "Dôvodov je veľa, stačí ich len nájsť.", "pray",2);
        long idGrate = habitMainRepository.insert(morningGratefulness);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idGrate,2));


        Habit morningShower = new Habit("Studená sprcha", "Studená sprcha ťa osvieži, prebudí a naštartuje tvoj metabolizmus správnym smerom. Studená sprcha rovnako podnecuje i chudnutie, zvyšuje ostražitosť, eliminuje stres a vytvára pocit pevnej vôle.","Sprcha ťa osvieži, prebudí a zredukuje stres.","coldshower", 2);
        long idShower = habitMainRepository.insert(morningShower);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idShower,2));


        Habit makeYourBed = new Habit("Ustlať posteľ", "Zaberie ti to menej než minútu, a zrána hneď splníš prvú úlohu dňa. Dodá ti to drobný pocit hrdosti, nabudí ťa to do ďalších povinností. Ustlaná posteľ pomáha dotvárať duševnú disciplínu. A ak tvoj deň nebol podľa predstáv, posledná skúsenosť tvojho dňa bude práve to niečo, čo si zvládol.", "Zaberie ti to menej než minútu, a zrána hneď splníš prvú úlohu dňa.","bed", 2);
        long idBed = habitMainRepository.insert(makeYourBed);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idBed,2));


        Habit visualization = new Habit("Vizualizácia", "Predstav si, ako bude vyzerať tvoj deň, čo más všetko na pláne. Stačia to byť veci, ktoré potrebuješ spraviť, nemusí to byť detailný plán. Ak si totiž mozog dokáže niečo predstaviť, vizualizovať, samotná realizácia je jednoduchšia.", "Ak si mozog dokáže niečo predstaviť, vizualizovať, samotná realizácia je jednoduchšia.","vision", 2);
        long idVis = habitMainRepository.insert(visualization);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idVis,2));


        Habit breakfast = new Habit("Raňajky", "Najdôležitejšie jedlo dňa. Energia, ktorú získaš naraňajkovaním sa je energia, ktorú si nesieš počas dňa. Snaž sa, aby boli tvoje raňajky zdravé. ", "Energia, ktorú získaš raňajkami, je energia, ktorú si nesieš počas dňa.", "breakfast",2);
        long idBreak = habitMainRepository.insert(breakfast);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idBreak,2));


        Habit timeForMe = new Habit("Chvíľa pre seba", "Často je nemožné nájsť si čas počas dňa, kedy nerobíš absolútne nič. Preto je najlepšou možnosťou ráno. Obetuj tomuto času 10-15 min, uži si ráno, slnko, vypočuj si obľúbenú pieseň. Budeš zrelaxovaný pred odštartovaním dňa a povinností.", "Budeš zrelaxovaný pred odštartovaním dňa a povinností.","stones",2);
        long idTime = habitMainRepository.insert(timeForMe);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idTime,2));

        Habit morningWater = new Habit("Ranný pohár vody", "Po prebudení sa odporúča vypiť aspoň dva poháre čistej vlažnej vody. Pitie vody vyplavuje nepotrebné toxíny von z tela, naštartuje metabolizmus, brzdí apetít, čo vedie k vhodnejšiemu výberu jedla na raňajky. Taktiež má priaznivý účinok na jasnejšie a bystrejšie vnímanie a sviežu myseľ v priebehu dňa.", "Po zobudení sa ako prvé napi vody. Budeš mať jasnejšie a bystrejšie vnímanie.","bottlebig",2);
        long idWaterMorning = habitMainRepository.insert(morningWater);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idWaterMorning,2));

    }

    private void createLunchHabits(){

        Habit lunch = new Habit("Ľahký obed", "Skús si cez obed pochutnať na ovocí, zelenine či rybách, vyhľadávaj jedlo s vysokou nutričnou hodnotou. Ľahký obed a vysoko nutričné jedlá sa odporúčajú práve pre účinné fungovanie mozgu. Obed je vhodnou dobou i na kúsok tmavej čokolády. Ťažkým obedom si ľahko vyčerpáš energiu.", "Vyhľadávaj vysoko nutričné jedlá. Ťažkým obedom si vyčerpáš energiu.", "lunch", 3);
        long idLunch = habitMainRepository.insert(lunch);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idLunch,3));

        Habit change = new Habit("Zmena prostredia", "Cieľom zmeny doterajšieho prostredia je udržanie a navýšenie sústredenosti. Ak totiž organizmus pracuje bez dlhšej prestávky, jeho psychologické a energické zdroje sa vyčerpávajú, a tým pádom je náročnejšie sa sústrediť a pracovať efektívne. ", "Skús sa čo i len nachvíľu odtrhnúť od doterajšieho prostredia a uvidíš zmenu vo svojom sústredení. ", "shuffle",3);
        long idChange = habitMainRepository.insert(change);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idChange,3));

        Habit trainingOneTask = new Habit("Robiť jednu vec naraz", "Skús sa počas obeda sústrediť na to, aby si vždy vykonával iba jednu vec v danom čase. Multitasking v takomto prípade nenapomáha študijnej efektívnosti. Ak sa pri jedení obedu sústredíš len na to, postupne tento návyk prenesieš i do ďalších oblastí.", "Multitasking nenapomáha študijnej efektívnosti. Trénuj sústredenie vždy len na jednu vec.", "concentration",3);
        long idTraining = habitMainRepository.insert(trainingOneTask);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idTraining,3));

        Habit nap = new Habit("Šlofík", "Krátky spánok v poludňajších hodinách dokáže podľa výskumov robiť zázraky. Má vplyv rovnako na tvoje zdravie, ako aj na tvoju koncentráciu a zníženie úrovne stresu. Odporúčaná doba spánku je 26 minút.", "Krátky spánok má pozitívny vplyv na zdravie, koncentráciu, znižovanie úrovne stresu.", "nap", 3);
        long idNap = habitMainRepository.insert(nap);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idNap,3));

        Habit joyYourself = new Habit("Urobiť si radosť", "Práve stred dňa je vhodným okamihom na realizáciu činností, ktoré ti robia radosť. Tento čas je totiž zdrojom energie pre ďalšie úlohy a činnosti, ktoré ťa v nasledujúcej časti dňa čakajú, preto sa považuje za významný v oblasti obnovenia sústredenia a energie. Či už je to stretnutie priateľov alebo prečítanie obľúbenej knižky.","Tento čas je totiž zdrojom energie pre ďalšie úlohy a činnosti, ktoré ťa v nasledujúcej časti dňa čakajú.","smiling",3);
        long idJoy = habitMainRepository.insert(joyYourself);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idJoy, 3));


    }

    private void createEveningHabits(){
        Habit lookingForward = new Habit("Teším sa na...", "Shawn Actor vo svojej knihe uviedol, že v minulosti bol uskutočnený výskum, v ktorom boli ľudia už len pri pomyslení na pozeranie svojho obľúbeného filmu nadšení, a navyše im tento pocit zvýšil hladinu endorfínov o 27 %. ", "Často je tou najkrajšou časťou súvisiacou s plánovanou činnosťou jej očakávanie. ", "happy",4);
        long idLooking = habitMainRepository.insert(lookingForward);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idLooking,4));

        Habit blueLight = new Habit("Vypnúť modré svetlo", "Používanie smartfónu pred večerným spánkom po dobu 10 minút je porovnateľné s hodinovou prechádzkou na ostrom slnku, preto sa v prípade používania telefónu v neskorých večerných hodinách ľuďom veľmi ťažko zaspáva.", "Skús obmedziť zariadenia vyžarujúce modré svetlo alebo na nich zapni špeciálny filter.","bluelight",4);
        long idLight = habitMainRepository.insert(blueLight);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idLight,4));

        Habit happiness = new Habit("Dnešné 3 šťastia", "Je dôležité si ku koncu dňa nájsť a spísať veci, ktoré ťa urobili šťastným. Ak máš i ťažký deň, vždy to môžu byť i maličkosti. Pozitívne psychologické cvičenia majú tendenciu robiť ľudí šťastnejšími a spokojnejšími. Zober pero a papier a objav 3 veci, ktoré ťa dnes urobili šťastným, a prečo.", "Spíš si 3 veci, ktoré ťa dnes urobili šťastným a prečo.", "writedown",4);
        long idHappy = habitMainRepository.insert(happiness);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idHappy,4));

        Habit forgive = new Habit("Odpustiť", "V prípade prechovávania hnevu voči určitým okolnostiam či ľuďom je dobré ku záveru dňa drobné omyly a chyby odpustiť, čo bude viesť i k pokojnejšiemu spánku, a postupne i ku kľudnejšiemu reagovaniu na rôzne situácie."," Ak prechovávaš hnev, skús odpustiť. Budeš mať čistejšiu myseľ a kľudný spánok.", "hand",4);
        long idForgive = habitMainRepository.insert(forgive);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idForgive,4));

        Habit sleep = new Habit("Príprava na spánok", "Americká organizácia National Sleep Foundation vo výskume zaoberajúcom sa odporúčanou dĺžkou spánku zistila, že pre mladých dospelých je najvhodnejšia doba trvania spánku 7 - 8 hodín, pričom nedostatok spánku má negatívny vplyv na imunitu, pamäť, premýšľanie či koncentráciu. Odporúča sa vytvoriť si príjemné prostredie na spánok vopred.", "Odporúčaná doba spánku je 7 - 8 hodín. Ak to nie je možné, magnézium a vitamín D3 umožnia ľahšie vstávanie.", "sleep", 4);
        long idSleep = habitMainRepository.insert(sleep);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idSleep,4));

        Habit toDo = new Habit("Zoznam povinností", "Pre efektívne zvládnutie povinnosti nasledujúci deň sa odporúča spísať si 5 najdôležitejších činností, najlepšie na papier.", "Spíš si 5 najdôležitejší činností, ktoré ťa zajtra čakajú.","todo",4);
        long idToDo = habitMainRepository.insert(toDo);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idToDo,4));

    }

    private void createOtherHabits(){
        Habit bellyBreathing = new Habit("Dýchanie bruchom","Jednu ruku si polož na brucho a druhú na hrudník. Snaž sa svoj dych smerovať na brucho, aby sa tvoja ruka na bruchu pohybovala a ruka na hrudníku zostala bez pohybu. Kontrolované dýchanie pomáha uvoľňovať stres a napätie. ", "Snaž sa sústrediť na svoje dýchanie a vydychovať do brucha. ","lungs",1);
        long idBelly = habitMainRepository.insert(bellyBreathing);

        Habit breathing = new Habit("Dýchanie","Kontrolované dýchanie pomáha uvoľňovať stres a napätie. Pri vydychovaní si skús predstaviť, ako sa tvoje telo zbavuje všetkého negatívneho a stáva sa uvoľneným. Vydychovanie by malo trvať 2x viac ako nádych a zvyčajná dĺžka trvania relaxácie je 2-3 minúty.", "Zbav sa negativity. Nech je tvoj výdych 2x dlhší ako nádych. ", "nose",1);
        long idBreathing = habitMainRepository.insert(breathing);

        Habit muscle = new Habit("Relaxácia svalov","Snaž sa uvoľňovať a napínať jednotlivé svaly a pritom sa na túto činnosť sústreďovať. Začni buď od hlavy až k päte, alebo naopak. Cieľom je uvoľniť nepotrebné napätie v tele. ", "Uvoľni si nepotrebné napätie v tele napínaním a uvoľňovaním jednotlivých svalov.", "lotusposition", 1);
        long idMuscle = habitMainRepository.insert(muscle);

        Habit imagine = new Habit("Predstav si","Vizualizovanie si príjemného miesta alebo situácie rovnako patrí medzi osvedčené meditačné a relaxačné techniky. Súčasťou účinného vykonania je zapojiť čo najviac zmyslov. ", "Predstav si obľúbené miesto alebo situáciu, zapoj čo najviac zmyslov a uvoľni napätie. ", "beach",1);
        long idImagine = habitMainRepository.insert(imagine);

        Habit exercise = new Habit("Cvičenie", "Cvičenie má pozitívny vplyv na zmierňovanie únavy, zlepšovanie ostražitosti a sústredenia, a celkovo na lepšiu funkčnosť kognitívnych funkcií. Cvičením sa uvoľňujú endorfíny, a naopak sa znižuje úroveň stresových hormónov. Nie je nutné cvičiť do úplného vyčerpania, stačí primerane rozhýbať svoje telo", "Rozhýb svoje telo, či už náročnejším workoutom alebo jemným pretiahnutím svalov. Sústreď sa na telo.", "exercise",1);
        long idExercise = habitMainRepository.insert(exercise);

        Habit animal = new Habit("Domáci maznáčik", "Zvieratá pomáhajú ľuďom uvoľňovať bolesť, stres, úzkosť, napätie či únavu. Rôzne štúdie preukázali, že prítomnosť zvieraťa v domácnostiach redukovala úroveň stresu u ľudí, na ktorých vplývali rôzne stresory.","Ak máš zviera, povenuj sa mu krátku chvíľu. ", "dog", 1);
        long idAnimal = habitMainRepository.insert(animal);

        Habit classicalMusic = new Habit("Klasická hudba", "Doktor Kevin Labar sa vyjadril, že klasická hudba dokáže zlepšiť intelektuálny výkon. Jej počúvaním sa v tele vytvára upokojujúci pocit spojený s uvoľňovaním dopamínu a znižovaním úrovne stresových hormónov, čo v konečnom dôsledku vytvára pokojnú náladu a stav mysle.", "Počúvaním klasickej hudby sa zvyšuje aktivita génov súvisiacich s učením, pamäťou či uvoľňovaním dobrých hormónov", "music",1);
        long idMusic = habitMainRepository.insert(classicalMusic);

        Habit humor = new Habit("Humor","Úsmev a smiech sú dobrými životabudičmi a zdrojmi energie. Každý deň si nájdi spôsoby, ako sa dať rozosmiať a považuj to za nutnú súčasť dňa.", "Nájdi si zdroj zábavy, ktorý ťa pobaví a uvoľní. Smiech lieči. ", "laughing",1);
        long idHumor = habitMainRepository.insert(humor);

        Habit coloring = new Habit("Vyfarbovanie", "Vyfarbovanie, či už vo forme antistresových omaľovániek alebo inej formy, má značný vplyv na obmedzenie prítomnosti negatívnych myšlienok a je zdrojom oddychu. Pravidelné vymaľovávanie podporuje kreativitu, logiku a uvoľňuje stres.", "Pravidelné vymaľovávanie podporuje kreativitu, logiku a uvoľňuje stres.", "color",1);
        long idColoring = habitMainRepository.insert(coloring);

        Habit notice = new Habit("Všímavosť", "Byť všímamým, pozorovať okolie i svoje pocity je súčasťou relaxačných techník vedúcich k uvoľňovaniu stresu. Cieľom je zamerať sa na súčasné okamihy namiesto venovania pozornosti strachom o budúcnosť. Účelom je zamerať sa na svoj dych, následne na zvuky, myšlienky, bez ohľadu na to, či sú dobré, alebo zlé. ", "Zameraj na sa na myšlienky, zvuky, dych. Zacieľ svoje sústredenie na prítomný okamih.", "observe", 1);
        long idNotice = habitMainRepository.insert(notice);


        Habit yoga = new Habit("Jóga", "Jóga je skvelý prostriedok na uvoľnenie tela i duše. Pomáha odblokovať stuhnuté svalstvo a dostať sa do duševnej a telesnej rovnováhy.", "Uvoľní tvoje telo i dušu.", "yoga",1);
        long idYoga = habitMainRepository.insert(yoga);

        Habit water = new Habit("Pitný režim","Nedostatok prijímanej vody môže mať negatívny vplyv na funkčnosť poznávacích funkcií, ako sú koncentrácia, ostražitosť a problémy s pamäťou. Voda takisto pozitívne vplýva na elimináciu nervozity alebo úzkosti, a jej pravidelným pitím upokojuje myseľ. Je dôležité vypiť cez deň aspoň 2 litre vody.", "Snaž sa mať pohár alebo fľašu s vodou vždy blízko pri sebe.", "drop",1);
        long idWater = habitMainRepository.insert(water);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idWater,1));


        Habit distractions = new Habit("Nájdi ticho", "Pri relaxácii je potrebné nájsť si tiché a kľudné prostredie, pri ktorom sa stačí iba rozjímať. Snaž sa eliminovať všetky prvky, ktoré ťa rozrušujú a uži si ticho.", "Eliminuj všetky rozrušenia a uži si ticho.","silence",1);
        long idDistr = habitMainRepository.insert(distractions);

        Habit reward = new Habit("Odmeň sa","Vykonal si niečo, na čo si pyšný? Ak je to tá dlho odkladaná aktivita, tá náročná úloha, odmeň sa tým, čo ti urobí najväčšiu radosť. Zaslúžiš si to!","Zaslúžiš si to! Odmeň sa za čokoľvek, z čoho sa tešíš, že si vykonal/a.", "reward", 1);
        long idReward = habitMainRepository.insert(reward);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idReward,1));


        Habit friends = new Habit("Rodina a priatelia", "Stretnutie s blízkymi osobami redukuje uvoľňovanie stresového hormónu v ťažkých situáciách v porovnaní so situáciami, keď blízky priateľ nebol prítomný.", "Venuj čas blízkej osobe.", "teamwork",1);
        long idFriends = habitMainRepository.insert(friends);

        Habit walking = new Habit("Prechádzka", "Štúdie na Stanfordskej univerzite preukázali, že prechádzanie sa zvyšuje kreativitu. Nie je dôležité prostredie, ale samotný akt prechádzania sa. Ak je to však možné, pokús sa stráviť krátky čas i prechádzkou na čerstvom vzduchu. Príroda má značný vplyv na zníženie hladiny stresu. ","Prechádzka ti obnoví aktivitu mozgu. ", "walking", 1);
        long idWalking = habitMainRepository.insert(walking);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idWalking,1));


        Habit nature = new Habit("Preskúmaj prírodu","Prírodné zvuky a prostredie sú zdrojom ukľudňujúcich pocitov. Prechádzka po lese, parku, pri rieke ti obnoví aktivitu mozgu, sústredenie a budeš zregenerovaný.", "Príroda je zdrojom kreativity a pokojnej mysle.","nature",1);
        long idNature = habitMainRepository.insert(nature);

        Habit call = new Habit("Zavolať blízkej osobe", "Niekedy nie je možné sa stretnúť s osobou, ktorú máš rád. Často i jej/jeho hlas v telefóne je zdrojom radostných pocitov a uvoľnenia. Zavolaj niekomu, koho by si rád počul.", "Zavolaj niekomu, koho chceš počuť.", "phone",1);
        long idCall = habitMainRepository.insert(call);

        Habit reading = new Habit("Čítanie","Na zníženie hladiny stresu má významný účinok i čítanie kníh, ktoré navyše pomáhajú zlepšovať predstavivosť, napomáhajú k lepšiemu a kvalitnejšiemu spánku, a rovnako čítanie ukázalo priaznivý vplyv na predchádzanie problémov s výskytom Alzheimerovej choroby.", "Prečítaj si časť zo svojej obľúbenej knižky.", "book128",1);
        long idReading = habitMainRepository.insert(reading);

        Habit inspired = new Habit("Inšpiruj sa", "Často sa ľudia dostávajú do situácie, keď strácajú motiváciu k výkonu rôznych činností. Nájdi si chvíľu pre nájdenie svojho zdroja inšpirácie.","Vyhľadaj si niečo, čo ťa inšpiruje.","inspired",1);
        long idInspired = habitMainRepository.insert(inspired);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idInspired,1));


        Habit fruits = new Habit("Ovocie a zelenina","Obsahujú veľa minerálov a vitamínov, ktoré sú pre tvoje telo veľmi prospešné. Pomáhajú predchádzať obezite, znižujú cholesterol a krvný tlak. Pomáhajú ako prevencia proti srdcovým ochoreniam.","Obsahujú prospešné vitamíny a minerály napomáhajúce tvojmu zdraviu.","fruits",1);
        long idFrutis = habitMainRepository.insert(fruits);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idFrutis,1));


        Habit stretching = new Habit("Strečing", "Strečing je účinná metóda pri redukovaní stresu. Spočíva v uvoľnení svalov a natiahnutí svalov a je to časovo nenáročná záležitosť.", "Ruky z oblasti šije postupne vzpaž a postav sa na špičky.","friends",1);
        long idStretch = habitMainRepository.insert(stretching);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idStretch,1));


        Habit tea = new Habit("Čaj","Antioxidanty v čaji slúžia ako prevencia proti viacerým chorobám. Hydratuje a povzbudzuje telo a znižuje úroveň stresových hormónov.","Pomôže ti zrelaxovať a lepšie sa sústrediť.","tea",1);
        long idTea = habitMainRepository.insert(tea);

        Habit air = new Habit("Čerstvý vzduch", "Ak si v uzavretej miestnosti, je dobré sa ísť nadýchnuť pri okno, pozorovať okolie. Prekrví ti to mozog a i krátka prechádzka je nápomocná. Vôňa čerstvého vzduchu znižuje stres a zvyšuje pocit šťastia.","Nadýchaj sa čerstvého vzduchu aj z izby.","air",1);
        long idAir = habitMainRepository.insert(air);
        planMainRepository.insertAssociaton(new PlanHabitAssociation(idAir,1));


    }

    public void onPause(){
        super.onPause();
        Log.i("Bakalarka","onPause");
    }

    public void onStart(){
        super.onStart();
                Log.i("Bakalarka", "onStart");
    }

    //skontrolovat ci ide o rovnaky den alebo nie
    private boolean sameDay(long date1, long date2){
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTimeInMillis(date1);
        Calendar calendarMy = Calendar.getInstance();
        calendarMy.setTimeInMillis(date2);
        boolean sameDay = calendarCurrent.get(Calendar.YEAR) == calendarMy.get(Calendar.YEAR) &&
                calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMy.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }

}
