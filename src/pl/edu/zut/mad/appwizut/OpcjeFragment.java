package pl.edu.zut.mad.appwizut;

import android.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.widget.Toast;
//import android.R;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Marcin on 30.10.13.
 */
public class OpcjeFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener{

    Spinner listaStopien;
    Spinner listaTyp;
    Spinner listaKierunek;
    Spinner listaGrupa;
    Spinner listaRok;

    TypedArray rokStudiowWartosci;
    TypedArray kierunekStudiowWartosci;
    TypedArray typStudiowWartosci;
    TypedArray stopnieStudiowWartosci;

    String stopien="";
    String typ="";
    String kierunek="";
    String rok="";
    String grupa="";
    String regex="";

    ArrayList<String> stacjonarne,niestacjonarne;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.opcje_fragment, container, false);
        rokStudiowWartosci = getResources().obtainTypedArray(R.array.rokStudiowWartosci);
        kierunekStudiowWartosci = getResources().obtainTypedArray(R.array.kierunekStudiowWartosci);
        typStudiowWartosci = getResources().obtainTypedArray(R.array.typStudiowWartosci);
        stopnieStudiowWartosci = getResources().obtainTypedArray(R.array.stopnieStudiowWartosci);



        listaGrupa = (Spinner) view.findViewById(R.id.listaGrup);
        listaTyp = (Spinner) view.findViewById(R.id.listaTyp);
        listaKierunek = (Spinner) view.findViewById(R.id.listaKierunek);
        listaStopien = (Spinner) view.findViewById(R.id.listaStopien);
        listaRok = (Spinner) view.findViewById(R.id.listaRok);

        listaGrupa.setOnItemSelectedListener(this);
        listaTyp.setOnItemSelectedListener(this);
        listaKierunek.setOnItemSelectedListener(this);
        listaStopien.setOnItemSelectedListener(this);
        listaRok.setOnItemSelectedListener(this);

        //TODO: ustawienie wczesniej wybranych wartosci jesli sa jakies wybrane


        stacjonarne=new ArrayList<String>();
        niestacjonarne=new ArrayList<String>();

        if(true)
        {

            for(String grupa : getGroups("Stacjonarne"))
             stacjonarne.add(grupa);

            for(String grupa : getGroups("Niestacjonarne"))
             niestacjonarne.add(grupa);
        }
        else
        {
            Log.d("HttpConnect", "brak polaczenia a i tak wywala? :F");
        }

        return view;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;
        Boolean zmiana=false;

        if(spinner.getId()==R.id.listaStopien )
        {
            if(!stopien.equals(stopnieStudiowWartosci.getString(i)))
                zmiana=true;

            stopien=stopnieStudiowWartosci.getString(i);
            //Toast.makeText(getActivity().getApplicationContext(),"stopien: "+stopien,Toast.LENGTH_SHORT).show();
        }
        else if(spinner.getId()==R.id.listaKierunek)
        {
            if(!kierunek.equals(kierunekStudiowWartosci.getString(i)))
                zmiana=true;
            kierunek=kierunekStudiowWartosci.getString(i);
            //Toast.makeText(getActivity().getApplicationContext(),"kierunek: "+kierunek,Toast.LENGTH_SHORT).show();
        }
        else if(spinner.getId()==R.id.listaTyp)
        {
            if(!typ.equals(typStudiowWartosci.getString(i)))
                zmiana=true;
            typ=typStudiowWartosci.getString(i);
            //Toast.makeText(getActivity().getApplicationContext(),"typ: "+typ,Toast.LENGTH_SHORT).show();
        }
        else if(spinner.getId()==R.id.listaRok)
        {
            if(!rok.equals(rokStudiowWartosci.getString(i)))
                zmiana=true;
           rok=rokStudiowWartosci.getString(i);
            //Toast.makeText(getActivity().getApplicationContext(),"rok: "+rok,Toast.LENGTH_SHORT).show();
        }
        else if(spinner.getId()==R.id.listaGrup)
            {
                if(grupa.equals(spinner.getSelectedItem().toString()))
                {
                    zmiana=true;
                    grupa=spinner.getSelectedItem().toString();
                    Toast.makeText(getActivity().getApplicationContext(),"zapis grupy"+grupa,Toast.LENGTH_SHORT).show();
                }
            }
        //+dodanie do preferencji

        //Toast.makeText(getActivity().getApplicationContext(),"rok: "+rok+typ+stopien+kierunek,Toast.LENGTH_SHORT).show();

        if(kierunek!="" && stopien!="" && typ!="" && rok!="" && spinner.getId()!=R.id.listaGrup && zmiana)
            filtrujGrupy();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
       if(adapterView.getId()==R.id.listaGrup)
        Toast.makeText(getActivity().getApplicationContext(),"nieznaleziono",Toast.LENGTH_SHORT).show();

        }
    public void filtrujGrupy()
    {
        //nie ma konstruktora bez domyslnych wartosci: <


        
        ArrayAdapter<String> dozwolone= new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1 );


        regex=kierunek+stopien+"-"+rok;


        if(typ.equals("Stacjonarne"))
        {
           for (String grupa : stacjonarne)
            {

                if(grupa.contains(regex))
                     dozwolone.add(grupa);

            }
        }
        else
        {
             for (String grupa : niestacjonarne)
             {
                if(grupa.contains(regex))
                    dozwolone.add(grupa);
             }
        }


        dozwolone.notifyDataSetChanged();
        listaGrupa.setAdapter(dozwolone);
        listaGrupa.refreshDrawableState();

    }

    public String[] getGroups(String rodzajStudiow) {

        String TAG="getGroups";
        String siteIn = "http://wi.zut.edu.pl/plan/Wydruki/PlanGrup/";


        HttpConnect con = new HttpConnect(10000, siteIn + rodzajStudiow);

        String site = "";


        try {
            site = con.getPage();
            Log.d(TAG, "Polaczono ze strona");
        } catch (Exception e) {

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Log.e(TAG, e.toString()+errors.toString());
        }

        if ("" == site) {
            Log.e(TAG, "Error con.getStrona()");
        }
        Log.e(TAG, "strona:"+site);

        // wybor kierunku i roku
        Pattern p = Pattern.compile(">(BI|I|ZIP|IC){1}" + "([1-3])" + "-" + "[1-4]"
                + "[0-9]{1,2}\\.pdf<");

        // sP = this.getRodzaj(rodzajStudiow, kierunek, stopien, rok);

        /**
         * W momencie gdy zle zostana podane dane: rok; kierunek; rodzaj;
         */
        if (null == p) {
            Log.d(TAG, "b�edne dane, zwracam nulla...");
            return null;
        }

        // p = Pattern.compile(sP);

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
}
