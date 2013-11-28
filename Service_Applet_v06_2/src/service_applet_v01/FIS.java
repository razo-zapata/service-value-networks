/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service_applet_v01;

/**
 *
 * @author ivan
 */
public class FIS {
    
    public double MyNormD(double x, double c, double sigma, double alpha)
    {
        double y = 0.0;
        
        /*Pos = x-c;
        z1 = (1 / (sqrt(2*pi*sigma^2)) );
        z2 = exp( -alpha*(Pos^2) / sigma^2); 
        z3  = z1*z2;
        if(z3 > 0.05)
            y = z3;
        end
        */
        double Pos = x - c;
        double z1 = (1 / Math.sqrt(2 * Math.PI * Math.pow(sigma, 2)));
        double z2 = (double) (Math.exp( (-1 * alpha * Math.pow(Pos, 2)) / Math.pow(sigma, 2)));
        double z3 = z1 * z2;
        
        if(z3 > 0.05)
            y = z3;
        
        return y;
    }
    
    
    
}
