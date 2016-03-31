package com.esri.geoevent.solutions.transport.irc.jerklib.events.impl.dcc;

import com.esri.geoevent.solutions.transport.irc.jerklib.Channel;
import com.esri.geoevent.solutions.transport.irc.jerklib.Session;
import com.esri.geoevent.solutions.transport.irc.jerklib.events.dcc.DccEvent;
import com.esri.geoevent.solutions.transport.irc.jerklib.events.dcc.DccUnknownEvent;

/**
 * 
 * @author Andres N. Kievsky
 */
public class DccUnknownEventImpl extends DccEventImpl implements DccUnknownEvent
{

	public DccUnknownEventImpl(String ctcpString, String hostName, String message, String nick, String userName, String rawEventData, Channel channel, Session session)
	{
		super(ctcpString, hostName, message, nick, userName, rawEventData, channel, session);
	}

	public DccType getDccType()
	{
		return DccEvent.DccType.UNKNOWN;
	}

}
