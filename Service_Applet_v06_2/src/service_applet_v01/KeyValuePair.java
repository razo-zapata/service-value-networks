/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;


/**
 *
 * @author ivan
 */
public class KeyValuePair
{

    private String R_URI;
    private String R_Label;

    private int Score;
    
    public KeyValuePair(String This_URI,String This_Label)
    {
        R_URI = This_URI;
        R_Label = This_Label;
    }

    public KeyValuePair(String This_URI,String This_Label,int Score)
    {
        R_URI = This_URI;
        R_Label = This_Label;
        this.Score = Score;
    }

    public String Get_URI()
    {
        return R_URI;
    }

    public String Get_Label()
    {
        return R_Label;
    }

    public int Get_Score()
    {
        return  Score;
    }
    
    @Override public String toString() //@Override
    {
        return R_Label;
    }    

}   
