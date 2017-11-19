package baludo.chat.com.br.chat_baludo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import baludo.chat.com.br.chat_baludo.DataBase.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CadastroActivity.class);
                startActivity(intent);
            }
        });

        entrar = (Button) findViewById(R.id.btn_entrar);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txt_id = (EditText) findViewById(R.id.txt_id);
                EditText txt_senha = (EditText) findViewById(R.id.txt_senha);

                int error = 0;

                if(txt_id.getText().toString().equals("")){
                    txt_id.setError("Preencha o campo");
                    txt_id.requestFocus();
                    error = 1;
                }else if(txt_senha.getText().toString().equals("")){
                    txt_senha.setError("Preencha o campo");
                    txt_senha.requestFocus();
                    error = 1;
                }

                if(error == 0){
                    String URL = "http://neuraapi-net.umbler.net/methods/login_user.php";

                    SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
                    String SECUTIRY_TOKEN = preferences.getString("token_usuario", "");

                    Ion.with(getBaseContext())
                            .load(URL)
                            .setBodyParameter("email_user", txt_id.getText().toString())
                            .setBodyParameter("senha_user", txt_senha.getText().toString())
                            .setBodyParameter("token_user", SECUTIRY_TOKEN)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if(result.get("retorno").getAsInt() > 0){
                                        SharedPreferences.Editor preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE).edit();
                                        preferences.putInt("id_usuario", result.get("retorno").getAsInt());
                                        preferences.putString("nome_usuario", result.get("nome_usuario").getAsString());
                                        preferences.putString("email_usuario", result.get("email_usuario").getAsString());
                                        preferences.putString("photo_usuario", result.get("photo_usuario").getAsString());
                                        preferences.commit();

                                        SaveContact(result.get("retorno").getAsInt());

                                        showChat();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void showChat(){
        SharedPreferences preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE);
        if (preferences.getInt("id_usuario", 0) > 0) {
            Intent it = new Intent(MainActivity.this, ChatsActivity.class);
            startActivity(it);
            finish();
        }
    }

    private void SaveContact(final int id){
        String URL = "http://neuraapi-net.umbler.net/methods/get_all_contacts.php";

        Ion.with(getBaseContext())
                .load(URL)
                .setBodyParameter("id_user", String.valueOf(id))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for (int i = 0; i < result.size(); i++){
                            JsonObject obj = result.get(i).getAsJsonObject();
                            //JSON COM TODOS OS CONTATOS BUSCADOS DO BANCO
                            SQLiteHelper db = new SQLiteHelper(getBaseContext());
                            db.save_contact(id, Integer.parseInt(obj.get("id_usuario").getAsString()), obj.get("nome_usuario").getAsString(), obj.get("photo_usuario").getAsString());
                        }
                    }
                });
    }
}
