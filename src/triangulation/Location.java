/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

/**
 *
 * @author user
 */
public class Location {
    private double longitude;
    private double latitude;
    
    Location(double lon, double lat){
        longitude = lon;
        latitude = lat;
    }
    
   double getLongitude(){
       return longitude;
   }
   
   double getLatitude(){
       return latitude;
   }
   
   void setLongitude(double mx){
       longitude = mx;
   }
   
   void setLatitude(double my){
       latitude = my;
   }
}
