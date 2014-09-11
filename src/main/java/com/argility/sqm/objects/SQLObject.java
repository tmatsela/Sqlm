/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argility.sqm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mdiederick
 */
public class SQLObject {
    
    public void applySql2Master(List<String> sqllines, Connection connClient, String sqlfilename)throws Exception {
        
        
        Iterator sql_string = sqllines.iterator();
        StringBuilder sqlBuffer = new StringBuilder();
        while(sql_string.hasNext()){
           sqlBuffer.append(sql_string.next().toString());
        }
        System.out.println("sqlBuffer :" + sqlBuffer);
        
        PreparedStatement psClient=null;
        try {
            psClient = connClient.prepareStatement(sqlBuffer.toString());
            psClient.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(SQLObject.class.getName()).log(Level.SEVERE, "Problem adding to master_list : " + psClient.toString(), ex);
            Exception myEx = new Exception("Problem applying sql to client " + psClient.toString());
            throw myEx;
        }
       
        String oboSql = "insert into sql_history (classname) values (?);";
        PreparedStatement psHist = connClient.prepareStatement(oboSql) ;
        psHist.setString(1, sqlfilename);

        try {
            int psStatus =  psHist.executeUpdate();
            if (psStatus==0) {
                //Don't throw error - but must report to DBA !!  SQL already applied to MASTER.
                Logger.getLogger(SQLObject.class.getName()).log(Level.INFO, "Problem adding to sql_history :" + psClient.toString());
                //////////SEND EMAIL!
            }
        } catch (SQLException ex) {
            //Don't throw error - but must report to DBA !!  SQL already applied to MASTER.
            Logger.getLogger(SQLObject.class.getName()).log(Level.SEVERE, "Problem adding to sql_history : " + psClient.toString(), ex);
            //////////SEND EMAIL!
        }
       
       
       
       //and sql_history !
       
    }
    
    public void addMasterList (String mlName, String mlType, Connection connClient) throws Exception{
        
        String sqlAddML = "insert into master_list (ml_name,ml_type) values (?,?);";
        PreparedStatement psML=null;
        try {
            psML = connClient.prepareStatement(sqlAddML);
            psML.setString(1, mlName);
            psML.setString(2, mlType);
            psML.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(SQLObject.class.getName()).log(Level.SEVERE, "Problem adding to master_list : " + psML.toString(), ex);
            Exception myEx = new Exception("Problem adding to master_list");
            throw myEx;
        }
    }
    
    public HashSet checkTableNew (HashSet tableList, Connection connClient) throws Exception {
        
        Iterator setIterator = tableList.iterator();
        List<String> newTableList = new ArrayList<String>();
        while(setIterator.hasNext()){
           String checktable = setIterator.next().toString();
           
            String readSql = "select ml_name from master_list where ml_name= ?";
            String mltype="";
            PreparedStatement psML=null;
            try {
                psML = connClient.prepareStatement(readSql);
                psML.setString(1, checktable);
                ResultSet rsSeq = psML.executeQuery();
                if(!rsSeq.next()) {
                    
                    newTableList.add(checktable);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SQLObject.class.getName()).log(Level.SEVERE, "Problem reading master_list : " + psML.toString(), ex);
                Exception myEx = new Exception("Problem reading master_list");
                throw myEx;
            }
        }
        HashSet hashsetList = new HashSet<String>(newTableList);
        return hashsetList;
    }
    
    public boolean checkRegionCorp (HashSet tableList, Connection connClient) throws Exception {
        
        boolean boolRegCorp = false;
        Iterator setIterator = tableList.iterator();
        while(setIterator.hasNext()){
           String checktable = setIterator.next().toString();
           
            String readSql = "select ml_type from master_list where ml_name= ?";
            String mltype="";
            PreparedStatement psML=null;
            try {
                psML = connClient.prepareStatement(readSql);
                psML.setString(1, checktable);
                ResultSet rsSeq = psML.executeQuery();
                if(rsSeq.next()) {
                    mltype=rsSeq.getString(1);
                    if ((mltype.equalsIgnoreCase("REGION")) ||
                            (mltype.equalsIgnoreCase("CORP")) ||
                            (mltype.equalsIgnoreCase("region_child")) ||
                            (mltype.equalsIgnoreCase("corp_child")))
                    {
                        boolRegCorp=true;
                    }
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(SQLObject.class.getName()).log(Level.SEVERE, "Problem reading master_list : " + psML.toString(), ex);
                Exception myEx = new Exception("Problem reading master_list");
                throw myEx;
            }
        }
        
        return boolRegCorp;
    }
    
    public HashSet getTableList (List<String> sqllines) {
        
            String sql="";
          //  String [] tableList = new String [nooftokens];
            List<String> tableList = new ArrayList<String>();
            for (int x=0; x< sqllines.size(); x++)
            {
                boolean foundTable=false;
                
                    sql=sqllines.get(x);
                    System.out.println("Read sql ="+sql);
                    StringTokenizer tokenz = new StringTokenizer(sql," ");
                    int nooftokens = tokenz.countTokens();
                    String [] splitSql = new String [nooftokens];
                    boolean ignoreNextWord=false;
                    for (int i=0; i < nooftokens; i++) {
                        if (foundTable==false)
                        {
                            splitSql[i] = tokenz.nextToken();
                            //IGNORE KEY WORDS
                            
                            if (splitSql[i].trim().equalsIgnoreCase("INDEX"))
                            {
                                ignoreNextWord=true;
                            } else 
                            {
                                if (!splitSql[i].trim().equalsIgnoreCase("UPDATE") 
                                        && !splitSql[i].trim().equalsIgnoreCase("INSERT")
                                        && !splitSql[i].trim().equalsIgnoreCase("INTO")
                                        && !splitSql[i].trim().equalsIgnoreCase("DELETE")
                                        && !splitSql[i].trim().equalsIgnoreCase("TABLE")
                                        && !splitSql[i].trim().equalsIgnoreCase("FROM")
                                        && !splitSql[i].trim().equalsIgnoreCase("CREATE")
                                        && !splitSql[i].trim().equalsIgnoreCase("ALTER")
                                        && !splitSql[i].trim().equalsIgnoreCase("ONLY")
                                        && !splitSql[i].trim().equalsIgnoreCase("ON")
                                        && !splitSql[i].trim().equalsIgnoreCase("SEQUENCE")
                                        && !ignoreNextWord==true)
                                {
                                    System.out.println("TABLE="+splitSql[i].toLowerCase());
                                    tableList.add(splitSql[i]);
                                    foundTable=true;
                                }
                                ignoreNextWord=false;
                            }
                        }
                    }
                    
            }
            
            HashSet hashsetList = new HashSet<String>(tableList);
             System.out.printf("Unique table list: %s%n", hashsetList);
        
        return hashsetList;
    }

    /*
    FROM OLD SQLOBJECT
    
    public void validateDML_GenericTables () throws Exception {
        
        //WHAT NEEDS TO BE VALIDATED:
        /*
            1. In DML, no SELECT or IN keywords allowed
            2. User Specified TableName - confirm if sql doesn't refer to any other tables
            3. This is for Generic Tables (Norm,Master) - so no Region/Corp Tables
            4. No DDL allowed here
            5. Only DML Allowed: INSERT,UPDATE,DELETE
            6. Column names must be specified on INSERT
        
        
            try {
                
                String errCheck;
                String testSQL = getStrSQL().toLowerCase();
                String testTABLE = getSqlTableName().toUpperCase();
                String testTABLETYPE = getSqlTableType().toUpperCase();
                
                
                String[] xstr = testSQL.split(";");
                
                System.out.println("TESTING TABLE [testTABLE]: " + testTABLE);
                System.out.println("TESTING TABLE Type [testTABLETYPE]: " + testTABLETYPE);
                
                for (int i = 0; i < xstr.length; i++) {
                
                    System.out.println("TESTING SQL [testSQL]: " + xstr[i].trim());

                    // #1
                    errCheck = "KEYWORD [SELECT] NOT ALLOWED IN SQL";
                    if(xstr[i].indexOf("select") != -1){
                            Logger.getLogger(SQLObject_old.class.getName()).log(Level.SEVERE, "exception - " + smMsg.getMsgSqlValidateFail() + errCheck);
                            Exception myException = new Exception(smMsg.getMsgSqlValidateFail() + errCheck);
                            throw myException;
                    }

                    // #3
                    errCheck = "ONLY NORM/MASTER TABLE ALLOWED IN GENERIC DML SQL, NOT REGION/CORP";
                    if (!testTABLETYPE.equals("MASTER") && !testTABLETYPE.equals("NORM")) {
                        
                        String strMsg = " (Table [" + testTABLE + "] is master_list type of [" + testTABLETYPE + "])";
                       
                            Logger.getLogger(SQLObject_old.class.getName()).log(Level.SEVERE, "exception - " + smMsg.getMsgSqlValidateFail() + errCheck + strMsg);
                            Exception myException = new Exception(smMsg.getMsgSqlValidateFail() + errCheck + strMsg);
                            throw myException;
                    }

                    // #4
                    errCheck = "DDL NOT ALLOWED IN DML SQL";
                    //Not necessary - as check #5 already make sure only insert/update/delete is used..

                    // #5
                    errCheck = "ONLY DML ALLOWED IS : [INSERT, UPDATE, DELETE]";
                    String checkDML=xstr[i].replace("\n", "").trim().substring(0, 6).toUpperCase();
                    System.out.println("checkDML [" + checkDML + "]");
                    if (!checkDML.equals("INSERT") && !checkDML.equals("UPDATE") && !checkDML.equals("DELETE")) {
                            Logger.getLogger(SQLObject_old.class.getName()).log(Level.SEVERE, "exception - " + smMsg.getMsgSqlValidateFail() + errCheck);
                            Exception myException = new Exception(smMsg.getMsgSqlValidateFail() + errCheck);
                            throw myException;
                    }

                    // #2
                    errCheck = "OTHER TABLE REFERENCED IN SQL THAN USER SPECIFIED TABLE [" + testTABLE + "]";
                    String dmlStartTable;
                    String valCompare;
                    int lenTblName = testTABLE.length();
                    if (checkDML.equals("INSERT")) {
                            dmlStartTable =xstr[i].replace("\n", "").trim().replace(" ", "").toUpperCase().substring(0, 10+lenTblName);
                            valCompare = "INSERTINTO" + testTABLE;
                            System.out.println("dmlStartTable [" + dmlStartTable + "]");
                            System.out.println("valCompare [" + valCompare + "]");
                            if (!dmlStartTable.equals(valCompare)) {
                                Logger.getLogger(SQLObject_old.class.getName()).log(Level.SEVERE, "exception - " + smMsg.getMsgSqlValidateFail() + errCheck);
                                Exception myException = new Exception(smMsg.getMsgSqlValidateFail() + errCheck);
                                throw myException;
                            }
                    } else if (checkDML.equals("UPDATE")) {
                            dmlStartTable =xstr[i].replace("\n", "").trim().replace(" ", "").toUpperCase().substring(0, 6+lenTblName);
                            valCompare = "UPDATE" + testTABLE;
                            System.out.println("dmlStartTable [" + dmlStartTable + "]");
                            System.out.println("valCompare [" + valCompare + "]");
                            if (!dmlStartTable.equals(valCompare)) {
                                Logger.getLogger(SQLObject_old.class.getName()).log(Level.SEVERE, "exception - " + smMsg.getMsgSqlValidateFail() + errCheck);
                                Exception myException = new Exception(smMsg.getMsgSqlValidateFail() + errCheck);
                                throw myException;
                            }
                    } else if (checkDML.equals("DELETE")) {
                            dmlStartTable =xstr[i].replace("\n", "").trim().replace(" ", "").toUpperCase().substring(0, 10+lenTblName);
                            valCompare = "DELETEFROM" + testTABLE;
                            System.out.println("dmlStartTable [" + dmlStartTable + "]");
                            System.out.println("valCompare [" + valCompare + "]");
                            if (!dmlStartTable.equals(valCompare)) {
                                Logger.getLogger(SQLObject_old.class.getName()).log(Level.SEVERE, "exception - " + smMsg.getMsgSqlValidateFail() + errCheck);
                                Exception myException = new Exception(smMsg.getMsgSqlValidateFail() + errCheck);
                                throw myException;
                            }
                    } else {
                            Logger.getLogger(SQLObject_old.class.getName()).log(Level.SEVERE, "exception - " + smMsg.getMsgSqlValidateFail() + errCheck);
                            Exception myException = new Exception(smMsg.getMsgSqlValidateFail() + errCheck);
                            throw myException;
                    }

                    // #6
                    errCheck = "NO COLUMN NAMES ON INSERT STATEMENT";
                    if (checkDML.equals("INSERT")) {
                        //next character should be ( for column names
                        valCompare = "INSERTINTO" + testTABLE;
                        
                        String checkNextChar = xstr[i].replace("\n", "").trim().replace(" ", "").substring(valCompare.length(), valCompare.length()+1);
                            System.out.println("checkinsert column names nextchar [" + checkNextChar + "]");
                            
                        if (!checkNextChar.equals("(")) {
                            Logger.getLogger(SQLObject_old.class.getName()).log(Level.SEVERE, "exception - " + smMsg.getMsgSqlValidateFail() + errCheck);
                            Exception myException = new Exception(smMsg.getMsgSqlValidateFail() + errCheck);
                            throw myException;
                        }
                    }
                
                }
            } catch (Exception ex) {
                    Logger.getLogger(SQLObject_old.class.getName()).log(Level.SEVERE, "Exception - {0}", ex.getMessage());
                    throw ex;
            }
        
    }
    */
    
    
}
