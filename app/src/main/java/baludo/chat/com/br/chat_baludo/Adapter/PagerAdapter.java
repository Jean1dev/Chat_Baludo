package baludo.chat.com.br.chat_baludo.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
}
