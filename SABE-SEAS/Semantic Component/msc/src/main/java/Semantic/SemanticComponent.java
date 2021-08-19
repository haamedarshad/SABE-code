package Semantic;
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
import org.semanticweb.owlapi.model.OWLClassExpression;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
import openllet.owlapi.OWL;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class SemanticComponent {

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {		
	
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
	}	

/////////////////////////////////////////////////////////////////////////////////////////// Finding Subclasses and Equivalent Classes of a particular class 
	public static Vector<String> findRelevantAttributes(Vector Attributes) throws OWLOntologyCreationException {
		
		System.out.println("Just arrived in findRelevantAttributes");
		
        Vector<String> relevantAttributes = new Vector<String>();
        
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        File file = new File("src/OSN.owl");
    	OWLOntology o = man.loadOntologyFromOntologyDocument(file);
    	OWLDataFactory df = man.getOWLDataFactory();	
        IRI ontologyIRI = IRI.create("http://www.semanticweb.org/mackbook/ontologies/2021/1/untitled-ontology-34#");
        PrefixManager pm = new DefaultPrefixManager(ontologyIRI.toString());

        final OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(o);  

        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
	    reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
	    
	    for (int i=0; i<Attributes.size(); i++)
	    {
	    	OWLClass t = df.getOWLClass((String) Attributes.get(i), pm);
        	NodeSet<OWLClass> subclasses = reasoner.getSubClasses(t);
        	Node<OWLClass> equivalenclasses = reasoner.getEquivalentClasses(t);
        	for (OWLClass j : subclasses.getFlattened()) {
        		relevantAttributes.add((j.getIRI().getShortForm()).toString());
  			}
        	for (OWLClass k : equivalenclasses) {
        		relevantAttributes.add((k.getIRI().getShortForm()).toString());
    		}
        }
        
        LinkedHashSet<String> lhSet = new LinkedHashSet<String>(relevantAttributes);		// Since the Set does not allow duplicate elements, only unique elements will be added to it.
        relevantAttributes.clear();        //clear the vector
        relevantAttributes.addAll(lhSet);         //add all unique elements back to the vector  
        relevantAttributes.removeElement("Nothing"); ///Nothing concept is a subclass of all concepts; therefore, it has to be removed from the results 
	    
        for (int l=0;l<relevantAttributes.size();l++){ 
        	System.out.println(relevantAttributes.get(l));
        }
        
        
		return relevantAttributes;
		
	}
//////////////////////////////////////////////////////////////////////////////////////////// Finding if (A AND B) means C or not

public static Vector<String> findANDattributes(Vector Attributes) throws OWLOntologyCreationException{
		
        Vector<String> andAttributes = new Vector<String>();
        
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        File file = new File("src/OSN.owl");
    	OWLOntology o = man.loadOntologyFromOntologyDocument(file);
    	OWLDataFactory df = man.getOWLDataFactory();	
        IRI ontologyIRI = IRI.create("http://www.semanticweb.org/mackbook/ontologies/2021/1/untitled-ontology-34#");
        PrefixManager pm = new DefaultPrefixManager(ontologyIRI.toString());
        
        OWLNamedIndividual alice = df.getOWLNamedIndividual("Alice", pm);

        for (int i=0; i<Attributes.size(); i++) {
        OWLClass attribute = df.getOWLClass(Attributes.get(i).toString(), pm);
        OWLClassAssertionAxiom classAssertion = df.getOWLClassAssertionAxiom(attribute, alice); 
        AddAxiom addAxiom1 = new AddAxiom(o, classAssertion);
        man.applyChange(addAxiom1);
        }
        
        final OpenlletReasoner reasoner = OpenlletReasonerFactory.getInstance().createReasoner(o); 
        reasoner.precomputeInferences(InferenceType.values());

       
	    reasoner.getKB().realize();

        NodeSet<OWLClass> Inferredclasses = reasoner.getTypes(alice);
       
        for (OWLClass k : Inferredclasses.getFlattened()) {
        	andAttributes.add((k.getIRI().getShortForm()).toString());
			}
        
        ////////////////// Inferred classes contains the superclasses and equivalent classes of the submitted attributes A and B in (A AND B), whereas we do not need them
        for (int i=0; i<Attributes.size(); i++) {
            OWLClass attribute = df.getOWLClass(Attributes.get(i).toString(), pm);
            NodeSet<OWLClass> superClasses = reasoner.getSuperClasses(attribute);
            Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(attribute);
            for (OWLClass sc : superClasses.getFlattened()) {
        	andAttributes.removeElement((sc.getIRI().getShortForm()).toString());
  			}
        	for (OWLClass ec : equivalentClasses) {
        		andAttributes.removeElement((ec.getIRI().getShortForm()).toString());
    		}
        }
        
      System.out.println("attributes are: " + andAttributes);
      System.out.println("Size of result is: " + andAttributes.size());

    //  man.saveOntology(o);
		return andAttributes;
}
	
////////////////////////////////////////////////////////////////////////////////////////////	
}
