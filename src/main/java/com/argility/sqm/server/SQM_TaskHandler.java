/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argility.sqm.server;

import com.argility.sqm.objects.AuditObject;
import com.argility.sqm.objects.SQLObject;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author mdiederick
 */
public class SQM_TaskHandler {
    
        //Get seqno & create filename-only
        //Apply SQL to client.  & sql_history
        //Create SQL File & FTP (maybe just build sql)
        //Add audit info
    public String sql_DMLDDL (AuditObject ao,Connection jdbcControl) throws Exception{
        
        StringBuilder commentUpdateJira=new StringBuilder("SQL-MASTER:");
        commentUpdateJira.append(System.getProperty("line.separator"));
        commentUpdateJira.append("CLIENT: " + ao.getClientName());
        commentUpdateJira.append(System.getProperty("line.separator"));
        /*
        get seqno
        <NO> iApply -> only for region/corp
        <NO> sql-header ->ignore
        Apply Master SQL AND& Add Master obo-entry (sql-history)
        Create sqlfile for store and FTP
        Add audit info
        Audit table list that was updated
        Create jira-comment
        
        */
        SQM_Audit sqmAud = new SQM_Audit();
        int seqno = sqmAud.getClientSeqNo(jdbcControl, ao.getSqmClient());
        
        String sqlfilename = sqmAud.createSQLFileName(ao.getJiraKey(), ao.getUserName(), ao.getSqlApplyType(), seqno);
        ao.setSqlFileName(sqlfilename);
        System.out.println("sqlfilename " + sqlfilename);
        commentUpdateJira.append("sqlfilename " + sqlfilename);
        commentUpdateJira.append(System.getProperty("line.separator"));
        
        SQLObject sqlo = new SQLObject();
        sqlo.applySql2Master(ao.getSql(),ao.getConnClient(),ao.getSqlFileName());
        
        sqmAud.writeSqlFile(sqlfilename, "", ao.getSql());
        
        //AUDIT  - add audit and table_list
        sqmAud.addSQM_Audit(jdbcControl, ao, seqno);
        
        
        return commentUpdateJira.toString();
    }
    
    public String sql_RegionCorp (AuditObject ao,Connection jdbcControl) throws Exception{
        
        StringBuilder commentUpdateJira=new StringBuilder("SQL-MASTER:");
        commentUpdateJira.append(System.getProperty("line.separator"));
        commentUpdateJira.append("CLIENT: " + ao.getClientName());
        commentUpdateJira.append(System.getProperty("line.separator"));
        /*
        Differences between REGION/CORP and INSERT/UPDATE/DELETE
        */
        
        
        return commentUpdateJira.toString();
    }
    
    public String sql_Release (AuditObject ao,Connection jdbcControl, Connection releaseControl, int iSqlfileSeqno) throws Exception{
        
        StringBuilder commentUpdateJira=new StringBuilder("SQL-MASTER RLEASE:");
        commentUpdateJira.append(System.getProperty("line.separator"));
        commentUpdateJira.append("CLIENT: " + ao.getClientName());
        commentUpdateJira.append(System.getProperty("line.separator"));
        /*
        
        Add audit info to SQM
        Add audit info to Client sqlmaster
        Add branch list release to Client sqlmaster
        Create jira-comment
        
        */
        SQM_Audit sqmAud = new SQM_Audit();
   //     int seqno = sqmAud.getClientSeqNo(jdbcControl, ao.getSqmClient());
   //     System.out.println("getClientSeqNo " + seqno);
        int aud_release_seqno = sqmAud.getClientSeqNoRelease(jdbcControl, releaseControl , ao.getSqmClient());
        System.out.println("getClientSeqNoRelease " + aud_release_seqno);
        
        //get SQLinfo
        ao = sqmAud.getClientSQL(jdbcControl, ao, iSqlfileSeqno);
        
        //get release branches
        HashSet brlist = sqmAud.getReleaseBranchList(ao.getConnClient());
        
        //AUDIT  - add audit and table_list
        sqmAud.addSQM_AuditRelease(jdbcControl, releaseControl, ao, aud_release_seqno, iSqlfileSeqno, brlist);
        commentUpdateJira.append("sqlfilename " + ao.getSqlFileName());
        commentUpdateJira.append(System.getProperty("line.separator"));
        commentUpdateJira.append("sent to brlist count: " + brlist.size());
        commentUpdateJira.append(System.getProperty("line.separator"));
        
        
        return commentUpdateJira.toString();
    }
               
}
