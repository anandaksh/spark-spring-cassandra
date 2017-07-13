package com.example.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.spark.SparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.apache.spark.api.java.JavaSparkContext;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraTableScanJavaRDD;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.example.model.*;

import scala.Function;
import scala.Tuple2;

@Service
public class BomService {
   static Logger logger = Logger.getLogger(BomService.class);
   @Autowired
   SparkContext sparkContext;
   @Autowired
   private Environment environment;
   @Autowired 
   BranchService branchService;
   
   @Bean
   public CassandraTableScanJavaRDD<CassandraRow> cassandraTable() {
       return CassandraJavaUtil.javaFunctions(sparkContext)
               .cassandraTable(environment.getProperty("cassandra.keyspace"), "bom");
   }
   
   public List<Bom> getBom() {
      logger.info("retrieving records from bom");
     return cassandraTable().select("uuid","bom")
           .map(Bom::fromCassandraRow)
           .collect();
   }
   
   public Bom createBom(Bom bom) {
      JavaSparkContext sc = new JavaSparkContext(sparkContext);
      JavaRDD<Bom> b = sc.parallelize(Arrays.asList(bom));
      logger.info("createBom: "+ bom.getBom().toString()+ "and "+ bom.getUuid());
       CassandraJavaUtil.javaFunctions(b).
                      writerBuilder("measurements", "bom", CassandraJavaUtil.mapToRow(Bom.class)).saveToCassandra();
       return bom;
   }
   
   public boolean exitsBom(String uuid) {
      List<Bom> bUuid =cassandraTable().select("uuid","bom").map(Bom::fromCassandraRow).collect();
      return bUuid.stream().filter(a->a.getUuid().equals(uuid)) != null;
   }

   public Bom findBomById(String uuid) {
      List<Bom> bUuid =cassandraTable().select("uuid","bom").map(Bom::fromCassandraRow).collect();
      return bUuid.stream().filter(a->a.getUuid().equals(uuid)).findFirst().get();   
   }

   public void deleteBom(String uuid){ 
      
                     
   }

   public void editBom(Bom bom, String uuid) {
      logger.info("updating bom");
      JavaSparkContext sc = new JavaSparkContext(sparkContext);
      JavaRDD<Bom> b = sc.parallelize(Arrays.asList(bom));
      logger.info("update bom with : "+ bom.getBom().toString()+ "and "+ bom.getUuid());
       CassandraJavaUtil.javaFunctions(b).
                      writerBuilder("measurements", "bom", CassandraJavaUtil.mapToRow(Bom.class)).saveToCassandra();
   }
   
   public List<Bom> getBomByBranchName()
   {
      logger.info("getBomByBranchName bom");
      JavaRDD<Bom> postUserJoin = CassandraJavaUtil.javaFunctions(sparkContext)
            .cassandraTable("measurements", "bom", CassandraJavaUtil.mapRowTo(Bom.class));
      Map<Bom, Branch> joinedTables = CassandraJavaUtil.javaFunctions(postUserJoin)
            .joinWithCassandraTable("measurements", "branch",
            CassandraJavaUtil.allColumns,
            CassandraJavaUtil.someColumns("uuid"),
            CassandraJavaUtil.mapRowTo(Branch.class),
            CassandraJavaUtil.mapToRow(Bom.class)).collectAsMap();
     Set<Bom> types = joinedTables.keySet();
        List<Bom> b = new ArrayList<>();
        b.addAll(types);  
     return b;
   }

}
