package tp.model;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.ws.http.HTTPException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

@XmlRootElement
public class Center {

    Collection<Cage> cages;
    Position position;
    String name;

    public Center() {
        cages = new LinkedList<>();
    }

    public Center(Collection<Cage> cages, Position position, String name) {
        this.cages = cages;
        this.position = position;
        this.name = name;
    }

    public Animal findAnimalById(UUID uuid) throws AnimalNotFoundException {
        return this.cages.stream()
                .map(Cage::getResidents)
                .flatMap(Collection::stream)
                .filter(animal -> uuid.equals(animal.getId()))
                .findFirst()
                .orElseThrow(AnimalNotFoundException::new);
    }
    
    // said
   
public Animal findAnimalByName(String name) throws AnimalNotFoundException {
        return this.cages.stream()
                .map(Cage::getResidents)
                .flatMap(Collection::stream)
                .filter(animal -> name.equals(animal.getName()))
                .findFirst()
                .orElseThrow(AnimalNotFoundException::new);
    }
    
    public Animal findAnimalByPosition(String position) throws AnimalNotFoundException {
        return this.cages
                .stream()
                .filter(center -> position.equals(center.getPosition().getLatitude()+"+"+center.getPosition().getLongitude()))
                .map(Cage::getResidents)
                .flatMap(Collection::stream)
                .findFirst()
                .orElseThrow(AnimalNotFoundException::new);             
    }
    
    public Cage findAnimalNearPosition(String position) throws AnimalNotFoundException {
        return this.cages
                .stream()
                .filter(center -> position.equals(center.getPosition().getLatitude()+"+"+center.getPosition().getLongitude()))
                .findAny()
                .get();
    }
    //said
    

    public Collection<Cage> getCages() {
        return cages;
    }

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void setCages(Collection<Cage> cages) {
        this.cages = cages;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }
}
