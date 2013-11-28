/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;

import java.util.Vector;
import java.util.HashSet;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author ivan
 */
public class OrdinalScale extends Scale {

   //implements a set of elements, ordered according to an ordinal scale

    private String scaleName;
    private Vector<Resource> ordinalScaleVector;

    public OrdinalScale(){

    ordinalScaleVector= new Vector();}

    public OrdinalScale(Vector<Resource> conseqVector){

    ordinalScaleVector= new Vector(conseqVector);}

       public OrdinalScale(String name){
    ordinalScaleVector= new Vector();
    scaleName= name;}

    @Override
       public String getScaleName() { return scaleName;}

    public void putConsequenceVector(Vector<Resource> conseqVector) {
         ordinalScaleVector.addAll(conseqVector);
    }

    public void putConsequence(Resource resource) {
         ordinalScaleVector.add(resource);
    }

    //the following method is very dirty. Reason this is here; an artefact from ancient times, when ordinalScale was still implemented as a HashSet. It remains intact however, since chaging every reference to ordinalscale in the main program from set to vector requires lots of effort
    @Override
     public HashSet getScaleAsResourceSet() {
        return new HashSet(ordinalScaleVector);

    }

      public Vector<Resource> getScaleAsResourceVector() {
         return ordinalScaleVector;
    }
}

