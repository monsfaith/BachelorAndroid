package running.java.mendelu.cz.bakalarskapraca.db;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import running.java.mendelu.cz.bakalarskapraca.CreateExamActivity;
import running.java.mendelu.cz.bakalarskapraca.R;

/**
 * Created by Monika on 13.02.2018.
 */

public class SubjectAdapter extends CursorAdapter {

    private SubjectMainRepository subjectMainRepository;

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
        viewHolder.colorOfSubject = (Button) view.findViewById(R.id.colorOfSubject);
        //viewHolder.subjectVisibility = (ImageButton) view.findViewById(R.id.subjectVisibility);
        subjectMainRepository = new SubjectMainRepository(context);

        view.setTag(viewHolder); //tam si ten uakzatel ulozim do znacky
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Subject subject = new Subject(cursor);
        ViewHolder viewHolder  = (ViewHolder) view.getTag(); //tu to vytiahnu z toho radku

        viewHolder.nameOfSubject.setText(String.valueOf(subject.getName()));
        int colorSubject = subject.getColor();
        viewHolder.colorOfSubject.getBackground().setColorFilter(new LightingColorFilter(colorSubject,0));
        /*viewHolder.subjectVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Skryť predmet?").setMessage("Chceš skutočne skryť predmet?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("hidden",true);
                        subjectMainRepository.update2(subject.getID(),contentValues);

                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.updateSubjectLists();
                        dialog.dismiss();

                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
            }
        });*/


        //plus este ci je ukonceny

    }

    private static class ViewHolder {

        public TextView nameOfSubject;
        public Button colorOfSubject;
        //public ImageButton subjectVisibility;
    }
}
