package running.java.mendelu.cz.bakalarskapraca;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.ExamNotificationReceiver;

/**
 * Created by Monika on 17.04.2018.
 */

//https://medium.com/@JakobUlbrich/building-a-settings-screen-for-android-part-3-ae9793fd31ec

public class TimePickerPreferenceDialog extends PreferenceDialogFragmentCompat {
    private TimePicker timePicker;
    //lebo sa tam nachadza aj Project
    private SubjectMainRepository subjectMainRepository;

    public static TimePickerPreferenceDialog newInstance(
            String key) {
        final TimePickerPreferenceDialog
                fragment = new TimePickerPreferenceDialog();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        timePicker = (TimePicker) view.findViewById(R.id.edit);
        subjectMainRepository = new SubjectMainRepository(getContext());

        // Exception when there is no TimePicker
        if (timePicker == null) {
            throw new IllegalStateException("No dialog here");
        }

        // Get the time from the related Preference
        Integer minutesAfterMidnight = null;
        DialogPreference preference = getPreference();
        if (preference instanceof TimePickerPreference) {
            minutesAfterMidnight =
                    ((TimePickerPreference) preference).getTime();
        }

        // Set the time to the TimePicker
        if (minutesAfterMidnight != null) {
            int hours = minutesAfterMidnight / 60;
            int minutes = minutesAfterMidnight % 60;
            boolean is24hour = DateFormat.is24HourFormat(getContext());

            timePicker.setIs24HourView(is24hour);
            timePicker.setCurrentHour(hours);
            timePicker.setCurrentMinute(minutes);
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int hours = timePicker.getCurrentHour();
            int minutes = timePicker.getCurrentMinute();
            int minutesAfterMidnight = (hours * 60) + minutes;
            ContentValues contentValues = new ContentValues();
            contentValues.put("minute",minutes);
            contentValues.put("hour",hours);
            subjectMainRepository.updateProject(1,contentValues);
            setExamNotification();

            // Get the related Preference and save the value
            DialogPreference preference = getPreference();
            if (preference instanceof TimePickerPreference) {
                TimePickerPreference timePreference =
                        ((TimePickerPreference) preference);
                // This allows the client to ignore the user value.
                if (timePreference.callChangeListener(
                        minutesAfterMidnight)) {
                    // Save the value
                    timePreference.setTime(minutesAfterMidnight);
                }
            }
        }
    }

    private void setExamNotification(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
            /*contentValues.put("enabled",false);
            planMainRepository.update2(1, contentValues);*/

        //calendar.add(Calendar.MINUTE, 1);

        //nova cast, podla mna
        //calendar.set(Calendar.MINUTE,00);
        //calendar.set(Calendar.HOUR_OF_DAY,8);

        //po upraveni settings
        calendar.set(Calendar.MINUTE,subjectMainRepository.getProjectById(1).getMinute());
        calendar.set(Calendar.HOUR_OF_DAY,subjectMainRepository.getProjectById(1).getHour());
        if (System.currentTimeMillis() > calendar.getTimeInMillis()){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }

        Intent i = new Intent(getActivity(), ExamNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 500, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(getContext(),"prestavene na " + subjectMainRepository.getProjectById(1).getHour() + " " + subjectMainRepository.getProjectById(1).getMinute(), Toast.LENGTH_SHORT).show();

    }
}
