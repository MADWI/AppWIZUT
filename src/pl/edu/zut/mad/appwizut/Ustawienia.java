package pl.edu.zut.mad.appwizut;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.WindowManager.LayoutParams;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.edu.zut.mad.appwizut.connection.HttpConnect;
import pl.edu.zut.mad.appwizut.connection.PlanDownloader;


public class Ustawienia extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    ListPreference stopienList;
    ListPreference trybList;
    ListPreference rokList;
    ListPreference kierunekList;
    ListPreference grupaList;
    Preference pobierz;

    String stopien = "";
    String tryb = "";
    String kierunek = "";
    String rok = "";
    String grupa = "";
    String regex = "";

    SharedPreferences ustawienia;

    public ArrayList<String> stacjonarne, niestacjonarne, dozwolone;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
    }


    private void setupSimplePreferencesScreen() {
        // <!-- http://code.google.com/p/android/issues/detail?id=4611 #20 komentarz-->

        if (getIntent().getData() != null && getIntent().getData().toString().equals("preferences://activity2")) {
            addPreferencesFromResource(R.xml.ustawienia2);

        } else {
            addPreferencesFromResource(R.xml.ustawienia);
            final Preference about = (Preference) findPreference("about");

            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                public boolean onPreferenceClick(Preference preference) {

                    final Dialog dialog = new Dialog(Ustawienia.this);
                    dialog.setContentView(R.layout.dialog_info_layout);
                    dialog.setTitle(getString(R.string.about_title));
                    dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    Button dialogButton = (Button) dialog
                            .findViewById(R.id.btnOkDialog);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                    return true;
                }
            });


            final Preference plans = (Preference) findPreference("plans");

            plans.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Ustawienia.this, RemovePlans.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Ustawienia.this.startActivity(intent);
                    return true;
                }
            });

            return;
        }

        pobierz = (Preference) findPreference("pobierz");

        pobierz.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                if (!grupa.equals("") && trybList.getValue() != null) {
                    class PobraniePdfa extends AsyncTask<Void, Void, Boolean> {


                        @Override
                        protected Boolean doInBackground(Void[] objects) {
                            Boolean resultpobrania = PlanDownloader.downloadPlan(getApplicationContext(),tryb, grupa);

                            return resultpobrania;
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            if (result == true)
                                Toast.makeText(getApplicationContext(), getString((R.string.successfull_collected_plan)), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), getString((R.string.something_went_wrong)), Toast.LENGTH_SHORT).show();
                        }
                    }
                    ;

                    if(HttpConnect.isOnline(getApplicationContext()))
                    {
                        PobraniePdfa pobranie = new PobraniePdfa();
                        pobranie.execute();
                    }


                } else
                    Toast.makeText(getApplicationContext(), getString((R.string.group_not_selected)), Toast.LENGTH_SHORT).show();


                return true;
            }
        });


        stopienList = ((ListPreference) findPreference("stopien"));
        trybList = (ListPreference) findPreference("tryb");
        rokList = (ListPreference) findPreference("rok");
        kierunekList = (ListPreference) findPreference("kierunek");
        grupaList = (ListPreference) findPreference("grupa");

        stopienList.setOnPreferenceChangeListener(this);
        trybList.setOnPreferenceChangeListener(this);
        rokList.setOnPreferenceChangeListener(this);
        grupaList.setOnPreferenceChangeListener(this);
        kierunekList.setOnPreferenceChangeListener(this);


        stacjonarne = new ArrayList<String>();
        niestacjonarne = new ArrayList<String>();
        dozwolone = new ArrayList<String>();


        ustawienia = PreferenceManager.getDefaultSharedPreferences(this);


        //wczytanie wczesniejszych wartosci
        stopien = ustawienia.getString("stopien", "");
        rok = ustawienia.getString("rok", "");
        tryb = ustawienia.getString("tryb", "");
        kierunek = ustawienia.getString("kierunek", "");
        grupa = ustawienia.getString("grupa", "");

        //ustawienie wczesniej wybr wartosci
        stopienList.setValue(stopien);
        kierunekList.setValue(kierunek);
        trybList.setValue(tryb);
        rokList.setValue(rok);

        //ustawienie summary
        int index_stopien = stopienList.findIndexOfValue(stopien);
        int index_rok = rokList.findIndexOfValue(rok);
        int index_tryb = trybList.findIndexOfValue(tryb);
        int index_kierunek = kierunekList.findIndexOfValue(kierunek);

        stopienList.setSummary(index_stopien >= 0 ? stopienList.getEntries()[index_stopien] : null);
        rokList.setSummary(index_rok >= 0 ? rokList.getEntries()[index_rok] : null);
        trybList.setSummary(index_tryb >= 0 ? trybList.getEntries()[index_tryb] : null);
        kierunekList.setSummary(index_kierunek >= 0 ? kierunekList.getEntries()[index_kierunek] : null);


        filtrujGrupy();

        grupaList.setValue(grupa);
        int index_grupa = grupaList.findIndexOfValue(grupa);
        grupaList.setSummary(index_grupa >= 0 ? grupaList.getEntries()[index_grupa] : null);

        if(index_grupa<0)
            pobierz.setEnabled(false);

        if (HttpConnect.isOnline(getApplicationContext())) {

            PobieranieGrup pg = new PobieranieGrup(this, ustawienia.edit());
            pg.execute();


        } else {


            stacjonarne = stringToAL(ustawienia.getString("stacjonarne", ""));
            niestacjonarne = stringToAL(ustawienia.getString("niestacjonarne", ""));

            if (stacjonarne.size() == 0 && niestacjonarne.size() == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString((R.string.no_connection_and_local_data)), Toast.LENGTH_SHORT).show();

            }
        }

    }


    public boolean onPreferenceChange(Preference preference, Object value) {

        //zmiana tytulu na obecna wartosc
        String stringValue = value.toString();
        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);

        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
        }


        if (preference == findPreference("tryb")) {
            tryb = (String) value;
        } else if (preference == findPreference("kierunek")) {
            kierunek = (String) value;
        } else if (preference == findPreference("rok")) {
            rok = (String) value;
        } else if (preference == findPreference("grupa")) {
            grupa = (String) value;
        } else if (preference == findPreference("stopien")) {
            stopien = (String) value;
        }
        filtrujGrupy();


        zapiszZmiany();
        return true;
    }


    public void zapiszZmiany() {
        ustawienia.edit().putString("grupa", grupa).putString("rok", rok).putString("tryb", tryb).putString("kierunek", kierunek).putString("stopien", stopien).apply();
    }


    public void filtrujGrupy() {
        String trybDoRegexa = "";
        if (tryb.equals("Niestacjonarne"))
            trybDoRegexa = "n";

        regex = "^" + kierunek + stopien + trybDoRegexa + "-" + rok + ".*";


        dozwolone.clear();

        if (tryb.equals("Niestacjonarne")) {
            for (String grupa : niestacjonarne) {
                if (grupa.matches(regex))
                    dozwolone.add(grupa);
            }
        } else if (tryb.equals("Stacjonarne")) {
            for (String grupa : stacjonarne) {
                if (grupa.matches(regex))
                    dozwolone.add(grupa);
            }

        }

        grupaList.setEntries(dozwolone.toArray(new CharSequence[dozwolone.size()]));
        grupaList.setEntryValues(dozwolone.toArray(new CharSequence[dozwolone.size()]));
        if (dozwolone.size() == 0) {
            grupaList.setEnabled(false);
            pobierz.setEnabled(false);
        } else {
            grupaList.setEnabled(true);
            pobierz.setEnabled(true);
        }
    }

    public String[] getGroups(String rodzajStudiow) {

        String TAG = "getGroups";
        String siteIn = "http://wi.zut.edu.pl/plan/Wydruki/PlanGrup/";

        HttpConnect con = new HttpConnect(10000, siteIn + rodzajStudiow);

        String site = "";


        try {
            site = con.getPage();
            Log.d(TAG, "Polaczono ze strona");
        } catch (Exception e) {

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.e(TAG, e.toString() + errors.toString());
        }

        if ("" == site) {
            Log.e(TAG, "Error con.getStrona()");
        }
        Log.e(TAG, "strona:" + site);

        // wybor kierunku i roku
        Pattern p = Pattern.compile(">(BI|I|ZIP|IC){1}" + "([1-3])" + "(n)?" + "-" + "[1-4]"
                + "[0-9x]{1,2}\\.pdf<");


        /**
         * W momencie gdy zle zostana podane dane: rok; kierunek; rodzaj;
         */
        if (null == p) {
            Log.d(TAG, "bledne dane, zwracam nulla...");
            return null;
        }


        Matcher m = p.matcher(site);
        int i = 0;
        while (m.find()) {
            i++;
        }
        m.reset();
        String[] outputTab = new String[i];
        i = 0;
        while (m.find()) {
            outputTab[i] = m.group().subSequence(1, m.group().indexOf(".pdf"))
                    .toString();
            Log.d(TAG, outputTab[i]);
            i++;
        }
        return outputTab;
    }


    class PobieranieGrup extends AsyncTask<Void, Void, Void> {

        ProgressDialog progDailog;
        SharedPreferences.Editor edytor;
        Ustawienia kontekst;

        PobieranieGrup(Ustawienia akontekst, SharedPreferences.Editor aedytor) {
            kontekst = akontekst;
            edytor = aedytor;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            for (String grupa : kontekst.getGroups("Stacjonarne"))
                kontekst.stacjonarne.add(grupa);

            for (String grupa : kontekst.getGroups("Niestacjonarne"))
                kontekst.niestacjonarne.add(grupa);

            edytor.putString("stacjonarne", stacjonarne.toString());
            edytor.putString("niestacjonarne", niestacjonarne.toString());
            edytor.apply();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progDailog.dismiss();
            kontekst.filtrujGrupy();


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(kontekst);
            progDailog.setMessage(getResources().getString((R.string.downloading_in_progress)));
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.show();
        }
    }


    private ArrayList<String> stringToAL(String str) {
        str = str.substring(1, str.length() - 1);
        List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
        return new ArrayList<String>(items);
    }

}
