package baludo.chat.com.br.chat_baludo.FiraBase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import baludo.chat.com.br.chat_baludo.CadastroActivity;
import baludo.chat.com.br.chat_baludo.ChatActivity;
import baludo.chat.com.br.chat_baludo.DataBase.SQLiteHelper;
import baludo.chat.com.br.chat_baludo.R;

/**
 * Created by Jean on 18/11/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage message){

        if(message.getData().size() > 0){
            if(message.getData().get("action").equals("NEW_MESSAGE")){
                int id_user = Integer.parseInt(message.getData().get("id_user"));
                int contact_user = Integer.parseInt(message.getData().get("contact_user"));
                String mensagem = message.getData().get("message");
                String data = message.getData().get("data");

                sendNotification(mensagem, contact_user);

                SQLiteHelper db = new SQLiteHelper(getBaseContext());
                db.insert_message(id_user, contact_user, mensagem, data);

                Intent intent = new Intent("REFRESH");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }else if (message.getData().get("action").equals("NEW_CONTACT")){
                int id_user = Integer.parseInt(message.getData().get("id_user"));
                int contact_user = Integer.parseInt(message.getData().get("contact_user"));
                String foto_usuario = message.getData().get("photo_user");
                String nome_usuario = message.getData().get("nome_user");

                SQLiteHelper db = new SQLiteHelper(getBaseContext());
                db.save_contact(contact_user, id_user, nome_usuario, foto_usuario);

                Intent it = new Intent("CONTACT");
                LocalBroadcastManager.getInstance(this).sendBroadcast(it);
            }
        }
    }

    private void sendNotification(String mensagem, int contato){

        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra("contact_user", contato);
        intent.putExtra("name_contact", "NOME PADRAO");
        intent.putExtra("photo_contact", "url foto");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent
                , PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.postgree)
                .setContentTitle("Nova mensagem")
                .setContentText(mensagem)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();
        notificationManager.notify(random.nextInt(), notificacao.build());
    }
}
