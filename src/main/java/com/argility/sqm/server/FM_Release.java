/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argility.sqm.server;

import com.argility.sqm.objects.AuditObject;
import com.argility.sqm.objects.ConnectionObject;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.util.concurrent.Promise;
import java.sql.Connection;

/**
 *
 * @author mdiederick
 */
public class FM_Release {

    private static String DB_RESOURCE = "com\\argility\\sqm\\resources\\smControlDB.properties";
    
    public static void main(String[] args) throws Exception {
            //Set Controlling Connection (argserv)
        try{
            ConnectionObject connControl = new ConnectionObject();
            connControl.setSetDBResouceFile(DB_RESOURCE);
            Connection jdbcControl = connControl.getSmConnection();
        
        
  /*
  i - Input #1
  */
            //INPUT INFORMATION
            String iUserName = "melanie.diedericks";
            String iUserPw = "melome7";
            String iJiraKeyRelease = "EHLDBS-8250";

            String iSqmTaskOption = "SQL.RELEASE";   

            String iClient = "FM.TEST";

            int iSqlfileSeqno = 15;


            AuditObject ao = new AuditObject();
            ao.setUserName(iUserName);
            ao.setJiraKey(iJiraKeyRelease);
            ao.setSqmTaskOption(iSqmTaskOption);
            ao.setSqlApplyType("now");

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

            Promise<Issue> issue = irc.getIssue(iJiraKeyRelease);

            //GET SUMMARY
            Issue issuex = issue.claim(); 
            String issueDet = issuex.getSummary();
            System.out.println("Jira issue Summary: " + issueDet);

      /*
      i - Input #2
      */
            //Set Client Connection
            ConnectionObject connControlClient = new ConnectionObject();
            ConnectionObject connReleaseClient = new ConnectionObject();
            Connection releaseControl =connReleaseClient.getSmClientReleaseConnection(jdbcControl,iClient);
            ao.setConnClient(connControlClient.getSmClientConnection(jdbcControl,iClient));
            
            System.out.println("DB SQM Control: " + connControl.getConnURL());
            System.out.println("DB Client Control: " + connControlClient.getConnURL());
            System.out.println("DB Release Control: " + connReleaseClient.getConnURL());
            ao.setSqmClient(connControlClient.getClientID());
            ao.setClientName(connControlClient.getClientlongname());

            //RELEASE TO ALL STORES.
            SQM_TaskHandler sqmTask = new SQM_TaskHandler();
            String commentUpdateJira = sqmTask.sql_Release(ao, jdbcControl, releaseControl, iSqlfileSeqno);

            
            //Update Jira  (code above)
                 irc.addComment(issuex.getCommentsUri(), Comment.valueOf(commentUpdateJira)).claim();
            System.out.println("commentUpdateJira:");
            System.out.println(commentUpdateJira);
        } catch (Exception ex) {
            throw ex;
        }
        
        System.out.println("==============DONE=============");
        
    }
    
}
