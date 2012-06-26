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

import java.util.List;
import java.util.Vector;
import java.util.Map;

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
import com.qualcommlabs.usercontext.protocol.profile.AttributeCategory;
import com.qualcommlabs.usercontext.protocol.profile.Profile;
import com.qualcommlabs.usercontext.protocol.profile.ProfileAttribute;

public class SpeakersKnowledgeActivity extends Activity implements OnClickListener {

    private static final String TAG = "SpeakersKnowledge";

    private ContextCoreConnector contextCoreConnector;
    private ContextInterestsConnector contextInterestsConnector;
    private ContextPlaceConnector contextPlaceConnector;
    private ContextImageRecognitionConnector imageRecognitionConnector;
    private Demograph userDemograph;

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
    
    /***
     * This returns the demograph for this user, or null if not set yet.
     */
    public Demograph getDemograph(){
    	contextInterestsConnector.requestProfile(new Callback<Profile>() {
            @Override
            public void success(Profile profile) {
                if (profile != null) {
                	userDemograph = new Demograph();
                    Log.d(TAG, profile.toString());
                    Map<String,ProfileAttribute> profileAttributes = profile.getAttributes();
                    for (Map.Entry<String, ProfileAttribute> entry : profileAttributes.entrySet())
                    {
                        Log.d(TAG, entry.getKey() + ":::");
                        //HACK
                        String entryKey = entry.getKey();
                        double highestLikelihood = 0.0;
                        int bestLikelihoodCategory = 0;
                        
                        List<AttributeCategory> attributeCategories = entry.getValue().getAttributeCategories();
                        
                        
                        if(entryKey.equals("Age")){
                        	for(AttributeCategory aCat: attributeCategories){
                        		double likelihood = aCat.getLikelihood();
                        		if(likelihood > highestLikelihood){
                        			highestLikelihood = likelihood;
                        			String aCatKey = aCat.getKey();
                        			//DOUBLE HACK
                        			if(aCatKey.equals("13 - 18")){
                        				bestLikelihoodCategory = 0;
                        			}else if(aCatKey.equals("18 - 24")){
                        				bestLikelihoodCategory = 1;
                        			}else if(aCatKey.equals("25 - 34")){
                        				bestLikelihoodCategory = 2;
                        			}else if(aCatKey.equals("35 - 44")){
                        				bestLikelihoodCategory = 3;
                        			} else if(aCatKey.equals("45 - 54")){
                        				bestLikelihoodCategory = 4;
                        			} else if(aCatKey.equals("55+")){
                        				bestLikelihoodCategory = 5;
                        			}
                        		}
                            	Log.d(TAG, aCat.getKey() + " : " + aCat.getLikelihood());
                            }
                        	userDemograph.age = bestLikelihoodCategory;
                        }else if(entryKey.equals("Education")){
                        	for(AttributeCategory aCat: attributeCategories){
                        		double likelihood = aCat.getLikelihood();
                        		if(likelihood > highestLikelihood){
                        			highestLikelihood = likelihood;
                        			String aCatKey = aCat.getKey();
                        			//DOUBLE HACK
                        			if(aCatKey.equals("College")){
                        				bestLikelihoodCategory = 1;
                        			}else if(aCatKey.equals("No College")){
                        				bestLikelihoodCategory = 0;
                        			}
                        		}
                            	Log.d(TAG, aCat.getKey() + " : " + aCat.getLikelihood());
                            }
                        	userDemograph.education = bestLikelihoodCategory;
                        }else if(entryKey.equals("Gender")){
                        	for(AttributeCategory aCat: attributeCategories){
                        		double likelihood = aCat.getLikelihood();
                        		if(likelihood > highestLikelihood){
                        			highestLikelihood = likelihood;
                        			String aCatKey = aCat.getKey();
                        			//DOUBLE HACK
                        			if(aCatKey.equals("Male")){
                        				bestLikelihoodCategory = 0;
                        			}else if(aCatKey.equals("Female")){
                        				bestLikelihoodCategory = 1;
                        			}
                        		}
                            	Log.d(TAG, aCat.getKey() + " : " + aCat.getLikelihood());
                            }
                        	userDemograph.gender = bestLikelihoodCategory;
                        }else if(entryKey.equals("Ethnicity")){
                        	for(AttributeCategory aCat: attributeCategories){
                        		double likelihood = aCat.getLikelihood();
                        		if(likelihood > highestLikelihood){
                        			highestLikelihood = likelihood;
                        			String aCatKey = aCat.getKey();
                        			//DOUBLE HACK
                        			if(aCatKey.equals("Asian")){
                        				bestLikelihoodCategory = 1;
                        			}else if(aCatKey.equals("African American")){
                        				bestLikelihoodCategory = 0;
                        			}else if(aCatKey.equals("Hispanic")){
                        				bestLikelihoodCategory = 3;
                        			}else if(aCatKey.equals("Caucasian")){
                        				bestLikelihoodCategory = 2;
                        			} 
                        		}
                            	Log.d(TAG, aCat.getKey() + " : " + aCat.getLikelihood());
                            }
                        	userDemograph.ethnicity = bestLikelihoodCategory;
                        }else if(entryKey.equals("Income")){
                        	for(AttributeCategory aCat: attributeCategories){
                        		double likelihood = aCat.getLikelihood();
                        		if(likelihood > highestLikelihood){
                        			highestLikelihood = likelihood;
                        			String aCatKey = aCat.getKey();
                        			//DOUBLE HACK
                        			if(aCatKey.equals("$30 - 60k")){
                        				bestLikelihoodCategory = 1;
                        			}else if(aCatKey.equals("$0 - 30k")){
                        				bestLikelihoodCategory = 0;
                        			}else if(aCatKey.equals("$60 - 100k")){
                        				bestLikelihoodCategory = 2;
                        			}else if(aCatKey.equals("$100k+")){
                        				bestLikelihoodCategory = 3;
                        			} 
                        		}
                            	Log.d(TAG, aCat.getKey() + " : " + aCat.getLikelihood());
                            }
                        	userDemograph.income = bestLikelihoodCategory;
                        }else if(entryKey.equals("Kids")){
                        	for(AttributeCategory aCat: attributeCategories){
                        		double likelihood = aCat.getLikelihood();
                        		if(likelihood > .5){
                        			userDemograph.kids = true;
                        		}else{
                        			userDemograph.kids = false;
                        		}
                        		
                            	Log.d(TAG, aCat.getKey() + " : " + aCat.getLikelihood());
                            }
                        }	else if(entryKey.equals("Interests")){
                        		Vector<String> interests = new Vector<String>();
                            	for(AttributeCategory aCat: attributeCategories){
                            		interests.add(aCat.getKey());
                                	Log.d(TAG, aCat.getKey() + " : " + aCat.getLikelihood());
                                }
                            	userDemograph.interests = interests;
                            }
                        
                    	}
                        
                        
                    }
                
            }

			@Override
			public void failure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				userDemograph = null;
			}
        });

  	return userDemograph;
      
    }
    
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