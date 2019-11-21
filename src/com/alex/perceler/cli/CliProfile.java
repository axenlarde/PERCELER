package com.alex.perceler.cli;

import java.util.ArrayList;

import com.alex.perceler.utils.Variables.itmType;

/**
 * Class used to describe a cli injection profile
 *
 * @author Alexandre RATEL
 */
public class CliProfile
	{
	/**
	 * Variables
	 */
	public enum cliProtocol
		{
		ssh,
		telnet,
		auto
		};
	
	private itmType type;
	private ArrayList<OneLine> howToAuthenticate, cliList;
	
	public CliProfile(itmType type, ArrayList<OneLine> howToAuthenticate, ArrayList<OneLine> cliList)
		{
		super();
		this.type = type;
		this.howToAuthenticate = howToAuthenticate;
		this.cliList = cliList;
		}

	public itmType getType()
		{
		return type;
		}

	public void setType(itmType type)
		{
		this.type = type;
		}

	public ArrayList<OneLine> getHowToAuthenticate()
		{
		return howToAuthenticate;
		}

	public void setHowToAuthenticate(ArrayList<OneLine> howToAuthenticate)
		{
		this.howToAuthenticate = howToAuthenticate;
		}

	public ArrayList<OneLine> getCliList()
		{
		return cliList;
		}

	public void setCliList(ArrayList<OneLine> cliList)
		{
		this.cliList = cliList;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
