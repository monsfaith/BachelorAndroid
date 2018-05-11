package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Monika on 13.02.2018.
 */

public class Subject {

    static final String TABLE_SUBJECTS = "subject";
    static final String ID = BaseColumns._ID;
    static final String COLOR = "color";
    static final String NAME = "name";

    protected ContentValues values; //package protected jsme si to udelali

    public Subject(Cursor cursor){
        values = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor,values);
    }

    public Subject(String name, int color) {
        this.values = new ContentValues();
        this.setName(name);
        this.setColor(color);
    }

    public void setName(String name) {
        values.put(NAME,name);
    }

    public String getName(){
        return values.getAsString(NAME);
    }

    public long getID(){
        return values.getAsLong(ID);
    }

    public void setColor(int color) {
        values.put(COLOR,color);
    }

    public int getColor(){
        return values.getAsInteger(COLOR);
    }


}
