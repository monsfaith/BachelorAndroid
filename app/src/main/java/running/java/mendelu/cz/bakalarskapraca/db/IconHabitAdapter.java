package running.java.mendelu.cz.bakalarskapraca.db;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.CheckedImage;
import running.java.mendelu.cz.bakalarskapraca.MainOverviewFragment;
import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 05.03.2018.
 */

public class IconHabitAdapter extends RecyclerView.Adapter<IconHabitAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private List<PlanHabitAssociation> habits = Collections.emptyList();
    private Context context;
    private AlertDialog.Builder myBuilder;
    private AlertDialog habitDialog;
    private CheckBox check;
    private MainOverviewFragment mainOverviewFragment;





    public IconHabitAdapter(Context context, List<PlanHabitAssociation> habits, MainOverviewFragment mainOverviewFragment){
        layoutInflater = LayoutInflater.from(context);
        this.habits = habits;
        this.context = context;
        this.mainOverviewFragment = mainOverviewFragment;


    }
    @Override
    public IconHabitAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_overview_plan_habit, parent, false);
        IconHabitAdapter.MyViewHolder myViewHolder = new IconHabitAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final IconHabitAdapter.MyViewHolder holder, int position) {

        final PlanHabitAssociation pha = habits.get(position);
        HabitMainRepository habitMainRepository = new HabitMainRepository(context);
        Habit habit = habitMainRepository.getById(pha.getIdHabit());
        holder.habitImage.setImageDrawable(getResources(habit.getIcon()));
        final long id = pha.getIdHabit();
        final long idPha = pha.getId();

        //final int greyPicture = Color.argb(175, 204, 204, 0);

       if (pha.getDone() == true){
            //holder.habitImage.setColorFilter(greyPicture, PorterDuff.Mode.SRC_ATOP);
            holder.habitImage.setChecked(true);

        } else {
           holder.habitImage.setChecked(false);
       }

        holder.habitImage.setOnClickListener(new View.OnClickListener() {


            HabitMainRepository habitMainRepository = new HabitMainRepository(context);
            PlanMainRepository planMainRepository = new PlanMainRepository(context);

            @Override
            public void onClick(View v) {
                myBuilder = new AlertDialog.Builder(context);
                View view = layoutInflater.inflate(R.layout.dialog_habit_main_info, null);
                TextView habitInfoName = (TextView) view.findViewById(R.id.habitNameInfo);
                TextView habitInfoDescription = (TextView) view.findViewById(R.id.habitShortDescription);
                check = (CheckBox) view.findViewById(R.id.checkHabitMain);
                TextView done = (TextView) view.findViewById(R.id.doneActivity);

                habitInfoName.setText(habitMainRepository.getById(id).getName());
                habitInfoDescription.setText(habitMainRepository.getById(id).getShortDescription());

                if (pha.getDone()){
                    check.setVisibility(View.GONE);
                    done.setVisibility(View.GONE);
                }

                myBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (check.isChecked()){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("done",true);
                            contentValues.put("date",System.currentTimeMillis());
                            planMainRepository.updateAssociation(idPha,contentValues);
                            Toast.makeText(context,"vzkonane" + pha.getDone(),Toast.LENGTH_SHORT).show();
                            mainOverviewFragment.loadListView();
                        }
                    }
                });


                myBuilder.setView(view);
                myBuilder.setNegativeButton("Zrušiť", null);
                habitDialog = myBuilder.create();
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

        CheckedImage habitImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            habitImage = (CheckedImage) itemView.findViewById(R.id.habitOverviewImage);


        }
    }
}
