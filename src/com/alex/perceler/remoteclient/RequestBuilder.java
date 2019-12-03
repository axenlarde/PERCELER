package com.alex.perceler.remoteclient;

import com.alex.perceler.remoteclient.Request.requestType;

/**
 * Static method to build request
 * @author Alexandre
 *
 */
public class RequestBuilder
	{
	
	/**
	 * To build a replace ip request
	 */
	public synchronized static Request buildReplaceIP(String currentIP, String newIP)
		{
		Request r = new Request(requestType.replaceip, currentIP+":"+newIP);
		
		return r;
		}
	
	/**
	 * To build a restart service request
	 */
	public synchronized static Request buildRestartService()
		{
		Request r = new Request(requestType.restartservice,"");
		
		return r;
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/