package pl.edu.zut.mad.appwizut;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class MainActivity extends Activity implements OnItemClickListener{

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;

    String mTitle="";
 
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        mTitle = (String) getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
 
        mDrawerToggle = new ActionBarDrawerToggle( this,
            mDrawerLayout,
            R.drawable.ic_drawer_menu,
            R.string.drawer_open,
            R.string.drawer_close){
 
                /** Wywo³ywane kiedy Drawer jest otwarty */
                public void onDrawerClosed(View view) {
                    getActionBar().setTitle(mTitle);
                    invalidateOptionsMenu();
                }
 
                /** Wywo³ywane kiedy drawer jest zamkniêty */
                public void onDrawerOpened(View drawerView) {
                    getActionBar().setTitle("Wybierz z listy");
                    invalidateOptionsMenu();
                }
        };
 
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getBaseContext(),
            R.layout.menu_list_item,
            getResources().getStringArray(R.array.menu_items)
        );
 
        mDrawerList.setAdapter(adapter);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerList.setOnItemClickListener(this);
    }

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String[] rivers = getResources().getStringArray(R.array.menu_items);

        mTitle = rivers[position];
        MenuFragment mFragment = new MenuFragment();

        Bundle data = new Bundle();
        data.putInt("position", position);

        mFragment.setArguments(data);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.content_frame, mFragment);

        ft.commit();

        mDrawerLayout.closeDrawer(mDrawerList);
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
 
    /** Handling the touch event of app icon */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
 
    /** Wywo³ywane dla ka¿dego wywo³ania invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
 
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
