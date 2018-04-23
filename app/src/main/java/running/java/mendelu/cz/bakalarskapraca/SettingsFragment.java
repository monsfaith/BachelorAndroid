package running.java.mendelu.cz.bakalarskapraca;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import java.util.Calendar;

import running.java.mendelu.cz.bakalarskapraca.db.Plan;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.CancelEveningHabitNotificationReceiver;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.EveningHabitNotificationReceiver;

/**
 * Created by Monika on 16.04.2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private android.support.v14.preference.SwitchPreference switchPreference;
    private PlanMainRepository planMainRepository;

    //SubjectMainRepository subjectMainRepository = new SubjectMainRepository(getActivity());


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_settings);

        switchPreference = (android.support.v14.preference.SwitchPreference) findPreference("turn_notif"); //Preference Key



    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("turn_notif")) {
            boolean notif = sharedPreferences.getBoolean("turn_notif", true);
            //Do whatever you want here. This is an example.
            if (notif) {
                setDailyPlan(getActivity());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean notif = preferences.getBoolean("turn_notif", false);

        if (notif) {
            setDailyPlan(getActivity());
        }
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Try if the preference is one of our custom Preferences
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePickerPreference) {
            // Create a new instance of TimePreferenceDialogFragment with the key of the related
            // Preference
            dialogFragment = TimePickerPreferenceDialog
                    .newInstance(preference.getKey());
        }

        // If it was one of our cutom Preferences, show its dialog
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(),
                    "Zle je");
        }
        else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void setDailyPlan(Context context){
        PlanMainRepository planMainRepository = new PlanMainRepository(context);
        //ContentValues contentValues = new ContentValues();
        //contentValues.put("enabled",true);
        planMainRepository = new PlanMainRepository(context);
        //planMainRepository.update2(1,contentValues);
        Plan dailyPlan = planMainRepository.getByType(1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,dailyPlan.getFromHour());
        calendar.set(Calendar.MINUTE,dailyPlan.getFromMinute());

        Calendar to = Calendar.getInstance();
        to.set(Calendar.HOUR_OF_DAY,dailyPlan.getToHour());
        to.set(Calendar.MINUTE,dailyPlan.getToMinute());
        if (System.currentTimeMillis() < calendar.getTimeInMillis()) {
            setHabitNotification(calendar.getTimeInMillis(), context, to.getTimeInMillis());
        } else if (System.currentTimeMillis() < to.getTimeInMillis()) {
            setHabitNotification(System.currentTimeMillis(), context, to.getTimeInMillis());
        } else {
            to.add(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            setHabitNotification(calendar.getTimeInMillis(), context, to.getTimeInMillis());
        }
    }


    private void setHabitNotification(long time, Context context, long toTime){
        PlanMainRepository planMainRepository = new PlanMainRepository(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, EveningHabitNotificationReceiver.class);
        i.putExtra("REQUESTCODE",1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, planMainRepository.getByType(1).getRepetition(), pendingIntent);

        Intent cancelIntent = new Intent(context, CancelEveningHabitNotificationReceiver.class);
        cancelIntent.putExtra("CANCEL",1);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 100, cancelIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, toTime, cancelPendingIntent);
    }
}