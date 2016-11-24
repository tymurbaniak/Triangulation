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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Triangulation {

    private static ArrayList<WiFiNetwork> lines;
    static void getNetworks() throws ClassNotFoundException{
        ArrayList<WiFiNetwork> bufferlist = new ArrayList<WiFiNetwork>();
        Class.forName("org.sqlite.JDBC");
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/user/Documents/NetBeansProjects/Triangulation/src/res/wifipicker.db");
            System.out.println(connection.getCatalog());
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(5);
            ResultSet networkList = statement.executeQuery("SELECT * FROM WIFINETWORKS");
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
            }
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
        lines = new ArrayList<WiFiNetwork>();
        try {
            getNetworks();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Triangulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
