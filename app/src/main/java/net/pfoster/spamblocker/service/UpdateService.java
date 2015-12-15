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

package net.pfoster.spamblocker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by philip on 12/14/15.
 */
public class UpdateService extends IntentService {

    // link from https://consumercomplaints.fcc.gov/hc/en-us/articles/205239443
    //
    // does this link stay the same each update, or will we need to scrape the fcc page for the download
    // link each time?
    private static final String CSV_DOWNLOAD_URL = "https://p2.zdassets.com/hc/theme_assets/513073/200051444/Telemarketing_RoboCall_Weekly_Data_.csv";

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
        //TODO: Download the spammer list CSV and add entries into the sqlite db
    }
}
