package edu.vt.alerts.android.library.demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;

import edu.vt.alerts.android.library.R;

public class GCMIntentService extends GCMBaseIntentService {

	@Override
	protected void onError(Context context, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onMessage(Context context, Intent rIntent) {
		generateNotification(context, "VT Alerts Notification", 
		    rIntent.getExtras().getString("headline"));
	}

	@Override
	protected void onRegistered(Context context, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onUnregistered(Context context, String arg1) {
		// TODO Auto-generated method stub
	}
	
	private void generateNotification(Context context, String title, String text) {
	  Intent intent = new Intent(this, MainActivity.class);
    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
    		NotificationManager notificationManager = (NotificationManager) 
				  context.getSystemService(NOTIFICATION_SERVICE);
		Notification noti = new NotificationCompat.Builder(this)
			.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND 
			          | Notification.DEFAULT_LIGHTS | Notification.FLAG_SHOW_LIGHTS)
		        .setContentTitle(title)
		        .setContentText(text).setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pIntent)
		        .build();
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, noti);
	}

}
