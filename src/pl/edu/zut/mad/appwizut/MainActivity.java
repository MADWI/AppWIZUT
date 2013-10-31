package pl.edu.zut.mad.appwizut;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements OnItemClickListener{

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;

    String mTitle="";
 
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
 
                /** Wywo�ywane kiedy Drawer jest otwarty */
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(mTitle);
                    supportInvalidateOptionsMenu();
                }
 
                /** Wywo�ywane kiedy drawer jest zamkni�ty */
                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle("Wybierz z listy");
                    supportInvalidateOptionsMenu();
                }
        };
 
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            getBaseContext(),
            R.layout.menu_list_item,
            getResources().getStringArray(R.array.menu_items)
        );
 
        mDrawerList.setAdapter(adapter);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerList.setOnItemClickListener(this);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String[] rivers = getResources().getStringArray(R.array.menu_items);

        mTitle = rivers[position];
        MenuFragment mFragment = new MenuFragment();

        Bundle data = new Bundle();
        data.putInt("position", position);

        mFragment.setArguments(data);

        FragmentManager fragmentManager = getSupportFragmentManager();
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
 
    /** Wywo�ywane dla ka�dego wywo�ania invalidateOptionsMenu() */
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
