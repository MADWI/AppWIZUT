package pl.edu.zut.mad.appwizut.models.fragments;

import pl.edu.zut.mad.appwizut.connection.HTTPLinks;

public class ChangesInThePlan extends UpdateAndSetNews{

	public ChangesInThePlan() {
		super();
		super.startConnect(HTTPLinks.ZMIANY_W_PLANIE);
	}
}
