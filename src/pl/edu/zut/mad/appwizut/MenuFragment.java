package pl.edu.zut.mad.appwizut;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
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
        getActivity().getActionBar().setTitle(rivers[position]);
 
        return view;
    }
}
