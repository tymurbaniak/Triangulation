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
public class Point {
    private double x;
    private double y;
    private double r;
    
    Point(double mx, double my, double mr){
        x = mx;
        y = my;
        r = mr;
    }
    
   double getX(){
       return x;
   }
   
   double getY(){
       return y;
   }
   
   double getR(){
       return r;
   }
   
   void setX(double mx){
       x = mx;
   }
   
   void setY(double my){
       y = my;
   }
   
   void setR(double mr){
       r = mr;
   }
}
