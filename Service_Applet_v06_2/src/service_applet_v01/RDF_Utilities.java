/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;


//import java.io.IOException;

//import arq.examples.engine.MyQueryEngine;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Model;
//import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
//import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 *
 * @author ivan
 */
public class RDF_Utilities {
   
   public RDF_Utilities()
   {
       
   }

   public RDF_Utilities(boolean Flag)
   {

   }

   Vector<Resource> getType(Model model, String type)
   {
      Vector<Resource> result = new Vector<Resource>();
      ResIterator iter = model.listResourcesWithProperty(RDF.type);
      while (iter.hasNext())
      {
         Resource next = (Resource) iter.nextResource();
         Resource o = (Resource) next.getRequiredProperty(RDF.type).getObject();

         if (o.getLocalName().equals(type))
         {
            result.add(next);
         }
      }
      return result;
   }


   Vector<Resource> getRelatedObjects(Vector<Resource> sourceNodes, Model model, String literal)
   {
      Vector<RDFNode> conseqURI = new Vector<RDFNode>();
      Vector<Resource> result = new Vector<Resource>();
      for (int i = 0; i < sourceNodes.size(); i++)
      {
         conseqURI = getURIs(sourceNodes.get(i), literal);
         for (int j = 0; j < conseqURI.size(); j++)
         {
            result.add(model.getResource(conseqURI.get(j).toString()));
         }
      }
      return result;
   }

   public Vector<RDFNode> getURIs(Resource res, String pred)
   {
      Vector<RDFNode> result = new Vector<RDFNode>();
      StmtIterator it = res.listProperties();

      while (it.hasNext())
      {
         Statement stmt = it.nextStatement();
         Property  predicate = stmt.getPredicate(); // get the predicate
         RDFNode   object    = stmt.getObject();    // get the object

         if (object instanceof Resource && predicate.toString().equals(pred))
         {
            result.add(object);
         }
      }
      return result;
   }

    public String getLiteral(Resource res, String lit)
    {
        String ThisLiteral = new String();
        StmtIterator it = res.listProperties();
        while (it.hasNext())
        {
            Statement stmt = it.nextStatement();
            Property predicate = stmt.getPredicate();

            // object is a literal
            if (!(stmt.getObject() instanceof Resource) && predicate.toString().equals(lit))
            {
                ////System.out.print(stmt.getObject().toString());
                ThisLiteral = new String(stmt.getObject().toString());
            }
        }
        return ThisLiteral;
    }

   public Vector<Resource> Generate_Bundles(Model MSuppliers, Vector<Resource> MyConsequences, Vector<Resource> PossibleSuppliers, Model MyModel, B2B MyB2B)
   {
      Vector<Resource> solutions = new Vector<Resource>();
      Vector<Resource> solutionCandidates = new Vector<Resource>();
      Vector<Tuple> Indexed_Vector = new Vector<Tuple>();

      solutionCandidates = getRelatedObjects(PossibleSuppliers, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#el_performs_va");
      int numConsequences = MyConsequences.size();
      int numVActivities = solutionCandidates.size();
      int Objective = (int) Math.pow(2, numConsequences) - 1;
      int []Counting_Storage = new int[Objective]; // To allow linear-time sorting

      int MaxServices, MaxSIndex, MaxBundles, MaxBIndex;
      int SecondMB, SecondMBIndex;
      MaxServices = MaxSIndex = MaxBundles = MaxBIndex = 0;
      SecondMB = SecondMBIndex = 0;
      
      // Generate empty structure for clusters
      Vector<Cluster> MyClusters = new Vector<Cluster>();
      String binstr = Integer.toBinaryString(Objective);
      int ThisSize = binstr.length();
      for(int i=0; i < Objective; i++)
      {
         Cluster MyTmpCluster = new Cluster();
         // Generate code for MyTmpCluster
         // binstr = Integer.toBinaryString(i+1);
         MyTmpCluster.Generate_Code(i+1, ThisSize);
         MyClusters.add(MyTmpCluster);
      }

      // Get matrix representation
      int [][]MyMatrix = new int[numVActivities][numConsequences + 2];
      // MyMatrix[x][0] = x Value Acitvity ID
      // MyMatrix[x][numConsequences + 1] = Decimal representation;

      int Greatest_Value = 0;

      for(int i=0; i < numVActivities; i++)
      {
         // Get value activity
         Resource CurrentVA = solutionCandidates.get(i);
         String  VA_ID = new String(CurrentVA.getProperty(B2B.e3_has_uid).getObject().toString());
         MyMatrix[i][0] = Integer.parseInt(VA_ID);

         Vector <Resource> TmpVA = new Vector<Resource>();
         TmpVA.add(CurrentVA);
         Vector<Resource> ProvidedFC = getRelatedObjects(getRelatedObjects(getRelatedObjects(getRelatedObjects(
                 getRelatedObjects(TmpVA, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#va_has_vi"),
                 MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of"),
                 MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp"),
                 MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo"),
                 MyModel, "http://www.cs.vu.nl/~gordijn/e3value#resource_has_consequence");

         for(int j=0; j < numConsequences; j++)
         {
            Resource CurrentFC = MyConsequences.get(j);
            for(int k=0; k < ProvidedFC.size(); k++)
            {
               Resource TmpProvided = ProvidedFC.get(k);
               if( CurrentFC.getProperty(B2B.e3_has_name).getObject().toString().equals(TmpProvided.getProperty(B2B.e3_has_name).getObject().toString()))
               {
                  // CurrentVA provides CurrentFC
                  MyMatrix[i][j+1] = 1;
               }
            } // End - for(int k=0; k < ProvidedFC.size(); k++)
         }// End - for(int j=0; j < numConsequences; j++)

         // Compute Decimal Value
         int MyDecSum = 0;
         double count = 0.5;
         for(int j=1; j <= numConsequences; j++)
         {
            count = 2 * count;
            if(MyMatrix[i][j] == 1)
            {
               MyDecSum = MyDecSum + (int) count;
            }
         }
         MyMatrix[i][numConsequences + 1] = MyDecSum;

         // Store service in Cluster
         Cluster MyTmpCluster = MyClusters.get(MyDecSum-1);
         int []Code = new int[numConsequences];
         for(int j=0; j < numConsequences; j++)
         {
            Code[j] = MyMatrix[i][j+1];
         }
         MyTmpCluster.add_Service(VA_ID,numConsequences,Code);
         if(MyTmpCluster.NumServices > MaxServices)
         {
             MaxServices = MyTmpCluster.NumServices;
             MaxSIndex = MyDecSum-1;
         }

         // Store Tuple for sorting
         Tuple MyTuple = new Tuple(i,MyDecSum);
         Indexed_Vector.add(MyTuple);

         // Counting Storage
         Counting_Storage[MyDecSum - 1]++;

         if(MyDecSum > Greatest_Value)
            Greatest_Value = MyDecSum;
            
      }// End - for(int i=0; i < numVActivities; i++)

      // Sorting based on Count Sorting algorithm
      // http://ocw.mit.edu/courses/electrical-engineering-and-computer-science
      // /6-046j-introduction-to-algorithms-sma-5503-fall-2005/video-lectures/
      // lecture-5-linear-time-sorting-lower-bounds-counting-sort-radix-sort/

      int []CS_Prime = new int[Objective];
      int Cumulative_Sum = Counting_Storage[0];
      CS_Prime[0] = Cumulative_Sum;
      for(int i = 1; i < Objective; i++)
      {
         Cumulative_Sum = Cumulative_Sum + Counting_Storage[i];
         CS_Prime[i]   = Cumulative_Sum;
      }

      Vector<Tuple> Indexed_Vector2 = new Vector<Tuple>();
      for(int i = 0; i < numVActivities; i++)
      {
         Tuple NewT = new Tuple(0,0);
         Indexed_Vector2.add(NewT);
      }

      for(int i = numVActivities - 1; i >= 0; i--)
      {
         Tuple MyTuple   = Indexed_Vector.get(i);
         int ThisValue   = MyTuple.get_Value();
         int ThisPosition = MyTuple.get_Position();
         CS_Prime[ThisValue-1]--;
         int RealPos    = CS_Prime[ThisValue-1];
         if(RealPos < 0)
         {
            int MyX = 5;
         }

         Tuple MyTuple2  = Indexed_Vector2.get(RealPos);
         MyTuple2.set_Position(ThisPosition);
         MyTuple2.set_Value(ThisValue);
      }

      int STOP = (int) Math.ceil(Objective/2);
      // Get upper clusters
      // Get lower clusters
      int API = 0;
      int Current = API;
      int Previous;
      while(API < STOP)
      {
         if(Current != API)
         {
            Previous = Current;
            Current  = API;

            for(int i = 0; i <= Previous; i++)
            {
               Cluster MyTmpCluster = MyClusters.get(i);
               if( (MyTmpCluster.NumServices > 0) || (MyTmpCluster.NumClusters > 0) )
               {
                  Cluster APICluster = MyClusters.get(API);
                  if(!MyTmpCluster.Overlaped_Code(APICluster.Code))
                  {
                     int NumCluster = (API + 1) + (i + 1);
                     Cluster NewCluster = MyClusters.get(NumCluster - 1);
                     NewCluster.Merge_Clusters(MyTmpCluster, APICluster);

                     if(NewCluster.NumBundles > MaxBundles)
                     {
                         SecondMB      = MaxBundles;
                         SecondMBIndex = MaxBIndex;
                         MaxBundles    = NewCluster.NumBundles;
                         MaxBIndex     = NumCluster - 1;
                     }

                  }
               }
            }
         }
         API++;
      }
      // Generate combinations
      int [][]SolutionB = new int[(Objective/2) + 1][2];
      int CountSB = 0;
      int PU = Greatest_Value;
      int PL = 1;
      
      while( (PU >= STOP) && (PU > 0))
      {
         if(PU > 0)
         {
            Cluster MyCluster = MyClusters.get(PU-1);
            if( (MyCluster.NumServices > 0) || (MyCluster.NumClusters > 0) )
            {
                for(PL = 1; PL < PU; PL++)
                {
                    if( (PU + PL) == Objective)
                    {
                        SolutionB[CountSB][0] = PU;
                        SolutionB[CountSB][1] = PL;
                        CountSB++;
                    }
                }
            }
            PU--;
         }
      }

      // Computing Solution Bundles
      int ID_C1;
      int ID_C2;
      int ID_Sol = 0;
      for(int i=0; i < CountSB; i++)
      {
         // Combine SolutionB[i][0] with SolutionB[i][1]
         ID_C1 = SolutionB[i][0] - 1;
         ID_C2 = SolutionB[i][1] - 1;

         Cluster C1 = MyClusters.get(ID_C1);
         Cluster C2 = MyClusters.get(ID_C2);

         ID_Sol = (SolutionB[i][0] + SolutionB[i][1]) - 1;
         Cluster C3 = MyClusters.get(ID_Sol);
         
         C3.Merge_Clusters(C1,C2);
      }

      // Generate only a considerable amount of Bundles
      int NumberB = 50;
      // Temp, just to test what happens with incomplete solutions
      //Cluster C3 = MyClusters.get(ID_Sol);
      Cluster C3 = MyClusters.get(ID_Sol);
      
      if(C3.NumBundles == 0)
      {
          if(MaxBundles > 0)
              C3 = MyClusters.get(MaxBIndex);
          else
          {
              C3 = MyClusters.get(MaxSIndex);
              // Careful... we have to crate bundles containing single services,
              // it requires a different reasoning!!!!!!

              // Offer all the single services
              System.out.println("\n Special Case - offering single services");
              
              for(int i = 0; i < C3.NumServices; i++)
              {
                  Resource MyCA = MyB2B.Generate_Composite_Actor(MyModel, "SVN (" + i + ")");
                  //System.out.print(" " + MyBundle.Services.get(j) + ": ");
                  Resource TmpService;
                  Resource TmpActor;
                  String TmpID = new String(new String(""+C3.Services.get(i)+""));
                  String VA_ID = new String(MyB2B.IName);
                  TmpService = MyModel.getResource(VA_ID+TmpID);
                  if(TmpService != null)
                  {
                    if(TmpService.hasProperty(B2B.e3_has_name))
                    {
                        //System.out.print(" " + TmpService.getProperty(B2B.e3_has_name).getObject().toString() + ", ");
                    }
                  }
                  TmpActor = MyModel.getResource(TmpService.getProperty(B2B.va_performed_by_el).getObject().toString());
                  // Copy TmpActor
                  Resource New_Actor = copy_actor(TmpActor, false, MyModel, MyB2B);
                  // Copy Activiy
                  Resource New_Activity = MyB2B.Copy_Value_Activity(TmpService, MyModel);
                  // Add Service to bundle
                  Assign_Activity_to_Bundle(New_Activity, New_Actor, MyCA, MyModel, null, MyB2B);
                  // Merge Interfaces
                  //Merge_Interfaces(MyCA, MyConsequences, true, MyModel, MyB2B);
                  Merge_Interfaces_Smartly(MyCA, MyConsequences, true, MyModel, MyB2B);

                  // MyCA solve B2B dependencies
                  Solve_B2B_Dependencies(MSuppliers,MyModel,MyCA,MyB2B);

                  // Add bundle to solutions
                  solutions.add(MyCA);
              }
          }
      }
      
      System.out.print("\n Num Bundles: " + C3.NumBundles);
      
      if (C3.NumBundles < NumberB)
      {
        // Generate all the bundles
        //System.out.println("\n Bundling A");
        for(int i = 0; i < C3.NumBundles; i++)
        {
            Bundle MyBundle = C3.Bundles.get(i);
            //System.out.print("\nBundling (" + i + ") : {");
            Resource MyCA = MyB2B.Generate_Composite_Actor(MyModel, "SVN (" + i + ")");
            for(int j = 0; j < MyBundle.NumServices; j++)
            {
                //System.out.print(" " + MyBundle.Services.get(j) + ": ");
                Resource TmpService;
                Resource TmpActor;
                String TmpID = new String(new String(""+MyBundle.Services.get(j)+""));
                String VA_ID = new String(MyB2B.IName);
                TmpService = MyModel.getResource(VA_ID+TmpID);
                if(TmpService != null)
                {
                    if(TmpService.hasProperty(B2B.e3_has_name))
                    {
                        //System.out.print(" " + TmpService.getProperty(B2B.e3_has_name).getObject().toString() + ", ");
                    }
                }
                TmpActor = MyModel.getResource(TmpService.getProperty(B2B.va_performed_by_el).getObject().toString());
                // Copy TmpActor
                Resource New_Actor = copy_actor(TmpActor, false, MyModel, MyB2B);
                // Copy Activiy
                Resource New_Activity = MyB2B.Copy_Value_Activity(TmpService, MyModel);
                // Add Service to bundle
                Assign_Activity_to_Bundle(New_Activity, New_Actor, MyCA, MyModel, null, MyB2B);
                // Merge Interfaces
                //Merge_Interfaces(MyCA, MyConsequences, true, MyModel, MyB2B);
                Merge_Interfaces_Smartly(MyCA, MyConsequences, true, MyModel, MyB2B);
            }// End - for j
            //System.out.print("}")

            // MyCA solve B2B dependencies
            Solve_B2B_Dependencies(MSuppliers,MyModel,MyCA,MyB2B);

            // Add bundle to solutions
            solutions.add(MyCA);
        }// End - for i
      }// End if(C3.NumBundles < NumberB)
      else
      {
        // Generate only NumberB bundles
        int increment = (int) Math.ceil(C3.NumBundles / NumberB);

        for(int i = 0; i < C3.NumBundles; i+=increment) //for(int i = 0; i < NumberB; i++)
        {
            Bundle MyBundle = C3.Bundles.get(i);
            //System.out.print("\nBundle (" + i + ") : {");
            Resource MyCA = MyB2B.Generate_Composite_Actor(MyModel, "SVN (" + i + ")");
            for(int j = 0; j < MyBundle.NumServices; j++)
            {
                //System.out.print(" " + MyBundle.Services.get(j) + ": ");
                Resource TmpService;
                Resource TmpActor;
                String TmpID = new String(new String(""+MyBundle.Services.get(j)+""));
                String VA_ID = new String(MyB2B.IName);
                TmpService = MyModel.getResource(VA_ID+TmpID);
                if(TmpService != null)
                {
                    if(TmpService.hasProperty(B2B.e3_has_name))
                    {
                        //System.out.print(" " + TmpService.getProperty(B2B.e3_has_name).getObject().toString() + ", ");
                    }
                }
                TmpActor = MyModel.getResource(TmpService.getProperty(B2B.va_performed_by_el).getObject().toString());
                // Copy TmpActor
                Resource New_Actor = copy_actor(TmpActor, false, MyModel, MyB2B);
                // Copy Activiy
                Resource New_Activity = MyB2B.Copy_Value_Activity(TmpService, MyModel);
                // Add Service to bundle
                Assign_Activity_to_Bundle(New_Activity, New_Actor, MyCA, MyModel, null, MyB2B);
                // Merge Interfaces
                //Merge_Interfaces(MyCA, MyConsequences, true, MyModel, MyB2B);
                Merge_Interfaces_Smartly(MyCA, MyConsequences, true, MyModel, MyB2B);

            }// End - for j
            //System.out.print("}");

            // MyCA solve B2B dependencies
            Solve_B2B_Dependencies(MSuppliers,MyModel,MyCA,MyB2B);

            // Add bundle to solutions
            solutions.add(MyCA);
        }// End - for i
      }// End - else
      //System.out.println("Listing Bundles --- ");
      return solutions;
   }

   public void Solve_B2B_Dependencies(Model MSuppliers, Model MyModel, Resource MyCA, B2B MyB2B)
   {
      // Get Interfaces offering money
      Vector<Resource> VMyCA = new Vector<Resource>();
      VMyCA.add(MyCA);
      Vector<Resource> TheseInterfaces = getRelatedObjects(VMyCA, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi"); //ac_has_vi
      //
      System.out.println("\n Solving depencendies for : " + MyCA.getProperty(B2B.e3_has_name).getObject().toString());
      for(int i = 0; i < TheseInterfaces.size(); i++)
      {
         Resource ThisVI = TheseInterfaces.get(i);
         Vector<Resource> VThisInterface1 = new Vector<Resource>();
         VThisInterface1.add(ThisVI);
         Vector<Resource> TheseOfferings  = getRelatedObjects(VThisInterface1, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of");
         Vector<Resource> ThesePorts      = getRelatedObjects(TheseOfferings, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp");
         
         for(int j = 0; j < ThesePorts.size(); j++)
         {
            Resource ThisPort = ThesePorts.get(j);
            if(ThisPort.getProperty(B2B.vp_has_dir).getObject().toString().equals("true"))
            {
               Resource ThisObject = MyModel.getResource(ThisPort.getProperty(B2B.vp_requests_offers_vo).getObject().toString());
               if(ThisObject.getProperty(B2B.e3_has_name).getObject().toString().equals("MONEY"))
               {
                  Vector<Resource> VThisport = new Vector<Resource>();
                  VThisport.add(ThisPort);
                  Vector<Resource> VThisInterface2 = getRelatedObjects(getRelatedObjects(VThisport, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_in_vo"),
                          MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_in_vi");

                  Vector<Resource> TheseObjects = getRelatedObjects(getRelatedObjects(getRelatedObjects(VThisInterface2, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of"),
                          MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp"),
                          MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo");
                  
                  System.out.println("\n We are looking for: ");
                  for(int y = 0; y < TheseObjects.size(); y++)
                  {
                     Resource TmpObj = TheseObjects.get(y);
                     System.out.println("\n VO: " + TmpObj.getProperty(B2B.e3_has_name).getObject().toString());
                  }

                  Resource TmpVI = VThisInterface2.get(0);
                  // Matching Value Interface
                  MyB2B.Match_Value_Interface(MyModel, TmpVI, MSuppliers,false);
               }
            }
         }
      }
   }
   
   Resource copy_actor(Resource MyActor, boolean Everything, Model MyModel, B2B MyB2B)
   {
      if(MyActor == null)
      {
         //System.out.println("Error-Actor");
      }
      String Old_Name = MyActor.getProperty(B2B.e3_has_name).getObject().toString();
      String delims = "[&]";
      String[] First_Name = null;
      First_Name = Old_Name.split(delims);
      int AuxID = (MyB2B.getID() + 1);
      String New_Name = new String(First_Name[0] +  " & " + AuxID);
      //MyActor.removeAll(MyB2B.e3_has_name);
      //MyActor.addProperty(MyB2B.e3_has_name, New_Name);

      // Create a copy of this Elementary Actor
      Resource MyNewActor = MyB2B.Generate_Elementary_Actor(MyModel, New_Name,true);
      // Remove e3_has_formula
      MyNewActor.removeAll(B2B.e3_has_formula);
      MyNewActor.addProperty(B2B.e3_has_formula, MyActor.getProperty(B2B.e3_has_formula).getObject().toString());

      if(Everything)
      {
         // Copy all its interfaces
         Vector<Resource> TmpActor = new Vector<Resource>();
         TmpActor.add(MyActor);
         /*Vector<Resource> VInterfaces = new Vector<Resource>();
         VInterfaces = getRelatedObjects(TmpActor, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");
         for(int index = 0; index < VInterfaces.size(); index++)
         {
            // Get Value Interface in actor
            Resource MyVI = VInterfaces.get(index);
            // Generate Value Interface in EA
            Resource EA_VI = MyB2B.Generate_Value_Interface(MyModel);
            MyB2B.Copy_Value_Interfaces(MyModel, MyVI, MyModel, EA_VI, false);
            MyModel.add(MyNewActor, MyB2B.ac_has_vi, EA_VI);
            MyModel.add(EA_VI, MyB2B.vi_assigned_to_ac, MyNewActor);
         }*/

         // Value Activities
         Vector<Resource> VActivities = new Vector<Resource>();
         VActivities = getRelatedObjects(TmpActor, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#el_performs_va");
         for(int index = 0; index < VActivities.size(); index++)
         {
            // Get Value Activity in actor
            Resource MyVA = VActivities.get(index);
            // Generate Value Activity in EA
            Resource New_VA = MyB2B.Copy_Value_Activity(MyVA, MyModel);
            MyB2B.Assign_Activity_to_Actor(MyModel, New_VA, MyNewActor,null);
         }
      }
      return MyNewActor;
   }


   void Assign_Activity_to_Bundle(Resource Value_Activity, Resource Actor, Resource CActor, Model MyModel, Resource Consequence, B2B MyB2B)
   {
      Boolean Already_There = false;
      Boolean Erase = false;
      String Activity_Formula = Value_Activity.getProperty(B2B.e3_has_formula).getObject().toString();
      String Activity_Name   = Value_Activity.getProperty(B2B.e3_has_name).getObject().toString();
      Resource MyActor = null;

      // Get Internal Activities
      Vector<Resource> SB_Elements = new Vector<Resource>();
      SB_Elements.add(CActor);
      Vector<Resource> Internal_Activities = new Vector<Resource>();
      Internal_Activities = getRelatedObjects(getRelatedObjects(getRelatedObjects(SB_Elements,
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ca_consists_of_vi"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_assigned_to_ac"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#el_performs_va");

      String This_Formula = null;
      String This_Name =  null;
      for(int index = 0; index < Internal_Activities.size(); index++)
      {
         Resource IntActivity = Internal_Activities.get(index);
         This_Formula = new String(IntActivity.getProperty(B2B.e3_has_formula).getObject().toString());
         This_Name =  new String(IntActivity.getProperty(B2B.e3_has_name).getObject().toString());
         if(This_Formula.equals(Activity_Formula))
         {
            Already_There = true;
            MyActor = MyModel.getResource(IntActivity.getProperty(B2B.va_performed_by_el).getObject().toString());
            if(This_Name.equals(Activity_Name))
               Erase = true;
            break;
         }
      }

      if(Already_There)
      {
         // Check Names, if they are not the same erase Value_Activity
         if(Erase)
         {
            MyB2B.remove_value_activity(MyModel, Value_Activity);
            remove_actor(Actor, MyModel, MyB2B);
         }
      }
      else
      {
         //If actor is in bundle - STRONG ASSUMPTION TEMPORAL - WE DON'T ADD THE SERVICE!!!!!!
         if(IsActorInBundle(CActor,Actor,MyModel, MyB2B))
         {
            // Get correct actor
            MyActor = get_actor_from_bundle(Actor,CActor,MyModel, MyB2B);
            remove_actor(Actor, MyModel, MyB2B);
            Actor = MyActor;

            // STRONG ASSUMPTION - THE ACTOR MIGHT HAVE A SERVICE WITH SIMILAR CONSEQUENCES
            //MyB2B.remove_value_activity(MyModel, Value_Activity);
            //Assign activity to actor
            MyB2B.Assign_Activity_to_Actor(MyModel, Value_Activity, Actor, CActor);
         }
         else
         {
            //Assign activity to actor
            MyB2B.Assign_Activity_to_Actor(MyModel, Value_Activity, Actor,null);
            // Assign actor to bundle
            Assign_Actor_to_Bundle(Actor, CActor, MyModel, Consequence, MyB2B);
         }

      }// END ELSE

   }// End Assing_Activity_to_Bundle

   void remove_actor(Resource MyActor, Model MyModel, B2B MyB2B)
   {
      // Delete all its interfaces
      Vector<Resource> TmpActor = new Vector<Resource>();
      TmpActor.add(MyActor);
      Vector<Resource> VInterfaces = new Vector<Resource>();
      VInterfaces = getRelatedObjects(TmpActor, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");
      for(int index = 0; index < VInterfaces.size(); index++)
      {
         // Get Value Interface in actor
         Resource MyVI = VInterfaces.get(index);
         MyB2B.remove_value_interface(MyModel, MyVI);
      }

      // Value Activities
      Vector<Resource> VActivities = new Vector<Resource>();
      VActivities = getRelatedObjects(TmpActor, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#el_performs_va");
      for(int index = 0; index < VActivities.size(); index++)
      {
         // Get Value Activity in actor
         Resource MyVA = VActivities.get(index);
         MyB2B.remove_value_activity(MyModel, MyVA);
      }
      MyActor.removeProperties();
   }// End remove actor

   boolean IsActorInBundle(Resource MyCA, Resource TmpActor, Model MyModel, B2B MyB2B)
   {
      Boolean Already_There = false;
      String Actor_Formula = TmpActor.getProperty(B2B.e3_has_formula).getObject().toString();
      // Get Internal Actors
      Vector<Resource> SB_Elements = new Vector<Resource>();
      SB_Elements.add(MyCA);
      Vector<Resource> Internal_Actors = new Vector<Resource>();
      Internal_Actors = getRelatedObjects(getRelatedObjects(SB_Elements,
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ca_consists_of_vi"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_assigned_to_ac");

      for(int index = 0; index < Internal_Actors.size(); index++)
      {
         Resource IntActor = Internal_Actors.get(index);
         String This_Formula   = IntActor.getProperty(B2B.e3_has_formula).getObject().toString();
         if(This_Formula.equals(Actor_Formula))
         {
            Already_There = true;
            break;
         }
      }
      return Already_There;
   }// End IsActorinBundle

   Resource get_actor_from_bundle(Resource Actor,Resource CActor, Model MyModel, B2B MyB2B)
   {
      Resource MyActor = null;
      Boolean Already_There = false;
      Boolean Erase = false;
      String Actor_Formula = Actor.getProperty(B2B.e3_has_formula).getObject().toString();
      // Get Internal Actors
      Vector<Resource> SB_Elements = new Vector<Resource>();
      SB_Elements.add(CActor);
      Vector<Resource> Internal_Actors = new Vector<Resource>();
      Internal_Actors = getRelatedObjects(getRelatedObjects(SB_Elements,
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ca_consists_of_vi"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_assigned_to_ac");
      for(int index = 0; index < Internal_Actors.size(); index++)
      {
         Resource IntActor = Internal_Actors.get(index);
         String This_Formula = IntActor.getProperty(B2B.e3_has_formula).getObject().toString();
         String This_Name =  IntActor.getProperty(B2B.e3_has_name).getObject().toString();
         if(This_Formula.equals(Actor_Formula))
         {
            Already_There = true;
            MyActor = IntActor;
            break;
         }
      }
      return MyActor;
   }// End get_actor_from_bundle

   void Assign_Actor_to_Bundle(Resource TmpActor, Resource MyCA, Model MyModel, Resource Consequence, B2B MyB2B)
   {
      Boolean Already_There = false;
      Boolean Erase = false;
      String Actor_Formula = TmpActor.getProperty(B2B.e3_has_formula).getObject().toString();
      String Actor_Name   = TmpActor.getProperty(B2B.e3_has_name).getObject().toString();

      // Get Internal Actors
      Vector<Resource> SB_Elements = new Vector<Resource>();
      SB_Elements.add(MyCA);
      Vector<Resource> Internal_Actors = new Vector<Resource>();
      Internal_Actors = getRelatedObjects(getRelatedObjects(SB_Elements,
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ca_consists_of_vi"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_assigned_to_ac");

      for(int index = 0; index < Internal_Actors.size(); index++)
      {
         Resource IntActor = Internal_Actors.get(index);
         String This_Formula   = IntActor.getProperty(B2B.e3_has_formula).getObject().toString();
         String This_Name =  IntActor.getProperty(B2B.e3_has_name).getObject().toString();
         if(This_Formula.equals(Actor_Formula))
         {
            Already_There = true;
            if(!This_Name.equals(Actor_Name))
               Erase = true;
            break;
         }
      }

      if(Already_There)
      {
         // Check Names, if they are not the same they erase TmpActor
         if(Erase)
         {
            //TmpActor.removeProperties();
            remove_actor(TmpActor,MyModel, MyB2B);
         }
         //String Old_Name = TmpActor.getProperty(MyB2B.e3_has_name).getObject().toString();
         //TmpActor.removeAll(MyB2B.e3_has_name);
         //TmpActor.addProperty(MyB2B.e3_has_name, Old_Name + " DELETED ");
      }
      else
      {
         // Actor Properties
         Vector<Resource> MyActor = new Vector<Resource>();
         MyActor.add(TmpActor);
         Vector<Resource> Value_Interfaces = new Vector<Resource>();
         // Get Value Interfaces
         Value_Interfaces = getRelatedObjects(MyActor, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");
         for(int index = 0; index < Value_Interfaces.size(); index++)
         {
            // Get Value Interface in actor
            Resource MyVI = Value_Interfaces.get(index);
            MyModel.add(MyCA, B2B.ca_consists_of_vi, MyVI);
            MyModel.add(MyVI, B2B.vi_in_ca, MyCA);
            // Generate Value Interface in SB
            Resource CA_VI = MyB2B.Generate_Value_Interface(MyModel);
            MyModel.add(MyCA, B2B.ac_has_vi, CA_VI);
            MyModel.add(CA_VI, B2B.vi_assigned_to_ac, MyCA);
            // Link Value Interfaces
            MyB2B.Copy_Value_Interfaces(MyModel, MyVI, MyModel, CA_VI, false);
            MyB2B.Link_Value_Interfaces_2(MyModel, CA_VI, MyVI);
         }
      }// END ELSE
   }// End Assign Actor_to_Bundle

   void Merge_Interfaces(Resource MyCA, Vector<Resource> MyConsequences, boolean More_Actors, Model MyModel, B2B MyB2B)
   {
      Vector<Resource> Remove = new Vector<Resource>();
      Vector<Resource> MYVIs = new Vector<Resource>();
      Vector<Resource> MyTmpActor = new Vector<Resource>();
      MyTmpActor.add(MyCA);

      MYVIs = getRelatedObjects(MyTmpActor, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");
      Property AC_HAS_VI = MyModel.getProperty("http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");
      for(int x = 0; x < MYVIs.size(); x++)
      {
         Resource MyVI = MYVIs.get(x);
         if(!Remove.contains(MyVI))
         {
            for(int y = 0; y < MYVIs.size(); y++)
            {
               Resource MyVI2 = MYVIs.get(y);
               if( (x != y) && (!Remove.contains(MyVI2)) )
               {
                  // One actor can have the same consequences - SENA {Artist Permission and Producer Permission}
                  /*if(More_Actors)
                  {
                     if(different_consequences(MyVI,MyVI2,MyConsequences))
                     {
                        // Remove link between MyVI2 and MyCA
                        MyB2B.remove_this_Property(MyModel, MyCA, AC_HAS_VI, MyVI2);
                        Transfer_offerings(MyVI,MyVI2);
                        Remove.add(MyVI2);
                     }
                  }
                  else
                  {
                     if(!different_consequences(MyVI,MyVI2,MyConsequences))
                     {
                        // Remove link between MyVI2 and MyCA
                        MyB2B.remove_this_Property(MyModel, MyCA, AC_HAS_VI, MyVI2);
                        Transfer_offerings(MyVI,MyVI2);
                        Remove.add(MyVI2);
                     }
                  }*/

                  if(thereAreConsequences(MyVI,MyVI2,MyConsequences,MyModel, MyB2B))
                  {
                     // Remove link between MyVI2 and MyCA
                     MyB2B.remove_this_Property(MyModel, MyCA, AC_HAS_VI, MyVI2);
                     Transfer_offerings(MyVI,MyVI2, MyModel, MyB2B);
                     Remove.add(MyVI2);
                  }
               }
            }
         }//End IF contains MyVI
      }// End For x
   }// End of merge interfaces


   void Merge_Interfaces_Smartly(Resource MyCA, Vector<Resource> MyConsequences, boolean More_Actors, Model MyModel, B2B MyB2B)
   {
      Vector<Resource> Remove = new Vector<Resource>();
      Vector<Resource> MYVIs = new Vector<Resource>();
      Vector<Resource> MyTmpActor = new Vector<Resource>();
      MyTmpActor.add(MyCA);

      MYVIs = getRelatedObjects(MyTmpActor, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");
      Property AC_HAS_VI = MyModel.getProperty("http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");
      for(int x = 0; x < MYVIs.size(); x++)
      {
         Resource MyVI = MYVIs.get(x);
         if(!Remove.contains(MyVI))
         {
            for(int y = 0; y < MYVIs.size(); y++)
            {
               Resource MyVI2 = MYVIs.get(y);
               if( (x != y) && (!Remove.contains(MyVI2)) )
               {
                  // One actor can have the same consequences - SENA {Artist Permission and Producer Permission}
                  /*if(More_Actors)
                  {
                     if(different_consequences(MyVI,MyVI2,MyConsequences))
                     {
                        // Remove link between MyVI2 and MyCA
                        MyB2B.remove_this_Property(MyModel, MyCA, AC_HAS_VI, MyVI2);
                        Transfer_offerings(MyVI,MyVI2);
                        Remove.add(MyVI2);
                     }
                  }
                  else
                  {
                     if(!different_consequences(MyVI,MyVI2,MyConsequences))
                     {
                        // Remove link between MyVI2 and MyCA
                        MyB2B.remove_this_Property(MyModel, MyCA, AC_HAS_VI, MyVI2);
                        Transfer_offerings(MyVI,MyVI2);
                        Remove.add(MyVI2);
                     }
                  }*/

                  //if(thereAreConsequences(MyVI,MyVI2,MyConsequences,MyModel, MyB2B))
                  if(Money_Same_Direction(MyVI,MyVI2,MyConsequences,MyModel, MyB2B))
                  {
                     // Remove link between MyVI2 and MyCA
                     MyB2B.remove_this_Property(MyModel, MyCA, AC_HAS_VI, MyVI2);
                     Transfer_offerings(MyVI,MyVI2, MyModel, MyB2B);
                     Remove.add(MyVI2);
                  }
               }
            }
         }//End IF contains MyVI
      }// End For x
   }// End of merge interfaces

   boolean Money_Same_Direction(Resource MyVI, Resource MyVI2, Vector<Resource> MyConsequences, Model MyModel, B2B MyB2B)
   {
      boolean Answer = false;
      Vector<Resource> TmpMyVI = new Vector<Resource>();
      Vector<Resource> TmpMyVI2 = new Vector<Resource>();

      TmpMyVI.add(MyVI);
      TmpMyVI2.add(MyVI2);

      Vector<Resource> VP1 = new Vector<Resource>();
      Vector<Resource> VP2 = new Vector<Resource>();

      VP1 = getRelatedObjects(getRelatedObjects(TmpMyVI,
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of"),
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp");

      VP2 = getRelatedObjects(getRelatedObjects(TmpMyVI2,
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of"),
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp");
      
      for(int i = 0; i < VP1.size(); i++)
      {
        Resource ThisVP1 = VP1.get(i);
        if(ThisVP1.getProperty(B2B.vp_has_dir).getObject().toString().equals("true"))
        {
            for(int j = 0; j < VP2.size(); j++)
            {
                Resource ThisVP2 = VP2.get(j);
                if(ThisVP2.getProperty(B2B.vp_has_dir).getObject().toString().equals("true"))
                {
                    // Get value objects and compare them, if they are the same, we can merge the interfaces
                    Resource VO1 = MyModel.getResource(ThisVP1.getProperty(B2B.vp_requests_offers_vo).getObject().toString());
                    Resource VO2 = MyModel.getResource(ThisVP2.getProperty(B2B.vp_requests_offers_vo).getObject().toString());
                    String NameVO1 = VO1.getProperty(B2B.e3_has_name).getObject().toString();
                    String NameVO2 = VO2.getProperty(B2B.e3_has_name).getObject().toString();
                    //System.out.print("\n TRUE - Comparing " + VO1 + " against " + VO2);
                    if(NameVO1.equals(NameVO2))
                    {
                        //System.out.println("\t they are equal");
                        // Set it tru if we want to merge the interfaces for enablers
                        //Answer = true;
                    }
                    //else
                        //System.out.println("\t they are different");
                }
            }
        }
        else
        {
            for(int j = 0; j < VP2.size(); j++)
            {
                Resource ThisVP2 = VP2.get(j);
                if(ThisVP2.getProperty(B2B.vp_has_dir).getObject().toString().equals("false"))
                {
                    // Get value objects and compare them, if they are the same, we can merge the interfaces
                    Resource VO1 = MyModel.getResource(ThisVP1.getProperty(B2B.vp_requests_offers_vo).getObject().toString());
                    Resource VO2 = MyModel.getResource(ThisVP2.getProperty(B2B.vp_requests_offers_vo).getObject().toString());
                    String NameVO1 = VO1.getProperty(B2B.e3_has_name).getObject().toString();
                    String NameVO2 = VO2.getProperty(B2B.e3_has_name).getObject().toString();
                    //System.out.print("\n FALSE - Comparing " + VO1 + " against " + VO2);
                    if((NameVO1.equals(NameVO2)) && (NameVO1.equals("MONEY")))
                    {
                        //System.out.println("\t they are equal");
                        Answer = true;
                    }
                    //else
                        //System.out.println("\t they are different");
                }
            }
        }
      }

      /*Vector<Resource> VO1 = new Vector<Resource>();
      Vector<Resource> VO2 = new Vector<Resource>();

      VO1 = getRelatedObjects(getRelatedObjects(getRelatedObjects(TmpMyVI,
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of"),
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp"),
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo");

      VO2 = getRelatedObjects(getRelatedObjects(getRelatedObjects(TmpMyVI2,
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of"),
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp"),
            MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo");*/

      // We assume they 
      //Answer = true;

      return Answer;
   }//End of Money_Same_Direction

   boolean thereAreConsequences(Resource MyVI, Resource MyVI2, Vector<Resource> MyConsequences, Model MyModel, B2B MyB2B)
   {
      boolean Answer = false;
      Vector<Resource> TmpMyVI = new Vector<Resource>();
      Vector<Resource> TmpMyVI2 = new Vector<Resource>();

      TmpMyVI.add(MyVI);
      TmpMyVI2.add(MyVI2);

      Vector<Resource> Consequences1 = new Vector<Resource>();
      Vector<Resource> Consequences2 = new Vector<Resource>();

      Consequences1 = getRelatedObjects(getRelatedObjects(getRelatedObjects(getRelatedObjects(TmpMyVI,
                                                                           MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of"),
                                                                           MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp"),
                                                                           MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo"),
                                                                           MyModel, "http://www.cs.vu.nl/~gordijn/e3value#resource_has_consequence");

      Consequences2 = getRelatedObjects(getRelatedObjects(getRelatedObjects(getRelatedObjects(TmpMyVI2,
                                                                           MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of"),
                                                                           MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp"),
                                                                           MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo"),
                                                                           MyModel, "http://www.cs.vu.nl/~gordijn/e3value#resource_has_consequence");

      // We assume they cover different consequences
      Answer = true;
      //if( (Consequences1.isEmpty()) || (Consequences2.isEmpty()) )
         //Answer = false;
      return Answer;
   }//End of thereAreConsequences

   void Transfer_offerings(Resource MyVI, Resource MyVI2, Model MyModel, B2B MyB2B)
   {
      // Transfer offerings from MyVI2 to MyVI
      Vector<Resource> TmpMyVI2 = new Vector<Resource>();
      TmpMyVI2.add(MyVI2);
      Vector<Resource> AllOfferings = new Vector<Resource>();
      AllOfferings = getRelatedObjects(TmpMyVI2, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of");
      for(int x = 0; x < AllOfferings.size(); x++)
      {
         // Remove link between this offering and the value interface
         Resource TmpOff = AllOfferings.get(x);
         TmpOff.removeAll(B2B.vo_in_vi);
         // Value Ports linked to TmpOff
         Vector<Resource> Tmp_OLD_Off = new Vector<Resource>();
         Tmp_OLD_Off.add(TmpOff);
         Vector<Resource> Value_Ports = new Vector<Resource>();
         Value_Ports = getRelatedObjects(Tmp_OLD_Off, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp");

         // Re-assign value ports, get the proper offering
         Vector<Resource> TmpMyVI = new Vector<Resource>();
         TmpMyVI.add(MyVI);
         Vector<Resource> MyVI_Offerings = new Vector<Resource>();
         MyVI_Offerings = getRelatedObjects(TmpMyVI, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of");
         if(TmpOff.getProperty(B2B.e3_has_name).getObject().toString().equals("out"))
         {
            // Get out offering in MyVI
            for(int index = 0; index < MyVI_Offerings.size(); index++)
            {
               Resource This_Offering = MyVI_Offerings.get(index);
               if(This_Offering.getProperty(B2B.e3_has_name).getObject().toString().equals("out"))
               {
                  for(int index2 = 0; index2 < Value_Ports.size(); index2++)
                  {
                     Resource This_Port = Value_Ports.get(index2);
                     if(This_Port.getProperty(B2B.vp_has_dir).getObject().toString().equals("true"))
                     {
                        This_Port.removeAll(B2B.vp_in_vo);
                        MyModel.add(This_Offering, B2B.vo_consists_of_vp, This_Port);
                        MyModel.add(This_Port, B2B.vp_in_vo, This_Offering);
                     }
                     else
                     {
                        //System.out.println("WE HAVE A MISTAKE: Value Port with wrong direction ... ");
                     }
                  }
                  break;
               }
            }
         }
         else
         {
            // Get in offering in MyVI
            for(int index = 0; index < MyVI_Offerings.size(); index++)
            {
               Resource This_Offering = MyVI_Offerings.get(index);
               if(This_Offering.getProperty(B2B.e3_has_name).getObject().toString().equals("in"))
               {
                  for(int index2 = 0; index2 < Value_Ports.size(); index2++)
                  {
                     Resource This_Port = Value_Ports.get(index2);
                     if(This_Port.getProperty(B2B.vp_has_dir).getObject().toString().equals("false"))
                     {
                        This_Port.removeAll(B2B.vp_in_vo);
                        MyModel.add(This_Offering, B2B.vo_consists_of_vp, This_Port);
                        MyModel.add(This_Port, B2B.vp_in_vo, This_Offering);
                     }
                     else
                     {
                        //System.out.println("WE HAVE A MISTAKE: Value Port with wrong direction ... ");
                     }
                  }
                  break;
               }
            }
         }

         // Assign Offering to Interface - CHANGE
         //MyModel.add(MyVI, MyB2B.vi_consists_of_of, TmpOff);
         //MyModel.add(TmpOff, MyB2B.vo_in_vi, MyVI);
         TmpOff.removeProperties();
      }
      MyVI2.removeProperties();
   }//End of Transfer_offerings

   //this method groups a random set of quality consequences into scales
   Vector<Scale> getScaledConseq(Vector<Resource> qualityConsequences, Model model)
   {
      //warning; the following code is so dirty that it might cause irreperable trauma to those who learned to program in a structured manner         NominalScale nomScale = new NominalScale("toedeloe");
      NominalScale nomScale = new NominalScale( "toedeloe" );
      OrdinalScale ordScale = new OrdinalScale( "toedeloe" );

      B2B MyAuxB2B = new B2B(1);
      
      Vector<Scale> result = new Vector<Scale>();
      // attempt to explain the following code: for each quality consequence,
      // we make a scale so that when there are i consequences, we end up with i scales.
      // Although this leads to duplicate scales, we  need to do this because the list
      // of consequences "as is"is unordered, thus leaving open the possibility that the
      // last consequences from this unordered list constitute together a new scale.
      
      for (int i = 0; i < qualityConsequences.size(); i ++)
      {
         //hack
         Vector<Resource> qConseqI = new Vector<Resource>();
         qConseqI.add(qualityConsequences.get(i));
         //MyAuxB2B.print_list_properties(model, qualityConsequences.get(i));

         Vector <Resource> scaleConseqIVec =  getRelatedObjects(qConseqI, model, "http://www.cs.vu.nl/~gordijn/e3value#has_scale");
         Resource scaleConseqI = scaleConseqIVec.get(0);
         
         //MyAuxB2B.print_list_properties(model, scaleConseqI);
         
         
         //String ScaleType = scaleConseqI.getRequiredProperty(RDF.type).getObject().toString();
         String ScaleType = scaleConseqI.getProperty(RDF.type).getObject().toString();

         //the horror! (see below)
         if(ScaleType.equals("http://www.cs.vu.nl/~gordijn/e3value#nominal"))
         {
            nomScale = new NominalScale(giveLiteral(scaleConseqI, "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name"));
            nomScale.putConsequence(qualityConsequences.get(i));
         }

         //create a new ordinal scale (and add consequence i to it), if and only if consequence i has ranking 1.
         if(scaleConseqI.getRequiredProperty(RDF.type).getObject().toString().equals("http://www.cs.vu.nl/~gordijn/e3value#ordinal") && giveLiteral(qualityConsequences.get(i), "http://www.cs.vu.nl/~gordijn/e3value#rank").equals("1"))
         {
            ordScale = new OrdinalScale(giveLiteral(scaleConseqI, "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name"));
            ordScale.putConsequence(qualityConsequences.get(i));
         }

         for (int j = 0; j < qualityConsequences.size(); j ++)
         {
            if (i!=j)
            { //a resource should not be compared to itself
               //hack; create a vector of 1 resource, so that the scale of conseqeunce(j) can be retrieved by using the method getrelatedobjects
               Vector<Resource> qConseqJ = new Vector<Resource>();
               qConseqJ.add(qualityConsequences.get(j));
               Vector <Resource> scaleConseqJVec =  getRelatedObjects(qConseqJ, model, "http://www.cs.vu.nl/~gordijn/e3value#has_scale");
               Resource scaleConseqJ = scaleConseqJVec.get(0);
               if (giveLiteral(scaleConseqI, "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name").equals(giveLiteral(scaleConseqJ, "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name")))
               {
                  if(scaleConseqI.getRequiredProperty(RDF.type).getObject().toString().equals("http://www.cs.vu.nl/~gordijn/e3value#nominal") && !nomScale.getNominalScaleAsResourceVector().contains(qualityConsequences.get(j)))
                  {
                     nomScale.putConsequence(qualityConsequences.get(j));
                  }
                  //consequence j should only be added to an ordinal scale if it ranks 1 higher than the last consequence in the vector
                  if(scaleConseqI.getRequiredProperty(RDF.type).getObject().toString().equals("http://www.cs.vu.nl/~gordijn/e3value#ordinal") && ordScale.getScaleAsResourceVector().size()!=0 && giveLiteral(ordScale.getScaleAsResourceVector().lastElement(),"http://www.cs.vu.nl/~gordijn/e3value#rank").compareTo(giveLiteral(qualityConsequences.get(j),"http://www.cs.vu.nl/~gordijn/e3value#rank")) == -1)
                  {
                     ordScale.putConsequence(qualityConsequences.get(j));
                  }
               }
            }
            result.add(nomScale);
            result.add(ordScale);
         }
      }

      //remove empty scales from result
      for(int i = 0; i < result.size(); i ++)
      {
         Vector<Resource> iConseqVec = new Vector<Resource> (result.get(i).getScaleAsResourceSet());
         if (iConseqVec.size() ==0 )
            result.removeElementAt(i);
      }

      //remove duplicate scales from result
      for(int i = 0; i < result.size(); i ++)
      {
         Vector<Resource> iConseqVec = new Vector<Resource> (result.get(i).getScaleAsResourceSet());
         for (int j = 0; j < result.size(); j ++)
         {
            Vector<Resource> jConseqVec = new Vector <Resource>(result.get(j).getScaleAsResourceSet());
            if ( iConseqVec.containsAll(jConseqVec) && jConseqVec.containsAll(iConseqVec)&& i!=j)
            {
               result.removeElementAt(j);
               j = 0;
            } //j=0 ==> bij het verwijderen van een vector-element moet j opnieuw geinitialiseerd worden. Immers, het element op plaats  j + 1 komt op plaats j te staan, waardoor dit element niet meer wordt vergeleken met i.
         }
      }

      return result;
   }//End of getScaledConseq

   String giveLiteral(Resource res, String lit)
   {
      StmtIterator it = res.listProperties();
      while (it.hasNext())
      {
         Statement stmt = it.nextStatement();
         Property predicate = stmt.getPredicate();

         // object is a literal
         if (!(stmt.getObject() instanceof Resource) && predicate.toString().equals(lit))
         {
            //retourneert het object dat aan de gevraagde literal voldoet
            return stmt.getObject().toString();
         }
      }
      return null;
   }//End of giveLiteral

   Vector<Resource> matchConsumerSupplierConsequences(Vector<Resource> consequenceConsumer, Vector<Resource> consequenceSupplier)
   {
      Vector<Resource> result = new Vector<Resource> ();
      for (int i = 0; i < consequenceConsumer.size(); i ++)
      {
         for (int j = 0; j < consequenceSupplier.size(); j ++)
         {
            if (giveLiteral(consequenceConsumer.get(i), "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name").equals(giveLiteral(consequenceSupplier.get(j), "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name")))
            {
               result.add(consequenceSupplier.get(j));
            }
         }
      }
      return result;
   }//End of matchConsumerSupplierConsequences

   Vector<Resource> get_special_ports(Vector<Resource> sourceNodes, Model model, String literal)
   {
      Vector<RDFNode> conseqURI = new Vector<RDFNode>();
      Vector<Resource> result = new Vector<Resource>();
      Vector<Resource> VP = new Vector<Resource>();
      Vector<Resource> VO = new Vector<Resource>();
      Vector<Resource> VI = new Vector<Resource>();
      Vector<Resource> AC = new Vector<Resource>();
      Vector<Resource> Value_Port_TMP = null;

      //for (int i = 0; i < sourceNodes.size(); i++)
      //{
         VP = getRelatedObjects(sourceNodes, model, "http://www.cs.vu.nl/~gordijn/e3value#vo_offered_requested_by_vp");

         for(int k = 0; k < VP.size(); k++)
         {

            Value_Port_TMP = new Vector<Resource>();
            Value_Port_TMP.add(VP.get(k));
            Resource TMP_Port = VP.get(k);
            Property TMP_Property = model.getProperty("http://www.cs.vu.nl/~gordijn/e3value#vp_has_dir");
            if(TMP_Port.getProperty(TMP_Property).getObject().toString().equals("true"))
            {
               VO = getRelatedObjects(Value_Port_TMP, model, "http://www.cs.vu.nl/~gordijn/e3value#vp_in_vo");
               VI = getRelatedObjects(VO, model, "http://www.cs.vu.nl/~gordijn/e3value#vo_in_vi");
               AC = getRelatedObjects(VI, model, "http://www.cs.vu.nl/~gordijn/e3value#vi_assigned_to_ac");
               if(!AC.isEmpty())
               {
                  result.add(VP.get(k));
               }
            }
         }
      //}
      return result;
   }//End of get_special_ports

   Vector<Resource> getServicesFromBundle(Resource Bundle, Model MyModel)
   {
      // Get Internal Activities
      Vector<Resource> SB_Elements = new Vector<Resource>();
      SB_Elements.add(Bundle);
      Vector<Resource> Internal_Activities = new Vector<Resource>();
      Internal_Activities = getRelatedObjects(getRelatedObjects(getRelatedObjects(SB_Elements,
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ca_consists_of_vi"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_assigned_to_ac"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#el_performs_va");

      //Internal_Activities.
      Vector<Resource> Inner_Services = new Vector<Resource>();
      for(int i = 0; i < Internal_Activities.size(); i++)
      {
          Resource ThisS = Internal_Activities.get(i);
          if(!Inner_Services.contains(ThisS))
          {
              Inner_Services.add(ThisS);
          }
      }

      return Inner_Services;

   }// End of getServicesFromBundle

   Vector<Resource> getFCsFromBundle(Resource Bundle, Model MyModel)
   {
      // Get Functional Consequences
      Vector<Resource> SB_Elements = new Vector<Resource>();
      SB_Elements.add(Bundle);
      
      Vector<Resource> Functional_Consequences = new Vector<Resource>();

      Functional_Consequences = getRelatedObjects(getRelatedObjects(getRelatedObjects(getRelatedObjects(getRelatedObjects(SB_Elements,
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo"),
              MyModel, "http://www.cs.vu.nl/~gordijn/e3value#resource_has_consequence");

      //System.out.println(" Number of FCs: " + Functional_Consequences.size());

      //Functional Consequences
      Vector<Resource> FCs = new Vector<Resource>();
      for(int i = 0; i < Functional_Consequences.size(); i++)
      {
          Resource ThisS = Functional_Consequences.get(i);
          if(!FCs.contains(ThisS))
          {
              FCs.add(ThisS);
          }
      }

      return FCs;

   }// End of getFCsFromBundle
    
    //calculates score for single bundle, on basis of (1) consequences from bundle, (2) importance scores and (3) matched scales . (3) is needed to keep the mapping between consequences and importance scores
    Double doScoringBundle(Vector<Resource> consequencesBundle, Vector<Scale> matchedScales, Vector<Vector<Double>> preferenceScoresperScale, Model model)
    {
        Vector<Double> preferenceScores = new Vector<Double>();
        Vector<Double> consequenceScores = new Vector<Double>();
        System.out.print("\nScoring bundle ");
        System.out.println(" FCs ");
        for (int i = 0; i < consequencesBundle.size(); i ++)
        {
            Resource TempC = consequencesBundle.get(i);
            System.out.print("\n\t FC: " + TempC.getProperty(B2B.e3_has_name).getObject().toString());
        }
        System.out.println("\n\t Scales ");
        for (int i = 0; i < matchedScales.size(); i ++)
        {
            Scale TempS = matchedScales.get(i);
            System.out.print("\n\t Scale: " + TempS.getScaleName());
        }

        // Get Quality Consequences based on the FC
        Vector<Resource> VQCs = getRelatedObjects(consequencesBundle, model, "http://www.cs.vu.nl/~gordijn/e3value#depends_on");
        consequencesBundle.removeAllElements();
        //consequencesBundle = VQCs;

        for (int i = 0; i < VQCs.size(); i ++)
        {
            Resource TempC = VQCs.get(i);
            if(!consequencesBundle.contains(TempC))
            {
                consequencesBundle.add(TempC);
            }
        }
        
        System.out.println(" QCs " + VQCs.size() + " now : " + consequencesBundle.size());
        for (int i = 0; i < consequencesBundle.size(); i ++)
        {
            Resource TempC = consequencesBundle.get(i);
            System.out.print("\n\t QC: " + TempC.getProperty(B2B.e3_has_name).getObject().toString());
        }
        
        for (int i = 0; i < consequencesBundle.size(); i ++)
        {
            boolean Found = false;
            
            Vector<Resource> temp = new Vector<Resource>();
            temp.add(consequencesBundle.get(i));
            Double preferenceScoreConsequence= 0.0;
            Double consequenceScore=0.0;
            if (getRelatedObjects(temp, model, "http://www.cs.vu.nl/~gordijn/e3value#has_scale").size() !=0)
            {
                for(int j =0; j < matchedScales.size(); j ++)
                {
                    String ScaleName = matchedScales.get(j).getScaleName();
                    String FCScaleName = giveLiteral(getRelatedObjects(temp,
                        model, "http://www.cs.vu.nl/~gordijn/e3value#has_scale").get(0),
                        "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name");
                        
                    if (FCScaleName.equals(ScaleName))
                    {
                        int Limit = matchedScales.get(j).getScaleAsResourceSet().size();
                        for (int k = 0; k < Limit; k ++)
                        {
                            String ClassName = matchedScales.get(j).getClass().toString();
                            //if (matchedScales.get(j).getClass().toString().equals("class NominalScale"))
                            if (ClassName.equals("class service_applet_v01.NominalScale"))
                            {
                                NominalScale blaat = (NominalScale)matchedScales.get(j);
                                String Lit1 = giveLiteral(blaat.getNominalScaleAsResourceVector().get(k),
                                "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name");
                                String Lit2 = giveLiteral(consequencesBundle.get(i),
                                "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name" );
                                if (Lit1.equals(Lit2))
                                {
                                    preferenceScoreConsequence= preferenceScoresperScale.get(j).get(k);
                                    consequenceScore = 1.0;
                                }
                            }
                            //if (matchedScales.get(j).getClass().toString().equals("class OrdinalScale"))
                            if (ClassName.equals("class service_applet_v01.OrdinalScale"))
                            {
                                OrdinalScale blaat = (OrdinalScale)matchedScales.get(j);
                                if (giveLiteral(blaat.getScaleAsResourceVector().get(k),
                                "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name").equals(giveLiteral(consequencesBundle.get(i),
                                "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name" )))
                                {
                                    preferenceScoreConsequence = preferenceScoresperScale.get(j).get(k);
                                    consequenceScore =Calculation.CalculateROCForRanking(blaat.getScaleAsResourceVector()).get(k);
                                }
                            }
                        }
                    }
                }
            }

            preferenceScores.add(preferenceScoreConsequence);
            consequenceScores.add(consequenceScore);
        }// End - for (int i = 0; i < consequencesBundle.size(); i ++)
        
        Double RES = Calculation.CalculateMAScoreForBundle(preferenceScores, consequenceScores);
        return RES;
    }// End - doScoringBundle
   
    //calculates score for single bundle, on basis of (1) consequences from bundle, (2) importance scores and (3) matched scales . (3) is needed to keep the mapping between consequences and importance scores
    Double doScoringBundle2(Vector<Resource> consequencesBundle, Vector<Scale> matchedScales, Vector<KeyValuePair> ScoresperScale, Model model)
    {
        Vector<Double> preferenceScores = new Vector<Double>();
        Vector<Double> consequenceScores = new Vector<Double>();
        System.out.print("\nScoring bundle ");
        System.out.println(" FCs ");
        for (int i = 0; i < consequencesBundle.size(); i ++)
        {
            Resource TempC = consequencesBundle.get(i);
            System.out.print("\n\t FC: " + TempC.getProperty(B2B.e3_has_name).getObject().toString());
        }
        System.out.println("\n\t Scales ");
        for (int i = 0; i < matchedScales.size(); i ++)
        {
            Scale TempS = matchedScales.get(i);
            System.out.print("\n\t Scale: " + TempS.getScaleName());
        }

        // Get Quality Consequences based on the FC
        Vector<Resource> VQCs = getRelatedObjects(consequencesBundle, model, "http://www.cs.vu.nl/~gordijn/e3value#depends_on");
        consequencesBundle.removeAllElements();
        //consequencesBundle = VQCs;

        for (int i = 0; i < VQCs.size(); i ++)
        {
            Resource TempC = VQCs.get(i);
            if(!consequencesBundle.contains(TempC))
            {
                consequencesBundle.add(TempC);
            }
        }
        
        System.out.println(" QCs " + VQCs.size() + " now : " + consequencesBundle.size());
        for (int i = 0; i < consequencesBundle.size(); i ++)
        {
            Resource TempC = consequencesBundle.get(i);
            System.out.print("\n\t QC: " + TempC.getProperty(B2B.e3_has_name).getObject().toString());
        }
        
        for (int i = 0; i < consequencesBundle.size(); i ++)
        {
            boolean Found = false;
            Resource QCBundle = consequencesBundle.get(i);
            Vector<Resource> temp = new Vector<Resource>();
            temp.add(QCBundle);
            Double preferenceScoreConsequence= 0.0;
            Double consequenceScore=0.0;
            Vector<Resource> VQCScales = getRelatedObjects(temp, model, "http://www.cs.vu.nl/~gordijn/e3value#has_scale");
            
            if (VQCScales.size() !=0)
            {

               String BundleQCName = QCBundle.getProperty(B2B.e3_has_name).getObject().toString();
               String FCScaleName = giveLiteral(getRelatedObjects(temp,
                        model, "http://www.cs.vu.nl/~gordijn/e3value#has_scale").get(0),
                        "http://www.cs.vu.nl/~gordijn/e3value#e3_has_name");

               // Explore scores given by the customer
               for(int j = 0; j < ScoresperScale.size(); j++)
               {
                  KeyValuePair ThisTriple = ScoresperScale.get(j);
                  Resource CustomerQC = model.getResource(ThisTriple.Get_URI());
                  String CustomerQCName = ThisTriple.Get_Label();
                  if(CustomerQCName.equals(BundleQCName))
                  {
                     preferenceScoreConsequence = (Double) (1.0 * ThisTriple.Get_Score());
                     consequenceScore = 1.0;
                  }
                  else
                  {
                     // if it's ordinal the score could be different to zero
                  }
                  
               }
            }
            preferenceScores.add(preferenceScoreConsequence);
            consequenceScores.add(consequenceScore);
        }// End - for (int i = 0; i < consequencesBundle.size(); i ++)
        
        Double RES = Calculation.CalculateMAScoreForBundle(preferenceScores, consequenceScores);
        RES = (RES / ScoresperScale.size());
        return RES;
    }// End - doScoringBundle2

    public Double getFitness(Vector<Resource> consequencesBundle, Vector<Resource> requiredFCs, Vector<KeyValuePair> scoresperFCs, Model model)
    {
        Double Res = 0.0;
        Double SumCustP = 0.0;
        Double Wp = 0.0;
        Double Wm = 0.0;
        Double Wn = 0.0;
        Double AWEFCs = 0.125;
        Vector<Double> CustP = new Vector<Double>();
        
        for(int k=0; k < scoresperFCs.size(); k++)
        {
            KeyValuePair ThisScore = scoresperFCs.get(k);
            SumCustP = SumCustP + (ThisScore.Get_Score() * 0.1);
        }
        
        int NumMatched = 0;
        for(int i = 0; i < requiredFCs.size(); i++)
        {
            Resource ReqFC = requiredFCs.get(i);
            String NameRFC = ReqFC.getProperty(B2B.e3_has_name).getObject().toString();
            for(int j = 0; j < consequencesBundle.size(); j++)
            {
                Resource BundleFC = consequencesBundle.get(j);
                String NambeBFC = BundleFC.getProperty(B2B.e3_has_name).getObject().toString();
                if(NameRFC.equals(NambeBFC))
                {
                    NumMatched++;
                    // Get also the preference
                    for(int k = 0; k < scoresperFCs.size(); k++)
                    {
                        KeyValuePair ThisScore = scoresperFCs.get(k);
                        if(NameRFC.equals(ThisScore.Get_Label()))
                        {
                            Wp = Wp + (ThisScore.Get_Score() * 0.1);                    
                        }
                    }
                }
            }
        }
        Wm = SumCustP - Wp;
        int Extra = consequencesBundle.size() - requiredFCs.size();  
        if(Extra > 0)
           Wn = Extra * AWEFCs;
        // Fractions
        Double TWc = SumCustP;
        Double TWsvn = Wp + Wm + Wn;
        Double FP = Wp / TWc;
        Double FM = Wm / TWc;
        Double FN = Wn / TWsvn;
        // Fuzzification
        FIS MyFIS = new FIS();
        
        Double FPfew  = MyFIS.MyNormD(FP,0.0,0.4,2);
        Double FPsome = MyFIS.MyNormD(FP,0.5,0.4,2);
        Double FPmany = MyFIS.MyNormD(FP,1.0,0.4,2);
    
        Double FMfew  = MyFIS.MyNormD(FM,0.0,0.4,2);
        Double FMsome = MyFIS.MyNormD(FM,0.5,0.4,2);
        Double FMmany = MyFIS.MyNormD(FM,1.0,0.4,2);
    
        Double FNfew  = MyFIS.MyNormD(FN,0.0,0.4,2);
        Double FNsome = MyFIS.MyNormD(FN,0.5,0.4,2);
        Double FNmany = MyFIS.MyNormD(FN,1.0,0.4,2);
        
        // Inference / Analysis
        Double Perfect = 0.0;
        Double Good    = 0.0;
        Double Average = 0.0;
        Double Poor    = 0.0;
    
        // 1
        if ( ( (FPmany > 0.0) && (FMfew > 0.0)) && (FNfew > 0) )
              Perfect = Perfect + Math.min(Math.min(FPmany,FMfew),FNfew);
    
        //% 2
        if( ( (FPmany > 0) && (FMfew > 0)) && (FNsome > 0))
           Good = Good + Math.min(Math.min(FPmany,FMfew),FNsome);
            //Perfect = Perfect + Math.min(Math.min(FPmany,FMfew),FNsome);
    
        //% 3
        if( ( (FPmany > 0) && (FMsome > 0)) && (FNfew > 0))
           Good = Good + Math.min(Math.min(FPmany,FMsome),FNfew);
        // % 4
        if( ( (FPmany > 0) && (FMsome > 0)) && (FNsome > 0))
           Good = Good + Math.min(Math.min(FPmany,FMsome),FNsome);
        // % 5
        if( ( (FPsome > 0) && (FMfew > 0)) && (FNfew > 0))
           Average = Average + Math.min(Math.min(FPsome,FMfew),FNfew);
        // 6   
        if( ( (FPsome > 0) && (FMfew > 0)) && (FNsome > 0))
           Average = Average + Math.min(Math.min(FPsome,FMfew),FNsome);
        // 7    
        if( ( (FPsome > 0) && (FMsome > 0)) && (FNfew > 0))
           Poor = Poor + Math.min(Math.min(FPsome,FMsome),FNfew);
        // 8
        if( ( (FPsome > 0) && (FMsome > 0)) && (FNsome > 0))
           Poor = Poor + Math.min(Math.min(FPsome,FMsome),FNsome);
           
        // Defuzzification
        // Po = zeros(size(z));
        // Av = zeros(size(z));
        // Go = zeros(size(z));
        // Pe = zeros(size(z));
        // Sh = zeros(size(z));
        float[] Po = new float[11];
        float[] Av = new float[11];
        float[] Go = new float[11];
        float[] Pe = new float[11];
        float[] Sh = new float[11];
        
        for(int i=1; i<11; i++)
        {
            Sh[i] = 0;
        }
        
        //Perfect
        if(Perfect > 0)
           for (int i=1; i < 11; i++)
           {
              Pe[i] = (float) MyFIS.MyNormD((double)(i*1.0),11,0.4,0.1250);
              if(Pe[i] > Perfect)
                Pe[i] = (float) (1.0 * Perfect);
              Sh[i] = Pe[i];
           }
        
        //%Good
        if(Good > 0)
           for (int i=1; i < 11; i++)
           {
              Go[i] = (float) MyFIS.MyNormD((double)(i*1.0),7.5,0.4,0.1250);
              if(Go[i] > Good)
                 Go[i] = (float) (1.0 * Good);
              if(Go[i] > Sh[i])
                Sh[i] = Go[i];
           }
        
        //%Average
        if(Average > 0)
           for(int i=1; i < 11; i++)
           {
              Av[i] = (float) MyFIS.MyNormD((double)(i*1.0),5.0,0.4,0.1250);
              if(Av[i] > Average)
                 Av[i] = (float) (1.0 * Average);
              if(Av[i] > Sh[i])
                Sh[i] = Av[i];
           }
            
        //%Poor
        if(Poor> 0)
           for(int i=1; i < 11; i++)
           {
              Po[i] = (float) MyFIS.MyNormD((double)(i*1.0),2.5,0.4,0.1250);
              if(Po[i] > Poor)
                 Po[i] = (float) (1.0 * Poor);
              if(Po[i] > Sh[i])
                 Sh[i] = Po[i];
           }
            
        //(z*Sh')/sum(Sh)];
        float MyNum = 0;
        float MyDen = 0;
        for(int i=1; i < 11; i++)
        {
            MyNum = MyNum + (i * Sh[i]);
            MyDen = MyDen + Sh[i];
        }
        
        Res = (double) MyNum / MyDen;
        
        return Res;
    }// End - getFitness
    
    public Vector<Resource> computeCulpit(Vector<KeyValuePair> PreferencesFCs, Model model)
    {
       Vector<Resource> NewFCs = new Vector<Resource>();
       //System.out.println("\n Let's see what we got");

       for(int i = 0; i < PreferencesFCs.size(); i++)
       {
          KeyValuePair ThiKV = PreferencesFCs.get(i);
          int ThisScore = ThiKV.Get_Score();
          Resource ThisFC = model.getResource(ThiKV.Get_URI());
          if(ThisScore >= 8)
          {
             if(ThisFC != null)
             {
                NewFCs.add(ThisFC);
             }
          }

          //System.out.print("\n FC: " + ThiKV.Get_Label());
          //System.out.print("\t Score: " + ThiKV.Get_Score());
          //System.out.print("\n URI: " + ThiKV.Get_URI());
       }
       
       return NewFCs;
    }
}

