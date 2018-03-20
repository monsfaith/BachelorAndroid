package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Date;

import running.java.mendelu.cz.bakalarskapraca.db.PlanHabitAssociation;
import running.java.mendelu.cz.bakalarskapraca.db.PlanMainRepository;

/**
 * Created by Monika on 12.03.2018.
 */

public class DatabaseRecordReceiver extends BroadcastReceiver{

    PlanMainRepository planMainRepository;


    @Override
    public void onReceive(Context context, Intent intent) {

        planMainRepository = new PlanMainRepository(context);
        long idUpdate = planMainRepository.updateAssociation();
        Toast.makeText(context, "shit dabase" + idUpdate, Toast.LENGTH_LONG).show();


    }
}
