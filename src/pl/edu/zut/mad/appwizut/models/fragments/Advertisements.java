package pl.edu.zut.mad.appwizut.models.fragments;

import pl.edu.zut.mad.appwizut.connection.HTTPLinks;

public class Advertisements extends UpdateAndSetNews {

	public Advertisements() {
		super();
		super.startConnect(HTTPLinks.OGLOSZENIA);
	}
}
