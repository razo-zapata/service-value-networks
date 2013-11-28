/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;

import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 *
 * @author ivan
 */
public class Calculation
{
    public static Vector<Double> CalculateROCForRanking(Vector<Resource> rankedResources)
    {
        //assumes predefined ranking of resources on the parameter rankedResources: R.get(0) < R.get(1) .....< R.get(n)
        Vector<Double> result = new Vector<Double>();

        for (int i = 1; i <= rankedResources.size(); i ++)
        {
            double ROCScore =  0;
            for (int j =i; j <= rankedResources.size(); j ++)
            {
                ROCScore += 1.0/j;
            }
            ROCScore = ROCScore/rankedResources.size();
            result.add(ROCScore);
        }

        return result;
    }

    public static Double CalculateMAScoreForBundle(Vector<Double> importanceRating , Vector<Double> consequenceScores)
    {
        //assumes importancerating vector and consequencescore vector are of the same size, and that the importance rating at place i, corresponds to the cosequencescore at place i.
        double bundleScore =  0;
        for (int i = 0; i < importanceRating.size(); i ++)
        {
            bundleScore += importanceRating.get(i) * consequenceScores.get(i);
        }
        return bundleScore;
    }
}
