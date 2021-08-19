package cpabe;
///////////////////////////////////////////
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/////////////////////////////////////////
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.sun.tools.javac.util.List;

//import cpabe.Cpabe.*;
import ExtendAttributes.ExtendingAnAttribute;
import Semantic.SemanticComponent;
import bswabe.BswabeCph;
import bswabe.BswabePolicy;
import bswabe.Bswabe;


public class mymain {
	
	static String pubfile = "pub_key";
    static String mskfile = "master_key";
    static String prvfile = "prv_key";    
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws Exception {		
		// TODO Auto-generated method stub
		int Algorithm =0;
		String pubkName, mskName, prvkName, inputfilename, policy, updatedPolicy;
		String attr_str3 = null;
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
			//call the original KeyGen algorithm
			Cpabe.keygen(pubkName, prvkName, mskName, attrs);
		    break;
		  }
		  case 3:{
		    System.out.println("Encryption algorithm is selected!");
		    System.out.println("Please enter the name of the Public Key.");
		    pubkName = scanKeyGen.nextLine();  // Read user input
		    System.out.println("Please enter the name of the file that you are going to encrypt it.");
		    inputfilename = scanKeyGen.nextLine();  // Read user input
		    System.out.println("Please specify your intedned access policy/structure.");
		    policy = scanKeyGen.nextLine();  // Read user input
		    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		    //Semantic Encryption
		    Vector<String> updated = new Vector<String>();	
	    	Vector<String> submitted = new Vector<String>();
	    	Vector<String> received = new Vector<String>();
	    	Vector<String> receivedAND = new Vector<String>();
	        Vector<String> tokens = new Vector<String>();
	        StringTokenizer tokenizer = new StringTokenizer(policy, " ");
	        while (tokenizer.hasMoreElements()) {
	            tokens.add(tokenizer.nextToken());
	        }
	        for(int n=0; n<tokens.size();n++) {
	        	String token =tokens.get(n); 
	        	if(token.equals("2of2")) {
	        		if(!tokens.get(n-2).equals("1of2") && !tokens.get(n-1).equals("1of2"))
	        		{
	        			//System.out.println("Find the atrributes relevant to "+ tokens.get(n-2) + " and then replace the attribute with OR of all the relevant ones");
	        			submitted.add(tokens.get(n-2));
        				received.addAll(SemanticComponent.findRelevantAttributes(submitted)); 
	        			received.add("1of"+received.size());
	        			submitted.clear();
	        			
	        			//System.out.println("Find the atrributes relevant to "+ tokens.get(n-1)+ " and then replace the attribute with OR of all the relevant ones");
	        			//add AND (2of2)
	        			submitted.add(tokens.get(n-1));
        				received.addAll(SemanticComponent.findRelevantAttributes(submitted)); 
	        			received.add("1of"+received.size());
	        			submitted.clear();
	        			received.add("2of2");
	        			
	        			//System.out.println("Find the atrributes relevant to "+ tokens.get(n-2) + " AND " + tokens.get(n-1) + " and then Add an OR after this AND");
	        			submitted.add(tokens.get(n-2));
	        			submitted.add(tokens.get(n-1));
	        			receivedAND.addAll(SemanticComponent.findANDattributes(submitted));
	        			if(receivedAND.size()>0){
	        				received.addAll(receivedAND);
	        				received.add("1of"+(receivedAND.size()+1));
	        				receivedAND.clear();
	        			}
	        			//add OR the inferred if any
	        		}else
	        		if(tokens.get(n-1).equals("1of2") | tokens.get(n-2).equals("1of2"))
		        	{	
	        			if(tokens.get(n-1).equals("1of2") && !tokens.get(n-4).equals("1of2")) {
	        				//System.out.println("Find the atrributes relevant to "+ tokens.get(n-4) + " and replace that with an OR of all relevant ones");
	        				submitted.add(tokens.get(n-4));
	        				received.addAll(SemanticComponent.findRelevantAttributes(submitted)); 
		        			received.add("1of"+received.size());
		        			submitted.clear();
	        				
	        				//System.out.println("Find the atrributes relevant to "+ tokens.get(n-4) + " AND " + tokens.get(n-3) + " and add an OR after the AND");
		        			submitted.add(tokens.get(n-4));
		        			submitted.add(tokens.get(n-3));
		        			receivedAND.addAll(SemanticComponent.findANDattributes(submitted));
		        			if(receivedAND.size()>0){
		        			//	System.out.println("Received AND is: "+ receivedAND);
		        				received.add("2of2");
		        				received.addAll(receivedAND);
		        				received.add("1of"+(receivedAND.size()+1));
		        				receivedAND.clear();
		        			}
		        			submitted.clear();
		        			
	        				//System.out.println("Find the atrributes relevant to "+ tokens.get(n-4) + " AND " + tokens.get(n-2) + " and add an OR after the AND");
		        			submitted.add(tokens.get(n-4));
		        			submitted.add(tokens.get(n-2));
		        			receivedAND.addAll(SemanticComponent.findANDattributes(submitted));
		        			if(receivedAND.size()>0){
		        				//System.out.println("Received AND is: "+ receivedAND);
		        				received.add("2of2");
		        				received.addAll(receivedAND);
		        				received.add("1of"+(receivedAND.size()+1));
		        				receivedAND.clear();
		        			}
		        			
	        			}else
	        			if(!tokens.get(n-1).equals("1of2")) {
		        			//System.out.println("Find the atrributes relevant to "+ tokens.get(n-1) + " and replace that with an OR of all relevant ones");
	        				submitted.add(tokens.get(n-1));
	        				received.addAll(SemanticComponent.findRelevantAttributes(submitted));
	        				if(received.size()>0)
	        				{
	        					received.add("1of"+received.size());
	        				}else{
	        					received.addAll(submitted);
	        					received.add("2of2");
	        					}
		        			submitted.clear();
	        				
		        			//System.out.println("Find the atrributes relevant to "+ tokens.get(n-4) + " AND " + tokens.get(n-1) + " and add an OR after the AND");
		        			receivedAND.clear();
		        			submitted.add(tokens.get(n-4));
		        			submitted.add(tokens.get(n-1));
		        			receivedAND.addAll(SemanticComponent.findANDattributes(submitted));
		        			if(receivedAND.size() > 0){
		        				received.add("2of2");
		        				received.addAll(receivedAND);
		        				received.add("1of"+(receivedAND.size()+1));
		        				receivedAND.clear();
		        			}
		        			submitted.clear();

	        				//System.out.println("Find the atrributes relevant to "+ tokens.get(n-3) + " AND " + tokens.get(n-1) + " and add an OR after the AND");
	        				submitted.add(tokens.get(n-3));
		        			submitted.add(tokens.get(n-1));
		        			receivedAND.addAll(SemanticComponent.findANDattributes(submitted));
		        			if(receivedAND.size()>0){
		        				received.add("2of2");
		        				received.addAll(receivedAND);
		        				received.add("1of"+(receivedAND.size()+1));
		        				receivedAND.clear();
		        			}

		        		}
	        			else
	        			if(tokens.get(n-1).equals("1of2") && tokens.get(n-4).equals("1of2")) {
	        				//System.out.println("Find the atrributes relevant to "+ tokens.get(n-6) + " AND " + tokens.get(n-3) + " and add an OR after the AND");
	        				submitted.add(tokens.get(n-6));
		        			submitted.add(tokens.get(n-3));
		        			receivedAND.addAll(SemanticComponent.findANDattributes(submitted));
		        			if(receivedAND.size()>0){
		        				received.add("2of2");
		        				received.addAll(receivedAND);
		        				received.add("1of"+(receivedAND.size()+1));
		        				receivedAND.clear();
		        			}
		        			submitted.clear();

	        				//System.out.println("Find the atrributes relevant to "+ tokens.get(n-6) + " AND " + tokens.get(n-2) + " and add an OR after the AND");
		        			submitted.add(tokens.get(n-6));
		        			submitted.add(tokens.get(n-2));
		        			receivedAND.addAll(SemanticComponent.findANDattributes(submitted));
		        			if(receivedAND.size()>0){
		        				received.add("2of2");
		        				received.addAll(receivedAND);
		        				received.add("1of"+(receivedAND.size()+1));
		        				receivedAND.clear();
		        			}
		        			submitted.clear();

	        				//System.out.println("Find the atrributes relevant to "+ tokens.get(n-5) + " AND " + tokens.get(n-3) + " and add an OR after the AND");
		        			submitted.add(tokens.get(n-5));
		        			submitted.add(tokens.get(n-3));
		        			receivedAND.addAll(SemanticComponent.findANDattributes(submitted));
		        			if(receivedAND.size()>0){
		        				received.addAll(receivedAND);
		        				received.add("1of"+(receivedAND.size()+1));
		        				receivedAND.clear();
		        			}
		        			submitted.clear();
	        				
	        				//System.out.println("Find the atrributes relevant to "+ tokens.get(n-5) + " AND " + tokens.get(n-2) + " and add an OR after the AND");
		        			submitted.add(tokens.get(n-5));
		        			submitted.add(tokens.get(n-2));
		        			receivedAND.addAll(SemanticComponent.findANDattributes(submitted));
		        			if(receivedAND.size()>0){
		        				received.addAll(receivedAND);
		        				received.add("1of"+(receivedAND.size()+1));
		        				receivedAND.clear();
		        			}

	        			}
		        	}
	        	}else
	        	
	        	if(token.equals("1of2")) {
	        		if(!tokens.get(n-2).equals("2of2") && !tokens.get(n-1).equals("2of2"))
	        		{
	        			//System.out.println("Find the atrributes relevant to "+ tokens.get(n-2) + " and "+ tokens.get(n-1));
	        			//System.out.println("Remove the repeated attributes");
	        			submitted.add(tokens.get(n-1));
	        			submitted.add(tokens.get(n-2));
	        		 	received.addAll(SemanticComponent.findRelevantAttributes(submitted)); 
	        			received.add("1of"+received.size());
	        			//System.out.println("replace the OR with a new OR composed of all the infered attributes 1ofX" );
	        		}else
	        		
	        		if(tokens.get(n-2).equals("2of2") | tokens.get(n-1).equals("2of2"))
	        		{
	        			if(tokens.get(n-1).equals("2of2") && !tokens.get(n-4).equals("2of2")) {
	        				//System.out.println("Find the atrributes relevant to "+ tokens.get(n-4) + " and replace that with an OR of all relevant ones");
	        				submitted.add(tokens.get(n-4));
	        				received.addAll(SemanticComponent.findRelevantAttributes(submitted)); 
		        			received.add("1of"+received.size());
	        			}else
	        			if(!tokens.get(n-1).equals("2of2")) {
		        			//System.out.println("Find the atrributes relevant to "+ tokens.get(n-1) + " and replace that with an OR of all relevant ones");
		        			submitted.add(tokens.get(n-1));
	        				received.addAll(SemanticComponent.findRelevantAttributes(submitted)); 
		        			received.add("1of"+received.size());
		        		}	
	        		}
	        		
	        	}
	        	
	        	updated.addAll(received);	     //COPY RECEIVED TO A NEW VECTOR
	        	received.clear();	        	//REMOVE RECEIVED    
	        	submitted.clear();
		}

	        
	    	//System.out.println("Final updated policy is " + updated );
	   //// The expected Policy is a string. So, the vector of extended policy is converted to a single string value 
	   			StringBuilder builderpolicy = new StringBuilder();
	   			for(int k=0; k < updated.size(); k++){
	   			    builderpolicy.append(updated.get(k));
	   			    builderpolicy.append(" "); 
	   			}
	   			
	   			updatedPolicy = builderpolicy.toString();
	   			System.out.println("The updated policy that will be sent to the encryption algorithm is " + updatedPolicy);
		    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	   	    //call Encryption method
	   			Cpabe.enc(pubkName, updatedPolicy, inputfilename);
		    break;
		  }
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