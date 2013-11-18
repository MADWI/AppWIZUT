package pl.edu.zut.mad.appwizut.models.fragments;

import pl.edu.zut.mad.appwizut.connection.HTTPLinks;

public class News extends UpdateAndSetNews {

	public News() {
		super();
		super.setHttpLink(HTTPLinks.AKTUALNOSCI);
		super.startConnect();
	}
}
