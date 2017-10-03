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

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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

        entrar = (Button) findViewById(R.id.btnCadastrar);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txt_id = (EditText) findViewById(R.id.txt_id);
                EditText txt_senha = (EditText) findViewById(R.id.txtSenha);

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
                    String URL = "https://localhost/chat/metodo";

                    SharedPreferences preferences = getBaseContext().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
                    String SECUTIRY_TOKEN = preferences.getString("token_usuario", "");

                    Ion.with(getBaseContext())
                            .load(URL)
                            .setBodyParameter("id_usuario", txt_id.getText().toString())
                            .setBodyParameter("senha_usuario", txt_senha.getText().toString())
                            .setBodyParameter("token_usuario", SECUTIRY_TOKEN)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if(result.get("retorno").getAsInt() > 0){
                                        SharedPreferences.Editor preferences = getSharedPreferences("USER_INFORMATION", MODE_PRIVATE).edit();
                                        preferences.putInt("id_usuario", result.get("retorno").getAsInt());
                                    }
                                }
                            });
                }
            }
        });
    }
}
