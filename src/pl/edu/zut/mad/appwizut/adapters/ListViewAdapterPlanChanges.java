package pl.edu.zut.mad.appwizut.adapters;

import java.util.ArrayList;

import pl.edu.zut.mad.appwizut.R;
import pl.edu.zut.mad.appwizut.models.MessagePlanChanges;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Wlasny adapter dla zmian w planie ListView
 * 
 * @author Sebastian Swierczek
 * @version 1.1.4
 */
public class ListViewAdapterPlanChanges extends
		ArrayAdapter<MessagePlanChanges> {

	/** Staly obiekt typu Context */
	private final Context context;

	/** ArrayList obiektow MessagePlanChanges */
	private ArrayList<MessagePlanChanges> messages = new ArrayList<MessagePlanChanges>();

	/**
	 * Konstruktor klasy
	 * 
	 * @param context
	 *            kontekst aplikacji
	 * @param resource
	 *            co zapisujemy
	 * @param textViewResourceId
	 *            id zasobu
	 * @param objects
	 *            do jakiej ArrayList zapisujemy
	 */
	public ListViewAdapterPlanChanges(Context context, int resource,
			int textViewResourceId, ArrayList<MessagePlanChanges> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.messages = objects;
	}
	
	public ListViewAdapterPlanChanges(Context context, int resource, ArrayList<MessagePlanChanges> objects) {
		super(context, resource, objects);
		this.context = context;
		this.messages = objects;
	}

	/**
	 * Metoda zwracajaca dane View
	 * 
	 * @param position
	 *            indeks pozycji w danym View
	 * @param convertView
	 *            widok z ktorego wczytujemy
	 * @param parent
	 *            nadrzedne View
	 * 
	 * @return rowView zwraca View w postaci rzedu
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.list_item_layout, parent, false);
		TextView title = (TextView) rowView.findViewById(R.id.titleText);
		TextView date =  (TextView) rowView.findViewById(R.id.dateText);
		TextView body =  (TextView) rowView.findViewById(R.id.bodyText);
		
		
		if (messages.get(position) != null)
			body.setVisibility(View.GONE);

		String temp = messages.get(position).getTitle();
		temp = temp.substring(0, 1).toUpperCase()
				+ temp.substring(1, temp.length());

		Spanned sp = Html.fromHtml(temp);
		temp = sp.toString().replaceAll("[\r\n]{1,}$", "");
		title.setText(temp);

		sp = Html.fromHtml(messages.get(position).getBody().trim());
		temp = sp.toString().replaceAll("[\r\n]{1,}$", "");
		body.setText(temp);

		date.setText("Data: " + messages.get(position).getDate());

		return rowView;
	}
}
