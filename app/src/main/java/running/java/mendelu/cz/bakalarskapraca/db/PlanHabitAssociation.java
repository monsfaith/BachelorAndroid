package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.BaseColumns;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Monika on 02.03.2018.
 */

public class PlanHabitAssociation {

    static final String ID_HABIT = "id_habit";
    static final String ID_PLAN = "id_plan";
    static final String TABLE = "plan_habit";
    static final String ID = BaseColumns._ID;
    static final String DONE = "done";
    static final String DATE = "date";
    //static final String REMIND = "remind";
    static final String CANCEL = "cancel";


    protected ContentValues contentValues;

    public PlanHabitAssociation(long idHabit, long idPlan){
        this.contentValues = new ContentValues();
        this.setIdHabit(idHabit);
        this.setIdPlan(idPlan);
        this.setCancel(false);
        this.setDone(false);
        //this.setRemind(false);
        this.setDate(null);
    }

    public PlanHabitAssociation(Cursor cursor){
        contentValues = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor,contentValues);

    }

    public void setIdHabit(long idHabit){
        contentValues.put(ID_HABIT, idHabit);
    }

    public void setIdPlan(long idPlan){
        contentValues.put(ID_PLAN,idPlan);
    }

    public void setDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        contentValues.put(DATE,cal.getTimeInMillis());

    }

    /*public void setRemind(boolean remind){
        int bool = (remind)? 1 : 0;
        contentValues.put(REMIND, bool);
    }*/

    public void setCancel(boolean cancel){
        int bool = (cancel)? 1 : 0;
        contentValues.put(CANCEL,bool);
    }

    public void setDone(boolean done){
        int bool = (done)? 1 : 0;
        contentValues.put(DONE,bool);
    }


    public long getIdHabit(){
        return contentValues.getAsLong(ID_HABIT);
    }

    public long getIdPlan(){
        return contentValues.getAsLong(ID_PLAN);
    }

    public Date getDate(){
        long millisecond = contentValues.getAsLong(DATE);
        return contentValues.get(DATE)!=null ? new Date(millisecond) : null;

    }

    /*public boolean getRemind(){
        return (contentValues.getAsInteger(REMIND) == 1);
    }
*/
    public boolean getCancel(){
        return (contentValues.getAsInteger(CANCEL) == 1);
    }

    public boolean getDone(){
        return (contentValues.getAsInteger(DONE) == 1);
    }

    public long getId() {
        return contentValues.getAsLong(ID);
    }






}
