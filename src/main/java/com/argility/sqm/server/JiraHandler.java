package com.argility.sqm.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
//import com.atlassian.jira.rest.client.JiraRestClient;
//import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.User;
//import com.atlassian.jira.rest.client.domain.User;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
 
import java.net.URI;

/**
 *
 * @author mdiederick
 */
public class JiraHandler {
    public String LoginName;
    public String LoginPassword;
    public String EmailAddress;
    
    private String urlJira = "https://argilityrop.jira.com/";

    
    public JiraRestClient loginJira () throws Exception {

        try{
            
        System.out.println(String.format("Logging in to %s with username '%s'", urlJira, LoginName));
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        URI uri = new URI(urlJira);
        JiraRestClient restClient = factory.createWithBasicHttpAuthentication(uri, LoginName, LoginPassword);
    
    /*        BasicHttpAuthenticationHandler myAuthenticationHandler = new BasicHttpAuthenticationHandler(LoginName, LoginPassword);
            JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
            URI jiraServerUri = new URI(urlJira);
            JiraRestClient restClient = factory.create(jiraServerUri, myAuthenticationHandler);
*/
       return restClient;
            
        } catch(Exception ex) {
                throw ex;
        }

    }
    
    public String getUserEmail (JiraRestClient restClient) {
        
        
        // Invoke the JRJC Client
        Promise<User> promise = restClient.getUserClient().getUser(LoginName);
        User user = promise.claim();

            return user.getEmailAddress();
    }
    
    
    public void updateJiraLog(JiraRestClient restClient,String updateMessage) {
        
        IssueRestClient issue = restClient.getIssueClient();
        //client.addComment(newIssue.getCommentsUri(), Comment.valueOf(contents2)).claim();
        //EHLDBS-8250
    //    issue.addComment("");
        
                
       
        
        
    }
    
    public void createJiraLog() {
        
        
    }
    
    
    /**
     * @return the LoginName
     */
    public String getLoginName() {
        return LoginName;
    }

    /**
     * @param LoginName the LoginName to set
     */
    public void setLoginName(String LoginName) {
        this.LoginName = LoginName;
    }

    /**
     * @return the LoginPassword
     */
    public String getLoginPassword() {
        return LoginPassword;
    }

    /**
     * @param LoginPassword the LoginPassword to set
     */
    public void setLoginPassword(String LoginPassword) {
        this.LoginPassword = LoginPassword;
    }

    /**
     * @return the EmailAddress
     */
    public String getEmailAddress() {
        return EmailAddress;
    }

    /**
     * @param EmailAddress the EmailAddress to set
     */
    public void setEmailAddress(String EmailAddress) {
        this.EmailAddress = EmailAddress;
    }
    
}
