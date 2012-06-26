package com.company;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.apx.speakersknowledge.R;
import com.qualcommlabs.usercontext.ContentListener;
import com.qualcommlabs.usercontext.PermissionChangeListener;
import com.qualcommlabs.usercontext.PlaceEventListener;
import com.qualcommlabs.usercontext.protocol.ContentDescriptor;
import com.qualcommlabs.usercontext.protocol.ContentEvent;
import com.qualcommlabs.usercontext.protocol.ContextConnectorPermissions;
import com.qualcommlabs.usercontext.protocol.PlaceEvent;
import com.qualcommlabs.usercontext.service.UserContextService;

public class CompanyService extends UserContextService implements PlaceEventListener, ContentListener,
        PermissionChangeListener {
    static int notificationId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        addPlaceEventListener(this);
        addContentListener(this);
        addPermissionChangeListener(this);
    }

    @Override
    public void contentEvent(ContentEvent contentEvent) {

        for (ContentDescriptor contentDescriptor : contentEvent.getContent()) {
            Notification notification = new Notification(R.drawable.icon, contentDescriptor.getTitle(),
                    System.currentTimeMillis());
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.ledARGB = 0xff31a2dd;
            notification.ledOnMS = 500;
            notification.ledOffMS = 200;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            notification.defaults |= Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;

            PendingIntent pendingIntent = createPendingIntent(contentDescriptor);

            notification.setLatestEventInfo(this, contentDescriptor.getTitle(),
                    contentDescriptor.getContentDescription(), pendingIntent);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(notificationId, notification);

            notificationId += 1;
        }
    }

    public PendingIntent createPendingIntent(ContentDescriptor contentDescriptor) {
        Bundle extras = new Bundle();
        extras.putString(ContentActivity.CONTENT_URL_KEY, contentDescriptor.getContentUrl());

        Intent launchIntent = new Intent();
        launchIntent.setClass(this, ContentActivity.class);
        launchIntent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void placeEvent(PlaceEvent event) {

    }

    @Override
    public void permissionChanged(ContextConnectorPermissions contextConnectorPermissions) {
        if (contextConnectorPermissions.isEnabled() == false) {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
        }
    }

}
