package running.java.mendelu.cz.bakalarskapraca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import running.java.mendelu.cz.bakalarskapraca.db.Subject;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;

public class CreateSubjectActivity extends AppCompatActivity {

    //private Button subjectCreateButton;
    private EditText subjectNameInput;
    private EditText subjectShortcutInput;
    private SubjectMainRepository subjectMainRepository;
    public static final String SUB_ID = "subId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);
        //subjectCreateButton = (Button) findViewById(R.id.createSubBtn);
        subjectNameInput = (EditText) findViewById(R.id.subjectNameInput);
        subjectShortcutInput = (EditText) findViewById(R.id.subjectShortcutInput);
        subjectMainRepository = new SubjectMainRepository(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    /*public void newSubjectCreation(View v){
        String subjectName = subjectNameInput.getText().toString();
        String subjectShortcut = subjectShortcutInput.getText().toString().toUpperCase();
        Subject sub = new Subject(subjectName,subjectShortcut);
        Long lastInsertedId = subjectMainRepository.insert(sub);

        Toast.makeText(CreateSubjectActivity.this,"" + lastInsertedId + "id",Toast.LENGTH_LONG).show();

        Intent i = new Intent();
        i.putExtra(SUB_ID, lastInsertedId);
        setResult(RESULT_OK,i);
        finish();
    }*/

    public void createSubject(MenuItem item) {
        String subjectName = subjectNameInput.getText().toString();
        String subjectShortcut = subjectShortcutInput.getText().toString().toUpperCase();
        Subject sub = new Subject(subjectName,subjectShortcut);
        Long lastInsertedId = subjectMainRepository.insert(sub);

        Toast.makeText(CreateSubjectActivity.this,"" + lastInsertedId + "id",Toast.LENGTH_LONG).show();

        Intent i = new Intent();
        i.putExtra(SUB_ID, lastInsertedId);
        setResult(RESULT_OK,i);
        finish();
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
}
