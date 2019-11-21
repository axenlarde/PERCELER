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
	
	private String name;
	private int defaultInterCommandTimer;
	private itmType type;
	private ArrayList<OneLine> howToAuthenticate, cliList;
	
	public CliProfile(String name, itmType type, ArrayList<OneLine> howToAuthenticate, ArrayList<OneLine> cliList, int defaultInterCommandTimer)
		{
		super();
		this.name = name;
		this.type = type;
		this.howToAuthenticate = howToAuthenticate;
		this.cliList = cliList;
		this.defaultInterCommandTimer = defaultInterCommandTimer;
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

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public int getDefaultInterCommandTimer()
		{
		return defaultInterCommandTimer;
		}

	public void setDefaultInterCommandTimer(int defaultInterCommandTimer)
		{
		this.defaultInterCommandTimer = defaultInterCommandTimer;
		}
	
	/*2019*//*RATEL Alexandre 8)*/
	}
