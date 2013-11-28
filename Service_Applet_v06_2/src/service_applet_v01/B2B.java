/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;

import java.io.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import java.util.*;

/**
 *
 * @author ivan
 */
public class B2B {
    /**
     * @param args the command line arguments
     */
   //static final String myURI = "http://www.cs.vu.nl/~gordijn/e3value";
   public static Property ce_with_down_de;
   public static Property ce_with_up_de;
   public static Property de_down_ce;
   public static Property de_up_ce;
   public static Property di_has_mc;
   public static Property mc_in_di;
   public static Property mc_in_mo;
   public static Property mo_has_mc;
   public static Property va_has_vi;
   public static Property ve_has_in_po;
   public static Property ve_has_out_po;
   public static Property ve_in_vt;
   public static Property vi_assigned_to_va;
   public static Property vi_consists_of_of;
   public static Property vo_consists_of_vp;
   public static Property vo_in_vi;
   public static Property vo_offered_requested_by_vp;
   public static Property vp_in_connects_ve;
   public static Property vp_in_vo;
   public static Property vp_out_connects_ve;
   public static Property vp_requests_offers_vo;
   public static Property vt_consists_of_ve;

   public static Property down_fraction;
   public static Property e3_has_formula;
   public static Property e3_has_name;
   public static Property e3_has_uid;
   public static Property up_fraction;
   public static Property vp_has_dir;
   public static Property vt_has_fraction;

   public static Property el_performs_va;
   public static Property vi_assigned_to_ac;
   public static Property ac_has_vi;
   public static Property va_performed_by_el;

   public static Property ve_has_first_vp;
   public static Property ve_has_second_vp;
   public static Property vp_first_connects_ve;
   public static Property vp_second_connects_ve;

   public static Property depends_on;
   public static Property dependent_of;

   //public static Property vp_has_attribute_at;
   //public static Property at_assigned_to_vp;
   public static Property e3_has_value;
   public static Property vi_depends_on_vi;

   // Instances
   public static Property vi_has_vii, vii_of_vi;
   public static Property vii_has_vpi, vpi_of_vii, vpi_has_dir;
   public static Property vpi_has_vai, vai_of_vpi;

   // consequences
   public static Property consequence_has_resource, resource_has_consequence;
   public static Property has_scale;
   public static Property consists_of;
   public static Property has_want;
   public static Property has;
   

   // Composite actor
   public static Property ca_consists_of_vi, vi_in_ca;

   public Integer TrackID;

   public String CA;
   public String EA;
   public String CE;
   public String AND;
   public String OR;
   public String VI;
   public String VA;
   public String VOb;
   public  String e3value;
   public String SS;
   public String ES;
   public String IName;
   public String SKName;
   public boolean Traverse;
   public boolean Using_Explosion;
   public Integer ID;

   public String VII;
   public String FConsequence;
   public String VQConsequence;
   public String VNominalS;
   public String Want;

   public B2B(Integer TrackID)
   {
      EA   = new String("http://www.cs.vu.nl/~gordijn/e3value#elementary_actor");
      CE   = new String("http://www.cs.vu.nl/~gordijn/e3value#connection_element");
      AND  = new String("http://www.cs.vu.nl/~gordijn/e3value#e3value_AND");
      OR  = new String("http://www.cs.vu.nl/~gordijn/e3value#e3value_OR");
      VI   = new String("http://www.cs.vu.nl/~gordijn/e3value#value_interface");
      VA   = new String("http://www.cs.vu.nl/~gordijn/e3value#value_activity");
      VOb  = new String("http://www.cs.vu.nl/~gordijn/e3value#value_object");
      e3value = new String("http://www.cs.vu.nl/~gordijn/e3value#");
      SS    = new String("http://www.cs.vu.nl/~gordijn/e3value#start_stimulus");
      ES    = new String("http://www.cs.vu.nl/~gordijn/e3value#end_stimulus");
      CA    = new String("http://www.cs.vu.nl/~gordijn/e3value#composite_actor");
      IName = new String("http://www.cs.vu.nl/~gordijn/BusinessCase01#");
      SKName = new String("http://www.cs.vu.nl/~gordijn/Skeleton01#");

      FConsequence = new String("http://www.cs.vu.nl/~gordijn/e3value#functional_consequence");
      VQConsequence = new String("http://www.cs.vu.nl/~gordijn/e3value#quality_consequence");
      VNominalS = new String("http://www.cs.vu.nl/~gordijn/e3value#nominal");
      Want = new String("http://www.cs.vu.nl/~gordijn/e3value#want");
      VII   = new String("http://www.cs.vu.nl/~gordijn/e3value#value_interface_ins");

      ID   = 1;
      Traverse = true;
      Using_Explosion = false;
      this.TrackID = TrackID;
   }

   public Integer generateID()
   {
      if(ID == 1667)
      {
         //System.out.println("\n\t\t\t ??? ");
      }
      return ++ID;
   }

   public Integer getID()
   {
      return ID;
   }

   public void SetID(int NewID)
   {
      ID = NewID;
   }

   public static void Principal(String[] args)
   {
      // TODO code application logic here
      B2B MyB2B = new B2B(313556); // DE_WAARHEID
      //System.out.println(" Main Algorithm ");

      Model Consumer_Need = ModelFactory.createDefaultModel();
      Model Service_Providers = ModelFactory.createDefaultModel();
      Consumer_Need = MyB2B.Read_Model("Config_Data//Need-3-1.rdf");
      Service_Providers = MyB2B.Read_Model("Config_Data//Service_Providers_v01.rdf");
      MyB2B.Read_Properties(Consumer_Need);

      Model MyModel = ModelFactory.createDefaultModel();
      MyB2B.Generate_Diagram_and_Model(MyModel);

      //start_stimulus = Find_Star_Stimulus(Skeleton);
      Resource start_stimulus = MyB2B.Find_Start_Stimulus(Consumer_Need);

      //AuxVA    = Get_Value_Activity(start_stimulus);
      Resource AuxVA = MyB2B.Get_Value_Activity(Consumer_Need, start_stimulus);
      Resource AuxEA = MyB2B.Get_Elementary_Actor(Consumer_Need, AuxVA);

      //System.out.println(" Elementary Actor " + AuxEA.toString());

      //MyVA     = Generate_Value_Activity(AuxVA, MyModel);
      Resource MyVA = MyB2B.Generate_Value_Activity(AuxVA, MyModel);
      Resource MyEA = MyB2B.Generate_Elementary_Actor(MyModel,AuxEA.getProperty(e3_has_name).getObject().toString(),true);
      MyB2B.Assign_Activity_to_Actor(MyModel, MyVA, MyEA, null);

      //AuxCE    = start_stimulus->connection_element;
      Resource AuxCE = MyB2B.Get_First_CE(start_stimulus,Consumer_Need, de_down_ce);
      Resource MySS  = MyB2B.Generate_Start_Stimulus(MyModel);
      Resource MyCE  = MyB2B.Generate_Connection_Element(MyModel);
      MyModel.add(MySS, de_down_ce, MyCE);
      MyModel.add(MyCE, ce_with_up_de, MySS);
      ////System.out.println("   Resource : " + AuxCE.toString());

      // Reading start stimulus in Consumer_Need
      Resource CDstart_stimulus = MyB2B.Find_Start_Stimulus(Consumer_Need);
      //System.out.println("\n===   T R A V E R S I N G    M O D E L === \n");
      String InitialObject = new String("");
      MyB2B.Traverse_CNeed(AuxCE, MyVA, MyModel, Consumer_Need, MyCE,Service_Providers, MyEA);

      //System.out.println(" ============================================= ");
      //System.out.println("                  M o d e l                    ");
      //System.out.println(" ============================================= ");
      MyB2B.Fill_Diagram_and_Model(MyModel);
      MyModel.setNsPrefix("a", MyB2B.e3value);
      MyModel.write(System.out);
      //System.out.println(" ID : " + MyB2B.getID() );
      //System.out.println("a:down_fraction=M");
      //System.out.println("a:e3_has_value_object=RIGHT_MP");
      //System.out.println("a:down_fraction=N");
      //System.out.println("a:e3_has_role=ROLE_P");
      //System.out.println(" CHANGE SPECICI-CASE INFORMATION");
      //Consumer_Need.write(System.out);
   }

   public void Generate_Diagram_and_Model(Model MyModel)
   {
      Resource model;
      Resource diagram;
      model = MyModel.createResource(IName + "0");
      model.addProperty(e3_has_name, "model");
      model.addProperty(e3_has_uid, "0");
      Resource X     = MyModel.createResource(e3value + "model");
      MyModel.add(model, RDF.type, X);

      diagram = MyModel.createResource(IName + "1");
      diagram.addProperty(e3_has_name, "Bussines_Model");
      diagram.addProperty(e3_has_uid, "1");
      X     = MyModel.createResource(e3value + "diagram");
      MyModel.add(diagram, RDF.type, X);
   }

   public void Fill_Diagram_and_Model(Model MyModel)
   {
      Resource model;
      Resource diagram;
      Resource AuxRes;
      int i;
      model = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      for(i = 2; i <= getID(); i++)
      {
         AuxRes = MyModel.getResource(IName + i);
         MyModel.add(model, mo_has_mc, AuxRes);
         MyModel.add(diagram, di_has_mc, AuxRes);
      }
   }

   public void Fill_Diagram_and_Model(Model MyModel, int IncID, int LastID)
   {
      Resource model;
      Resource diagram;
      Resource AuxRes;
      int i;
      model = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      for(i = 2; i <= (2 + IncID); i++)
      {
         AuxRes = MyModel.getResource(IName + (LastID + i - 2));
         MyModel.add(model, mo_has_mc, AuxRes);
         MyModel.add(diagram, di_has_mc, AuxRes);
      }
   }

   public Resource Get_Connected_To(Resource CE, Model Skeleton, Property ThisProperty)
   {
      Resource MyResource = null;
      Statement AuxSt     = null;
      // connection_element -> ce_with_down_de
      String AuxStr;
      AuxSt = CE.getProperty(ThisProperty); //ce_with_down_de
      ////System.out.println(" ce_with_down_de Connected to: " + AuxSt.getObject().toString());
      if(AuxSt != null)
         MyResource = Skeleton.getResource(AuxSt.getObject().toString());
      else
      {
         String TypeProperty = CE.getProperty(RDF.type).getObject().toString();
         if(TypeProperty != null)
         {
            if(TypeProperty.equals(VI))
               MyResource = Get_Connected_To_VI(CE, Skeleton);
         }
      }
      return MyResource;
   }

   public Resource Get_Connected_To_VI(Resource VI1, Model Skeleton)
   {
      // VI1 is a value interface element
      // This functions returns the value interface connected to VI1
      Resource MyResource = null;
      Resource VI2      = null;
      Statement AuxSt     = null;
      // connection_element -> ce_with_down_de
      String AuxStr;

      // ===============================================
      ////System.out.println("  Value Offerings : ");
      Property AuxProperty;
      String AuxString = null;
      StmtIterator iter2 = VI1.listProperties(vi_consists_of_of); //VI1.listProperties();
      while (iter2.hasNext())
      {
         AuxSt = null;
         AuxProperty = null;
         AuxSt = iter2.nextStatement();
         ////System.out.println("\t " + AuxProperty.toString());
         ////System.out.println("\t " + AuxSt.getObject().toString());
         Resource AuxRes = Skeleton.getResource(AuxSt.getObject().toString());

         //vo_consists_of_vp
         AuxSt = AuxRes.getProperty(vo_consists_of_vp);
         ////System.out.println("\t\t " + AuxSt.getObject().toString());
         AuxRes = Skeleton.getResource(AuxSt.getObject().toString());

         //vp_out_connects_ve
         AuxSt = AuxRes.getProperty(vp_out_connects_ve);
         if(AuxSt == null)
         {
            AuxSt = AuxRes.getProperty(vp_second_connects_ve);
         }

         if(AuxSt != null)
         {
            ////System.out.println("\t\t\t " + AuxSt.getObject().toString());
            AuxRes = Skeleton.getResource(AuxSt.getObject().toString());

            //ve_has_in_po
            AuxSt = AuxRes.getProperty(ve_has_in_po);
            if(AuxSt == null)
            {
               AuxSt = AuxRes.getProperty(ve_has_first_vp);
            }

            if(AuxSt != null)
            {
               ////System.out.println("\t\t\t\t " + AuxSt.getObject().toString());
               AuxRes = Skeleton.getResource(AuxSt.getObject().toString());

               //vp_in_vo
               AuxSt = AuxRes.getProperty(vp_in_vo);
               if(AuxSt != null)
               {
                  ////System.out.println("\t\t\t\t\t " + AuxSt.getObject().toString());
                  AuxRes = Skeleton.getResource(AuxSt.getObject().toString());

                  //vo_in_vi
                  AuxSt = AuxRes.getProperty(vo_in_vi);
                  ////System.out.println("\t\t\t\t\t\t " + AuxSt.getObject().toString());
                  ////System.out.println("\t Next " + AuxSt.getObject().toString());
                  AuxRes = Skeleton.getResource(AuxSt.getObject().toString());
                  VI2 = AuxRes;
                  break;
               }
            }
         }
      }
      // ===============================================

      //AuxSt = VI2.getProperty(ThisProperty);//de_down_ce
      ////System.out.println(" ce_with_down_de Connected to: " + AuxSt.getObject().toString());
      //MyResource = Skeleton.getResource(AuxSt.getObject().toString());
      MyResource = VI2;
      return MyResource;
   }

   // Resource AuxCe - Connection Element in the Skeleton
   // Resource MyVA - Current Value activity in MyModel
   // Model    MyModel - Model to be generated
   // Model    Skeleton - Skeleton to explore
   // Model    Consumer_Need   - Model with all the info about Consumer_Need
   // Resource MyCE     - Connection Element in MyModel
   // String Specific_Object - Specific_Object to be sent
   public void Traverse_CNeed(Resource AuxCE, Resource MyVA, Model MyModel,
           Model Consumer_Need, Resource MyCE, Model Service_Providers, Resource MyEA)
   {
      Resource MyResource = null;
      String connected = null;
      Property AuxProperty = null;

      MyResource = Get_Connected_To(AuxCE,Consumer_Need,ce_with_down_de);
      connected = MyResource.getProperty(RDF.type).getObject().toString();

      //System.out.print("\n CONSUMER NEED ===== " + AuxCE.toString());
      //System.out.println(" Connected to : " + connected);
      //System.out.print(MyResource.toString());

      if(connected.equals(VI))
      {
         ////System.out.println(" === VALUE INTERFACE === ");
         ////System.out.println(" ");
         Resource MyVI  = Generate_Value_Interface(MyModel);
         Resource MyVI2 = Generate_Value_Interface(MyModel);
         // Linking VI to CE
         MyModel.add(MyCE, ce_with_down_de, MyVI);
         MyModel.add(MyVI, de_up_ce, MyCE);
         // Linking VI to VA
         MyModel.add(MyVI, vi_assigned_to_va, MyVA);
         MyModel.add(MyVA, va_has_vi, MyVI);
         // Copy info from MyResource to MyVI
         Copy_Value_Interfaces(Consumer_Need, MyResource, MyModel, MyVI,false);
         // Get Second Interface
         Resource VI2 = Get_Connected_To_VI(MyResource, Consumer_Need);
         // Copy info from MyResource to MyVI
         Copy_Value_Interfaces(Consumer_Need, VI2, MyModel, MyVI2,false);
         // Linking VI2 to VEA
         MyModel.add(MyVI2, vi_assigned_to_ac, MyEA);
         MyModel.add(MyEA, ac_has_vi, MyVI2);
         Link_Value_Interfaces_2(MyModel, MyVI, MyVI2);

         // Copy Value Interface Instances
         Copy_Value_Interface_Instances(Consumer_Need, VI2, MyModel, MyVI2, true, null);

         // Start Looking for Value Objects to match this Value Interface - MyVI
         Match_Value_Interface(MyModel, MyVI2, Service_Providers,false);
      }
      else
      {
         if(connected.equals(AND))
         {
            // STARTING AND

            ////System.out.println(" +++ AND +++ ");
            ////System.out.println(" ");
            Resource MyAnd = Generate_e3value_AND(MyModel);
            MyModel.add(MyCE, ce_with_down_de, MyAnd);
            MyModel.add(MyAnd, de_up_ce, MyCE);
            // ===============================================
            ////System.out.println("   Connections : ");
            String AuxString = null;
            StmtIterator iter2 = MyResource.listProperties();
            while (iter2.hasNext())
            {
               Statement AuxSt, AuxSt2;
               AuxSt2 = null;
               AuxProperty = null;
               AuxSt = iter2.nextStatement();
               ////System.out.println("\t " + AuxSt.getObject().toString());
               AuxProperty = AuxSt.getPredicate();
               if(AuxProperty != null)
                  if(AuxProperty.equals(de_down_ce))
                  {
                     Resource AuxRes = Consumer_Need.getResource(AuxSt.getObject().toString());
                     ////System.out.println("\t " + AuxProperty.toString());
                     ////System.out.println("\t " + AuxSt.getObject().toString());

                     Resource MyAndCE = Generate_Connection_Element(MyModel);
                     MyModel.add(MyAnd, de_down_ce, MyAndCE);
                     MyModel.add(MyAndCE, ce_with_up_de, MyAnd);

                     Traverse_CNeed(AuxRes, MyVA, MyModel, Consumer_Need,MyAndCE,Service_Providers, MyEA);
                  }
            }// while (iter2.hasNext()) Each connection element in the AND gate
         }// End AND
      }
   }

   public void Match_Value_Interface(Model MyModel, Resource MyVI, Model Service_Providers, boolean Firstfound)
   {
      // Look for each value interface ...
      //System.out.println("\n Trying to match : " + MyVI.toString());
      boolean Matched = false;
      ResIterator RITValue_Interfaces = MyModel.listResourcesWithProperty(vi_assigned_to_ac);
      Resource Second_VI = null;
      //if(OnlyMyModel)
      //{
        System.out.println("\n\t First Exploring current model");
        while(RITValue_Interfaces.hasNext())
        {
            Second_VI = RITValue_Interfaces.nextResource();
            Resource Possible_Actor = MyModel.getResource(Second_VI.getProperty(vi_assigned_to_ac).getObject().toString());
            ////System.out.println("\n\t Possible Actor : " + Possible_Actor.getProperty(e3_has_name).getObject().toString());
            Resource MyVII = null;

            MyVII = Linkables(MyModel, MyVI, MyModel, Second_VI,true);
            if(!Firstfound)
            {
                // Special case for copy_svn
                boolean Answer = IsItFree(MyModel, Second_VI);
                if(!Answer)
                    MyVII = null;
            }

            if(MyVII != null)
            {
                //  Generate value exchanges
                Generate_Value_Exchanges_2(MyModel, MyVI, Second_VI);
                Matched = true;
                break;
            }
        }
      //}
      //if( (!Matched) && (!OnlyMyModel))
      if(!Matched)
      {
         System.out.println("\n\t If not matched exploring service providers ... ");
         RITValue_Interfaces = Service_Providers.listResourcesWithProperty(vi_assigned_to_ac);
         Second_VI = null;
         while(RITValue_Interfaces.hasNext())
         {
            Second_VI = RITValue_Interfaces.nextResource();
            Resource Possible_Actor = Service_Providers.getResource(Second_VI.getProperty(vi_assigned_to_ac).getObject().toString());
            //System.out.println("\n\t Possible Actor : " + Possible_Actor.getProperty(e3_has_name).getObject().toString());
            Resource MyVII = null;
            MyVII = Linkables(MyModel, MyVI, Service_Providers, Second_VI,true);
            if(MyVII != null)
            {
               System.out.println("\n\t MATHED : " + Possible_Actor.getProperty(e3_has_name).getObject().toString());
               // Get current actor
               // Resource MyFirstActor = MyModel.getResource(MyVI.getProperty(vi_assigned_to_ac).getObject().toString());
               // Generate a new value interface
               Resource MyVI2 = Generate_Value_Interface(MyModel);
               //  Copy everything that is in the Service Providers side
               Resource TmpSecondActor = Service_Providers.getResource(Second_VI.getProperty(vi_assigned_to_ac).getObject().toString());
               Resource MySecondActor = Generate_Elementary_Actor(MyModel,TmpSecondActor.getProperty(e3_has_name).getObject().toString(),true);
               //  Generate value exchanges
               Generate_Value_Exchanges(MyModel, MyVI, MyVI2);
               // Assign the SECOND value_interface to the SECOND actor
               MyModel.add(MyVI2, vi_assigned_to_ac, MySecondActor);
               MyModel.add(MySecondActor, ac_has_vi, MyVI2);

               // Filtrate attributes from Second_VI to MyVI2 according to MyVI
               // Copy_Attributes(MyModel, MyVI, MyModel, MyVI2, true);

               // Resource New_VII = Copy_Value_Interface_Instances(Service_Providers, null, MyModel, MyVI2, false, MyVII);


               //  Determine dependencies for the second VI
               //Copy_Dependent_Interfaces(MyModel, MySecondActor, MyVI2, New_VII, Service_Providers);

               // Copy from the service provider the activities within the flow
               //System.out.println("\n\t\t\t\t\t MyVI (0) : " + MyVI2.toString());
               //System.out.println("\n\t\t\t\t\t Next Resource (0) : " + Second_VI.toString());

               Copy_Activities_in_Flow(MyModel, MySecondActor, MyVI2, null, Service_Providers, TmpSecondActor, Second_VI);

               // Later we have to evaluate if more than one interface can match
               break;
            }
         }
      }
   }

   public void Copy_Dependent_Interfaces(Model MyModel, Resource MyActor, Resource MyVI, Resource MyVII, Model Service_Providers)
   {
      //System.out.println("\n\t\t\t\t Copy_Dependent_Interfaces for : "  + MyVI.toString());
      //System.out.println("\n\t\t\t\t Copy_Dependent_Interfaces from : " + MyVII.toString());
      //System.out.println("\n\t\t\t\t\t Actor : " + MyActor.getProperty(e3_has_name).getObject().toString());
      StmtIterator IT_VII = MyVII.listProperties(de_down_ce);
      Statement ST_VII = null;
      while(IT_VII.hasNext())
      {
         ST_VII = IT_VII.nextStatement();
         Resource TmpVII = Service_Providers.getResource(ST_VII.getObject().toString());
         Resource TmpVI = Service_Providers.getResource(TmpVII.getProperty(vii_of_vi).getObject().toString());
         //System.out.println("\n\t\t\t\t\t " + TmpVII.toString());
         //System.out.println("\t\t\t\t\t " + TmpVI.toString());
         // ==============
         Resource MyNewVI  = Generate_Value_Interface(MyModel);
         // Linking MyNewVI to MyActor
         MyModel.add(MyNewVI, vi_assigned_to_ac, MyActor);
         MyModel.add(MyActor, ac_has_vi, MyNewVI);
         // Copy info from TmpVII to MyNewVI
         Copy_Value_Interfaces(Service_Providers, TmpVI, MyModel, MyNewVI,false);
         // Copy Value Interface Instances
         Copy_Value_Interface_Instances(Service_Providers, TmpVI, MyModel, MyNewVI, false, TmpVII);
         // ==============
      }// END while(ItSecond_VI.hasNext())
   }

   public boolean Copy_Activities_in_Flow(Model MyModel, Resource MyActor, Resource MyResource, Resource Current_Activity,
           Model Service_Providers, Resource SecondActor, Resource SecondResource)
   {
      boolean result = false;
      Resource NextResource = null;
      String connected = null;
      ////System.out.println("\n\t\t Copy_Activities_in_Flow .... ");
      
      NextResource = Get_Connected_To(SecondResource,Service_Providers,de_down_ce);
      if(NextResource == null)
      {
         NextResource = Get_Connected_To(SecondResource,Service_Providers,ce_with_down_de);
         if(NextResource == null)
         {
            // Special case - maybe calling Match_Value_Interface
            // Validate if SecondResource is a VI
         }
      }
      String MyType = NextResource.getProperty(RDF.type).getObject().toString();

      // Value Interface
      if(MyType.equals(VI))
      {
         //IF VI is assigned to an activity
         //System.out.println("\n\t\t\t Value Interface ...");
         Statement StAssigned = NextResource.getProperty(vi_assigned_to_va);
         if(StAssigned != null)
         {
            String Assigned = StAssigned.getObject().toString();
            Resource SP_VA = Service_Providers.getResource(Assigned);
            if(SP_VA != null)
            {
               // Generate VA, Copy VI, Assign VI to VA, Generate VTransfers between ValueInterfaces
               Resource MyVA = Generate_Value_Activity(SP_VA, MyModel, MyActor.getProperty(e3_has_name).getObject().toString());
               Resource MyVI = Generate_Value_Interface(MyModel);
               // Copy info from MyResource to MyVI
               Copy_Value_Interfaces(Service_Providers, NextResource, MyModel, MyVI,false);
               // Activity was already in the model
               if(MyVA.hasProperty(va_performed_by_el))
               {
                  // Link VI with previous element
                  MyModel.add(MyVI, de_up_ce, MyResource);
                  MyModel.add(MyResource, ce_with_down_de, MyVI);
               }
               else // Assign activity
               {
                  Assign_Activity_to_Actor(MyModel, MyVA, MyActor,null);
                  // VTransfers ...
                  Link_Value_Interfaces_2(MyModel, MyResource, MyVI);
               }
               // Linking VI to VA
               MyModel.add(MyVI, vi_assigned_to_va, MyVA);
               MyModel.add(MyVA, va_has_vi, MyVI);
               //NextResource = Get_Connected_To(NextResource,Service_Providers,de_down_ce);
               //System.out.println("\n\t\t\t\t\t MyVI (1) : " + MyVI.toString());
               //System.out.println("\n\t\t\t\t\t Next Resource (1) : " + NextResource.toString());
               Copy_Activities_in_Flow(MyModel, MyActor, MyVI, MyVA, Service_Providers, SecondActor, NextResource);
            }
         }
         else // Assigned to actor
         {
            StAssigned = NextResource.getProperty(vi_assigned_to_ac);
            String Assigned = StAssigned.getObject().toString();
            Resource SP_VA = Service_Providers.getResource(Assigned);
            if(SP_VA != null)
            {
               //
               //System.out.println("\n\t\t VI Assigned to actor ");
               // Find the right VI
               StmtIterator ItMyActorVI = MyActor.listProperties(ac_has_vi);
               //System.out.println("\n\t\t\t MyActor " + MyActor.getProperty(e3_has_name).getObject().toString());
               //System.out.println("\n\t\t\t ID " + MyActor.getProperty(e3_has_uid).getObject().toString());
               Statement StMyActorVI = null;
               String ID1 = MyResource.getProperty(e3_has_uid).getObject().toString();
               if(MyActor.hasProperty(ac_has_vi))
               {
                  while(ItMyActorVI.hasNext())
                  {
                     StMyActorVI = ItMyActorVI.nextStatement();
                     Resource ThisVI = MyModel.getResource(StMyActorVI.getObject().toString());
                     String ID2 = ThisVI.getProperty(e3_has_uid).getObject().toString();
                     if(!ID1.equals(ID2))
                     {
                        if(Internally_Linkables(MyModel, MyResource, MyModel, ThisVI,false))
                        {
                           // VTransfers ...
                           Link_Value_Interfaces_2(MyModel, MyResource, ThisVI);
                           //System.out.println("\n\t\t\t\t\t MyResource : " + MyResource.toString());
                           //System.out.println("\n\t\t\t\t\t Next Resource : " + ThisVI.toString());
                           // Start Looking for Value Objects to match this Value Interface - MyVI
                           Match_Value_Interface(MyModel, ThisVI, Service_Providers,false);
                           break;
                        }
                     }
                     //System.out.println("\n\t\t\t Entering ");
                  }// END  while(ItMyActorVI.hasNext())
               }// END if(ItMyActorVI != null)
            }
         }
      }
      else
      {
         if(MyType.equals(CE))
         {
            //System.out.println("\n\t\t\t Connection Element ... SR " + SecondResource.toString() + " : NR " + NextResource.toString());
            // Connection element
            Resource MyCE = Generate_Connection_Element(MyModel);
            MyModel.add(MyResource, de_down_ce, MyCE);
            MyModel.add(MyCE, ce_with_up_de, MyResource);

            Resource NextElement = Get_Connected_To(NextResource,Service_Providers,ce_with_down_de);
            MyType = NextElement.getProperty(RDF.type).getObject().toString();
            // Three types : AND / OR gates, Value Interface
            if(MyType.equals(VI))
            {
               //System.out.println("\n\t VI connection");
               Resource MyVI = Generate_Value_Interface(MyModel);
               // Copy info from MyResource to MyVI
               Copy_Value_Interfaces(Service_Providers, NextElement, MyModel, MyVI,false);
               // Linking VI to CE
               MyModel.add(MyVI, de_up_ce, MyCE);
               MyModel.add(MyCE, ce_with_down_de, MyVI);
               // Linking VI to Current_Activity
               MyModel.add(MyVI, vi_assigned_to_va, Current_Activity);
               MyModel.add(Current_Activity, va_has_vi, MyVI);

               //System.out.println("\n\t\t\t\t\t MyVI (2) : " + MyVI.toString());
               //System.out.println("\n\t\t\t\t\t Next Resource (2) : " + NextElement.toString());

               Copy_Activities_in_Flow(MyModel, MyActor, MyVI, Current_Activity, Service_Providers, SecondActor, NextElement);
            }
            else
            {
               if(MyType.equals(AND))
               {
                  //System.out.println("\n\t AND connection : " + NextElement.toString());
                  Resource MyAnd = Generate_e3value_AND(MyModel);
                  MyModel.add(MyAnd, de_up_ce, MyCE);
                  MyModel.add(MyCE, ce_with_down_de, MyAnd);

                  StmtIterator AndIt = NextElement.listProperties(de_down_ce);
                  Statement Connection_Element = null;
                  while(AndIt.hasNext())
                  {
                     Connection_Element = AndIt.nextStatement();
                     Resource RCE = Service_Providers.getResource(Connection_Element.getObject().toString());
                     //System.out.println("\n\t\t\t New connection element (AND) : " + Connection_Element.getProperty(e3_has_uid).getObject().toString());
                     Resource NewCE = Generate_Connection_Element(MyModel);
                     MyModel.add(MyAnd, de_down_ce, NewCE);
                     MyModel.add(NewCE, ce_with_up_de, MyAnd);

                     //System.out.println("\n\t\t\t\t\t MyAnd (3) : " + MyAnd.toString());
                     //System.out.println("\n\t\t\t\t\t Next Resource (3) : " + RCE.toString());

                     Copy_Activities_in_Flow(MyModel, MyActor, NewCE, Current_Activity, Service_Providers, SecondActor, RCE);
                  }
               }// END --- AND
               else
               {
                  if(MyType.equals(OR))
                  {
                     //System.out.println("\n\t OR connection");
                  }
                  else
                  {
                     if(MyType.equals(ES))
                     {
                        //System.out.println("\n\t End Stimulus");
                        Resource MyES = Generate_End_Stimulus(MyModel);
                        MyModel.add(MyES, de_up_ce, MyCE);
                        MyModel.add(MyCE, ce_with_down_de, MyES);
                     }
                     else
                     {
                        //System.out.println("\n\t BIG ERROR ");
                        System.exit(1);
                     }
                  }
               }
            }

            // TMP
            //Resource MyAnd = Generate_e3value_AND(MyModel);
            //MyModel.add(MyAnd, de_down_ce, MyCE);
            //MyModel.add(MyCE, ce_with_down_de, MyAnd);
         }
      }
      return result;
   }

   public void Add_These_Attributes(Model MyModel2, Resource MyVI2, String IN, String OUT)
   {
      // Explore Offerings in the second value interface
      StmtIterator STIVOff2 = MyVI2.listProperties(vi_consists_of_of);
      Statement STVOff2    = null;
      while(STIVOff2.hasNext())
      {
         STVOff2 = STIVOff2.next();
         // Value Offering
         Resource MyVOff2 = MyModel2.getResource(STVOff2.getObject().toString());
         String Offer_Request2 = MyVOff2.getProperty(e3_has_name).getObject().toString();
         // Value Port
         Statement STVP2  = MyVOff2.getProperty(vo_consists_of_vp);
         Resource MyVPort2 = MyModel2.getResource(STVP2.getObject().toString());
         String VP_Dir2 = MyVPort2.getProperty(vp_has_dir).getObject().toString();
         // Value Object
         Statement STVObj2 = MyVPort2.getProperty(vp_requests_offers_vo);
         Resource MyVObj2 = MyModel2.getResource(STVObj2.getObject().toString());

         if(VP_Dir2.equals("true"))
         {
            MyVPort2.addProperty(e3_has_formula, OUT);
         }
         else
         {
            MyVPort2.addProperty(e3_has_formula, IN);
         }
      }// End STIVOff2
   }// END Add_These_Attributes

   public Resource Linkables(Model MyModel, Resource MyVI, Model Service_Providers, Resource Second_VI, boolean Match_Attributes)
   {
      Resource Result = null;
      int Num_Value_Offerings = 0;
      int Num_Value_Offerings_Matched = 0;
      // Explore each value offering in MyVI, if all of them can be covered by Second_VI, the the value interfaces are linkables.
      StmtIterator STIVOff1 = MyVI.listProperties(vi_consists_of_of);
      Statement STVOff1    = null;

      //System.out.println("\n Are " + MyVI.toString() + " and " + Second_VI.toString() + " linkables?");

      while(STIVOff1.hasNext())
      {
         STVOff1 = STIVOff1.next();
         // Value Offering
         Resource MyVOff1 = MyModel.getResource(STVOff1.getObject().toString());
         String Offer_Request1 = MyVOff1.getProperty(e3_has_name).getObject().toString();
         // Value Port
         Statement STVP1  = MyVOff1.getProperty(vo_consists_of_vp);
         Resource MyVPort1 = MyModel.getResource(STVP1.getObject().toString());
         String VP_Dir1 = MyVPort1.getProperty(vp_has_dir).getObject().toString();
         // Value Object
         Statement STVObj1 = MyVPort1.getProperty(vp_requests_offers_vo);
         Resource MyVObj1 = MyModel.getResource(STVObj1.getObject().toString());

         ////System.out.println("\n \t Looking for : " + MyVObj1.getProperty(e3_has_name).getObject().toString());

         // Explore Offerings in the second value interface
         StmtIterator STIVOff2 = Second_VI.listProperties(vi_consists_of_of);
         Statement STVOff2    = null;
         while(STIVOff2.hasNext())
         {
            STVOff2 = STIVOff2.next();
            // Value Offering
            Resource MyVOff2 = Service_Providers.getResource(STVOff2.getObject().toString());
            String Offer_Request2 = MyVOff2.getProperty(e3_has_name).getObject().toString();
            // Value Port
            Statement STVP2  = MyVOff2.getProperty(vo_consists_of_vp);
            Resource MyVPort2 = Service_Providers.getResource(STVP2.getObject().toString());
            String VP_Dir2 = MyVPort2.getProperty(vp_has_dir).getObject().toString();
            // Value Object
            Statement STVObj2 = MyVPort2.getProperty(vp_requests_offers_vo);
            Resource MyVObj2 = Service_Providers.getResource(STVObj2.getObject().toString());

            String VObj1 = MyVObj1.getProperty(e3_has_name).getObject().toString().trim();
            String VObj2 = MyVObj2.getProperty(e3_has_name).getObject().toString().trim();
            
            //System.out.print("\n \t \t This is : " + VObj1);
            //System.out.println("\t and this is : " + VObj2);
            //System.out.println("\n \t \t VP1 " + VP_Dir1 + " VP2 " + VP_Dir2);


            // Same Value Objects ??
            if(MyVObj1.getProperty(e3_has_name).getObject().toString().equals(MyVObj2.getProperty(e3_has_name).getObject().toString()))
            {
               //System.out.println("\n \t \t \t FOUND ");
               /*if( (Offer_Request1.equals("out")) && (VP_Dir1.equals("true")) )
               {
                  // What the interface offers
                  if((Offer_Request2.equals("in")) && (VP_Dir2.equals("false")))
                  {
                     Num_Value_Offerings_Matched++;
                  }
               }
               else
               {
                  // What the interface requests
                  if( (Offer_Request1.equals("in")) && (VP_Dir1.equals("false")) )
                  {
                     // What the interface offers
                     if((Offer_Request2.equals("out")) && (VP_Dir2.equals("true")))
                     {
                        Num_Value_Offerings_Matched++;
                     }
                  }
               }*/
               if(!VP_Dir1.equals(VP_Dir2))
                  Num_Value_Offerings_Matched++;
            }
         }
         Num_Value_Offerings++;
      }
      if(Num_Value_Offerings_Matched == Num_Value_Offerings)
      {
         // Matched
         //System.out.println("\n Value Interfaces can be connected");
         //System.out.println("\t VI (1) : " + MyVI.toString());
         //System.out.println("\t VI (2) : " + Second_VI.toString());
         //System.out.println("\n Next Step to evaluate attributes 1 ... ");

         // When having attributes we have to do this checking, for now we can avoid it
         //System.out.println("\n When having attributes we have to do a proper checking, for now we can avoid it");
         Result = Second_VI;
         
         /*if(Match_Attributes)
         {
            Resource MyVII = null;
            MyVII = Evaluate_Attributes(MyModel, MyVI, Service_Providers, Second_VI);
            if(MyVII != null)
            {
               Result = MyVII;
            }
         }
         else
         {
            Result = null;
         }*/
      }
      else
      {
         //System.out.println("\n Value Interfaces can not be connected " + Num_Value_Offerings + " vs " + Num_Value_Offerings_Matched);
      }

      return Result;
   }// END Linkables

   public boolean Internally_Linkables(Model MyModel, Resource MyVI, Model Service_Providers, Resource Second_VI, boolean Match_Attributes)
   {
      boolean Result = false;
      int Num_Value_Offerings = 0;
      int Num_Value_Offerings_Matched = 0;
      // Explore each value offering in MyVI, if all of them can be covered by Second_VI, the the value interfaces are linkables.
      StmtIterator STIVOff1 = MyVI.listProperties(vi_consists_of_of);
      Statement STVOff1    = null;

      ////System.out.println("\n Are " + MyVI.toString() + " and " + Second_VI.toString() + " linkables?");

      while(STIVOff1.hasNext())
      {
         STVOff1 = STIVOff1.next();
         // Value Offering
         Resource MyVOff1 = MyModel.getResource(STVOff1.getObject().toString());
         String Offer_Request1 = MyVOff1.getProperty(e3_has_name).getObject().toString();
         // Value Port
         Statement STVP1  = MyVOff1.getProperty(vo_consists_of_vp);
         Resource MyVPort1 = MyModel.getResource(STVP1.getObject().toString());
         String VP_Dir1 = MyVPort1.getProperty(vp_has_dir).getObject().toString();
         // Value Object
         Statement STVObj1 = MyVPort1.getProperty(vp_requests_offers_vo);
         Resource MyVObj1 = MyModel.getResource(STVObj1.getObject().toString());

         ////System.out.println("\n \t Looking for : " + MyVObj1.getProperty(e3_has_name).getObject().toString());

         // Explore Offerings in the second value interface
         StmtIterator STIVOff2 = Second_VI.listProperties(vi_consists_of_of);
         Statement STVOff2    = null;
         while(STIVOff2.hasNext())
         {
            STVOff2 = STIVOff2.next();
            // Value Offering
            Resource MyVOff2 = Service_Providers.getResource(STVOff2.getObject().toString());
            String Offer_Request2 = MyVOff2.getProperty(e3_has_name).getObject().toString();
            // Value Port
            Statement STVP2  = MyVOff2.getProperty(vo_consists_of_vp);
            Resource MyVPort2 = Service_Providers.getResource(STVP2.getObject().toString());
            String VP_Dir2 = MyVPort2.getProperty(vp_has_dir).getObject().toString();
            // Value Object
            Statement STVObj2 = MyVPort2.getProperty(vp_requests_offers_vo);
            Resource MyVObj2 = Service_Providers.getResource(STVObj2.getObject().toString());

            ////System.out.println("\n \t \t This is : " + MyVObj2.getProperty(e3_has_name).getObject().toString());
            // Same Value Objects ??
            if(MyVObj1.getProperty(e3_has_name).getObject().toString().equals(MyVObj2.getProperty(e3_has_name).getObject().toString()))
            {
               ////System.out.println("\n \t \t \t FOUND ");
               if( (Offer_Request1.equals("out")) && (VP_Dir1.equals("true")) )
               {
                  // What the interface offers
                  if((Offer_Request2.equals("out")) && (VP_Dir2.equals("true")))
                  {
                     Num_Value_Offerings_Matched++;
                  }
               }
               else
               {
                  // What the interface requests
                  if( (Offer_Request1.equals("in")) && (VP_Dir1.equals("false")) )
                  {
                     // What the interface offers
                     if((Offer_Request2.equals("in")) && (VP_Dir2.equals("false")))
                     {
                        Num_Value_Offerings_Matched++;
                     }
                  }
               }
            }
         }
         Num_Value_Offerings++;
      }
      if(Num_Value_Offerings_Matched == Num_Value_Offerings)
      {
         // Matched
         //System.out.println("\n Value Interfaces might be connected : ");
         //System.out.println("\t VI (1) : " + MyVI.toString());
         //System.out.println("\t VI (2) : " + Second_VI.toString());
         //System.out.println("\n Next Step to evaluate attributes 2 ... ");
         if(Match_Attributes)
         {
            Resource MyVII = null;
            MyVII = Evaluate_Attributes(MyModel, MyVI, Service_Providers, Second_VI);
            if(MyVII != null)
            {
               Result = true;
            }
         }
         else
         {
            Result = true;
         }
      }
      else
      {
         ////System.out.println("\n Value Interfaces can not be connected " + Num_Value_Offerings + " vs " + Num_Value_Offerings_Matched);
      }

      return Result;
   }// END    Internally_Linkables

   public Resource Evaluate_Attributes(Model MyModel, Resource MyVI,
                             Model Service_Providers, Resource Second_VI)
   {
      Resource result = null;
      // Evaluating Attributes
      //System.out.println("\n\t\t Evaluating Attributes");
      StmtIterator ITMyVI = MyVI.listProperties(vi_has_vii);
      Statement   STMyVI = null;
      if(ITMyVI.hasNext()) // Usually is only one vii
      {
         STMyVI = ITMyVI.next();
         // Value Interface Instance in MyModel
         Resource MyVII = MyModel.getResource(STMyVI.getObject().toString());
         // Value Interface Instacens in Service_Providers
         StmtIterator ITSP_VII = Second_VI.listProperties(vi_has_vii);
         Statement STSP_VII = null;
         Resource SP_VII = null;
         while( (ITSP_VII.hasNext()) && (result == null) )
         {
            STSP_VII = ITSP_VII.next();
            SP_VII = Service_Providers.getResource(STSP_VII.getObject().toString());
            result = Compare_Value_Interface_Instaces(MyModel, MyVII, Service_Providers, SP_VII);
         }
      }
      return result;
   }

   public Resource Compare_Value_Interface_Instaces(Model MyModel, Resource MyVII, Model Service_Providers, Resource SP_VII)
   {
      Resource Result = null;
      // Evaluating Value Ports
      //System.out.println("\n\t\t\t Evaluating Value Ports ");

      int Num_Value_Ports = 0;
      int Num_Value_Ports_Matched = 0;
      // Explore each value offering in MyVI, if all of them can be covered by Second_VI, the the value interfaces are linkables.
      StmtIterator STIVPorts1 = MyVII.listProperties(vii_has_vpi);
      Statement VPorts1    = null;

      ////System.out.println("\n Are " + MyVI.toString() + " and " + Second_VI.toString() + " linkables?");

      while(STIVPorts1.hasNext())
      {
         VPorts1 = STIVPorts1.next();
         Resource MyVPort1 = MyModel.getResource(VPorts1.getObject().toString());
         String VP_Dir1 = MyVPort1.getProperty(vpi_has_dir).getObject().toString();

         // Explore Value Ports in the second value interface
         StmtIterator STIVPorts2 = SP_VII.listProperties(vii_has_vpi);
         Statement VPorts2    = null;
         while(STIVPorts2.hasNext())
         {
            VPorts2 = STIVPorts2.next();
            Resource MyVPort2 = Service_Providers.getResource(VPorts2.getObject().toString());
            String VP_Dir2 = MyVPort2.getProperty(vpi_has_dir).getObject().toString();

            // Same Attributes ??
            if(VP_Dir1.equals("true"))
            {
               if(VP_Dir2.equals("false"))
               {
                  if(Same_Attributes(MyModel, MyVPort1, Service_Providers, MyVPort2))
                     Num_Value_Ports_Matched++;
               }
            }
            else
            {
               if(VP_Dir2.equals("true"))
               {
                  if(Same_Attributes(MyModel, MyVPort1, Service_Providers, MyVPort2))
                     Num_Value_Ports_Matched++;
               }
            }
         }
         Num_Value_Ports++;
      }
      // ===========================
      if(Num_Value_Ports == Num_Value_Ports_Matched)
      {
         Result = SP_VII;
      }
      return Result;
   }

   public boolean Same_Attributes(Model MyModel, Resource MyVPort1, Model Service_Providers, Resource MyVPort2)
   {
      int Num_Attributes = 0;
      int Num_Attributes_Matched = 0;
      boolean result = false;
      //System.out.println("\n\t\t\t\t Evaluating Each Value Attribute ");
      StmtIterator ITVP1 = MyVPort1.listProperties(vpi_has_vai);
      Statement   STVP1 = null;
      while(ITVP1.hasNext())
      {
         STVP1 = ITVP1.next();
         Resource VAtt1 = MyModel.getResource(STVP1.getObject().toString());
         String At1Name = VAtt1.getProperty(e3_has_name).getObject().toString();
         String At1Value = VAtt1.getProperty(e3_has_value).getObject().toString();
         StmtIterator ITVP2 = MyVPort2.listProperties(vpi_has_vai);
         Statement   STVP2 = null;
         while(ITVP2.hasNext())
         {
            STVP2 = ITVP2.next();
            Resource VAtt2 = Service_Providers.getResource(STVP2.getObject().toString());
            String At2Name = VAtt2.getProperty(e3_has_name).getObject().toString();
            String At2Value = VAtt2.getProperty(e3_has_value).getObject().toString();
            if(At1Name.equals(At2Name))
            {
               if(At1Name.equals("ISRC"))
               {
                  //System.out.print("\n\t\t\t\t\t ISRC " + At1Value + " - " + At2Value + "  ?");
                  if(At1Value.equals(At2Value))
                  {
                     Num_Attributes_Matched++;
                     //System.out.print(" YES ");
                  }
                  else
                  {
                     //System.out.print(" NO ");
                  }
                  //System.out.println(" |");
               }
               else
               {
                  //System.out.println("\n\t\t\t\t\t OTHER : " + At1Name + " = " + At1Value + " - " + At2Value + " this does not matter ");
                  Num_Attributes_Matched++;
               }
            }
         }
         Num_Attributes++;
      }
      if(Num_Attributes == Num_Attributes_Matched)
      {
         result = true;
      }
      return result;
   }

   public void Copy_Value_Interfaces(Model ThisModel, Resource MyResource, Model MyModel, Resource MyVI, boolean Inverse)
   {
      // Copying from MyResource to MyVI
      Vector<Resource> VThisVI = new Vector<Resource>();
      VThisVI.add(MyResource);
      Vector<Resource> TheseOffs = new Vector<Resource>();
      RDF_Utilities TmpObj = new RDF_Utilities(false);
      TheseOffs = TmpObj.getRelatedObjects(VThisVI, ThisModel, "http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of");
      
      for(int i = 0; i < TheseOffs.size(); i++)
      {
      //while(STITOff.hasNext())
      //{
         //STOff = STITOff.next();
         //Resource VOff = ThisModel.getResource(STOff.getObject().toString());
          Resource VOff = TheseOffs.get(i);
          
         // Generate Offering
         Resource MyVOffering = null;
         if(Inverse)
         {
            if(VOff.getProperty(e3_has_name).getObject().toString().equals("in"))
               MyVOffering = Generate_Value_Offering(MyModel,"out");
            else
               MyVOffering = Generate_Value_Offering(MyModel,"in");
         }
         else
         {
             String ThisName = VOff.getProperty(e3_has_name).getObject().toString();
             MyVOffering = Generate_Value_Offering(MyModel,ThisName);
         }
         // Link Offering with interface
         MyModel.add(MyVI, vi_consists_of_of, MyVOffering);
         MyModel.add(MyVOffering, vo_in_vi, MyVI);

         Vector<Resource> VThisOff = new Vector<Resource>();
         VThisOff.add(VOff);
         Vector<Resource> TheseVPs = new Vector<Resource>();
         RDF_Utilities TmpObj2 = new RDF_Utilities(false);
         TheseVPs = TmpObj2.getRelatedObjects(VThisOff, ThisModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp");

         for(int j = 0; j < TheseVPs.size(); j++)
         {
         //while(STITVP.hasNext())
         //{
            //STVP = STITVP.next();
            //Resource VPort = ThisModel.getResource(STVP.getObject().toString());
             Resource VPort = TheseVPs.get(j);
            // Generate Value Port
            Resource MyValuePort = Generate_Value_Port(MyModel);
            if(Inverse)
            {
               if(VPort.getProperty(vp_has_dir).getObject().toString().equals("true"))
                  MyValuePort.addProperty(vp_has_dir, "false");
               else
                  MyValuePort.addProperty(vp_has_dir, "true");
            }
            else
               MyValuePort.addProperty(vp_has_dir, VPort.getProperty(vp_has_dir).getObject().toString());           

            // Link value port to offering
            MyModel.add(MyVOffering, vo_consists_of_vp, MyValuePort);
            MyModel.add(MyValuePort, vp_in_vo, MyVOffering);

            Resource Value_Object = ThisModel.getResource(VPort.getProperty(vp_requests_offers_vo).getObject().toString());
            // Generate Value Object
            Resource MyValueObject = Generate_Value_Object(MyModel,Value_Object.getProperty(e3_has_name).getObject().toString(),false,"");
            // Link Value Object with Value Port
            MyModel.add(MyValueObject, vo_offered_requested_by_vp, MyValuePort);
            MyModel.add(MyValuePort, vp_requests_offers_vo, MyValueObject);

            // Extension to copy functional consequences
            StmtIterator STITFC = Value_Object.listProperties(resource_has_consequence);
            Statement STFC = null;
            while(STITFC.hasNext())
            {
               STFC = STITFC.nextStatement();
               Resource TmpFC = ThisModel.getResource(STFC.getObject().toString());
               // Generate Functional Consequence
               String FC_Name = TmpFC.getProperty(e3_has_name).getObject().toString();
               Resource MyFC = Generate_Functional_Consequence(MyModel, FC_Name);
               // Link Functional Consequence to Value Object
               MyModel.add(MyValueObject, resource_has_consequence, MyFC);
               MyModel.add(MyFC, consequence_has_resource, MyValueObject);


               if(TmpFC.hasProperty(depends_on))
               {
                   // Copy Quality Consequences
                   Vector<Resource> VTmpFC = new Vector<Resource>();
                   VTmpFC.add(TmpFC);
                   Vector<Resource> TheseQCs = TmpObj2.getRelatedObjects(VTmpFC, ThisModel, "http://www.cs.vu.nl/~gordijn/e3value#depends_on");

                   for(int k = 0; k < TheseQCs.size(); k++)
                   {
                       Resource ThisQC = TheseQCs.get(k);
                       Resource MyQC = Generate_Quality_Consequence(MyModel, ThisQC.getProperty(e3_has_uid).getObject().toString(),
                               ThisQC.getProperty(e3_has_name).getObject().toString());

                       MyModel.add(MyFC, depends_on, MyQC);

                       // Scale ?? -- we assume it's a nominal scale
                       if(ThisQC.hasProperty(has_scale))
                       {
                           Resource ThisScale = ThisModel.getResource(ThisQC.getProperty(has_scale).getObject().toString());
                           Resource MyScale = Generate_Nominal_Scale(MyModel, ThisScale.getProperty(e3_has_uid).getObject().toString(),
                                   ThisScale.getProperty(e3_has_name).getObject().toString());
                           MyModel.add(MyQC, has_scale, MyScale);
                       }
                   }
               }

            }
         }
      }
   }

   public Resource Copy_Value_Interface_Instances(Model Current_Model, Resource Current_Resource, Model MyModel, Resource MyVI, boolean Everything, Resource ThisVII)
   {
      // Copying from MyResource to MyVI
      Resource MyVII = null;

      if(Everything)
      {
         StmtIterator ITVII = Current_Resource.listProperties(vi_has_vii);
         Statement STVII = null;
         if(ITVII.hasNext())
         {
            //System.out.println("\t Copying Value Interface Instances ");
            while(ITVII.hasNext())
            {
               STVII = ITVII.next();
               Resource VIIns = Current_Model.getResource(STVII.getObject().toString());
               //System.out.println("\t \t Value Interface Instance : " + VIIns.toString());

               // Create Value Interface Instance
               MyVII = Generate_Value_Interface_Ins(MyModel, VIIns.getProperty(e3_has_name).getObject().toString() + "-" + generateID());
               MyModel.add(MyVI, vi_has_vii, MyVII);
               MyModel.add(MyVII, vii_of_vi, MyVI);
               // Explore Value Port Instances
               StmtIterator ITVP = VIIns.listProperties(vii_has_vpi);
               Statement   STVP = null;
               while(ITVP.hasNext())
               {
                  STVP = ITVP.next();
                  Resource VPIns = Current_Model.getResource(STVP.getObject().toString());
                  //System.out.println("\t \t \t Value Port Instance : " + VPIns.toString());
                  // Generate Value Port Instance
                  Resource MyVPI = Generate_Value_Port_Ins(MyModel, VPIns.getProperty(e3_has_name).getObject().toString() + "-" + generateID());
                  MyVPI.addProperty(vpi_has_dir, VPIns.getProperty(vpi_has_dir).getObject());
                  MyModel.add(MyVII, vii_has_vpi, MyVPI);
                  MyModel.add(MyVPI, vpi_of_vii, MyVII);
                  // Explore Value Attribute Instances
                  StmtIterator ITVAtt = VPIns.listProperties(vpi_has_vai);
                  Statement   STVAtt = null;
                  while(ITVAtt.hasNext())
                  {
                     STVAtt = ITVAtt.next();
                     Resource VAttIns = Current_Model.getResource(STVAtt.getObject().toString());
                     //System.out.println("\t \t \t \t Value Attribute Instance : " + VAttIns.toString());
                     // Generate Value Attribute Instance
                     Resource MyVAI = Generate_Value_Attribute_Ins(MyModel, VAttIns.getProperty(e3_has_uid).getObject().toString() + "-" + generateID());
                     MyVAI.addProperty(e3_has_name, VAttIns.getProperty(e3_has_name).getObject().toString());
                     MyVAI.addProperty(e3_has_value, VAttIns.getProperty(e3_has_value).getObject().toString());
                     MyModel.add(MyVPI, vpi_has_vai, MyVAI);
                     MyModel.add(MyVAI, vai_of_vpi, MyVPI);
                  }
               }
            }
         }
      } // END EVERYTHING
      else
      {
         // =================================
         //System.out.println(" Copy a Specific Value Interface Instance ");
         // Create Value Interface Instance
         MyVII = Generate_Value_Interface_Ins(MyModel, ThisVII.getProperty(e3_has_name).getObject().toString() + "-" + generateID());
         MyModel.add(MyVI, vi_has_vii, MyVII);
         MyModel.add(MyVII, vii_of_vi, MyVI);
         // Explore Value Port Instances
         StmtIterator ITVP = ThisVII.listProperties(vii_has_vpi);
         Statement   STVP = null;
         while(ITVP.hasNext())
         {
            STVP = ITVP.next();
            Resource VPIns = Current_Model.getResource(STVP.getObject().toString());
            //System.out.println("\t \t \t Value Port Instance : " + VPIns.toString());
            // Generate Value Port Instance
            Resource MyVPI = Generate_Value_Port_Ins(MyModel, VPIns.getProperty(e3_has_name).getObject().toString() + "-" + generateID());
            MyVPI.addProperty(vpi_has_dir, VPIns.getProperty(vpi_has_dir).getObject());
            MyModel.add(MyVII, vii_has_vpi, MyVPI);
            MyModel.add(MyVPI, vpi_of_vii, MyVII);
            // Explore Value Attribute Instances
            StmtIterator ITVAtt = VPIns.listProperties(vpi_has_vai);
            Statement   STVAtt = null;
            while(ITVAtt.hasNext())
            {
               STVAtt = ITVAtt.next();
               Resource VAttIns = Current_Model.getResource(STVAtt.getObject().toString());
               //System.out.println("\t \t \t \t Value Attribute Instance : " + VAttIns.toString());
               // Generate Value Attribute Instance
               Resource MyVAI = Generate_Value_Attribute_Ins(MyModel, VAttIns.getProperty(e3_has_uid).getObject().toString() + "-" + generateID());
               MyVAI.addProperty(e3_has_name, VAttIns.getProperty(e3_has_name).getObject().toString());
               MyVAI.addProperty(e3_has_value, VAttIns.getProperty(e3_has_value).getObject().toString());

               MyModel.add(MyVPI, vpi_has_vai, MyVAI);
               MyModel.add(MyVAI, vai_of_vpi, MyVPI);
            }
         }
         // Copy Dependencies
         StmtIterator ITDependencies = ThisVII.listProperties(depends_on);
         Statement   STDependencies = null;
         while(ITDependencies.hasNext())
         {
            STDependencies = ITDependencies.next();
            MyVII.addProperty(de_down_ce, STDependencies.getObject().toString());
         }

         // ================================
      }// END ELSE
      return MyVII;
   }

   public Resource Copy_Value_Activity(Resource Value_Activity, Model MyModel)
   {
      if(Value_Activity == null)
      {
         //System.out.println("Error Copying Value Activity");
      }
      String Old_Name = Value_Activity.getProperty(e3_has_name).getObject().toString();
      String New_Name = new String(Old_Name);
      Value_Activity.removeAll(e3_has_name);
      Value_Activity.addProperty(e3_has_name, New_Name);

      //System.out.println("\n Copying Value Activity : " + Old_Name + " with URI : " + Value_Activity.getURI().toString() +  " and UID : " + Value_Activity.getProperty(e3_has_uid).getObject().toString());

      // Create a copy of this Value Activity
      Resource MyNewActivity = Generate_Value_Activity(MyModel, Old_Name);

      // Remove e3_has_formula
      MyNewActivity.removeAll(e3_has_formula);
      MyNewActivity.addProperty(e3_has_formula, Value_Activity.getProperty(e3_has_formula).getObject().toString());

      // Copy all its interfaces
      Vector<Resource> TmpActivity = new Vector<Resource>();
      TmpActivity.add(Value_Activity);
      Vector<Resource> VInterfaces = new Vector<Resource>();
      RDF_Utilities TmpObj = new RDF_Utilities(false);
      VInterfaces = TmpObj.getRelatedObjects(TmpActivity, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#va_has_vi");
      //System.out.print("\n Value Activity : " + Value_Activity.getProperty(e3_has_name).getObject().toString());
      //System.out.println("\t has " + VInterfaces.size() + " interfaces ");
      // Resource to link interfaces.
      Resource MyCE = null;
      Resource ThisUP = null;
      Resource ThisDOWN = null;

      for(int index = 0; index < VInterfaces.size(); index++)
      {
         // Get Value Interface in actor
         Resource MyVI = VInterfaces.get(index);
         // Generate Value Interface in EA
         Resource EA_VI = Generate_Value_Interface(MyModel);
         Copy_Value_Interfaces(MyModel, MyVI, MyModel, EA_VI, false);
         MyModel.add(MyNewActivity, va_has_vi, EA_VI);
         MyModel.add(EA_VI, vi_assigned_to_va, MyNewActivity);
         // Connections?
         if(MyVI.hasProperty(B2B.de_down_ce))
         {
             //System.out.println("\n we should link with CE down");
             ThisUP = EA_VI;
         }
         if(MyVI.hasProperty(B2B.de_up_ce))
         {
             //System.out.println("\n we should link with CE up");
             ThisDOWN = EA_VI;
         }
         if( (ThisUP != null) && (ThisDOWN != null))
         {
             MyCE = Generate_Connection_Element(MyModel);
             MyModel.add(ThisUP, de_down_ce, MyCE);
             MyModel.add(ThisDOWN, de_up_ce, MyCE);
             MyModel.add(MyCE, ce_with_up_de, ThisUP);
             MyModel.add(MyCE, ce_with_down_de, ThisDOWN);
             /*MyCE = null;
             ThisUP = null;
             ThisDOWN = null;*/
         }
      }
      
      return MyNewActivity;
   }

   public Resource Generate_Value_Interface_Ins(Model MyModel, String Name)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      MyResource = Look_for_Resource(VII,e3_has_name,Name,MyModel);

      if(MyResource == null)
      {
         MyResource = MyModel.createResource(IName + Name);
         MyResource.addProperty(e3_has_name,Name);

         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);

         MyResource.addProperty(e3_has_uid, ""+ Name);
         Resource X     = MyModel.createResource(e3value + "value_interface_ins");
         MyModel.add(MyResource, RDF.type, X);
      }
      else
      {
         //System.out.print(" VII already in the model ");
      }
      return MyResource;
   }// End Generate_Value_Interface_Ins

   public Resource Generate_Value_Port_Ins(Model MyModel, String Name)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      MyResource = MyModel.createResource(IName + Name);
      MyResource.addProperty(e3_has_name, Name);

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, Name);
      Resource X     = MyModel.createResource(e3value + "value_port_ins");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }// End Value Port Instance

   public Resource Generate_Value_Attribute_Ins(Model MyModel, String Name)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;
      MyResource = MyModel.createResource(IName + Name);
      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);
      MyResource.addProperty(e3_has_uid, Name);
      Resource X     = MyModel.createResource(e3value + "value_attribute_ins");
      MyModel.add(MyResource, RDF.type, X);
      return MyResource;
   }

   public void remove_this_Property(Model MyModel, Resource FromThisR, Property ThisProperty, Resource ThisResource)
   {
      //Resource MyAuxResource = FromThisR; // Backup resource
      StmtIterator STIAuxResource = FromThisR.listProperties(ThisProperty);
      Statement StAuxResource = null;
      Vector<Resource> OLD = new Vector<Resource>();
      while(STIAuxResource.hasNext())
      {
         StAuxResource = STIAuxResource.nextStatement();
         Resource MyAuxResource2 = MyModel.getResource(StAuxResource.getObject().toString());
         // Copy properties that do not point to ThisResource
         String ThisRID       = ThisResource.getProperty(e3_has_uid).getObject().toString();
         String MyAuxResource2ID = MyAuxResource2.getProperty(e3_has_uid).getObject().toString();

         if(!ThisRID.equals(MyAuxResource2ID))
         {
            OLD.add(MyAuxResource2);
         }
      }
      FromThisR.removeAll(ThisProperty);  // Remove all the properties
      // Copy properties that do not point to ThisResource
      for(int index = 0; index < OLD.size(); index++)
      {
         Resource MyAuxResource2 = OLD.get(index);
         MyModel.add(FromThisR, ThisProperty, MyAuxResource2);
      }
   }

   public boolean Compare_Value_Interfaces(Resource MyVI1, Resource MyVI2, Model MyModel)
   {
      boolean Same_Objects = false;
      List<String> VI1 = new ArrayList<String>();
      List<String> VI2 = new ArrayList<String>();

      StmtIterator ITVI1 = MyVI1.listProperties(vi_consists_of_of);
      Statement AuxIT1 = null;
      while(ITVI1.hasNext())
      {
         AuxIT1 = ITVI1.nextStatement();
         Resource AuxVO1 = MyModel.getResource(AuxIT1.getObject().toString());
         StmtIterator AuxITVO1 = AuxVO1.listProperties(vo_consists_of_vp);
         Statement StAuxITVO1 = null;
         while(AuxITVO1.hasNext())
         {
            StAuxITVO1 = AuxITVO1.nextStatement();
            Resource AuxVP1 = MyModel.getResource(StAuxITVO1.getObject().toString());
            Resource AuxVOb1 = MyModel.getResource(AuxVP1.getProperty(vp_requests_offers_vo).getObject().toString());
            //System.out.println("    Adding : " + AuxVOb1.getProperty(e3_has_name).getObject().toString());
            VI1.add(AuxVOb1.getProperty(e3_has_name).getObject().toString());
         }
      }

      ITVI1 = MyVI2.listProperties(vi_consists_of_of);
      AuxIT1 = null;
      while(ITVI1.hasNext())
      {
         AuxIT1 = ITVI1.nextStatement();
         Resource AuxVO1 = MyModel.getResource(AuxIT1.getObject().toString());
         StmtIterator AuxITVO1 = AuxVO1.listProperties(vo_consists_of_vp);
         Statement StAuxITVO1 = null;
         while(AuxITVO1.hasNext())
         {
            StAuxITVO1 = AuxITVO1.nextStatement();
            Resource AuxVP1 = MyModel.getResource(StAuxITVO1.getObject().toString());
            Resource AuxVOb1 = MyModel.getResource(AuxVP1.getProperty(vp_requests_offers_vo).getObject().toString());
            //System.out.println("    Now : " + AuxVOb1.getProperty(e3_has_name).getObject().toString());
            VI2.add(AuxVOb1.getProperty(e3_has_name).getObject().toString());
         }
      }
      Same_Objects = VI1.containsAll(VI2);
      //System.out.println(" Do they have the same value objects ?" + Same_Objects);
      return Same_Objects;
   }

   public void Link_Value_Interfaces(Model MyModel, Resource New_Value_Interface, Resource MyValue_Interface)
   {
      ////System.out.println("\t  @@@@ Trying to link value interfaces ... ");
      // Linking from actor to activity
      // MyVI -> vi_consists_of_of -> vo_consists_of_vp -> MyVP1
      //
      StmtIterator IteratorOF = MyValue_Interface.listProperties(vi_consists_of_of);
      Statement AuxOF = null;
      while(IteratorOF.hasNext())
      {
         AuxOF = IteratorOF.nextStatement();
         Resource Value_Offering1 = MyModel.getResource(AuxOF.getObject().toString());
         Resource Value_Port1 = MyModel.getResource(Value_Offering1.getProperty(vo_consists_of_vp).getObject().toString());
         String DIR = Value_Port1.getProperty(vp_has_dir).getObject().toString();
         ////System.out.println("\t\t  &&&&& Getting value port ... " + Value_Port1.toString() + " dir " + DIR);
         if(DIR.equals("true"))
         {
            ////System.out.println("\t\t\t  ******** Linking output port");
            // + Generate VALUE_EXCHANGE (ID++)
            Resource Value_Exchange = Generate_Value_Exchange(MyModel);

            // + Generate VALUE_PORT (ID++)
            Resource New_Value_Port = Generate_Value_Port(MyModel);
            New_Value_Port.addProperty(vp_has_dir, "true");
            // + Generate VALUE_OFFERING (ID++)
            Resource New_Value_Offering = Generate_Value_Offering(MyModel,"out");


            // - Link value_offering with value_port
            MyModel.add(New_Value_Offering, vo_consists_of_vp, New_Value_Port);
            MyModel.add(New_Value_Port, vp_in_vo, New_Value_Offering);
            // - Link value_offering with FIRST value_interface
            MyModel.add(New_Value_Offering, vo_in_vi, New_Value_Interface);
            MyModel.add(New_Value_Interface, vi_consists_of_of, New_Value_Offering);
            // - Link value_port with value_exchange
            MyModel.add(Value_Exchange,ve_has_second_vp, Value_Port1);
            MyModel.add(Value_Exchange,ve_has_first_vp, New_Value_Port);
            MyModel.add(Value_Port1,vp_second_connects_ve, Value_Exchange);
            MyModel.add(New_Value_Port,vp_first_connects_ve, Value_Exchange);
         }
         else
         {
            ////System.out.println("\t\t\t  ******** Linking input port");
            // + Generate VALUE_EXCHANGE (ID++)
            Resource Value_Exchange = Generate_Value_Exchange(MyModel);
            // + Generate VALUE_PORT (ID++)
            Resource New_Value_Port = Generate_Value_Port(MyModel);
            New_Value_Port.addProperty(vp_has_dir, "false");

            //MyModel.add(Value_Port1, vp_requests_offers_vo, Value_Object1);
            //MyModel.add(Value_Object1, vo_offered_requested_by_vp, Value_Port1);

            // + Generate VALUE_OFFERING (ID++)
            Resource New_Value_Offering = Generate_Value_Offering(MyModel,"in");
            // - Link value_offering with value_port
            MyModel.add(New_Value_Offering, vo_consists_of_vp, New_Value_Port);
            MyModel.add(New_Value_Port, vp_in_vo, New_Value_Offering);
            // - Link value_offering with FIRST value_interface
            MyModel.add(New_Value_Offering, vo_in_vi, New_Value_Interface);
            MyModel.add(New_Value_Interface, vi_consists_of_of, New_Value_Offering);
            // - Link value_port with value_exchange
            MyModel.add(Value_Exchange,ve_has_first_vp, Value_Port1);
            MyModel.add(Value_Exchange,ve_has_second_vp, New_Value_Port);
            MyModel.add(Value_Port1,vp_first_connects_ve, Value_Exchange);
            MyModel.add(New_Value_Port,vp_second_connects_ve, Value_Exchange);

         }
      }
   }// END Link_Value_Interfaces

   public void Link_Value_Interfaces_2(Model MyModel, Resource MyValue_Interface, Resource New_Value_Interface)
   {
      ////System.out.println("\t  @@@@ Trying to link value interfaces 2 ... ");
      // Internal linking (inside actors) without neither generating new offering nor ports
      // From activity to actor
      // MyVI -> vi_consists_of_of -> vo_consists_of_vp -> MyVP1
      //
      StmtIterator IteratorOF = MyValue_Interface.listProperties(vi_consists_of_of);
      Statement AuxOF = null;

      if(!IteratorOF.hasNext())
      {
         //System.out.println("\n \n BIG MISTAKE !!!! THERE SHOULD BE OFFERINGS");
      }

      while(IteratorOF.hasNext())
      {
         AuxOF = IteratorOF.nextStatement();
         Resource Value_Offering1 = MyModel.getResource(AuxOF.getObject().toString());
         Resource Value_Port1 = MyModel.getResource(Value_Offering1.getProperty(vo_consists_of_vp).getObject().toString());
         String DIR = Value_Port1.getProperty(vp_has_dir).getObject().toString();
         ////System.out.println("\t\t  &&&&& Getting value port ... " + Value_Port1.toString() + " dir " + DIR);
         if(DIR.equals("true"))
         {
            ////System.out.println("\t\t\t  ******** Linking output port");
            // + Generate VALUE_EXCHANGE (ID++)
            Resource Value_Exchange = Generate_Value_Exchange(MyModel);
            // + Get VALUE_OFFERING
            Resource New_Value_Offering = Get_Value_Offering(MyModel, New_Value_Interface, "true");
            //Generate_Value_Offering(MyModel,"out");
            Statement StVPort = New_Value_Offering.getProperty(vo_consists_of_vp);
            // + Get VALUE_PORT with DIR == true
            Resource New_Value_Port = MyModel.getResource(StVPort.getObject().toString());
            if(New_Value_Port == null)
            {
               //System.out.println(" \n INVALID VALUE PORT 1 ");
            }
            // - Link value_offering with value_port
            MyModel.add(New_Value_Offering, vo_consists_of_vp, New_Value_Port);
            MyModel.add(New_Value_Port, vp_in_vo, New_Value_Offering);
            // - Link value_offering with FIRST value_interface
            MyModel.add(New_Value_Offering, vo_in_vi, New_Value_Interface);
            MyModel.add(New_Value_Interface, vi_consists_of_of, New_Value_Offering);
            // - Link value_port with value_exchange
            MyModel.add(Value_Exchange,ve_has_second_vp, Value_Port1);
            MyModel.add(Value_Exchange,ve_has_first_vp, New_Value_Port);
            MyModel.add(Value_Port1,vp_second_connects_ve, Value_Exchange);
            MyModel.add(New_Value_Port,vp_first_connects_ve, Value_Exchange);
         }
         else
         {
            ////System.out.println("\t\t\t  ******** Linking input port");
            // + Generate VALUE_EXCHANGE (ID++)
            Resource Value_Exchange = Generate_Value_Exchange(MyModel);
            // + Get VALUE_PORT with DIR == false
            //Resource New_Value_Port = Get_Value_Offering(MyModel, New_Value_Interface, "false");
            // + Generate VALUE_OFFERING (ID++)
            //Resource New_Value_Offering = Generate_Value_Offering(MyModel,"in");

            // + Get VALUE_OFFERING
            Resource New_Value_Offering = Get_Value_Offering(MyModel, New_Value_Interface, "false");
            //Generate_Value_Offering(MyModel,"in");
            Statement StVPort = New_Value_Offering.getProperty(vo_consists_of_vp);
            // + Get VALUE_PORT with DIR == false
            Resource New_Value_Port = MyModel.getResource(StVPort.getObject().toString());

            if(New_Value_Port == null)
            {
               //System.out.println(" \n INVALID VALUE PORT 2 ");
            }

            // - Link value_offering with value_port
            MyModel.add(New_Value_Offering, vo_consists_of_vp, New_Value_Port);
            MyModel.add(New_Value_Port, vp_in_vo, New_Value_Offering);
            // - Link value_offering with FIRST value_interface
            MyModel.add(New_Value_Offering, vo_in_vi, New_Value_Interface);
            MyModel.add(New_Value_Interface, vi_consists_of_of, New_Value_Offering);
            // - Link value_port with value_exchange
            MyModel.add(Value_Exchange,ve_has_first_vp, Value_Port1);
            MyModel.add(Value_Exchange,ve_has_second_vp, New_Value_Port);
            MyModel.add(Value_Port1,vp_first_connects_ve, Value_Exchange);
            MyModel.add(New_Value_Port,vp_second_connects_ve, Value_Exchange);
         }
      }
   }// END Link_Value_Interfaces_2

   public Resource Get_Value_Offering(Model MyModel, Resource MyValue_Interface, String Dir)
   {
      Resource MyVOffering = null;
      StmtIterator STITVOfferings = MyValue_Interface.listProperties(vi_consists_of_of);
      Statement StVOffering = null;
      while(STITVOfferings.hasNext())
      {
         StVOffering = STITVOfferings.nextStatement();
         MyVOffering = MyModel.getResource(StVOffering.getObject().toString());
         Statement StVPort = MyVOffering.getProperty(vo_consists_of_vp);
         Resource MyVPort = MyModel.getResource(StVPort.getObject().toString());
         Statement StDir = MyVPort.getProperty(vp_has_dir);
         if(StDir.getObject().toString().equals(Dir))
         {
            //MyVPort = MyModel.getResource(StVPort.getObject().toString());
            break;
         }
      }
      return MyVOffering;
   }

   public void Generate_Value_Exchanges(Model MyModel, Resource Value_Interface1, Resource Value_Interface2)
   {
      // ===========================================================================================
      // + Generate 2 (two) value_interface elements (RememberIDs, ID+=2)
      //Resource Value_Interface1 = Generate_Value_Interface(MyModel);
      //Resource Value_Interface2 = Generate_Value_Interface(MyModel);
      RDF_Utilities TmpObj = new RDF_Utilities(false);
      

      StmtIterator STITVOfferings = Value_Interface1.listProperties(vi_consists_of_of);
      Statement StVOffering = null;
      while(STITVOfferings.hasNext())
      {
         StVOffering = STITVOfferings.nextStatement();
         Resource MyVOffering = MyModel.getResource(StVOffering.getObject().toString());

         Vector<Resource> VMyVOf = new Vector<Resource>();
         VMyVOf.add(MyVOffering);
         Vector<Resource> TheseVPs = TmpObj.getRelatedObjects(VMyVOf, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp");
         for(int j = 0; j < TheseVPs.size(); j++)
         {
            Resource MyVPort   = TheseVPs.get(j);
            Statement StVObject = MyVPort.getProperty(vp_requests_offers_vo);
            Resource MyVObject = MyModel.getResource(StVObject.getObject().toString());

            // Link
            // + Generate VALUE_EXCHANGE (ID++)
            Resource Value_Exchange1 = Generate_Value_Exchange(MyModel);
            // + Generate VALUE_PORT (ID++)
            Resource Value_Port2 = Generate_Value_Port(MyModel);
            // - Link value_port with FIRST value_object
            MyModel.add(Value_Port2, vp_requests_offers_vo, MyVObject);
            MyModel.add(MyVObject, vo_offered_requested_by_vp, Value_Port2);

            if(MyVPort.getProperty(vp_has_dir).getObject().toString().equals("true"))
            {
                // + Generate VALUE_OFFERING (ID++)
                Resource Value_Offering2 = Generate_Value_Offering(MyModel,"in");
                // - Link value_offering with value_port
                MyModel.add(Value_Offering2, vo_consists_of_vp, Value_Port2);
                MyModel.add(Value_Port2, vp_in_vo, Value_Offering2);
                // - Link value_offering with SECOND value_interface
                MyModel.add(Value_Offering2, vo_in_vi, Value_Interface2);
                MyModel.add(Value_Interface2, vi_consists_of_of, Value_Offering2);
                Value_Port2.addProperty(vp_has_dir, "false");
                // - Link value_port with value_exchange
                MyModel.add(Value_Exchange1,ve_has_out_po, MyVPort);
                MyModel.add(MyVPort,vp_out_connects_ve, Value_Exchange1);
                // - Link value_port with value_exchange
                MyModel.add(Value_Exchange1, ve_has_in_po, Value_Port2);
                MyModel.add(Value_Port2, vp_in_connects_ve, Value_Exchange1);
            }
            else
            {
                // + Generate VALUE_OFFERING (ID++)
                Resource Value_Offering2 = Generate_Value_Offering(MyModel,"out");
                // - Link value_offering with value_port
                MyModel.add(Value_Offering2, vo_consists_of_vp, Value_Port2);
                MyModel.add(Value_Port2, vp_in_vo, Value_Offering2);
                // - Link value_offering with SECOND value_interface
                MyModel.add(Value_Offering2, vo_in_vi, Value_Interface2);
                MyModel.add(Value_Interface2, vi_consists_of_of, Value_Offering2);
                Value_Port2.addProperty(vp_has_dir, "true");
                // - Link value_port with value_exchange
                MyModel.add(Value_Exchange1,ve_has_in_po, MyVPort);
                MyModel.add(MyVPort,vp_in_connects_ve, Value_Exchange1);
                // - Link value_port with value_exchange
                MyModel.add(Value_Exchange1, ve_has_out_po, Value_Port2);
                MyModel.add(Value_Port2, vp_out_connects_ve, Value_Exchange1);
            }// End - else
         }// End - for
      }// END While
      // ===========================================================================================
   }// END Generate_Value_Exchanges

   public void Generate_Value_Exchanges_2(Model MyModel, Resource Value_Interface1, Resource Value_Interface2)
   {
      // ===========================================================================================
      //
      StmtIterator STITVOfferings = Value_Interface1.listProperties(vi_consists_of_of);
      Statement StVOffering = null;
      while(STITVOfferings.hasNext())
      {
         StVOffering = STITVOfferings.nextStatement();
         Resource MyVOffering = MyModel.getResource(StVOffering.getObject().toString());
         Statement StVPort = MyVOffering.getProperty(vo_consists_of_vp);
         Resource MyVPort   = MyModel.getResource(StVPort.getObject().toString());
         Statement StVObject = MyVPort.getProperty(vp_requests_offers_vo);
         //Resource MyVObject = MyModel.getResource(StVObject.getObject().toString());

         // Link
         // + Generate VALUE_EXCHANGE (ID++)
         Resource Value_Exchange1 = Generate_Value_Exchange(MyModel);

         if(MyVPort.getProperty(vp_has_dir).getObject().toString().equals("true"))
         {
            // + Generate VALUE_OFFERING (ID++)
            Resource Value_Offering2 = Get_Value_Offering(MyModel, Value_Interface2, "false");
            // + Generate VALUE_PORT (ID++)
            // Resource Value_Port2 = Generate_Value_Port(MyModel);
            Statement StVPort2 = Value_Offering2.getProperty(vo_consists_of_vp);
            // + Get VALUE_PORT with DIR == true
            Resource Value_Port2 = MyModel.getResource(StVPort2.getObject().toString());

            // - Link value_offering with value_port
            MyModel.add(Value_Offering2, vo_consists_of_vp, Value_Port2);
            MyModel.add(Value_Port2, vp_in_vo, Value_Offering2);
            // - Link value_offering with SECOND value_interface
            MyModel.add(Value_Offering2, vo_in_vi, Value_Interface2);
            MyModel.add(Value_Interface2, vi_consists_of_of, Value_Offering2);
            Value_Port2.addProperty(vp_has_dir, "false");
            // - Link value_port with value_exchange
            MyModel.add(Value_Exchange1,ve_has_out_po, MyVPort);
            MyModel.add(MyVPort,vp_out_connects_ve, Value_Exchange1);
            // - Link value_port with value_exchange
            MyModel.add(Value_Exchange1, ve_has_in_po, Value_Port2);
            MyModel.add(Value_Port2, vp_in_connects_ve, Value_Exchange1);
         }
         else
         {
            // + Generate VALUE_OFFERING (ID++)
            // Resource Value_Offering2 = Generate_Value_Offering(MyModel,"out");
            // + Generate VALUE_PORT (ID++)
            // Resource Value_Port2 = Generate_Value_Port(MyModel);

            // + Generate VALUE_OFFERING (ID++)
            Resource Value_Offering2 = Get_Value_Offering(MyModel, Value_Interface2, "true");
            // + Generate VALUE_PORT (ID++)
            // Resource Value_Port2 = Generate_Value_Port(MyModel);
            Statement StVPort2 = Value_Offering2.getProperty(vo_consists_of_vp);
            // + Get VALUE_PORT with DIR == false
            Resource Value_Port2 = MyModel.getResource(StVPort2.getObject().toString());

            // - Link value_offering with value_port
            MyModel.add(Value_Offering2, vo_consists_of_vp, Value_Port2);
            MyModel.add(Value_Port2, vp_in_vo, Value_Offering2);
            // - Link value_offering with SECOND value_interface
            MyModel.add(Value_Offering2, vo_in_vi, Value_Interface2);
            MyModel.add(Value_Interface2, vi_consists_of_of, Value_Offering2);
            Value_Port2.addProperty(vp_has_dir, "true");
            // - Link value_port with value_exchange
            MyModel.add(Value_Exchange1,ve_has_in_po, MyVPort);
            MyModel.add(MyVPort,vp_in_connects_ve, Value_Exchange1);
            // - Link value_port with value_exchange
            MyModel.add(Value_Exchange1, ve_has_out_po, Value_Port2);
            MyModel.add(Value_Port2, vp_out_connects_ve, Value_Exchange1);
         }

      }// END While
      // ===========================================================================================
   }// END Generate_Value_Exchanges_2

   public Resource Get_First_CE(Resource start_stimulus, Model Skeleton, Property ThisProperty)
   {
      Resource connection_element = null;
      Statement AuxSt     = null;
      // start_stimulus -> de_down_ce
      String AuxStr;

      AuxSt = start_stimulus.getProperty(ThisProperty); //de_down_ce
      ////System.out.println(" de_down_ce Connected to: " + AuxSt.getObject().toString());
      connection_element = Skeleton.getResource(AuxSt.getObject().toString());
      AuxStr = connection_element.getProperty(RDF.type).getObject().toString();
      ////System.out.println("   Resource : " + connection_element.toString() + " - type " + AuxStr);
      return connection_element;
   }

   public Resource Look_for_Resource(String ThisType, Property ThisProperty, String ThisValue, Model MyModel)
   {
      Resource  MyResource = null;
      Resource  AlreadyHere = null;
      ResIterator iter   = MyModel.listResourcesWithProperty(RDF.type);
      ////System.out.println(" Looking for resource " + ThisValue);
      if (iter.hasNext())
      {
         ////System.out.println(" The model contains at least this start_stimulus:");
         while (iter.hasNext())
         {
            ////System.out.println("  " + iter.next().getProperty(de_down_ce).getString());
            MyResource = iter.next();
            String RequestedType = MyResource.getProperty(RDF.type).getObject().toString();
            if(RequestedType.equals(ThisType))
            {
               String RequestedValue = MyResource.getProperty(ThisProperty).getObject().toString();
               ////System.out.println("   " + RequestedValue + " " + MyResource.toString());
               if(RequestedValue.equals(ThisValue))
               {
                  AlreadyHere = MyResource;
                  break;
               }
            }
         }
      }
      else
      {
         //System.out.println("The resource was not found in the model");
      }
      return AlreadyHere;
   }

   public Resource Generate_Elementary_Actor(Model MyModel, String ThisName, boolean Check_Instance)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      ////System.out.println(" Trying to create a new value activity : " + AuxSt.getObject().toString());

      if(Check_Instance)
        MyResource = Look_for_Resource(EA,e3_has_name,ThisName,MyModel);
      
      if(MyResource == null)
      {
         MyResource = MyModel.createResource(IName + generateID());
         MyResource.addProperty(e3_has_name, ThisName);

         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);

         //<a:el_performs_va rdf:resource="http://www.cs.vu.nl/~gordijn/BasicModel#4"/>
         //<a:ac_has_vi rdf:resource="http://www.cs.vu.nl/~gordijn/BasicModel#7"/>
         int ThisID = getID();
         MyResource.addProperty(e3_has_formula, "INVESTMENT=" + ThisID);
         //MyResource.addProperty(e3_has_formula, "EXPENSES=0");
         MyResource.addProperty(e3_has_uid, ""+ ThisID);
         Resource X     = MyModel.createResource(e3value + "elementary_actor");
         MyModel.add(MyResource, RDF.type, X);
         //Traverse = true;
         ////System.out.println("    YES New Elementary Actor Name : " + ThisName);
      }
      else
      {
         ////System.out.println("    NO there is already an elementary actor called : " + ThisName);
         Traverse = false;
      }
      return MyResource;
   }

   public Resource Generate_Composite_Actor(Model MyModel, String ThisName)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      ////System.out.println(" Trying to create a new Generate_Composite_Actor : " + AuxSt.getObject().toString());

      MyResource = Look_for_Resource(CA,e3_has_name,ThisName,MyModel);

      if(MyResource == null)
      {
         MyResource = MyModel.createResource(IName + generateID());
         MyResource.addProperty(e3_has_name, ThisName);

         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);

         MyResource.addProperty(e3_has_formula, "INVESTMENT=0");
         MyResource.addProperty(e3_has_formula, "EXPENSES=0");
         MyResource.addProperty(e3_has_uid, ""+ getID());
         Resource X     = MyModel.createResource(e3value + "composite_actor");
         MyModel.add(MyResource, RDF.type, X);
      }
      else
      {
         ////System.out.println("    NO there is already a composite actor called : " + ThisName);
      }
      return MyResource;
   }

   public Resource Generate_Value_Activity(Resource AuxVA, Model MyModel)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;
      AuxSt = AuxVA.getProperty(e3_has_name);
      //System.out.println(" Trying to create a new value activity 1 : " + AuxSt.getObject().toString());

      //MyResource = Look_for_Resource(VA,e3_has_name,AuxSt.getObject().toString(),MyModel);

      if(MyResource == null)
      {
         MyResource = MyModel.createResource(IName + generateID());
         MyResource.addProperty(e3_has_name, AuxSt.getObject().toString());
         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);

         int ThisID = getID();
         //MyResource.addProperty(e3_has_formula, "INVESTMENT=0");
         MyResource.addProperty(e3_has_formula, "INVESTMENT=" + ThisID);
         //MyResource.addProperty(e3_has_formula, "EXPENSES=0");
         MyResource.addProperty(e3_has_uid, ""+ ThisID);
         Resource X     = MyModel.createResource(e3value + "value_activity");
         MyModel.add(MyResource, RDF.type, X);
         Traverse = true;
         //System.out.println("    YES New Value Activity Name : " + AuxSt.getObject().toString());
      }
      else
      {
         ////System.out.println("    NO there is already a value activity for : " + AuxSt.getObject().toString());
         Traverse = false;
      }
      return MyResource;
   }

   public Resource Generate_Value_Activity(Resource AuxVA, Model MyModel, String ThisName)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;
      String Complete_Name;
      AuxSt = AuxVA.getProperty(e3_has_name);
      Complete_Name = new String(AuxSt.getObject().toString() + "_" + ThisName);
      //Complete_Name = new String(AuxSt.getObject().toString());
      //System.out.println(" Trying to create a new value activity : " + Complete_Name + " TN - " + ThisName);

      //MyResource = Look_for_Resource(VA,e3_has_name,Complete_Name,MyModel);
      //MyResource = Look_for_Resource(VA,e3_is_instance_for,Complete_Name + "_" + ThisName,MyModel);

      if(MyResource == null)
      {
         MyResource = MyModel.createResource(IName + generateID());
         MyResource.addProperty(e3_has_name, Complete_Name);

         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);

         int ThisID = getID();
         //MyResource.addProperty(e3_has_formula, "INVESTMENT=0");
         MyResource.addProperty(e3_has_formula, "INVESTMENT=" + ThisID);
         //MyResource.addProperty(e3_has_formula, "EXPENSES=0");
         MyResource.addProperty(e3_has_uid, ""+ ThisID);

         Resource X     = MyModel.createResource(e3value + "value_activity");
         MyModel.add(MyResource, RDF.type, X);
         Traverse = true;
         //System.out.println("    YES New Value Activity Name : " + Complete_Name);
      }
      else
      {
         ////System.out.println("    NO there is already a value activity for : " + Complete_Name);
         Traverse = false;
      }
      return MyResource;
   }

   public Resource Generate_Value_Activity(Model MyModel, String ThisName)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      String Complete_Name;
      Complete_Name = new String(ThisName);
      //Complete_Name = new String(AuxSt.getObject().toString());
      ////System.out.println(" Trying to create a new value activity : " + Complete_Name + " TN - " + ThisName);

      //MyResource = Look_for_Resource(VA,e3_has_name,Complete_Name,MyModel);
      //MyResource = Look_for_Resource(VA,e3_is_instance_for,Complete_Name + "_" + ThisName,MyModel);

      if(MyResource == null)
      {
         MyResource = MyModel.createResource(IName + generateID());
         MyResource.addProperty(e3_has_name, Complete_Name);

         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);

         int ThisID = getID();
         //MyResource.addProperty(e3_has_formula, "INVESTMENT=0");
         MyResource.addProperty(e3_has_formula, "INVESTMENT=" + ThisID);
         //MyResource.addProperty(e3_has_formula, "EXPENSES=0");
         MyResource.addProperty(e3_has_uid, ""+ ThisID);

         Resource X     = MyModel.createResource(e3value + "value_activity");
         MyModel.add(MyResource, RDF.type, X);
         Traverse = true;
         //System.out.print("    YES New Value Activity Name : " + Complete_Name);
         //System.out.println(" Unique-URI " + MyResource.getURI());
      }
      else
      {
         //System.out.println("    NO there is already a value activity for : " + Complete_Name);
         Traverse = false;
      }
      return MyResource;
   }

   public Resource Generate_Value_Interface(Model MyModel)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      MyResource = MyModel.createResource(IName + generateID());
      MyResource.addProperty(e3_has_name, "vi" + getID());

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, ""+ getID());
      Resource X     = MyModel.createResource(e3value + "value_interface");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }

   public Resource Generate_Value_Object(Model MyModel, String Name, boolean Skeleton, String ClassVO)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      // WATCH OUT : Before creating the value object, we should check if this
      // vo already exists
      MyResource = Look_for_Resource(VOb,e3_has_name,Name,MyModel);
      ////System.out.println("Trying to generate value object " + Name);
      if(MyResource == null)
      {
         if(Skeleton)
            MyResource = MyModel.createResource(SKName + (1000 + generateID()));
         else
            MyResource = MyModel.createResource(IName + generateID());
         MyResource.addProperty(e3_has_name, Name);

         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);

         MyResource.addProperty(e3_has_uid, ""+ getID());
         Resource X     = MyModel.createResource(e3value + "value_object");
         MyModel.add(MyResource, RDF.type, X);
         ////System.out.println("   YES new value object created " + Name + " id : " + MyResource.toString());
      }
      else
      {
         ////System.out.println("The value object " + Name + " already exists.");
         Traverse = false;
      }
      return MyResource;
   }

   public Resource Generate_Value_Exchange(Model MyModel)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      // WATC OUT : Before creating the value object, we should check if this
      // vo already exists
      MyResource = MyModel.createResource(IName + generateID());
      MyResource.addProperty(e3_has_name, "ve" + getID());

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, ""+ getID());
      MyResource.addProperty(e3_has_formula, "CARDINALITY=1");
      Resource X     = MyModel.createResource(e3value + "value_exchange");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }

   public Resource Generate_Value_Port(Model MyModel)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      // WATC OUT : Before creating the value object, we should check if this
      // vo already exists
      MyResource = MyModel.createResource(IName + generateID());
      MyResource.addProperty(e3_has_name, "vp" + getID());

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, ""+ getID());
      Resource X     = MyModel.createResource(e3value + "value_port");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }

   public Resource Generate_Value_Offering(Model MyModel, String MyName)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      // WATC OUT : Before creating the value object, we should check if this
      // vo already exists
      MyResource = MyModel.createResource(IName + generateID());
      MyResource.addProperty(e3_has_name, MyName);

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, ""+ getID());
      Resource X     = MyModel.createResource(e3value + "value_offering");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }

   public Resource Generate_Value_Port_Attribute(Model MyModel)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      // WATC OUT : Before creating the value object, we should check if this
      // vo already exists
      MyResource = MyModel.createResource(IName + generateID());
      //MyResource.addProperty(e3_has_name, "ss" + getID());

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, ""+ getID());
      MyResource.addProperty(e3_has_formula, "OCURRENCES=1");
      Resource X     = MyModel.createResource(e3value + "value_port_attribute");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }

   public Resource Generate_Start_Stimulus(Model MyModel)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      // WATC OUT : Before creating the value object, we should check if this
      // vo already exists
      MyResource = MyModel.createResource(IName + generateID());
      MyResource.addProperty(e3_has_name, "ss" + getID());

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, ""+ getID());
      MyResource.addProperty(e3_has_formula, "OCURRENCES=1");
      Resource X     = MyModel.createResource(e3value + "start_stimulus");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }

   public Resource Generate_End_Stimulus(Model MyModel)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      // WATC OUT : Before creating the value object, we should check if this
      // vo already exists
      MyResource = MyModel.createResource(IName + generateID());
      MyResource.addProperty(e3_has_name, "es" + getID());

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, ""+ getID());
      Resource X     = MyModel.createResource(e3value + "end_stimulus");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }

   public Resource Generate_Functional_Consequence(Model MyModel, String Name)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      // WATCH OUT : Before creating the functional consequence, we should check if this
      // functional consequence already exists
      MyResource = Look_for_Resource(FConsequence,e3_has_name,Name,MyModel);
      ////System.out.println("Trying to generate functional consequence " + Name);
      if(MyResource == null)
      {
         MyResource = MyModel.createResource(IName + generateID());
         MyResource.addProperty(e3_has_name, Name);
         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);
         MyResource.addProperty(e3_has_uid, ""+ getID());
         Resource X = MyModel.createResource(e3value + "functional_consequence");
         MyModel.add(MyResource, RDF.type, X);
         ////System.out.println("   YES new functional_consequence created " + Name + " id : " + MyResource.toString());
      }
      else
      {
         ////System.out.println("The functional_consequence " + Name + " already exists.");
      }
      return MyResource;
   }

   public Resource Generate_Quality_Consequence(Model MyModel, String Name, String Label)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      // WATCH OUT : Before creating the quality consequence, we should check if this
      // quality consequence already exists
      MyResource = Look_for_Resource(VQConsequence,e3_has_uid,Name,MyModel);
      //System.out.println("Trying to generate quality consequence " + Name);
      if(MyResource == null)
      {
         //MyResource = MyModel.createResource(IName + generateID());
         generateID();
         MyResource = MyModel.createResource(IName + Name);
         MyResource.addProperty(e3_has_name, Label);
         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);

         //MyResource.addProperty(e3_has_uid, ""+ getID());
         MyResource.addProperty(e3_has_uid, Name);

         Resource X = MyModel.createResource(e3value + "quality_consequence");
         MyModel.add(MyResource, RDF.type, X);
         //System.out.println("   YES new functional_consequence created " + Name + " id : " + MyResource.toString());
      }
      else
      {
         //System.out.println("The quality_consequence " + Name + " already exists.");
      }
      return MyResource;
   }

   public Resource Generate_Want(Model MyModel, String Name)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      // WATCH OUT : Before creating the functional consequence, we should check if this
      // functional consequence already exists
      MyResource = Look_for_Resource(Want,e3_has_name,Name,MyModel);
      ////System.out.println("Trying to generate functional consequence " + Name);
      if(MyResource == null)
      {
         MyResource = MyModel.createResource(IName + generateID());
         MyResource.addProperty(e3_has_name, Name);
         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);
         MyResource.addProperty(e3_has_uid, ""+ getID());
         Resource X = MyModel.createResource(e3value + "want");
         MyModel.add(MyResource, RDF.type, X);
         ////System.out.println("   YES new want created " + Name + " id : " + MyResource.toString());
      }
      else
      {
         ////System.out.println("The want " + Name + " already exists.");
      }
      return MyResource;
   }

   public Resource Generate_Nominal_Scale(Model MyModel, String Name, String Label)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      // WATCH OUT : Before creating the nominal scale, we should check if this
      // nominal scale already exists
      MyResource = Look_for_Resource(VNominalS,e3_has_uid,Name,MyModel);
      //System.out.println("Trying to generate quality consequence " + Name);
      if(MyResource == null)
      {
         //MyResource = MyModel.createResource(IName + generateID());
         generateID();
         MyResource = MyModel.createResource(IName + Name);
         MyResource.addProperty(e3_has_name, Label);
         model  = MyModel.getResource(IName + "0");
         diagram = MyModel.getResource(IName + "1");
         MyModel.add(MyResource, mc_in_mo, model);
         MyModel.add(MyResource, mc_in_di, diagram);

         //MyResource.addProperty(e3_has_uid, ""+ getID());
         MyResource.addProperty(e3_has_uid, Name);

         Resource X = MyModel.createResource(e3value + "nominal");
         MyModel.add(MyResource, RDF.type, X);
         //System.out.println("   YES new functional_consequence created " + Name + " id : " + MyResource.toString());
      }
      else
      {
         //System.out.println("The nominal scale " + Name + " already exists.");
      }
      return MyResource;
   }

   public Resource Generate_Connection_Element(Model MyModel)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      // WATC OUT : Before creating the value object, we should check if this
      // vo already exists
      MyResource = MyModel.createResource(IName + generateID());
      MyResource.addProperty(e3_has_name, "ce" + getID());

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, ""+ getID());
      MyResource.addProperty(down_fraction, "1");
      MyResource.addProperty(up_fraction, "1");
      Resource X     = MyModel.createResource(e3value + "connection_element");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }

   public Resource Generate_e3value_AND(Model MyModel)
   {
      Resource  MyResource = null;
      Resource model, diagram;
      Statement AuxSt     = null;

      // WATC OUT : Before creating the value object, we should check if this
      // vo already exists
      MyResource = MyModel.createResource(IName + generateID());
      MyResource.addProperty(e3_has_name, "and" + getID());

      model  = MyModel.getResource(IName + "0");
      diagram = MyModel.getResource(IName + "1");
      MyModel.add(MyResource, mc_in_mo, model);
      MyModel.add(MyResource, mc_in_di, diagram);

      MyResource.addProperty(e3_has_uid, ""+ getID());
      Resource X     = MyModel.createResource(e3value + "e3value_AND");
      MyModel.add(MyResource, RDF.type, X);

      return MyResource;
   }

   // Remove
   void remove_value_interface(Model MyModel, Resource MyVI)
   {
      // Remove UP - de_up_ce
      StmtIterator IT_CE_UP = MyVI.listProperties(de_up_ce);
      Statement   ST_CE_UP = null;
      while(IT_CE_UP.hasNext())
      {
         ST_CE_UP = IT_CE_UP.nextStatement();
         Resource This_CE = MyModel.getResource(ST_CE_UP.getObject().toString());
         remove_connection_element(MyModel,This_CE,"up");
      }
      // Remove Down - de_down_ce
      StmtIterator IT_CE_DOWN = MyVI.listProperties(de_down_ce);
      Statement   ST_CE_DOWN = null;
      while(IT_CE_DOWN.hasNext())
      {
         ST_CE_DOWN = IT_CE_DOWN.nextStatement();
         Resource This_CE = MyModel.getResource(ST_CE_DOWN.getObject().toString());
         remove_connection_element(MyModel,This_CE,"down");
      }
      // Remove Offerings
      StmtIterator IT_OFFERINGS = MyVI.listProperties(vi_consists_of_of);
      Statement   ST_OFFERINGS = null;
      while(IT_OFFERINGS.hasNext())
      {
         ST_OFFERINGS = IT_OFFERINGS.nextStatement();
         Resource This_OFF = MyModel.getResource(ST_OFFERINGS.getObject().toString());
         remove_value_offering(MyModel,This_OFF);
      }
      MyVI.removeProperties();

   }

   void remove_value_offering(Model MyModel, Resource MyOffering)
   {
      // Remove Ports
      StmtIterator IT_PORTS = MyOffering.listProperties(vo_consists_of_vp);
      Statement   ST_PORTS = null;
      while(IT_PORTS.hasNext())
      {
         ST_PORTS = IT_PORTS.nextStatement();
         Resource This_Port = MyModel.getResource(ST_PORTS.getObject().toString());
         // remove value exchanges vp_first_connects_ve
         if(This_Port.hasProperty(vp_first_connects_ve))
         {
            String VPFirst = This_Port.getProperty(vp_first_connects_ve).getObject().toString();
            Resource RFirst = MyModel.getResource(VPFirst);
            if(RFirst != null)
               RFirst.removeProperties();
         }
         // vp_second_connects_ve
         if(This_Port.hasProperty(vp_second_connects_ve))
         {
            String VPSecond = This_Port.getProperty(vp_second_connects_ve).getObject().toString();
            Resource RSecond = MyModel.getResource(VPSecond);
            if(RSecond != null)
               RSecond.removeProperties();
         }
         // vp_in_connects_ve
         if(This_Port.hasProperty(vp_in_connects_ve))
         {
            String VPIN = This_Port.getProperty(vp_in_connects_ve).getObject().toString();
            Resource RIN = MyModel.getResource(VPIN);
            if(RIN != null)
               RIN.removeProperties();
         }
         // vp_out_connects_ve
         if(This_Port.hasProperty(vp_out_connects_ve))
         {
            String VPOUT = This_Port.getProperty(vp_out_connects_ve).getObject().toString();
            Resource ROUT = MyModel.getResource(VPOUT);
            if(ROUT != null)
               ROUT.removeProperties();
         }
         // Remove link with value object
         if(This_Port.hasProperty(vp_requests_offers_vo))
         {
            String VO = This_Port.getProperty(vp_requests_offers_vo).getObject().toString();
            Resource VObject = MyModel.getResource(VO);
            if(VObject != null)
            {
               remove_this_Property(MyModel, VObject, vo_offered_requested_by_vp, This_Port);
            }
         }
         This_Port.removeProperties();
      }
      MyOffering.removeProperties();
   }

   void remove_connection_element(Model MyModel, Resource MyCE, String Dir)
   {
      if(Dir.equals("up"))
      {
         // Connection element ce_with_up_de
         StmtIterator IT_CE_UP = MyCE.listProperties(ce_with_up_de);
         Statement   ST_CE_UP = null;
         while(IT_CE_UP.hasNext())
         {
            ST_CE_UP = IT_CE_UP.nextStatement();
            Resource This_E = MyModel.getResource(ST_CE_UP.getObject().toString());
            if(This_E != null)
               This_E.removeProperties();
         }
      }
      else
      {
         // Connection element - ce_with_down_de
         StmtIterator IT_CE_DOWN = MyCE.listProperties(ce_with_down_de);
         Statement   ST_CE_DOWN = null;
         while(IT_CE_DOWN.hasNext())
         {
            ST_CE_DOWN = IT_CE_DOWN.nextStatement();
            Resource This_E = MyModel.getResource(ST_CE_DOWN.getObject().toString());
            if(This_E != null)
               This_E.removeProperties();
         }
      }
      MyCE.removeProperties();
   }

   void remove_value_activity(Model MyModel, Resource MyVA)
   {
      // Remove Interfaces
      StmtIterator IT_Interfaces = MyVA.listProperties(va_has_vi);
      Statement   STMyVI      = null;
      while(IT_Interfaces.hasNext())
      {
         STMyVI = IT_Interfaces.nextStatement();
         // Value Interface
         Resource MyVI = MyModel.getResource(STMyVI.getObject().toString());
         remove_value_interface(MyModel, MyVI);
      }
      MyVA.removeProperties();
   }

   public Resource Get_Value_Activity(Model Skeleton, Resource RElement)
   {
      Resource  MyResource = null;
      Resource  AuxRes    = null;
      Statement AuxSt     = null;
      // connection_element -> ce_with_down_de
      // e3value_AND        -> de_down_ce
      String AuxStr;

      //AuxSt = RElement.getProperty(de_down_ce);
      ////System.out.println(" de_down_ce Connected to: " + AuxSt.getObject().toString());
      //AuxRes = Skeleton.getResource(AuxSt.getObject().toString());
      AuxRes = RElement;
      AuxStr = AuxRes.getProperty(RDF.type).getObject().toString();

      ////System.out.println("   Resource : " + AuxRes.toString() + " - type " + AuxStr);
      while(!AuxStr.equals(VI))
      {
         if( (AuxStr.equals(AND)) || (AuxStr.equals(SS)))
         {
            AuxSt = AuxRes.getProperty(de_down_ce);
            ////System.out.println(" de_down_ce Connected to: " + AuxSt.getObject().toString());
         }
         else
            if(AuxStr.equals(CE))
            {
               AuxSt = AuxRes.getProperty(ce_with_down_de);
               ////System.out.println(" ce_with_down_de Connected to: " + AuxSt.getObject().toString());
            }
         AuxRes = Skeleton.getResource(AuxSt.getObject().toString());
         AuxStr = AuxRes.getProperty(RDF.type).getObject().toString();
         ////System.out.println("   Resource : " + AuxRes.toString() + " - type " + AuxStr);
      }
      if(AuxStr.equals(VI))
      {
         AuxSt = AuxRes.getProperty(vi_assigned_to_va);
         AuxRes = Skeleton.getResource(AuxSt.getObject().toString());
         ////System.out.println("   Value_Activity : " + AuxRes.toString());
         //AuxSt = AuxRes.getProperty(e3_has_name);
         ////System.out.println("   Name : " + AuxSt.getObject().toString());
         MyResource = AuxRes;
      }

      return MyResource;
   }

   public Resource Get_Elementary_Actor(Model Consumer_Need, Resource Aux_Value_Activity)
   {
      Resource  MyResource = null;
      Statement AuxSt     = null;

      AuxSt     = Aux_Value_Activity.getProperty(va_performed_by_el);
      MyResource = Consumer_Need.getResource(AuxSt.getObject().toString());
      return MyResource;
   }

   public void Assign_Activity_to_Actor(Model MyModel, Resource MyValue_Activity, Resource MyActor, Resource Composite_Actor)
   {
      // MyValue_Activity - va_has_vi
      //                    va_performed_by_el
      //                    Value_Interface - vi_assigned_to_va
      //                                      vi_assigned_to_ac

      /*
       * 1 - For each value interface assigned to MyValue_Activity
       *   a - The value interface must be assigned to MyActor
       *       vi_assigned_to_ac -> MyActor
       *       MyActor ac_has_vi -> value interface
       *       remove vi_assigned_to_va
       *   b - Generate a new value interface
       *   c - Link this new vi with MyValue_Activity
       *       change va_has_vi from MyValue_Activity
       *   d - Link this new vi with the value interface visited ?????
       * 2 - Assign MyValue_Activity to MyActor
       */
      ////System.out.print(" $$$ Now exploring value activity : ");
      //System.out.println(MyValue_Activity.getProperty(e3_has_name).getObject().toString());
       Vector<Resource> VThisVA = new Vector<Resource>();
       VThisVA.add(MyValue_Activity);
       Vector<Resource> TheseVIs = new Vector<Resource>();
       RDF_Utilities TmpObj = new RDF_Utilities(false);
       TheseVIs = TmpObj.getRelatedObjects(VThisVA, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#va_has_vi");
       MyValue_Activity.removeAll(va_has_vi);

      for(int x = 0; x < TheseVIs.size(); x++)
      {

         Resource MyValue_Interface = TheseVIs.get(x);

         MyModel.add(MyValue_Interface, vi_assigned_to_ac, MyActor);
         MyModel.add(MyActor, ac_has_vi, MyValue_Interface);
         MyValue_Interface.removeAll(vi_assigned_to_va);
         ////System.out.println(" \t Value Interface was reassigned ... ");

         Resource New_Value_Interface = Generate_Value_Interface(MyModel);
         MyModel.add(New_Value_Interface, vi_assigned_to_va, MyValue_Activity);
         MyModel.add(MyValue_Activity, va_has_vi, New_Value_Interface);
         ////System.out.println(" \t New Value Interface generated ... ");

         // Connection elements
         Statement AuxSTCE = null;
         Resource AuxCE = null;
         AuxSTCE = MyValue_Interface.getProperty(de_up_ce);
         if(AuxSTCE == null)
         {
            // Link with down
            if(MyValue_Interface.hasProperty(de_down_ce))
            {
               AuxSTCE = MyValue_Interface.getProperty(de_down_ce);
               AuxCE = MyModel.getResource(AuxSTCE.getObject().toString());
               AuxCE.removeAll(ce_with_up_de);
               MyValue_Interface.removeAll(de_down_ce);
               MyModel.add(New_Value_Interface, de_down_ce, AuxCE);
               MyModel.add(AuxCE, ce_with_up_de, New_Value_Interface);
               //System.out.println("\n AuxCE : " + AuxCE.toString() + " (ce_with_up_de) reconnected to : " + New_Value_Interface.toString());
            }
         }
         else
         {
            // Link with up
            AuxCE = MyModel.getResource(AuxSTCE.getObject().toString());
            AuxCE.removeAll(ce_with_down_de);
            MyValue_Interface.removeAll(de_up_ce);
            MyModel.add(New_Value_Interface, de_up_ce, AuxCE);
            MyModel.add(AuxCE, ce_with_down_de, New_Value_Interface);
            //System.out.println("\n AuxCE : " + AuxCE.toString() + " (ce_with_down_de) reconnected to : " + New_Value_Interface.toString());
         }
         //Statement DownCE = MyValue_Interface.getProperty(de_down_ce);

         // Link Value Interfaces
         // New_Value_Interface -> MyValue_Interface
         Link_Value_Interfaces(MyModel, New_Value_Interface, MyValue_Interface);

         if(Composite_Actor != null)
         {
            // Link Interface in Actor with Composite Actor
            Resource CA_VI = Generate_Value_Interface(MyModel);
            //
            MyModel.add(Composite_Actor, ca_consists_of_vi, MyValue_Interface);
            MyModel.add(MyValue_Interface, vi_in_ca, Composite_Actor);

            Copy_Value_Interfaces(MyModel, MyValue_Interface, MyModel, CA_VI, false);
            Link_Value_Interfaces_2(MyModel, CA_VI, MyValue_Interface);
            MyModel.add(Composite_Actor, ac_has_vi, CA_VI);
            MyModel.add(CA_VI, vi_assigned_to_ac, Composite_Actor);

            /*
            // Get Value Interface in actor
            Resource MyVI = Value_Interfaces.get(index);
            MyModel.add(MyCA, MyB2B.ca_consists_of_vi, MyVI);
            MyModel.add(MyVI, MyB2B.vi_in_ca, MyCA);
            // Generate Value Interface in SB
            Resource CA_VI = MyB2B.Generate_Value_Interface(MyModel);
            MyModel.add(MyCA, MyB2B.ac_has_vi, CA_VI);
            MyModel.add(CA_VI, MyB2B.vi_assigned_to_ac, MyCA);
            // Link Value Interfaces
            MyB2B.Copy_Value_Interfaces(MyModel, MyVI, MyModel, CA_VI, false);
            MyB2B.Link_Value_Interfaces_2(MyModel, MyVI, CA_VI);
             */
         }

      }
      // Assigning MyValue_Activity to MyActor
      MyModel.add(MyActor, el_performs_va, MyValue_Activity);
      MyModel.add(MyValue_Activity, va_performed_by_el, MyActor);
   }

   public Resource Find_Start_Stimulus(Model Skeleton)
   {
      Resource MyResource = null;
      //Property RDF.type    = Skeleton.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#","type");
      ResIterator iter   = Skeleton.listResourcesWithProperty(RDF.type);
      if (iter.hasNext())
      {
         //System.out.println(" The model contains at least this start_stimulus:");
         while (iter.hasNext())
         {
            ////System.out.println("  " + iter.next().getProperty(de_down_ce).getString());
            MyResource = iter.next();
            String SStimulus = MyResource.getProperty(RDF.type).getObject().toString();
            if(SStimulus.equals(SS))
            {
               //System.out.println("   " + MyResource.toString());
               // ===============================================
               ////System.out.println("   Properties : ");
               StmtIterator iter2 = MyResource.listProperties();
               while (iter2.hasNext())
               {
                  Statement AuxSt;
                  AuxSt = iter2.nextStatement();
                  ////System.out.println("\t " + AuxSt.getObject().toString());
                  ////System.out.println("\t " + AuxSt.toString());
               }
               // ===============================================
               break;
            }
         }
      }
      else
      {
         //System.out.println("No start_stimulus were found in the model");
      }
      return MyResource;
   }

   public Model Read_Model(String Filename)
   {
      Model model = ModelFactory.createDefaultModel();
      //System.out.println(" Trying to read file " + Filename);
	   try
      {
         InputStream in =new  FileInputStream(Filename);
		   if (in == null)
         {
			   //System.out.println("   File " + Filename +" not found");
         }
         else
         {
            model.read(in," ");
            //System.out.println("   File " + Filename +" was successfully read");
         }
		   //model.write(System.out);
      }
      catch(Exception e){}
      return model;
   }

   public void Read_Properties(Model Consumer_Need)
   {
      ce_with_down_de = Consumer_Need.getProperty(e3value,"ce_with_down_de");
      ce_with_up_de = Consumer_Need.getProperty(e3value,"ce_with_up_de");
      de_down_ce     = Consumer_Need.getProperty(e3value,"de_down_ce");
      de_up_ce     = Consumer_Need.getProperty(e3value,"de_up_ce");
      di_has_mc     = Consumer_Need.getProperty(e3value,"di_has_mc");
      mc_in_di     = Consumer_Need.getProperty(e3value,"mc_in_di");
      mc_in_mo     = Consumer_Need.getProperty(e3value,"mc_in_mo");
      mo_has_mc    = Consumer_Need.getProperty(e3value,"mo_has_mc");
      va_has_vi    = Consumer_Need.getProperty(e3value,"va_has_vi");
      ve_has_in_po     = Consumer_Need.getProperty(e3value,"ve_has_in_po");
      ve_has_out_po     = Consumer_Need.getProperty(e3value,"ve_has_out_po");
      ve_in_vt     = Consumer_Need.getProperty(e3value,"ve_in_vt");
      vi_assigned_to_va = Consumer_Need.getProperty(e3value,"vi_assigned_to_va");
      vi_consists_of_of = Consumer_Need.getProperty(e3value,"vi_consists_of_of");
      vo_consists_of_vp = Consumer_Need.getProperty(e3value,"vo_consists_of_vp");
      vo_in_vi     = Consumer_Need.getProperty(e3value,"vo_in_vi");
      vo_offered_requested_by_vp = Consumer_Need.getProperty(e3value,"vo_offered_requested_by_vp");
      vp_in_connects_ve       = Consumer_Need.getProperty(e3value,"vp_in_connects_ve");
      vp_in_vo              = Consumer_Need.getProperty(e3value,"vp_in_vo");
      vp_out_connects_ve       = Consumer_Need.getProperty(e3value,"vp_out_connects_ve");
      vp_requests_offers_vo = Consumer_Need.getProperty(e3value,"vp_requests_offers_vo");
      vt_consists_of_ve = Consumer_Need.getProperty(e3value,"vt_consists_of_ve");

      down_fraction     = Consumer_Need.getProperty(e3value,"down_fraction");
      e3_has_formula     = Consumer_Need.getProperty(e3value,"e3_has_formula");
      e3_has_name     = Consumer_Need.getProperty(e3value,"e3_has_name");
      e3_has_uid     = Consumer_Need.getProperty(e3value,"e3_has_uid");
      up_fraction     = Consumer_Need.getProperty(e3value,"up_fraction");
      vp_has_dir     = Consumer_Need.getProperty(e3value,"vp_has_dir");
      vt_has_fraction     = Consumer_Need.getProperty(e3value,"vt_has_fraction");

      // Consumer_Need

      depends_on = Consumer_Need.getProperty(e3value, "depends_on");
      dependent_of = Consumer_Need.getProperty(e3value, "dependent_of");

      el_performs_va    = Consumer_Need.getProperty(e3value, "el_performs_va");
      vi_assigned_to_ac  = Consumer_Need.getProperty(e3value, "vi_assigned_to_ac");
      ac_has_vi        = Consumer_Need.getProperty(e3value, "ac_has_vi");
      va_performed_by_el = Consumer_Need.getProperty(e3value, "va_performed_by_el");

      ve_has_first_vp  = Consumer_Need.getProperty(e3value, "ve_has_first_vp");
      ve_has_second_vp  = Consumer_Need.getProperty(e3value, "ve_has_second_vp");
      vp_first_connects_ve  = Consumer_Need.getProperty(e3value, "vp_first_connects_ve");
      vp_second_connects_ve  = Consumer_Need.getProperty(e3value, "vp_second_connects_ve");

      //vp_has_attribute_at = Consumer_Need.getProperty(e3value, "vp_has_attribute_at");
      //at_assigned_to_vp  = Consumer_Need.getProperty(e3value, "at_assigned_to_vp");
      e3_has_value      = Consumer_Need.getProperty(e3value, "e3_has_value");
      vi_depends_on_vi   = Consumer_Need.getProperty(e3value, "vi_depends_on_vi");

      // Instances
      vi_has_vii = Consumer_Need.getProperty(e3value,"vi_has_vii");
      vii_of_vi  = Consumer_Need.getProperty(e3value,"vii_of_vi");
      vii_has_vpi = Consumer_Need.getProperty(e3value,"vii_has_vpi");
      vpi_of_vii  = Consumer_Need.getProperty(e3value,"vpi_of_vii");
      vpi_has_vai = Consumer_Need.getProperty(e3value,"vpi_has_vai");
      vpi_has_dir = Consumer_Need.getProperty(e3value,"vpi_has_dir");
      vai_of_vpi  = Consumer_Need.getProperty(e3value,"vai_of_vpi");

      // Functional consequences
      consequence_has_resource = Consumer_Need.getProperty(e3value,"consequence_has_resource");
      resource_has_consequence = Consumer_Need.getProperty(e3value,"resource_has_consequence");
      has_scale = Consumer_Need.getProperty(e3value,"has_scale");
      consists_of = Consumer_Need.getProperty(e3value,"consists_of");
      has_want = Consumer_Need.getProperty(e3value,"has_want");
      has = Consumer_Need.getProperty(e3value,"has");
      
      // Composite actor
      ca_consists_of_vi = Consumer_Need.getProperty(e3value,"ca_consists_of_vi");
      vi_in_ca = Consumer_Need.getProperty(e3value,"vi_in_ca");


   }

   public void check_my_model(Model MyModel)
   {
      Resource  MyResource = null;
      Resource  AlreadyHere = null;
      boolean Flag = false;
      int Number = 1;
      boolean Analyze = true;
      while(Analyze)
      {
         Analyze = false;
         //System.out.println(" Analyzing Model ... " + Number);
         Number++;
         ResIterator iter = MyModel.listResourcesWithProperty(RDF.type);
         if (iter.hasNext())
         {
            while (iter.hasNext())
            {
               MyResource = iter.next();
               String RequestedType = MyResource.getProperty(RDF.type).getObject().toString();
               ////System.out.println("\n" + RequestedType + " " + MyResource.getProperty(e3_has_uid).getObject().toString());
               StmtIterator STITR = MyResource.listProperties();
               Property MyProperty = null;
               Flag = false;
               while(STITR.hasNext())
               {
                  Statement AuxSt;
                  AuxSt = STITR.nextStatement();
                  MyProperty = AuxSt.getPredicate();
                  if( (!MyProperty.equals(e3_has_name)) && (!MyProperty.equals(e3_has_formula)) &&
                     (!MyProperty.equals(e3_has_uid)) && (!MyProperty.equals(e3_has_value))   &&
                     (!MyProperty.equals(RDF.type))   && (!MyProperty.equals(vp_has_dir))    &&
                     (!MyProperty.equals(up_fraction))   && (!MyProperty.equals(down_fraction))
                    )
                  {
                     ////System.out.print("\t" + MyProperty.toString());
                     Resource ToCheckR = MyModel.getResource(AuxSt.getObject().toString());
                     if(ToCheckR != null)
                     {
                        String ThisType = null;
                        if(ToCheckR.hasProperty(RDF.type))
                        {
                           ThisType = new String(ToCheckR.getProperty(RDF.type).getObject().toString());
                        }
                        else
                        {
                           //System.out.println("\n" + RequestedType + " " + MyResource.getProperty(e3_has_uid).getObject().toString());
                           //System.out.print("\t" + MyProperty.toString());
                           //System.out.println("\t\t NULL OBJECT 1" + AuxSt.getObject().toString());
                           Flag = true;
                           //break;
                        }
                        ////System.out.println("\t EXISTS " + ThisType);
                     }
                     else
                     {
                        //System.out.println("\n" + RequestedType + " " + MyResource.getProperty(e3_has_uid).getObject().toString());
                        //System.out.print("\t" + MyProperty.toString());
                        //System.out.println("\t\t NULL OBJECT 2" + AuxSt.getObject().toString());
                        Flag = true;
                        //break;
                     }
                  }
               }// End While STITR.hasNext()
               if(Flag)
               {
                  MyResource.removeProperties();
                  Analyze = true;
               }
            }
         }
         else
         {
            //System.out.println("The model is empty");
         }
      }
   }

   void print_list_properties(Model MyModel, Resource MyResource)
   {
      StmtIterator IT = MyResource.listProperties();
      Statement   ST = null;
      System.out.println("\n R: " + MyResource.toString());
      System.out.println("\t\t URI " + MyResource.getURI());
      System.out.println("\t\t Local Name " + MyResource.getLocalName());
      System.out.println("\t\t Name Space " + MyResource.getNameSpace());
      if(IT.hasNext())
      {
        while(IT.hasNext())
        {
            ST = IT.nextStatement();
            System.out.println("\t\t ST: " + ST.toString());
        }
      }
      else
      {
          System.out.println("\t\t The Resource does not have properties ");
          System.out.println("\t\t URI " + MyResource.getURI());
          System.out.println("\t\t Local Name " + MyResource.getLocalName());
          System.out.println("\t\t Name Space " + MyResource.getNameSpace());
      }
   }

   public boolean IsItFree(Model MyModel, Resource Second_VI)
   {
       boolean Res = true;
       
       Resource MyResource = Get_Connected_To_VI(Second_VI, MyModel);
       if(MyResource != null)
       {
           if(MyResource.hasProperty(vi_assigned_to_ac))
               Res = false;
       }
       return true;
   }

}

