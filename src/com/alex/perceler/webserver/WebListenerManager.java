package com.alex.perceler.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.alex.perceler.utils.Variables;
import com.alex.perceler.webserver.ManageWebRequest.webRequestType;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Class used to manage web request
 *
 * @author Alexandre
 */
public class WebListenerManager implements HttpHandler
	{
	/**
	 * Variables
	 */
	
	/**
	 * Constructor
	 */
	public WebListenerManager()
		{
		try
			{
			HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(UsefulMethod.getTargetOption("webserverport"))), 0);
			HttpContext context = server.createContext("/WRM", this);
			server.start();
			Variables.getLogger().debug("Web Server started !");
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : While listening for new web request : "+e.getMessage(),e);
			} 
		}
	
	@Override
	public void handle(HttpExchange exc) throws IOException
		{
		Variables.getLogger().debug("Web Server : Request received");
		
		try
			{
			//we treat only request from the local web server
			if(exc.getLocalAddress().getAddress().equals(exc.getRemoteAddress().getAddress()))
				{
				if(exc.getRequestMethod().equals("HEAD"))
					{
					Variables.getLogger().debug("Web Server : "+exc.getRequestMethod()+" Method received");
					
					Headers hs = exc.getResponseHeaders();
					hs.add("Connection", "Keep-Alive");
					hs.add("Keep-Alive", "timeout = 20000 max = 100");
					exc.sendResponseHeaders(200, 0);
					}
				else if(exc.getRequestMethod().equals("POST"))
					{
					Variables.getLogger().debug("Web Server : "+exc.getRequestMethod()+" Method received");
					
					String content = getContent(exc);
					Variables.getLogger().debug("Web request content : ");
					Variables.getLogger().debug(content);
					
					WebRequest wr = ManageWebRequest.parseWebRequest(content);
					WebRequest reply = null;
					
					if(wr.getType().equals(webRequestType.getCUCMUsers))
						{
						reply = ManageWebRequest.getCUCMUsers(content);
						}
					else if(wr.getType().equals(webRequestType.getSalesforceUsers))
						{
						reply = ManageWebRequest.getSalesforceUsers(content);
						}
					else if(wr.getType().equals(webRequestType.getUserList))
						{
						reply = ManageWebRequest.getUserList();
						}
					else if(wr.getType().equals(webRequestType.addUser))
						{
						reply = ManageWebRequest.addUser(content);
						}
					else if(wr.getType().equals(webRequestType.updateUser))
						{
						reply = ManageWebRequest.updateUser(content);
						}
					else if(wr.getType().equals(webRequestType.deleteUser))
						{
						reply = ManageWebRequest.deleteUser(content);
						}
					else if(wr.getType().equals(webRequestType.getUser))
						{
						reply = ManageWebRequest.getUser(content);
						}
					else if(wr.getType().equals(webRequestType.getSettings))
						{
						reply = ManageWebRequest.getSettings();
						}
					else if(wr.getType().equals(webRequestType.updateSettings))
						{
						reply = ManageWebRequest.updateSettings(content);
						}
					
					OutputStream os = exc.getResponseBody();
					
					if(reply != null)
						{
						Variables.getLogger().debug("Web Server : Sending response : ");
						Variables.getLogger().debug(reply.getContent());
						
						exc.sendResponseHeaders(200, reply.getContent().getBytes().length);
						os.write(reply.getContent().getBytes());
						}
					else
						{
						Variables.getLogger().debug("Something went wrong while building the reply so we send an error message");
						exc.sendResponseHeaders(500, 0);
						os.write("".getBytes());//Not sure is useful
						}
					
					os.flush();
					os.close();
					
					Variables.getLogger().debug("Web Server : Response sent");
					}
				else
					{
					Variables.getLogger().debug("Unkown method : "+exc.getRequestMethod());
					}
				}
			else
				{
				Variables.getLogger().debug("Request received from an external source \""+exc.getRemoteAddress().getAddress()+"\" so we discard it");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : While treating a web request : "+e.getMessage(),e);
			}
		}
	
	
	private String getContent(HttpExchange exc) throws Exception
		{
		BufferedReader in = new BufferedReader(new InputStreamReader(exc.getRequestBody()));
		StringBuffer buf = new StringBuffer();
		
		while(true)
			{
			String s = in.readLine();
			if(s != null)
				{
				buf.append(s);
				buf.append("\r\n");
				}
			else
				{
				break;
				}
			}
		
		return buf.toString();
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
