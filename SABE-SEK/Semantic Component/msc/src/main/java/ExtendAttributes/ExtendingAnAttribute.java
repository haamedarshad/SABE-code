package ExtendAttributes;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.File;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.reasoner.IndividualNodeSetPolicy;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
import openllet.owlapi.OWL;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class ExtendingAnAttribute {

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {		
		 /*System.out.println("Please enter an attribute");
	    Scanner myScanner = new Scanner(System.in);  // Create a Scanner object
	  
	    *  String[] InputAttribute = myScanner.nextLine();  // Read user input
	   
	    myScanner.close();
		shouldCreatePropertyAssertions(InputAttribute);
		*/
	    System.out.println("Please enter your attributes. All on one line with a space between them. Press the ENTER key when you are done!");
		//read a line from user 
	    Scanner myScanner = new Scanner(System.in);  // Create a Scanner object

		String attrs = myScanner.nextLine();
		//we'd like to split the line into words, so we create a
		//scanner to read from the line
		Scanner linesc = new Scanner (attrs);
		Vector attr_str = new Vector();
		Vector<String> attr_str2 = new Vector<String>();
		while (linesc.hasNext()) {
		    String nextWord = linesc.next(); 
		    attr_str.add(nextWord);
		}
		shouldCreatePropertyAssertions(attr_str);
		}
		
///////////////////////////////////////////////////////////////////////////////////////////
	/**
     * This example shows how to specify various property assertions for
     * individuals.
     * 
     * @throws OWLOntologyStorageException
     * @throws OWLOntologyCreationException
	 * @throws IOException 
     */
	  //public static Vector<String> shouldCreatePropertyAssertions(String Attribute)
	public static Vector<String> shouldCreatePropertyAssertions(Vector Attributes)
            throws OWLOntologyStorageException, OWLOntologyCreationException, IOException {
        
    	OWLOntologyManager man = OWLManager.createOWLOntologyManager();
	//	File file = new File("C:\\Users\\MackBook\\Desktop\\test onto\\UniOnto.owl");
    	File file = new File("src/UniOnto.owl");
		OWLOntology o = man.loadOntologyFromOntologyDocument(file);
		OWLDataFactory df = man.getOWLDataFactory();	
        IRI ontologyIRI = IRI.create("http://www.semanticweb.org/mackbook/ontologies/2020/10/UniTest#");
        PrefixManager pm = new DefaultPrefixManager(ontologyIRI.toString());
        OWLNamedIndividual christian = df.getOWLNamedIndividual("Christian", pm);
        OWLDataProperty hasID = df.getOWLDataProperty("hasID", pm);
        OWLDataPropertyAssertionAxiom propertyAssertion = df.getOWLDataPropertyAssertionAxiom(hasID, christian, 5);
        AddAxiom addAxiom0 = new AddAxiom(o, propertyAssertion);
        man.applyChange(addAxiom0);

        OWLDataPropertyAssertionAxiom propertyAssertion2 ;
        for(int i =0; i<Attributes.size();i++)
        {
       // 	if(Attributes.get(i).toString().matches("hasSupervised(.*)"))
         	if(Attributes.get(i).toString().matches("has(.*)"))
        	{
        		String value;
        		int index = Attributes.get(i).toString().indexOf("=");
        		//System.out.println("index of = is "+index );
        		
        		String hasproperty =  Attributes.get(i).toString().substring(0, index);
        		value = Attributes.get(i).toString().substring(index+1);

        		//number = Attributes.get(i).toString().substring(14);
        		OWLDataProperty hasProperty = df.getOWLDataProperty(hasproperty, pm);
        		//OWLDataProperty hasSupervised = df.getOWLDataProperty("hasSupervised", pm);
        		//Integer val=Integer.valueOf(number);

        		if(hasproperty.equals("hasSupervised"))
        		{
        			Integer value2=Integer.valueOf(value);
        	       // OWLDataPropertyAssertionAxiom propertyAssertion2  = df.getOWLDataPropertyAssertionAxiom(hasProperty, christian, value2);
        			 propertyAssertion2  = df.getOWLDataPropertyAssertionAxiom(hasProperty, christian, value2);
        		}
        		else {
        			//OWLDataPropertyAssertionAxiom propertyAssertion2 = df.getOWLDataPropertyAssertionAxiom(hasProperty, christian, value);	
        			propertyAssertion2 = df.getOWLDataPropertyAssertionAxiom(hasProperty, christian, value);
        		}
                AddAxiom addAxiom01 = new AddAxiom(o, propertyAssertion2);
                man.applyChange(addAxiom01);
        	}
        	else{
        	OWLClass attribute = df.getOWLClass(Attributes.get(i).toString(), pm);
            OWLClassAssertionAxiom classAssertion = df.getOWLClassAssertionAxiom(attribute, christian); 
            AddAxiom addAxiom1 = new AddAxiom(o, classAssertion);
            man.applyChange(addAxiom1);
        	}
        }
        
    
    // Hermit reasoner    OWLReasonerFactory reasonerFactory = new ReasonerFactory();
	// Hermit reasoner    OWLReasoner reasoner = reasonerFactory.createReasoner(o);
	
        // Pellet reasoner
        final OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(o);  
		
        /*		
	    reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
	    reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
	    reasoner.precomputeInferences(InferenceType.DATA_PROPERTY_ASSERTIONS);
	    reasoner.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
	    */
	    reasoner.precomputeInferences(InferenceType.values());
        
	    //related to the Pellet reasoner
	    reasoner.getKB().realize();
	//	reasoner.getKB().printClassTree();
	    
    //    man.saveOntology(o); //I commented out this because I want to enter all the received (entered) attributes for a user HA.
		//printIndividualsByclass(o,df,pm); //printing the results//It is commented out because I have commented out the following method.
//////////////////////////I did copy the following method here
	   // OWLReasonerFactory reasonerFactory = new ReasonerFactory();
	   // OWLReasoner reasoner = reasonerFactory.createReasoner(o, new SimpleConfiguration());
      //  OWLNamedIndividual christian = df.getOWLNamedIndividual("Christian", pm); //It is supposed that all the received attributes are inserted into the Ontology for an individual named Christian.
      //  Vector<String> attributes2 = new Vector<String>();
        Vector<String> attributes = new Vector<String>();

        NodeSet<OWLClass> Inferredclasses = reasoner.getTypes(christian);
  /*    
        for (OWLClassAssertionAxiom c : o.getClassAssertionAxioms(christian)) {
        	//System.out.println(c.getClassExpression().asOWLClass().getIRI().getShortForm());
        	NodeSet<OWLClass> classes = reasoner.getSuperClasses(c.getClassExpression());
        	Node<OWLClass> classes2 = reasoner.getEquivalentClasses((c.getClassExpression()));
        	//
        	
        	for (OWLClass i : classes.getFlattened()) {
  			  //System.out.println(i.getIRI().getShortForm());
  			  attributes2.add((i.getIRI().getShortForm()).toString());
  			}
        	for (OWLClass j : classes2) {
    			//System.out.println(j.getIRI().getShortForm());
        		attributes2.add((j.getIRI().getShortForm()).toString());
    		}
        	
        }
        
        System.out.println("attributes are: " + attributes2);
        */
        for (OWLClass k : Inferredclasses.getFlattened()) {
			  //System.out.println(i.getIRI().getShortForm());
			  attributes.add((k.getIRI().getShortForm()).toString());
			}
      //  System.out.println("attributes are: " + attributes);
      //  man.saveOntology(o);
		return attributes;
////////////////////////////////////////////////////////////////        
    }
    
/////////////////////////////////////////////////////////////////////////////////////////////
    /*
	  public static void printIndividualsByclass(OWLOntology o,OWLDataFactory df,PrefixManager pm) throws OWLOntologyCreationException{
    	
		OWLReasonerFactory reasonerFactory = new ReasonerFactory();
	    OWLReasoner reasoner = reasonerFactory.createReasoner(o, new SimpleConfiguration());
        OWLNamedIndividual christian = df.getOWLNamedIndividual("Christian", pm); //It is supposed that all the received attributes are inserted into the Ontology for an individual named Christian.
        Vector<String> attributes = new Vector<String>();
/////Getting all the classes that Christian is somehow involved in.
        for (OWLClassAssertionAxiom c : o.getClassAssertionAxioms(christian)) {
        	//System.out.println(c.getClassExpression().asOWLClass().getIRI().getShortForm());
        	NodeSet<OWLClass> classes = reasoner.getSuperClasses(c.getClassExpression());
        	Node<OWLClass> classes2 = reasoner.getEquivalentClasses((c.getClassExpression()));
        	for (OWLClass i : classes.getFlattened()) {
  			  //System.out.println(i.getIRI().getShortForm());
  			  attributes.add((i.getIRI().getShortForm()).toString());
  			}
        	for (OWLClass j : classes2) {
    			//System.out.println(j.getIRI().getShortForm());
        		attributes.add((j.getIRI().getShortForm()).toString());
    		}
        }
        
      //  System.out.println(attributes); //Printing all the attributes in a list  
        //Printing all attributes, each in a line
        for (int k=0;k<attributes.size();k++){ 
        	System.out.println(attributes.get(k));
        }    
    }
    */
///////////////////////////////////////////////////////////////////////////////////////////

}

