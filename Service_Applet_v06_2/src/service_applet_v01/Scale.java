/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;

import java.util.HashSet;

/**
 *
 * @author ivan
 */
public class Scale {
   private HashSet scaleVec;

    public Scale(){
        scaleVec = new HashSet();

    }


    public HashSet getScaleAsResourceSet(){
    return scaleVec;
    }
    public String getScaleName() { return "moehaha";}
}