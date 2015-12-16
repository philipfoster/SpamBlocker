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

package net.pfoster.spamblocker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This service is responsible for updating the spammer database.
 */
public class UpdateService extends IntentService {

    // link from https://consumercomplaints.fcc.gov/hc/en-us/articles/205239443
    //
    // does this link stay the same each update, or will we need to scrape the fcc page for the download
    // link each time?
    // does the fcc upload a delta, or the entire file?
    // how do we handle duplicates?
    private static final String CSV_DOWNLOAD_URL = "https://p2.zdassets.com/hc/theme_assets/513073/200051444/Telemarketing_RoboCall_Weekly_Data_.csv";
    private static final String TAG = "UpdateService";

    /**
     * Starts an instance of this service.
     * @param ctx the Context of the application package
     */
    public static void start(Context ctx) {
        Intent svcIntent = new Intent(ctx, UpdateService.class);
        ctx.startService(svcIntent);
    }

    public UpdateService() {
        super("UpdateService");
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // update the shared preferences to reflect the new update.
        SharedPreferences prefs = getSharedPreferences("net.pfoster.spamblocker", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("lastDownload", System.currentTimeMillis());
        editor.apply();

        //TODO: Download the spammer list CSV and add entries into the sqlite db
        String ret;
        String s = download();

    }

    private String download() {
        HttpURLConnection conn = null;
        String result = "";

        //noinspection TryWithIdenticalCatches
        try {
            URL url = new URL(CSV_DOWNLOAD_URL);
            conn = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());

            if (conn.getContentLength() == -1) {
                throw new IOException("Content-Length header was not set by the server.");
            }

            byte[] buffer = new byte[conn.getContentLength()];
            is.read(buffer);

            result = new String(buffer);
        } catch (MalformedURLException e) {
            // should never happen.
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: attempt to re-download the file at a later time
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }


}
