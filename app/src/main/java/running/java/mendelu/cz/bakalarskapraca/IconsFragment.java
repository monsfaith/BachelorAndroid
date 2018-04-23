package running.java.mendelu.cz.bakalarskapraca;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.Habit;
import running.java.mendelu.cz.bakalarskapraca.db.HabitMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.IconAuthorAdapter;
import running.java.mendelu.cz.bakalarskapraca.db.MainOpenHelper;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.ResultAdapter;


public class IconsFragment extends Fragment {

    private RecyclerView author1;
    private RecyclerView author2;
    private RecyclerView author3;
    private RecyclerView author4;
    private RecyclerView author5;
    private RecyclerView author6;

    private TextView author1Text;
    private TextView author2Text;
    private TextView author3Text;
    private TextView author4Text;
    private TextView author5Text;
    private TextView author6Text;

    private IconAuthorAdapter iconAuthorAdapter1;
    private IconAuthorAdapter iconAuthorAdapter2;
    private IconAuthorAdapter iconAuthorAdapter3;
    private IconAuthorAdapter iconAuthorAdapter4;
    private IconAuthorAdapter iconAuthorAdapter5;
    private IconAuthorAdapter iconAuthorAdapter6;


    private HabitMainRepository habitMainRepository;




    public IconsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_icons, container, false);
        habitMainRepository = new HabitMainRepository(getContext());
        author1 = (RecyclerView) view.findViewById(R.id.firstA);
        author2 = (RecyclerView) view.findViewById(R.id.secondA);
        author3 = (RecyclerView) view.findViewById(R.id.thirdA);
        author4 = (RecyclerView) view.findViewById(R.id.forthA);
        author5 = (RecyclerView) view.findViewById(R.id.fifthA);
        author6 = (RecyclerView) view.findViewById(R.id.sixA);


        author1Text = (TextView) view.findViewById(R.id.firstAuthor);
        author2Text = (TextView) view.findViewById(R.id.secondAuthor);
        author3Text = (TextView) view.findViewById(R.id.thirdAuthor);
        author4Text = (TextView) view.findViewById(R.id.forthAuthor);
        author5Text = (TextView) view.findViewById(R.id.fifthAuthor);
        author6Text = (TextView) view.findViewById(R.id.sixAuthor);


        author1Text.setText("Ikony vytvorené používateľom Freepik z www.flaticon.com ");
        author2Text.setText("Ikony vytvorené používateľom Roundicons z www.flaticon.com");
        author3Text.setText("Ikony vytvorené používateľom Smashicons z www.flaticon.com");
        author4Text.setText("Ikony vytvorené používateľom Vectors Market z www.flaticon.com");
        author5Text.setText("Ikony vytvorené používateľom Icon Pond z www.flaticon.com");
        author6Text.setText("Ikony vytvorené používateľom Flat icons z www.flaticon.com");


        loadRecyclerViews();


        return view;
    }

    public void loadRecyclerViews(){
        iconAuthorAdapter1 = new IconAuthorAdapter(getContext(),getList("Freepik"));
        author1.setAdapter(iconAuthorAdapter1);
        author1.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));

        iconAuthorAdapter2 = new IconAuthorAdapter(getContext(),getList("Roundicons"));
        author2.setAdapter(iconAuthorAdapter2);
        author2.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));

        iconAuthorAdapter3 = new IconAuthorAdapter(getContext(),getList("Smashicons"));
        author3.setAdapter(iconAuthorAdapter3);
        author3.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));

        iconAuthorAdapter4 = new IconAuthorAdapter(getContext(),getList("VectorsM"));
        author4.setAdapter(iconAuthorAdapter4);
        author4.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));

        iconAuthorAdapter5 = new IconAuthorAdapter(getContext(),getList("Pond"));
        author5.setAdapter(iconAuthorAdapter5);
        author5.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));

        iconAuthorAdapter6 = new IconAuthorAdapter(getContext(),getList("Flat"));
        author6.setAdapter(iconAuthorAdapter6);
        author6.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.HORIZONTAL));


    }

    private List<Habit> getList(String author){
        return habitMainRepository.getAuthorHabits(author);
    }


}
