package running.java.mendelu.cz.bakalarskapraca;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.ExamNotificationAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.HabitAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.MainOpenHelper;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanHabitAssociation;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Subject;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelEveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.ExamNotificationReceiver;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by Monika on 12.02.2018.
 */

public class MyPlanTab1Fragment extends Fragment {
        //implements FragmentInterface {

    private static final String TAG = "MyPlanTab";

    private HabitAdapter dailyHabitAdapter;
    private HabitAdapter morningHabitAdapter;
    private HabitAdapter lunchHabitAdapter;
    private HabitAdapter eveningHabitAdapter;

    private RecyclerView dailyRecyclerView;
    private RecyclerView morningRecyclerView;
    private RecyclerView lunchRecyclerView;
    private RecyclerView eveningRecyclerView;

    private HabitMainRepository habitMainRepository;
    private PlanMainRepository planMainRepository;
    private ExamMainRepository examMainRepository;

    private CardView dailyPlanCardView;
    private CardView morningPlanCardView;
    private CardView lunchPlanCardView;
    private CardView eveningPlanCardView;

    private TextView dailyTextView;
    private TextView morningTextView;
    private TextView lunchTextView;
    private TextView eveningTextView;
    private TextView infoAboutDailyPlan;

    private ProgressBar progressDailyBar;
    private ProgressBar progressMorningBar;
    private ProgressBar progressLunchBar;
    private ProgressBar progressEveningBar;

    private int dailyHabits;
    private int morningHabits;
    private int lunchHabits;
    private int eveningHabits;

    private TextView tx;
    private TextView txMorning;
    private TextView txLunch;
    private TextView txEvening;

    private TextView focusText;

    private TextView txDaily;
    private TextView txMorningGone;
    private TextView txLunchGone;
    private TextView txEveningGone;

    private ImageButton settingsButtonDaily;
    private ImageButton settingsButtonMorning;
    private ImageButton settingsButtonLunch;
    private ImageButton settingsButtonEvening;

    private ImageButton infoPlan;

    private Switch switchDaily;
    private View myView;
    private SubjectMainRepository subjectMainRepository;

    private ExamNotificationAdapter examNotificationAdapter;


    /*list.clear
    list.addAll
    notifyDaataetchanged*/

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_plan_tab, container, false);
        myView = view;

        habitMainRepository = new HabitMainRepository(getActivity());
        planMainRepository = new PlanMainRepository(getActivity());
        examMainRepository = new ExamMainRepository(getActivity());
        subjectMainRepository = new SubjectMainRepository(getActivity());

        dailyRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDailyHabits);
        morningRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMorningHabits);
        lunchRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewLunchHabits);
        eveningRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEveningHabits);
        settingsButtonDaily = (ImageButton) view.findViewById(R.id.settingsButtonDaily);
        settingsButtonMorning = (ImageButton) view.findViewById(R.id.settingsButtonMorning);
        settingsButtonLunch = (ImageButton) view.findViewById(R.id.settingsButtonLunch);
        settingsButtonEvening = (ImageButton) view.findViewById(R.id.settingsButtonEvening);
        switchDaily = (Switch) view.findViewById(R.id.switchDailyPlan);
        dailyTextView = (TextView) view.findViewById(R.id.dailyTextView);
        progressDailyBar = (ProgressBar) view.findViewById(R.id.progressDailyBar);
        progressMorningBar = (ProgressBar) view.findViewById(R.id.progressMorningBar);
        progressLunchBar = (ProgressBar) view.findViewById(R.id.progressBarLunch);
        progressEveningBar = (ProgressBar) view.findViewById(R.id.progressBarEvening);
        morningTextView = (TextView) view.findViewById(R.id.morningTextViewMyPlan);
        lunchTextView = (TextView) view.findViewById(R.id.lunchTextViewMyPlan);
        eveningTextView = (TextView) view.findViewById(R.id.eveningTextViewMyPlan);
        infoAboutDailyPlan = (TextView) view.findViewById(R.id.infoAboutDailyPlan);
        focusText = (TextView) view.findViewById(R.id.focusText);
        infoPlan = (ImageButton) view.findViewById(R.id.infoAboutPlan);

        txDaily = (TextView) view.findViewById(R.id.txDaily);
        txMorningGone = (TextView) view.findViewById(R.id.txMorningGone);
        txLunchGone = (TextView) view.findViewById(R.id.txLunchGone);
        txEveningGone = (TextView) view.findViewById(R.id.txEveningGone);

        examNotificationAdapter = new ExamNotificationAdapter(getActivity(), getExamResults());

        setAdapters();

        dailyPlanCardView = (CardView) view.findViewById(R.id.card_view_daily_plan);
        morningPlanCardView = (CardView) view.findViewById(R.id.card_view_morning_plan);
        lunchPlanCardView = (CardView) view.findViewById(R.id.card_view_lunch_plan);
        eveningPlanCardView = (CardView) view.findViewById(R.id.card_view_evening_plan);
        //createTextViews();

        dailyRecyclerView.setNestedScrollingEnabled(false);
        morningRecyclerView.setNestedScrollingEnabled(false);
        lunchRecyclerView.setNestedScrollingEnabled(false);
        eveningRecyclerView.setNestedScrollingEnabled(false);

        setHabitsSize();
        checkPlans();

        setListeners(settingsButtonDaily, 1);
        setListeners(settingsButtonLunch, 3);
        setListeners(settingsButtonEvening, 4);
        setListeners(settingsButtonMorning, 2);

        updateProgressBars();

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(300); // half second between each showcase view
        config.setDismissTextColor(getResources().getColor(R.color.yellow_700));

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), "14");

        sequence.setConfig(config);

        sequence.addSequenceItem(create(focusText,
                "Tento zoznam ti zobrazuje aktivity, ktoré boli vložené do plánu. "));

        sequence.addSequenceItem(create(dailyPlanCardView, "Kliknutím na zaškrtávacie políčko udávaš, že si danú činnosť vykonal."));

        sequence.addSequenceItem(create(switchDaily,
                "Toto tlačidlo slúži na prepínanie medzi plánmi. Jeho prepnutím si zvolíš skúšku, na ktorú sa chceš učiť."));

        sequence.addSequenceItem(create(settingsButtonDaily,
                "Môžeš si nastaviť interval upozornení. Celodenný plán je primárne nastavený na 90 minút, čiastkové na 50 minút."));

        /*sequence.addSequenceItem(focusText,
                "Tento zoznam ti zobrazuje aktivity, ktoré boli vložené do plánu. ", "Rozumiem!");

        sequence.addSequenceItem(dailyPlanCardView, "Kliknutím na zaškrtávacie políčko udávaš, že si danú činnosť vykonal.","Rozumiem!");

        sequence.addSequenceItem(switchDaily,
                "Toto tlačidlo slúži na prepínanie medzi plánmi. Jeho prepnutím si zvolíš skúšku, na ktorú sa chceš učiť.", "Rozumiem!");

        sequence.addSequenceItem(settingsButtonDaily,
                "Môžeš si nastaviť interval upozornení.", "Rozumiem!");*/


        sequence.start();

        infoPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), InfoActivity.class);
                startActivity(i);
            }
        });

        /*YoYo.with(Techniques.Tada)
                .duration(500)
                .repeat(2)
                .playOn(infoPlan);*/


        switchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    if (planMainRepository.getByType(1).getEnabled() == true){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_start_exam_plan, null);
                    Button doIt = (Button) view.findViewById(R.id.letsDoIt);
                    Button fab = (Button) view.findViewById(R.id.letsCancelit);
                    ImageView noExams = (ImageView) view.findViewById(R.id.noExamsImage);
                    TextView infoAboutPlan = (TextView) view.findViewById(R.id.infoAboutStartingPlans);

                    fab.setVisibility(View.GONE);
                    doIt.setVisibility(View.GONE);
                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.availableExamsRecyclerVie);
                    recyclerView.setAdapter(examNotificationAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (examMainRepository.findNextExams().size() < 1) {
                        infoAboutPlan.setText("V najbližšej dobe ťa nečakajú žiadne skúšky.");
                        dialogBuilder.setPositiveButton("Ok", null);
                        noExams.setVisibility(View.VISIBLE);
                        switchDaily.setChecked(true);

                    } else {
                        dialogBuilder.setPositiveButton("Potvrdiť", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //if (examNotificationAdapter.numberOfSelectedExams() > 0) {
                                    if (examNotificationAdapter.numberOfSelectedExams() > 0) {

                                        updateCheckedExams(examNotificationAdapter.getCheckedExams());
                                    switchDaily.setChecked(false);
                                    progressDailyBar.setVisibility(View.GONE);
                                    dailyPlanCardView.setVisibility(View.GONE);
                                    settingsButtonDaily.setVisibility(View.GONE);

                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("enabled", false);
                                    planMainRepository.update2(1, contentValues);


                                    //zrusenie denneho planu
                                    //cancelDividedNotification(1);
                                    //NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                                    //notificationManager.cancel(100);


                                    cancelDividedNotification(1);
                                    setNotificationTime(2);
                                    setNotificationTime(3);
                                    setNotificationTime(4);
                                    setVisible(1);
                                    setExamNotification();
                                        examNotificationAdapter.deleteCheckedExams();
                                        examNotificationAdapter.clearCount();

                                    } else {
                                    switchDaily.setChecked(true);
                                        examNotificationAdapter.deleteCheckedExams();
                                        examNotificationAdapter.clearCount();
                                    //Toast.makeText(getActivity(), "zostava to ako " + planMainRepository.getByType(1).getEnabled(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        dialogBuilder.setNegativeButton("Zrušiť", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int size = examNotificationAdapter.getCheckedExams().size();
                                //int id = examNotificationAdapter.getCheckedExams().get(0)
                                //clearCheckedExams(examNotificationAdapter.getCheckedExams());
                                examNotificationAdapter.deleteCheckedExams();
                                examNotificationAdapter.clearCount();

                                int sizevymaz = examNotificationAdapter.getCheckedExams().size();
                                //Toast.makeText(getActivity(),size + " velkost " + sizevymaz,Toast.LENGTH_LONG).show();

                                switchDaily.setChecked(true);
                                progressDailyBar.setVisibility(View.VISIBLE);
                                dailyPlanCardView.setVisibility(View.VISIBLE);
                                settingsButtonDaily.setVisibility(View.VISIBLE);
                                //Toast.makeText(getActivity(), "Je nastaveny denny plan " + planMainRepository.getByType(1).getEnabled(), Toast.LENGTH_SHORT).show();
                                setVisible(0);

                            }
                        });
                    }
                    dialogBuilder.setView(view);
                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                }
                //horna zatvorka bola pridana kvoli tomu getenabled == true kvoli onresume

                } else {
                    if (planMainRepository.getByType(1).getEnabled() == false) {
                        progressDailyBar.setVisibility(View.VISIBLE);
                        dailyPlanCardView.setVisibility(View.VISIBLE);
                        settingsButtonDaily.setVisibility(View.VISIBLE);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("enabled", true);
                        planMainRepository.update2(1, contentValues);
                        setNotificationTime(1); //pre plan 1
                        cancelDividedNotification(2);
                        cancelDividedNotification(3);
                        cancelDividedNotification(4);
                        setVisible(0);
                    }
                }

            }

        });

        if (planMainRepository.getByType(1).getEnabled() == true){
            switchDaily.setChecked(true);
            progressDailyBar.setVisibility(View.VISIBLE);
            dailyPlanCardView.setVisibility(View.VISIBLE);
            settingsButtonDaily.setVisibility(View.VISIBLE);
           // Toast.makeText(getActivity(), "Je nastaveny denny plan spodna cast " + planMainRepository.getByType(1).getEnabled(), Toast.LENGTH_SHORT).show();
            setVisible(0);

        } else {
            switchDaily.setChecked(false);
            progressDailyBar.setVisibility(View.GONE);
            dailyPlanCardView.setVisibility(View.GONE);
            settingsButtonDaily.setVisibility(View.GONE);
            //Toast.makeText(getActivity(), "Je nastaveny deleny plan spodna cast " + planMainRepository.getByType(1).getEnabled(), Toast.LENGTH_SHORT).show();
            setVisible(1);
        }

        myView = view;
        return view;

    }


    //nastavenie jednotlivych upozorneni na zaklade aktualneho casu a casu upozorneni v databaze
    private void setNotificationTime(int idPlan) {
        Plan plan = planMainRepository.getByType(idPlan);
        Calendar from = Calendar.getInstance();
        from.set(Calendar.HOUR_OF_DAY,plan.getFromHour());
        from.set(Calendar.MINUTE,plan.getFromMinute());
        Calendar to = Calendar.getInstance();
        to.set(Calendar.HOUR_OF_DAY,plan.getToHour());
        to.set(Calendar.MINUTE,plan.getToMinute());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        //kvoli vecernemu?
        if (idPlan == 4){
            if (planMainRepository.getByType(4).getToHour() < planMainRepository.getByType(4).getFromHour()){
                to.add(Calendar.DATE,1);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,00);
                calendar.set(Calendar.MINUTE,1);
                if (from.after(calendar)){
                    from.add(Calendar.DATE,-1);
                    to.add(Calendar.DATE,-1);
                }
            }
        }

        if (System.currentTimeMillis() < from.getTimeInMillis()) {
            setDividedNotifications(from.getTimeInMillis(), idPlan, to.getTimeInMillis());
            //Toast.makeText(getActivity(), "Plan nastaveny do " + sdf.format(to.getTimeInMillis()), Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() < to.getTimeInMillis()) {
            setDividedNotifications(System.currentTimeMillis(), idPlan, to.getTimeInMillis());
           // Toast.makeText(getActivity(), "Plan nastaveny do " + sdf.format(to.getTimeInMillis()), Toast.LENGTH_SHORT).show();
        } else {
            from.add(Calendar.DAY_OF_MONTH,1);
            to.add(Calendar.DAY_OF_MONTH,1);
            //Toast.makeText(getActivity(), "Plan nastaveny do " + sdf.format(to.getTimeInMillis()), Toast.LENGTH_SHORT).show();
            setDividedNotifications(from.getTimeInMillis(), idPlan, to.getTimeInMillis());
        }
    }


    //odstranenie notifikacii pre jednotlive plany
    private void cancelDividedNotification(int type){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent cancelIntent = new Intent(getActivity(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",type);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), cancelPendingIntent);

    }

    //nastavenie delenych notifikacii
    private void setDividedNotifications(long fromTime, int type, long toTime){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, i, PendingIntent.FLAG_UPDATE_CURRENT);
        //setExactPlanNotification(alarmManager, pendingIntent, 2,200);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, fromTime, planMainRepository.getByType(type).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",type);
        //cancelIntent.putExtra("CANCELINTENT", pendingIntent);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, toTime, cancelPendingIntent);

    }

    //nastavenie notifikacii pre denny plan
    private void setDailyNotification(long time, int type, long toTime){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, i, 0);
        //setExactPlanNotification(alarmManager, pendingIntent, 2,200);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(type).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",type);
        //cancelIntent.putExtra("CANCELINTENT", pendingIntent);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, toTime, cancelPendingIntent);
    }

    //priradenie velkosti poli
    private void setHabitsSize(){
        dailyHabits = getDailyHabits().size();
        morningHabits = getMorningHabits().size();
        lunchHabits = getLunchHabits().size();
        eveningHabits = getEveningHabits().size();
    }

    //priradenie buttonom listenery
    private void setListeners(ImageButton button, final int idPlan){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.interval_settings:
                                setSettingsButtons(idPlan);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.tabs_settings);
                popupMenu.show();

            }
        });

    }





    //nastavenie buttonov na upravu notifikacii
    private void setSettingsButtons(final int idPlan) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_set_notification, null);
        Button setNotification25 = (Button) view.findViewById(R.id.setNotification25);
        Button setNotification40 = (Button) view.findViewById(R.id.setNotification40);
        Button setNotification60 = (Button) view.findViewById(R.id.setNotification60);

        myBuilder.setNegativeButton("Zrušiť", null);
        myBuilder.setView(view);
        final AlertDialog dialog = myBuilder.create();
        dialog.show();


            setNotification25.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPlanNotification(25, idPlan);
                    setNotificationTime(idPlan);
                    dialog.cancel();

                }
            });

            setNotification40.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPlanNotification(50, idPlan);
                    setNotificationTime(idPlan);
                    dialog.cancel();
                }
            });

            setNotification60.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPlanNotification(90, idPlan);
                    setNotificationTime(idPlan);
                    dialog.cancel();
                }
            });

    }

    //nastavenie minut a planov
    private void setPlanNotification(int minute, int idPlan){
        PlanMainRepository planMainRepository = new PlanMainRepository(getActivity());
        ContentValues contentValues = new ContentValues();
        int min = minute;
        if (minute == 25){
            min = 30;
        } else if (minute == 50){
            min = 60;
        } else if (minute == 90){
            min = 100;
        }
        contentValues.put("repetition_id",min*60000);
        planMainRepository.update2(idPlan, contentValues);
        Toast.makeText(getActivity(), "Upozornenie nastavené každých " + minute +" min. ", Toast.LENGTH_SHORT).show();

    }

    public List<PlanHabitAssociation> getDailyHabits(){
        List<PlanHabitAssociation> habits = habitMainRepository.getDailyPlanHabits();

        return habits;

    }

    public List<PlanHabitAssociation> getMorningHabits(){
        List<PlanHabitAssociation> habits = habitMainRepository.getMorningPlanHabits();
        return habits;

    }

    public List<PlanHabitAssociation> getLunchHabits(){
        List<PlanHabitAssociation> habits = habitMainRepository.getLunchPlanHabits();
        return habits;

    }

    public List<PlanHabitAssociation> getEveningHabits(){
        List<PlanHabitAssociation> habits = habitMainRepository.getEveningPlanHabits();
        return habits;

    }


    private void setAdapterEvening(){
        eveningHabitAdapter = new HabitAdapter(getActivity(), getEveningHabits(), new HabitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PlanHabitAssociation planHabit) {
                updateProgressBars();
            }
        });
        eveningRecyclerView.setAdapter(eveningHabitAdapter);
        eveningRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setAdapterDaily(){
        dailyHabitAdapter = new HabitAdapter(getActivity(), getDailyHabits(), new HabitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PlanHabitAssociation planHabit) {
                updateProgressBars();
            }
        });
        dailyRecyclerView.setAdapter(dailyHabitAdapter);
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    private void setAdapterMorning(){
        morningHabitAdapter = new HabitAdapter(getActivity(), getMorningHabits(), new HabitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PlanHabitAssociation planHabit) {
                updateProgressBars();
            }
        });
        morningRecyclerView.setAdapter(morningHabitAdapter);
        morningRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void setAdapterLunch(){
        lunchHabitAdapter = new HabitAdapter(getActivity(), getLunchHabits(), new HabitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PlanHabitAssociation planHabit) {
                updateProgressBars();
            }
        });
        lunchRecyclerView.setAdapter(lunchHabitAdapter);
        lunchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void setAdapters(){
        setAdapterEvening();
        setAdapterLunch();
        setAdapterMorning();
        setAdapterDaily();
    }

    private void clearCheckedExams(List<Exam> exams){
        if (exams.size() != 0) {
            for (int i = 0; i < exams.size(); i++) {
                Exam e = exams.get(i);
                long studyDate = e.getStudyDate();
                ExamMainRepository examMainRepository = new ExamMainRepository(getActivity());
                ContentValues contentValues = new ContentValues();
                int studying = e.getStudying();
                contentValues.put("studying", studying - 1);
                contentValues.put("study_date", 0);
                if (studying != 0) {
                    examMainRepository.update2(e.getId(), contentValues);
                    //Toast.makeText(getActivity(), " id exam " + e.getId(), Toast.LENGTH_SHORT).show();
                }


            }
        }

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
                //Toast.makeText(getActivity(), " id exam " + e.getId(), Toast.LENGTH_SHORT).show();



            }
        }
    }

    private boolean sameDay(long time){
        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarMy = Calendar.getInstance();
        calendarMy.setTimeInMillis(time);
        boolean sameDay = calendarCurrent.get(Calendar.YEAR) == calendarMy.get(Calendar.YEAR) &&
                calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMy.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }




    private void createTextViews(){

        tx = new TextView(getActivity());
        tx.setText("Nie je momentálne zvolený žiadny plán. ");
        tx.setPadding(50, 50, 0, 50);
        tx.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        txMorning = new TextView(getActivity());
        txMorning.setText("Nie je momentálne zvolený žiadny plán. ");
        txMorning.setGravity(50);
        txMorning.setPadding(50, 50, 0, 50);
        txMorning.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        txLunch = new TextView(getActivity());
        txLunch.setText("Nie je momentálne zvolený žiadny plán. ");
        txLunch.setGravity(50);
        txLunch.setPadding(50, 50, 0, 50);
        txLunch.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        txEvening = new TextView(getActivity());
        txEvening.setText("Nie je momentálne zvolený žiadny plán. ");
        txEvening.setGravity(50);
        txEvening.setPadding(50, 50, 0, 50);
        txEvening.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

    }

    private void checkPlans(){


            if (getDailyHabits().size() == 0) {
                //dailyPlanCardView.removeAllViews();
                //dailyPlanCardView.addView(tx);
                txDaily.setVisibility(View.VISIBLE);
            } else {
                //dailyPlanCardView.removeView(tx);
                txDaily.setVisibility(View.GONE);


                //LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.mydailyView);
                //dailyPlanCardView.addView(layout);
                //LinearLayout view = (LinearLayout) myView.findViewById(R.id.mydailyView);
                //dailyPlanCardView.addView(view);
            }

            if (getMorningHabits().size() == 0) {
                //morningPlanCardView.removeAllViews();
                //morningPlanCardView.addView(txMorning);
                txMorningGone.setVisibility(View.VISIBLE);
            } else {
                //morningPlanCardView.removeView(txMorning);
                txMorningGone.setVisibility(View.GONE);

            }

            if (getLunchHabits().size() == 0) {
                //lunchPlanCardView.removeAllViews();
                //lunchPlanCardView.addView(txLunch);
                txLunchGone.setVisibility(View.VISIBLE);
            } else {
                //lunchPlanCardView.removeView(txLunch);
                txLunchGone.setVisibility(View.GONE);

            }

            if (getEveningHabits().size() == 0) {
                //eveningPlanCardView.removeAllViews();
                //eveningPlanCardView.addView(txEvening);
                txEveningGone.setVisibility(View.VISIBLE);
            } else {
                //eveningPlanCardView.removeView(txEvening);
                txEveningGone.setVisibility(View.GONE);


            }
        }



    private void setVisible(int visibility){
        switch (visibility){
            case 0:
                progressEveningBar.setVisibility(View.GONE);
                progressLunchBar.setVisibility(View.GONE);
                progressMorningBar.setVisibility(View.GONE);

                morningPlanCardView.setVisibility(View.GONE);
                eveningPlanCardView.setVisibility(View.GONE);
                lunchPlanCardView.setVisibility(View.GONE);

                settingsButtonEvening.setVisibility(View.GONE);
                settingsButtonLunch.setVisibility(View.GONE);
                settingsButtonMorning.setVisibility(View.GONE);

                morningTextView.setVisibility(View.GONE);
                eveningTextView.setVisibility(View.GONE);
                lunchTextView.setVisibility(View.GONE);


                infoAboutDailyPlan.setText(R.string.infoAboutDailyPlanVisible);

                break;
            case 1:
                progressEveningBar.setVisibility(View.VISIBLE);
                progressLunchBar.setVisibility(View.VISIBLE);
                progressMorningBar.setVisibility(View.VISIBLE);

                morningPlanCardView.setVisibility(View.VISIBLE);
                eveningPlanCardView.setVisibility(View.VISIBLE);
                lunchPlanCardView.setVisibility(View.VISIBLE);

                settingsButtonEvening.setVisibility(View.VISIBLE);
                settingsButtonLunch.setVisibility(View.VISIBLE);
                settingsButtonMorning.setVisibility(View.VISIBLE);

                dailyTextView.setVisibility(View.VISIBLE);
                morningTextView.setVisibility(View.VISIBLE);
                eveningTextView.setVisibility(View.VISIBLE);
                lunchTextView.setVisibility(View.VISIBLE);

                infoAboutDailyPlan.setText(R.string.infoAboutDailyPlan);
                break;
        }

    }



    private List<Exam> getExamResults(){
        return examMainRepository.getExamResultsListNotification();
    }

    public void onResume(){
        super.onResume();
        Log.i(TAG,"onResume");
        refreshSwitch();
        eveningHabitAdapter.notifyDataSetChanged();
        morningHabitAdapter.notifyDataSetChanged();
        lunchHabitAdapter.notifyDataSetChanged();
        dailyHabitAdapter.notifyDataSetChanged();
        checkPlans();
        updateProgressBars();
        setAdapters();
    }

    public void refreshSwitch(){
        if (switchDaily.isChecked() && planMainRepository.getByType(1).getEnabled() == false){
            switchDaily.setChecked(false);
            progressDailyBar.setVisibility(View.GONE);
            dailyPlanCardView.setVisibility(View.GONE);
            settingsButtonDaily.setVisibility(View.GONE);
            setVisible(1);
        } else if (switchDaily.isChecked() == false && planMainRepository.getByType(1).getEnabled() == true){
            switchDaily.setChecked(true);
            progressDailyBar.setVisibility(View.VISIBLE);
            dailyPlanCardView.setVisibility(View.VISIBLE);
            settingsButtonDaily.setVisibility(View.VISIBLE);
            setVisible(0);
        }
    }

    private void setExamNotification(){
        Calendar calendar = Calendar.getInstance();
        ContentValues contentValues = new ContentValues();
        //Toast.makeText(getActivity(), examMainRepository.findNextExams().size() + " size of exams", Toast.LENGTH_SHORT).show();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //nova cast, podla mna
        calendar.set(Calendar.MINUTE,subjectMainRepository.getProjectById(1).getMinute());
        calendar.set(Calendar.HOUR_OF_DAY,subjectMainRepository.getProjectById(1).getHour());

        if (System.currentTimeMillis() > calendar.getTimeInMillis()){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }

        Intent i = new Intent(getActivity(), ExamNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.interval_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private long getCurrentDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTimeInMillis();
    }

    private void updateProgressBars(){
        progressDailyBar.setProgress(habitMainRepository.getDoneDailyPlanHabits());
        progressDailyBar.setMax(habitMainRepository.getDailyPlanHabits().size());

        progressMorningBar.setProgress(habitMainRepository.getDoneMorningPlanHabits());
        progressMorningBar.setMax(habitMainRepository.getMorningPlanHabits().size());

        progressLunchBar.setProgress(habitMainRepository.getDoneLunchPlanHabits());
        progressLunchBar.setMax(habitMainRepository.getLunchPlanHabits().size());

        progressEveningBar.setProgress(habitMainRepository.getDoneEveningPlanHabits());
        progressEveningBar.setMax(habitMainRepository.getEveningPlanHabits().size());
    }

    public void updateFragment(){
        updateProgressBars();
        checkPlans();
        setAdapters();
    }

}
