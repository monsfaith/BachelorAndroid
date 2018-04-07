package running.java.mendelu.cz.bakalarskapraca.db;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Monika on 12.02.2018.
 */

public class ExamMainRepository {

    private MainOpenHelper mainOpenHelper;

    public ExamMainRepository(Context context){
        mainOpenHelper = new MainOpenHelper(context);
    }

    public long insert(Exam exam){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        try {
            return db.insert(Exam.TABLE, null, exam.values); //vraci mi to id insertu
        } finally {
            db.close();
        }

    }

    public MainOpenHelper getMainOpenHelper(){
        return this.mainOpenHelper;

    }

    public long update(long id, String column, String value){
        ContentValues cv = new ContentValues();
        cv.put(column,value);

        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.update(Exam.TABLE, cv, "_id= "+id, null);
        } finally {
            db.close();
        }

    }

    public List<Exam> findAllExams(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Exam> result = new LinkedList<>();
            Cursor c = db.query(Exam.TABLE,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null); //dobra metoda pro vsetky selecty, ktore budem mat v appke

            try {

                while (c.moveToNext()) {
                    result.add(new Exam(c));

                }
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }

    }

    public List<Exam> findDoneExams(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Exam> result = new LinkedList<>();
            Cursor c = db.query(Exam.TABLE,
                    null,
                    Exam.DATE + " < " + System.currentTimeMillis(),
                    null,
                    null,
                    null,
                    null); //dobra metoda pro vsetky selecty, ktore budem mat v appke

            try {

                while (c.moveToNext()) {
                    result.add(new Exam(c));

                }
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }

    }

    public List<Exam> findNextExams(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String actualDate = sdf.format(calendar.getTimeInMillis());

        try {
            List<Exam> result = new LinkedList<>();
            Cursor c = db.query(Exam.TABLE,
                    null,
                    Exam.DATE + " > " + calendar.getTimeInMillis(),
                    null,
                    null,
                    null,
                    null); //dobra metoda pro vsetky selecty, ktore budem mat v appke

            try {

                while (c.moveToNext()) {
                    result.add(new Exam(c));

                }
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }

    }

    public Exam getById(long id){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            Exam result = null;
            Cursor c = db.query(Exam.TABLE,
                    null,
                    Exam.ID + "= " + id,
                    null,
                    null,
                    null,
                    null);

            try {

                while (c.moveToNext()) {
                    result = new Exam(c);

                }
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }




    }

    public Exam getClosestExam(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();

        try {
            Exam result = null;
            Cursor c = db.query(Exam.TABLE,
                    null,
                    Exam.DATE + " < " + calendar.getTimeInMillis(),
                    null,
                    null,
                    null,
                    Exam.DATE + " DESC");

            try {

                while (c.moveToNext()) {
                    result = new Exam(c);

                }
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }




    }



    public int update1(Exam exam, ContentValues cv){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        try {
            return db.update(Exam.TABLE, cv, Exam.ID + " = " + exam.getId(), null); //vraci mi to id insertu
        } finally {
            db.close();
        }

    }

    public long update2(long id, ContentValues contentValues){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.update(Exam.TABLE, contentValues, "_id= "+id, null);
        } finally {
            db.close();
        }

    }

    public long decreaseDays(long id){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        Exam exam = getById(id);
        int days = exam.getDays();
        if (days != 0){
            days--;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("days", days);

        try {
            return db.update(Exam.TABLE, contentValues, "_id= "+id, null);
        } finally {
            db.close();
        }
    }


    public long delete(long id){

        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.delete(Exam.TABLE, "_id =" + id, null);
        } finally {
            db.close();
        }

    }

    public Cursor getExamResults(Date date){
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.HOUR_OF_DAY,23);
        cal1.set(Calendar.MINUTE,59);


        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

            return db.rawQuery("SELECT e.*, s.name, s.color FROM exam e left join subject s on e.subject_id = s._id where e.date > ? and e.date < ?",new String[]{String.valueOf(date.getTime()), String.valueOf(cal1.getTimeInMillis())});
        }


    public Cursor getOtherExamResults(Date date){


        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

            return db.rawQuery("SELECT e.*, s.name, s.color FROM exam e left join subject s on e.subject_id = s._id where e.date > ? order by e.date",new String[]{String.valueOf(date.getTime())});

    }


}
