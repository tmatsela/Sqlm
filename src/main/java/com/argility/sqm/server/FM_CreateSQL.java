package com.argility.sqm.server;

import com.argility.sqm.objects.AuditObject;
import com.argility.sqm.objects.ConnectionObject;
import com.argility.sqm.objects.SQLObject;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.util.concurrent.Promise;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mdiederick
 */
public class FM_CreateSQL {

    private static String DB_RESOURCE = "com\\argility\\sqm\\resources\\smControlDB.properties";
        
    public static void main(String[] args) throws Exception {
  
            //Set Controlling Connection (argserv)
        ConnectionObject connControl = new ConnectionObject();
        connControl.setSetDBResouceFile(DB_RESOURCE);
        Connection jdbcControl = connControl.getSmConnection();
        System.out.println("DB Connection Control: " + connControl.getConnURL());
        
        
  /*
  i - Input #1
  */
        //INPUT INFORMATION
        String iUserName = "melanie.diedericks";
        String iUserPw = "melome7";
        /* LIVE 
        String iJiraKey = "FMMERCH-342";
        String iSqmTaskOption = "SQL.DMLDDL";   //SQL.REGIONCORP
        String iClient = "FM.LIVE";
        String iSqlApplyType = "build5";  //now, build1, master-only
        */
        /* TEST */
        String iJiraKey = "EHLDBS-8250";
        String iSqmTaskOption = "SQL.DMLDDL";   //SQL.REGIONCORP
        String iClient = "FM.TEST";
        String iSqlApplyType = "now";  //now, build1, master-only
              
        List<String> iSQL = readSqlLinesInput();   
        
        AuditObject ao = new AuditObject();
        ao.setUserName(iUserName);
        ao.setJiraKey(iJiraKey);
        ao.setSqmTaskOption(iSqmTaskOption);
        ao.setSqlApplyType(iSqlApplyType);
        ao.setSql(iSQL);
        
            
        
        
        //STEP 1
        //getter/setter for Login Info
        JiraHandler jiraHandle = new JiraHandler();
        jiraHandle.setLoginName(iUserName);
        jiraHandle.setLoginPassword(iUserPw);
        
       //LOGIN
        JiraRestClient restClient = jiraHandle.loginJira();
        
        //EMAIL
        System.out.println("Jira user email: " + jiraHandle.getUserEmail(restClient));
        ao.setUserEmail(jiraHandle.getUserEmail(restClient));
        
        //GET ISSUE
        IssueRestClient irc = restClient.getIssueClient();
        
        Promise<Issue> issue = irc.getIssue(iJiraKey);
        
        //GET SUMMARY
        Issue issuex = issue.claim(); 
        String issueDet = issuex.getSummary();
        System.out.println("Jira issue Summary: " + issueDet);
        
        
  /*
  i - Input #2
  */
        //Set Client Connection
        ConnectionObject connControlClient = new ConnectionObject();
        try{
            ao.setConnClient(connControlClient.getSmClientConnection(jdbcControl,iClient));
        } catch (Exception ex) {
            throw ex;
        }
        System.out.println("DB Client Control: " + connControlClient.getConnURL());
        System.out.println("DB Client ID: " + connControlClient.getClientID());
        ao.setSqmClient(connControlClient.getClientID());
        ao.setClientName(connControlClient.getClientlongname());

        //Check SQL
        SQLObject sqlo = new SQLObject();
        HashSet listTables = sqlo.getTableList(iSQL);
        ao.setTableList(listTables);

        //SQL - Handle new tables first
        HashSet newTableList = sqlo.checkTableNew(listTables, ao.getConnClient());
        System.out.println("Any new Tables? " + newTableList);

        Iterator setIterator = newTableList.iterator();
        while(setIterator.hasNext()){
           String newtable = setIterator.next().toString();
           String mltype = readMLType(newtable);
           sqlo.addMasterList(newtable, mltype, ao.getConnClient());
        }

        //SQL - VALIDATE SQL
        SQM_TaskHandler sqmTask = new SQM_TaskHandler();
        String commentUpdateJira = "";
        if (iSqmTaskOption == "SQL.DMLDDL")
        {
            boolean checkRegCorp = sqlo.checkRegionCorp(listTables, ao.getConnClient());
            System.out.println("Any Region/Corp Tables? " + checkRegCorp);
            //PS = define region/corp child tables to identify!!
            if (checkRegCorp==true)
            {
                Exception myEx = new Exception("The SQL includes REGION/CORP Sql - please apply this SQL under the option REGION/CORP-SQL");
                throw myEx;
            }
            commentUpdateJira = sqmTask.sql_DMLDDL(ao, jdbcControl);
        }
        if (iSqmTaskOption == "SQL.REGIONCORP")   //Create instore sql
        {
                Exception myEx = new Exception("This option not developed yet");
                throw myEx;

        }
        if (iSqmTaskOption == "SQL.DML.ONLY")   //for example EHL ...etc
        {
                Exception myEx = new Exception("This option not developed yet");
                throw myEx;

        }

        //Update Jira  (code above)
        irc.addComment(issuex.getCommentsUri(), Comment.valueOf(commentUpdateJira)).claim();
        System.out.println("commentUpdateJira:");
        System.out.println(commentUpdateJira);

            //CREATE JIRA


            //CLOSE JIRA


        ///(m next step, would be release jobs)
        System.out.println("==============DONE=============");
                       
    }
    
    private static String readMLType (String newTable) {
        
        String returnMlType="";
        
        BufferedReader br = new BufferedReader(new
        InputStreamReader(System.in));
        System.out.println("What is the master_list TYPE of table [" +newTable + "]? ");
        do {
            System.out.println("Choose from Options-> norm,master,region,corp,region_child,corp_child : ");
            try{
                returnMlType = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(FM_CreateSQL.class.getName()).log(Level.SEVERE, "Problem reading sql from user input", ex);
            }
        //System.out.println(str);
            //norm,master,region,corp,region_child,corp_child
        } while(!returnMlType.trim().equalsIgnoreCase("norm") &&
                !returnMlType.trim().equalsIgnoreCase("master") &&
                !returnMlType.trim().equalsIgnoreCase("region") &&
                !returnMlType.trim().equalsIgnoreCase("corp") &&
                !returnMlType.trim().equalsIgnoreCase("region_child") &&
                !returnMlType.trim().equalsIgnoreCase("corp_child"));
        
        return returnMlType;
    }
    
    private static List<String> readSqlLinesInput () {
        
        // create a BufferedReader using System.in
        List<String> sqllines = new ArrayList<String>();
        int linesno = 0;
        BufferedReader br = new BufferedReader(new
        InputStreamReader(System.in));
        String str="";
        System.out.println("Enter lines of SQL. Each SQL must have its own line.");
        System.out.println("Enter 'stop' to quit.");
        do {
            try {
                str = br.readLine();
                if (!str.trim().equalsIgnoreCase("stop"))
                {
                    sqllines.add(str);
                    linesno++;
                }
            } catch (IOException ex) {
                Logger.getLogger(FM_CreateSQL.class.getName()).log(Level.SEVERE, "Problem reading sql from user input", ex);
            }
        //System.out.println(str);
        } while(!str.equals("stop"));
        
        return sqllines;
    }
    
}
