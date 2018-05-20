package com.apress.spring.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.transaction.annotation.Transactional;

import com.apress.spring.domain.Map;
import com.apress.spring.repository.MapRepository;

@Controller
public class MapController {
	@Autowired
    MapRepository repo_map;
    String name2;
    int count = 0;

    @RequestMapping(value = "/map", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody List<Map> getMap() {
       List<Map> x = repo_map.findAll();
       if(count!= 0) {
    	   count=0;
    	   Map mp = new Map();
    	   mp.setName(name2);
    	   mp.setColor("#666666");
    	   x.add(mp);
       }
       return x;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/poll", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody List<Map> getPoll() {
        return repo_map.findAll();
    }

   
//이 위치에 미완코드(데이터생성: POST) 완성할 것.    
    @RequestMapping(method = RequestMethod.POST, value = "/poll", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody ResponseEntity<Map> createPoll(@RequestBody Map map) throws ParseException {
    	
    	
    	if(map.getName().isEmpty()) {
    		map.setName("#error");
    		return new ResponseEntity<Map>(map, HttpStatus.NOT_ACCEPTABLE);
    	}
    	
	    String name = map.getName();
	    String color = map.getColor();
	       
	    Map m = new Map(name, color);
	    Map m2 = repo_map.save(m);
	
	    return new ResponseEntity<Map>(m2, HttpStatus.OK);
    }

   
//이 위치에 미완코드(데이터수정: PUT) 완성할 것.               
    @RequestMapping(method = RequestMethod.PUT, value = "/poll", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody ResponseEntity<Map> updatePoll(@RequestBody Map map) throws ParseException {
    	String name = map.getName();
    	String color = map.getColor();
       
    	List<Map> temp = repo_map.findByName(name);
    	if(temp != null) {
    		if(name != "") temp.get(0).setName(name);
    		if(color != "") temp.get(0).setColor(color);
    	}
    	repo_map.save(temp.get(0));
       
    	return new ResponseEntity<Map>(temp.get(0), HttpStatus.OK);
    }

   
//이 위치에 미완코드(데이터삭제: DELETE) 완성할 것.               
    @RequestMapping(method = RequestMethod.DELETE, value = "/poll", produces = { MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody ResponseEntity<Void> deletePoll(@RequestBody Map map) throws ParseException {
    	
    	String name = map.getName();
    	name2 = name;
    	count = 1; //flag 
    	
    	List<Map> m = repo_map.findByName(name);
    
    	if(m == null) {
    		return new ResponseEntity<Void> (HttpStatus.NOT_FOUND);
    	}
    	
    	
    	repo_map.deleteInBatch(m);
    	
    	return new ResponseEntity<Void>(HttpStatus.OK);

    }
    
}