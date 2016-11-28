/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
 */
public class TriangulationTest {
    
    public TriangulationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getNetworks method, of class Triangulation.
     */
//    @Test
//    public void testGetNetworks() throws Exception {
//        System.out.println("getNetworks");
//        Triangulation.getNetworks();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of main method, of class Triangulation.
     */
//    @Test
//    public void testMain() {
//        System.out.println("main");
//        String[] args = null;
//        Triangulation.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
    @Test
    public void testComputeLocation() {
        System.out.println("Compute Location");
        ArrayList<WiFiNetwork> wifilist = new ArrayList<WiFiNetwork>();
        wifilist.add(new WiFiNetwork("test", "test", 10, 12, 0, 1, 0, "test1", "time", false));
        wifilist.add(new WiFiNetwork("test", "test", 10, 12, -2, 1, -0, "test1", "time", false));
        wifilist.add(new WiFiNetwork("test", "test", 10, 12, -1, 1, -1, "test1", "time", false));
        Triangulation.computeLocation(wifilist);
    }
    
}
