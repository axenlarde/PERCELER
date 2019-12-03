package com.alex.perceler.remoteclient;

import java.io.Serializable;



/**
 * Represent a simple request
 */
public class Request implements Serializable
	{
	/**
	 * Variables
	 */
	
	public enum requestType
		{
		restartservice,
		replaceip,
		success,
		error
		};
	
	private static final long serialVersionUID = 1L;
	requestType type;
	String content;
	
	public Request(requestType type, String content)
		{
		super();
		this.type = type;
		this.content = content;
		}
	
	public requestType getType()
		{
		return type;
		}
	public String getContent()
		{
		return content;
		}
	
	}

/*2019*//*RATEL Alexandre 8)*/