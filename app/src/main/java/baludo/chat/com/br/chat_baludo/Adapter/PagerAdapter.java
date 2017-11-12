package baludo.chat.com.br.chat_baludo.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import baludo.chat.com.br.chat_baludo.Fragment.ContatosFragment;
import baludo.chat.com.br.chat_baludo.Fragment.ConversasFragment;
import baludo.chat.com.br.chat_baludo.Fragment.PerfilFragment;
import baludo.chat.com.br.chat_baludo.R;

/**
 * Created by Jean on 14/10/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private Context context = null;

    String tabs[] = new String[] {"Contato", "Chats", "Perfil"};

    public PagerAdapter(FragmentManager fm, Context ctx){
        super(fm);
        this.context = ctx;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            default:
                return null;
            case 0:
                return new ContatosFragment();
            case 1:
                return new ConversasFragment();
            case 2:
                return new PerfilFragment();
        }
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabs[position];
    }


    public View getTableView(int position){
        View tab = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView txtTab = (TextView)tab.findViewById(R.id.txvTab);
        txtTab.setText(getPageTitle(position));
        return tab;
    }
}
