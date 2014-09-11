/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argility.sqm.objects;

import java.sql.Connection;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author mdiederick
 */
public class AuditObject {
    public int sqmAudSeqNo;
    public String sqmTaskOption;
    public int sqmClient;
    public String clientName;
    public String userName;
    public String userEmail;
    public String jiraKey;
    
/*    public List<String> applyBranch = new ArrayList<String>();
    public List<String> applyGroup = new ArrayList<String>();
    public List<String> applySkuPrc = new ArrayList<String>();
    public List<String> applyCountry = new ArrayList<String>();
    */
    
    public String sqlApplyType; //now,build,master-only
    public String sqlFileName;
    public List<String> sql = new ArrayList<String>();
    
    public SQLXML auditxml;
    
    public HashSet tableList = new HashSet<String>();
    
    public Connection connClient;
            
/*
    TABLE =====
aud_seqno
sm_aud_ts   -> default now()
task_option
sm_client_id	
user_name
user_email
jira_key
list_applyBranch
list_applyGroup
list_applySkuPrc
list_applyCountry
sql_ApplyType
sql_FileName
sql_XML

    
*/

    /**
     * @return the sqmAudSeqNo
     */
    public int getSqmAudSeqNo() {
        return sqmAudSeqNo;
    }

    /**
     * @param sqmAudSeqNo the sqmAudSeqNo to set
     */
    public void setSqmAudSeqNo(int sqmAudSeqNo) {
        this.sqmAudSeqNo = sqmAudSeqNo;
    }

    /**
     * @return the sqmTaskOption
     */
    public String getSqmTaskOption() {
        return sqmTaskOption;
    }

    /**
     * @param sqmTaskOption the sqmTaskOption to set
     */
    public void setSqmTaskOption(String sqmTaskOption) {
        this.sqmTaskOption = sqmTaskOption;
    }

    /**
     * @return the sqmClient
     */
    public int getSqmClient() {
        return sqmClient;
    }

    /**
     * @param sqmClient the sqmClient to set
     */
    public void setSqmClient(int sqmClient) {
        this.sqmClient = sqmClient;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail the userEmail to set
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return the jiraKey
     */
    public String getJiraKey() {
        return jiraKey;
    }

    /**
     * @param jiraKey the jiraKey to set
     */
    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    /**
     * @return the sqlApplyType
     */
    public String getSqlApplyType() {
        return sqlApplyType;
    }

    /**
     * @param sqlApplyType the sqlApplyType to set
     */
    public void setSqlApplyType(String sqlApplyType) {
        this.sqlApplyType = sqlApplyType;
    }

    /**
     * @return the sqlFileName
     */
    public String getSqlFileName() {
        return sqlFileName;
    }

    /**
     * @param sqlFileName the sqlFileName to set
     */
    public void setSqlFileName(String sqlFileName) {
        this.sqlFileName = sqlFileName;
    }

    /**
     * @return the connClient
     */
    public Connection getConnClient() {
        return connClient;
    }

    /**
     * @param connClient the connClient to set
     */
    public void setConnClient(Connection connClient) {
        this.connClient = connClient;
    }

    /**
     * @return the sqlList
     */
    public List<String> getSql() {
        return sql;
    }

    /**
     * @param sqlList the sqlList to set
     */
    public void setSql(List<String> sql) {
        this.sql = sql;
    }

    /**
     * @return the tableList
     */
    public HashSet getTableList() {
        return tableList;
    }

    /**
     * @param tableList the tableList to set
     */
    public void setTableList(HashSet tableList) {
        this.tableList = tableList;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the auditxml
     */
    public SQLXML getAuditxml() {
        return auditxml;
    }

    /**
     * @param auditxml the auditxml to set
     */
    public void setAuditxml(SQLXML auditxml) {
        this.auditxml = auditxml;
    }
    
}
