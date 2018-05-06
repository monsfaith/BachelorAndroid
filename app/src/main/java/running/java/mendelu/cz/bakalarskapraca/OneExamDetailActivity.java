package running.java.mendelu.cz.bakalarskapraca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;

public class OneExamDetailActivity extends AppCompatActivity {

    private TextView subjectTextView;
    private TextView dateTextView;
    //private TextView timeTextView;
    private TextView daysTextView;
    private TextView classroomTextView;
    private TextView noteTextView;
    private TextView quoteTextView;
    private ImageView image;
    private Exam intentExam;
    private ExamMainRepository examMainRepository;
    private SubjectMainRepository subjectMainRepository;
    private CardView cardViewQuote;
    private long examId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_exam_detail);
        subjectTextView = (TextView) findViewById(R.id.examSubjectDetail);
        dateTextView = (TextView) findViewById(R.id.examDateDetail);
        //timeTextView = (TextView) findViewById(R.id.examTimeDetail);
        daysTextView = (TextView) findViewById(R.id.examDaysDetail);
        classroomTextView = (TextView) findViewById(R.id.examClassroomDetail);
        noteTextView = (TextView) findViewById(R.id.examNoteDetail);
        quoteTextView = (TextView) findViewById(R.id.examQuoteDetail);
        image = (ImageView) findViewById(R.id.examDetailImage);
        cardViewQuote = (CardView) findViewById(R.id.cardViewDetailQuote);
        examMainRepository = new ExamMainRepository(this);
        subjectMainRepository = new SubjectMainRepository(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null) {
            setIntentExtras();
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
        inflater.inflate(R.menu.menu_edit_exam, menu);
        return true;
    }

    public void editExam(MenuItem item) {
        Intent i = new Intent(this, CreateExamActivity.class);
        i.putExtra("UPDATE_ID",examId);
        startActivity(i);

    }

    private void setIntentExtras(){
        Intent i = getIntent();
        intentExam = examMainRepository.getById(i.getExtras().getLong("ID"));
        examId = intentExam.getId();
      //  Toast.makeText(this, "Id: " + examId, Toast.LENGTH_LONG).show();
        subjectTextView.setText(subjectMainRepository.getById(intentExam.getSubjectId()).getName());
        subjectTextView.setTextColor(subjectMainRepository.getById(intentExam.getSubjectId()).getColor());
        dateTextView.setText(android.text.format.DateFormat.format("dd.MM.yyyy", intentExam.getDate()).toString() + ", " + android.text.format.DateFormat.format("HH:mm", intentExam.getTime()).toString() + " hod. ");
        //timeTextView.setText(android.text.format.DateFormat.format("HH:mm", intentExam.getTime()).toString() + " hod") ;
        daysTextView.setText(String.valueOf(intentExam.getStudying() + "/" + intentExam.getDays()));
        classroomTextView.setText(String.valueOf(intentExam.getClassroom()));
        noteTextView.setText(String.valueOf(intentExam.getNote()));

    }

    public void onResume(){
        super.onResume();
        setIntentExtras();
    }

}
