# This folder contains A runnable jar file of the prototype

* In order to execute the prototype in the command prompt you may use the following command:
	* java -jar SemanticABE.jar

* It is worth mentioning that you need to have the folder **src** and the **UniOnto.owl** file next to the jar file.  

* Please note that if you want to encrypt a file, you have to specify the access policy/structure as follows:

The policy that you have to enter | Meaning
------------ | -------------
PhD_Student Employee 1of2 | (PhD_Student OR Employee)
PhD_Student Employee 1of2 Academic 2of2 | ((PhD_Student OR Employee) AND Academic)

