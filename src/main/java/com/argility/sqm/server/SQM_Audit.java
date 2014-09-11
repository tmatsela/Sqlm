/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argility.sqm.server;

import com.argility.sqm.objects.AuditObject;
import com.argility.sqm.objects.SQLObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mdiederick
 */
public class SQM_Audit {
    
    public void addSQM_Audit (Connection jdbcControl, AuditObject ao, int aud_seqno) throws Exception {
        
        
        //ADD AUDIT
        String sqlAud = "insert into sm_audit (task_option,sm_client_id,user_name,user_email,jira_key,"
                + "sql_ApplyType,sql_FileName,sql_XML,client_seqno) "
                + "values (?,?,?,?,?,?,?,?,?)";
        
        
        PreparedStatement psAudit=null;
        try {
            psAudit = jdbcControl.prepareStatement(sqlAud);
            psAudit.setString(1, ao.getSqmTaskOption());
            psAudit.setInt(2, ao.getSqmClient());
            psAudit.setString(3, ao.getUserName());
            psAudit.setString(4, ao.getUserEmail());
            psAudit.setString(5, ao.getJiraKey());
            psAudit.setString(6, ao.getSqlApplyType());
            psAudit.setString(7, ao.getSqlFileName());
            
            //Transform SQL into XML
                Iterator sql_string = ao.getSql().iterator();
                StringBuilder sqlBuffer = new StringBuilder();
                while(sql_string.hasNext()){
                   sqlBuffer.append(sql_string.next().toString());
                }

                SQLXML auditxml = jdbcControl.createSQLXML();
                auditxml.setString(sqlBuffer.toString());
            psAudit.setSQLXML(8, auditxml);
                        
            //audit sequence number
            psAudit.setInt(9, aud_seqno);
                    
            psAudit.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(SQM_Audit.class.getName()).log(Level.SEVERE, "Problem adding to SQLMASTER Audit : " + psAudit.toString(), ex);
            Exception myEx = new Exception("Problem adding to SQLMASTER Audit");
            throw myEx;
        }
        
        //ADD AUDIT TABLE LIST
        /*
        create table sm_audit_tablelist(
            aud_seqno int not null,
            table_name text not null);
        */
        String sqlAudTable = "insert into sm_audit_tablelist (client_seqno,table_name) "
                + "values (?,?)";
        
        PreparedStatement psAuditTable=null;
        try {
            Iterator tblList = ao.getTableList().iterator();
            while(tblList.hasNext()){
                psAuditTable = jdbcControl.prepareStatement(sqlAudTable);
                psAuditTable.setInt(1, aud_seqno);
                psAuditTable.setString(2, tblList.next().toString());
            System.out.println("psAuditTable: " + psAuditTable.toString());
                psAuditTable.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SQM_Audit.class.getName()).log(Level.SEVERE, "Problem adding to SQLMASTER Audit TABLE LIST: " + psAuditTable.toString(), ex);
            Exception myEx = new Exception("Problem adding to SQLMASTER Audit TABLE LIST");
            throw myEx;
        }
        
    }
    
    public void addSQM_AuditRelease (Connection jdbcControl, Connection releaseControl, AuditObject ao, int aud_release_seqno, int iSqlfileSeqno, HashSet branchlist) throws Exception {
        
        
        //ADD AUDIT
        String sqlAud = "insert into sm_release (client_seqno,sm_client_id,client_releaseseqno,task_option,"
                + "user_name,user_email,jira_key) "
                + "values (?,?,?,?,?,?,?)";   
        
        PreparedStatement psAudit=null;
        try {
            psAudit = jdbcControl.prepareStatement(sqlAud);
            psAudit.setInt(1, iSqlfileSeqno);  //? aud_seqno
            psAudit.setInt(2, ao.getSqmClient());
            psAudit.setInt(3, aud_release_seqno);
            psAudit.setString(4, ao.getSqmTaskOption());
            psAudit.setString(5, ao.getUserName());
            psAudit.setString(6, ao.getUserEmail());
            psAudit.setString(7, ao.getJiraKey());
            
            psAudit.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(SQM_Audit.class.getName()).log(Level.SEVERE, "Problem adding to SQLMASTER Audit Release : " + psAudit.toString(), ex);
            Exception myEx = new Exception("Problem adding to SQLMASTER Audit");
            throw myEx;
        }
        
        String sqlAudRelease = "insert into sqm_release_audit(rel_seqno,sqm_file_seqno,rel_jira_key,user_name,user_email,sql_filename,sql_xml) "
                + "values (?,?,?,?,?,?,?)";
        
        PreparedStatement psAuditRelease=null;
        try {
            psAuditRelease = releaseControl.prepareStatement(sqlAudRelease);
            psAuditRelease.setInt(1, aud_release_seqno);
            psAuditRelease.setInt(2, iSqlfileSeqno);
            psAuditRelease.setString(3, ao.getJiraKey());
            psAuditRelease.setString(4, ao.getUserName());
            psAuditRelease.setString(5, ao.getUserEmail());
            psAuditRelease.setString(6, ao.getSqlFileName());
            psAuditRelease.setString(7, ao.getAuditxml().getString());

            psAuditRelease.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(SQM_Audit.class.getName()).log(Level.SEVERE, "Problem adding to client Audit release: " + psAuditRelease.toString(), ex);
            Exception myEx = new Exception("Problem adding to client Audit release");
            throw myEx;
        }
        
        String sqlAudReleaseBranches = "INSERT INTO sqm_release_branches (rel_seqno,oboh_br_cde) "
                + "values (?,?)";
        
        PreparedStatement psAuditReleaseBranches=null;
        try {
            Iterator iBrList = branchlist.iterator();
            while(iBrList.hasNext()){
                psAuditReleaseBranches = releaseControl.prepareStatement(sqlAudReleaseBranches);
                psAuditReleaseBranches.setInt(1, aud_release_seqno);
                psAuditReleaseBranches.setString(2, iBrList.next().toString());

                psAuditReleaseBranches.executeUpdate();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SQM_Audit.class.getName()).log(Level.SEVERE, "Problem adding to client Audit release BRANCHES: " + psAuditReleaseBranches.toString(), ex);
            Exception myEx = new Exception("Problem adding to client Audit release BRANCHES");
            throw myEx;
        }
        
        
    }
    
    public HashSet getReleaseBranchList (Connection connClient) throws Exception {
        
        List<String> brList = new ArrayList<String>();
        
        String readSql = "select br_cde from sqlmaster_brlist_release order by br_cde;";
        
        PreparedStatement psML=null;
        try {
            psML = connClient.prepareStatement(readSql);
            ResultSet rsSeq = psML.executeQuery();
            while(rsSeq.next()) {
                brList.add(rsSeq.getString(1));
                System.out.println("brList " + rsSeq.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLObject.class.getName()).log(Level.SEVERE, "Problem reading master_list : " + psML.toString(), ex);
            Exception myEx = new Exception("Problem reading master_list");
            throw myEx;
        }
            
        HashSet hashsetList = new HashSet<String>(brList);
        return hashsetList;
    }
    
    public AuditObject getClientSQL (Connection jdbcControl, AuditObject ao, int client_seqno) throws Exception {
        
        String getSqlInfo = "select sql_filename,sql_xml from sm_audit where sm_client_id=? and client_seqno=?";
        
        PreparedStatement psML=null;
        try {
            psML = jdbcControl.prepareStatement(getSqlInfo);
            psML.setInt(1, ao.getSqmClient());
            psML.setInt(2, client_seqno);
            ResultSet rsSI = psML.executeQuery();
            
            if(rsSI.next()) {
                ao.setSqlFileName(rsSI.getString(1));
                ao.setAuditxml(rsSI.getSQLXML(2));
            }
            else
            {
                //exception - no seqno found!
                Exception myEx = new Exception("Nothing returned for client sequence no "+ psML.toString());
                throw myEx;
            }
            return ao;

        } catch (SQLException ex) {
            Logger.getLogger(SQM_Audit.class.getName()).log(Level.SEVERE, "Problem reading master_list : " + psML.toString(), ex);
            Exception myEx = new Exception("Problem reading master_list");
            throw myEx;
        }
    }
    
    public int getClientSeqNo (Connection jdbcControl, int clientId) throws Exception {
        
        String getSeqName = "select sm_seq_name from sm_connections where sm_schema='MASTER' and sm_client_id=?";
        
        PreparedStatement psML=null;
        try {
            psML = jdbcControl.prepareStatement(getSeqName);
            psML.setInt(1, clientId);
            ResultSet rsSeqName = psML.executeQuery();
            
            if(rsSeqName.next()) {
                String sequenceName=rsSeqName.getString(1);
                String readSql = "SELECT nextval(?)";
                    psML = jdbcControl.prepareStatement(readSql);
                    psML.setString(1, sequenceName);
                    ResultSet rsSeq = psML.executeQuery();
                    if(rsSeq.next()) {
                            return rsSeq.getInt(1);
                    }
                    else
                    {
                        //exception - no seqno found!
                        Exception myEx = new Exception("Nothing returned for client sequence no "+ psML.toString());
                        throw myEx;
                    }
            }
            else
            {
                //exception - no seqname found!
                Exception myEx = new Exception("No sequence name found for client "+ psML.toString());
                throw myEx;
            }

        } catch (SQLException ex) {
            Logger.getLogger(SQM_Audit.class.getName()).log(Level.SEVERE, "Problem reading master_list : " + psML.toString(), ex);
            Exception myEx = new Exception("Problem reading master_list");
            throw myEx;
        }
    }
    
    public int getClientSeqNoRelease (Connection jdbcControl, Connection clientControl, int clientId) throws Exception {
        
        String getSeqName = "select sm_seq_name from sm_connections where sm_schema='RELEASE' and sm_client_id=?";
        
        PreparedStatement psML=null;
        try {
            psML = jdbcControl.prepareStatement(getSeqName);
            psML.setInt(1, clientId);
            ResultSet rsSeqName = psML.executeQuery();
            
            if(rsSeqName.next()) {
                String sequenceName=rsSeqName.getString(1);
                String readSql = "SELECT nextval(?)";
                    psML = clientControl.prepareStatement(readSql);
                    psML.setString(1, sequenceName);
                    ResultSet rsSeq = psML.executeQuery();
                    if(rsSeq.next()) {
                            return rsSeq.getInt(1);
                    }
                    else
                    {
                        //exception - no seqno found!
                        Exception myEx = new Exception("Nothing returned for client sequence no "+ psML.toString());
                        throw myEx;
                    }
            }
            else
            {
                //exception - no seqname found!
                Exception myEx = new Exception("No sequence name found for client "+ psML.toString());
                throw myEx;
            }

        } catch (SQLException ex) {
            Logger.getLogger(SQM_Audit.class.getName()).log(Level.SEVERE, "Problem reading master_list : " + psML.toString(), ex);
            Exception myEx = new Exception("Problem reading master_list");
            throw myEx;
        }
    }
    
    public String createSQLFileName(String jiraID, String userName, String buildDet, int smSeqNo) {
        
        String newFileName =jiraID + "," + userName + "," + buildDet + ",sqlmaster_" + smSeqNo;
        
        return newFileName;
    }
    
    public String writeSqlFile(String SQLFileName, String strSQLHeader, List<String> sqllines) {

        String workingDir = "C:\\Users\\mdiederick\\Documents\\work.Argility\\sqls\\";
        WriteFile wf = new WriteFile();
        String workFile = workingDir + SQLFileName;
        wf.setWriteFileName(workFile);
        if (strSQLHeader!="")
        {
            wf.writeFile(strSQLHeader);
        }
        
        Iterator sql_string = sqllines.iterator();
        while(sql_string.hasNext()){
            wf.writeFile(sql_string.next().toString());
        }
        
        System.out.println("Write sql to sqlfile on disk " + workFile);

  //      FTPClientEvent fce = new FTPClientEvent();
        //  parameter: TELL which client!
   //     fce.sendFiles(fileName, targetFile);

        return workFile;
    }
    
    public String createSQLHeader(Connection jdbcClientControl, AuditObject ao) throws Exception{
        
        /*
        //CODE ORIGINALLY FROM SMWriteSqlFile
        
        String sqlType = ao.getSqltype();
        String strBranch = ao.getSqlApplyBranch();
        String strGroup = ao.getSqlApplyGroup();
        String strPriceReg = ao.getSqlApplySkuPrc();
        String strCountry = ao.getSqlApplyCountry();
        
        //#--TYPE=DML,GROUP=1~9~1,PRICING_REGION=ZAFK,COUNTRY=ZAF;
        //BRANCH=
        //String sqlHeader = "--TYPE=DML;";
        
        String sqlm_header = "--TYPE=";
        String sqh_col = "";
        
        sqlm_header = sqlm_header + sqlType;
        int checkValid=0;
        
        try{
                if (!"".equals(strBranch) && strBranch!=null) { 
                    checkValid=smAudit.validateSqlHeader("branch", "br_accept_sql_releases =true and br_cde", strBranch, jdbcClientControl, ao);
                    sqlm_header = sqlm_header + ",BRANCH=" + strBranch ; 
                }

                if (!"".equals(strGroup) && strGroup!=null) { 
                    checkValid=smAudit.validateSqlHeader("corp_grp", "grp_cde", strGroup, jdbcClientControl, ao);
                    sqlm_header = sqlm_header + ",GROUP=" + strGroup ; 
                }

                if (!"".equals(strPriceReg) && strPriceReg!=null) { 
                    checkValid=smAudit.validateSqlHeader("sku_price_tmpl_hdr", "sku_price_reg_cde", strPriceReg, jdbcClientControl, ao);
                    sqlm_header = sqlm_header + ",PRICING_REGION=" + strPriceReg ; 
                }

                if (!"".equals(strCountry) && strCountry!=null) { 
                    checkValid=smAudit.validateSqlHeader("corp_country", "country_cde", strCountry, jdbcClientControl, ao);
                    sqlm_header = sqlm_header + ",COUNTRY=" + strCountry ; 
                }

                sqlm_header = sqlm_header + ";";
                return sqlm_header;
                
            } catch (Exception ex) {
                    throw ex;
            }
        */
        return "";
    }
    
}
