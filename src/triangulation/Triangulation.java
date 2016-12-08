/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private static int precision = 40;
    public static void main(String[] args) {
        ArrayList<Point> points = new ArrayList<Point>();
//        points.add(new Point(0, 0, 1));
//        points.add(new Point(0, 2, 1));
//        points.add(new Point(3, 1, 2));
//        points.add(new Point(0, 0, 1));
//        points.add(new Point(0, 2, 1));
//        points.add(new Point(2, 0, 1));
        points.add(new Point(new BigDecimal("17.01820382"), new BigDecimal("53.90794026"), new BigDecimal("0.00000000000000000089")));
        points.add(new Point(new BigDecimal("17.0182951"), new BigDecimal("53.9078353"), new BigDecimal("0.00000000000000000089")));
        points.add(new Point(new BigDecimal("17.0183422"), new BigDecimal("53.9077724"), new BigDecimal("0.00000000000000000086")));
        points.add(new Point(new BigDecimal("17.01839036"), new BigDecimal("53.90767927"), new BigDecimal("0.00000000000000000086")));
        ArrayList<Powerline> powerlines = computePowerlines(points);
        for(int i=0; i<powerlines.size(); i++){
            System.out.println(powerlines.get(i).getYFactor() + "*y = " + powerlines.get(i).getXFactor() + "*x + " + powerlines.get(i).getConstant());
        }
        ArrayList<Point> computedPoints = figureVertices(powerlines);
        for(int i = 0; i<computedPoints.size(); i++){
            System.out.println(computedPoints.get(i).getY() + " , " + computedPoints.get(i).getX());
        }
        Point center = computeCentroid(computedPoints);
        System.out.println("------------");
        System.out.println(String.valueOf(center.getY()) + " , " + String.valueOf(center.getX()));
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
     * @return returns table of cordinates in form of [BigDecimal x, BigDecimal y] or returns null if denouement is indetermined or conflicted
     */
    private static BigDecimal[] determinants(BigDecimal ax1, BigDecimal by1, BigDecimal c1, BigDecimal ax2, BigDecimal by2, BigDecimal c2) {
        
        BigDecimal w = ax1.multiply(by2.negate()).subtract(by1.negate().multiply(ax2));
        BigDecimal wx = c1.negate().multiply(by2.negate()).subtract(by1.negate().multiply(c2.negate()));
        BigDecimal wy = ax1.multiply(c2.negate()).subtract(c1.negate().multiply(ax2));
        if(w.compareTo(new BigDecimal("0")) == 0){
            return null;
        }else{
            BigDecimal[] tab = {(wx.divide(w, precision, RoundingMode.HALF_UP)),(wy.divide(w, precision, RoundingMode.HALF_UP))};
            return tab;
        }
    }
    private static ArrayList<Point> figureVertices(ArrayList<Powerline> lines){
        ArrayList<Point> mPoints = new ArrayList<Point>();
        BigDecimal[] buff = new BigDecimal[2];
        for(int i=0; i<lines.size(); i++){
            for(int j=i+1; j<lines.size(); j++){
                buff = determinants(lines.get(i).getXFactor(), lines.get(i).getYFactor(), lines.get(i).getConstant(),
                        lines.get(j).getXFactor(),lines.get(j).getYFactor(),lines.get(j).getConstant());
                if(buff != null){
                    mPoints.add(new Point(buff[0], buff[1], new BigDecimal("0")));
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
    private static Point computeCentroid(ArrayList<Point> mPoints){
    BigDecimal zero = new BigDecimal("0");
    Point centroid = new Point(zero, zero, zero);
    BigDecimal signedArea = zero;
    BigDecimal x0 = zero; // Current vertex X
    BigDecimal y0 = zero; // Current vertex Y
    BigDecimal x1 = zero; // Next vertex X
    BigDecimal y1 = zero; // Next vertex Y
    BigDecimal a = zero;  // Partial signed area
    BigDecimal centerx = zero;
    BigDecimal centery = zero;
    // For all vertices
    
    for (int i=0; i<mPoints.size(); ++i)
    {
        x0 = mPoints.get(i).getX();
        y0 = mPoints.get(i).getY();
        x1 = mPoints.get((i+1) % mPoints.size()).getX();
        y1 = mPoints.get((i+1) % mPoints.size()).getY();
        a = x0.multiply(y1).subtract(x1.multiply(y0));
        signedArea = signedArea.add(a);
        centerx = centerx.add(x0.add(x1).multiply(a));
        centery = centery.add(y0.add(y1).multiply(a));
    }
    signedArea = signedArea.multiply(new BigDecimal("0.5"));
    centerx = centerx.divide(signedArea.multiply(new BigDecimal("6")), precision, RoundingMode.HALF_UP);
    centery = centery.divide(signedArea.multiply(new BigDecimal("6")), precision, RoundingMode.HALF_UP);
    
    centroid.setX(centerx);
    centroid.setY(centery);

    return centroid;
    }
    
    private static BigDecimal[] compare(BigDecimal[] tab){
        if(tab[0] == tab[1]){
            System.out.println("test");
            return null;
        }else{
            return tab;
        }
    }
}
