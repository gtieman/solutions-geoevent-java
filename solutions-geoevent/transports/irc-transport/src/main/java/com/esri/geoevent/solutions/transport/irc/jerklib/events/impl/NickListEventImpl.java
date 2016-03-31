package com.esri.geoevent.solutions.transport.irc.jerklib.events.impl;


import java.util.List;

import com.esri.geoevent.solutions.transport.irc.jerklib.Channel;
import com.esri.geoevent.solutions.transport.irc.jerklib.Session;
import com.esri.geoevent.solutions.transport.irc.jerklib.events.NickListEvent;

/**
 * @author NickListEventImpl
 *
 */
public class NickListEventImpl implements NickListEvent
{
	private final Type type = Type.NICK_LIST_EVENT;
	private final List<String> nicks;
	private final Channel channel;
	private final String rawEventData;
	private final Session session;

	public NickListEventImpl(String rawEventData, Session session, Channel channel, List<String> nicks)
	{
		this.rawEventData = rawEventData;
		this.session = session;
		this.channel = channel;
		this.nicks = nicks;

	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.NickListEvent#getChannel()
	 */
	public Channel getChannel()
	{
		return channel;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.NickListEvent#getNicks()
	 */
	public List<String> getNicks()
	{
		return nicks;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.IRCEvent#getRawEventData()
	 */
	public String getRawEventData()
	{
		return rawEventData;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.IRCEvent#getSession()
	 */
	public Session getSession()
	{
		return session;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.IRCEvent#getType()
	 */
	public Type getType()
	{
		return type;
	}

}
