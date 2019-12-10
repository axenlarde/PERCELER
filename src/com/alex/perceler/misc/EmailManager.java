package com.alex.perceler.misc;

import java.util.ArrayList;

import com.alex.perceler.utils.LanguageManagement;
import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;

/**
 * Used to send email in a dedicated thread
 *
 * @author Alexandre RATEL
 */
public class EmailManager extends Thread
	{
	/**
	 * Variables
	 */
	ArrayList<ItemToMigrate> itmList;
	
	public EmailManager(ArrayList<ItemToMigrate> itmList)
		{
		super();
		this.itmList = itmList;
		start();
		}
	
	public void run()
		{
		sendReportEmail();
		}

	private void sendReportEmail()
		{
		try
			{
			Variables.getLogger().debug("Preparing email content");
			
			StringBuffer content = new StringBuffer("");
			content.append(LanguageManagement.getString("emailreportcontent"));
			for(ItemToMigrate itm : itmList)
				{
				content.append(itm.getInfo()+" : "+itm.getStatus()+"\r\n");
				}
			content.append(LanguageManagement.getString("emailfooter"));

			//Variables.getLogger().debug("Email content ready to be sent : "+content.toString());
			
			UsefulMethod.sendEmailToTheAdminList(
					LanguageManagement.getString("emailreportsubject"),
					content.toString());
			
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while sending email : "+e.getMessage());
			}
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
