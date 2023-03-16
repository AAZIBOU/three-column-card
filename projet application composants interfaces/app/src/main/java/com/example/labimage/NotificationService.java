package com.example.labimage;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "notification_channel";
    private static final String CHANNEL_NAME = "Notification Channel";
    private static final int NOTIFICATION_ID = 1;

    private NotificationManagerCompat mNotificationManager;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new Handler();
        mNotificationManager = NotificationManagerCompat.from(this);

        // Créer le canal de notification pour Android Oreo et plus
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        // Planifier la tâche de notification pour s'exécuter toutes les 10 secondes
        mRunnable = new Runnable() {
            @Override
            public void run() {
                showNotification();
                mHandler.postDelayed(this, 10000);
            }
        };
        mHandler.postDelayed(mRunnable, 10000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification() {
        // Construire la notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Nouvelle notification")
                .setContentText("Vous avez une nouvelle notification.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Ouvrir l'activité de notification lorsque l'utilisateur clique sur la notification
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Afficher la notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
