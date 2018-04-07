package running.java.mendelu.cz.bakalarskapraca.db;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.util.Freezable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
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
    private Context context;


    public ResultAdapter(Context context, List<Exam> exams){
        layoutInflater = LayoutInflater.from(context);
        this.exams = exams;
        this.context = context;


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
    public void onBindViewHolder(final ResultAdapter.MyViewHolder holder, int position) {

        Exam currentExam = exams.get(position);
        SubjectMainRepository subjectMainRepository = new SubjectMainRepository(context);
        final long id = currentExam.getId();

        if (holder.examAddGrade.getVisibility() == View.VISIBLE){
            holder.examAddGrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_grade_picker, null);
                    ContentValues contentValues = new ContentValues();
                    Button bA = (Button) view.findViewById(R.id.gradeA);
                    Button bB = (Button) view.findViewById(R.id.gradeB);
                    Button bC = (Button) view.findViewById(R.id.gradeC);
                    Button bD = (Button) view.findViewById(R.id.gradeD);
                    Button bE = (Button) view.findViewById(R.id.gradeE);
                    Button bF = (Button) view.findViewById(R.id.gradeF);
                    Button b1 = (Button) view.findViewById(R.id.grade1);
                    Button b2 = (Button) view.findViewById(R.id.grade2);
                    Button b3 = (Button) view.findViewById(R.id.grade3);
                    Button b4 = (Button) view.findViewById(R.id.grade4);
                    Button b5 = (Button) view.findViewById(R.id.grade5);

                    myBuilder.setNegativeButton("Zrušiť", null);

                    myBuilder.setView(view);
                    AlertDialog dialog = myBuilder.create();
                    dialog.show();

                    setOnClickListener("A", bA, holder, id, dialog);
                    setOnClickListener("B", bB, holder, id, dialog);
                    setOnClickListener("C", bC, holder, id, dialog);
                    setOnClickListener("D", bD, holder, id, dialog);
                    setOnClickListener("E", bE, holder, id, dialog);
                    setOnClickListener("F", bF, holder, id, dialog);
                    setOnClickListener("1", b1, holder, id, dialog);
                    setOnClickListener("2", b2, holder, id, dialog);
                    setOnClickListener("3", b3, holder, id, dialog);
                    setOnClickListener("4", b4, holder, id, dialog);
                    setOnClickListener("5", b5, holder, id, dialog);
                }
            });
        }

        holder.examName.setText(subjectMainRepository.getById(currentExam.getSubjectId()).getName());
        if (currentExam.getGrade().trim().length() == 0){
            holder.examGrade.setVisibility(View.GONE);
            holder.examAddGrade.setVisibility(View.VISIBLE);
        } else {
            holder.examGrade.setText(currentExam.getGrade());
            holder.examGrade.setVisibility(View.VISIBLE);
            holder.examAddGrade.setVisibility(View.GONE);
        }


        if (currentExam.getGrade().equals("A") || currentExam.getGrade().equals("1")){
            //int greyPicture = Color.argb(150, 204, 204, 0);
            int yellow = R.color.yellow_300;
            holder.examImage.setColorFilter(yellow, PorterDuff.Mode.SRC_ATOP);
        }
        if (currentExam.getGrade().equals("B") || currentExam.getGrade().equals("2")) {
            int yellow = R.color.yellow_600;
            holder.examImage.setColorFilter(yellow, PorterDuff.Mode.SRC_ATOP);
        }
        if (currentExam.getGrade().equals("C") || currentExam.getGrade().equals("3")){
            int yellow = R.color.orange_400;
            holder.examImage.setColorFilter(yellow, PorterDuff.Mode.SRC_ATOP);
        }
        if (currentExam.getGrade().equals("D") || currentExam.getGrade().equals("E") || currentExam.getGrade().equals("4")){
            int yellow = R.color.orange_700;
            holder.examImage.setColorFilter(yellow, PorterDuff.Mode.SRC_ATOP);
        }
        if (currentExam.getGrade().equals("F") || currentExam.getGrade().equals("5")){
            int yellow = R.color.red_700;
            holder.examImage.setColorFilter(yellow, PorterDuff.Mode.SRC_ATOP);
        }
        holder.examDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(currentExam.getDate()));

        //holder.habitImage.setImageResource(currentHabit.getIconId());*/

        /*if (!cursor.moveToPosition(position)) {
            return;
        }

        String subjectName = cursor.getString(cursor.getColumnIndex(Subject.NAME));
        long examDate = cursor.getLong(cursor.getColumnIndex(Exam.DATE));
        String examGrade = cursor.getString(cursor.getColumnIndex(Exam.GRADE));

        if (examGrade == ""){

        }

        holder.examName.setText(subjectName);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        holder.examDate.setText(sdf.format(examDate));*/


    }

    private void setOnClickListener(final String grade, Button button, final ResultAdapter.MyViewHolder holder, final long id, final AlertDialog dialog){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.examGrade.setText(grade);
                holder.examGrade.setVisibility(View.VISIBLE);
                holder.examAddGrade.setVisibility(View.GONE);
                ContentValues contentValues = new ContentValues();
                contentValues.put("grade",grade);
                ExamMainRepository examMainRepository = new ExamMainRepository(context);
                examMainRepository.update2(id,contentValues);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {

        return exams.size();
        //return cursor.getCount();
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
        TextView examGrade;
        TextView examDate;
        ImageView examImage;
        ImageButton examAddGrade;

        public MyViewHolder(View itemView) {
            super(itemView);
            examName = (TextView) itemView.findViewById(R.id.examName);
            examGrade = (TextView) itemView.findViewById(R.id.concreteGrade);
            examDate = (TextView) itemView.findViewById(R.id.examDateResult);
            examImage = (ImageView) itemView.findViewById(R.id.subjectColoricon);
            examAddGrade = (ImageButton) itemView.findViewById(R.id.addExamResult);


        }
    }
}
