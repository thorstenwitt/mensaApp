package de.stdevelopment.MensaAppDatas;

import java.util.ArrayList;


public class Mittagsgericht {
	String mName;
	float preisStud;
	float preisMit;
	float preisGast;
	ArrayList<Mittagsgericht> m = new ArrayList<Mittagsgericht>();

	 
	
	public Mittagsgericht() {
		
	}
	public Mittagsgericht(String lName,float lpreisStud,float lpreisMit, float lpreisGast) {
		
		mName = lName;
		preisStud = lpreisStud;
		preisMit = lpreisMit;
		preisGast = lpreisGast;
	}
	
	public void addMittagsgericht(String lName,float lpreisStud,float lpreisMit, float lpreisGast) {
		/*mName = lName;
		preisStud = lpreisStud;
		preisMit = lpreisMit;
		preisGast = lpreisGast;
		*/
		m.add(new Mittagsgericht(lName,lpreisStud,lpreisMit,lpreisGast));
		
	}
	
	public ArrayList<Mittagsgericht> returnGerichte () {
		
		return m;
	}
	// 1=Student, 2=Mitarbeiter, 3=Gast

	public ArrayList<String> returnGerichtsnamen () {
		ArrayList<String> gn = new ArrayList<String>();
		for (int i=0; i<m.size(); i++) {
			if(m.get(i).mName.equals("")) {
				gn.add("<Unbenanntes Mittagsgericht>");
			}
			else {
				gn.add(m.get(i).mName);
			}
			
		}
		
		
		return gn;
	}
	
	
}

	

