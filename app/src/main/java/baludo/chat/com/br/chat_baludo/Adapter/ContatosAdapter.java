package baludo.chat.com.br.chat_baludo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import baludo.chat.com.br.chat_baludo.Model.Contato;
import baludo.chat.com.br.chat_baludo.R;

/**
 * Created by Jean on 02/11/2017.
 */

public class ContatosAdapter extends BaseAdapter {

    private Context context;
    private List<Contato> list = null;

    public ContatosAdapter(Context context, List<Contato> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Contato getItem(int position){
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
            view = inflater.inflate(R.layout.model_contacts, null);
        }else {
            view = convertView;
        }

        Contato contato = getItem(position);

        TextView textnome = (TextView)view.findViewById(R.id.txvNome);
        textnome.setText(contato.getName_contact());

        ImageView img = (ImageView)view.findViewById(R.id.imgvPhoto);
        Picasso.with(context).load(contato.getPhoto_contact()).resize(200, 200)
                .centerCrop().into(img);

        return view;
    }
}
