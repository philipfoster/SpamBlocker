/*
 * Copyright (c) 2015-2015 Philip Foster Jr. <philipfoster462@gmail.com>
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

package net.pfoster.spamblocker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import net.pfoster.spamblocker.service.UpdateService;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        downloadCsvIfNecessary();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Will launch the service to download the CSV if it's our first time running, or it's been
     * a while since it has been updated.
     */
    private void downloadCsvIfNecessary() {
        SharedPreferences prefs = getSharedPreferences("net.pfoster.spamblocker", Context.MODE_PRIVATE);
        String hasRunKey = "hasRunBefore";
        String lastDownloadKey = "lastDownload";

        boolean hasRun = prefs.getBoolean(hasRunKey, false);
        long lastDownload = prefs.getLong(lastDownloadKey, 0);

        // calculate last update + 7 days
        Calendar nextUpdate = Calendar.getInstance();
        nextUpdate.setTimeInMillis(lastDownload);
        nextUpdate.roll(Calendar.HOUR, 168); // 168 = hours in a week

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        // if this is the first run, or it's been 7+ days since the last update,
        // start the update service.
        if (!hasRun || now.after(nextUpdate)) {
            Log.d(TAG, "Updating spammer list");
            UpdateService.start(this);

            // we don't need to set the lastDownload value in sharedprefs because it is handled
            // by the UpdateService
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(hasRunKey, true);
            editor.apply();
        } else {
            Log.d(TAG, "Spammer list is up to date");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
