package de.thorstenwitt.mensaapp.parser;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;

import de.thorstenwitt.mensaapp.common.businessobject.Mensa;

/**
 * Created by Herre on 27.02.2017.
 */

@RunWith(PowerMockRunner.class)
public class LunchParserTest {

    private LunchParserMock lpMock;

    @Before
    public void setUp() {
        lpMock = new LunchParserMock();
    }

    @Test
    public void testGetInstance() {
        LunchParser lp = LunchParser.getInstance();
        Assert.assertNotNull(lp);
    }

    @Test
    public void testGetInstance_WhenAlreadyCreated() {
        LunchParser.getInstance();
        LunchParser lp = LunchParser.getInstance();
        Assert.assertNotNull(lp);
    }

    @Test
    public void testGetLunchDataForAllMensa() {
        LunchParser lp = LunchParser.getInstance();
        lp.mensaList = lpMock.getMensaListMock();
        List<Mensa> mensaList = lp.getLunchDataForAllMensas();
        Assert.assertNotNull(mensaList);
        Assert.assertEquals(mensaList.size(), lpMock.getMensaListMock().size());
    }

    @Test
    public void testParse() throws IOException {
        LunchParser lp = LunchParser.getInstance();
        lp.parse();

        Assert.assertTrue(lp.mensaList.size()==4);
        Assert.assertTrue(lp.mensaList.get(0).getLunchOffers().size()>0);
    }
}
