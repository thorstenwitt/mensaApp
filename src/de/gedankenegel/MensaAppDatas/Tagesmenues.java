package de.gedankenegel.MensaAppDatas;

import java.util.ArrayList;


public class Tagesmenues {
	public String mydate;
	public ArrayList<Mittagsgericht> mittagsgerichte;
	
	
	
	public Tagesmenues(String mydate, ArrayList<Mittagsgericht> mittagsgerichte) {
		this.mydate = mydate;
		this.mittagsgerichte = mittagsgerichte;
	}
	public String getMydate() {
		return mydate;
	}
	public void setMydate(String mydate) {
		this.mydate = mydate;
	}
	public ArrayList<Mittagsgericht> getMittagsgerichte() {
		return mittagsgerichte;
	}
	public void setMittagsgerichte(ArrayList<Mittagsgericht> mittagsgerichte) {
		this.mittagsgerichte = mittagsgerichte;
	}

}
