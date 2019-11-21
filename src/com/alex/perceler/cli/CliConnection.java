package com.alex.perceler.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetNotificationHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

import com.alex.perceler.cli.CliProfile.cliProtocol;
import com.alex.perceler.utils.Variables;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * Used to establish the connection to a device
 *
 * @author Alexandre RATEL
 */
public class CliConnection implements TelnetNotificationHandler
	{
	/**
	 * Variables
	 */
	private String user,password,ip,info;
	private BufferedWriter out;
	private BufferedReader in;
	private AnswerReceiver receiver;
	private cliProtocol protocol;
	private TelnetClient telnetConnection;
	private Channel SSHConnection;
	private int timeout;
	
	public CliConnection(String user, String password, String ip, String info, cliProtocol protocol, int timeout)
		{
		super();
		this.user = user;
		this.password = password;
		this.ip = ip;
		this.info = info;
		this.protocol = protocol;
		this.timeout = timeout;
		}

	public void connect() throws Exception
		{
		if(protocol.equals(cliProtocol.ssh))
			{
			initSSH();	
			}
		else
			{
			initTelnet();
			}
		receiver = new AnswerReceiver(info, in);
		receiver.start();
		}
	
	/***********************************
	 * Method used to open and prepare 
	 * the connection with the gateway
	 * using SSH protocol
	 ***********************************/
	public void initSSH() throws Exception
		{
		Variables.getLogger().debug(info+" CLI : init SSH connection");
		
		JSch jsch = new JSch();
		Session session=jsch.getSession(user, ip, 22);
		session.setPassword(password);
		
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
			session.connect(timeout);
			}
		catch(Exception exc)
			{
			Variables.getLogger().debug(info+" : CLI : Unable to connect with SSH version 2, trying with version 1");
			try
				{
				session.setClientVersion("SSH-1.5");
				session.connect(timeout);
				}
			catch(Exception ex)
				{
				throw new Exception(info+" CLI : Unable to connect using SSH : "+ex.getMessage());
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
	
	/***********************************
	 * Method used to open and prepare 
	 * the connection with the gateway
	 * using telnet protocol
	 ***********************************/
	public void initTelnet() throws Exception
		{
		Variables.getLogger().debug(info+" CLI : init SSH connection");
		
		telnetConnection = new TelnetClient();
        
        //Options
        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", true, false, true, true);
        EchoOptionHandler echoopt = new EchoOptionHandler(false, false, true, true);
        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(false, false, true, true);
        
        telnetConnection.addOptionHandler(ttopt);
        telnetConnection.addOptionHandler(echoopt);
        telnetConnection.addOptionHandler(gaopt);
        
        telnetConnection.connect(ip,23);
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
        Variables.getLogger().debug(info+" CLI : Telnet negociation command received : "+command);
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
	
	/**
	 * To close the connection with the device
	 */
	public void close()
		{
		try
			{
			receiver.setStop(true);
			in.close();
			out.close();
			
			if(protocol.equals(cliProtocol.ssh))
				{
				SSHConnection.disconnect();
				}
			else
				{
				telnetConnection.disconnect();
				}
			Variables.getLogger().debug(info+" : CLI : Device disconnected successfully");
			}
		catch (Exception e)
			{
			Variables.getLogger().error(info+" : CLI : ERROR while closing connection : "+e.getMessage());
			}
		}

	public BufferedWriter getOut()
		{
		return out;
		}

	public BufferedReader getIn()
		{
		return in;
		}

	public AnswerReceiver getReceiver()
		{
		return receiver;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
