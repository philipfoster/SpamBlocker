/*
 * Copyright (c) 2015-2015 Philip Foster Jr.
 * Author: Philip Foster <philipfoster462@gmail.com>
 *
 * This file is part of Spam Blocker.
 *
 *     Spam Blocker is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Spam Blocker is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Spam Blocker.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.pfoster.spamblocker.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.pfoster.spamblocker.service.UpdateService;

import java.util.Calendar;

/**
 * This class is called initially on boot, and is used to start an alarm for
 * {@link net.pfoster.spamblocker.service.UpdateService}
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: check if it's been >= 1 week since the last boot. if so, fire off the service now, as well as setting the intent.

        Intent updateIntent = new Intent(context, UpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updateIntent, 0);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 60);
        long frequency = 604800000; // milliseconds in a week
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), frequency, pendingIntent);
    }
}
