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

package net.pfoster.spamblocker.io;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This task downloads the CSV file in the background.
 */
public class DownloadTask extends AsyncTask<Void, Void, String> {

    // link from https://consumercomplaints.fcc.gov/hc/en-us/articles/205239443
    //
    // does this link stay the same each update, or will we need to scrape the fcc page for the download
    // link each time?
    private static final String CSV_DOWNLOAD_URL = "https://p2.zdassets.com/hc/theme_assets/513073/200051444/Telemarketing_RoboCall_Weekly_Data_.csv";

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(Void... params) {
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
