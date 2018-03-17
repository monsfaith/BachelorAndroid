package running.java.mendelu.cz.bakalarskapraca;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Monika on 11.02.2018.
 */

public class PlanActAdapter extends BaseAdapter {

    private final Context mContext;
    private final Integer[] planHabits;

    //zmenit integer potom na triedu PlanHabit
    public PlanActAdapter(Context context, Integer[] planHabits){
        this.planHabits = planHabits;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return planHabits.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(mContext);
        textView.setText(String.valueOf(position));
        return textView;
    }
}
