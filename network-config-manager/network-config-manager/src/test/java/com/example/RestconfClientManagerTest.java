package com.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class RestconfClientManagerTest {

    @Test
    public void testGetConfig() throws Exception {
        String url = "https://172.20.0.109/restconf/data/ietf-interfaces:interfaces"; // Replace with actual URL
        String username = "admin"; // Replace with actual username
        String password = "cisco123"; // Replace with actual password
        RestconfClientManager clientManager = new RestconfClientManager(username, password);

        String response = clientManager.getConfig(url);
        assertNotNull(response);

        clientManager.close();
    }
}
