/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;
import java.util.Vector;

/**
 *
 * @author ivan
 */
public class Bundle {
   int NumServices;
   Vector<String> Services;

   public Bundle()
   {
      NumServices = 0;
      Services = new Vector<String>();
   }

   public Bundle(String Service)
   {
      NumServices = 0;
      Services = new Vector<String>();
      add_Service(Service);
   }

   public void add_Service(String Service)
   {
      NumServices++;
      Services.add(Service);
   }

   public void add_Set_of_Services(Vector<String> S)
   {
      for(int i = 0; i < S.size(); i++)
      {
         String Service = S.get(i);
         NumServices++;
         Services.add(Service);
      }
   }
}
