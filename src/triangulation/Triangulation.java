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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nevec.rjm.BigDecimalMath;

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
                ArrayList<Point> points = new ArrayList<Point>();
                points.clear();
                ResultSet networkGroup = statement.executeQuery("SELECT * FROM WIFINETWORKS WHERE BSSID = '"+iter.next()+"'");
                networkGroupTemp.clear();
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
                    if((networkList.getDouble("LONGITUTDE") != 0) && (networkList.getDouble("LATITUDE") != 0)){
                        points.add(new Point(new BigDecimal(networkList.getDouble("LONGITUTDE")), new BigDecimal(networkList.getDouble("LATITUDE")), new BigDecimal(networkList.getInt("LEVEL")).divide(new BigDecimal("10000000"))));
                    }
                }
                ArrayList<Point> newPoints = normalize(points);
                System.out.println("-----------" + networkGroupTemp.get(0).getSsid());
                for(int i=0; i<points.size(); i++){
                    System.out.println(points.get(i).getX() + " , " + points.get(i).getY());
                }
                for(int i=0; i<newPoints.size(); i++){
                    System.out.println(newPoints.get(i).getX() + " , " + newPoints.get(i).getY());
                }
                ArrayList<Powerline> powerlines = computePowerlines(newPoints);
//                for(int i=0; i<powerlines.size(); i++){
//                    System.out.println("y = " + powerlines.get(i).getXFactor() + "*x + " + powerlines.get(i).getConstant() + "; plot(x,y);");
//                }
                ArrayList<Point> computedPoints = figureVertices(powerlines);
//                for(int i = 0; i<computedPoints.size(); i++){
//                    System.out.println(computedPoints.get(i).getY() + " , " + computedPoints.get(i).getX());
//                }
                if(computedPoints.size() > 2){
                    System.out.println("-----------");
//                    Point center = computeCentroid(computedPoints);
//                    System.out.println(String.valueOf(center.getY()) + " , " + String.valueOf(center.getX()));
//                    System.out.println("-----------");
                }
            }
        }catch(SQLException e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    /**
     * @param args the command line arguments
     */
    private static int precision = 40;
    public static void main(String[] args) {
        
//        points.add(new Point(0, 0, 1));
//        points.add(new Point(0, 2, 1));
//        points.add(new Point(3, 1, 2));
//        points.add(new Point(0, 0, 1));
//        points.add(new Point(0, 2, 1));
//        points.add(new Point(2, 0, 1));
//        points.add(new Point(new BigDecimal("17.01820382"), new BigDecimal("53.90794026"), new BigDecimal("0.000712")));
//        points.add(new Point(new BigDecimal("17.0182951"), new BigDecimal("53.9078353"), new BigDecimal("0.000712")));
//        points.add(new Point(new BigDecimal("17.0183422"), new BigDecimal("53.9077724"), new BigDecimal("0.000688")));
//        points.add(new Point(new BigDecimal("17.01839036"), new BigDecimal("53.90767927"), new BigDecimal("0.000688")));
//        points.add(new Point(new BigDecimal("16.97098234"), new BigDecimal("53.99411274"), new BigDecimal("0.000088")));
//        points.add(new Point(new BigDecimal("16.971329"), new BigDecimal("53.9943963"), new BigDecimal("0.000088")));
//        points.add(new Point(new BigDecimal("16.97117051"), new BigDecimal("53.994024"), new BigDecimal("0.000088")));
        
        try {
            getNetworks();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Triangulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    private static ArrayList<Point> convertToCartesian(ArrayList<Point> points){
//        
//    }
    private static ArrayList<Point> sortPoints(ArrayList<Point> points){
        Point buff;
        Collections.sort(points, new Comparator<Point>(){
            @Override
            public int compare(Point point1, Point point2){
                return point1.getX().compareTo(point2.getX());
            }
        });
        return points;
    }
    private static final BigDecimal R = new BigDecimal("6372.8"); // In kilometers
    private static BigDecimal haversine(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        BigDecimal dLat = (lat2.subtract(lat1)).multiply(new BigDecimal(Math.PI).divide(new BigDecimal("180")));
        BigDecimal dLon = (lon2.subtract(lon1)).multiply(new BigDecimal(Math.PI).divide(new BigDecimal("180")));
        //BigDecimal dLon = Math.toRadians(lon2 - lon1);
        lat1 = lat1.multiply(new BigDecimal(Math.PI).divide(new BigDecimal("180")));
        lat2 = lat2.multiply(new BigDecimal(Math.PI).divide(new BigDecimal("180")));
 
        BigDecimal a =  BigDecimalMath.powRound(BigDecimalMath.sin(dLat.divide(new BigDecimal("2"))), 2).add( 
            BigDecimalMath.powRound(BigDecimalMath.sin(dLon.divide(new BigDecimal("2"))), 2).multiply(
                BigDecimalMath.cos(lat1).multiply(lat1)
            )
        );
        BigDecimal c = new BigDecimal("2").multiply(BigDecimalMath.asin(BigDecimalMath.sqrt(a)));
        //BigDecimal c = 2 * Math.asin(Math.sqrt(a));
        return R.multiply(c);
    }
    private static ArrayList<Point> normalize(ArrayList<Point> points){
        Point returnPoint = points.get(0);
        ArrayList<Point> retArray = new ArrayList<Point>();
        for(int i = 0; i < points.size(); i++){
            retArray.add(new Point(points.get(i).getX().subtract(returnPoint.getX()),
                                points.get(i).getY().subtract(returnPoint.getY()),
                                points.get(i).getR()));
//            points.get(i).setX(points.get(i).getX().subtract(returnPoint.getX()));
//            points.get(i).setY(points.get(i).getY().subtract(returnPoint.getY()));
        }
        retArray.add(returnPoint);
        return retArray;
    } 
//    private static ArrayList<Point> convertToCordinates(ArrayList<Point> points){
//        
//    }
//    private static ArrayList<Point> denormalize(ArrayList<Point> points){
//        
//    }
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
                if((mPoints.get(j).getY().subtract(mPoints.get(i).getY())).compareTo(BigDecimal.ZERO) != 0)
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
