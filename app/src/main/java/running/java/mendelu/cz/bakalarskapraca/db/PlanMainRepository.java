package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Monika on 01.03.2018.
 */

public class PlanMainRepository {

    private MainOpenHelper mainOpenHelper;

    public PlanMainRepository(Context context){
        mainOpenHelper = new MainOpenHelper(context);
    }

    public long insert(Plan plan){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        try {
            return db.insert(Plan.TABLE_PLANS, null, plan.values);
        } finally {
            db.close();
        }

    }

    public MainOpenHelper getMainOpenHelper(){
        return this.mainOpenHelper;

    }

    public long insertAssociaton(PlanHabitAssociation planHabitAssociation){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        try {
            return db.insert(PlanHabitAssociation.TABLE, null, planHabitAssociation.contentValues);
        } finally {
            db.close();
        }
    }

    public long deleteAssociation(long idPlan, long idHabit){


        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.delete(PlanHabitAssociation.TABLE, "id_habit = " + idHabit + " and id_plan = " + idPlan, null);
        } finally {
            db.close();
        }

    }

    public long deleteAllPlans(){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.delete(Plan.TABLE_PLANS, null, null);
        } finally {
            db.close();
        }

    }


    public Plan getByType(long id){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            Plan result = null;
            Cursor c = db.query(Plan.TABLE_PLANS,
                    null,
                    Plan.TYPE + "= " + id,
                    null,
                    null,
                    null,
                    null);

            try {

                while (c.moveToNext()) {
                    result = new Plan(c);

                }
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }

    }

    public List<Plan> getAllPlans(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Plan> result = new LinkedList<>();
            Cursor c = db.query(Plan.TABLE_PLANS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            try {

                while (c.moveToNext()) {
                    result.add(new Plan(c));

                }
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }

    }



    public PlanHabitAssociation getAssociationById(long id){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            PlanHabitAssociation result = null;
            Cursor c = db.query(PlanHabitAssociation.TABLE,
                    null,
                    PlanHabitAssociation.ID + "= " + id,
                    null,
                    null,
                    null,
                    null);

            try {

                while (c.moveToNext()) {
                    result = new PlanHabitAssociation(c);

                }
                c.close();
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }

    }

    public long update(long id, String column, int value){
        ContentValues cv = new ContentValues();
        cv.put(column,value);

        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.update(Plan.TABLE_PLANS, cv, "_id = "+id, null);
        } finally {
            db.close();
        }

    }

    public long update2(long id, ContentValues contentValues){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.update(Plan.TABLE_PLANS, contentValues, "type = "+id, null);
        } finally {
            db.close();
        }

    }

    public long updateAssociation(long id, ContentValues cv){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        try {
            return db.update(PlanHabitAssociation.TABLE, cv, "_id = " + id, null);
        } finally {
            db.close();
        }

    }


    public long updateAssociation(){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        long today = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        long yesterday = cal.getTimeInMillis();
        ContentValues cv = new ContentValues();
        cv.put("DATE", today);
        cv.put("DONE",false);
        cv.put("CANCEL", false);
        cv.put("REMIND",false);


        //kde je najneskorsi datum
        try {
            if (db.update(PlanHabitAssociation.TABLE, cv, "date = " + yesterday, null) == 0){
                return db.update(PlanHabitAssociation.TABLE, cv, null, null);
            } else {
                return db.update(PlanHabitAssociation.TABLE, cv, "date = " + yesterday, null);
            }
        } finally {
            db.close();
        }

    }

    public long getLastTime(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MINUTE, 01);
            cal.set(Calendar.HOUR_OF_DAY, 00);
            cal.set(Calendar.SECOND, 01);
            cal.set(Calendar.DAY_OF_MONTH, 0);
            cal.set(Calendar.YEAR, 0);
            cal.set(Calendar.MONTH, 0);
            long time = cal.getTimeInMillis();
            Cursor c = db.query(Plan.TABLE_PLANS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    Plan.TO_TIME);

            try {

                while (c.moveToNext()) {
                    time = new Plan(c).getToTime().getTime();
                    if (cal.getTimeInMillis() < time){
                        time = cal.getTimeInMillis();
                    }

                }
                c.close();
                return time;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }
    }

    //metoda na urcenie casu kvoli aktualizacii databaze
    public long getLastPlanTime(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getByType(4).getToTime().getTime());
        return cal.getTimeInMillis();
    }

    //upravit datum planov na aktualny
    public void updatePlanTime(long idPlan){

       if (!sameDay(idPlan)){
           Calendar calendar = Calendar.getInstance();
           ContentValues contentValues = new ContentValues();
           Plan plan = getByType(idPlan);
           calendar.setTime(plan.getFromTime());
           calendar.add(Calendar.DAY_OF_MONTH,1);
           contentValues.put("from_time", calendar.getTimeInMillis());
           calendar.setTime(plan.getToTime());
           calendar.add(Calendar.DAY_OF_MONTH, 1);
           contentValues.put("to_time",calendar.getTimeInMillis());
           update2(idPlan,contentValues);
       }

    }

    //skontrolovat ci ide o rovnaky den alebo nie
    private boolean sameDay(long idPlan){
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTimeInMillis(System.currentTimeMillis());
        Calendar calendarMy = Calendar.getInstance();
        calendarMy.setTime(getByType(idPlan).getFromTime());
        boolean sameDay = calendarCurrent.get(Calendar.YEAR) == calendarMy.get(Calendar.YEAR) &&
                calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMy.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }




    /*public void insertNewAssociation(Date date){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        //cal.before(cal.getTime());


        try {
            db.execSQL("INSERT INTO plan_habit SELECT * FROM plan_habit WHERE plan_habit.date = ?",new String[]{String.valueOf(cal.getTimeInMillis())});
            db.ex

        } finally {
            db.close();
        }

    }*/

}
