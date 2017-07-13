package com.example.model;

import java.io.Serializable;

import com.datastax.spark.connector.japi.CassandraRow;

public class Branch implements Serializable{
   String uuid;
   String branch;
   public String getUuid() {
      return uuid;
   }
   public void setUuid(String uuid) {
      this.uuid = uuid;
   }
   public String getBranch() {
      return branch;
   }
   public void setBranch(String branch) {
      this.branch = branch;
   }
   
   public static Branch fromCassandraRow(CassandraRow row) {
      return new Branch(
              row.getString("uuid"),
              row.getString("branch"));
  }
   public Branch(String uuid, String branch) {
      super();
      this.uuid = uuid;
      this.branch = branch;
   }
   
   public Branch()
   {
      
   }
}
