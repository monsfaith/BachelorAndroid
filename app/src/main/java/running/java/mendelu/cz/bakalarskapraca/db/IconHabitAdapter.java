package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.CheckedImage;
import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 05.03.2018.
 */

public class IconHabitAdapter extends RecyclerView.Adapter<IconHabitAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private List<PlanHabitAssociation> habits = Collections.emptyList();
    private Context context;


    public IconHabitAdapter(Context context, List<PlanHabitAssociation> habits){
        layoutInflater = LayoutInflater.from(context);
        this.habits = habits;
        this.context = context;


    }
    @Override
    public IconHabitAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_overview_plan_habit, parent, false);
        IconHabitAdapter.MyViewHolder myViewHolder = new IconHabitAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(IconHabitAdapter.MyViewHolder holder, int position) {

        PlanHabitAssociation pha = habits.get(position);
        HabitMainRepository habitMainRepository = new HabitMainRepository(context);
        Habit habit = habitMainRepository.getById(pha.getIdHabit());
        holder.habitImage.setImageDrawable(getResources(habit.getIcon()));


        //final int greyPicture = Color.argb(175, 204, 204, 0);

       if (pha.getDone() == true){
            //holder.habitImage.setColorFilter(greyPicture, PorterDuff.Mode.SRC_ATOP);
            holder.habitImage.setChecked(true);

        } else {
           holder.habitImage.setChecked(false);
       }




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

        CheckedImage habitImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitImage = (CheckedImage) itemView.findViewById(R.id.habitOverviewImage);


        }
    }
}
