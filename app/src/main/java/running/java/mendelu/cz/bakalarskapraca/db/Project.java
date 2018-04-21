package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;

/**
 * Created by Monika on 17.04.2018.
 */

public class Project {
    static final String TABLE_PROJECT = "project";
    static final String ID = BaseColumns._ID;
    static final String HOUR = "hour";
    static final String MINUTE = "minute";
    static final String NAME = "name";
    static final String TURNON = "turn_on";

    protected ContentValues values; //package protected jsme si to udelali

    public Project(Cursor cursor){
        values = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor,values);
    }

    public Project(int hour, int minute, String name, int turnOn) {
        this.values = new ContentValues();
        this.setName(name);
        this.setHour(hour);
        this.setMinute(minute);
        this.setTurnOn(turnOn);
    }

    public void setName(String name){
        this.values.put(NAME,name);
    }

    public void setHour(int hour){
        this.values.put(HOUR,hour);
    }

    public void setMinute(int minute){
        this.values.put(MINUTE,minute);
    }

    public void setTurnOn(int turnOn){
        this.values.put(TURNON,turnOn);
    }

    public int getHour(){
        return this.values.getAsInteger(HOUR);
    }

    public int getMinute(){
        return this.values.getAsInteger(MINUTE);
    }

    public String getName(){
        return this.values.getAsString(NAME);
    }

    public int getTurnOn(){
        return this.values.getAsInteger(TURNON);
    }


}
