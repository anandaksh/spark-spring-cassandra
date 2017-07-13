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

import com.example.model.Branch;
import com.example.service.BranchService;

@RestController
@RequestMapping("/branch")
public class BranchController {
   static Logger logger = Logger.getLogger(BranchController.class);
   @Autowired
   private BranchService service;
   
   @RequestMapping( method = RequestMethod.GET, produces = "application/json" )
   public ResponseEntity<List<Branch>> getBranch(){
      logger.info("GET: /branch,method = getBranch");
       return new ResponseEntity(service.getBranch(), HttpStatus.OK);
   }
   
   /**
    * @return Returns list of branch after POST call
    */
   @RequestMapping(method = RequestMethod.POST,produces = "application/json", consumes = "application/json" )
   public ResponseEntity<Branch> postBranch(@RequestBody Branch branch ) {
      logger.info("POST: /branch,method = postBranch");
      service.createBranch(branch);
        return new ResponseEntity(branch, HttpStatus.CREATED);
   }
   
   @RequestMapping( path = "/{uuid}", method = RequestMethod.GET, produces = "application/json" )
   public ResponseEntity<Branch> getBranchById(@PathVariable String uuid){
       if(!service.exitsBranch(uuid)){
           return new ResponseEntity(HttpStatus.NOT_FOUND);
       }
       Branch branch = service.findBranchById(uuid);
       return new ResponseEntity(branch,HttpStatus.ACCEPTED);
   }
   
  /* @RequestMapping(path = "/{uuid}",method = RequestMethod.DELETE)
   public ResponseEntity<Branch> deleteBranch(@PathVariable String uuid){
       if(!service.exitsBranch(uuid)){
           return new ResponseEntity(HttpStatus.NOT_FOUND);
       }
       Branch branch = service.findBranchById(uuid);
       service.deleteBranch(uuid);
       return new ResponseEntity(branch, HttpStatus.ACCEPTED);
   }*/
   
   @RequestMapping(  path = "/{uuid}",method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
   public ResponseEntity<Branch> editBranch(@PathVariable String uuid,@RequestBody Branch branch){
      logger.info("PUT: branchController");
       if(branch.getUuid() == null || !service.exitsBranch(uuid)){
          logger.info("PUT: branchController: NOT FOUND");
           return new ResponseEntity(HttpStatus.NOT_FOUND);
       }
       service.editBranch(branch,uuid);
       return new ResponseEntity(branch, HttpStatus.ACCEPTED);
   } 
}
