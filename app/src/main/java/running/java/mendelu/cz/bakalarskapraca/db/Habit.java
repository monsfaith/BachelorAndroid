package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.media.Image;
import android.provider.BaseColumns;
import android.widget.ImageView;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Monika on 21.02.2018.
 */

public class Habit {

    static final String TABLE_HABITS = "habit";
    static final String ID = BaseColumns._ID;
    static final String NAME = "name";
    static final String DESCRIPTION = "description";
    //static final String REPETITION_ID = "repetition_id";
    static final String SHORT_DESCRIPTION = "short_description";
    //static final String REMIND = "remind"; //neurobit to, ale odlozit, boolean
    //static final String CANCEL = "cancel"; //dnes uz nepripominat
    static final String DONE = "done";
    //static final String DATE = "date";
    //static final String TIME = "time";
    static final String ICON = "icon";
    static final String PLAN = "plan";
    //static final String ASSOCIATON_TABLE_ID = "associaton_table_id";
    //static final String AUTHOR = "author";

    protected ContentValues values;

    /*public Habit(Date date, Time time, String name, String description, int repetitionId, boolean remind, boolean cancel, boolean done, int associatonTableId, String icon, int plan){
        this.values = new ContentValues();
        this.setDate(date);
        this.setTime(time);
        this.setName(name);
        this.setDescription(description);
        this.setRepetitionId(repetitionId);
        this.setRemind(remind);
        this.setCancel(cancel);
        this.setDone(done);
        this.setAssociationTableId(associatonTableId);
        this.setIcon(icon);
        this.setPlan(plan);
    }*/

    /*public Habit(String name, String description){
        this.values = new ContentValues();
        this.setName(name);
        this.setDescription(description);
        this.setRepetitionId(1);
        this.setRemind(false);
        this.setCancel(false);
        this.setDone(true);
        this.setAssociationTableId(5);
    }*/

    public Habit(Cursor cursor){
        values = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor,values);

    }

    public Habit(String name, String description, String shortDescription, String icon, int plan){
        this.values = new ContentValues();
        this.setName(name);
        this.setDescription(description);
        this.setDone(false);
        //this.setRemind(true);
        //this.setCancel(false);
        //this.setAssociationTableId(0);
        //this.setRepetitionId(2);
        this.setIcon(icon);
        this.setPlan(plan);
        this.setShortDescription(shortDescription);
        //this.setAuthor(author);
    }

    public void setIcon(String icon) {
        values.put(ICON,icon);
    }

    public String getIcon(){
        return values.getAsString(ICON);
    }

    public void setPlan(int plan){
        values.put(PLAN,plan);
    }

    public int getPlan(){
        return values.getAsInteger(PLAN);
    }

    public void setShortDescription(String shortDescription){
        values.put(SHORT_DESCRIPTION, shortDescription);
    }

    public String getShortDescription(){
        return values.getAsString(SHORT_DESCRIPTION);
    }


    /*public void setDate(Date date) {
        values.put(DATE, date.getTime());
    }

    public void setTime(Time time){
        values.put(TIME,time.getTime());
    }*/

    public void setName(String name){
        values.put(NAME,name);
    }

    /*public void setAuthor(String name){
        values.put(AUTHOR,name);
    }
*/

    public void setDescription(String description){
        values.put(DESCRIPTION,description);
    }

    //public void setRepetitionId(int repetitionId){
        //values.put(REPETITION_ID,repetitionId);
    //}

    /*public void setRemind(boolean remind){
        int bool = (remind)? 1 : 0;
        values.put(REMIND, bool);
    }

    public void setCancel(boolean cancel){
        int bool = (cancel)? 1 : 0;
        values.put(CANCEL,bool);
    }*/

    public void setDone(boolean done){
        int bool = (done)? 1 : 0;
        values.put(DONE,bool);
    }


    //public void setAssociationTableId(int association){
    //    values.put(ASSOCIATON_TABLE_ID,association);
    //}

    /*public Date getDate(){
        long millisecond = values.getAsLong(DATE);
        return values.get(DATE)!=null ? new Date(millisecond) : null;

    }

    public Time getTime(){
        long millisecond = values.getAsLong(TIME);
        return values.get(TIME)!=null ? new Time(millisecond) : null;

    }*/

    public String getName(){
        return values.getAsString(NAME);
    }

    /*public String getAuthor(){
        return values.getAsString(AUTHOR);
    }*/


    public String getDescription(){
        return values.getAsString(DESCRIPTION);
    }

    //public int getRepetitionId(){
        //return values.getAsInteger(REPETITION_ID);
    //}

    /*public boolean getRemind(){
        return (values.getAsInteger(REMIND) == 1);
    }

    public boolean getCancel(){
        return (values.getAsInteger(CANCEL) == 1);
    }*/

    public boolean getDone(){
        return (values.getAsInteger(DONE) == 1);
    }

    //public int getAssociationTableId() {
        //return values.getAsInteger(ASSOCIATON_TABLE_ID);
    //}

    public long getId() {
        return values.getAsLong(ID);
    }
}
