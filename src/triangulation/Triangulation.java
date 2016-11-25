/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Triangulation {

    private static ArrayList<WiFiNetwork> lines;
    /**
     * Will return ArrayList<WiFiNetwork>
     * @throws ClassNotFoundException 
     */
    static void getNetworks() throws ClassNotFoundException{
        ArrayList<ArrayList<WiFiNetwork>> groupedNetworks = new ArrayList<ArrayList<WiFiNetwork>>();
        ArrayList<WiFiNetwork> networkGroupTemp = new ArrayList<WiFiNetwork>();
        Set<String> ssidSet = new HashSet<String>();
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/user/Documents/NetBeansProjects/Triangulation/src/res/wifipicker.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet networkList = statement.executeQuery("SELECT * FROM WIFINETWORKS");
            while(networkList.next()){
                ssidSet.add(networkList.getString("BSSID"));
            }
            //ssidSet.forEach(System.out::println);
            Iterator iter = ssidSet.iterator();
            while(iter.hasNext()){
                ResultSet networkGroup = statement.executeQuery("SELECT * FROM WIFINETWORKS WHERE BSSID = '"+iter.next()+"'");
                while(networkGroup.next()){
                    networkGroupTemp.add(new WiFiNetwork(networkList.getString("BSSID"),
                                            networkList.getString("CAPABILITIES"),
                                            networkList.getInt("FREQUENCY"),
                                            networkList.getLong("ID"),
                                            networkList.getDouble("LATITUDE"),
                                            networkList.getInt("LEVEL"),
                                            networkList.getDouble("LONGITUTDE"),
                                            networkList.getString("SSID"),
                                            networkList.getString("TIMESTAMPDATETIME"),
                                            networkList.getBoolean("triangulated")));
                }
                computeLocation(networkGroupTemp);
                networkGroupTemp.clear();
            }
            /*
            while(networkList.next()){
                System.out.println("SSID: " + networkList.getString("SSID")
                + " Level: " + networkList.getString("LEVEL")
                + "Longitude: " + networkList.getString("LONGITUTDE")
                + "Latitude: " + networkList.getString("LATITUDE"));
            bufferlist.add(new WiFiNetwork(networkList.getString("BSSID"),
                                            networkList.getString("CAPABILITIES"),
                                            networkList.getInt("FREQUENCY"),
                                            networkList.getLong("ID"),
                                            networkList.getDouble("LATITUDE"),
                                            networkList.getInt("LEVEL"),
                                            networkList.getDouble("LONGITUTDE"),
                                            networkList.getString("SSID"),
                                            networkList.getString("TIMESTAMPDATETIME"),
                                            networkList.getBoolean("triangulated")));
            }*/
        }catch(SQLException e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
    /*
    Location weightCenter(ArrayList<powerline> powerlines){
        
    }
    */
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            getNetworks();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Triangulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * TODO: will return Location
     * @param wifilist 
     */
    private static void computeLocation(ArrayList<WiFiNetwork> wifilist) {
        System.out.println("----------------------------------------------------------"+wifilist.get(0).getSsid());
        ArrayList<Powerline> powerlines = new ArrayList<Powerline>();
        Iterator iter = wifilist.iterator();
        for(int i = 0; i < wifilist.size(); i++){
            for(int j = i + 1; j < wifilist.size(); j++){
                powerlines.add(new Powerline(
                new Location(wifilist.get(i).getLongitude(), wifilist.get(i).getLatitude()),
                new Location(wifilist.get(j).getLongitude(), wifilist.get(j).getLatitude()),
                wifilist.get(i).getLevel(), wifilist.get(j).getLevel()));
            }
        }
//        for(int i = 0; i < powerlines.size(); i++){
//            System.out.println(powerlines.get(i).getYFactor() + "y = " + powerlines.get(i).getXFactor() + "x + " + powerlines.get(i).getConstant());
//        }
        ArrayList<double[]> points = new ArrayList<double[]>();
        for(int i = 0; i < powerlines.size(); i++){
            for(int j = i + 1; j < powerlines.size(); j++){
                points.add(determinants(powerlines.get(i).getXFactor(), 
                                powerlines.get(i).getYFactor(),
                                powerlines.get(i).getConstant(),
                                powerlines.get(j).getXFactor(),
                                powerlines.get(j).getYFactor(),
                                powerlines.get(j).getConstant()));
            }
        }
        for(int i = 0; i < points.size(); i++){
            if(points.get(i) == null) points.remove(i);
            System.out.println(points.get(i)[0] + " , " + points.get(i)[1]);
        }
    }
    /**
     * 
     * @param ax1
     * @param by1
     * @param c1
     * @param ax2
     * @param by2
     * @param c2
     * @return returns table of cordinates in form of [double x, double y] or returns null if denouement is indetermined or conflicted
     */
    private static double[] determinants(double ax1, double by1, double c1, double ax2, double by2, double c2){
        
        double w = (ax1*by2) - (by1*ax2);
        double wx = (c1*by2) - (by1*c2);
        double wy = (ax1*c2) - (c1*ax2);
        if(w==0){
            return null;
        }else{
            double[] tab = {(wx/w),(wy/w)};
            return tab;
        }
    }
}
