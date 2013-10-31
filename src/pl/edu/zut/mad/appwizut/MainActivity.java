package pl.edu.zut.mad.appwizut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnItemClickListener{

	/**
	 * List of options in drawer
	 *
	 * First item is default and must be DrawerMenuItemFragment
	 *
	 * Possible option formats are:
	 *  new DrawerMenuItem(R.string.resource_for_title) { onClick(...) {...} }
	 *  new DrawerMenuItemFragment(R.string.resource_for_title, SomeFragment.class)
	 *  new DrawerMenuItemFragment(R.string.resource_for_title, SomeFragment.class, fragmentArgumentsBundle)
	 */
	private static final DrawerMenuItem[] DRAWER_ITEMS = new DrawerMenuItem[] {
			new DrawerMenuItemFragment(R.string.group, SampleFragment.class, SampleFragment.getArgumentsForMessage("group/week")),
			new DrawerMenuItemFragment(R.string.news, SampleFragment.class, SampleFragment.getArgumentsForMessage("news")),
			new DrawerMenuItemFragment(R.string.announcements, SampleFragment.class, SampleFragment.getArgumentsForMessage("announcements")),
			new DrawerMenuItemFragment(R.string.changes, SampleFragment.class, SampleFragment.getArgumentsForMessage("changes")),
			new DrawerMenuItemFragment(R.string.group, SampleFragment.class, SampleFragment.getArgumentsForMessage("group")),
			new DrawerMenuItem(R.string.settings) {
				@Override
				void onClick(MainActivity activity, int optionIndex) {
					activity.openSettings();
				}
			}
	};

	private static final String STATE_CURRENTLY_SELECTED_FRAGMENT_INDEX = "pl.edu.zut.mad.currentFragmentIndex";

	private int mCurrentlySelectedFragmentIndex = -1;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	/**
	 * Open app settings
	 */
	private void openSettings() {
		// TODO: open app settings
		Toast.makeText(this, "TODO: open app settings", Toast.LENGTH_LONG).show();
		startActivity(new Intent("android.settings.SETTINGS"));
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		// Load default fragment or restore instance state
		if (savedInstanceState == null) {
			DRAWER_ITEMS[0].onClick(this, 0);
		} else {
			mCurrentlySelectedFragmentIndex = savedInstanceState.getInt(STATE_CURRENTLY_SELECTED_FRAGMENT_INDEX);
		}

		// Prepare drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
 
        mDrawerToggle = new ActionBarDrawerToggle( this,
            mDrawerLayout,
            R.drawable.ic_drawer_menu,
            R.string.drawer_open,
            R.string.drawer_close){
                public void onDrawerClosed(View drawerView) {
					super.onDrawerClosed(drawerView);
                    supportInvalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);
                    supportInvalidateOptionsMenu();
                }
        };
 
        mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Prepare list
		mDrawerList.setAdapter(
				new ArrayAdapter<DrawerMenuItem>(this, 0, DRAWER_ITEMS) {
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = getLayoutInflater().inflate(R.layout.menu_list_item, parent, false);
						}
						((TextView) convertView).setText(getText(DRAWER_ITEMS[position].mTitleRes));
						return convertView;
					}
				}
		);
		mDrawerList.setOnItemClickListener(this);

		// Enable drawer expand button
		getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_CURRENTLY_SELECTED_FRAGMENT_INDEX, mCurrentlySelectedFragmentIndex);
	}

	/**
	 * Called when list on drawer item has been clicked
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DRAWER_ITEMS[position].onClick(this, position);

        mDrawerLayout.closeDrawer(mDrawerList);
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
 
    /**
	 * Handle touch of app icon or settings option
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		if (item.getItemId() == R.id.action_settings) {
			openSettings();
		}
        return super.onOptionsItemSelected(item);
    }
 
    /**
	 * Update contents of options menu and title
	 * Called after creation of activity and drawer open/close
	 *
	 * {@link #supportInvalidateOptionsMenu()}
	 */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
 
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		getSupportActionBar().setTitle(getText(
				drawerOpen ?
						R.string.drawer_open_title :
						DRAWER_ITEMS[mCurrentlySelectedFragmentIndex].mTitleRes
		));

        return super.onPrepareOptionsMenu(menu);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



	/**
	 * Generic drawer menu item
	 *
	 * For fragments use {@link MainActivity.DrawerMenuItemFragment},
	 * for other actions implement onClick
	 */
	private static abstract class DrawerMenuItem {
		int mTitleRes;

		DrawerMenuItem(int titleRes) {
			mTitleRes = titleRes;
		}

		abstract void onClick(MainActivity activity, int optionIndex);
	}

	private static class DrawerMenuItemFragment extends DrawerMenuItem {
		DrawerMenuItemFragment(int titleRes, Class<? extends Fragment> fragmentClass, Bundle arguments) {
			super(titleRes);
			mFragmentClass = fragmentClass;
			mArguments = arguments;
		}

		DrawerMenuItemFragment(int titleRes, Class<? extends Fragment> fragmentClass) {
			this(titleRes, fragmentClass, null);
		}

		Class<? extends Fragment> mFragmentClass;
		Bundle mArguments;

		@Override
		void onClick(MainActivity activity, int optionIndex) {
			// Ensure this isn't currently selected fragment
			if (optionIndex == activity.mCurrentlySelectedFragmentIndex) {
				return;
			}
			activity.mCurrentlySelectedFragmentIndex = optionIndex;

			// Instantiate new fragment
			Fragment fragment;
			try {
				fragment = mFragmentClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			// Set arguments
			fragment.setArguments(mArguments);

			// Put new fragment in activity
			FragmentManager fragmentManager = activity.getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			ft.commit();
		}
	}
}
