/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triangulation;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author user
 */
public class Powerline {
    private BigDecimal xfactor;
    private BigDecimal yfactor;
    private BigDecimal constant;
    private BigDecimal rr1;
    private BigDecimal rr2;
    private int precision = 40;
    Powerline(Point l1, Point l2){
        rr1 = l1.getR().abs();
        rr2 = l2.getR().abs();
        BigDecimal two = new BigDecimal("2");
        if(l2.getY() != l1.getY()){
            yfactor = new BigDecimal("1");
            xfactor = (l2.getX().subtract(l1.getX()).divide(l2.getY().subtract(l1.getY()), precision, RoundingMode.HALF_UP)).negate();
            constant = (rr1.pow(2).subtract(rr2.pow(2)).subtract(l1.getX().pow(2)).add(l2.getX().pow(2)).subtract(l1.getY().pow(2)).add(l2.getY().pow(2))).divide(two.multiply(l2.getY().subtract(l1.getY())), precision, RoundingMode.HALF_UP);
            //constant = (((rr1*rr1)-(rr2*rr2)-(l1.getX()*l1.getX())+(l2.getX()*l2.getX())-(l1.getY()*l1.getY())+(l2.getY()*l2.getY()))/(2*(l2.getY() - l1.getY())));
        }else{
            yfactor = new BigDecimal("0");
            xfactor = new BigDecimal("1").negate();
            constant = (rr1.pow(2).subtract(rr2.pow(2)).subtract(l1.getX().pow(2)).add(l2.getX().pow(2))).divide(two.multiply(l2.getX().subtract(l1.getX())));
            //constant = ((rr1*rr1)-(rr2*rr2)-(l1.getX()*l1.getX())+(l2.getX()*l2.getX())/(2*(l2.getX()-l1.getX())));
        }
        
    }
    void showFunction(){
        System.out.println(yfactor + " = " + xfactor + "x + " + constant);
    }
    BigDecimal getXFactor(){
        return xfactor;
    }
    BigDecimal getYFactor(){
        return yfactor;
    }
    BigDecimal getConstant(){
        return constant;
    }
}
