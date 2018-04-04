package running.java.mendelu.cz.bakalarskapraca.db;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.CreateExamActivity;
import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 27.02.2018.
 */

public class HabitToChooseAdapter extends RecyclerView.Adapter<HabitToChooseAdapter.MyViewHolder>{

    private LayoutInflater layoutInflater;
    private List<Habit> habits = Collections.emptyList();
    private Context context;
    private int idOfPlan;



    public HabitToChooseAdapter(Context context, List<Habit> habits, int idOfPlan){
        layoutInflater = LayoutInflater.from(context);
        this.habits = habits;
        this.context = context;
        this.idOfPlan = idOfPlan;


    }
    @Override
    public HabitToChooseAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_habits_grid, parent, false);
        HabitToChooseAdapter.MyViewHolder myViewHolder = new HabitToChooseAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(HabitToChooseAdapter.MyViewHolder holder, int position) {

        Habit currentHabit = habits.get(position);

        holder.habitName.setText(currentHabit.getName());
        final Long id = currentHabit.getId();
        holder.habitImage.setImageDrawable(getResources(currentHabit.getIcon()));


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {

            HabitMainRepository habitMainRepository = new HabitMainRepository(context);
            PlanMainRepository planMainRepository = new PlanMainRepository(context);

            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
                View view = layoutInflater.inflate(R.layout.dialog_habit_info, null);
                TextView habitInfoName = (TextView) view.findViewById(R.id.habitInfoName);
                TextView habitInfoDescription = (TextView) view.findViewById(R.id.habitInfoDescription);

                habitInfoName.setText(habitMainRepository.getById(id).getName());
                habitInfoDescription.setText(habitMainRepository.getById(id).getDescription());

                if (habitMainRepository.isInPlan(id, idOfPlan)){
                    myBuilder.setPositiveButton("Odobrať", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                planMainRepository.deleteAssociation(idOfPlan, id);
                                Toast.makeText(context, "Odobrané z plánu " + idOfPlan, Toast.LENGTH_SHORT).show();

                        }

                    });
                } else {

                    myBuilder.setPositiveButton("Pridať", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                PlanHabitAssociation pha = new PlanHabitAssociation(id, idOfPlan);
                                planMainRepository.insertAssociaton(pha);
                                Toast.makeText(context, "Pridané do plánu " + idOfPlan, Toast.LENGTH_SHORT).show();
                        }

                    });
                }

                myBuilder.setView(view);
                myBuilder.setNegativeButton("Zrušiť", null);
                AlertDialog habitDialog = myBuilder.create();
                habitDialog.show();

            }
        });

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
        ImageView habitImage;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitName = (TextView) itemView.findViewById(R.id.habitToChooseTextView);
            habitImage = (ImageView) itemView.findViewById(R.id.habitToChooseImage);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutHabitClick);

        }
    }
}
