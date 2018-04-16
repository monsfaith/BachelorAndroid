package running.java.mendelu.cz.bakalarskapraca;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Created by Monika on 17.04.2018.
 */

public class TimePickerPreference extends DialogPreference {

    private int time;
    private int dialogLayout = R.layout.dialog_pref_time;

    public TimePickerPreference(Context context) {
        this(context, null);
    }

    public TimePickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public TimePickerPreference(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public TimePickerPreference(Context context, AttributeSet attrs,
                                int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
        //ulozili sme do sharedpref
        persistInt(time);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // Default value from attribute. Fallback value is set to 0.
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
                                     Object defaultValue) {
        // Read the value. Use the default value if it is not possible.
        setTime(restorePersistedValue ?
                getPersistedInt(time) : (int) defaultValue);
    }

    @Override
    public int getDialogLayoutResource() {
        return dialogLayout;
    }
}
