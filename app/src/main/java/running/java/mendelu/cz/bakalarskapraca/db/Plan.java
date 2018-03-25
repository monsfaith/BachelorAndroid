package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Monika on 01.03.2018.
 */

public class Plan {

    static final String TABLE_PLANS = "plan";
    static final String ID = BaseColumns._ID;
    static final String FROM_TIME = "from_time";
    static final String TO_TIME = "to_time";
    static final String REPETITION_ID = "repetition_id";
    //static final String REMIND = "remind";
    static final String DATE = "date";
    //static final String NAME = "name";
    static final String TYPE = "type";
    //static final String NOTIFICATION = "notification";
    static final String ENABLED = "enabled";
    static final String TO_HOUR = "to_hour";
    static final String FROM_HOUR = "from_hour";
    static final String TO_MINUTE = "to_minute";
    static final String FROM_MINUTE = "from_minute";

    protected ContentValues values;

    public Plan(Cursor cursor){
        values = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor,values);

    }

    public Plan(long fromTime, long toTime, int type, boolean enabled, int fromHour, int toHour, int fromMinute, int toMinute, long repetition){
        this.values = new ContentValues();
        this.setFromTime(fromTime);
        this.setToTime(toTime);
        this.setType(type);
        //this.setDate(date);
        this.setRepetitionId(repetition);
        //this.setNotification(false);
        this.setEnabled(enabled);
        this.setFromHour(fromHour);
        this.setToHour(toHour);
        this.setFromMinute(fromMinute);
        this.setToMinute(toMinute);

    }

    /*public void setNotification (boolean notification){
        int bool = (notification)? 1 : 0;
        this.values.put(NOTIFICATION,bool);
    }*/

    /*public boolean getNotification(){
        return (values.getAsInteger(NOTIFICATION) == 1);
    }*/

    /*public void setName(String name){
        this.values.put(NAME,name);
    }*/

    public void setFromTime(long time){
        this.values.put(FROM_TIME,time);
    }

    public void setToTime(long time){
        this.values.put(TO_TIME,time);
    }

    public void setRepetitionId(long repetitionId){
        this.values.put(REPETITION_ID,repetitionId);
    }

    /*public void setRemind(boolean remind){
        int bool = (remind)? 1 : 0;
        this.values.put(REMIND,bool);
    }*/

    /*public boolean getRemind(){
        return (values.getAsInteger(REMIND) == 1);
    }*/

    public void setType(int type){
        this.values.put(TYPE,type);
    }

    public void setToHour(int toHour){
        this.values.put(TO_HOUR,toHour);
    }

    public void setFromHour(int fromHour){
        this.values.put(FROM_HOUR,fromHour);
    }

    public void setToMinute(int toMinute){
        this.values.put(TO_MINUTE,toMinute);
    }

    public void setFromMinute(int fromMinute){
        this.values.put(FROM_MINUTE,fromMinute);
    }


    public int getType(){
        return values.getAsInteger(TYPE);
    }

    public int getFromHour(){
        return values.getAsInteger(FROM_HOUR);
    }

    public int getToHour(){
        return values.getAsInteger(TO_HOUR);
    }

    public int getToMinute(){
        return values.getAsInteger(TO_MINUTE);
    }

    public int getFromMinute(){
        return values.getAsInteger(FROM_MINUTE);
    }



    public void setDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        this.values.put(DATE,cal.getTimeInMillis());

    }

    /*public String getName(){
        return values.getAsString(NAME);
    }*/

    public Date getDate(){
        long millisecond = values.getAsLong(DATE);
        return values.get(DATE)!=null ? new Date(millisecond) : null;

    }

    public Time getFromTime(){
        long millisecond = values.getAsLong(FROM_TIME);
        return values.get(FROM_TIME)!=null ? new Time(millisecond) : null;

    }

    public Time getToTime(){
        long millisecond = values.getAsLong(TO_TIME);
        return values.get(TO_TIME)!=null ? new Time(millisecond) : null;

    }

    public long getId(){
        return values.getAsLong(ID);
    }


    public long getRepetition(){
        return values.getAsLong(REPETITION_ID);
    }


    public void setEnabled(boolean enabled) {
        int bool = (enabled)? 1 : 0;
        this.values.put(ENABLED,bool);
    }



    public boolean getEnabled(){
        return (values.getAsInteger(ENABLED) == 1);
    }
}
