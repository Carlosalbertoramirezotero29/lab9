/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.msgbroker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author FamiliaRamirez
 */
@Service
public class PersistenceStub {
    
    private static final Map<Integer,ArrayList<String>> datos = new HashMap<>();
    
    public PersistenceStub(){
    
    }

    public Map<Integer, ArrayList<String>> getDatos() {
        return datos;
    }
    
}
