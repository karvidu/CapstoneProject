package com.example;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class NetconfClientManager {
    private HttpURLConnection connection;

    public void connect(String url) throws Exception {
        URL serverUrl = new URL(url);
        connection = (HttpURLConnection) serverUrl.openConnection();
        connection.setDoOutput(true);
    }

    public String getConfig() throws Exception {
        String getConfigRequest = "<rpc message-id=\"1\"><get-config><source><running/></source></get-config></rpc>";
        return sendRpc(getConfigRequest);
    }

    public String editConfig(String config) throws Exception {
        String editConfigRequest = "<rpc message-id=\"2\"><edit-config><target><running/></target><config>" + config + "</config></edit-config></rpc>";
        return sendRpc(editConfigRequest);
    }

    // Method to perform get operation and print response in a formatted way
    public void performGetAndPrint() throws Exception {
        String getRequest = "<rpc message-id=\"1\"><get><filter type=\"subtree\"></filter></get></rpc>";
        String response = sendRpc(getRequest);

        // Print the response in a formatted manner
        System.out.println("[INFO] GET Response:");
        System.out.println(response);
    }
 // Method to perform rpc operation and print response in a formatted way
    public void performRpcAndPrint(String rpcRequest) throws Exception {
        String response = sendRpc(rpcRequest);

        // Print the response in a formatted manner
        System.out.println("[INFO] RPC Response:");
        System.out.println(response);
    }

    private String sendRpc(String rpc) throws Exception {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.getOutputStream().write(rpc.getBytes(StandardCharsets.UTF_8));

        StringWriter writer = new StringWriter();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(rpc)));
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.toString();
    }

    public void close() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    public static void main(String[] args) {
        NetconfClientManager manager = new NetconfClientManager();
        String serverUrl = "http://172.20.0.117"; // Replace with your server URL
        String configData = "<config><interface><name>eth0</name><description>Test Interface</description></interface></config>"; // Replace with your actual config data

        try {
            manager.connect(serverUrl);
            System.out.println("Connected to Netconf server.");

            // Perform get-config operation and print response
            manager.performGetAndPrint();

            // Perform edit-config operation and print response
            String editConfigResponse = manager.editConfig(configData);
            System.out.println("[INFO] EDIT Response:");
            System.out.println(editConfigResponse);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.close();
            System.out.println("Connection closed.");
        }
    }
}
