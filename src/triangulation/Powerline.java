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
public class Powerline {
    private double xfactor;
    private double yfactor;
    private double constant;
    private double rr1;
    private double rr2;
    Powerline(Location l1, Location l2, int r1, int r2){
        rr1 = Math.abs(r1);
        rr2 = Math.abs(r2);
        yfactor = 1;
        xfactor = -((l2.getLongitude() - l1.getLongitude())/(l2.getLatitude() - l1.getLatitude()));
        constant = (((rr1*rr1)-(rr2*rr2)-(l1.getLongitude()*l1.getLongitude())+(l2.getLongitude()*l2.getLongitude())-(l1.getLatitude()*l1.getLatitude())+(l2.getLatitude()*l2.getLatitude()))/(l2.getLatitude() - l1.getLatitude()));
    }
    void showFunction(){
        System.out.println(yfactor + " = " + xfactor + "x + " + constant);
    }
    double getXFactor(){
        return xfactor;
    }
    double getYFactor(){
        return yfactor;
    }
    double getConstant(){
        return constant;
    }
}
