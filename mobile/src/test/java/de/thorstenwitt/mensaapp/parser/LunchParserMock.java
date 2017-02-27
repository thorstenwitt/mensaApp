package de.thorstenwitt.mensaapp.parser;

import java.util.ArrayList;
import java.util.List;

import de.thorstenwitt.mensaapp.common.businessobject.Lunch;
import de.thorstenwitt.mensaapp.common.businessobject.LunchOffer;
import de.thorstenwitt.mensaapp.common.businessobject.Mensa;

/**
 * Created by Herre on 27.02.2017.
 */

public class LunchParserMock {

    private ArrayList<Mensa> mensaList;

    public LunchParserMock() {
        ArrayList<Lunch> lunchList1 = new ArrayList<>();
        ArrayList<Lunch> lunchList2 = new ArrayList<>();
        ArrayList<LunchOffer> lunchOffers1 = new ArrayList<>();
        ArrayList<LunchOffer> lunchOffers2 = new ArrayList<>();
        mensaList = new ArrayList<>();
        Lunch l1 = new Lunch("reis", 1f,2f,3f, false);
        Lunch l2 = new Lunch("pommes", 2f,3f,4f, false);
        Lunch l3 = new Lunch("nodeln", 3f,4f,5f, false);

        lunchList1.add(l1);
        lunchList1.add(l2);
        lunchList2.add(l3);

        LunchOffer lunchOffer1 = new LunchOffer("2017-01-04", lunchList1);
        LunchOffer lunchOffer2 = new LunchOffer("2017-01-04", lunchList2);
        LunchOffer lunchOffer3 = new LunchOffer("2017-01-05", lunchList2);

        lunchOffers1.add(lunchOffer1);
        lunchOffers1.add(lunchOffer3);
        lunchOffers2.add(lunchOffer2);

        Mensa mensa1 = new Mensa("Mensa1", lunchOffers1);
        Mensa mensa2 = new Mensa("Mensa2", lunchOffers2);

        mensaList.add(mensa1);
        mensaList.add(mensa2);

    }

    public ArrayList<Mensa> getMensaListMock() {
        return mensaList;
    }



}
