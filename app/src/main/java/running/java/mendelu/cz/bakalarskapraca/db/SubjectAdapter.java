package running.java.mendelu.cz.bakalarskapraca.db;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 13.02.2018.
 */

public class SubjectAdapter extends CursorAdapter {

    public SubjectAdapter(Context context) {

        super(context, openCursor(context), true);
    }

    private static Cursor openCursor(Context context){
        MainOpenHelper mainOpenHelper = new MainOpenHelper(context);
        SQLiteDatabase db = mainOpenHelper.getReadableDatabase();

        return db.query(Subject.TABLE_SUBJECTS,
                null,
                null,
                null,
                null,
                null,
                Subject.NAME
        );
        //neuzavira spojeni do databaze ani kurzor, o to sa postara ListView
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.row_subject_layout, null, true);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.nameOfSubject = (TextView) view.findViewById(R.id.nameOfSubject);
        viewHolder.colorOfSubject = (ImageView) view.findViewById(R.id.colorOfSubject);

        view.setTag(viewHolder); //tam si ten uakzatel ulozim do znacky
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Subject subject = new Subject(cursor);
        ViewHolder viewHolder  = (ViewHolder) view.getTag(); //tu to vytiahnu z toho radku

        viewHolder.nameOfSubject.setText(String.valueOf(subject.getName()));
        //plus este ci je ukonceny

    }

    private static class ViewHolder {

        public TextView nameOfSubject;
        public ImageView colorOfSubject;
    }
}
