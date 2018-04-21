package running.java.mendelu.cz.bakalarskapraca;

import android.content.ContentValues;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;

/**
 * Created by Monika on 16.04.2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    //SubjectMainRepository subjectMainRepository = new SubjectMainRepository(getActivity());


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_settings);



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
}