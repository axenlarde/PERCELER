package com.alex.perceler.webserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.alex.perceler.office.misc.BasicOffice;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.webserver.ManageWebRequest.webRequestType;

/**
 * Used to build web request
 *
 * @author Alexandre RATEL
 */
public class WebRequestBuilder
	{
	
	/**
	 * To build the requested request
	 * getOfficeList
	 */
	public static WebRequest buildGetOfficeListReply()
		{
		StringBuffer content = new StringBuffer();
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>getOfficeList</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			<offices>\r\n");
		
		try
			{
			for(BasicOffice o : Variables.getOfficeList())
				{
				content.append("				<office>\r\n");
				content.append("					<id>"+o.getId()+"</id>\r\n");
				content.append("					<idcomu>"+o.getIdcomu()+"</idcomu>\r\n");
				content.append("					<idcaf>"+o.getIdCAF()+"</idcaf>\r\n");
				content.append("					<fullname>"+o.getFullname()+"</fullname>\r\n");
				content.append("					<shortname>"+o.getShortname()+"</shortname>\r\n");
				content.append("					<newname>"+o.getNewName()+"</newname>\r\n");
				content.append("					<officetype>"+o.getOfficeType()+"</officetype>\r\n");
				content.append("					<voiceiprange>"+o.getVoiceIPRange().getCIDRFormat()+"</voiceiprange>\r\n");
				content.append("					<dataiprange>"+o.getDataIPRange().getCIDRFormat()+"</dataiprange>\r\n");
				content.append("					<newvoiceiprange>"+o.getNewVoiceIPRange().getCIDRFormat()+"</newvoiceiprange>\r\n");
				content.append("					<newdataiprange>"+o.getNewDataIPRange().getCIDRFormat()+"</newdataiprange>\r\n");
				content.append("				</office>\r\n");
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while retrieving the office list : "+e.getMessage());
			content.append("				</office>\r\n");
			}
		
		content.append("			</offices>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), webRequestType.getOfficeList);
		}
	
	
	
	
	
	/**
	 * To build the requested request
	 */
	public synchronized static WebRequest buildSuccess(webRequestType type)
		{
		StringBuffer content = new StringBuffer();
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			</success>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), webRequestType.success);
		}
	
	/**
	 * To build the requested request
	 */
	public synchronized static WebRequest buildError(webRequestType type)
		{
		StringBuffer content = new StringBuffer();
		
		content.append("<xml>\r\n");
		content.append("	<reply>\r\n");
		content.append("		<type>"+type.name()+"</type>\r\n");
		content.append("		<content>\r\n");
		content.append("			</error>\r\n");
		content.append("		</content>\r\n");
		content.append("	</reply>\r\n");
		content.append("</xml>\r\n");
		
		return new WebRequest(content.toString(), webRequestType.error);
		}
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
