package net.pfoster.spamblocker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * This class is notified by the OS when a call is received, as defined in {@code AndroidManifest.xml}
 */
public class CallFilterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        FilterListener listener = new FilterListener();

        tmgr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class FilterListener extends PhoneStateListener {

        private static final int STATE_RINGING = 1;
        private static final String TAG = "CallFilter";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (state == STATE_RINGING) {
                //TODO: check if the phone is on the list of blocked numbers
            }
        }
    }
}