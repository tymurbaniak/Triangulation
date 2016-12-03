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
    Powerline(Point l1, Point l2){
        rr1 = Math.abs(l1.getR());
        rr2 = Math.abs(l2.getR());
        yfactor = 1;
        xfactor = -((l2.getX() - l1.getX())/(l2.getY() - l1.getY()));
        constant = (((rr1*rr1)-(rr2*rr2)-(l1.getX()*l1.getX())+(l2.getX()*l2.getX())+(l1.getY()*l1.getY())-(l2.getY()*l2.getY()))/(2*(l2.getY() - l1.getY())));
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
