package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Date;

import running.java.mendelu.cz.bakalarskapraca.CreateExamActivity;
import running.java.mendelu.cz.bakalarskapraca.OneExamDetailActivity;

import static running.java.mendelu.cz.bakalarskapraca.R.*;

/**
 * Created by Monika on 12.02.2018.
 */

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.MyViewHolder>{

    private LayoutInflater layoutInflater;
    private Cursor cursor;
    private Context context;
    private String examIdTest;

    public ExamAdapter(Context context, Cursor cursor){
        layoutInflater = LayoutInflater.from(context);
        this.cursor = cursor;
        this.context = context;

    }

    @Override
    public ExamAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(layout.row_daily_exam_layout, parent, false);
        ExamAdapter.MyViewHolder myViewHolder = new ExamAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(ExamAdapter.MyViewHolder holder, int position) {

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
        String examClassroom = cursor.getString(cursor.getColumnIndex(Exam.CLASSROOM));
        long examDate = cursor.getLong(cursor.getColumnIndex(Exam.DATE));
        long studyDate = cursor.getLong(cursor.getColumnIndex(Exam.STUDY_DATE));
        long examTime = cursor.getLong(cursor.getColumnIndex(Exam.TIME));
        String examNote = cursor.getString(cursor.getColumnIndex(Exam.NOTE));

        holder.subjectName.setText(subjectName);
        holder.examDate.setText(android.text.format.DateFormat.format("dd.MM.yyyy HH:mm", new Date(examDate)) + ", " + (android.text.format.DateFormat.format("HH:mm", new Time(examTime))));
        holder.examDetails.setText((examClassroom +  ", " + examNote + ", " + studyDate));

        holder.examEdit.setOnClickListener(new View.OnClickListener() {
            long examId = cursor.getLong(cursor.getColumnIndex(Exam.ID));
            @Override
            public void onClick(View v) {
                Toast.makeText(context, examId + "fungzzuje?", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, OneExamDetailActivity.class);
                        i.putExtra("ID", examId);
                        context.startActivity(i);

                    }
                });
    }


    public Cursor getCursor(int position){
        return this.cursor;
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView subjectName;
        TextView examDetails;
        TextView examDate;
        ImageView examIcon;
        ImageButton examEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(id.subjectName);
            examDetails = (TextView) itemView.findViewById(id.examDetails);
            examIcon = (ImageView) itemView.findViewById(id.iconExam);
            examEdit = (ImageButton) itemView.findViewById(id.examEdit);
            examDate = (TextView) itemView.findViewById(id.examDateTime);


        }

    }
}
