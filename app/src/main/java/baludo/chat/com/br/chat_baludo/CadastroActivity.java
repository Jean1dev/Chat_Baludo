package baludo.chat.com.br.chat_baludo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import baludo.chat.com.br.chat_baludo.ImageControler.ImagePicker;

public class CadastroActivity extends AppCompatActivity {

    Button btn_cadastrar;
    private final static int REQUEST_DIALOG_PHOTO = 1;
    private int photo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        final ImageView img = (ImageView) findViewById(R.id.imgFoto);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //img.setImageResource(android.R.color.transparent);
                Intent intentCamera = ImagePicker.getPickImageIntent(getBaseContext());
                startActivityForResult(intentCamera, REQUEST_DIALOG_PHOTO);
            }
        });

        btn_cadastrar = (Button) findViewById(R.id.btnCadastrar);
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txt_nome = (EditText) findViewById(R.id.txtNome);
                EditText txt_email = (EditText) findViewById(R.id.txtEmail);
                EditText txt_senha = (EditText) findViewById(R.id.txt_senha);
                EditText txt_confirmar_senha = (EditText) findViewById(R.id.txtSenha2);

                int error = 0;

                //BLOCO DE VALIDAÇÃO
                if (txt_nome.getText().toString().equals("")) {
                    txt_nome.setError("è necessario preencher");
                    txt_nome.requestFocus();
                    error = 1;
                } else if (txt_email.getText().toString().equals("")) {
                    txt_email.setError("preencha o email");
                    txt_email.requestFocus();
                    error = 1;
                } else if (txt_senha.getText().toString().equals("")) {
                    txt_senha.setError("e necessario preencher");
                    txt_senha.requestFocus();
                    error = 1;
                } else if (txt_confirmar_senha.getText().toString().equals("")) {
                    txt_confirmar_senha.setError("e necessario preencher");
                    txt_confirmar_senha.requestFocus();
                    error = 1;
                } else if (!txt_senha.getText().toString().equals(txt_confirmar_senha.getText().toString())) {
                    Toast.makeText(getBaseContext(), "As senhas são diferentes", Toast.LENGTH_SHORT).show();
                    error = 1;
                } else if (photo == 0) {
                    Toast.makeText(getBaseContext(), " Coloque uma foto", Toast.LENGTH_SHORT).show();
                    error = 1;
                }
                //FIM DO BLOCO DE VALIDAÇÃO

                if (error == 0) {
                    String URL = "http://neuraapi-net.umbler.net/methods/insert_user.php";
                    String photoFile = "";

                    try {
                        photoFile = getBaseContext().getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0).applicationInfo.dataDir + "//photo/perfil.png";
                    } catch (PackageManager.NameNotFoundException e) {

                    }

                    Ion.with(getBaseContext())
                            .load(URL)
                            .setMultipartParameter("nome", txt_nome.getText().toString())
                            .setMultipartParameter("email", txt_email.getText().toString())
                            .setMultipartParameter("senha", txt_senha.getText().toString())
                            .setMultipartFile("foto", new File(photoFile))
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (result.get("retorno").getAsString().equals("SUCESSO")) {
                                        Toast.makeText(getBaseContext(), "Sucesso", Toast.LENGTH_SHORT).show();
                                    } else if (result.get("retorno").getAsString().equals("ERRO")) {
                                        Toast.makeText(getBaseContext(), "Lamento", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    //ESSE METODO RECEBE O RESULTADO DE QUANDO EU CHAMEI A INTENT CAMERA LÁ EM CIMA
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DIALOG_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap photoUser = ImagePicker.getImageFromResult(getBaseContext(), resultCode, data);
                ImageView imgFoto = (ImageView) findViewById(R.id.imgFoto);
                imgFoto.setImageBitmap(photoUser);
                photo = 1;

                // Grava foto pasta
                File diretorio = Environment.getDataDirectory();
                String path = "//data//" + getBaseContext().getPackageName() + "//photo//";

                diretorio = new File(diretorio, path);
                diretorio.mkdirs();

                OutputStream out = null;

                File outputFile = new File(diretorio, "perfil.png");

                try {
                    out = new FileOutputStream(outputFile);
                    photoUser.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {

                }

            } else {
                Toast.makeText(getBaseContext(), "Por favor, selecione uma foto", Toast.LENGTH_LONG).show();
            }
        }
    }
}


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_DIALOG_PHOTO){
            if(requestCode == Activity.RESULT_OK){
                Bitmap foto = ImagePicker.getImageFromResult(getBaseContext(), resultCode, data);
                ImageView img_foto = (ImageView) findViewById(R.id.imgFoto);
                img_foto.setImageBitmap(foto);
                photo = 1;

                //GRAVA A FOTO EM UMA PASTA
                File dir = Environment.getDataDirectory();
                String dir_path = "//data" + getBaseContext().getPackageName() + "//photo//";

                dir = new File(dir, dir_path);
                dir.mkdirs();

                OutputStream output = null;

                File file_arq = new File(dir, "perfil.png");

                try {
                    output = new FileOutputStream(file_arq);
                    foto.compress(Bitmap.CompressFormat.PNG, 100, output);
                    output.flush();
                    output.close();
                }catch (Exception e){

                }
            }else {
                Toast.makeText(getBaseContext(), "Selecione uma foto", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

