package arturo.deaquino.com.cardflip;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class CardFlipActivity extends Activity
        implements FragmentManager.OnBackStackChangedListener {

    private Handler mHandler = new Handler();
    private boolean mShowingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);

        if(savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        }else{
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_flip, menu);

        MenuItem item = menu.add(Menu.NONE, R.id.action_flip,
                Menu.NONE,
                mShowingBack
                        ? "View photo"
                        : "Photo info");
        item.setIcon(mShowingBack
                ? R.drawable.ic_action_photo
                : R.drawable.ic_action_info);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_flip){
            flipCard();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void flipCard(){
        if(mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }

        mShowingBack = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.container, new CardBackFragment())
                .addToBackStack(null)
                .commit();

        mHandler.post(new Runnable(){
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager()
        .getBackStackEntryCount() > 0);

        invalidateOptionsMenu();
    }

    ////////////////FRAGMENT///////////////////////
    public static class CardFrontFragment extends Fragment {
        public CardFrontFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState ){
            return inflater.inflate(R.layout.fragment_card_front,
                    container,
                    false);
        }
    }
    public static class CardBackFragment extends Fragment {
        public CardBackFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState ){
            return inflater.inflate(R.layout.fragment_card_back,
                    container,
                    false);
        }
    }
}
