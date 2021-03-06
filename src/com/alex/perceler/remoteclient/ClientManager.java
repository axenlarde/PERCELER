package com.alex.perceler.remoteclient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.alex.perceler.remoteclient.Request.requestType;
import com.alex.perceler.utils.Variables;

public class ClientManager
	{
	/**
	 * Variables
	 */
	private String clientIP, clientPort;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public ClientManager(String clientIP, String clientPort) throws Exception
		{
		super();
		this.clientIP = clientIP;
		this.clientPort = clientPort;
		
		this.socket = new Socket(clientIP, Integer.parseInt(clientPort));
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		
		}
	
	public void sendRequest(Request r) throws Exception
		{
		try
			{
			Variables.getLogger().debug("Sending request "+r.getType()+"...");
			out.writeObject(r);
			out.flush();
			
			Object o = in.readObject();
			
			if(o instanceof Request)
				{
				Request reply = (Request)o;
				
				if(reply.getType().equals(requestType.success))
					{
					Variables.getLogger().debug("Request "+r.getType()+" executed with success");
					}
				else if(reply.getType().equals(requestType.error))
					{
					Variables.getLogger().debug("Request "+r.getType()+" failed to execute");
					throw new Exception("Request "+r.getType()+" failed to execute");
					}
				}
			else
				{
				Variables.getLogger().debug("Unknown request received !");
				}
			}
		catch (Exception e)
			{
			close();
			throw new Exception("ERROR while sending request to the client : "+e.getMessage());
			}
		}
	
	public void close()
		{
		try
			{
			out.close();
			socket.close();
			Variables.getLogger().debug("Client connection closed with success !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR whil closing conneciton to client : "+e.getMessage(),e);
			}
		}

	public String getClientIP()
		{
		return clientIP;
		}

	public String getClientPort()
		{
		return clientPort;
		}
	
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
