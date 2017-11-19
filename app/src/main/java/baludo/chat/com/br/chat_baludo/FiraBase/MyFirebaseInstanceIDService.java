package baludo.chat.com.br.chat_baludo.FiraBase;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Jean on 18/11/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    public void OnTokenRefresh(){
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshToken);
        saveInPreference(refreshToken);
    }

    private void saveInPreference(String token){
        SharedPreferences.Editor preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE).edit();
        preferences.putString("token_usuario", token);
        preferences.commit();
    }

    private void sendRegistrationToServer(String token){
        String URL = "http://neuraapi-net.umbler.net/methods/update_token.php";

        SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
        final String id_user = String.valueOf(preferences.getInt("id_usuario", 0));

        if (preferences.getInt("id_usuario", 0) > 0){
            Ion.with(getBaseContext())
                    .load(URL)
                    .setMultipartParameter("id_user", id_user)
                    .setMultipartParameter("token_user", token)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                        }
                    });
        }
    }
}
