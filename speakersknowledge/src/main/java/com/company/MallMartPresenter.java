package com.company;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apx.speakersknowledge.R;
import com.qualcommlabs.usercontext.protocol.ContentDescriptor;
import com.qualcommlabs.usercontext.protocol.ContentEvent;
import com.qualcommlabs.usercontext.protocol.ContextConnectorPermissions;
import com.qualcommlabs.usercontext.protocol.PlaceEvent;

public class MallMartPresenter {

    private static final String TAG = "MallMart";

    private final MallMartActivity activity;
    private View settingsLayout;
    private ImageButton userContextSettingsButton;
    private TextView placeEventTextView;
    private TextView contentTextView;
    private LinearLayout enableUserContextAreaLayout;
    private Button enableUserContextButton;
    private Button acceptTermsButton;
    private LinearLayout locationAndContentAreoLayout;
    private ImageButton imageRecognitionButton;

    public MallMartPresenter(MallMartActivity mallMartActivity) {
        this.activity = mallMartActivity;
    }

    public void initializeView() {
        activity.setContentView(R.layout.main);
        placeEventTextView = (TextView) activity.findViewById(R.id.place_event_text);
        contentTextView = (TextView) activity.findViewById(R.id.content_text);
        settingsLayout = activity.findViewById(R.id.settings_layout);
        enableUserContextAreaLayout = (LinearLayout) activity.findViewById(R.id.enable_relasphere_area);
        locationAndContentAreoLayout = (LinearLayout) activity.findViewById(R.id.location_content_area);
        enableUserContextButton = (Button) activity.findViewById(R.id.download_relasphere_button);
        acceptTermsButton = (Button) activity.findViewById(R.id.accept_terms_button);

        initializeSettingsButton();
        initializeImageRecognitionButton();
    }

    private void initializeSettingsButton() {
        userContextSettingsButton = (ImageButton) activity.findViewById(R.id.settings_button);
        userContextSettingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showUpdatePermissionsUI();
            }
        });
    }

    private void initializeImageRecognitionButton() {
        imageRecognitionButton = (ImageButton) activity.findViewById(R.id.ir_button);
        imageRecognitionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showImageRecognitionUI();
            }
        });
    }

    public void onContextConnectorInitializationSuccess() {
        hideEnableContextConnectorButton();
    }

    public void onContextConnectorEnableFailure(String message) {
        showEnableContextConnectorButton();
        if (message != null) {
            toastAndLogError(message);
        }
    }

    public void showEnableContextConnectorButton() {
        setPlaceEventAndContentAreaVisibility(View.GONE);
        enableUserContextAreaLayout.setVisibility(View.VISIBLE);
        initializeEnableUserContextButton();
    }

    public void hideEnableContextConnectorButton() {
        setPlaceEventAndContentAreaVisibility(View.VISIBLE);
        enableUserContextAreaLayout.setVisibility(View.GONE);
    }

    private void setPlaceEventAndContentAreaVisibility(int visibility) {
        locationAndContentAreoLayout.setVisibility(visibility);
    }

    private void initializeEnableUserContextButton() {
        acceptTermsButton.setVisibility(View.GONE);
        enableUserContextButton.setVisibility(View.VISIBLE);
        enableUserContextButton.setText(R.string.download_usercontext);
        enableUserContextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.enableContextConnector();
            }
        });

        enableUserContextButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    enableUserContextButton.setBackgroundDrawable(activity.getResources().getDrawable(
                            R.drawable.button_download_relasphere_pressed));
                }
                else {
                    enableUserContextButton.setBackgroundDrawable(activity.getResources().getDrawable(
                            R.drawable.button_download_relasphere));
                }
                return false;
            }
        });

    }

    public void updatePlaceEventTextView(PlaceEvent placeEvent) {
        String placeEventDescription = activity.getString(R.string.unknown);
        if (placeEvent != null) {
            placeEventDescription = String.format("%s %s", placeEvent.getEventType(), placeEvent.getName());
        }
        updatePlaceEventText(placeEventDescription);
    }

    public void updatePlaceEventText(String placeText) {
        placeEventTextView.setText(placeText);
    }

    public void toastAndLogError(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "Received an unexpected error with a null message";
        }
        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
        Log.e(TAG, errorMessage);
    }

    public void showSettings() {
        settingsLayout.setVisibility(View.VISIBLE);
    }

    public void hideSettings() {
        settingsLayout.setVisibility(View.GONE);
    }

    public void updatePlaceEventText(int resId) {
        updatePlaceEventText(activity.getString(resId));
    }

    public void updateContentTextAndSetOnClick(ContentEvent contentEvent) {
        if (!contentEvent.getContent().isEmpty()) {
            final ContentDescriptor contentDescriptor = contentEvent.getContent().get(0);
            updateContextTextAndSetOnClick(contentDescriptor);
        }
    }

    protected void updateContextTextAndSetOnClick(final ContentDescriptor contentDescriptor) {
        String title = contentDescriptor.getTitle();
        updateContentText(title);
        locationAndContentAreoLayout.setOnClickListener(onClickTransitionToContent(contentDescriptor));
    }

    private OnClickListener onClickTransitionToContent(final ContentDescriptor contentDescriptor) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToContentActivity(contentDescriptor.getContentUrl());
            }
        };
    }

    protected void updateContentText(String title) {
        contentTextView.setText(title);
    }

    public void transitionToContentActivity(String url) {
        Intent intent = new Intent();
        intent.setClass(activity, ContentActivity.class);
        intent.putExtra(ContentActivity.CONTENT_URL_KEY, url);
        activity.startActivity(intent);
    }

    public void updatePermissionTextView(ContextConnectorPermissions contextConnectorPermissions) {
        if (contextConnectorPermissions.isEnabled() == false) {
            onPermissionsDisabled();
        }
        else if (contextConnectorPermissions.isLocationEnabled() == false) {
            onLocationPermissionDisabled();
        }
        else if (placeEventTextView.getText().equals(activity.getString(R.string.location_disabled))) {
            updatePlaceEventText(activity.getString(R.string.unknown));
        }
    }

    private void onPermissionsDisabled() {
        updatePlaceEventText(activity.getString(R.string.disabled));
    }

    private void onLocationPermissionDisabled() {
        updatePlaceEventText(activity.getString(R.string.location_disabled));
    }

}
