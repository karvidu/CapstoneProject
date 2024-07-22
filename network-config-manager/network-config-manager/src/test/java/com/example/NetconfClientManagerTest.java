package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class NetconfClientManagerTest {

    @Test
    public void testGetConfig() throws Exception {
        String serverUrl = "http://172.20.0.110"; // Replace with your server URL
        NetconfClientManager clientManager = new NetconfClientManager();
        clientManager.connect(serverUrl);

        String response = clientManager.getConfig();
        assertNotNull(response);
        System.out.println(response);
        clientManager.close();
    }

    @Test
    public void testEditConfig() throws Exception {
        String serverUrl = "http://172.20.0.110"; // Replace with your server URL
        String configData = "<config><interface><name>eth0</name><description>Test Interface</description></interface></config>"; // Replace with your actual config data
        NetconfClientManager clientManager = new NetconfClientManager();
        clientManager.connect(serverUrl);

        String response = clientManager.editConfig(configData);
        assertNotNull(response);
        System.out.println(response);
        clientManager.close();
    }

    @Test
    public void testPerformGetAndPrint() throws Exception {
        String serverUrl = "http://172.20.0.110"; // Replace with your server URL
        NetconfClientManager clientManager = new NetconfClientManager();
        clientManager.connect(serverUrl);

        clientManager.performGetAndPrint();
        clientManager.close();
    }
    @Test
    public void testPerformRpcAndPrint() throws Exception {
        String serverUrl = "http://172.20.0.110"; // Replace with your server URL
        String rpcRequest = "<rpc message-id=\"1\"><edit-config><target><running/></target><config><interface><name>eth0</name><description>Test Interface</description></interface></config></edit-config></rpc>";
        NetconfClientManager clientManager = new NetconfClientManager();
        clientManager.connect(serverUrl);

        clientManager.performRpcAndPrint(rpcRequest);
        clientManager.close();
    }

}