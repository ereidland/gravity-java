package com.evanreidland.e.net;

public enum TCPEvent
{
	RECEIVE, // For exceptions.
	CONNECT, // New client or connected to server.
	DISCONNECT, // Removed client or disconnected from server.
	SEND, // For exceptions.
	LISTEN // For exceptions.
}