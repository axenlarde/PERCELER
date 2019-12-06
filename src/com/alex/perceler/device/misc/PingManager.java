package com.alex.perceler.device.misc;

import java.util.ArrayList;

import com.alex.perceler.utils.UsefulMethod;
import com.alex.perceler.utils.Variables;

/**
 * used to manage ping process
 *
 * @author Alexandre RATEL
 */
public class PingManager extends Thread
	{
	/**
	 * Variables
	 */
	private ArrayList<PingProcess> pingList;
	private int maxThread;
	private boolean stop, pause;
	private int timeout;
	
	public PingManager()
		{
		stop = false;
		pause = false;
		pingList = new ArrayList<PingProcess>();
		
		try
			{
			this.timeout = Integer.parseInt(UsefulMethod.getTargetOption("pingtimeout"));
			this.maxThread = Integer.parseInt(UsefulMethod.getTargetOption("maxpingthread"));
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Couldn't find ping value. Applying default value");
			this.timeout = 5000;
			this.maxThread = 10;
			}
		}
	
	/*****
	 * Here we will start Ping process all at the same time
	 * according to the max thread value
	 */
	public void run()
		{
		try
			{
			int index = 0;
			while((index < pingList.size()) && (!stop))
				{
				for(int i=index; i<pingList.size(); i++)
					{
					if(startNewThread()&&(!pause))
						{
						pingList.get(i).setTimeout(timeout);
						pingList.get(i).start();
						index++;
						}
					else
						{
						/**
						 * If the max thread is reach or if the pause is on there is no point of trying the next item
						 * So we break here to directly go to the sleep step
						 */ 
						break;
						}
					}
				this.sleep(100);
				}
			
			/**
			 * To end this thread we wait for all the ping process to end
			 */
			boolean alive = true;
			while(alive)
				{
				alive = false;
				for(PingProcess pp : pingList)
					{
					if(pp.isAlive())
						{
						alive = true;
						break;
						}
					}
				this.sleep(100);
				}
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR : Something went wrong with the ping manager : "+e.getMessage(),e);
			}
		}
	
	private boolean startNewThread()
		{
		int totalInProgress = 0;
		for(PingProcess pp : pingList)
			{
			if(pp.isAlive())totalInProgress++;
			}
		
		if(totalInProgress<=maxThread)return true;
		return false;
		}

	public ArrayList<PingProcess> getPingList()
		{
		return pingList;
		}

	public void setPingList(ArrayList<PingProcess> pingList)
		{
		this.pingList = pingList;
		}

	public void setStop(boolean stop)
		{
		this.stop = stop;
		}

	public void setPause(boolean pause)
		{
		this.pause = pause;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
