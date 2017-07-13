package com.example.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.model.Bom;
import com.example.service.BomService;

@RestController
@RequestMapping("/bom")
public class BomController {
   static Logger logger = Logger.getLogger(BomController.class);
   @Autowired
   private BomService service;
   
   @RequestMapping( method = RequestMethod.GET, produces = "application/json" )
   public ResponseEntity<List<Bom>> getBoms(){
      logger.info("GET: /bom,method = getBoms");
       return new ResponseEntity(service.getBom(), HttpStatus.OK);
   }
   
   /**
    * @return Returns list of bom after POST call
    */
   @RequestMapping(method = RequestMethod.POST,produces = "application/json", consumes = "application/json" )
   public ResponseEntity<Bom> postBom(@RequestBody Bom bom ) {
      logger.info("POST: /bom,method = postBom");
      service.createBom(bom);
        return new ResponseEntity(bom, HttpStatus.CREATED);
   }
   
   @RequestMapping( path = "/{uuid}", method = RequestMethod.GET, produces = "application/json" )
   public ResponseEntity<Bom> getBomById(@PathVariable String uuid){
       if(!service.exitsBom(uuid)){
           return new ResponseEntity(HttpStatus.NOT_FOUND);
       }
       Bom bom = service.findBomById(uuid);
       return new ResponseEntity(bom,HttpStatus.ACCEPTED);
   }
   
  /* @RequestMapping(path = "/{uuid}",method = RequestMethod.DELETE)
   public ResponseEntity<Bom> deleteBom(@PathVariable String uuid){
       if(!service.exitsBom(uuid)){
           return new ResponseEntity(HttpStatus.NOT_FOUND);
       }
       Bom bom = service.findBomById(uuid);
       service.deleteBom(uuid);
       return new ResponseEntity(bom, HttpStatus.ACCEPTED);
   }*/
   
   @RequestMapping(  path = "/{uuid}",method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
   public ResponseEntity<Bom> editBom(@PathVariable String uuid,@RequestBody Bom bom){
      logger.info("PUT: bomController");
       if(bom.getUuid() == null || !service.exitsBom(uuid)){
          logger.info("PUT: bomController: NOT FOUND");
           return new ResponseEntity(HttpStatus.NOT_FOUND);
       }
       service.editBom(bom,uuid);
       return new ResponseEntity(bom, HttpStatus.ACCEPTED);
   }
   
   @RequestMapping( method = RequestMethod.GET, path = "/branch",produces = "application/json" )
   public ResponseEntity<List<Bom>> getBomByBranchName(){
      logger.info("GET: bombranchController");
       return new ResponseEntity(service.getBomByBranchName(), HttpStatus.OK);
   }
   

}
