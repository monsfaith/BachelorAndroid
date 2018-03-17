package running.java.mendelu.cz.bakalarskapraca;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Subject;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.SubjectMainRepository;

public class CreateExamActivity extends AppCompatActivity {

    private ExamMainRepository examMainRepository;
    private ListView listViewSubjects;
    private SubjectMainRepository subjectMainRepository;
    private SubjectAdapter subjectAdapter;
    private Button createNewSubjectButt;
    //private Button chosenSubject;
    private EditText chosenSubject;
    private EditText chosenDate;
    private EditText chosenTime;
    private EditText chosenDays;
    //private EditText chosenDifficulty;
    private SeekBar seekbarDifficulty;
    private int progressSeekbar;
    private Button addExamButton;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private long milliseconds;
    private long timeMilliseconds;
    private EditText noteInput;
    private EditText classroomInput;
    private long finalSubject;
    private long createdSubjectId;
    private AlertDialog subjectDialog;
    private final long today = System.currentTimeMillis() - 1000 - 86400000;
    private Exam intentExam;
    private DatePickerDialog datePickerDialog;
    private int days;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam);
        chosenSubject = (EditText) findViewById(R.id.chosenSubject);
        chosenDate = (EditText) findViewById(R.id.chosenDate);
        chosenTime = (EditText) findViewById(R.id.chosenTime);
        addExamButton = (Button) findViewById(R.id.addExamButton);
        chosenDays = (EditText) findViewById(R.id.chosenDays);
        seekbarDifficulty = (SeekBar) findViewById(R.id.difficultySeekBar);
        classroomInput = (EditText) findViewById(R.id.classroomInput);
        noteInput = (EditText) findViewById(R.id.noteInput);
        subjectMainRepository = new SubjectMainRepository(CreateExamActivity.this);
        examMainRepository = new ExamMainRepository(this);

        Toast.makeText(this, "Pocet subjektov: " + subjectMainRepository.findAllSubjects().size() + examMainRepository.findAllExams().size(), Toast.LENGTH_LONG).show();

        seekbarDifficulty.setEnabled(false);
        chosenDays.setEnabled(false);
        chosenDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                seekbarDifficulty.setEnabled(true);
                chosenDays.setEnabled(true);
                seekbarDifficulty.setMax(7);
                //seekbarDifficulty.setProgress(1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        seekbarDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progressSeekbar = progress;
                int days;

                if ((setMaxSeekBar() / 7) * progress < 1) {
                    seekbarDifficulty.setMax(setMaxSeekBar());
                    days = progress;
                } else {
                    days = (setMaxSeekBar() / 7) * progress;
                }
                chosenDays.setText("" + days);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        if (isUpdateMode()) {
            Intent i = getIntent();
            intentExam = examMainRepository.getById(i.getExtras().getLong("UPDATE_ID"));
            Toast.makeText(this, "Id: " + getIntent(), Toast.LENGTH_LONG).show();
            chosenSubject.setText(subjectMainRepository.getById(intentExam.getSubjectId()).getName());
            milliseconds = intentExam.getDate().getTime();
            timeMilliseconds = intentExam.getTime().getTime();
            chosenDate.setText(android.text.format.DateFormat.format("dd.MM.yyyy", intentExam.getDate()).toString());
            chosenTime.setText(android.text.format.DateFormat.format("HH:mm", intentExam.getTime()).toString());
            //chosenDifficulty.setText(String.valueOf(intentExam.getDifficulty()));
            chosenDays.setText(String.valueOf(intentExam.getDays()));
            classroomInput.setText(String.valueOf(intentExam.getClassroom()));
            noteInput.setText(String.valueOf(intentExam.getNote()));


        }
    }

    private boolean isUpdateMode() {
        return (getIntent().getExtras() != null);
    }


    private int setMaxSeekBar() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Calendar c = Calendar.getInstance();
            String todayString = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);

            String myStringDate = chosenDate.getText().toString();

            Date myDate = new Date(sdf.parse(myStringDate).getTime());
            Date today = new Date(sdf.parse(todayString).getTime());

            long diff = Math.abs(today.getTime() - myDate.getTime());
            long dateDiff = diff / (24 * 60 * 60 * 1000);

            String str = String.valueOf(dateDiff);

            return Integer.valueOf(str);

        } catch (Exception exception) {
            Log.e("nefunguje", "exception " + exception);
            return 0;
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


    public void choseSubject(View view) {
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_subject, null);

        listViewSubjects = (ListView) view.findViewById(R.id.listViewSub);
        createNewSubjectButt = (Button) view.findViewById(R.id.createNewSubjectButton);
        subjectAdapter = new SubjectAdapter(this);


        myBuilder.setTitle("Vybrat predmet");
        myBuilder.setAdapter(subjectAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Long id = subjectAdapter.getItemId(item);
                finalSubject = id;
                Subject sub = subjectMainRepository.getById(id);
                chosenSubject.setText(sub.getName());
                subjectAdapter.notifyDataSetChanged();
            }
        });


        myBuilder.setView(mView);
        myBuilder.setNegativeButton("Zrušiť", null);
        /*AlertDialog dialog = myBuilder.create();
        dialog.show();*/
        subjectDialog = myBuilder.create();
        subjectDialog.show();

    }

    public void createNewSubject(View view) {
        Intent i = new Intent(CreateExamActivity.this, CreateSubjectActivity.class);
        startActivityForResult(i, 0);
        /*Intent intComing = new Intent();
        intComing.getStringExtra("subName");
        onActivityResult(0,RESULT_OK,intComing);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            finalSubject = data.getLongExtra("subId", 0);
            chosenSubject.setText(subjectMainRepository.getById(finalSubject).getName());
            subjectDialog.dismiss();
        }
    }

    public void choseTime(View view) {
        Calendar cal = Calendar.getInstance();
        int timeMinute = cal.get(Calendar.MINUTE);
        int timeHour = cal.get(Calendar.HOUR_OF_DAY);
        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateExamActivity.this, 3, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                chosenTime.setText(selectedHour + ":" + selectedMinute);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                cal.set(Calendar.MINUTE, selectedMinute);
                cal.set(Calendar.SECOND, 0);
                timeMilliseconds = cal.getTimeInMillis();
            }
        }, timeHour, timeMinute, true);

        timePickerDialog.setTitle("Vyber si čas");
        timePickerDialog.show();


    }

    public void choseDate(View view) {

        chosenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int year = cal.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(
                        CreateExamActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,
                        year, month, day
                );

                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());


                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String strdate = dayOfMonth + "/" + month + "/" + year;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date d = new Date(sdf.parse(strdate).getTime());
                    milliseconds = d.getTime();
                    chosenDate.setText(strdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

    }


    public void addExam(View view) {
        if (!chosenDate.getText().toString().isEmpty() && !chosenTime.getText().toString().isEmpty() && !chosenSubject.getText().toString().isEmpty()) {
            if (chosenDays.getText().toString() != "") //&& chosenDifficulty.getText().toString() != "")
            // {
            //int difficulty = Integer.valueOf(chosenDays.getText().toString());
            {
                days = Integer.valueOf(chosenDays.getText().toString());
                String chosenDatefromTF = chosenDate.getText().toString();
                String classroom = classroomInput.getText().toString();
                String note = noteInput.getText().toString();
                DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                formatter.setLenient(false);

                Exam exam = new Exam(new Date(milliseconds), new Time(timeMilliseconds), days, 4, classroom, finalSubject, note);
                exam.setRealization(true);
                Long lastid = examMainRepository.insert(exam);
                Toast.makeText(CreateExamActivity.this,
                        "vydarilo sa " + finalSubject + "predmet" + lastid + subjectMainRepository.getById(finalSubject).getName(),
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(CreateExamActivity.this,
                        "nevydalo", Toast.LENGTH_SHORT).show();
            }

            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (isUpdateMode()) {
            inflater.inflate(R.menu.menu_update_exam, menu);
        } else {
            inflater.inflate(R.menu.menu_add_exam, menu);
        }
        return true;
    }

    public void createExam(MenuItem item) {

        if (!chosenDate.getText().toString().isEmpty() && !chosenTime.getText().toString().isEmpty() && !chosenSubject.getText().toString().isEmpty()) {
            if (chosenDays.getText().toString() != "") //&& chosenDifficulty.getText().toString() != "")
            // {
            //int difficulty = Integer.valueOf(chosenDays.getText().toString());
            {
                int days = Integer.valueOf(chosenDays.getText().toString());
                String chosenDatefromTF = chosenDate.getText().toString();
                String classroom = classroomInput.getText().toString();
                String note = noteInput.getText().toString();
                DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                formatter.setLenient(false);

                Exam exam = new Exam(new Date(milliseconds), new Time(timeMilliseconds), days, 4, classroom, finalSubject, note);
                exam.setRealization(true);
                Long lastid = examMainRepository.insert(exam);
                Toast.makeText(CreateExamActivity.this,
                        "vydarilo sa " + finalSubject + "predmet" + lastid + subjectMainRepository.getById(finalSubject).getName(),
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(CreateExamActivity.this,
                        "Nutné určiť náročnosť", Toast.LENGTH_SHORT).show();
            }

            finish();
        } else if (!chosenDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Nutné vyplniť povinné polia", Toast.LENGTH_SHORT).show();

        }

    }

    public void updateExam(MenuItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATE",milliseconds);
        contentValues.put("TIME",timeMilliseconds);
        contentValues.put("DAYS",days);
        contentValues.put("CLASSROOM",classroomInput.getText().toString());
        contentValues.put("NOTE",noteInput.getText().toString());
        examMainRepository.update2(intentExam.getId(),contentValues);
        finish();
    }
}


