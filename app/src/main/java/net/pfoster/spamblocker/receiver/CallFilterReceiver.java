/*
 * Copyright (c) 2015-${year} Philip Foster Jr. <philipfoster462@gmail.com>
 *
 * This file is part of SpamBlocker.
 *
 *     SpamBlocker is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     SpamBlocker is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with SpamBlocker.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pfoster.spamblocker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

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