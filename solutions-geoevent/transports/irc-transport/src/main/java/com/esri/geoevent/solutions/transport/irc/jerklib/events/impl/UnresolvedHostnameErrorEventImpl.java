package com.esri.geoevent.solutions.transport.irc.jerklib.events.impl;


import java.nio.channels.UnresolvedAddressException;

import com.esri.geoevent.solutions.transport.irc.jerklib.Session;
import com.esri.geoevent.solutions.transport.irc.jerklib.events.UnresolvedHostnameErrorEvent;

/**
 * @author mohadib
 * @see UnresolvedHostnameErrorEvent
 *
 */
public class UnresolvedHostnameErrorEventImpl implements UnresolvedHostnameErrorEvent
{
	private Session session;
	private String rawEventData, hostName;
	private UnresolvedAddressException exception;

	public UnresolvedHostnameErrorEventImpl
	(
		Session session, 
		String rawEventData, 
		String hostName, 
		UnresolvedAddressException exception
	)
	{
		this.session = session;
		this.rawEventData = rawEventData;
		this.hostName = hostName;
		this.exception = exception;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.UnresolvedHostnameErrorEvent#getException()
	 */
	public UnresolvedAddressException getException()
	{
		return exception;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.UnresolvedHostnameErrorEvent#getHostName()
	 */
	public String getHostName()
	{
		return hostName;
	}

	/* (non-Javadoc)
	 * @see com.esri.ges.transport.Irc.jerklib.events.ErrorEvent#getErrorType()
	 */
	public ErrorType getErrorType()
	{
		return ErrorType.UNRESOLVED_HOSTNAME;
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
		return Type.ERROR;
	}

}
