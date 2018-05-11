package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.OneExamDetailActivity;
import running.java.mendelu.cz.bakalarskapraca.R;
import running.java.mendelu.cz.bakalarskapraca.notifications.receivers.ExamNotificationReceiver;

/**
 * Created by Monika on 18.03.2018.
 */

public class ExamNotificationAdapter extends RecyclerView.Adapter<ExamNotificationAdapter.MyViewHolder> {


    private LayoutInflater layoutInflater;
    private Cursor cursor;
    private Context context;
    //spocitava pocet fajok pri skuske
    private int count;
    private boolean zero;
    private List<Exam> exams = Collections.emptyList();
    private List<Exam> checkedExams = new ArrayList<>();

    public ExamNotificationAdapter(Context context, Cursor cursor){
        layoutInflater = LayoutInflater.from(context);
        this.cursor = cursor;
        this.context = context;
        this.count = 0;

    }

    public ExamNotificationAdapter(Context context, List<Exam> exams){
        layoutInflater = LayoutInflater.from(context);
        this.exams = exams;
        this.context = context;
        this.zero = false;


    }


    @Override
    public ExamNotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_exams_notification, parent, false);
        ExamNotificationAdapter.MyViewHolder myViewHolder = new ExamNotificationAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final ExamNotificationAdapter.MyViewHolder holder, int position) {

        /*if (!cursor.moveToPosition(position)) {
            return;
        }*/

        final Exam currentExam = exams.get(position);
        SubjectMainRepository subjectMainRepository = new SubjectMainRepository(context);




        //String subjectName = cursor.getString(cursor.getColumnIndex(Subject.NAME));
        //long examDate = cursor.getLong(cursor.getColumnIndex(Exam.DATE));
        //long examTime = cursor.getLong(cursor.getColumnIndex(Exam.TIME));

        String subjectName = subjectMainRepository.getById(currentExam.getSubjectId()).getName();
        long examDate = currentExam.getDate().getTime();



        holder.subjectName.setText(subjectName);
        int colorSubject = subjectMainRepository.getById(currentExam.getSubjectId()).getColor();
        holder.subjectName.setTextColor(colorSubject);
        holder.examDate.setText(android.text.format.DateFormat.format("dd.MM.yyyy HH:mm", new Date(examDate)));

        //int wantedDays = cursor.getInt(cursor.getColumnIndex(Exam.DAYS));
        //int actualDays = cursor.getInt(cursor.getColumnIndex(Exam.STUDYING));
        int wantedDays = currentExam.getDays();
        int actualDays = currentExam.getStudying();


        holder.examDays.setText(actualDays + "/" + wantedDays);

        holder.examCheck.setHighlightColor(colorSubject);
        holder.examCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            //long idEx = cursor.getLong(cursor.getColumnIndex(Exam.ID));
            //long studyDate = cursor.getLong(cursor.getColumnIndex(Exam.STUDY_DATE));
            //int wantedDays = cursor.getInt(cursor.getColumnIndex(Exam.DAYS));
            //int actualDays = cursor.getInt(cursor.getColumnIndex(Exam.STUDYING));
            long idEx = currentExam.getId();
            long studyDate = currentExam.getStudyDate();
            int wantedDays = currentExam.getDays();
            int actualDays = currentExam.getStudying();

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ExamMainRepository examMainRepository = new ExamMainRepository(context);
                ContentValues contentValues = new ContentValues();

                if (isChecked){
                    /*ContentValues contentValues = new ContentValues();
                    contentValues.put("done",true);
                    holder.habitCheck.setEnabled(false);
                    planMainRepository.updateAssociation(id, contentValues);*/
                    if ((sameDay(studyDate) == false)) {
                        count = count + 1;
                        int studying = examMainRepository.getById(idEx).getStudying();
                        if (zero == false) {
                            contentValues.put("studying", studying + 1);
                            contentValues.put("study_date", System.currentTimeMillis());
                            examMainRepository.update2(idEx, contentValues);
                            checkedExams.add(currentExam);
                            //Toast.makeText(context, "idex " + idEx + "curr " + currentExam.getId() , Toast.LENGTH_SHORT).show();
                        }


                        holder.examDays.setText(actualDays + "/" + wantedDays);

                        //Toast.makeText(context, "checknute" + count, Toast.LENGTH_SHORT).show();
                    } else {
                        count = count + 1;
                    }
                } else {
                    int studying = examMainRepository.getById(idEx).getStudying();
                    if (studying > 0) {
                        contentValues.put("studying", studying - 1);
                    }
                    contentValues.put("study_date",0);
                    examMainRepository.update2(idEx, contentValues);
                    holder.examDays.setText(actualDays + "/" + wantedDays);
                    if (count > 0) {
                        count = count - 1;
                    }
                    //Toast.makeText(context, "nechecknute" + count, Toast.LENGTH_SHORT).show();

                }
            }
        });

        /*if (zero == true){
            if (holder.examCheck.isChecked() == true){
                long idE = currentExam.getId();
                //holder.examCheck.setChecked(false);
                ExamMainRepository examMainRepository = new ExamMainRepository(context);
                ContentValues contentValues = new ContentValues();
                int studying = examMainRepository.getById(idE).getStudying();
                contentValues.put("studying", studying - 1);
                contentValues.put("study_date",0);

            }
        }*/



    }

    public boolean isSelected(){
        return (count>0);

    }


    public void setZero(boolean gotZero){
        this.zero = gotZero;
    }

    public void setNoExams(){
        for (int i = 0; i < checkedExams.size(); i++){
            Exam e = checkedExams.get(i);
            long studyDate = e.getStudyDate();
            ExamMainRepository examMainRepository = new ExamMainRepository(context);
            ContentValues contentValues = new ContentValues();
            int studying = e.getStudying();
            contentValues.put("studying", studying - 1);
            contentValues.put("study_date",0);
            if (studying > 0) {
                examMainRepository.update2(e.getId(), contentValues);
            }


        }
    }

    public int numberOfSelectedExams(){
        return count;
    }

    public Cursor getCursor(int position){
        return this.cursor;
    }

    //skontrolovat ci ide o rovnaky den alebo nie
    private boolean sameDay(long time){
        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarMy = Calendar.getInstance();
        calendarMy.setTimeInMillis(time);
        boolean sameDay = calendarCurrent.get(Calendar.YEAR) == calendarMy.get(Calendar.YEAR) &&
                calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMy.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }


    @Override
    public int getItemCount() {
        return exams.size();
        //return cursor.getCount();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView subjectName;
        TextView examDate;
        CheckBox examCheck;
        TextView examDays;

        public MyViewHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.subjectExam);
            examDate = (TextView) itemView.findViewById(R.id.dateOfExam);
            examCheck = (CheckBox) itemView.findViewById(R.id.examCheckB);
            examDays = (TextView) itemView.findViewById(R.id.examDaysPlanned);


        }


    }
}
