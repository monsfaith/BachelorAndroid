package running.java.mendelu.cz.bakalarskapraca;

import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

/**
 * Created by Monika on 17.04.2018.
 */

//https://medium.com/@JakobUlbrich/building-a-settings-screen-for-android-part-3-ae9793fd31ec

public class TimePickerPreferenceDialog extends PreferenceDialogFragmentCompat {
    private TimePicker timePicker;

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

        // Exception when there is no TimePicker
        if (timePicker == null) {
            throw new IllegalStateException("Dialog view must contain" +
                    " a TimePicker with id 'edit'");
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
}
