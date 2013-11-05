package pl.edu.zut.mad.appwizut.models;

/**
 * Klasa w ktorej sa zawarte informacje o zmianach w planie
 */
public class MessagePlanChanges {

	/** Zmienna okreslajaca tytul */
	private String title;

	/** Zmienna okreslajaca date */
	private String date;

	/** Zmienna okreslajaca tresc */
	private String body;

	/** Konstruktor inicjalizujacy zmnienne */
	public MessagePlanChanges() {
		this.title = "";
		this.date = "";
		this.body = "";
	}
	
	public MessagePlanChanges(String title, String date, String body) {
		this.title = title;
		this.date = date;
		this.body = body;
	}

	/**
	 * Metoda zwracajaca tytul
	 * 
	 * @return tytul wiadomosci
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Metoda zwracajaca date wiadomosci
	 * 
	 * @return data wiadomosci
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Metoda zwracajaca tresc wiadomosci
	 * 
	 * @return tresc wiadomosci
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Metoda ustawiajaca tytul wiadomosci
	 * 
	 * @param arg
	 *          zadany tytul
	 * */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Metoda ustawiajaca date wiadomosci
	 * 
	 * @param arg
	 *          zadana data
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Metoda ustawiajaca tresc wiadomosci
	 * 
	 * @param arg
	 *          zadana tresc
	 */
	public void setBody(String body) {
		this.body = body;
	}

}