package tp.rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

import tp.model.Animal;
import tp.model.AnimalNotFoundException;
import tp.model.Cage;
import tp.model.Center;
import tp.model.Position;

@WebServiceProvider
@ServiceMode(value = Service.Mode.MESSAGE)
public class MyServiceTP implements Provider<Source> {

    public final static String url = "http://127.0.0.1:8084/";

    public static void main(String args[]) {
        Endpoint e = Endpoint.create(HTTPBinding.HTTP_BINDING, new MyServiceTP());

        e.publish(url);
        System.out.println("Service started, listening on " + url);
        // pour arrÃªter : e.stop();
    }

    private JAXBContext jc;

    @javax.annotation.Resource(type = Object.class)
    protected WebServiceContext wsContext;

    private Center center = new Center(new LinkedList<>(), new Position(49.30494d, 1.2170602d), "Biotropica");

    public MyServiceTP() {
        try {
            jc = JAXBContext.newInstance(Center.class, Cage.class, Animal.class, Position.class);
        } catch (JAXBException je) {
            System.out.println("Exception " + je);
            throw new WebServiceException("Cannot create JAXBContext", je);
        }

        // Fill our center with some animals
        Cage usa = new Cage(
                "usa",
                new Position(49.305d, 1.2157357d),
                25,
                new LinkedList<>(Arrays.asList(
                        new Animal("Tic", "usa", "Chipmunk", UUID.randomUUID()),
                        new Animal("Tac", "usa", "Chipmunk", UUID.randomUUID())
                ))
        );

        Cage amazon = new Cage(
                "amazon",
                new Position(49.305142d, 1.2154067d),
                15,
                new LinkedList<>(Arrays.asList(
                        new Animal("Canine", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Incisive", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Molaire", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("De lait", "amazon", "Piranha", UUID.randomUUID())
                ))
        );
        Cage rouen = new Cage(
                "Rouen",
                new Position(49.443889,1.103333),
                30,
                new LinkedList<>()
        );

        Cage paris = new Cage(
                "Paris",
                new Position(48.856578,2.351828),
                100,
                new LinkedList<>()
        );

        Cage somalie = new Cage(
                "Somalie",
                new Position(2.333333,48.85),
                50,
                new LinkedList<>()
        );

        Cage bihorel = new Cage(
                "Bihorel",
                new Position(49.455278,1.116944),
                30,
                new LinkedList<>()
        );

        Cage london = new Cage(
                "Londres",
                new Position(51.504872,-0.07857),
                100,
                new LinkedList<>()
        );

        Cage canada = new Cage(
                "Canada",
                new Position(43.2,-80.38333),
                100,
                new LinkedList<>()
        );

        Cage porto = new Cage(
                "Porto-Vecchio",
                new Position(41.5895241,9.2627),
                40,
                new LinkedList<>()
        );

        Cage mont = new Cage(
                "Montreux",
                new Position(46.4307133,6.9113575),
                30,
                new LinkedList<>()
        );
        Cage villier = new Cage(
                "Villers-Bocage",
                new Position(50.0218,2.3261),
                30,
                new LinkedList<>()
        );
        center.getCages().addAll(Arrays.asList(usa, amazon, rouen, paris, somalie, bihorel, canada, porto, villier, mont, london));
        
    }

    public Source invoke(Source source) {
        MessageContext mc = wsContext.getMessageContext();
        String path = (String) mc.get(MessageContext.PATH_INFO);
        String method = (String) mc.get(MessageContext.HTTP_REQUEST_METHOD);

        // determine the targeted ressource of the call
        try {
            // no target, throw a 404 exception.
            if (path == null) {
                throw new HTTPException(404);
            }
            // "/animals" target - Redirect to the method in charge of managing this sort of call.
            else if (path.startsWith("animals")) {
                String[] path_parts = path.split("/");
                switch (path_parts.length){
                    case 1 :
                        return this.animalsCrud(method, source);
                    case 2 :
                        return this.animalCrud(method, source, path_parts[1]);
                    case 3 :
                    	return this.supprime_animal_crud(path_parts[2]);
                    default:
                        throw new HTTPException(404);
                }
            }
            
            //find
            else if (path.startsWith("find")) {
           	 String[] path_parts = path.split("/");
                if (path_parts.length == 3){
               	 switch (path_parts[1]){
                    case "byName" :
                   	 return this.find_animal_name(method, source, path_parts[2]);
                    case "at" :
                        return this.find_animal_at(method, source, path_parts[2]);
                    case "near" :
                        return this.find_animal_near(method, source, path_parts[2]);
                    default:
                        throw new HTTPException(404);
                }
               	 
                }else
               	 throw new HTTPException(503);
                               
           }
            //center
            else if(path.startsWith("center/journey/from")) {
            	String[] path_parts = path.split("/");
            	switch (path_parts.length) {
				case 4:
	            		return this.animalsCrud(method, source);
				default:
					throw new HTTPException(404);
				}
            }
            else if ("coffee".equals(path)) {
                throw new HTTPException(418);
            }
            else {
                throw new HTTPException(404);
            }
        } catch (JAXBException e) {
            throw new HTTPException(500);
        }
    }

    /**
     * Method bound to calls on /animals/{something}
     */
    
    private Source animalCrud(String method, Source source, String animal_id) throws JAXBException {
        if("GET".equals(method)){
            try {
                return new JAXBSource(this.jc, center.findAnimalById(UUID.fromString(animal_id)));
            } catch (AnimalNotFoundException e) {
                throw new HTTPException(404);
            }
        }
        else if("POST".equals(method)){
            Animal animal = unmarshalAnimal(source);
            this.center.getCages()
                    .stream()
                    .filter(cage -> cage.getName().equals(animal.getCage()))
                    .findFirst()
                    .orElseThrow(() -> new HTTPException(404))
                    .getResidents()
                    .add(animal);
            return new JAXBSource(this.jc, this.center);
        }
        /*supprimer l'animal par leur Id*/
        else if("DELETE".equals(method)){
        	/* recuperer toutes les cages */
        	Collection<Cage> cages = this.center.getCages();
            Cage cage;
            Collection<Animal> collection_animals;
            Iterator<Cage> iter_cage = cages.iterator();
            Iterator<Animal> iter_animal;
            
           /*parcourir la collection des cage*/
            while(iter_cage.hasNext()){
            	cage = iter_cage.next();
            	collection_animals = cage.getResidents();
            	iter_animal = collection_animals.iterator();
            	/*parcourir la listes des animaux par cage jusqu'a trouvé 
            	 * l'animal rechercher (à supprimer)*/
            	while(iter_animal.hasNext()){
            		Animal animal= iter_animal.next();
            	
            		if(animal.getId().equals(UUID.fromString(animal_id))){
            			collection_animals.remove(animal);
            		}
            	}
            }
            return new JAXBSource(this.jc, this.center);
        }
        else if("PUT".equals(method)){
        	/* recuperer les information a mettre a jour selon l'id de l'animal à modifier*/
        	Animal animal_put = unmarshalAnimal(source);
        	
        	/* recuperer toutes les cages */
            Collection<Cage> collection_cages = this.center.getCages();
             Cage cage;
             Collection<Animal> collection_animals;
             Iterator<Cage> iter_cages = collection_cages.iterator();
             Iterator<Animal> iter_animals;
             
             /*parcourir chaque cage*/
             while(iter_cages.hasNext()){
             	cage = iter_cages.next();
             	collection_animals = cage.getResidents();
             	iter_animals=collection_animals.iterator();
             	
             	/*parcourir les animaux de chaque cage*/
             	while(iter_animals.hasNext()){
             		Animal animal= iter_animals.next();
             		/*si l'animal recherché est retrouvé par son Id alors le modifier:*/
             		if(animal.getId().equals(UUID.fromString(animal_id))){
             			/**/
             			animal.setCage(animal_put.getCage());
             			animal.setName(animal_put.getName());
             			animal.setSpecies(animal_put.getSpecies());
             		}
             	}
             }
             return new JAXBSource(this.jc, this.center);
         }
        else{
            throw new HTTPException(405);
        }
        
           
        }

    /**
     * Method bound to calls on /animals
     */
    private Source animalsCrud(String method, Source source) throws JAXBException {
        if("GET".equals(method)){
            return new JAXBSource(this.jc, this.center);
        }
        else if("POST".equals(method)){
        	//System.out.println("je suis lààà");
            Animal animal = unmarshalAnimal(source);
            this.center.getCages()
                    .stream()
                    .filter(cage -> cage.getName().equals(animal.getCage()))
                    .findFirst()
                    .orElseThrow(() -> new HTTPException(404))
                    .getResidents()
                    .add(animal);
            return new JAXBSource(this.jc, this.center);
        }
        /*supprimer tout les animaux dans toutes les cages*/
        else if("DELETE".equals(method)){
            System.out.println("!!supprimer ou non !!!");
            /* recuperer toutes les cages */
            	System.out.println("rmimez");
           	 Collection<Cage> collection_cages = this.center.getCages();
                Iterator<Cage> iter_cages = collection_cages.iterator();
                /*parcourir chaque cage*/
                while(iter_cages.hasNext()){
                    Cage cage = iter_cages.next();
                    /*supprimer tout les animaux*/
                    cage.getResidents().removeAll(cage.getResidents());
                }
    
            return new JAXBSource(this.jc, this.center);
        }else if("PUT".equals(method)){
        	/*recuperer l'animal envoyé dans source*/
            Animal animal = unmarshalAnimal(source);
            /* recuperer toutes les cages */
            Collection<Cage> collection_cages = this.center.getCages();
            Iterator<Cage> iter_cages = collection_cages.iterator();
            while(iter_cages.hasNext()){
                Cage cage = iter_cages.next();
                if(cage.getName().equals(animal.getCage())){
                    for(Animal a: cage.getResidents()){
                    	/*modifier tout les animaux*/
                        a.setName(animal.getName());
                        a.setCage(animal.getCage());
                        a.setSpecies(animal.getSpecies());
                    }
                }
            }
            return new JAXBSource(this.jc, this.center);
        }
        else{
            throw new HTTPException(405);
        }
    }
    
    /*fonction permet de supprimer tout les animaux d'une cage donné */
    private Source supprime_animal_crud(String ville) throws JAXBException {

           	 Collection<Cage> collection_cages = this.center.getCages();
                Iterator<Cage> iter_cages = collection_cages.iterator();
                /*parcourir chaque cage*/
                while(iter_cages.hasNext()){
                    Cage cage = iter_cages.next();
                    /*supprimer tout les animaux*/
                    if(cage.getName().equals(ville))
                    cage.getResidents().removeAll(cage.getResidents());   
                }
            return new JAXBSource(this.jc, this.center);
    } 
   
    /*find
     * 
     */
    /*trouver les animaux par leurs nom /find/{name} */
    private Source find_animal_name(String method, Source source, String animal_name) throws JAXBException {
        if("GET".equals(method)){
            try {
                return new JAXBSource(this.jc, center.findAnimalByName(animal_name));
            } catch (AnimalNotFoundException e) {
                throw new HTTPException(404);
            }
        }
        
        else{
            throw new HTTPException(405);
        }
    }
    
 
    /*trouver les animaux a une position donné /find/at/{latitude}{longitude}*/
private Source find_animal_at(String method, Source source, String animal_name) throws JAXBException {
        if("GET".equals(method)){
            try {
                return new JAXBSource(this.jc, center.findAnimalByPosition(animal_name));
            } catch (AnimalNotFoundException e) {
                throw new HTTPException(404);
            }
        }
        else{
            throw new HTTPException(405);
        }
    }


/*trouver les animaux proche a une position donné /find/near/{latitude}{longitude}*/
private Source find_animal_near(String method, Source source, String animal_name) throws JAXBException {
    if("GET".equals(method)){
        try {
            return new JAXBSource(this.jc, center.findAnimalNearPosition(animal_name));
        } catch (AnimalNotFoundException e) {
            throw new HTTPException(404);
        }
    }
    else{
        throw new HTTPException(405);
    }
}

    private Animal unmarshalAnimal(Source source) throws JAXBException {
        return (Animal) this.jc.createUnmarshaller().unmarshal(source);
    }
}
