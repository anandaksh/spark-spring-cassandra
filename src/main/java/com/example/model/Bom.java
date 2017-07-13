package com.example.model;

import java.io.Serializable;

import com.datastax.spark.connector.japi.CassandraRow;

public class Bom implements Serializable{
   String uuid;
   String bom;

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getBom() {
      return bom;
   }

   public void setBom(String bom) {
      this.bom = bom;
   }
   
   public static Bom fromCassandraRow(CassandraRow row) {
      return new Bom(
              row.getString("uuid"),
              row.getString("bom"));
  }

   public Bom(String uuid, String bom) {
      super();
      this.uuid = uuid;
      this.bom = bom;
   }

   public Bom() {

   }

}
