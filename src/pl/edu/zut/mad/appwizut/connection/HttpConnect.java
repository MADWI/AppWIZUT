package pl.edu.zut.mad.appwizut.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Prosta klasa do polaczen HTTP.
 * 
 * @author Sebastian Swierczek
 * @version 1.0.8
 */
public class HttpConnect {

    /***
     * Zmienna do debuggowania.
     */
    private static final String TAG = "HttpConnect";

    /** Zmienna przechowujaca zrodlo strony jako String */
    private String strona;

    /** Obiekt klasy HttpClient. */
    private final HttpClient client;

    /** Obiekt klasy HttpGet. */
    private HttpGet requestGET;

    /** Obiekt klasy HttpResponse. */
    private HttpResponse response;

    /** Obiekt klasy HttpEntity */
    private HttpEntity entity;

    /**
     * Konstruktor sluzacy do polaczenia ze strona WWW.
     * 
     * @param timeout
     *            maksymalny czas oczekiwania na odpowiedz serwera w
     *            milisekundach
     * @param adres
     *            adres URL do strony
     */
    public HttpConnect(int timeout, String adres) {
	strona = "";
	HttpParams params = new BasicHttpParams();
	HttpConnectionParams.setConnectionTimeout(params, timeout);
	HttpConnectionParams.setSoTimeout(params, timeout);
	client = new DefaultHttpClient(params);
	try {
	    requestGET = new HttpGet(new URI(adres));
	} catch (URISyntaxException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Metoda do pobrania zrodla strony jako String.
     * 
     * @return zadana strona jako String.
     */
    public String getPage() {
	Log.i(TAG, "getPage");
	if (executeHttpGet() == false) {
	    return "";
	}
	return strona;
    }

    /**
     * Metoda realizujaca pobieranie strony
     * 
     * @return true, jezeli sie powiodlo.
     */
    private boolean executeHttpGet() {
	Log.i(TAG, "executeHttpGet");
	BufferedReader in = null;
	try {
	    response = client.execute(requestGET);
	    entity = response.getEntity();
	    if (entity == null)
		return false;

	    in = new BufferedReader(new InputStreamReader(entity.getContent()));
	    StringBuffer sb = new StringBuffer("");
	    String line = "";
	    String NL = System.getProperty("line.separator");

	    while ((line = in.readLine()) != null) {
		sb.append(line + NL);
	    }
	    strona = sb.toString();

	} catch (IOException e) {
	    Log.e(TAG, "Exception (executeHttpGet) " + e.toString());
	    e.printStackTrace();
	    return false;
	} finally {
	    try {
		entity.consumeContent();
	    } catch (IOException e1) {
		Log.e(TAG, "Exception 1 (executeHttpGet) " + e1.toString());
		e1.printStackTrace();
		return false;
	    }
	    if (in != null) {
		try {
		    in.close();
		} catch (IOException e2) {
		    Log.e(TAG, "Exception 2 (executeHttpGet) " + e2.toString());
		    e2.printStackTrace();
		    return false;
		}
	    }
	}
	return true;
    }

	/**
	 * Metoda sprawdzajaca polaczenie z Internetem
	 * 
	 * @param ctx
	 *            kontekst aplikacji
	 * 
	 * @return true jezeli stwierdzono polaczenie
	 */
	public static boolean isOnline(Context ctx) {
		Log.i(TAG, "isOnline...");

		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.isConnected()) {
			return hasActiveInternetConnection(ctx);
		} else {
			/*
			 * Toast.makeText(ctx, ctx.getString(R.string.no_Internet),
			 * Toast.LENGTH_SHORT).show();
			 */
			Toast.makeText(ctx, "Brak po��czenia z internetem!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/**
	 * Metoda sprawdza, czy mo�na po�aczyc si� z wybran� stron� (lepsze
	 * przetestowanie po��czenia internetowego)
	 * 
	 * @param context
	 *            kontekst aplikacji
	 * @return true je�li po��czono (kod 200), lub false w przeciwnym wypadku)
	 */
	private static boolean hasActiveInternetConnection(Context context) {
		try {
			HttpURLConnection urlc = (HttpURLConnection) (new URL(
					"http://www.google.com").openConnection());
			urlc.setRequestProperty("User-Agent", "Test");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(500);
			urlc.connect();
			return (urlc.getResponseCode() == 200);
		} catch (IOException e) {
			Log.e(TAG, "Error checking internet connection", e);
			return false;
		}
	}
}