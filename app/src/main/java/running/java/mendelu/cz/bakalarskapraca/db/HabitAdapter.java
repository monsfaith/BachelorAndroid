package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 21.02.2018.
 */

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PlanHabitAssociation planHabit);
    }

    private LayoutInflater layoutInflater;
    private List<PlanHabitAssociation> habits = Collections.emptyList();
    private Context context;
    private OnItemClickListener listener;


    public HabitAdapter(Context context, List<PlanHabitAssociation> habits){
        layoutInflater = LayoutInflater.from(context);
        this.habits = habits;
        this.context = context;


    }

    public HabitAdapter(Context context, List<PlanHabitAssociation> habits, OnItemClickListener listener){
        layoutInflater = LayoutInflater.from(context);
        this.habits = habits;
        this.context = context;
        this.listener = listener;


    }



    private boolean sameDay(long habitDate){
        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarMy = Calendar.getInstance();
        calendarMy.setTimeInMillis(habitDate);
        boolean sameDay = calendarCurrent.get(Calendar.YEAR) == calendarMy.get(Calendar.YEAR) &&
                calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMy.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_daily_habit_plan, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        PlanHabitAssociation currentAssociation = habits.get(position);
        final HabitMainRepository habitMainRepository = new HabitMainRepository(context);
        PlanMainRepository planMainRepository = new PlanMainRepository(context);

        if (listener != null){
            holder.bind(habits.get(position), listener);
        }

        if ((currentAssociation.getDone() == true) && (currentAssociation.getDate() != null)){
            if (sameDay(currentAssociation.getDate().getTime())){
                holder.habitCheck.setChecked(true);
                //holder.habitCheck.setEnabled(false);

            } else {
                holder.habitCheck.setEnabled(true);
                ContentValues contentValues = new ContentValues();
                contentValues.put("done",false);
                planMainRepository.updateAssociation(currentAssociation.getId(), contentValues);


            }
        } else {
            holder.habitCheck.setChecked(false);
        }

        Habit currentHabit = habitMainRepository.getById(currentAssociation.getIdHabit());
        holder.habitName.setText(currentHabit.getName());
        holder.habitDescription.setText(currentHabit.getShortDescription());
        holder.habitImage.setImageDrawable(getResources(currentHabit.getIcon()));


        final long id = currentAssociation.getId();

        holder.habitCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PlanMainRepository planMainRepository = new PlanMainRepository(context);

                if (isChecked){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("done",true);
                    YoYo.with(Techniques.Swing)
                            .duration(500)
                            .repeat(1)
                            .playOn(holder.habitImage);
                    contentValues.put("date",System.currentTimeMillis());
                    planMainRepository.updateAssociation(id, contentValues);

                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("done",false);
                    planMainRepository.updateAssociation(id, contentValues);
                }
            }
        });



        //holder.habitImage.setImageResource(currentHabit.getIconId());


    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    private Drawable getResources(String name){
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(name, "drawable",
                context.getPackageName());
        return resources.getDrawable(resourceId, null);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView habitName;
        TextView habitDescription;
        CheckBox habitCheck;
        ImageView habitImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitName = (TextView) itemView.findViewById(R.id.habitName);
            habitDescription = (TextView) itemView.findViewById(R.id.habitDescription);
            habitCheck = (CheckBox) itemView.findViewById(R.id.habitCheck);
            habitImage = (ImageView) itemView.findViewById(R.id.habitImage);


        }

        public void bind(final PlanHabitAssociation planHabitAssociation, final OnItemClickListener listener) {
            habitCheck.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(planHabitAssociation);
                }
            });
        }
    }

}

