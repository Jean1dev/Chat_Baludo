package baludo.chat.com.br.chat_baludo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import baludo.chat.com.br.chat_baludo.Model.Chat;
import baludo.chat.com.br.chat_baludo.R;

/**
 * Created by Jean on 18/11/2017.
 */

public class ChatAdapter extends BaseAdapter {

    private Context context = null;
    private List<Chat> list = null;

    public ChatAdapter(Context context, List<Chat> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Chat getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.model_chats, null);
        } else {
            view = convertView;
        }

        Chat c = getItem(position);

        TextView txtmensagem = (TextView) view.findViewById(R.id.txvMensagem);
        txtmensagem.setText(c.getMensagem());

        TextView txtdata = (TextView) view.findViewById(R.id.txvData);

        if (c.getData().length() == 0) {
            txtdata.setText("enviando...");
        } else {
            String data = c.getData();
            Calendar c1 = Calendar.getInstance();
            if ((c1.get(Calendar.YEAR) == Integer
                    .parseInt(data.split("\\-")[0]))
                    && ((c1.get(Calendar.MONTH) + 1) == Integer.parseInt(data
                    .split("\\-")[1]))
                    && (c1.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(data
                    .split("\\-")[2].substring(0, 2)))) {
                txtdata.setText(data.split("\\ ")[1]);
            } else {
                String new_data_br = data.split("\\-")[2].substring(0, 2) + "/" + data.split("\\-")[1] + "/" + data.split("\\-")[0] + " " + data.split("\\-")[2].substring(2);
                txtdata.setText(new_data_br);
            }
        }
        LinearLayout containerCHAT = (LinearLayout) view.findViewById(R.id.containerChat);
        LinearLayout containerCHAT_2 = (LinearLayout) view.findViewById(R.id.containerChat);

        SharedPreferences preferences = ((Activity) context).getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);
        final int id_user = preferences.getInt("id_usuario", 0);

        if (id_user == c.getId_user()) {
            txtmensagem.setBackgroundResource(R.drawable.bubble_green);
            containerCHAT.setGravity(Gravity.RIGHT);
            containerCHAT_2.setGravity(Gravity.RIGHT);
        } else {
            txtmensagem.setBackgroundResource(R.drawable.bubble_yellow);
            containerCHAT.setGravity(Gravity.LEFT);
            containerCHAT_2.setGravity(Gravity.LEFT);
        }

        return view;
    }
}
