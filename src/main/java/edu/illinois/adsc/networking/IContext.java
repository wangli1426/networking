package edu.illinois.adsc.networking;

import java.util.Map;

public interface IContext {
    /**
     * This method is invoked at the startup of messaging plugin
     * @param storm_conf storm configuration
     */
    public void prepare(Map storm_conf);

    /**
     * This method is invoked when a worker is unload a messaging plugin
     */
    public void term();

    /**
     * This method establishes a server side connection 
     * @param storm_id topology ID
     * @param port port #
     * @return server side connection
     */
    public IConnection bind(String storm_id, int port);

    /**
     * This method establish a client side connection to a remote server
     * @param storm_id topology ID
     * @param host remote host
     * @param port remote port
     * @return client side connection
     */
    public IConnection connect(String storm_id, String host, int port);
};
