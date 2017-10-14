package baludo.chat.com.br.chat_baludo.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import baludo.chat.com.br.chat_baludo.Model.Chat;
import baludo.chat.com.br.chat_baludo.Model.Contato;

/**
 * Created by Jean on 14/10/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String NOME_BD = "mydb.db";
    private static final int VERSAO_BD = 1;

    public SQLiteHelper(Context context){
        super(context, NOME_BD, null, VERSAO_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CONTATOS = "CREATE TABLE contatos (_id integer primary key autoincrement, ";
        SQL_CONTATOS += "id_user integer, contact_user integer, name_contact varchar(100), photo_contact varchar(255))";
        db.execSQL(SQL_CONTATOS);

        String SQL_MENSAGENS = "CREATE TABLE mensagens (_id integer primary key autoincrement, ";
        SQL_MENSAGENS += "id_user integer, contact_user integer, mensagem TEXT, data datetime)";
        db.execSQL(SQL_MENSAGENS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void save_contact(int id_user, int contact_user, String name_contact, String photo_contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ctv = new ContentValues();
        ctv.put("id_user", id_user);
        ctv.put("contact_user", contact_user);
        ctv.put("name_contact", name_contact);
        ctv.put("photo_contact", photo_contact);

        db.insert("contatos", "_id", ctv);
    }

    public long insert_message(int id_user, int contact_user, String mensagem, String data){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ctv = new ContentValues();
        ctv.put("id_user", id_user);
        ctv.put("contact_user", contact_user);
        ctv.put("mensagem", mensagem);
        ctv.put("data", data);

        return db.insert("mensagens", "_id", ctv);
    }

    public void update_message(long id_reg, String data){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ctv = new ContentValues();
        ctv.put("data", data);

        db.update("mensagens", ctv, "_id = ?", new String[]{String.valueOf(id_reg)});
    }

    public List<Chat> getMessages(int contact_user, int id_user){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] p = new String[]{String.valueOf(contact_user), String.valueOf(id_user), String.valueOf(id_user), String.valueOf(contact_user)};
        Cursor cursor = db.rawQuery("SELECT * FROM mensagens WHERE (contact_user = ?  and id_user = ?) or (contact_user = ?  and id_user = ?)", p);

        List<Chat> lista = new ArrayList<Chat>();

        while(cursor.moveToNext()){
            Chat c = new Chat();
            c.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            c.setId_user(cursor.getInt(cursor.getColumnIndex("id_user")));
            c.setContact_user(cursor.getInt(cursor.getColumnIndex("contact_user")));
            c.setMensagem(cursor.getString(cursor.getColumnIndex("mensagem")));
            c.setData(cursor.getString(cursor.getColumnIndex("data")));
            lista.add(c);
        }

        return lista;
    }

    public List<Chat> getAllChats(int id_user){
        SQLiteDatabase db = this.getReadableDatabase();

        //NA QUERY ABAIXO FOI FEITO UMA SUBQUERY
        StringBuilder strSQL = new StringBuilder();
        strSQL.append("SELECT m.* FROM mensagens m WHERE m._id in (");
        strSQL.append("SELECT MAX(m2._id) FROM mensagens m2 WHERE ");
        strSQL.append(String.valueOf(id_user));
        strSQL.append(" IN (m2.id_user, m2.contact_user)  GROUP BY max(m2.id_user, m2.contact_user) )");
        strSQL.append(" ORDER BY m._id desc");

        Cursor cursor = db.rawQuery(strSQL.toString(), null);

        List<Chat> lista = new ArrayList<Chat>();

        while(cursor.moveToNext()){
            Chat c = new Chat();
            c.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            c.setId_user(cursor.getInt(cursor.getColumnIndex("id_user")));
            c.setContact_user(cursor.getInt(cursor.getColumnIndex("contact_user")));
            c.setMensagem(cursor.getString(cursor.getColumnIndex("mensagem")));
            c.setData(cursor.getString(cursor.getColumnIndex("data")));
            lista.add(c);
        }

        return lista;
    }

    public Contato getContato(int contact_user){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contatos WHERE contact_user = ?", new String[]{String.valueOf(contact_user)});

        Contato c = new Contato();

        while(cursor.moveToNext()){
            c.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            c.setId_user(cursor.getInt(cursor.getColumnIndex("id_user")));
            c.setContact_user(cursor.getInt(cursor.getColumnIndex("contact_user")));
            c.setName_contact(cursor.getString(cursor.getColumnIndex("name_contact")));
            c.setPhoto_contact(cursor.getString(cursor.getColumnIndex("photo_contact")));
        }

        return c;
    }

    public List<Contato> getContacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM contatos", null);

        List<Contato> lista = new ArrayList<Contato>();

        while(cursor.moveToNext()){
            Contato c = new Contato();
            c.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            c.setId_user(cursor.getInt(cursor.getColumnIndex("id_user")));
            c.setContact_user(cursor.getInt(cursor.getColumnIndex("contact_user")));
            c.setName_contact(cursor.getString(cursor.getColumnIndex("name_contact")));
            c.setPhoto_contact(cursor.getString(cursor.getColumnIndex("photo_contact")));
            lista.add(c);
        }

        return lista;
    }
}
