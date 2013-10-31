package pl.edu.zut.mad.appwizut;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
 
        int position = getArguments().getInt("position");

        String[] rivers = getResources().getStringArray(R.array.menu_items);
        
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
 
        TextView tv = (TextView) view.findViewById(R.id.tv_content);
        tv.setText(rivers[position]);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(rivers[position]);
 
        return view;
    }
}
