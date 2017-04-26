package edu.eci.arsw.msgbroker;

import edu.eci.arsw.msgbroker.model.PersistenceStub;
import edu.eci.arsw.msgbroker.model.Person;
import edu.eci.arsw.msgbroker.model.Sala;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author FamiliaRamirez
 */
@RestController
@RequestMapping(value = "/dibujos")
public class Draw {
    
    @Autowired
    PersistenceStub ps = new PersistenceStub();

    @RequestMapping(path = "/{iddibujo}", method = RequestMethod.GET)
    public ResponseEntity<?> getIdDraw(@PathVariable int iddibujo) {
        try {
            return new ResponseEntity<>(ps.getDatos().get(iddibujo),HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(Draw.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error al agregar colaborador", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/{iddibujo}/colaboradores", method = RequestMethod.POST)
    public ResponseEntity<?> getIdDrawCollaborate(@PathVariable int iddibujo, @RequestBody Person persona) {
        try {
            if(!ps.getDatos().containsKey(iddibujo)){
                ps.getDatos().put(iddibujo, new ArrayList<>());
            }
            ps.getDatos().get(iddibujo).add(persona.getNombre());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error al agregar colaborador a la sala: "+iddibujo, HttpStatus.NOT_FOUND);
        }
    }

}
