package pl.edu.zut.mad.appwizut;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SampleFragment extends Fragment {

	private static final String ARG_MESSAGE = "SampleFragment.message";

	static Bundle getArgumentsForMessage(String message) {
		Bundle arguments = new Bundle();
		arguments.putString(ARG_MESSAGE, message);
		return arguments;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sample_fragment, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_content);
        tv.setText(getArguments().getString(ARG_MESSAGE));

        return view;
    }
}
