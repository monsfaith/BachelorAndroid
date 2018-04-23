package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import eltos.simpledialogfragment.form.Check;
import running.java.mendelu.cz.bakalarskapraca.CheckedImage;
import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 05.03.2018.
 */

public class IconAuthorAdapter extends RecyclerView.Adapter<IconAuthorAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Habit> habits = Collections.emptyList();
    private Context context;


    public IconAuthorAdapter(Context context, List<Habit> habits){
        layoutInflater = LayoutInflater.from(context);
        this.habits = habits;
        this.context = context;


    }
    @Override
    public IconAuthorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_habit_author, parent, false);
        IconAuthorAdapter.MyViewHolder myViewHolder = new IconAuthorAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(IconAuthorAdapter.MyViewHolder holder, int position) {

        Habit habit = habits.get(position);
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(habit.getIcon(), "drawable",
                context.getPackageName());
        Glide.with(context).load(resourceId).override(100,100).into(holder.habitImage);






    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView habitImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitImage = (ImageView) itemView.findViewById(R.id.habitAuthor);


        }
    }
}
