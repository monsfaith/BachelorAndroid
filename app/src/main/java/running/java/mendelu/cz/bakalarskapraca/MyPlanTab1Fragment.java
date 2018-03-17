package running.java.mendelu.cz.bakalarskapraca;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.HabitAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanHabitAssociation;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelEveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;

/**
 * Created by Monika on 12.02.2018.
 */

public class MyPlanTab1Fragment extends Fragment implements FragmentInterface {

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

    private ImageButton settingsButtonDaily;
    private ImageButton settingsButtonMorning;
    private ImageButton settingsButtonLunch;
    private ImageButton settingsButtonEvening;

    private Switch switchDaily;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_my_plan_tab, container, false);

        habitMainRepository = new HabitMainRepository(getActivity());
        planMainRepository = new PlanMainRepository(getActivity());

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



        setAdapterDaily();
        setAdapterEvening();
        setAdapterLunch();
        setAdapterMorning();

        dailyPlanCardView = (CardView) view.findViewById(R.id.card_view_daily_plan);
        morningPlanCardView = (CardView) view.findViewById(R.id.card_view_morning_plan);
        lunchPlanCardView = (CardView) view.findViewById(R.id.card_view_lunch_plan);
        eveningPlanCardView = (CardView) view.findViewById(R.id.card_view_evening_plan);
        createTextViews();
        tx.setVisibility(View.GONE);

        Toast.makeText(getActivity(), getDailyHabits().size() + "", Toast.LENGTH_LONG).show();
        setHabitsSize();
        checkPlans();

        setListeners(settingsButtonDaily, 1);
        setListeners(settingsButtonLunch, 3);
        setListeners(settingsButtonEvening,4);
        setListeners(settingsButtonMorning,2);

        switchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getActivity(), "Denný plán je vhodné zapínať pri neprebiehajúcom učiacom móde. ", Toast.LENGTH_LONG).show();
                    progressDailyBar.setVisibility(View.VISIBLE);
                    dailyPlanCardView.setVisibility(View.VISIBLE);
                    settingsButtonDaily.setVisibility(View.VISIBLE);
                    tx.setVisibility(View.VISIBLE);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("ENABLED",true);
                    planMainRepository.update2(1,contentValues);
                    setVisible(0);

                    cancelDividedNotification(2);
                    cancelDividedNotification(3);
                    cancelDividedNotification(4);

                    Plan dailyPlan = planMainRepository.getByType(1);
                    if (getCurrentTime() < dailyPlan.getFromTime().getTime() || getCurrentTime() == dailyPlan.getFromTime().getTime()) {
                        setDailyNotification(dailyPlan.getFromTime().getTime(), 1);
                    } else if (getCurrentTime() < dailyPlan.getToTime().getTime() || getCurrentTime() == dailyPlan.getToTime().getTime()) {
                        setDailyNotification(getCurrentTime(), 1);
                    } else {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dailyPlan.getFromTime());
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        setDailyNotification(cal.getTimeInMillis(), 1);
                    }


                } else {
                    Toast.makeText(getActivity(), "Denný plán je vhodné zapínať pri neprebiehajúcom učiacom móde. ", Toast.LENGTH_LONG).show();
                    progressDailyBar.setVisibility(View.GONE);
                    dailyPlanCardView.setVisibility(View.GONE);
                    settingsButtonDaily.setVisibility(View.GONE);
                    tx.setVisibility(View.GONE);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("ENABLED",false);
                    planMainRepository.update2(1,contentValues);
                    setNotificationTime(2);
                    setNotificationTime(3);
                    setNotificationTime(4);
                    setVisible(1);

                }
            }
        });

        if (planMainRepository.getByType(1).getEnabled() == true){
            switchDaily.setChecked(true);
            progressMorningBar.setVisibility(View.VISIBLE);
            dailyPlanCardView.setVisibility(View.VISIBLE);
            settingsButtonDaily.setVisibility(View.VISIBLE);
            tx.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Je nastaveny denny plan " + planMainRepository.getByType(1).getEnabled(), Toast.LENGTH_SHORT).show();
            setVisible(0);

        } else {
            switchDaily.setChecked(false);
            progressDailyBar.setVisibility(View.GONE);
            dailyPlanCardView.setVisibility(View.GONE);
            settingsButtonDaily.setVisibility(View.GONE);
            tx.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Je nastaveny deleny plan " + planMainRepository.getByType(1).getEnabled(), Toast.LENGTH_SHORT).show();
            setVisible(1);
        }

        return view;

    }

    //aktualny cas
    private long getCurrentTime(){
        long time = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTimeInMillis();
    }

    //nastavenie jednotlivych upozorneni na zaklade aktualneho casu a casu upozorneni v databaze
    private void setNotificationTime(int idPlan) {
        Plan plan = planMainRepository.getByType(idPlan);
        if (getCurrentTime() < plan.getFromTime().getTime() || getCurrentTime() == plan.getFromTime().getTime()) {
            setDividedNotifications(plan.getFromTime().getTime(), idPlan);
        } else if (getCurrentTime() < plan.getToTime().getTime()) {
            setDividedNotifications(getCurrentTime(), idPlan);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(plan.getFromTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            setDividedNotifications(cal.getTimeInMillis(), idPlan);
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
    private void setDividedNotifications(long time, int type){
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
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(type).getToTime().getTime(), cancelPendingIntent);

    }

    private long getPlanFromTime(long idPlan){
        return planMainRepository.getByType(idPlan).getFromTime().getTime();
    }

    //nastavenie notifikacii pre denny plan
    private void setDailyNotification(long time, int type){
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
        alarmManager.set(AlarmManager.RTC_WAKEUP, planMainRepository.getByType(type).getToTime().getTime(), cancelPendingIntent);
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
    private void setSettingsButtons(final int idPlan){
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_set_notification, null);
        ImageButton setNotification25 = (ImageButton) view.findViewById(R.id.setNotification25);
        ImageButton setNotification40 = (ImageButton) view.findViewById(R.id.setNotification40);
        ImageButton setNotification60 = (ImageButton) view.findViewById(R.id.setNotification60);

        myBuilder.setNegativeButton("Zrušiť",null);
        myBuilder.setView(view);
        final AlertDialog dialog = myBuilder.create();

        dialog.show();

        setNotification25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlanNotification(25, idPlan);
                dialog.cancel();

            }
        });

        setNotification40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlanNotification(40, idPlan);
                dialog.cancel();
            }
        });

        setNotification60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlanNotification(60, idPlan);
                dialog.cancel();
            }
        });
    }

    //nastavenie minut a planov
    private void setPlanNotification(int minute, int idPlan){
        PlanMainRepository planMainRepository = new PlanMainRepository(getActivity());
        ContentValues contentValues = new ContentValues();
        contentValues.put("REPETITION_ID",minute*60000);
        planMainRepository.update2(idPlan, contentValues);
        Toast.makeText(getActivity(), "Upozornenie nastavene kazdych " + minute + " min " + planMainRepository.getByType(idPlan).getRepetition() + " "  + planMainRepository.getByType(idPlan).getType(), Toast.LENGTH_LONG).show();

    }

    public List<PlanHabitAssociation> getDailyHabits(){
        List<PlanHabitAssociation> habits = habitMainRepository.getDailyPlanHabits();

        /*List<Exam> exams = new ArrayList<>();
        String[] titles = {"ahj","dkd","fdfd"};
        for (int i=0; i < titles.length; i++){
            Exam ex = new Exam(new Date(),new Time(5655),3,2,2,"d",5);
            ex.setClassroom(titles[i]);
            exams.add(ex);
        }*/
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
        eveningHabitAdapter = new HabitAdapter(getActivity(), getEveningHabits());
        eveningRecyclerView.setAdapter(eveningHabitAdapter);
        eveningRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setAdapterDaily(){
        dailyHabitAdapter = new HabitAdapter(getActivity(),getDailyHabits());
        dailyRecyclerView.setAdapter(dailyHabitAdapter);
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    private void setAdapterMorning(){
        morningHabitAdapter = new HabitAdapter(getActivity(), getMorningHabits());
        morningRecyclerView.setAdapter(morningHabitAdapter);
        morningRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    private void setAdapterLunch(){
        lunchHabitAdapter = new HabitAdapter(getActivity(),getLunchHabits());
        lunchRecyclerView.setAdapter(lunchHabitAdapter);
        lunchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        txEvening.setText("Nie je momentálne zvolený žiadny plán. " + habitMainRepository.getEveningHabits().size());
        txEvening.setGravity(50);
        txEvening.setPadding(50, 50, 0, 50);
        txEvening.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

    }

    private void checkPlans(){

            if (getDailyHabits().size() == 0) {
                dailyPlanCardView.removeAllViews();
                dailyPlanCardView.addView(tx);
            } else {
                dailyPlanCardView.removeView(tx);
            }

            if (getMorningHabits().size() == 0) {
                morningPlanCardView.removeAllViews();
                morningPlanCardView.addView(txMorning);
            } else {
                morningPlanCardView.removeView(txMorning);
            }

            if (getLunchHabits().size() == 0) {
                lunchPlanCardView.removeAllViews();
                lunchPlanCardView.addView(txLunch);
            } else {
                lunchPlanCardView.removeView(txLunch);
            }

            if (getEveningHabits().size() == 0) {
                eveningPlanCardView.removeAllViews();
                eveningPlanCardView.addView(txEvening);
            } else {
                eveningPlanCardView.removeView(txEvening);
            }
        }




    @Override
    public void fragmentSwitchToVisible() {

        if (getDailyHabits().size() == 0 || getMorningHabits().size() == 0 || getLunchHabits().size() == 0 || getEveningHabits().size() == 0) {
            checkPlans();
        }

        if (dailyHabits != getDailyHabits().size()){
            setAdapterDaily();
            dailyHabits = getDailyHabits().size();
        };

        if (morningHabits != getMorningHabits().size()){
            setAdapterMorning();
            morningHabits = getMorningHabits().size();
        };

        if (lunchHabits != getLunchHabits().size()){
            setAdapterLunch();
            lunchHabits = getLunchHabits().size();
        }

        if (eveningHabits != getEveningHabits().size()){
            setAdapterEvening();
            eveningHabits = getEveningHabits().size();
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


                txEvening.setVisibility(View.GONE);
                txMorning.setVisibility(View.GONE);
                txLunch.setVisibility(View.GONE);

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

                txEvening.setVisibility(View.VISIBLE);
                txMorning.setVisibility(View.VISIBLE);
                txLunch.setVisibility(View.VISIBLE);

                infoAboutDailyPlan.setText(R.string.infoAboutDailyPlan);
                break;
        }

    }

    public void onResume(){
        super.onResume();
        checkPlans();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.interval_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
