# TpArchitectureDistribue-
# Le premier projet part1TpArchi contient la class REST.java.
-on lance la Class : 
-sur un navigateur on peut accecder aux liens suivant
	-http://127.0.0.1:8084/hello/world
	-http://127.0.0.1:8089/test
	-http://127.0.0.1:8090/hello/world


***************************************************************************
# la deuxieme projet test contient le modele fornie et le package
rest qui contient les deux classe MyServiceTP.java et MyClient.java
MyClient.java:
	-cette classe contient toute les methodes qui permettent a un utilisateur
	de faire une requete "GET" "POST" "DELETE" "PUT"
	    //ajouter un animal, elle prend animal comme parametre
			-client.Ajouter_animal(new Animal("Bob", "amazon", "Arapaima gigas", UUID.randomUUID()));
        
        
        //supprimer tous les animaux
			-client.supprimer_animal();
        //supprimer un animal par son id
			-client.supprimer_animal_Par_Id(id);
			
        //Modifier un animal en sachant son id
			-client.put_animal_Par_Id(new Animal("mrenk","gunner","kaci",UUID.fromString(id)));
			
        //modifir tous les animaux dans une cage donné par exemple "amazon"
			-client.put_animal_tout(new Animal("mrenk","amazon","kaci",UUID.randomUUID()));
			
        //supprimer les animaux ville / exactement par cage
			-client.supprimer_animal_Par_Ville("usa");
			
        //ajouter un animal avec generation d'un UUID. On a met des valeur static dans la fonction
			-client.ajouter_animal_By_Id(UUID.randomUUID());

	-à l'interieur de chaque methode il specifier la Methode, le path et les parametre a passé pour le service.
	-la methode invoke permet de passé ces information sur le coté service.

MyServiceTP.java
	-cette classe contient l'ensemble de methode qui traite les requetes
	prevenant d'un cleint, 
	-on a gérer ces methodes selon leurs url.
	
	//cette methode definie qautre methodes Http "GET" "POST" "DELETE" "PUT"
	//elle fait le traitement pour un seul animal
	//c-à-d. elle traite les requete de genre "/animals/{somthing}"
		- private Source animalCrud(String method, Source source, String animal_id)
	
	//elle traite les requete de genre "/animals"
		-private Source animalsCrud(String method, Source source)
	
	//c'est la méthode qui permet de recevoir la requete de supression des animaux par ville(cage)"DELETE"
		-private Source supprime_animal_crud(String ville)
		
	//cette methode traite les requete qui contient la methode GET 
	//"animal par son nom" "find/{name}"
		-private Source find_animal_name(String method, Source source, String animal_name)

	//trouvé les animaux à une position donnée, traite la methode GET de la requete
	//find/at/{latitude}{longitude}
		-private Source find_animal_at(String method, Source source, String animal_name)

	//traite la mathode GET de la requete
	//trouver les animaux proche a une position donné /find/near/{latitude}{longitude}*/
		-private Source find_animal_near(String method, Source source, String animal_name)
 ******************************************************************************************
 TEST
Pour les methodes GET de la requete, il suffit de taper le lien correspondant dans le navigateur

-par contre les methode "POST" "DELETE" "PUT", premier lieu faut lancé la classe MYServiceTP.java
-puis dans le main de la classe MYClient.java on fait appel a la méthode volu comme par exemple:
		--client.supprimer_animals();
***************************************************************************************


