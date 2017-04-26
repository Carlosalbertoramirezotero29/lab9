/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.msgbroker;

import edu.eci.arsw.msgbroker.model.Point;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 *
 * @author FamiliaRamirez
 */
@Controller
public class STOMPMessagesHandler {

    @Autowired
    SimpMessagingTemplate msgt;

    static Logger logger = Logger.getLogger(STOMPMessagesHandler.class);

    CopyOnWriteArrayList<Point> threadSafeListtopic = new CopyOnWriteArrayList<>();
    ConcurrentHashMap<Integer,CopyOnWriteArrayList<Point>> threadSafeList = new ConcurrentHashMap<>();

    @MessageMapping("/newpoint")
    public void getLine(Point pt) throws Exception {
        threadSafeListtopic.add(pt);
        if (threadSafeList.size() == 4) {
            msgt.convertAndSend("/topic/newpoint", pt);
            msgt.convertAndSend("/topic/newpolygon", threadSafeListtopic);
            threadSafeList.clear();
        } else {
            msgt.convertAndSend("/topic/newpoint", pt);
        }

    }

    @MessageMapping("newpolygon.{idDraw}")
    public void handleBaz(Point pt, @DestinationVariable int idDraw) {
        System.out.println("Llego algo ");
        if (!threadSafeList.containsKey(idDraw)) {
            threadSafeList.put(idDraw, new CopyOnWriteArrayList<>());
        }
        threadSafeList.get(idDraw).add(pt);
        if (threadSafeList.get(idDraw).size() == 4) {
            msgt.convertAndSend("/topic/newpoint." + idDraw, pt);
            msgt.convertAndSend("/topic/newpolygon." + idDraw, threadSafeList.get(idDraw));
            threadSafeList.clear();
        } else {
            msgt.convertAndSend("/topic/newpoint." + idDraw, pt);
        }

    }

}
