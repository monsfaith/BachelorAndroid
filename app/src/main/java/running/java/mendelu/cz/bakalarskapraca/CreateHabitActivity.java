package running.java.mendelu.cz.bakalarskapraca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import running.java.mendelu.cz.bakalarskapraca.db.Habit;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;

public class CreateHabitActivity extends AppCompatActivity {

    private EditText addHabitName;
    private EditText addHabitDescription;
    private HabitMainRepository habitMainRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        addHabitName = (EditText) findViewById(R.id.addHabitName);
        addHabitDescription = (EditText) findViewById(R.id.addHabitDescription);
        habitMainRepository = new HabitMainRepository(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_habit, menu);
        return true;
    }

    public void createHabit(MenuItem item) {

        if (addHabitName.getText().toString().trim().length() != 0){
            Habit habit = new Habit(addHabitName.getText().toString(), addHabitDescription.getText().toString(), addHabitDescription.getText().toString(), "care128",1);
            Long id = habitMainRepository.insert(habit);
           // Toast.makeText(this, "Aktivita vytvorená" + id + habitMainRepository.getById(id).getDone(), Toast.LENGTH_SHORT).show();
            finish();

        } else {
            addHabitName.setError("Vyplň názov");
            //Toast.makeText(this, "Aktivita nebola úspešne vytvorená", Toast.LENGTH_SHORT).show();
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
}