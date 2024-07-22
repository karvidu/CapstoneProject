package com.example;
public class Main {

    public static void main(String[] args) {
        try {
            String url = "https://172.20.0.109/restconf/data/ietf-interfaces:interfaces/interface=GigabitEthernet2"; // Replace with actual URL
            String username = "admin"; // Replace with actual username
            String password = "cisco123"; // Replace with actual password

            RestconfClientManager restconfClientManager = new RestconfClientManager(username, password);

            // Example of a GET request
            String responseGet = restconfClientManager.getConfig(url);
            System.out.println("GET Response: " + responseGet);

            // Example of a PUT request to change IP address of GigabitEthernet2
            String requestBody = "<interface xmlns=\"urn:ietf:params:xml:ns:yang:ietf-interfaces\">\n" +
                    "    <name>GigabitEthernet2</name>\n" +
                    "    <ipv4 xmlns=\"urn:ietf:params:xml:ns:yang:ietf-ip\">\n" +
                    "        <address>\n" +
                    "            <ip>172.20.0.130</ip>\n" +
                    "            <netmask>255.255.255.0</netmask>\n" +
                    "        </address>\n" +
                    "    </ipv4>\n" +
                    "</interface>";

            String responsePut = restconfClientManager.putConfig(url, requestBody);
            System.out.println("PUT Response: " + responsePut);

            restconfClientManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
