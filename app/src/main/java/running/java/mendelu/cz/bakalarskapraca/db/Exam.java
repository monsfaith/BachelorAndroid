package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;
import android.view.ViewAnimationUtils;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Monika on 12.02.2018.
 */

public class Exam {

    static final String TABLE = "exam";
    static final String ID = BaseColumns._ID;
    static final String DATE = "date";
    static final String TIME = "time";
    //static final String SUCCESS = "success";
    static final String CLASSROOM = "classroom";
    //static final String TRYON = "tryon";
    static final String DIFFICULTY = "difficulty";
    static final String DAYS = "days";
    //static final String REALIZATION = "realization";
    static final String GRADE = "grade";
    //static final String RATING = "rating";
    static final String NOTE = "note";
    static final String SUBJECT_ID = "subject_id";
    static final String STUDYING = "studying";
    static final String STUDY_DATE =  "study_date";

    //znamka a hodnotenie

    protected ContentValues values; //package protected jsme si to udelali

    public Exam(Date date, Time time, int days, int difficulty, String classroom, long subjectId, String note ){
        this.values = new ContentValues();
        //this.setDate(date);
        this.setTime(time);
        this.setDateTime(date, time);
        this.setDays(days);
        //this.setRealization(false);
        //this.setSuccess(false);
        //this.setTry(1);
        this.setDifficulty(difficulty);
        this.setClassroom(classroom);
        this.setSubjectId(subjectId);
        this.setGrade("");
        //this.setRating(0);
        this.setNote(note);
        this.setStudying(0);
        this.setStudyDate(0);

    }

    public Exam(Cursor cursor){
        values = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor,values);

    }

    public Long getId(){
        return values.getAsLong(ID);
    }

    public Long getSubjectId(){
        return values.getAsLong(SUBJECT_ID);
    }


    public void setTime(Time time)
    {
        values.put(TIME, time.getTime());
    }


    public void setDateTime(Date date, Time time){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(time);
        cal1.set(Calendar.HOUR_OF_DAY,cal2.get(Calendar.HOUR_OF_DAY));
        cal1.set(Calendar.MINUTE,cal2.get(Calendar.MINUTE));
        values.put(DATE, cal1.getTimeInMillis());
    }

    public void setSubjectId(long subjectId)
    {
        values.put(SUBJECT_ID, subjectId);
    }

    public void setClassroom(String classroom)
    {
        values.put(CLASSROOM, classroom);
    }


    public void setDifficulty(int difficulty)
    {
        values.put(DIFFICULTY, difficulty);
    }

    public void setDays(int days)
    {
        values.put(DAYS, days);
    }

    /*public void setRealization(boolean realization)
    {
        int bool = (realization)? 1 : 0;
        values.put(REALIZATION, bool);
    }*/

    public void setGrade(String grade){
        values.put(GRADE,grade);
    }


    public String getGrade(){
        return values.getAsString(GRADE);
    }

    public Date getDate(){
        long millisecond = values.getAsLong(DATE);
        return values.get(DATE)!=null ? new Date(millisecond) : null;

    }

    public Time getTime(){
        long millisecond = values.getAsLong(TIME);
        return values.get(TIME)!=null ? new Time(millisecond) : null;

    }


    /*public boolean getRealization(){
        return (values.getAsInteger(REALIZATION) == 1);
    }*/

    public String getClassroom(){
        return values.getAsString(CLASSROOM);
    }


    public int getDifficulty(){
        return values.getAsInteger(DIFFICULTY);
    }

    public int getDays(){
        return values.getAsInteger(DAYS);
    }


    public void setNote(String note) {
        values.put(NOTE,note);
    }

    public String getNote(){
       return values.getAsString(NOTE);
    }

    public void setStudying(int studying) {
        this.values.put(STUDYING,studying);
    }

    public int getStudying(){
        return values.getAsInteger(STUDYING);
    }

    public long getStudyDate(){
        long millisecond = values.getAsLong(STUDY_DATE);
        return values.get(STUDY_DATE)!=null ? (millisecond) : 0;
    }

    public void setStudyDate(long milliseconds){
        values.put(STUDY_DATE,milliseconds);
    }
}
