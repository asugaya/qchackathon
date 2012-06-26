/**
 * Copyright (C) 2011 Qualcomm Labs, Inc. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Qualcomm
 * Labs, Inc.
 * 
 * The following sample code illustrates various aspects of the UserContext SDK.
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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.apx.speakersknowledge.R;
import com.qualcommlabs.context.ir.sdk.ContextImageRecognitionConnector;
import com.qualcommlabs.context.ir.sdk.ContextImageRecognitionConnectorFactory;
import com.qualcommlabs.usercontext.Callback;
import com.qualcommlabs.usercontext.ContentListener;
import com.qualcommlabs.usercontext.ContextCoreConnector;
import com.qualcommlabs.usercontext.ContextCoreConnectorFactory;
import com.qualcommlabs.usercontext.ContextInterestsConnector;
import com.qualcommlabs.usercontext.ContextInterestsConnectorFactory;
import com.qualcommlabs.usercontext.ContextPlaceConnector;
import com.qualcommlabs.usercontext.ContextPlaceConnectorFactory;
import com.qualcommlabs.usercontext.PermissionChangeListener;
import com.qualcommlabs.usercontext.PlaceEventListener;
import com.qualcommlabs.usercontext.StatusCallback;
import com.qualcommlabs.usercontext.protocol.ContentEvent;
import com.qualcommlabs.usercontext.protocol.ContextConnectorPermissions;
import com.qualcommlabs.usercontext.protocol.PlaceEvent;

public class SpeakersKnowledgeActivity extends Activity implements OnClickListener {

    private static final String TAG = "SpeakersKnowledge";

    private ContextCoreConnector contextCoreConnector;
    private ContextInterestsConnector contextInterestsConnector;
    private ContextPlaceConnector contextPlaceConnector;
    private ContextImageRecognitionConnector imageRecognitionConnector;


    PlaceEventListener placeEventListener = new PlaceEventListener() {
        @Override
        public void placeEvent(PlaceEvent placeEvent) {
        }
    };

    ContentListener contentListener = new ContentListener() {
        @Override
        public void contentEvent(ContentEvent contentEvent) {
        }
    };

    PermissionChangeListener permissionChangeListener = new PermissionChangeListener() {
        @Override
        public void permissionChanged(ContextConnectorPermissions contextConnectorPermissions) {
            checkContextConnectorStatus();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.scanImage).setOnClickListener(this);
        contextCoreConnector = ContextCoreConnectorFactory.get(this);
        contextInterestsConnector = ContextInterestsConnectorFactory.get(this);
        contextPlaceConnector = ContextPlaceConnectorFactory.get(this);
        imageRecognitionConnector = ContextImageRecognitionConnectorFactory.get();
        checkContextConnectorStatus();
    }

    @Override
    protected void onDestroy() {
        stopListeningForPlaceEvents();
        stopListeningForContentEvents();
        stopListeningForPermissionChanges();
        super.onDestroy();
    }

    public void checkContextConnectorStatus() {
        contextCoreConnector.checkStatus(new StatusCallback() {

            @Override
            public void enabled(ContextConnectorPermissions contextConnectorPermissions) {
                contextConnectorEnabled();
            }

            @Override
            public void disabled(int statusCode, String message) {
                toastAndLogError(message);

            }
        });
    }

    protected void enableContextConnector() {
        contextCoreConnector.enable(this, new Callback<Void>() {
            @Override
            public void success(Void responseObject) {
                contextConnectorEnabled();
            }

            @Override
            public void failure(int statusCode, String errorMessage) {
            }
        });
    }

    protected void showUpdatePermissionsUI() {
        contextCoreConnector.showUpdatePermissionsUI(this, new Callback<Void>() {
            @Override
            public void success(Void responseObject) {
                Log.i(TAG, "ContextConnector permissions updated.");
            }

            @Override
            public void failure(int statusCode, String errorMessage) {
                toastAndLogError(errorMessage);
            }
        });
    }

    private void contextConnectorEnabled() {
        getLatestContentEventAndStartListeningForContentEvents();
        getLatestPlaceEventsAndStartListeningForPlaceEvents();
        startListeningForPermissionChanges();
        getProfileAndSendToLog();
        retrieveImageRecognitionTargets();
    }

    private void retrieveImageRecognitionTargets() {
        imageRecognitionConnector.retrieveTargetBundle(SpeakersKnowledgeActivity.this, new Callback<Void>() {
            @Override
            public void success(Void responseObject) {
                Log.d(TAG, "Successfully retrieved targets");
            }

            @Override
            public void failure(int statusCode, String errorMessage) {
                Log.e(TAG, errorMessage);
            }
        });
    }

    private void getProfileAndSendToLog() {
//        contextInterestsConnector.requestProfile(new AbstractFailureLoggingCallback<Profile>(mallMartPresenter) {
//            @Override
//            public void success(Profile profile) {
//                if (profile != null) {
//                    Log.d(TAG, profile.toString());
//                }
//            }
//        });
    }

    private void getLatestPlaceEventsAndStartListeningForPlaceEvents() {
        getLatestPlaceEvents();
        startListeningForPlaceEvents();
    }

    private void getLatestContentEventAndStartListeningForContentEvents() {
        getContentHistory();
        startListeningForContentEvents();
    }

    private void getLatestPlaceEvents() {
//        contextPlaceConnector.requestLatestPlaceEvents(new AbstractFailureLoggingCallback<List<PlaceEvent>>(
//                mallMartPresenter) {
//            @Override
//            public void success(List<PlaceEvent> placeEvent) {
//                mallMartPresenter.updatePlaceEventTextView(placeEvent.get(0));
//            }
//
//        });
    }

    private void getContentHistory() {
//        contextCoreConnector.requestContentHistory(new AbstractFailureLoggingCallback<List<ContentDescriptorHistory>>(
//                mallMartPresenter) {
//            @Override
//            public void success(List<ContentDescriptorHistory> contentDescriptorHistories) {
//                if (!contentDescriptorHistories.isEmpty()) {
//                    ContentDescriptor contentDescriptor = contentDescriptorHistories.get(0).getContentDescriptor();
//                    mallMartPresenter.updateContextTextAndSetOnClick(contentDescriptor);
//                }
//            }
//        });
    }

    private void startListeningForContentEvents() {
        contextCoreConnector.addContentListener(contentListener);
    }

    private void startListeningForPlaceEvents() {
        contextPlaceConnector.addPlaceEventListener(placeEventListener);
    }

    private void startListeningForPermissionChanges() {
        contextCoreConnector.addPermissionChangeListener(permissionChangeListener);
    }

    private void stopListeningForPlaceEvents() {
        contextPlaceConnector.removePlaceEventListener(placeEventListener);
    }

    private void stopListeningForContentEvents() {
        contextCoreConnector.removeContentListener(contentListener);
    }

    private void stopListeningForPermissionChanges() {
        contextCoreConnector.removePermissionChangeListener(permissionChangeListener);
    }

    public void toastAndLogError(String errorMessage) {
    }

    public void showImageRecognitionUI() {
        imageRecognitionConnector.showImageRecognitionUI(this, false, new Callback<String>() {
            @Override
            public void success(String responseObject) {
                Log.d(TAG, "Successfully launched IR");
            }

            @Override
            public void failure(int statusCode, String errorMessage) {
                Log.e(TAG, errorMessage);
            }
        });
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scanImage: {
			showImageRecognitionUI();
			break;
		}
		}
	}
}