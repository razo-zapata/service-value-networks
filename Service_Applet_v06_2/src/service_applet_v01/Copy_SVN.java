/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 *
 * @author ivan
 */
public class Copy_SVN {

    private int ID;
    private B2B MyAuxB2B;
    private RDF_Utilities MyAuxRDF;
    private Resource MyNewSVN;
    private Resource Bundle2Export;

    public Copy_SVN(Resource ThisBundle)
    {
        ID = 3;
        MyAuxB2B = new B2B(1);
        MyAuxRDF = new RDF_Utilities();
        MyNewSVN = null;
        Bundle2Export = ThisBundle;
    }

    public Integer generateID()
    {
        return ++ID;
    }

    public Integer getID()
    {
        return ID;
    }

    public void setID(int NewID)
    {
        ID = NewID;
    }

    public void Generate_Diagram_and_Model(Model MyModel)
    {
        Resource model;
        Resource diagram;
        model = MyModel.createResource(MyAuxB2B.IName + "0");
        model.addProperty(B2B.e3_has_name, "model");
        model.addProperty(B2B.e3_has_uid, "0");
        Resource X     = MyModel.createResource(MyAuxB2B.e3value + "model");
        MyModel.add(model, RDF.type, X);

        diagram = MyModel.createResource(MyAuxB2B.IName + "1");
        diagram.addProperty(B2B.e3_has_name, "Bussines_Model");
        diagram.addProperty(B2B.e3_has_uid, "1");
        X = MyModel.createResource(MyAuxB2B.e3value + "diagram");
        MyModel.add(diagram, RDF.type, X);
    }// End Generate_Diagram_and_Model
    
    public void Fill_Diagram_and_Model(Model MyModel)
    {
        Resource model;
        Resource diagram;
        Resource AuxRes;
        int i;
        model = MyModel.getResource(MyAuxB2B.IName + "0");
        diagram = MyModel.getResource(MyAuxB2B.IName + "1");
        for(i = 2; i <= getID(); i++)
        {
            AuxRes = MyModel.getResource(MyAuxB2B.IName + i);
            MyModel.add(model, B2B.mo_has_mc, AuxRes);
            MyModel.add(diagram, B2B.di_has_mc, AuxRes);
        }
    }// End Fill_Diagram_and_Model
    
    public Resource cloneBundle(Model BigModel, Model SmallModel, Resource OriginalBundle, Vector<Resource> MyConsequences, String ThisNeed)
    {
        String BundleName = OriginalBundle.getProperty(B2B.e3_has_name).getObject().toString();
        Resource NewBundle = Generate_Service_Bundle(SmallModel, BundleName);
        
        // Get Actors Inside Original Bundle
        Vector<Resource> TheseServices = MyAuxRDF.getServicesFromBundle(OriginalBundle, BigModel);
        for(int i = 0; i < TheseServices.size(); i++)
        {
            Resource NewService = cloneService(BigModel, SmallModel, TheseServices.get(i));
            // Get Actor
            Resource ThisActor = BigModel.getResource(TheseServices.get(i).getProperty(B2B.va_performed_by_el).getObject().toString());
            // Clone Actor --- ThisActor = cloneActor(
            Resource NewActor = cloneActor(BigModel, SmallModel,ThisActor);

            // Assign NewService to NewActor
            MyAuxB2B.SetID(getID());
            MyAuxB2B.Assign_Activity_to_Actor(SmallModel, NewService, NewActor, NewBundle);
            setID(MyAuxB2B.getID());

            // Merge interfaces
            MyAuxB2B.SetID(getID());
            MyAuxRDF.Merge_Interfaces_Smartly(NewBundle, MyConsequences, true, SmallModel, MyAuxB2B);
            setID(MyAuxB2B.getID());
        }

        // Copy B2B dependencies
        copy_enablers(BigModel, SmallModel, OriginalBundle, NewBundle);

        // Generate Customer
        Vector<Resource> VBundle = new Vector<Resource>();
        VBundle.add(NewBundle);
        Vector<Resource> VIinBundle = MyAuxRDF.getRelatedObjects(VBundle, SmallModel, "http://www.cs.vu.nl/~gordijn/e3value#ca_consists_of_vi");
        Vector<Resource> TheseInterfaces = MyAuxRDF.getRelatedObjects(VBundle, SmallModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");

        for(int i = 0; i < TheseInterfaces.size(); i++)
        {
            Resource ThisInt = TheseInterfaces.get(i);
            Resource TempA = MyAuxB2B.Get_Connected_To_VI(ThisInt, SmallModel);
            if(TempA != null)
            {
                if(VIinBundle.contains(TempA))
                {
                    // ThisInt connects the bundle to the customer
                    Resource MyCustomer = Generate_Actor(SmallModel, "CUSTOMER");
                    Resource NewInt = Generate_Interface(SmallModel);
                            //cloneInterface(SmallModel, SmallModel, ThisInt, false);
                    Resource MyActivity = Generate_Service(SmallModel, ThisNeed);
                    SmallModel.add(MyActivity, B2B.va_has_vi, NewInt);
                    SmallModel.add(NewInt, B2B.vi_assigned_to_va, MyActivity);

                    // Assign MyActivity to MyCustomer
                    MyAuxB2B.SetID(getID());
                    //MyAuxB2B.Link_Value_Interfaces(SmallModel, NewInt, ThisInt);
                    MyAuxB2B.Generate_Value_Exchanges(SmallModel, ThisInt, NewInt);
                    MyAuxB2B.Assign_Activity_to_Actor(SmallModel, MyActivity, MyCustomer, null);
                    Resource MySS = MyAuxB2B.Generate_Start_Stimulus(SmallModel);
                    Resource MyCE = MyAuxB2B.Generate_Connection_Element(SmallModel);
                    setID(MyAuxB2B.getID());

                    // Link MySS with the value interface in the value activity
                    Resource ThisVAVI = SmallModel.getResource(MyActivity.getProperty(B2B.va_has_vi).getObject().toString());
                    //Linking VI to CE
                    SmallModel.add(ThisVAVI, B2B.de_down_ce, MyCE);
                    SmallModel.add(MyCE, B2B.ce_with_up_de, ThisVAVI);
                    //Linking CE to SS
                    SmallModel.add(MyCE, B2B.ce_with_down_de, MySS);
                    SmallModel.add(MySS, B2B.de_up_ce, MyCE);
                }
            }
        }
        return MyNewSVN;
    }

    public void copy_enablers(Model BigModel, Model SmallModel, Resource OriginalBundle, Resource NewBundle)
    {
       Vector<Resource> VBundle = new Vector<Resource>();
       VBundle.add(OriginalBundle);
       Vector<Resource> TheseInterfaces = MyAuxRDF.getRelatedObjects(VBundle, BigModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");
       for(int i = 0; i < TheseInterfaces.size(); i++)
       {
          Resource ThisInt = TheseInterfaces.get(i);
          Resource Second_VI = MyAuxB2B.Get_Connected_To_VI(ThisInt, BigModel);
          if(Second_VI != null)
          {

             Vector<Resource> VIinBundle = MyAuxRDF.getRelatedObjects(VBundle, BigModel, "http://www.cs.vu.nl/~gordijn/e3value#ca_consists_of_vi");
             boolean Second_Ok = true;
             if(VIinBundle.contains(Second_VI))
             {
                Second_Ok = false;
                // ThisInt connects the bundle to the customer
             }

             if(Second_Ok)
             {
               
               System.out.println("\n We got an actor ... ");
               Resource ThisActor = BigModel.getResource(Second_VI.getProperty(B2B.vi_assigned_to_ac).getObject().toString());
               Resource NewActor = cloneActor(BigModel, SmallModel, ThisActor);
               Vector<Resource> VThisActor = new Vector<Resource>();
               VThisActor.add(ThisActor);
               Vector<Resource> TheseServices = MyAuxRDF.getRelatedObjects(VThisActor, BigModel, "http://www.cs.vu.nl/~gordijn/e3value#el_performs_va");
               Vector<Resource> TheseServInterfaces = MyAuxRDF.getRelatedObjects(TheseServices,BigModel,"http://www.cs.vu.nl/~gordijn/e3value#va_has_vi");
               
               for(int j = 0; j < TheseServInterfaces.size(); j++)
               {
                   Resource ThisServInt = TheseServInterfaces.get(j);
                   Resource InternalVI = MyAuxB2B.Linkables(BigModel,ThisInt,BigModel,ThisServInt,false);
                   if(InternalVI != null)
                   {
                    Resource ThisService = BigModel.getResource(InternalVI.getProperty(B2B.vi_assigned_to_va).getObject().toString());
                    Resource NewService = cloneService(BigModel, SmallModel, ThisService);
                    MyAuxB2B.SetID(getID());
                    MyAuxB2B.Assign_Activity_to_Actor(SmallModel, NewService, NewActor, null);
                    setID(MyAuxB2B.getID());

                   }
                   else
                   {
                        System.out.println("\n WE HAVE A HUGE PROBLEM, THE ACTOR DOES NOT HAVE A SERVICE ....");
                   }
               }
             }// End if(Second_Ok)
          }
       }

       // Time to link enablers
       VBundle = new Vector<Resource>();
       VBundle.add(NewBundle);
       TheseInterfaces = MyAuxRDF.getRelatedObjects(VBundle, SmallModel, "http://www.cs.vu.nl/~gordijn/e3value#ac_has_vi");
       for(int i = 0; i < TheseInterfaces.size(); i++)
       {
          Resource ThisInt = TheseInterfaces.get(i);
          MyAuxB2B.SetID(getID());
          MyAuxB2B.Match_Value_Interface(SmallModel, ThisInt, SmallModel, false);
          setID(MyAuxB2B.getID());
       }
    }

    public Resource cloneActor(Model BigModel, Model SmallModel, Resource ThisActor)
    {
        Resource NewActor = null;
        NewActor = Generate_Actor(SmallModel, ThisActor.getProperty(B2B.e3_has_name).getObject().toString());
        return NewActor;
    }

    public Resource cloneService(Model BigModel, Model SmallModel, Resource ThisService)
    {
        Resource NewService = null;

        NewService = Generate_Service(SmallModel, ThisService.getProperty(B2B.e3_has_name).getObject().toString());

        // Get Value Interfaces
        Vector<Resource> VService = new Vector<Resource>();
        VService.add(ThisService);
        Vector<Resource> TheseInterfaces = MyAuxRDF.getRelatedObjects(VService, BigModel, "http://www.cs.vu.nl/~gordijn/e3value#va_has_vi");
        
        // Connections between interfaces
        Resource MyCE = null;
        Resource MyES = null;
        Resource MyUP = null;
        Resource MyDOWN = null;
        
        //System.out.println("\n Cloning Service : " + ThisService.getProperty(B2B.e3_has_name).getObject().toString());
        //System.out.print("\n Name : " + ThisService.toString());
        //System.out.println("\t with " + TheseInterfaces.size() + " interfaces.");

        for(int i = 0; i < TheseInterfaces.size(); i++)
        {
            // GetInterface
            Resource ThisInterface = TheseInterfaces.get(i);
            // Clone Interface
            Resource NewInterface = cloneInterface(BigModel,SmallModel,ThisInterface,true);
            // Assign interface to value activity
            SmallModel.add(NewService, B2B.va_has_vi, NewInterface);
            SmallModel.add(NewInterface, B2B.vi_assigned_to_va, NewService);

            //System.out.println(" VI: " + ThisInterface.toString());
            
            // Connections?
            if(ThisInterface.hasProperty(B2B.de_down_ce))
            {
                //System.out.println("\n we should link with CE down");
                MyUP = NewInterface;

                // It is connected to an end stimulus??
                Resource ThisCE = BigModel.getResource(ThisInterface.getProperty(B2B.de_down_ce).getObject().toString());
                if(ThisCE != null)
                {
                    if(ThisCE.hasProperty(B2B.ce_with_down_de))
                    {
                        Resource ThisEndS = BigModel.getResource(ThisCE.getProperty(B2B.ce_with_down_de).getObject().toString());
                        String ThisType = ThisEndS.getProperty(RDF.type).getObject().toString();
                        if(ThisType.equals(MyAuxB2B.ES))
                        {
                            // Generate Connection Element and end Stimulus
                            MyAuxB2B.SetID(getID());
                            MyCE = MyAuxB2B.Generate_Connection_Element(SmallModel);
                            MyES = MyAuxB2B.Generate_End_Stimulus(SmallModel);
                            setID(MyAuxB2B.getID());
                            
                            // Interface with CE
                            SmallModel.add(MyUP, B2B.de_down_ce, MyCE);
                            SmallModel.add(MyCE, B2B.ce_with_up_de, MyUP);
                            // CE with ES
                            SmallModel.add(MyCE, B2B.ce_with_down_de, MyES);
                            SmallModel.add(MyES, B2B.de_up_ce, MyCE);
                        }
                    }
                }

            }
            if(ThisInterface.hasProperty(B2B.de_up_ce))
            {
                //System.out.println("\n we should link with CE up");
                MyDOWN = NewInterface;
            }
            if( (MyUP != null) && (MyDOWN != null))
            {
                MyAuxB2B.SetID(getID());
                MyCE = MyAuxB2B.Generate_Connection_Element(SmallModel);
                setID(MyAuxB2B.getID());
                SmallModel.add(MyUP, B2B.de_down_ce, MyCE);
                SmallModel.add(MyDOWN, B2B.de_up_ce, MyCE);
                SmallModel.add(MyCE, B2B.ce_with_up_de, MyUP);
                SmallModel.add(MyCE, B2B.ce_with_down_de, MyDOWN);
                //System.out.println("\n the link was created ... toch?");
                MyCE = null;
                MyUP = null;
                MyDOWN = null;
            }
        }
        
        return NewService;
    }

    public Resource cloneInterface(Model BigModel, Model SmallModel, Resource ThisInterface, boolean SameDir)
    {
        Resource NewInterface = null;
        NewInterface = Generate_Interface(SmallModel);
        // Get Value Offerings
        Vector<Resource> VThisInterface = new Vector<Resource>();
        VThisInterface.add(ThisInterface);
        Vector<Resource> TheseOfferings = MyAuxRDF.getRelatedObjects(VThisInterface, BigModel,"http://www.cs.vu.nl/~gordijn/e3value#vi_consists_of_of");
        for(int i = 0; i < TheseOfferings.size(); i++)
        {
            Resource ThisOffering = TheseOfferings.get(i);
            Resource NewOffering = cloneOffering(BigModel, SmallModel,ThisOffering, SameDir);

            SmallModel.add(NewInterface, B2B.vi_consists_of_of, NewOffering);
            SmallModel.add(NewOffering, B2B.vo_in_vi, NewInterface);
        }
        // More stuff
        return NewInterface;
    }// End cloneInterface

    public Resource cloneOffering(Model BigModel, Model SmallModel, Resource ThisOffering, boolean SameDir)
    {
        Resource NewOffering = null;
        if (SameDir)
            NewOffering = Generate_Offering(SmallModel,ThisOffering.getProperty(B2B.e3_has_name).getObject().toString());
        else
        {
            if(ThisOffering.getProperty(B2B.e3_has_name).getObject().toString().equals("in"))
                NewOffering = Generate_Offering(SmallModel,"out");
            else
                NewOffering = Generate_Offering(SmallModel,"in");
        }

        Vector<Resource> VThisOffering = new Vector<Resource>();
        VThisOffering.add(ThisOffering);
        Vector<Resource> TheseVPs = MyAuxRDF.getRelatedObjects(VThisOffering, BigModel, "http://www.cs.vu.nl/~gordijn/e3value#vo_consists_of_vp");
        for(int i = 0; i < TheseVPs.size(); i++)
        {
            Resource ThisVP = TheseVPs.get(i);
            Resource NewValuePort = cloneValuePort(BigModel,SmallModel,ThisVP, SameDir);

            SmallModel.add(NewOffering,B2B.vo_consists_of_vp, NewValuePort);
            SmallModel.add(NewValuePort,B2B.vp_in_vo, NewOffering);
        }

        return NewOffering;
    }

    public Resource cloneValuePort(Model BigModel, Model SmallModel, Resource ThisVP, boolean SameDir)
    {
        Resource NewValuePort = null;
        
        NewValuePort = Generate_Value_Port(SmallModel);

        Vector<Resource> VThisVP = new Vector<Resource>();
        VThisVP.add(ThisVP);
        Vector<Resource> TheseVOs = MyAuxRDF.getRelatedObjects(VThisVP, BigModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo");
        if(TheseVOs.size() == 0)
        {
            VThisVP = new Vector<Resource>();
            // Get the value object through VE
            if(ThisVP.hasProperty(B2B.vp_second_connects_ve))
            {
                Resource ThisVE = BigModel.getResource(ThisVP.getProperty(B2B.vp_second_connects_ve).getObject().toString());
                //has_first_vp
                //vp_requests_offers_vo
                VThisVP.add(ThisVE);
                TheseVOs = MyAuxRDF.getRelatedObjects(
                        MyAuxRDF.getRelatedObjects(VThisVP, BigModel, "http://www.cs.vu.nl/~gordijn/e3value#ve_has_first_vp"),
                        BigModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo");
            }
            else
            {
                // 
                Resource ThisVE = BigModel.getResource(ThisVP.getProperty(B2B.vp_first_connects_ve).getObject().toString());
                //has_second_vp
                //vp_requests_offers_vo
                VThisVP.add(ThisVE);
                TheseVOs = MyAuxRDF.getRelatedObjects(
                        MyAuxRDF.getRelatedObjects(VThisVP, BigModel, "http://www.cs.vu.nl/~gordijn/e3value#ve_has_second_vp"),
                        BigModel, "http://www.cs.vu.nl/~gordijn/e3value#vp_requests_offers_vo");
            }
        }

        for(int i = 0; i < TheseVOs.size(); i++)
        {
            Resource ThisValueObject = TheseVOs.get(i);
            Resource NewValueObject = cloneValueObject(BigModel,SmallModel,ThisValueObject);

            SmallModel.add(NewValuePort, B2B.vp_requests_offers_vo, NewValueObject);
            SmallModel.add(NewValueObject, B2B.vo_offered_requested_by_vp, NewValuePort);
        }

        if (SameDir)
            NewValuePort.addProperty(B2B.vp_has_dir, ThisVP.getProperty(B2B.vp_has_dir).getObject().toString());
        else
        {
            if(ThisVP.getProperty(B2B.vp_has_dir).getObject().toString().equals("true"))
                NewValuePort.addProperty(B2B.vp_has_dir, "false");
            else
                NewValuePort.addProperty(B2B.vp_has_dir, "true");
        }

        
        return NewValuePort;
    }
    
    public Resource cloneValueObject(Model BigModel, Model SmallModel, Resource ThisValueObject)
    {
        Resource NewValueObject = null;

        NewValueObject = Generate_Value_Object(SmallModel,ThisValueObject.getProperty(B2B.e3_has_name).getObject().toString());
        // Let's go for the FCs
        Vector<Resource> VThisValueObject = new Vector<Resource>();
        Vector<Resource> TheseFCs = new Vector<Resource>();
        VThisValueObject.add(ThisValueObject);
        TheseFCs = MyAuxRDF.getRelatedObjects(VThisValueObject, BigModel, "http://www.cs.vu.nl/~gordijn/e3value#resource_has_consequence");
        
        for(int i = 0; i < TheseFCs.size(); i++)
        {
            Resource ThisFC = TheseFCs.get(i);
            Resource NewFC = cloneConsequence(BigModel,SmallModel,ThisFC);

            SmallModel.add(NewValueObject, B2B.resource_has_consequence, NewFC);
            SmallModel.add(NewFC, B2B.consequence_has_resource, NewValueObject);

            System.out.println("\n FC: " + ThisFC.getProperty(B2B.e3_has_name).getObject().toString());
        }
        
        return NewValueObject;
    }

    public Resource cloneConsequence(Model BigModel,Model SmallModel,Resource ThisFC)
    {
        Resource NewFC = null;
        NewFC = Generate_Consequence(SmallModel,ThisFC.getProperty(B2B.e3_has_name).getObject().toString());
        
        return NewFC;
    }

    public Resource Generate_Consequence(Model MyModel,String Name)
    {
        Resource  MyResource = null;
        Resource model, diagram;
        // WATCH OUT : Before creating the functional consequence, we should check if this
        // functional consequence already exists
        MyResource = MyAuxB2B.Look_for_Resource(MyAuxB2B.FConsequence,B2B.e3_has_name,Name,MyModel);
        ////System.out.println("Trying to generate functional consequence " + Name);
        if(MyResource == null)
        {
            MyResource = MyModel.createResource(MyAuxB2B.IName + generateID());
            MyResource.addProperty(B2B.e3_has_name, Name);
            model  = MyModel.getResource(MyAuxB2B.IName + "0");
            diagram = MyModel.getResource(MyAuxB2B.IName + "1");
            MyModel.add(MyResource, B2B.mc_in_mo, model);
            MyModel.add(MyResource, B2B.mc_in_di, diagram);
            MyResource.addProperty(B2B.e3_has_uid, ""+ getID());
            Resource X = MyModel.createResource(MyAuxB2B.e3value + "functional_consequence");
            MyModel.add(MyResource, RDF.type, X);
            ////System.out.println("   YES new functional_consequence created " + Name + " id : " + MyResource.toString());
        }
        else
        {
             ////System.out.println("The functional_consequence " + Name + " already exists.");
        }
        return MyResource;
    }

    public Resource Generate_Value_Object(Model MyModel, String Name)
    {
        Resource  MyResource = null;
        Resource model, diagram;
        // WATCH OUT : Before creating the value object, we should check if this
        // vo already exists
        MyResource = MyAuxB2B.Look_for_Resource(MyAuxB2B.VOb,B2B.e3_has_name,Name,MyModel);
        ////System.out.println("Trying to generate value object " + Name);
        if(MyResource == null)
        {
            MyResource = MyModel.createResource(MyAuxB2B.IName + generateID());
            MyResource.addProperty(B2B.e3_has_name, Name);

            model  = MyModel.getResource(MyAuxB2B.IName + "0");
            diagram = MyModel.getResource(MyAuxB2B.IName + "1");
            MyModel.add(MyResource, B2B.mc_in_mo, model);
            MyModel.add(MyResource, B2B.mc_in_di, diagram);

            MyResource.addProperty(B2B.e3_has_uid, ""+ getID());
            Resource X     = MyModel.createResource(MyAuxB2B.e3value + "value_object");
            MyModel.add(MyResource, RDF.type, X);
            ////System.out.println("   YES new value object created " + Name + " id : " + MyResource.toString());
        }
        else
        {
            ////System.out.println("The value object " + Name + " already exists.");
        }
        return MyResource;
    }//End Generate_Value_Object

    public Resource Generate_Value_Port(Model MyModel)
    {
        Resource  MyResource = null;
        Resource model, diagram;
        
        MyResource = MyModel.createResource(MyAuxB2B.IName + generateID());
        MyResource.addProperty(B2B.e3_has_name, "vp" + getID());
        
        model  = MyModel.getResource(MyAuxB2B.IName + "0");
        diagram = MyModel.getResource(MyAuxB2B.IName + "1");
        MyModel.add(MyResource, B2B.mc_in_mo, model);
        MyModel.add(MyResource, B2B.mc_in_di, diagram);
        
        MyResource.addProperty(B2B.e3_has_uid, ""+ getID());
        Resource X     = MyModel.createResource(MyAuxB2B.e3value + "value_port");
        MyModel.add(MyResource, RDF.type, X);
        
        return MyResource;
    }// End Generate_Value_Port

    public Resource Generate_Offering(Model MyModel, String MyName)
    {
        Resource  MyResource = null;
        Resource model, diagram;

        MyResource = MyModel.createResource(MyAuxB2B.IName + generateID());
        MyResource.addProperty(B2B.e3_has_name, MyName);
        
        model  = MyModel.getResource(MyAuxB2B.IName + "0");
        diagram = MyModel.getResource(MyAuxB2B.IName + "1");
        MyModel.add(MyResource, B2B.mc_in_mo, model);
        MyModel.add(MyResource, B2B.mc_in_di, diagram);
        
        MyResource.addProperty(B2B.e3_has_uid, ""+ getID());
        Resource X     = MyModel.createResource(MyAuxB2B.e3value + "value_offering");
        MyModel.add(MyResource, RDF.type, X);
        
        return MyResource;
    }
    
    public Resource Generate_Interface(Model MyModel)
    {
        Resource  MyResource = null;
        Resource model, diagram;
        //Statement AuxSt     = null;

        MyResource = MyModel.createResource(MyAuxB2B.IName + generateID());
        MyResource.addProperty(B2B.e3_has_name, "vi" + getID());

        model  = MyModel.getResource(MyAuxB2B.IName + "0");
        diagram = MyModel.getResource(MyAuxB2B.IName + "1");
        MyModel.add(MyResource, B2B.mc_in_mo, model);
        MyModel.add(MyResource, B2B.mc_in_di, diagram);

        MyResource.addProperty(B2B.e3_has_uid, ""+ getID());
        Resource X     = MyModel.createResource(MyAuxB2B.e3value + "value_interface");
        MyModel.add(MyResource, RDF.type, X);
        
        return MyResource;
    }

    public Resource Generate_Service_Bundle(Model MyModel, String ThisName)
    {
        Resource  MyResource = null;
        Resource model, diagram;
        
        MyResource = MyModel.createResource(MyAuxB2B.IName + generateID());
        MyResource.addProperty(B2B.e3_has_name, ThisName);
        
        model  = MyModel.getResource(MyAuxB2B.IName + "0");
        diagram = MyModel.getResource(MyAuxB2B.IName + "1");
        MyModel.add(MyResource, B2B.mc_in_mo, model);
        MyModel.add(MyResource, B2B.mc_in_di, diagram);

        MyResource.addProperty(B2B.e3_has_formula, "INVESTMENT=0");
        MyResource.addProperty(B2B.e3_has_formula, "EXPENSES=0");
        MyResource.addProperty(B2B.e3_has_uid, ""+ getID());
        Resource X     = MyModel.createResource(MyAuxB2B.e3value + "composite_actor");
        MyModel.add(MyResource, RDF.type, X);
        return MyResource;
    }// End of Generate_Service_Bundle

    public Resource Generate_Service(Model MyModel, String ThisName)
    {
        Resource  MyResource = null;
        Resource model, diagram;

        MyResource = MyModel.createResource(MyAuxB2B.IName + generateID());
        MyResource.addProperty(B2B.e3_has_name, ThisName);

        model  = MyModel.getResource(MyAuxB2B.IName + "0");
        diagram = MyModel.getResource(MyAuxB2B.IName + "1");
        MyModel.add(MyResource, B2B.mc_in_mo, model);
        MyModel.add(MyResource, B2B.mc_in_di, diagram);

        MyResource.addProperty(B2B.e3_has_formula, "INVESTMENT=0");
        MyResource.addProperty(B2B.e3_has_formula, "EXPENSES=0");
        MyResource.addProperty(B2B.e3_has_uid, ""+ getID());
        Resource X     = MyModel.createResource(MyAuxB2B.e3value + "value_activity");
        MyModel.add(MyResource, RDF.type, X);
        return MyResource;
    }// End of Generate_Service

    public Resource Generate_Actor(Model MyModel, String ThisName)
    {
        Resource  MyResource = null;
        Resource model, diagram;
        ////System.out.println(" Trying to create a new value activity : " + AuxSt.getObject().toString());

        MyResource = MyAuxB2B.Look_for_Resource(MyAuxB2B.EA,B2B.e3_has_name,ThisName,MyModel);

        if(MyResource == null)
        {
            MyResource = MyModel.createResource(MyAuxB2B.IName + generateID());
            MyResource.addProperty(B2B.e3_has_name, ThisName);

            model  = MyModel.getResource(MyAuxB2B.IName + "0");
            diagram = MyModel.getResource(MyAuxB2B.IName + "1");
            MyModel.add(MyResource, B2B.mc_in_mo, model);
            MyModel.add(MyResource, B2B.mc_in_di, diagram);

            int ThisID = getID();
            MyResource.addProperty(B2B.e3_has_formula, "INVESTMENT=" + ThisID);
            MyResource.addProperty(B2B.e3_has_uid, ""+ ThisID);
            Resource X     = MyModel.createResource(MyAuxB2B.e3value + "elementary_actor");
            MyModel.add(MyResource, RDF.type, X);
        }
        else
        {
            ////System.out.println("    NO there is already an elementary actor called : " + ThisName);
        }
        return MyResource;
   }//End of Generate_Actor

    public void assign_service_2_actor(Model MyModel, Resource NewService, Resource NewActor, Resource NewBundle)
    {
        // NewService - va_has_vi
        //              va_performed_by_el
        //              Value_Interface - vi_assigned_to_va
        //                                vi_assigned_to_ac

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

        Vector<Resource> VThisService = new Vector<Resource>();
        VThisService.add(NewService);
        Vector<Resource> TheseInterfaces = MyAuxRDF.getRelatedObjects(VThisService, MyModel, "http://www.cs.vu.nl/~gordijn/e3value#va_has_vi");
        NewService.removeAll(B2B.va_has_vi);

        for(int i = 0; i < TheseInterfaces.size(); i++)
        {
            ////System.out.print("\t VI : " + AuxST.getObject().toString());
            //Resource MyValue_Interface = MyModel.getResource(AuxST.getObject().toString());
            Resource MyValue_Interface = TheseInterfaces.get(i);
            ////System.out.print(" Properties : " + MyValue_Interface.toString());
            ////System.out.println("\t VI : " + MyValue_Interface.getProperty(vi_assigned_to_va).getObject().toString());

            MyModel.add(MyValue_Interface, B2B.vi_assigned_to_ac, NewActor);
            MyModel.add(NewActor, B2B.ac_has_vi, MyValue_Interface);
            MyValue_Interface.removeAll(B2B.vi_assigned_to_va);
            ////System.out.println(" \t Value Interface was reassigned ... ");

            Resource New_Value_Interface = Generate_Interface(MyModel);
            MyModel.add(New_Value_Interface, B2B.vi_assigned_to_va, NewService);
            MyModel.add(NewService, B2B.va_has_vi, New_Value_Interface);
            ////System.out.println(" \t New Value Interface generated ... ");

            // Connection elements
            Statement AuxSTCE = null;
            Resource AuxCE = null;
            AuxSTCE = MyValue_Interface.getProperty(B2B.de_up_ce);
            if(AuxSTCE == null)
            {
                // Link with down
                if(MyValue_Interface.hasProperty(B2B.de_down_ce))
                {
                    AuxSTCE = MyValue_Interface.getProperty(B2B.de_down_ce);
                    AuxCE = MyModel.getResource(AuxSTCE.getObject().toString());
                    AuxCE.removeAll(B2B.ce_with_up_de);
                    MyValue_Interface.removeAll(B2B.de_down_ce);
                    MyModel.add(New_Value_Interface, B2B.de_down_ce, AuxCE);
                    MyModel.add(AuxCE, B2B.ce_with_up_de, New_Value_Interface);
                }
            }
            else
            {
                // Link with up
                AuxCE = MyModel.getResource(AuxSTCE.getObject().toString());
                AuxCE.removeAll(B2B.ce_with_down_de);
                MyValue_Interface.removeAll(B2B.de_up_ce);
                MyModel.add(New_Value_Interface, B2B.de_up_ce, AuxCE);
                MyModel.add(AuxCE, B2B.ce_with_down_de, New_Value_Interface);
            }
            //Statement DownCE = MyValue_Interface.getProperty(de_down_ce);

            MyAuxB2B.SetID(getID());

            // Link Value Interfaces
            // New_Value_Interface -> MyValue_Interface
            MyAuxB2B.Link_Value_Interfaces(MyModel, New_Value_Interface, MyValue_Interface);
            //AuxST.getObject().
            //MyModel.add(MyValue_Activity, va_has_vi, New_Value_Interface);
            //AuxST.remove();

            setID(MyAuxB2B.getID());

            if(NewBundle != null)
            {
                // Link Interface in Actor with Composite Actor
                Resource CA_VI = Generate_Interface(MyModel);
                //
                MyModel.add(NewBundle, B2B.ca_consists_of_vi, MyValue_Interface);
                MyModel.add(MyValue_Interface, B2B.vi_in_ca, NewBundle);

                MyAuxB2B.SetID(getID());

                MyAuxB2B.Copy_Value_Interfaces(MyModel, MyValue_Interface, MyModel, CA_VI, false);
                MyAuxB2B.Link_Value_Interfaces_2(MyModel, CA_VI, MyValue_Interface);
                MyModel.add(NewBundle, B2B.ac_has_vi, CA_VI);
                MyModel.add(CA_VI, B2B.vi_assigned_to_ac, NewBundle);

                setID(MyAuxB2B.getID());
            }

        }
        // Assigning MyValue_Activity to MyActor
        MyModel.add(NewActor, B2B.el_performs_va, NewService);
        MyModel.add(NewService, B2B.va_performed_by_el, NewActor);
    }
    
}
