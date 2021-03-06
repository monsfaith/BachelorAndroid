package running.java.mendelu.cz.bakalarskapraca;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Habit;
import running.java.mendelu.cz.bakalarskapraca.db.MainOpenHelper;
import running.java.mendelu.cz.bakalarskapraca.db.ResultAdapter;


public class ResultsFragment extends Fragment {

    private ExamMainRepository examMainRepository;
    private RecyclerView recyclerView;
    private ResultAdapter resultAdapter;
    private MainOpenHelper mainOpenHelper;
    private SQLiteDatabase database;
    private TextView noExams;
    private ImageView noExamsImage;
    private List<Exam> exams = Collections.emptyList();

    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_results, container, false);
        examMainRepository = new ExamMainRepository(getActivity().getApplicationContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewResults);
        noExams = (TextView) view.findViewById(R.id.noExams);
        noExamsImage = (ImageView) view.findViewById(R.id.noExamImage);
      //  Toast.makeText(getActivity(), "ja som skuska", Toast.LENGTH_SHORT).show();

        //resultAdapter = new ResultAdapter(getActivity(),getResults());

        ((MainViewActivity) getActivity()).setActionBarTitle("Skúšky");

        if (getResults().size() == 0){
            noExams.setVisibility(View.VISIBLE);
            noExamsImage.setVisibility(View.VISIBLE);
        } else {
            noExams.setVisibility(View.GONE);
            noExamsImage.setVisibility(View.GONE);
        }

        resultAdapter = new ResultAdapter(getActivity(), getResults());

        recyclerView.setAdapter(resultAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));





        return view;
    }



    public List<Exam> getResults(){
        exams = examMainRepository.findDoneExams();
        return exams;

    }

    private void loadResults(){
        resultAdapter = new ResultAdapter(getActivity(), getResults());

        recyclerView.setAdapter(resultAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //INNER JOIN subject ON exam.subject_id = subject.id

    /*private Cursor getJoinResults(){
        return database.rawQuery("SELECT * FROM exam join subject on exam.subject_id = subject._id where exam.date < ?",new String[]{String.valueOf(System.currentTimeMillis())});

        //new String[]{String.valueOf(parameter?)}


    }*/

    public void onResume(){
        super.onResume();
        loadResults();

    }

}
