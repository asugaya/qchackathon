/**
 * Copyright (C) 2011 Qualcomm Labs, Inc. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Qualcomm
 * Labs, Inc.
 * 
 * The following sample code illustrates various aspects of the Relasphere SDK.
 * 
 * The sample code herein is provided for your convenience, and has not been
 * tested or designed to work on any particular system configuration. It is
 * provided AS IS and your use of this sample code, whether as provided or with
 * any modification, is at your own risk. Neither Qualcomm Labs, Inc. nor any
 * affiliate takes any liability nor responsibility with respect to the sample
 * code, and disclaims all warranties, express and implied, including without
 * limitation warranties on merchantability, fitness for a specified purpose,
 * and against infringement.
 */
package com.company;

import org.apache.log4j.Level;

import android.app.Application;

import com.apx.speakersknowledge.R;
import com.qlabs.util.log.Log4jConfig;

public class MallMartApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log4jConfig.config(this, false);
        Log4jConfig.setlevel(Level.DEBUG);
    }
}
