/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;


import java.util.HashSet;
import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author ivan
 */
public class NominalScale extends Scale {
    private Vector<Resource> nomScaleVec;
    private String scaleName;

    public NominalScale(){

    nomScaleVec= new Vector<Resource>();
   }



     public NominalScale(Vector<Resource> resources){


    nomScaleVec= new Vector<Resource>();
     nomScaleVec.addAll(resources);
     }

      public NominalScale(String name) {

          nomScaleVec= new Vector<Resource>();
    scaleName = name;
     }

    @Override
     public String getScaleName() { return scaleName;}



    void putConsequenceVector(Vector<Resource> resources) {
         nomScaleVec.addAll(resources);

    }

    void putConsequence(Resource resource) {
         nomScaleVec.add(resource);
    }

    Vector<Resource> getNominalScaleAsResourceVector(){
    return nomScaleVec; }

    @Override
    public HashSet getScaleAsResourceSet() {

        return new HashSet(nomScaleVec);
    }
}
