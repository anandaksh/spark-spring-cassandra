package com.example.service;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraTableScanJavaRDD;
import com.example.model.Branch;

@Service
public class BranchService {
   static Logger logger = Logger.getLogger(BranchService.class);
   @Autowired
   SparkContext sparkContext;
   @Autowired
   private Environment environment;
   
   @Bean
   public CassandraTableScanJavaRDD<CassandraRow> cassandraTable() {
       return CassandraJavaUtil.javaFunctions(sparkContext)
               .cassandraTable(environment.getProperty("cassandra.keyspace"), "branch");
   }
   
   public List<Branch> getBranch() {
      logger.info("retrieving records from branch");
     return cassandraTable().select("uuid","branch")
           .map(Branch::fromCassandraRow)
           .collect();
   }
   
   public Branch createBranch(Branch branch) {
      JavaSparkContext sc = new JavaSparkContext(sparkContext);
      JavaRDD<Branch> b = sc.parallelize(Arrays.asList(branch));
      logger.info("createBranch: "+ branch.getBranch().toString()+ "and "+ branch.getUuid());
       CassandraJavaUtil.javaFunctions(b).
                      writerBuilder("measurements", "branch", CassandraJavaUtil.mapToRow(Branch.class)).saveToCassandra();
       return branch;
   }
   
   public boolean exitsBranch(String uuid) {
      List<Branch> bUuid =cassandraTable().select("uuid","branch").map(Branch::fromCassandraRow).collect();
      return bUuid.stream().filter(a->a.getUuid().equals(uuid)) != null;
   }

   public Branch findBranchById(String uuid) {
      List<Branch> bUuid =cassandraTable().select("uuid","branch").map(Branch::fromCassandraRow).collect();
      return bUuid.stream().filter(a->a.getUuid().equals(uuid)).findFirst().get();   
   }

   public void deleteBranch(String uuid){ 
      
                     
   }

   public void editBranch(Branch branch, String uuid) {
      logger.info("updating branch");
      JavaSparkContext sc = new JavaSparkContext(sparkContext);
      JavaRDD<Branch> b = sc.parallelize(Arrays.asList(branch));
      logger.info("update branch with : "+ branch.getBranch().toString()+ "and "+ branch.getUuid());
       CassandraJavaUtil.javaFunctions(b).
                      writerBuilder("measurements", "branch", CassandraJavaUtil.mapToRow(Branch.class)).saveToCassandra();
   }

}
