package com.alex.perceler.remoteclient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.alex.perceler.utils.Variables;

/**
 * To manage multiple client
 *
 * @author Alexandre RATEL
 */
public class MultipleClientManager
	{
	/**
	 * Variables
	 */
	ArrayList<ClientManager> clientList;

	public MultipleClientManager(ArrayList<String> ipList, ArrayList<String> portList) throws Exception
		{
		super();
		this.clientList = new ArrayList<ClientManager>();
		init(ipList, portList);
		if(clientList.size() == 0)throw new Exception("No connection established");
		}
	
	private void init(ArrayList<String> ipList, ArrayList<String> portList)
		{
		for(int i=0; i<ipList.size(); i++)
			{
			try
				{
				clientList.add(new ClientManager(ipList.get(i), portList.get(i)));
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR with client "+ipList.get(i)+" : "+e.getMessage(),e);
				}
			}
		}
	
	public void sendRequest(Request r)
		{
		for(ClientManager cm : clientList)
			{
			try
				{
				cm.sendRequest(r);
				}
			catch (Exception e)
				{
				Variables.getLogger().error("ERROR with client "+cm.getClientIP()+" : "+e.getMessage(),e);
				}
			}
		}
	
	public void close()
		{
		for(ClientManager cm : clientList)
			{
			cm.close();
			}
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
