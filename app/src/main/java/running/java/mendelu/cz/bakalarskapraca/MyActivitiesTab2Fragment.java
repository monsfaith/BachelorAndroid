package running.java.mendelu.cz.bakalarskapraca;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.Habit;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.HabitToChooseAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelEveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelLunchHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelMorningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;

/**
 * Created by Monika on 12.02.2018.
 */

public class MyActivitiesTab2Fragment extends Fragment implements FragmentInterface{

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

    private Button timeButtonDaily;
    private Button timeButtonMorning;
    private Button timeButtonLunch;
    private Button timeButtonEvening;

    private RelativeLayout morningRelativeLayout;
    private RelativeLayout eveningRelativeLayout;
    private RelativeLayout lunchRelativeLayout;
    private RelativeLayout dailyRelativeLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
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


        //http://jakewharton.github.io/butterknife/
        timeButtonDaily = (Button) view.findViewById(R.id.timeButtonDaily);
        timeButtonEvening = (Button) view.findViewById(R.id.timeButtonEvening);
        timeButtonLunch = (Button) view.findViewById(R.id.timeButtonLunch);
        timeButtonMorning = (Button) view.findViewById(R.id.timeButtonMorning);


        Toast.makeText(getActivity(), planMainRepository.getAllPlans().size() + " pocet planov", Toast.LENGTH_SHORT).show();

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

        return view;

    }

    private void setListeners(Button button, final int idPlan){


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
                AlertDialog.Builder builder = myBuilder.setPositiveButton("Potvrdiť", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar cal = Calendar.getInstance();
                        if (Build.VERSION.SDK_INT >= 23) {
                            cal.set(Calendar.HOUR_OF_DAY, timePickerFrom.getHour());
                            cal.set(Calendar.MINUTE, timePickerFrom.getMinute());
                        } else {
                            cal.set(Calendar.HOUR_OF_DAY, timePickerFrom.getCurrentHour());
                            cal.set(Calendar.MINUTE, timePickerFrom.getCurrentMinute());
                        }

                        ContentValues cv = new ContentValues();
                        long fromTime = cal.getTimeInMillis();
                        cv.put("from_time", fromTime);
                        if (Build.VERSION.SDK_INT >= 23) {
                            cal.set(Calendar.HOUR_OF_DAY, timePickerTo.getHour());
                            cal.set(Calendar.MINUTE, timePickerTo.getMinute());
                        } else {
                            cal.set(Calendar.HOUR_OF_DAY, timePickerTo.getCurrentHour());
                            cal.set(Calendar.MINUTE, timePickerTo.getCurrentMinute());
                        }
                        long toTime = cal.getTimeInMillis();
                        cv.put("to_time", toTime);
                        if (new Date(fromTime).after(new Date(toTime))) {
                            Toast.makeText(getActivity(), "Je nutné upraviť čas ukončenia upozornenia. ", Toast.LENGTH_LONG).show();
                        } else {
                            planMainRepository.update2(idPlan, cv);
                            Plan plan = planMainRepository.getByType(idPlan);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                            if (getCurrentTime() < plan.getFromTime().getTime() || getCurrentTime() == plan.getFromTime().getTime()) {
                                setHabitNotification(planMainRepository.getByType(idPlan).getFromTime().getTime(), idPlan);
                                Toast.makeText(getActivity(), "Upozornenie od " + sdf.format(plan.getFromTime()), Toast.LENGTH_LONG).show();
                            } else if (getCurrentTime() < plan.getToTime().getTime()) {
                                //if (getNotificationClass(idPlan) != null){
                                    setHabitNotification(getCurrentTime(), idPlan);
                                //}
                                Toast.makeText(getActivity(), "Upozornenie od " + sdf.format(getCurrentTime()) + " " + sdf.format(plan.getToTime().getTime()), Toast.LENGTH_LONG).show();

                            } else {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(plan.getFromTime());
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                setHabitNotification(cal.getTimeInMillis(), idPlan);

                                Toast.makeText(getActivity(), "Upozornenie od zajtra" + sdf.format(cal.getTimeInMillis()), Toast.LENGTH_LONG).show();

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
        long time = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTimeInMillis();
    }


    public List<Habit> getAllHabits(){
        List<Habit> habits = habitMainRepository.getAllHabits();

        /*List<Exam> exams = new ArrayList<>();
        String[] titles = {"ahj","dkd","fdfd"};
        for (int i=0; i < titles.length; i++){
            Exam ex = new Exam(new Date(),new Time(5655),3,2,2,"d",5);
            ex.setClassroom(titles[i]);
            exams.add(ex);
        }*/
        return habits;

    }


    public void onResume(){
        super.onResume();
        setAdapterView();
        setTimeButtons();

    }

    private void setTimeButtons(){
        setTimeTextViews(timeButtonDaily,1);
        setTimeTextViews(timeButtonMorning,2);
        setTimeTextViews(timeButtonLunch,3);
        setTimeTextViews(timeButtonEvening,4);
    }

    private void setTimeTextViews(Button button, long id){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        button.setText(sdf.format(planMainRepository.getByType(id).getFromTime().getTime()) + " - " + sdf.format(planMainRepository.getByType(id).getToTime().getTime()));
    }

    private void setAdapterView(){
        setAdapters(dailyHabitToChooseAdapter, dailyRecyclerView, 1);
        setAdapters(morningHabitToChooseAdapter, morningRecyclerView, 2);
        setAdapters(lunchHabitToChooseAdapter, lunchRecyclerView, 3);
        setAdapters(eveningHabitToChooseAdapter, eveningRecyclerView, 4);
    }

    private void setAdapters(HabitToChooseAdapter habitToChooseAdapter, RecyclerView recyclerView, int planId){
        habitToChooseAdapter = new HabitToChooseAdapter(getActivity(),getAllHabits(),planId);
        recyclerView.setAdapter(habitToChooseAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));
    }

    private Calendar setTime(Time time){
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal;

    }


    @Override
    public void fragmentSwitchToVisible() {
        if (planMainRepository.getByType(1).getEnabled() == true){
            lunchRelativeLayout.setVisibility(View.GONE);
            morningRelativeLayout.setVisibility(View.GONE);
            eveningRelativeLayout.setVisibility(View.GONE);
            dailyRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            lunchRelativeLayout.setVisibility(View.VISIBLE);
            morningRelativeLayout.setVisibility(View.VISIBLE);
            eveningRelativeLayout.setVisibility(View.VISIBLE);
            dailyRelativeLayout.setVisibility(View.GONE);
        }

    }

    /*private void setHabitNotification(int type, Class<?> notifClass, Class<?> cancelClass){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), notifClass);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, i, 0);
        //setExactPlanNotification(alarmManager, pendingIntent, 2,200);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(type).getFromTime().getTime(), planMainRepository.getByType(type).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), cancelClass);
        //cancelIntent.putExtra("CANCELINTENT", pendingIntent);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(type).getToTime().getTime(), cancelPendingIntent);


    }*/

    private void setHabitNotification(long time, int type){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getActivity(), EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, i, 0);
        //setExactPlanNotification(alarmManager, pendingIntent, 2,200);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,300000 , pendingIntent);

        Intent cancelIntent = new Intent(getActivity(), CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",type);
        //cancelIntent.putExtra("CANCELINTENT", pendingIntent);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getActivity(), type*100, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(type).getToTime().getTime(), cancelPendingIntent);
    }

    /*private Class<?> getNotificationClass(int idPlan){
        switch (idPlan){
            case 2: return MorningHabitNotificationReceiver.class;
            case 3: return LunchHabitNotificationReceiver.class;
            case 4:return EveningHabitNotificationReceiver.class;
            default: return null;
        }
    }*/

    private Class<?> getCancelNotificationClass(int idPlan){
        switch (idPlan){
            case 2: return CancelMorningHabitNotificationReceiver.class;
            case 3: return CancelLunchHabitNotificationReceiver.class;
            case 4: return CancelEveningHabitNotificationReceiver.class;
            default: return null;
        }
    }




}
