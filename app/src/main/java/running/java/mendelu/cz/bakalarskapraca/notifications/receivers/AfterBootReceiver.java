package running.java.mendelu.cz.bakalarskapraca.notifications.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Monika on 30.04.2018.
 */

public class AfterBootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                Intent serviceIntent = new Intent("running.java.mendelu.cz.bakalarskapraca.notifications.receivers.DatabaseRecordReceiver");
                //context.startService(serviceIntent);
                context.sendBroadcast(serviceIntent);
            }
        }
    }

