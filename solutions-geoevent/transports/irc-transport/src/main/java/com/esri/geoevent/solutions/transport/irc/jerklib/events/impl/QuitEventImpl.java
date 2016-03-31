package com.esri.geoevent.solutions.transport.irc.jerklib.events.impl;


import java.util.List;

import com.esri.geoevent.solutions.transport.irc.jerklib.Channel;
import com.esri.geoevent.solutions.transport.irc.jerklib.Session;
import com.esri.geoevent.solutions.transport.irc.jerklib.events.IRCEvent;
import com.esri.geoevent.solutions.transport.irc.jerklib.events.QuitEvent;

/**
 * @author mohadib
 * @see QuitEvent
 *
 */
public class QuitEventImpl implements QuitEvent
{

	private final Type type = IRCEvent.Type.QUIT;
	private final String rawEventData, who, msg, userName, hostName;
	private final Session session;
	private final List<Channel> chanList;

	public QuitEventImpl(String rawEventData, Session session, String who, String userName, String hostName, String msg, List<Channel> chanList)
	{
		this.rawEventData = rawEventData;
		this.who = who;
		this.userName = userName;
		this.hostName = hostName;
		this.session = session;
		this.msg = msg;
		this.chanList = chanList;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.QuitEvent#getHostName()
	 */
	public String getHostName()
	{
		return hostName;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.QuitEvent#getUserName()
	 */
	public String getUserName()
	{
		return userName;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.QuitEvent#getNick()
	 */
	public final String getNick()
	{
		return who;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.IRCEvent#getType()
	 */
	public final Type getType()
	{
		return type;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.IRCEvent#getRawEventData()
	 */
	public final String getRawEventData()
	{
		return rawEventData;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.IRCEvent#getSession()
	 */
	public final Session getSession()
	{
		return session;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.QuitEvent#getQuitMessage()
	 */
	public final String getQuitMessage()
	{
		return msg;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.QuitEvent#getChannelList()
	 */
	public final List<Channel> getChannelList()
	{
		return chanList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return rawEventData;
	}

}
