package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import javax.xml.transform.Result;

import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 22.02.2018.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder>{

    private LayoutInflater layoutInflater;
    private List<Exam> exams = Collections.emptyList();
    private List<Subject> subjects = Collections.emptyList();
    private Cursor cursor;


    public ResultAdapter(Context context, List<Exam> exams){
        layoutInflater = LayoutInflater.from(context);
        this.exams = exams;


    }

    public ResultAdapter(Context context, Cursor cursor){
        layoutInflater = LayoutInflater.from(context);
        this.cursor = cursor;

    }

    @Override
    public ResultAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_exam_results, parent, false);
        ResultAdapter.MyViewHolder myViewHolder = new ResultAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(ResultAdapter.MyViewHolder holder, int position) {

        /*Exam currentExam = exams.get(position);
        Subject currentSubject = subjects.get(position);

        holder.examName.setText(Long.toString(currentExam.getSubjectId()));
        holder.examType.setText(currentExam.getClassroom());
        holder.examGrade.setText(Integer.toString(currentExam.getDays()));
        holder.examSatisfaction.setNumStars(4);
        //holder.habitImage.setImageResource(currentHabit.getIconId());*/

        if (!cursor.moveToPosition(position)) {
            return;
        }

        String subjectName = cursor.getString(cursor.getColumnIndex(Subject.NAME));
        int examTry = cursor.getInt(cursor.getColumnIndex(Exam.TRYON));

        holder.examName.setText(subjectName);
        holder.examTry.setText(Integer.toString(examTry));


    }

    @Override
    public int getItemCount() {

        //return exams.size();
        return cursor.getCount();
    }

    public void getAwayCursor(Cursor cursorNew){
        if (this.cursor != null){
            this.cursor.close();
        }

        this.cursor = cursorNew;

        if (cursorNew != null){
            notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView examName;
        TextView examType;
        TextView examGrade;
        TextView examTry;
        RatingBar examSatisfaction;
        ImageView examImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            examName = (TextView) itemView.findViewById(R.id.examName);
            examType = (TextView) itemView.findViewById(R.id.examType);
            examGrade = (TextView) itemView.findViewById(R.id.concreteGrade);
            examTry = (TextView) itemView.findViewById(R.id.concreteTry);
            examSatisfaction = (RatingBar) itemView.findViewById(R.id.examRatingBar);
            examImage = (ImageView) itemView.findViewById(R.id.subjectColoricon);


        }
    }
}
