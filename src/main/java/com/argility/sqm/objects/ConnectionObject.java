/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argility.sqm.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mdiederick
 */
public class ConnectionObject {
    
    
    //String strClient;
    //String chEnvironment;  //Test only; Test and Live
    
    private static String DB_RESOURCE = "com/argility/sqlmaster/resources/smControlDB.properties";
    
    private String connURL;
    private String connDriver;
    private String connUser;
    private String connPasswd;
    private int clientID;
    private int connID;
    
    private String setDBResouceFile;
    private String clientName;
    public String clientlongname;
    private String schemaName;
    
    private Connection smConnection;
    public Connection smClientConnection;
    //String targetURL;
    //String targetDriver;
    //String targetUser;
    //String targetPasswd;
    
    

    /**
     * @return the connURL
     */
    public String getConnURL() {
        return connURL;
    }

    /**
     * @param connURL the connURL to set
     */
    public void setConnURL(String connURL) {
        this.connURL = connURL;
    }

    /**
     * @return the connDriver
     */
    public String getConnDriver() {
        return connDriver;
    }

    /**
     * @param connDriver the connDriver to set
     */
    public void setConnDriver(String connDriver) {
        this.connDriver = connDriver;
    }

    /**
     * @return the connUser
     */
    public String getConnUser() {
        return connUser;
    }

    /**
     * @param connUser the connUser to set
     */
    public void setConnUser(String connUser) {
        this.connUser = connUser;
    }

    /**
     * @return the connPasswd
     */
    public String getConnPasswd() {
        return connPasswd;
    }

    /**
     * @param connPasswd the connPasswd to set
     */
    public void setConnPasswd(String connPasswd) {
        this.connPasswd = connPasswd;
    }

    /**
     * @return the setDBResouceFile
     */
    public String getSetDBResouceFile() {
        
        return setDBResouceFile;
    }

    /**
     * @param setDBResouceFile the setDBResouceFile to set
     */
    public void setSetDBResouceFile(String setDBResouceFile) {
        
                this.setConnURL("jdbc:postgresql://172.31.16.104:5433/sqlmaster");
                this.setConnDriver("org.postgresql.Driver");
                this.setConnUser("sqm_java");
                this.setConnPasswd("5q3java");
    }

    /**
     * @return the smConnection
     */
    public Connection getSmConnection() throws Exception{
        
        this.smConnection = null;
        this.setSetDBResouceFile(DB_RESOURCE);
        
            try {
                    Class.forName(this.getConnDriver());
                    return DriverManager.getConnection(this.getConnURL(), this.getConnUser(), this.getConnPasswd());
            }  catch (Exception ex) {
                
                Logger.getLogger(ConnectionObject.class.getName()).log(Level.SEVERE, "exception - " + ex);
                throw ex;
            }
                
        //return smConnection;
    }

    /**
     * @param smConnection the smConnection to set
     */
    public void setSmConnection(Connection smConnection) {
        this.smConnection = smConnection;
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
    
    public ConnectionObject getSM_Client_Connection(Connection jdbcControl, String clientName) throws Exception{

        ConnectionObject connClient = new ConnectionObject();
        StringBuffer sqlClientConnDetails = new StringBuffer();
        
        sqlClientConnDetails.append("select b.sm_client_name clientlongname, a.sm_url colurl,a.sm_schema colschema,a.sm_user colusr,a.sm_pass colpwd,a.sm_driver coldriver, b.sm_client_id colclientid, a.sm_conn_id colconnid");
        sqlClientConnDetails.append(" from sm_connections a, sm_client b");
        sqlClientConnDetails.append(" where a.sm_client_id=b.sm_client_id and b.sm_client_name='");
        sqlClientConnDetails.append(clientName);
        sqlClientConnDetails.append("';");
            System.out.println("sqlClientConnDetails: " + sqlClientConnDetails.toString());

        PreparedStatement ps;
        try {
                ps = jdbcControl.prepareStatement(sqlClientConnDetails.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                        connClient.setClientlongname(rs.getString("clientlongname"));
                        connClient.setConnURL(rs.getString("colurl"));
                        connClient.setConnUser(rs.getString("colusr"));
                        connClient.setConnPasswd(rs.getString("colpwd"));
                        connClient.setConnDriver(rs.getString("coldriver"));
                        connClient.setConnID(rs.getInt("colconnid"));
                        connClient.setClientID(rs.getInt("colclientid"));
                        connClient.setSchemaName(rs.getString("colschema"));
                        connClient.setClientName(clientName);

                        //Set Connection
                        return connClient;
                }
                Logger.getLogger(ConnectionObject.class.getName()).log(Level.SEVERE, "exception - No Reference for client connection returned");
                Exception myException = new Exception("exception - No Reference for client connection returned");
                throw myException;
                
        } catch(SQLException qex) {
                Logger.getLogger(ConnectionObject.class.getName()).log(Level.SEVERE, "exception - Error reading Reference for client connection - " + qex);
                Exception myException = new Exception("exception - " + qex);
                throw myException;
                
        }  catch (Exception ex) {
                
                throw ex;
            }


    }

    /**
     * @return the connID
     */
    public int getConnID() {
        return connID;
    }

    /**
     * @param connID the connID to set
     */
    public void setConnID(int connID) {
        this.connID = connID;
    }

    /**
     * @return the clientID
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * @param clientID the clientID to set
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    /**
     * @return the schemaName
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * @param schemaName the schemaName to set
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * @return the smClientConnection
     */
    public Connection getSmClientReleaseConnection(Connection jdbcControl, String clientName) throws Exception{
        
        //        ConnectionObject connClient = new ConnectionObject();
        StringBuffer sqlClientConnDetails = new StringBuffer();
        
        sqlClientConnDetails.append("select b.sm_client_name clientlongname, a.sm_url colurl,a.sm_schema colschema,a.sm_user colusr,a.sm_pass colpwd,a.sm_driver coldriver, b.sm_client_id colclientid, a.sm_conn_id colconnid");
        sqlClientConnDetails.append(" from sm_connections a, sm_client b");
        sqlClientConnDetails.append(" where a.sm_schema='RELEASE' and a.sm_client_id=b.sm_client_id and b.sm_client_shortname=?");
        PreparedStatement ps;
        
        try {
                
                ps = jdbcControl.prepareStatement(sqlClientConnDetails.toString());
                ps.setString(1, clientName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                        this.setClientlongname(rs.getString("clientlongname"));
                        this.setConnURL(rs.getString("colurl"));
                        this.setConnUser(rs.getString("colusr"));
                        this.setConnPasswd(rs.getString("colpwd"));
                        this.setConnDriver(rs.getString("coldriver"));
                        this.setConnID(rs.getInt("colconnid"));
                        this.setClientID(rs.getInt("colclientid"));
                        this.setSchemaName(rs.getString("colschema"));
                        this.setClientName(clientName);
                
                    Class.forName(this.getConnDriver());
                    return DriverManager.getConnection(this.getConnURL(), this.getConnUser(), this.getConnPasswd());
                } else {
                    Logger.getLogger(ConnectionObject.class.getName()).log(Level.SEVERE, "exception - No Reference for client connection");
                    Exception myException = new Exception("exception - No Reference for client connection");
                    throw myException;
                }
        } catch(SQLException qex) {
            Logger.getLogger(ConnectionObject.class.getName()).log(Level.SEVERE, "exception - Error reading Reference for client connection - " + qex);
            Exception myException = new Exception("exception - " + qex);
            throw myException;
                
        }  catch (Exception ex) {
                
                Logger.getLogger(ConnectionObject.class.getName()).log(Level.SEVERE, "exception - " + ex);
                throw ex;
        }
    }

    /**
     * @return the smClientConnection
     */
    public Connection getSmClientConnection(Connection jdbcControl, String clientName) throws Exception{
        
        //        ConnectionObject connClient = new ConnectionObject();
        StringBuffer sqlClientConnDetails = new StringBuffer();
        
        sqlClientConnDetails.append("select b.sm_client_name clientlongname, a.sm_url colurl,a.sm_schema colschema,a.sm_user colusr,a.sm_pass colpwd,a.sm_driver coldriver, b.sm_client_id colclientid, a.sm_conn_id colconnid");
        sqlClientConnDetails.append(" from sm_connections a, sm_client b");
        sqlClientConnDetails.append(" where a.sm_schema='MASTER' and a.sm_client_id=b.sm_client_id and b.sm_client_shortname=?");
        PreparedStatement ps;
        
        try {
                
                ps = jdbcControl.prepareStatement(sqlClientConnDetails.toString());
                ps.setString(1, clientName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                        this.setClientlongname(rs.getString("clientlongname"));
                        this.setConnURL(rs.getString("colurl"));
                        this.setConnUser(rs.getString("colusr"));
                        this.setConnPasswd(rs.getString("colpwd"));
                        this.setConnDriver(rs.getString("coldriver"));
                        this.setConnID(rs.getInt("colconnid"));
                        this.setClientID(rs.getInt("colclientid"));
                        this.setSchemaName(rs.getString("colschema"));
                        this.setClientName(clientName);
                
                    Class.forName(this.getConnDriver());
                    return DriverManager.getConnection(this.getConnURL(), this.getConnUser(), this.getConnPasswd());
                } else {
                    Logger.getLogger(ConnectionObject.class.getName()).log(Level.SEVERE, "exception - No Reference for client connection");
                    Exception myException = new Exception("exception - No Reference for client connection");
                    throw myException;
                }
        } catch(SQLException qex) {
            Logger.getLogger(ConnectionObject.class.getName()).log(Level.SEVERE, "exception - Error reading Reference for client connection - " + qex);
            Exception myException = new Exception("exception - " + qex);
            throw myException;
                
        }  catch (Exception ex) {
                
                Logger.getLogger(ConnectionObject.class.getName()).log(Level.SEVERE, "exception - " + ex);
                throw ex;
        }
    }

    /**
     * @param smClientConnection the smClientConnection to set
     */
    public void setSmClientConnection(Connection smClientConnection) {
        this.smClientConnection = smClientConnection;
    }

    /**
     * @return the clientlongname
     */
    public String getClientlongname() {
        return clientlongname;
    }

    /**
     * @param clientlongname the clientlongname to set
     */
    public void setClientlongname(String clientlongname) {
        this.clientlongname = clientlongname;
    }
}
