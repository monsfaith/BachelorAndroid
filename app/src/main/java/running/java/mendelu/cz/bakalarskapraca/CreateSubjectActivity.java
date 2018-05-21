package running.java.mendelu.cz.bakalarskapraca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import eltos.simpledialogfragment.SimpleDialog;
import eltos.simpledialogfragment.color.SimpleColorDialog;
import eltos.simpledialogfragment.list.CustomListDialog;
import running.java.mendelu.cz.bakalarskapraca.db.Subject;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;

public class CreateSubjectActivity extends AppCompatActivity implements SimpleDialog.OnDialogResultListener {

    private static final String COLOR_DIALOG = "color";
    //private Button subjectCreateButton;
    private EditText subjectNameInput;
    private FloatingActionButton subjectShortcutInput;
    private SubjectMainRepository subjectMainRepository;
    public static final String SUB_ID = "subId";
    private Context context;
    private int subjectColor;
    private SimpleColorDialog simpleColorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);
        //subjectCreateButton = (Button) findViewById(R.id.createSubBtn);
        subjectNameInput = (EditText) findViewById(R.id.subjectNameInput);
        subjectShortcutInput = (FloatingActionButton) findViewById(R.id.subjectShortcutInput);
        subjectMainRepository = new SubjectMainRepository(this);
        context = this;
        subjectColor = 0;


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subjectShortcutInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });


    }

    private void createDialog(){
        simpleColorDialog = SimpleColorDialog.build();
                simpleColorDialog.title("Vyber si rozlišovaciu farbu predmetu")
                .colorPreset(Color.RED)
                .allowCustom(false)
                .show(this, COLOR_DIALOG);
    }




    public void createSubject(MenuItem item) {
        String subjectName = subjectNameInput.getText().toString();
        if (subjectName.trim().length() == 0){
            subjectNameInput.setError("Nutné vyplniť");
        }
        //String subjectShortcut = subjectShortcutInput.getText().toString().toUpperCase();
        if (subjectColor == 0){
            Toast.makeText(this,"Nie je zvolená farba", Toast.LENGTH_SHORT).show();
        } else {
            Subject sub = new Subject(subjectName, subjectColor);
            Long lastInsertedId = subjectMainRepository.insert(sub);

           // Toast.makeText(CreateSubjectActivity.this, "" + lastInsertedId + " id " + subjectColor, Toast.LENGTH_LONG).show();

            Intent i = new Intent();
            i.putExtra(SUB_ID, lastInsertedId);
            setResult(RESULT_OK, i);
            finish();
        }
    }


        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_create_sub, menu);
        return true;
    }

    @Override
    public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
        if (which == BUTTON_POSITIVE && COLOR_DIALOG.equals(dialogTag)){;
            subjectColor = extras.getInt(SimpleColorDialog.COLOR);
            subjectShortcutInput.setImageResource(R.drawable.ic_edit_white_24dp);
            subjectShortcutInput.setBackgroundTintList(ColorStateList.valueOf(subjectColor));
            //subjectShortcutInput.setBackgroundResource(R.drawable.ic_done_black_24dp);
            //Toast.makeText(this, subjectColor + "", Toast.LENGTH_LONG).show();
            return true;
        } else {
            //subjectColor = 0;
            return false;
        }
    }
}
