package running.java.mendelu.cz.bakalarskapraca;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.Habit;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.HabitToChooseAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanHabitAssociation;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelEveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by Monika on 12.02.2018.
 */

public class MyActivitiesTab2Fragment extends Fragment{
        //implements FragmentInterface{

    private HabitMainRepository habitMainRepository;
    private PlanMainRepository planMainRepository;

    private HabitToChooseAdapter morningHabitToChooseAdapter;
    private HabitToChooseAdapter dailyHabitToChooseAdapter;
    private HabitToChooseAdapter lunchHabitToChooseAdapter;
    private HabitToChooseAdapter eveningHabitToChooseAdapter;
    private FloatingActionButton floatingActionButton;
    private TimePicker timePickerFrom;
    private TimePicker timePickerTo;
    private TextView timeFromTextView;
    private TextView timeToTextView;

    private RecyclerView dailyRecyclerView;
    private RecyclerView morningRecyclerView;
    private RecyclerView lunchRecyclerView;
    private RecyclerView eveningRecyclerView;

    private ImageButton timeButtonDaily;
    private ImageButton timeButtonMorning;
    private ImageButton timeButtonLunch;
    private ImageButton timeButtonEvening;

    private TextView timeTextViewDaily;
    private TextView timeTextViewMorning;
    private TextView timeTextViewLunch;
    private TextView timeTextViewEvening;

    private RelativeLayout relativeLayout;

    private RelativeLayout morningRelativeLayout;
    private RelativeLayout eveningRelativeLayout;
    private RelativeLayout lunchRelativeLayout;
    private RelativeLayout dailyRelativeLayout;

    private AlertDialog.Builder dailyDialog;
    private AlertDialog.Builder morningDialog;
    private AlertDialog.Builder lunchDialog;
    private AlertDialog.Builder eveningDialog;

    private AlertDialog.OnClickListener dailyListener;
    private AlertDialog.OnClickListener morningListener;
    private AlertDialog.OnClickListener lunchListener;
    private AlertDialog.OnClickListener eveningListener;

    private Button hiddenButton;

    private MyPlanTab1Fragment myPlanTabFragment;

    public void setMyPlanTabFragment(MyPlanTab1Fragment mptf){
        this.myPlanTabFragment = mptf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_my_activities_tab, container, false);


        habitMainRepository = new HabitMainRepository(getActivity());
        planMainRepository = new PlanMainRepository(getActivity());
        dailyRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDailyHabitToChoose);
        morningRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMorningHabitToChoose);
        lunchRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewLunchHabitToChoose);
        eveningRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEveningHabitToChoose);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButtonAddActivity);
        morningRelativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutActivitiesMorning);
        eveningRelativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutActivitiesEvening);
        lunchRelativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutActivitiesLunch);
        dailyRelativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutActivitiesDaily);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.fragmentActivitiesLayout);


        timeButtonDaily = (ImageButton) view.findViewById(R.id.timeButtonDaily);
        timeButtonEvening = (ImageButton) view.findViewById(R.id.timeButtonEvening);
        timeButtonLunch = (ImageButton) view.findViewById(R.id.timeButtonLunch);
        timeButtonMorning = (ImageButton) view.findViewById(R.id.timeButtonMorning);

        timeTextViewDaily = (TextView) view.findViewById(R.id.timeTextViewDaily);
        timeTextViewMorning = (TextView) view.findViewById(R.id.timeTextViewMorning);
        timeTextViewLunch = (TextView) view.findViewById(R.id.timeTextViewLunch);
        timeTextViewEvening = (TextView) view.findViewById(R.id.timeTextViewEvening);

        hiddenButton = (Button) view.findViewById(R.id.hiddenButton);

        relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ShowcaseConfig config = new ShowcaseConfig();
                config.setDelay(300); // half second between each showcase view
                config.setDismissTextColor(getResources().getColor(R.color.yellow_700));

                MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), "15");

                sequence.setConfig(config);
                sequence.addSequenceItem(create(timeTextViewDaily,"Ikony zobrazujú dostupné činnosti k vykonaniu, ktoré si môžeš pridať do plánu."));
                sequence.addSequenceItem(create(hiddenButton,"V tejto časti si tvoríš plán pridávaním a odoberaním aktivít."));
                sequence.addSequenceItem(create(timeButtonDaily,"Ikona budíka charakterizuje dobu upozorňovania na aktivity v pláne."));
                sequence.addSequenceItem(create(floatingActionButton,"Dokonca si môžeš vytvoriť i vlastnú aktivitu!"));
                //sequence.start();

                /*sequence.addSequenceItem(timeTextViewDaily, "Ikony zobrazujú dostupné činnosti k vykonaniu, ktoré si môžeš pridať do plánu.","Rozumiem!");

                sequence.addSequenceItem(hiddenButton, "V tejto časti si tvoríš plán a jeho aktivity podľa seba.","Rozumiem!");

                sequence.addSequenceItem(timeButtonDaily,
                        "Ikona budíka charakterizuje dobu upozorňovania na aktivity v pláne.", "Rozumiem!");

                sequence.addSequenceItem(floatingActionButton,
                        "Dokonca si môžeš vytvoriť i vlastnú aktivitu!", "Rozumiem!");*/


                sequence.start();
            }
        });


        //http://jakewharton.github.io/butterknife/



        /*ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(300); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), "12");

        sequence.setConfig(config);

        sequence.addSequenceItem(dailyRecyclerView,
                "Jednotlivé ikony Ti zobrazujú všetky dostupné činnosti k vykonaniu. Kliknutím na ne si zobrazíš informácie a môžeš si ich pridať do plánu.", "Chápem!");

        sequence.addSequenceItem(floatingActionButton,
                "Dokonca si môžeš vytvoriť i vlastnú aktivitu!", "Chápem!");


        sequence.start();*/


        //Toast.makeText(getActivity(), planMainRepository.getAllPlans().size() + " pocet planov", Toast.LENGTH_SHORT).show();

        setAdapterView();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateHabitActivity.class);
                startActivity(i);
            }
        });

        setListeners(timeButtonDaily, 1);
        setListeners(timeButtonMorning, 2);
        setListeners(timeButtonLunch,3);
        setListeners(timeButtonEvening,4);

        //setListeners(myPlanTabFragment);

        return view;

    }




    private void setListeners(ImageButton button, final int idPlan){


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time_picker, null);
                timePickerFrom = (TimePicker) view.findViewById(R.id.timePickerFrom);
                timePickerTo = (TimePicker) view.findViewById(R.id.timePickerTo);
                timeFromTextView = (TextView) view.findViewById(R.id.textViewTimePickerFrom);
                timeToTextView = (TextView) view.findViewById(R.id.textViewTimePickerTo);

                if (Build.VERSION.SDK_INT >=23){
                    timePickerFrom.setHour(setTime(planMainRepository.getByType(idPlan).getFromTime()).get(Calendar.HOUR_OF_DAY));
                    timePickerFrom.setMinute(setTime(planMainRepository.getByType(idPlan).getFromTime()).get(Calendar.MINUTE));
                    timePickerTo.setHour(setTime(planMainRepository.getByType(idPlan).getToTime()).get(Calendar.HOUR_OF_DAY));
                    timePickerTo.setMinute(setTime(planMainRepository.getByType(idPlan).getToTime()).get(Calendar.MINUTE));
                } else {
                    timePickerFrom.setCurrentHour(setTime(planMainRepository.getByType(idPlan).getFromTime()).get(Calendar.HOUR_OF_DAY));
                    timePickerFrom.setCurrentMinute(setTime(planMainRepository.getByType(idPlan).getFromTime()).get(Calendar.MINUTE));
                    timePickerTo.setCurrentHour(setTime(planMainRepository.getByType(idPlan).getToTime()).get(Calendar.HOUR_OF_DAY));
                    timePickerTo.setCurrentMinute(setTime(planMainRepository.getByType(idPlan).getToTime()).get(Calendar.MINUTE));
                }

                timeFromTextView.setText("Upozorňovať od ");
                timeToTextView.setText("Upozorňovať do ");
                timePickerFrom.setIs24HourView(true);
                timePickerTo.setIs24HourView(true);

                myBuilder.setView(view);
                myBuilder.setPositiveButton("Potvrdiť", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar cal = Calendar.getInstance();
                        ContentValues cv = new ContentValues();
                        Calendar from = Calendar.getInstance();
                        Calendar to = Calendar.getInstance();

                        if (Build.VERSION.SDK_INT >= 23) {
                            cal.set(Calendar.HOUR_OF_DAY, timePickerFrom.getHour());
                            cal.set(Calendar.MINUTE, timePickerFrom.getMinute());
                            cv.put("from_hour",timePickerFrom.getHour());
                            cv.put("from_minute",timePickerFrom.getMinute());
                            from.set(Calendar.HOUR_OF_DAY,timePickerFrom.getHour());
                            from.set(Calendar.MINUTE,timePickerFrom.getMinute());

                        } else {
                            cal.set(Calendar.HOUR_OF_DAY, timePickerFrom.getCurrentHour());
                            cal.set(Calendar.MINUTE, timePickerFrom.getCurrentMinute());
                            cv.put("from_hour",timePickerFrom.getCurrentHour());
                            cv.put("from_minute",timePickerFrom.getCurrentMinute());
                            from.set(Calendar.HOUR_OF_DAY, timePickerFrom.getCurrentHour());
                            from.set(Calendar.MINUTE, timePickerFrom.getCurrentMinute());
                        }

                        long fromTime = cal.getTimeInMillis();
                        cv.put("from_time", fromTime);
                        if (Build.VERSION.SDK_INT >= 23) {
                            cal.set(Calendar.HOUR_OF_DAY, timePickerTo.getHour());
                            cal.set(Calendar.MINUTE, timePickerTo.getMinute());
                            cv.put("to_hour",timePickerTo.getHour());
                            cv.put("to_minute",timePickerTo.getMinute());
                            to.set(Calendar.HOUR_OF_DAY, timePickerTo.getHour());
                            to.set(Calendar.MINUTE,timePickerTo.getMinute());
                        } else {
                            cal.set(Calendar.HOUR_OF_DAY, timePickerTo.getCurrentHour());
                            cal.set(Calendar.MINUTE, timePickerTo.getCurrentMinute());
                            cv.put("to_hour",timePickerTo.getCurrentHour());
                            cv.put("to_minute",timePickerTo.getCurrentMinute());
                            to.set(Calendar.HOUR_OF_DAY, timePickerTo.getCurrentHour());
                            to.set(Calendar.MINUTE,timePickerTo.getCurrentMinute());
                        }
                        long toTime = cal.getTimeInMillis();
                        cv.put("to_time", toTime);


                        Calendar cali = Calendar.getInstance();
                        //cali.add(Calendar.DAY_OF_MONTH,1);
                        cali.set(Calendar.HOUR_OF_DAY,3);
                        cali.set(Calendar.MINUTE,30);



                            if (new Date(fromTime).after(new Date(toTime))) {
                                if (idPlan == 4 && new Date(toTime).before(new Date(cali.getTimeInMillis()))){

                                        planMainRepository.update2(idPlan, cv);
                                        Plan plan = planMainRepository.getByType(idPlan);
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                        to.add(Calendar.DAY_OF_MONTH,1);

                                    if (System.currentTimeMillis() < from.getTimeInMillis()) {
                                            setHabitNotification(from.getTimeInMillis(), idPlan, to.getTimeInMillis());
                                        } else if (System.currentTimeMillis() < to.getTimeInMillis()) {

                                            setHabitNotification(System.currentTimeMillis(), idPlan, to.getTimeInMillis());


                                        } else {

                                            from.add(Calendar.DAY_OF_MONTH,1);
                                            to.add(Calendar.DAY_OF_MONTH,1);
                                            setHabitNotification(from.getTimeInMillis(), idPlan, to.getTimeInMillis());

                                            //Toast.makeText(getActivity(), "Upozornenie od zajtra" + sdf.format(from.getTimeInMillis()), Toast.LENGTH_LONG).show();

                                        }


                                        setTimeButtons();

                                } else {
                                    Toast.makeText(getActivity(), "Je nutné upraviť čas ukončenia upozornenia. ", Toast.LENGTH_LONG).show();
                                }
                            } else //(!(idPlan == 4 && new Date(toTime).before(new Date(cali.getTimeInMillis()))))
                        {
                                planMainRepository.update2(idPlan, cv);
                                Plan plan = planMainRepository.getByType(idPlan);
                                /*from.set(Calendar.HOUR_OF_DAY,plan.getFromHour());
                                from.set(Calendar.MINUTE, plan.getFromMinute());
                                to.set(Calendar.HOUR_OF_DAY, plan.getToHour());
                                to.set(Calendar.MINUTE, plan.getToMinute());*/


                                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                if (System.currentTimeMillis() < from.getTimeInMillis()) {
                                    setHabitNotification(from.getTimeInMillis(), idPlan, to.getTimeInMillis());
                                } else if (System.currentTimeMillis() < to.getTimeInMillis()) {
                                    //if (getNotificationClass(idPlan) != null){
                                    setHabitNotification(System.currentTimeMillis(), idPlan, to.getTimeInMillis());
                                    //}

                                } else {

                                    from.add(Calendar.DAY_OF_MONTH,1);
                                    to.add(Calendar.DAY_OF_MONTH,1);
                                    setHabitNotification(from.getTimeInMillis(), idPlan, to.getTimeInMillis());

                                    //Toast.makeText(getActivity(), "Upozornenie od zajtra" + sdf.format(from.getTimeInMillis()), Toast.LENGTH_LONG).show();

                                }


                                setTimeButtons();

                            }

                    }
                });
                myBuilder.setNegativeButton("Zrušiť", null);
                AlertDialog habitDialog = myBuilder.create();
                habitDialog.show();

            }
        });

    }

    private long getCurrentTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTimeInMillis();
    }


    public List<Habit> getAllHabits(){
        List<Habit> habits = habitMainRepository.getAllHabits();


        return habits;

    }


    public void onResume(){
        super.onResume();
        setAdapterView();
        setTimeButtons();

    }

    private void setTimeButtons(){
        setTimeTextViews(timeTextViewDaily,1);
        setTimeTextViews(timeTextViewMorning,2);
        setTimeTextViews(timeTextViewLunch,3);
        setTimeTextViews(timeTextViewEvening,4);
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

    private void setTimeTextViews(TextView button, long id){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        button.setText(sdf.format(planMainRepository.getByType(id).getFromTime().getTime()) + " - " + sdf.format(planMainRepository.getByType(id).getToTime().getTime()));
    }

    private void setAdapterView(){
        setAdapters();
    }

    private void setAdapters(){
        setAdapter();
        setMorningAdapter();
        setLunchAdapter();
        setEveningAdapter();

    }

    private void setAdapter(){
        dailyHabitToChooseAdapter = new HabitToChooseAdapter(getActivity(),getAllHabits(),1, myPlanTabFragment);
        dailyRecyclerView.setAdapter(dailyHabitToChooseAdapter);
        dailyRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));
    }

    private void setMorningAdapter() {
        morningHabitToChooseAdapter = new HabitToChooseAdapter(getActivity(), getMorningHabits(), 2, myPlanTabFragment);
        morningRecyclerView.setAdapter(morningHabitToChooseAdapter);
        morningRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
    }

    private void setLunchAdapter() {
        lunchHabitToChooseAdapter = new HabitToChooseAdapter(getActivity(), getLunchHabits(), 3, myPlanTabFragment);
        lunchRecyclerView.setAdapter(lunchHabitToChooseAdapter);
        lunchRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
    }

    private void setEveningAdapter() {
        eveningHabitToChooseAdapter = new HabitToChooseAdapter(getActivity(), getEveningHabits(), 4, myPlanTabFragment);
        eveningRecyclerView.setAdapter(eveningHabitToChooseAdapter);
        eveningRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
    }

    private Calendar setTime(Time time){
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal;

    }

    public List<Habit> getDailyHabits(){
        List<Habit> habits = habitMainRepository.getAllHabits();

        return habits;

    }

    public List<Habit> getMorningHabits(){
        List<Habit> habits = habitMainRepository.getAllMorningHabits();
        return habits;

    }

    public List<Habit> getLunchHabits(){
        List<Habit> habits = habitMainRepository.getAllLunchHabits();
        return habits;

    }

    public List<Habit> getEveningHabits(){
        List<Habit> habits = habitMainRepository.getAllEveningHabits();
        return habits;

    }


    private void setHabitNotification(long fromTime, int type, long toTime){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, i, PendingIntent.FLAG_UPDATE_CURRENT);
        //setExactPlanNotification(alarmManager, pendingIntent, 2,200);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, fromTime,planMainRepository.getByType(type).getRepetition() , pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",type);
        //cancelIntent.putExtra("CANCELINTENT", pendingIntent);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, toTime, cancelPendingIntent);
    }






}
