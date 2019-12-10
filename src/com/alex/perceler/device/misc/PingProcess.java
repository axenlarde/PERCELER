package com.alex.perceler.device.misc;

import java.net.InetAddress;

import com.alex.perceler.misc.ItemToMigrate.itmStatus;
import com.alex.perceler.utils.Variables;
import com.alex.perceler.utils.Variables.reachableStatus;

/**
 * used to ping a device
 *
 * @author Alexandre RATEL
 */
public class PingProcess extends Thread
	{
	/**
	 * Variables
	 */
	private Device device;
	private int timeout;

	public PingProcess(Device device)
		{
		super();
		this.device = device;
		this.timeout = 5000;
		}
	
	public void run()
		{
		if((device.getStatus().equals(itmStatus.postaudit)) ||
				(device.getStatus().equals(itmStatus.done)))
			{
			device.setReachable(ping(device.getNewip())?reachableStatus.reachable:reachableStatus.unreachable);
			}
		else
			{
			device.setReachable(ping(device.getIp())?reachableStatus.reachable:reachableStatus.unreachable);
			}
		}
	
	private boolean ping(String ip)
		{
		try
			{
			InetAddress inet = InetAddress.getByName(ip);
			return inet.isReachable(timeout);
			}
		catch (Exception e)
			{
			Variables.getLogger().error("ERROR while pinging "+device.getInfo());
			}
		return false;
		}

	public void setTimeout(int timeout)
		{
		this.timeout = timeout;
		}
	
	
	
	/*2019*//*RATEL Alexandre 8)*/
	}
