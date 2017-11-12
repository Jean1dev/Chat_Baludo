package baludo.chat.com.br.chat_baludo.Fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import baludo.chat.com.br.chat_baludo.Adapter.ChatsAdapter;
import baludo.chat.com.br.chat_baludo.ChatActivity;
import baludo.chat.com.br.chat_baludo.ChatsActivity;
import baludo.chat.com.br.chat_baludo.DataBase.SQLiteHelper;
import baludo.chat.com.br.chat_baludo.Model.Chat;
import baludo.chat.com.br.chat_baludo.Model.Contato;
import baludo.chat.com.br.chat_baludo.R;
import android.support.v4.app.Fragment;

/**
 * Created by Jean on 02/11/2017.
 */

public class ConversasFragment extends android.support.v4.app.Fragment {

    ListView listChat = null;
    List<Chat> list = null;

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_conversas, container, false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        listChat = (ListView) getView().findViewById(R.id.ltwChats);
        refreshListView();

        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences preferences = getActivity().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);

                Chat chat = (Chat)parent.getAdapter().getItem(position);

                Contato contato = null;

                //Context ctx = getContext();
                SQLiteHelper db = new SQLiteHelper(getContext());

                if(preferences.getInt("id_usuario", 0) == chat.getId_user()){
                    contato = db.getContato(chat.getContact_user());
                }else{
                    contato = db.getContato(chat.getId_user());
                }

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("contact_user", contato.getContact_user());
                intent.putExtra("name_contact", contato.getName_contact());
                intent.putExtra("photo_user", contato.getPhoto_contact());
                startActivity(intent);
            }
        });
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("REFRESH"));
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent){
            refreshListView();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void refreshListView(){
        SharedPreferences preferences = getActivity().getSharedPreferences("USER_INFORMATION", Context.MODE_PRIVATE);

        SQLiteHelper db = new SQLiteHelper(getContext());
        this.list = db.getAllChats(preferences.getInt("id_usuario", 0));

        if (listChat.getAdapter() != null){
            ((ChatsAdapter)listChat.getAdapter()).refresh(this.list);
        } else {
            listChat.setAdapter(new ChatsAdapter(getContext(), this.list));
        }
    }
}
