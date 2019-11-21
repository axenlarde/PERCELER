package com.alex.perceler.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetNotificationHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

import com.alex.perceler.utils.Variables;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * Static method anout cli
 *
 * @author Alexandre RATEL
 */
public class CliTools
	{
	public boolean telnetAuth(ArrayList<String> exchange, CliInjector clii) throws Exception
		{
		try
			{
			/*******
			 * Write instruction used to authenticate to a device using telnet protocol 
			 */
			clii.sleep(100);
			
			Variables.getLogger().debug("Telnet authentication process begin for : "+clii.getDevice().getInfo());
			GWMisc.waitForAWord(exchange, variable.defVar("",2,0,variable.getMaSheet(),variable.getGW().get(0).get(1).getArr()), timeout, t, false);
			t.sleep(100);
			oneLine.writeInstruction(desc, out, t, variable.getMaSheet().getCell(9,numGW).getContents());
			oneLine.writeInstruction(desc, out, t, variable.getMaSheet().getCell(10,numGW).getContents());
			utils.variable.getLogger().debug(desc+" : Authentication terminated");
			return true;
			}
		catch(Exception exc)
			{
			Variables.getLogger().error("ERROR : Telnet authentication failed : "+exc.getMessage(),exc);
			}
		return false;
		}
	
	/***********************************
	 * Method used to open and prepare 
	 * the connection with the gateway
	 * using telnet protocol
	 ***********************************/
	public void connexionTelnet() throws Exception
		{
		utils.variable.getLogger().debug("Cr�ation de la connexion Telnet : "+IP);
		
		telnetConnection = new TelnetClient();
        
        //Options
        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", true, false, true, true);
        EchoOptionHandler echoopt = new EchoOptionHandler(false, false, true, true);
        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(false, false, true, true);
        
        telnetConnection.addOptionHandler(ttopt);
        telnetConnection.addOptionHandler(echoopt);
        telnetConnection.addOptionHandler(gaopt);
        
        telnetConnection.connect(IP,23);
        telnetConnection.registerNotifHandler(this);
        out = new BufferedWriter(new OutputStreamWriter(telnetConnection.getOutputStream()));
		in = new BufferedReader(new InputStreamReader(telnetConnection.getInputStream()));
		}

	
	public void receivedNegotiation(int negotiation_code, int option_code)
		{
		String command = null;
        if(negotiation_code == TelnetNotificationHandler.RECEIVED_DO)
	        {
	            command = "DO";
	        }
        else if(negotiation_code == TelnetNotificationHandler.RECEIVED_DONT)
	        {
	            command = "DONT";
	        }
        else if(negotiation_code == TelnetNotificationHandler.RECEIVED_WILL)
	        {
	            command = "WILL";
	        }
        else if(negotiation_code == TelnetNotificationHandler.RECEIVED_WONT)
	        {
	            command = "WONT";
	        }
		}
	
	/***********************************
	 * Method used to open and prepare 
	 * the connection with the gateway
	 * using SSH protocol
	 ***********************************/
	public void connexionSSH() throws Exception
		{
		utils.variable.getLogger().info("Cr�ation de la connexion SSH : "+IP);
		
		//New ssh object
		JSch jsch = new JSch();
		//New SSh session based on ssh object
		Session session=jsch.getSession(variable.getMaSheet().getCell(9,numGW).getContents(), IP, 22);
		session.setPassword(variable.getMaSheet().getCell(10,numGW).getContents());
		
		UserInfo ui = new MyUserInfo()
			{
	        public void showMessage(String message)
	        	{
	        	//Here we accept all the certificate
	        	//JOptionPane.showMessageDialog(null, message);
	        	}
	        public boolean promptYesNo(String message)
	        	{
	        	return true;
	        	}
			};
      	session.setUserInfo(ui);
		
		//Connection to the ssh server with timeout
		try
			{
			session.connect(10000);
			}
		catch(Exception exc)
			{
			utils.variable.getLogger().debug("### Erreur avec la version 2 du ssh ###");
			try
				{
				utils.variable.getLogger().debug("### Essai avec la version 1 du ssh ###");
				session.setClientVersion("SSH-1.5");
				session.connect(10000);
				}
			catch(Exception ex)
				{
				throw new Exception(ex.getMessage());
				}
			}
		//Start the shell session
		SSHConnection = session.openChannel("shell");
		
		//Assign input and output Stream
		out = new BufferedWriter(new OutputStreamWriter(SSHConnection.getOutputStream()));
		in = new BufferedReader(new InputStreamReader(SSHConnection.getInputStream()));
		
		((ChannelShell)SSHConnection).setPtyType("vt100");
		SSHConnection.connect();
		}
	
	public static abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive
		{
	    public String getPassword(){ return null; }
	    public boolean promptYesNo(String str){ return false; }
	    public String getPassphrase(){ return null; }
	    public boolean promptPassphrase(String message){ return false; }
	    public boolean promptPassword(String message){ return false; }
	    public void showMessage(String message){ }
	    public String[] promptKeyboardInteractive(String destination,
	                                              String name,
	                                              String instruction,
	                                              String[] prompt,
	                                              boolean[] echo){return null;}
		}
	
	
	
	
	
	
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
