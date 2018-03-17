package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 05.03.2018.
 */

public class IconHabitAdapter extends RecyclerView.Adapter<IconHabitAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private List<PlanHabitAssociation> habits = Collections.emptyList();


    public IconHabitAdapter(Context context, List<PlanHabitAssociation> habits){
        layoutInflater = LayoutInflater.from(context);
        this.habits = habits;


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

        final int greyPicture = Color.argb(155, 185, 185, 185);

       if (pha.getDone() != true){
            holder.habitImage.setColorFilter(greyPicture, PorterDuff.Mode.SRC_ATOP);

        }


    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView habitImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitImage = (ImageView) itemView.findViewById(R.id.habitOverviewImage);


        }
    }
}
