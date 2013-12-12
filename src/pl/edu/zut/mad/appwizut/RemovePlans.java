package pl.edu.zut.mad.appwizut;

import android.app.Activity;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import pl.edu.zut.mad.appwizut.connection.PlanDownloader;
import pl.edu.zut.mad.appwizut.utils.Constans;

/** Klasa do usuwania planow zajec */
public class RemovePlans extends Activity implements OnClickListener {

    /** Lista planow zajec */
    private List<String> fileList = new ArrayList<String>();

    /** Obiekt klasy File okreslajacy folder glowny */
    private File root = new File(Environment.getExternalStorageDirectory()
            + Constans.PLAN_FOLDER);

    /** ListView zawierajace sciagniete plany */
    private ListView listPlans;

    /**
     * Obiekt klasy Button - przycisk usuwajacy wszystkie plany
     */
    private Button removePlans;

    /** Metoda wykonywana przy starcie aktywnosci */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_plans);

        listPlans = (ListView) findViewById(R.id.listPlany);
        removePlans = (Button) findViewById(R.id.btnUsunPlany);
        removePlans.setOnClickListener(this);

        ListDir(root);

    }

    /**
     * Metoda uzupelniajaca liste sciagnietych planow
     *
     * @param f
     *            sciezka do folderu
     */
    private void ListDir(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            fileList.clear();
            for (File file : files) {
                fileList.add(file.getPath());
            }

            ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, fileList);
            listPlans.setAdapter(directoryList);
            listPlans.setClickable(true);
            listPlans.setOnItemClickListener(myClickListener);
        }

    }

    public OnItemClickListener myClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1,
                                final int position, long arg3) {

            AlertDialog.Builder choseDialogBuildier = new AlertDialog.Builder(
                    RemovePlans.this);

            choseDialogBuildier.setItems(R.array.actions_on_plan,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (which == 0) // otworz plan
                            {
                                File file = new File(fileList.get(position));

                                if (file.exists()) {
                                    Uri path = Uri.fromFile(file);
                                    Intent intent = new Intent(
                                            Intent.ACTION_VIEW);
                                    intent.setDataAndType(path,
                                            "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            } else if (which == 1) // usun plan
                            {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        RemovePlans.this);
                                alertDialogBuilder
                                        .setTitle(getString(R.string.remove_plan_alert_dialog));

                                alertDialogBuilder
                                        .setMessage(
                                                R.string.remove_plan_message)
                                        .setCancelable(false)
                                        .setPositiveButton(
                                                "Tak",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int id) {

                                                        PlanDownloader
                                                                .removePlan(fileList
                                                                        .get(position));
                                                        ListDir(root);
                                                    }
                                                })
                                        .setNegativeButton(
                                                "Nie",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int id) {

                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alertDialog = alertDialogBuilder
                                        .create();
                                alertDialog.show();
                            }
                        }
                    });

            AlertDialog choseDialog = choseDialogBuildier.create();
            choseDialog.show();

        }
    };

    /**
     * Metoda wywolywana po kliknieciu w dane View
     *
     * @param v
     *            View ktore ma byc obsluzone
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUsunPlany:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        RemovePlans.this);

                // set title
                alertDialogBuilder
                        .setTitle(getString(R.string.remove_plans_alert_dialog));

                // set dialog message
                alertDialogBuilder
                        .setMessage(R.string.remove_plans_message)
                        .setCancelable(false)
                        .setPositiveButton("Tak",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        PlanDownloader.removePlans();
                                        finish();
                                    }
                                })
                        .setNegativeButton("Nie",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();

    }

}