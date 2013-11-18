package pl.edu.zut.mad.appwizut.models.fragments;

import java.util.ArrayList;

import pl.edu.zut.mad.appwizut.R;
import pl.edu.zut.mad.appwizut.adapters.ListViewAdapterPlanChanges;
import pl.edu.zut.mad.appwizut.connection.GetChanges;
import pl.edu.zut.mad.appwizut.connection.HTTPLinks;
import pl.edu.zut.mad.appwizut.connection.HttpConnect;
import pl.edu.zut.mad.appwizut.models.MessagePlanChanges;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateAndSetNews extends Fragment {

	/**
	 * Zmienna do debuggowania.
	 */
	private static final String TAG = "PlanChangesActivity";

	/** Obiekt typu GetPlanChanges */
	private GetChanges pars;

	/**
	 * Obiekt ArrayList zawierajacy obiekty klasy MessagePlanChanges, gdzie
	 * wyswietlane beda zmiany w planie
	 */
	private ArrayList<MessagePlanChanges> news;

	/** Obiekt ProgressDialog */
	private ProgressDialog progress;

	/** Zmienna stwierdzajaca wcisniecie przycisku odswiezania */
	private boolean enableExecuteRefresh = true;

	/** Obiekt ListView */
	private ListView articles;
	
	public UpdateAndSetNews() {
		news = new ArrayList<MessagePlanChanges>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Need to rotate the screen
		setRetainInstance(true);

		View rootView = inflater.inflate(R.layout.fragment_changes_layout,
				container, false);
		articles = (ListView) rootView.findViewById(R.id.articles);

		if (HttpConnect.isOnline(getActivity()))
			new AsyncTaskGetPlanChanges().execute();

		return rootView;
	}
	
	public void startConnect(String http) {
		try {
			pars = new GetChanges(http);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Metoda odswiezajaca ListView ze zmianami w planie */
	private void refreshListView() {
		Log.i(TAG, "refreshListView");

		final int ANIMATION_TIME = 300;

		ListViewAdapterPlanChanges adapter = new ListViewAdapterPlanChanges(
				getActivity(), R.layout.list_item_layout, news);

		articles.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		articles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final TextView body = (TextView) view
						.findViewById(R.id.bodyText);

				if (body.getVisibility() == View.GONE) {

					Animation animation = new AlphaAnimation(0.0f, 1.0f);
					animation = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 1.0f,
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.0f);
					animation.setDuration(ANIMATION_TIME);

					body.startAnimation(animation);
					body.setVisibility(View.VISIBLE);
				} else {

					Animation animation = new AlphaAnimation(0.0f, 1.0f);
					animation = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 1.1f,
							Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 0.0f);
					animation.setDuration(ANIMATION_TIME);
					animation.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							body.setVisibility(View.GONE);
						}
					});
					body.startAnimation(animation);

				}
			}
		});

		if (news.size() == 0) {
			Toast.makeText(getActivity(), "Brak wiadomo�ci", Toast.LENGTH_LONG)
					.show();
		}
	}

	/** Klasa pobierajaca zmiany w planie */
	private class AsyncTaskGetPlanChanges extends
			AsyncTask<String, Boolean, Void> {

		/**
		 * ArrayList obiektow MessagePlanChanges, gdzie beda przechowywane dane
		 * o zmianach w planie
		 */
		ArrayList<MessagePlanChanges> tempArray = null;

		/** Wykonywanie zadan w tle watku glownego */
		@Override
		protected Void doInBackground(String... params) {
			Log.i(TAG, "doInBackground");

			if (HttpConnect.isOnline(getActivity())) {
				tempArray = pars.getServerMessages();
				if (tempArray != null) {
					news = tempArray;
				} else {
					publishProgress(false);
				}
			}
			return null;
		}

		/**
		 * Metoda umozliwia aktualizowanie watku glownego podczas dzialania
		 * PlanChangesActivity
		 */
		@Override
		protected void onProgressUpdate(Boolean... values) {
			super.onProgressUpdate(values);
			if (values[0] == false)
				Toast.makeText(getActivity(),
						getResources().getString(R.string.loadPlanChanges),
						Toast.LENGTH_SHORT).show();
		}

		/** Metoda wykonywana przed doInBackground() */
		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
			progress = new ProgressDialog(getActivity());
			progress.setMessage(getResources().getString(
					R.string.dataCollection));
			progress.show();
			progress.setCancelable(false);
			enableExecuteRefresh = false;
		}

		/** Metoda wykonywana po doInBackground() */
		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, "onPostExecute");
			progress.dismiss();
			if (tempArray != null) {
				refreshListView();
				publishProgress(true);
			}
			enableExecuteRefresh = true;
		}
	}

}
