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
public class Cluster {

   int NumServices;
   int NumClusters;
   int NumDigits;
   int NumBundles;

   Vector<String> Services;
   Vector<Cluster> Clusters;
   Vector<Bundle> Bundles;
   int []Code;

   public Cluster()
   {
      NumServices = 0;
      NumClusters = 0;
      Services = new Vector<String>();
      Clusters = new Vector<Cluster>();
   }

   public void Generate_Code(int DecCode, int Digits)
   {
      String binstr = Integer.toBinaryString(DecCode);
      int ThiSize = binstr.length();
      Code = new int[Digits];
      NumDigits = Digits;
      for(int i = 0; i < Digits && i < ThiSize; i++)
      {
         char auxC = binstr.charAt((ThiSize-1)-i);
         if(auxC == '1')
            Code[i] = 1;
      }
   }

   public void add_Service(String Service, int Digits, int []MyCode)
   {
      NumServices++;
      Services.add(Service);
      if(NumServices == 1)
      {
         NumDigits = Digits;
         Code = new int [Digits];
         for(int i = 0; i < Digits; i++)
            Code[i] = MyCode[i];
      }
   }

   public void add_Cluster(Cluster MyCluster)
   {
      NumClusters++;
      Clusters.add(MyCluster);
   }

   public boolean Overlaped_Code(int []ThisCode)
   {
      boolean R = false;
      if( (Code != null) && (ThisCode != null) )
      {
         for (int i = 0; i < NumDigits; i++)
         {
            if( (Code[i] == 1) && (ThisCode[i] == 1))
               R = true;
         }
      }
      return R;
   }

   public void Merge_Clusters(Cluster C1, Cluster C2)
   {
      if( (NumServices == 0) && (NumClusters ==0))
      {
         NumDigits = C1.NumDigits;
         Code = new int[NumDigits];
      }
      for(int i = 0; i < NumDigits; i++)
      {
         Code[i] = C1.Code[i] + C2.Code[i];
      }
      add_Cluster(C1);
      add_Cluster(C2);

      if(NumBundles == 0)
      {
         Bundles = new Vector<Bundle>();
      }
      // Create Services with Services combinations
      for(int i = 0; i < C1.NumServices; i++)
      {
         for(int j = 0; j < C2.NumServices; j++)
         {
            Bundle MyBundle = new Bundle();
            MyBundle.add_Service(C1.Services.get(i));
            MyBundle.add_Service(C2.Services.get(j));
            Bundles.add(MyBundle);
            NumBundles++;
         }
      }

      // Create Services with Bundles combinations for C1
      for(int i = 0; i < C1.NumServices; i++)
      {
         for(int j = 0; j < C2.NumBundles; j++)
         {
            Bundle MyBundle = new Bundle();
            MyBundle.add_Service(C1.Services.get(i));
            Bundle TmpBundle = C2.Bundles.get(j);
            MyBundle.add_Set_of_Services(TmpBundle.Services);
            Bundles.add(MyBundle);
            NumBundles++;
         }
      }

      // Create Services with Bundles combinations for C2
      for(int i = 0; i < C2.NumServices; i++)
      {
         for(int j = 0; j < C1.NumBundles; j++)
         {
            Bundle MyBundle = new Bundle();
            MyBundle.add_Service(C2.Services.get(i));
            Bundle TmpBundle = C1.Bundles.get(j);
            MyBundle.add_Set_of_Services(TmpBundle.Services);
            Bundles.add(MyBundle);
            NumBundles++;
         }
      }

      // Create Bundles with Bundles
      for(int i = 0; i < C1.NumBundles; i++)
      {
         for(int j = 0; j < C2.NumBundles; j++)
         {
            Bundle MyBundle = new Bundle();
            Bundle TmpBundle1 = C1.Bundles.get(j);
            MyBundle.add_Set_of_Services(TmpBundle1.Services);
            Bundle TmpBundle2 = C2.Bundles.get(j);
            MyBundle.add_Set_of_Services(TmpBundle2.Services);
            Bundles.add(MyBundle);
            NumBundles++;
         }
      }
   }
}
