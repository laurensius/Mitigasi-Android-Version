package com.mitigasi1.webview;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
//import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceNotifikasi extends Service {

    boolean init = true;
    boolean load_data;
    int recent_jumlah_info_bencana,recent_jumlah_peringatan_dini,jumlah_info_bencana,jumlah_peringatan_dini;
    String id_info, id_dini;
    
    Timer timer = new Timer();


    public ServiceNotifikasi() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Notifikasi ON", Toast.LENGTH_LONG).show();
        final Handler handler = new Handler();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            GetJSONDataNotif getjsondata = new GetJSONDataNotif();
                            getjsondata.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Toast.makeText(this, "Service Notifikasi OFF", Toast.LENGTH_LONG).show();
    }

    private class GetJSONDataNotif extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String url = "http://192.168.0.101/mitigasi/webservice/api.php?query=android_service";
            String JSON_data = sh.makeServiceCall(url, ServiceHandler.GET);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    String code = jsonObj.getString("code");
                    if(code.equals("OK")){
                    	JSONObject data = jsonObj.getJSONObject("data");
                    	JSONObject info_bencana = data.getJSONObject("info_bencana");
                    	JSONObject peringatan_dini = data.getJSONObject("peringatan_dini"); 
                    	jumlah_info_bencana = Integer.parseInt(info_bencana.getString("jumlah"));
                    	jumlah_peringatan_dini = Integer.parseInt(peringatan_dini.getString("jumlah"));
                    	id_info = info_bencana.getString("recent_id");
                    	id_dini = peringatan_dini.getString("recent_id");
                    }
                    load_data = true;
                    Log.d("MITIGASI","Di dalam service");
                } catch (final JSONException e) {
                    Log.e("MITIGASI", e.getMessage());
                }
            }else{
                load_data = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String tipe = "";
            String uri = "";
            if(load_data==true){
                if(init==true){
                	recent_jumlah_info_bencana = jumlah_info_bencana;
                	recent_jumlah_peringatan_dini = jumlah_peringatan_dini;
                    init = false;
                }else{
                    if(jumlah_info_bencana > recent_jumlah_info_bencana){
                    	 tipe = "Informasi Bencana";
                    	 uri = "?id_info=".concat(id_info);
                    	 Notification.Builder builder = new Notification.Builder(getApplication().getBaseContext());
                         Intent notificationIntent = new Intent(getApplication().getBaseContext(),DetailNotif.class);
                         notificationIntent.putExtra("uri", uri);
                         PendingIntent pendingIntent = PendingIntent.getActivity(getApplication().getBaseContext(), 0,notificationIntent, 0);
                         builder.setSmallIcon(R.drawable.ic_launcher)
                                 .setContentTitle(tipe)
                                 .setContentText("Informasi lebih lanjut klik disini")
                                 .setContentIntent(pendingIntent);
                         Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                         builder.setSound(alarmSound);
                         NotificationManager notificationManager = (NotificationManager) getApplication().getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                         Notification notification = builder.getNotification();
                         notificationManager.notify(R.drawable.ic_launcher, notification);
                    }
                    if(jumlah_peringatan_dini > recent_jumlah_peringatan_dini){
	                   	 tipe = "Peringatan Dini";
	                	 uri = "?id_dini=".concat(id_dini);
	                	 Notification.Builder builder = new Notification.Builder(getApplication().getBaseContext());
	                     Intent notificationIntent = new Intent(getApplication().getBaseContext(),DetailNotif.class);

	                     notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                     notificationIntent.putExtra("uri", uri);
	                     PendingIntent pendingIntent = PendingIntent.getActivity(getApplication().getBaseContext(), 0,notificationIntent, 0);
	                     builder.setSmallIcon(R.drawable.ic_launcher)
	                             .setContentTitle(tipe)
	                             .setContentText("Informasi lebih lanjut klik disini")
	                             .setContentIntent(pendingIntent);
	                     Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	                     builder.setSound(alarmSound);
	                     NotificationManager notificationManager = (NotificationManager) getApplication().getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
	                     Notification notification = builder.getNotification();
	                     notificationManager.notify(R.drawable.ic_launcher, notification);
                   }
                    
                    Log.d("Recent jml info ", String.valueOf(recent_jumlah_info_bencana));
                    Log.d("Recent jml dini ", String.valueOf(recent_jumlah_peringatan_dini));
                    Log.d("Jumlah Info ", String.valueOf(jumlah_info_bencana));
                    Log.d("Jumlah Dini ", String.valueOf(jumlah_peringatan_dini));
                    Log.d("ID Info ", id_info);
                    Log.d("ID Dini ", id_dini);
                    
                    
                    recent_jumlah_info_bencana = jumlah_info_bencana;
                	recent_jumlah_peringatan_dini = jumlah_peringatan_dini;
                }
            }
        }
    }
}
