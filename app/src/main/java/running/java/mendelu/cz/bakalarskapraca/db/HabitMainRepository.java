package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Monika on 22.02.2018.
 */

public class HabitMainRepository {

    private MainOpenHelper mainOpenHelper;

    public HabitMainRepository(Context context){
        mainOpenHelper = new MainOpenHelper(context);
    }

    public long insert(Habit habit){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        try {
            return db.insert(Habit.TABLE_HABITS, null, habit.values); //vraci mi to id insertu
        } finally {
            db.close();
        }

    }

    public MainOpenHelper getMainOpenHelper(){
        return this.mainOpenHelper;

    }

    public long update(long id, String column, int value){
        ContentValues cv = new ContentValues();
        cv.put(column,value);

        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.update(Habit.TABLE_HABITS, cv, "_id= "+id, null);
        } finally {
            db.close();
        }

    }


    public List<PlanHabitAssociation> getDailyPlanHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<PlanHabitAssociation> result = new LinkedList<>();

            //Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 1 and plan_habit.date = ? ", new String[]{String.valueOf(getCurrentDate())});

            Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 1"
                    , null);

           // Cursor c = db.rawQuery("SELECT habit.* FROM habit right join plan_habit on habit._id = plan_habit.id_habit where plan_habit.id_plan = 1", null);


            try {

                while (c.moveToNext()) {
                    result.add(new PlanHabitAssociation(c));

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

    public int getDoneDailyPlanHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();


        try {
            int result = 0;

            //Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 2 and plan_habit.date = ? ", new String[]{String.valueOf(getCurrentDate())});

            Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 1 AND plan_habit.done = 1 AND plan_habit.date BETWEEN " + todayBeginning() + " AND " + todayEnd(), null);

            // Cursor c = db.rawQuery("SELECT habit.* FROM habit right join plan_habit on habit._id = plan_habit.id_habit where plan_habit.id_plan = 1", null);


            try {

                while (c.moveToNext()) {
                    result = result + 1;

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

    public List<Habit> getAllHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Habit> result = new LinkedList<>();
            Cursor c = db.query(Habit.TABLE_HABITS,
                    null,
                    Habit.PLAN + " = " + 1,
                    null,
                    null,
                    null,
                    null);

            try {

                while (c.moveToNext()) {
                    result.add(new Habit(c));

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

    public List<Habit> getAllMorningHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Habit> result = new LinkedList<>();
            Cursor c = db.query(Habit.TABLE_HABITS,
                    null,
                    Habit.PLAN + " = " + 2 + " OR " + Habit.PLAN + " = " + 1,
                    null,
                    null,
                    null,
                    null);


            try {

                while (c.moveToNext()) {
                    result.add(new Habit(c));

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

    public List<Habit> getAllLunchHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Habit> result = new LinkedList<>();
            Cursor c = db.query(Habit.TABLE_HABITS,
                    null,
                    Habit.PLAN + " = " + 3 + " OR " + Habit.PLAN + " = " + 1,
                    null,
                    null,
                    null,
                    null);


            try {

                while (c.moveToNext()) {
                    result.add(new Habit(c));

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

    public List<Habit> getAllEveningHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Habit> result = new LinkedList<>();
            Cursor c = db.query(Habit.TABLE_HABITS,
                    null,
                    Habit.PLAN + " = " + 4 + " OR " + Habit.PLAN + " = " + 1,
                    null,
                    null,
                    null,
                    null);


            try {

                while (c.moveToNext()) {
                    result.add(new Habit(c));

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

    public List<Habit> getAllDoneHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Habit> result = new LinkedList<>();
            Cursor c = db.query(Habit.TABLE_HABITS,
                    null,
                    Habit.DONE + "  = " + 1,
                    null,
                    null,
                    null,
                    null);

            try {

                while (c.moveToNext()) {
                    result.add(new Habit(c));

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


    public List<PlanHabitAssociation> getMorningPlanHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();


        try {
            List<PlanHabitAssociation> result = new LinkedList<>();

            //Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 2 and plan_habit.date = ? ", new String[]{String.valueOf(getCurrentDate())});

            Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 2", null);

            // Cursor c = db.rawQuery("SELECT habit.* FROM habit right join plan_habit on habit._id = plan_habit.id_habit where plan_habit.id_plan = 1", null);


            try {

                while (c.moveToNext()) {
                    result.add(new PlanHabitAssociation(c));

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

    public int getDoneMorningPlanHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();


        try {
            int result = 0;

            //Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 2 and plan_habit.date = ? ", new String[]{String.valueOf(getCurrentDate())});

            Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 2 AND plan_habit.done = 1 AND plan_habit.date BETWEEN " + todayBeginning() + " AND " + todayEnd(), null);

            // Cursor c = db.rawQuery("SELECT habit.* FROM habit right join plan_habit on habit._id = plan_habit.id_habit where plan_habit.id_plan = 1", null);


            try {

                while (c.moveToNext()) {
                    result = result + 1;

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


    public List<PlanHabitAssociation> getLunchPlanHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();


        try {
            List<PlanHabitAssociation> result = new LinkedList<>();

            //Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 3 and plan_habit.date = ? ", new String[]{String.valueOf(getCurrentDate())});

            Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 3", null);

            // Cursor c = db.rawQuery("SELECT habit.* FROM habit right join plan_habit on habit._id = plan_habit.id_habit where plan_habit.id_plan = 1", null);


            try {

                while (c.moveToNext()) {
                    result.add(new PlanHabitAssociation(c));
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

    public int getDoneLunchPlanHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();


        try {
            int result = 0;

            //Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 2 and plan_habit.date = ? ", new String[]{String.valueOf(getCurrentDate())});

            Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 3 AND plan_habit.done = 1 AND plan_habit.date BETWEEN " + todayBeginning() + " AND " + todayEnd(), null);

            // Cursor c = db.rawQuery("SELECT habit.* FROM habit right join plan_habit on habit._id = plan_habit.id_habit where plan_habit.id_plan = 1", null);


            try {

                while (c.moveToNext()) {
                    result = result + 1;

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


    public List<PlanHabitAssociation> getEveningPlanHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<PlanHabitAssociation> result = new LinkedList<>();

            //Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 4 and plan_habit.date = ? ", new String[]{String.valueOf(getCurrentDate())});

            Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 4", null);

            try {

                while (c.moveToNext()) {
                    result.add(new PlanHabitAssociation(c));

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

    public int getDoneEveningPlanHabits(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();


        try {
            int result = 0;

            //Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 2 and plan_habit.date = ? ", new String[]{String.valueOf(getCurrentDate())});

            Cursor c = db.rawQuery("SELECT plan_habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = 4 AND plan_habit.done = 1 AND plan_habit.date BETWEEN " + todayBeginning() + " AND " + todayEnd(), null);

            // Cursor c = db.rawQuery("SELECT habit.* FROM habit right join plan_habit on habit._id = plan_habit.id_habit where plan_habit.id_plan = 1", null);


            try {

                while (c.moveToNext()) {
                    result = result + 1;

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

    public Habit getById(long id){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            Habit result = null;
            Cursor c = db.query(Habit.TABLE_HABITS,
                    null,
                    Habit.ID + " = " + id,
                    null,
                    null,
                    null,
                    null);

            try {

                while (c.moveToNext()) {
                    result = new Habit(c);

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


    public long update1(Habit habit, ContentValues cv){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        try {
            return db.update(Habit.TABLE_HABITS, cv, Habit.ID + " = " + habit.getId(), null); //vraci mi to id insertu
        } finally {
            db.close();
        }

    }

    public long update2(long id, ContentValues contentValues){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.update(Habit.TABLE_HABITS, contentValues, "_id= "+id, null);
        } finally {
            db.close();
        }

    }


    public long delete(long id){


        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.delete(Habit.TABLE_HABITS, "_id =" + id, null);
        } finally {
            db.close();
        }

    }

    public long deleteAll(){


        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.delete(Habit.TABLE_HABITS,null,null);
        } finally {
            db.close();
        }

    }

    public boolean isInPlan(long idHabit, long idPlan){

        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Habit> result = new LinkedList<>();

            Cursor c = db.rawQuery("SELECT habit.* FROM plan_habit left join habit on plan_habit.id_habit = habit._id where plan_habit.id_plan = ? and plan_habit.id_habit = ? " , new String[]{String.valueOf(idPlan), String.valueOf(idHabit)});

            // Cursor c = db.rawQuery("SELECT habit.* FROM habit right join plan_habit on habit._id = plan_habit.id_habit where plan_habit.id_plan = 1", null);


            try {

                while (c.moveToNext()) {
                    result.add(new Habit(c));

                }
                c.close();
                return (result.size() > 0);

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }


    }

    private long todayBeginning(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,20);
        return calendar.getTimeInMillis();
    }

    private long todayEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,55);
        return calendar.getTimeInMillis();
    }
}
