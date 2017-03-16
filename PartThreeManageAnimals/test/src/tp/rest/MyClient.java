package tp.rest;

import tp.model.Animal;
import tp.model.Cage;
import tp.model.Center;
import tp.model.Position;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MyClient {
    private Service service;
    private JAXBContext jc;

    private static final QName qname = new QName("", "");
    private static final String url = "http://127.0.0.1:8084";

    public MyClient() {
        try {
            jc = JAXBContext.newInstance(Center.class, Cage.class, Animal.class, Position.class);
        } catch (JAXBException je) {
            System.out.println("Cannot create JAXBContext " + je);
        }
    }

/*ajouter un animal dans une cage*/
    public void Ajouter_animal(Animal animal) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
        Source result = dispatcher.invoke(new JAXBSource(jc, animal));
    printSource(result);
}
    /*spprimer tous les animeaux*/
    public void supprimer_animals() throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "DELETE");
        Source result = dispatcher.invoke(null);
        printSource(result);
    }
    /*modifier un animal par son id*/
    public void put_animal_Par_Id(Animal animal) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals/"+animal.getId().toString());
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
        Source result = dispatcher.invoke(new JAXBSource(jc, animal));
        printSource(result);
    }
    /*modifier tous les animeaux: exemple changer leurs nom*/
    public void put_animal_tout(Animal animal) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals");
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
        Source result = dispatcher.invoke(new JAXBSource(jc,animal));
        printSource(result);
    }
        /*supprimer un animal par son Id*/
    public void supprimer_animal_Par_Id(String id) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals/"+id);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "DELETE");
        Source result = dispatcher.invoke(null);
        printSource(result);
    }
    
    /*supprimer un animal par où s'es situé sa cage(ville)*/
    public void supprimer_animal_Par_Ville(String ville) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals/remove/"+ville);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "DELETE");
        Source result = dispatcher.invoke(null);
        printSource(result);
    }
    
    /*ajouter un Animal en generant un id*/
    public void ajouter_animal_By_Id(UUID id) throws JAXBException {
        service = Service.create(qname);
        service.addPort(qname, HTTPBinding.HTTP_BINDING, url + "/animals/"+id);
        Dispatch<Source> dispatcher = service.createDispatch(qname, Source.class, Service.Mode.MESSAGE);
        Map<String, Object> requestContext = dispatcher.getRequestContext();
        requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
        Source result = dispatcher.invoke(new JAXBSource(jc, new Animal("bla","amazon","bla",id)));
        printSource(result);
    }
    public void printSource(Source s) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(s, new StreamResult(System.out));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) throws Exception {
        MyClient client = new MyClient();
        /*for (int i=0; i<101; i++){
        	client.Ajouter_animal(new Animal("Bob", "amazon", "Arapaima gigas", UUID.randomUUID()));
        }*/
       String id_galago="8fa36c25-9f8f-4d6d-8e95-c55b399138cf";
       UUID iUuid=UUID.fromString(id_galago);
       
      client.supprimer_animals();
       System.out.println("animaux supprimer");
       client.Ajouter_animal(new Animal("pan", "Rouen", "Panda", UUID.randomUUID())); 
       System.out.println("Ajoutez un Panda à Rouen");
       client.Ajouter_animal(new Animal("hoc","Paris","Hocco unicorne",UUID.randomUUID()));
       System.out.println("Ajoutez un Hocco unicorne à Paris");
       client.put_animal_tout(new Animal("lago","Rouen","Lagotriche à queue jaune",UUID.randomUUID()));
       System.out.println("Modifiez l'ensemble des animaux par un Lagotriche à queue jaune à Rouen (Latitude :49.443889 ; Longitude : 1.103333)");
       client.Ajouter_animal(new Animal("oc","Somalie","Océanite de Matsudaira",UUID.randomUUID()));
       System.out.println("Ajoutez une Océanite de Matsudaira en Somalie");
       client.Ajouter_animal(new Animal("ara","Rouen","Ara de Spix",UUID.randomUUID()));
       System.out.println("Ajoutez un Ara de Spix à Rouen");
       client.Ajouter_animal(new Animal("galago","Bihorel","Galago de Rondo",UUID.randomUUID()));
       System.out.println("Ajoutez un Galago de Rondo à Bihorel");
       client.Ajouter_animal(new Animal("pale","Londres","Palette des Sulu",UUID.randomUUID()));
       System.out.println("Ajoutez une Palette des Sulu à Londres");
       client.Ajouter_animal(new Animal("koup","Paris","Kouprey",UUID.randomUUID()));
       System.out.println("Ajoutez un Kouprey à Paris");
       client.Ajouter_animal(new Animal("tuit","Paris","Tuit­tuit",UUID.randomUUID()));
       System.out.println("Ajoutez un Tuittuità Paris");
       client.Ajouter_animal(new Animal("saiga","Canada","Saïga",UUID.randomUUID()));
       System.out.println("Ajoutez une Saïga au Canada");
       client.Ajouter_animal(new Animal("inca","Porto-Vecchio","Inca de Bonaparte",UUID.randomUUID()));
       System.out.println("Ajoutez un Inca de Bonaparte à PortoVecchio");
       client.Ajouter_animal(new Animal("rale","Montreux","Râle de Zapata",UUID.randomUUID()));
       System.out.println("Ajoutez un Râle de Zapata à Montreux");
       client.Ajouter_animal(new Animal("rhino","Villers-Bocage","Rhinocéros de Java",UUID.randomUUID()));
       System.out.println("Ajoutez un Rhinocéros de Java à VillersBocage");
       for (int i=0; i<101; i++){
    	   
   	client.Ajouter_animal(new Animal("dalt"+1, "usa", "Dalmatiens", UUID.randomUUID()));
   }
       System.out.println("Ajoutez 101 Dalmatiens dans une cage aux USA");
       client.supprimer_animal_Par_Ville("Paris");
       System.out.println("Supprimez tous les animaux de Paris");
       client.supprimer_animal_Par_Id(id_galago);
       System.out.println("Supprimez le Galago de Rondo");
        
    }
}
