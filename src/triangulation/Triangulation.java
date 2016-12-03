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
import java.util.LinkedHashSet;
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
//    static void getNetworks() throws ClassNotFoundException{
//        ArrayList<ArrayList<WiFiNetwork>> groupedNetworks = new ArrayList<ArrayList<WiFiNetwork>>();
//        ArrayList<WiFiNetwork> networkGroupTemp = new ArrayList<WiFiNetwork>();
//        Set<String> ssidSet = new HashSet<String>();
//        Class.forName("org.sqlite.JDBC");
//        Connection connection = null;
//        try{
//            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/user/Documents/NetBeansProjects/Triangulation/src/res/wifipicker.db");
//            Statement statement = connection.createStatement();
//            statement.setQueryTimeout(30);
//            ResultSet networkList = statement.executeQuery("SELECT * FROM WIFINETWORKS");
//            while(networkList.next()){
//                ssidSet.add(networkList.getString("BSSID"));
//            }
//            //ssidSet.forEach(System.out::println);
//            Iterator iter = ssidSet.iterator();
//            while(iter.hasNext()){
//                ResultSet networkGroup = statement.executeQuery("SELECT * FROM WIFINETWORKS WHERE BSSID = '"+iter.next()+"'");
//                while(networkGroup.next()){
//                    networkGroupTemp.add(new WiFiNetwork(networkList.getString("BSSID"),
//                                            networkList.getString("CAPABILITIES"),
//                                            networkList.getInt("FREQUENCY"),
//                                            networkList.getLong("ID"),
//                                            networkList.getDouble("LATITUDE"),
//                                            networkList.getInt("LEVEL"),
//                                            networkList.getDouble("LONGITUTDE"),
//                                            networkList.getString("SSID"),
//                                            networkList.getString("TIMESTAMPDATETIME"),
//                                            networkList.getBoolean("triangulated")));
//                }
//                computeLocation(networkGroupTemp);
//                networkGroupTemp.clear();
//            }
//            /*
//            while(networkList.next()){
//                System.out.println("SSID: " + networkList.getString("SSID")
//                + " Level: " + networkList.getString("LEVEL")
//                + "Longitude: " + networkList.getString("LONGITUTDE")
//                + "Latitude: " + networkList.getString("LATITUDE"));
//            bufferlist.add(new WiFiNetwork(networkList.getString("BSSID"),
//                                            networkList.getString("CAPABILITIES"),
//                                            networkList.getInt("FREQUENCY"),
//                                            networkList.getLong("ID"),
//                                            networkList.getDouble("LATITUDE"),
//                                            networkList.getInt("LEVEL"),
//                                            networkList.getDouble("LONGITUTDE"),
//                                            networkList.getString("SSID"),
//                                            networkList.getString("TIMESTAMPDATETIME"),
//                                            networkList.getBoolean("triangulated")));
//            }*/
//        }catch(SQLException e){
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//    }
    /*
    Point weightCenter(ArrayList<powerline> powerlines){
        
    }
    */
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Point> points = new ArrayList<Point>();
//        points.add(new Point(0, 0, 1));
//        points.add(new Point(0, 2, 1));
//        points.add(new Point(3, 1, 2));
//        points.add(new Point(0, 0, 1));
//        points.add(new Point(0, 2, 1));
//        points.add(new Point(2, 0, 1));
        points.add(new Point(0, 0, 1));
        points.add(new Point(0, 2, 1));
        points.add(new Point(2, 0, 1));
        points.add(new Point(2, 2, 2));
        ArrayList<Powerline> powerlines = computePowerlines(points);
        for(int i=0; i<powerlines.size(); i++){
            System.out.println(powerlines.get(i).getYFactor() + "*y = " + powerlines.get(i).getXFactor() + "*x + " + powerlines.get(i).getConstant());
        }
        ArrayList<Point> computedPoints = figureVertices(powerlines);
        for(int i = 0; i<computedPoints.size(); i++){
            System.out.println(computedPoints.get(i).getX() + " , " + computedPoints.get(i).getY());
        }
//        try {
//            getNetworks();
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Triangulation.class.getName()).log(Level.SEVERE, null, ex);
//        }
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
    private static double[] determinants(double ax1, double by1, double c1, double ax2, double by2, double c2) {
        
        double w = (ax1*-by2) - (-by1*ax2);
        double wx = (-c1*-by2) - (-by1*-c2);
        double wy = (ax1*-c2) - (-c1*ax2);
        if(w==0){
            return null;
        }else{
            double[] tab = {(wx/w),(wy/w)};
            return tab;
        }
    }
    private static ArrayList<Point> figureVertices(ArrayList<Powerline> lines){
        ArrayList<Point> mPoints = new ArrayList<Point>();
        double[] buff = new double[2];
        for(int i=0; i<lines.size(); i++){
            for(int j=i+1; j<lines.size(); j++){
                buff = determinants(lines.get(i).getXFactor(), lines.get(i).getYFactor(), lines.get(i).getConstant(),
                        lines.get(j).getXFactor(),lines.get(j).getYFactor(),lines.get(j).getConstant());
                if(buff != null){
                    mPoints.add(new Point(buff[0], buff[1], 0));
                }
            }
        }
        return mPoints;
    }
    
    private static ArrayList<Powerline> computePowerlines(ArrayList<Point> mPoints){
        ArrayList<Powerline> listOfPowerlines = new ArrayList<Powerline>();
        for(int i=0; i<mPoints.size(); i++){
            for(int j=i+1; j<mPoints.size(); j++){
                listOfPowerlines.add(new Powerline(mPoints.get(i), mPoints.get(j)));
            }
        }
        return listOfPowerlines;
    }
    
    private static double[] compare(double[] tab){
        if(tab[0] == tab[1]){
            System.out.println("test");
            return null;
        }else{
            return tab;
        }
    }
}
