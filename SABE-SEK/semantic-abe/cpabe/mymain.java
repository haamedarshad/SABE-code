package cpabe;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Vector;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import ExtendAttributes.ExtendingAnAttribute;



public class mymain {
	
	static String pubfile = "pub_key";
    static String mskfile = "master_key";
    static String prvfile = "prv_key";
   /////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws Exception {		
		int Algorithm =0;
		String pubkName, mskName, prvkName, inputfilename, policy;
	    Scanner scanKeyGen = new Scanner(System.in);
		while(Algorithm ==0)
		{
		System.out.println("Please enter the number of one of the following algorithms:");
		System.out.println("1: Setup");
		System.out.println("2: KeyGen");
		System.out.println("3: Encryption");
		System.out.println("4: Decryption");
		Scanner algorithmscanner = new Scanner(System.in);  // Create a Scanner object
	    Algorithm = algorithmscanner.nextInt();  // Read user input
		
		switch (Algorithm) {
/////////////////////////////////////////////
		  case 1:{
		    System.out.println("Setup algorithm is selected!");
		    Cpabe.setup(pubfile, mskfile);
		    break;
		  }
/////////////////////////////////////////////
		  case 2:{ 
			  
		    System.out.println("KeyGen algorithm is selected!");
		    System.out.println("Please enter the name of the Public Key.");
		    pubkName = scanKeyGen.nextLine();  // Read user input
		    System.out.println("Please enter the name of the Master Secret Key.");
		    mskName= scanKeyGen.nextLine();  // Read user input
		    System.out.println("Please enter a name for your Private Key (the output).");
		    prvkName= scanKeyGen.nextLine();  // Read user input
			System.out.println("Please enter your attributes. All on one line with a space between them. Press the ENTER key when you are done!");
			//read a line from user 
			String attrs = scanKeyGen.nextLine();
			//we'd like to split the line into words, so we create a
			//scanner to read from the line
			Scanner linesc = new Scanner (attrs);
			Vector attr_str = new Vector();
			Vector<String> attr_str2 = new Vector<String>();
			while (linesc.hasNext()) {
			    String nextWord = linesc.next(); 
			    attr_str.add(nextWord);
			}
			attr_str2.addAll(ExtendingAnAttribute.shouldCreatePropertyAssertions(attr_str));
			///////////////
			// Since the Set does not allow duplicate elements, only unique elements will be added to it.	         
	        LinkedHashSet<String> lhSet = new LinkedHashSet<String>(attr_str2);
	        //clear the vector
	        attr_str2.clear();
	        //add all unique elements back to the vector
	        attr_str2.addAll(lhSet);
			///////////////			
			for(int j=0; j<attr_str2.size(); j++)
				System.out.println("Extended attribute is " + attr_str2.get(j));			
			//// The expected input of the original KeyGen algorithm is a string of attributes. So, the vector of extended attributes is converted to a single string value 
			StringBuilder builder = new StringBuilder();
			for(int k=0; k < attr_str2.size(); k++){
			    builder.append(attr_str2.get(k));
			    builder.append(" "); 
			}
			String attr_str3 = builder.toString();
			/////////////////////
			//call the original KeyGen algorithm
			Cpabe.keygen(pubkName, prvkName, mskName, attr_str3);
		    ///////////////////////////////////////////////////////////////
		    break;
		  }
/////////////////////////////////////////////
		  case 3:{
		    System.out.println("Encryption algorithm is selected!");
		    
		    System.out.println("Please enter the name of the Public Key.");
		    pubkName = scanKeyGen.nextLine();  // Read user input
		    
		    System.out.println("Please enter the name of the file that you are going to encrypt it.");
		    inputfilename = scanKeyGen.nextLine();  // Read user input
		  
		    System.out.println("Please specify your intedned access policy/structure.");
		    policy = scanKeyGen.nextLine();  // Read user input
		    
		    //call Encryption method
		    Cpabe.enc(pubkName, policy, inputfilename);
		    
		    /*Attributes: "baf", "fim1", "fim", "foo"

		    Policy encodes a postorder traversal of threshold tree. For "foo bar fim 2of3 baf 1of2" we get two threshold gates and four leaves.

		    Examples:

		    "fim bar baf 2of3 foo 1of2" 
		    "foo bar baf 2of3 fim 1of2"
		    */
		    break;
		  }
/////////////////////////////////////////////
		  case 4:{
		    System.out.println("Decryption algorithm is selected!");

		    System.out.println("Please enter the name of the Public Key.");
		    pubkName = scanKeyGen.nextLine();  // Read user input
		    
		    System.out.println("Please enter the name of the file that you are going to decrypt it.");
		    inputfilename = scanKeyGen.nextLine();  // Read user input
		  
		    System.out.println("Please enter the name of your private key.");
		    prvkName = scanKeyGen.nextLine();  // Read user input
		    
		    Cpabe.dec(pubkName, prvkName, inputfilename);
		    break;
		  }
		  default:{
			    System.out.println("You have enetered a wrong number!");
			    Algorithm = 0;
			    }
		}
		}	
	}
}