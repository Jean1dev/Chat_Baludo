package baludo.chat.com.br.chat_baludo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import baludo.chat.com.br.chat_baludo.DataBase.SQLiteHelper;
import baludo.chat.com.br.chat_baludo.Model.Chat;
import baludo.chat.com.br.chat_baludo.Model.Contato;
import baludo.chat.com.br.chat_baludo.R;

/**
 * Created by Jean on 12/11/2017.
 */

public class ChatsAdapter extends BaseAdapter {

    private Context context = null;
    private List<Chat> list = null;

    public ChatsAdapter(Context context, List<Chat> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Chat getItem(int position){
        return list.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = null;

        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.model_all_chats, null);
        }else{
            view = convertView;
        }

        Chat c = getItem(position);
        SQLiteHelper db = new SQLiteHelper(context);

        SharedPreferences preferences = context.getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);

        Contato contato = null;

        if(preferences.getInt("id_usuario", 0)== c.getId_user()){
            contato = db.getContato(c.getContact_user());
        }else{
            contato = db.getContato(c.getId_user());
        }

        TextView txtNome = (TextView)view.findViewById(R.id.txvNome);
        txtNome.setText(contato.getName_contact());

        ImageView img = (ImageView)view.findViewById(R.id.imgvPhoto);

        Picasso.with(context).load(contato.getPhoto_contact()).resize(200, 200)
                .centerCrop().into(img);

        TextView txtMsng = (TextView)view.findViewById(R.id.txvMensagem);
        if(c.getMensagem().length() > 10){
            txtMsng.setText(c.getMensagem().substring(0, 10));
        }else{
            txtMsng.setText(c.getMensagem().substring(0, c.getMensagem().length()));
        }
        return view;
    }

    public void refresh(List<Chat> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
