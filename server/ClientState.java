package server;

/**
 * ClientState describes the states that the server.Client can be in: INIT,
 * PLAYING, ERROR, DONE.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */
public enum ClientState {
	INIT, PLAYING, ERROR, DONE;
}
