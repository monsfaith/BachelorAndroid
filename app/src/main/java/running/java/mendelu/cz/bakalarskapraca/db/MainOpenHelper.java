package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Monika on 12.02.2018.
 */

public class MainOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "main.db";
    private static final int DATABASE_VERSION = 62;

    public MainOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Exam.TABLE + " (" +
                Exam.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Exam.DATE + " INTEGER NOT NULL, " +
                Exam.TIME + " INTEGER NOT NULL, " +
                //Exam.SUCCESS + " INTEGER NOT NULL, " +
                Exam.CLASSROOM + " TEXT, " +
                //Exam.TRYON + " INTEGER NOT NULL, " +
                Exam.STUDYING + " INTEGER NOT NULL, " +
                Exam.DIFFICULTY + " INTEGER NOT NULL, " +
                Exam.DAYS + " INTEGER NOT NULL, " +
                Exam.SUBJECT_ID + " INTEGER NOT NULL, " +
                Exam.GRADE + " TEXT, " +
                Exam.STUDY_DATE + " INTEGER, " +
                Exam.NOTE + " TEXT, " +
                Exam.REALIZATION + " INTEGER NOT NULL " + ")");
        db.execSQL("CREATE TABLE " + Subject.TABLE_SUBJECTS + " (" +
                Subject.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Subject.COLOR + " INTEGER NOT NULL, " +
                Subject.NAME + " TEXT NOT NULL" + ")");
        db.execSQL("CREATE TABLE " + Habit.TABLE_HABITS + " (" +
                Habit.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Habit.NAME + " TEXT NOT NULL, " +
                Habit.DESCRIPTION + " TEXT, " +
                Habit.SHORT_DESCRIPTION + " TEXT, " +
                //Habit.CANCEL + " INTEGER, " +
                Habit.DONE + " INTEGER NOT NULL, " +
                Habit.ASSOCIATON_TABLE_ID + " INTEGER, " +
                //Habit.DATE + " INTEGER, " +
                //Habit.REMIND + " INTEGER, " +
                Habit.REPETITION_ID + " INTEGER NOT NULL, " +
                Habit.ICON + " TEXT NOT NULL, " +
                Habit.PLAN + " INTEGER NOT NULL" + ")");
        db.execSQL("CREATE TABLE " + Plan.TABLE_PLANS + " (" +
                Plan.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                //Plan.NAME + " TEXT, " +
                //Plan.DATE + " INTEGER NOT NULL, " +
                Plan.FROM_TIME + " INTEGER NOT NULL, " +
                Plan.TO_TIME + " INTEGER NOT NULL, " +
                Plan.FROM_HOUR + " INTEGER NOT NULL, " +
                Plan.TO_HOUR + " INTEGER NOT NULL, " +
                Plan.FROM_MINUTE + " INTEGER NOT NULL, " +
                Plan.TO_MINUTE + " INTEGER NOT NULL, " +
                Plan.TYPE + " INTEGER NOT NULL, " +
                //Plan.NOTIFICATION + " INTEGER NOT NULL, " +
                Plan.ENABLED + " INTEGER NOT NULL, " +
                Plan.REPETITION_ID + " INTEGER NOT NULL " + ")");
                //Plan.REMIND + " INTEGER " + ")");
        db.execSQL("CREATE TABLE " + PlanHabitAssociation.TABLE + " (" +
                PlanHabitAssociation.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PlanHabitAssociation.ID_HABIT + " INTEGER NOT NULL, " +
                PlanHabitAssociation.DONE + " INTEGER NOT NULL, " +
                PlanHabitAssociation.DATE + " INTEGER NOT NULL, " +
                //PlanHabitAssociation.REMIND + " INTEGER NOT NULL, " +
                //PlanHabitAssociation.CANCEL + " INTEGER NOT NULL, " +
                PlanHabitAssociation.ID_PLAN + " INTEGER NOT NULL " + ")");

    }


   // PlanHabitAssociation static final String REPETITION = "repetition";




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Exam.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Subject.TABLE_SUBJECTS);//toto je pre nas vyvojarska varianta, takto to nasadit na Googel je zle
        db.execSQL("DROP TABLE IF EXISTS " + Habit.TABLE_HABITS);
        db.execSQL("DROP TABLE IF EXISTS " + Plan.TABLE_PLANS);
        db.execSQL("DROP TABLE IF EXISTS " + PlanHabitAssociation.TABLE);


        onCreate(db);

    }

}
