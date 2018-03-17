package running.java.mendelu.cz.bakalarskapraca.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Monika on 13.02.2018.
 */

public class SubjectMainRepository {

    private MainOpenHelper mainOpenHelper;

    public SubjectMainRepository(Context context){
        mainOpenHelper = new MainOpenHelper(context);
    }

    public long insert(Subject subject){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        try {
            return db.insert(Subject.TABLE_SUBJECTS, null, subject.values); //vraci mi to id insertu
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
            return db.update(Subject.TABLE_SUBJECTS, cv, "_id= "+id, null);
        } finally {
            db.close();
        }

    }


    public List<Subject> findAllSubjects(){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            List<Subject> result = new LinkedList<>();
            Cursor c = db.query(Subject.TABLE_SUBJECTS,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null); //dobra metoda pro vsetky selecty, ktore budem mat v appke

            try {

                while (c.moveToNext()) {
                    result.add(new Subject(c));

                }
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }

    }



    public Subject getById(long id){
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        try {
            Subject result = null;
            Cursor c = db.query(Subject.TABLE_SUBJECTS,
                    null,
                    Subject.ID + "= " + id,
                    null,
                    null,
                    null,
                    null);

            try {

                while (c.moveToNext()) {
                    result = new Subject(c);

                }
                return result;

            } finally {
                db.close();
            }

        } finally {
            db.close();
        }




    }


    public int update1(Subject subject, ContentValues cv){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();
        try {
            return db.update(Subject.TABLE_SUBJECTS, cv, Subject.ID + " = " + subject.getID(), null); //vraci mi to id insertu
        } finally {
            db.close();
        }

    }

    public long update2(long id, ContentValues contentValues){
        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.update(Subject.TABLE_SUBJECTS, contentValues, "_id= "+id, null);
        } finally {
            db.close();
        }

    }


    public long delete(long id){


        SQLiteDatabase db = mainOpenHelper.getWritableDatabase();

        try {
            return db.delete(Subject.TABLE_SUBJECTS, "_id =" + id, null);
        } finally {
            db.close();
        }

    }
}
