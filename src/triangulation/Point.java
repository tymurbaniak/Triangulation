/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

import java.math.BigDecimal;

/**
 *
 * @author user
 */
public class Point {
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;
    
    Point(BigDecimal mx, BigDecimal my, BigDecimal mr){
        x = mx;
        y = my;
        r = mr;
    }
    
   BigDecimal getX(){
       return x;
   }
   
   BigDecimal getY(){
       return y;
   }
   
   BigDecimal getR(){
       return r;
   }
   
   void setX(BigDecimal mx){
       x = mx;
   }
   
   void setY(BigDecimal my){
       y = my;
   }
   
   void setR(BigDecimal mr){
       r = mr;
   }
}
