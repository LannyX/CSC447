package logistic_application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Facility_Inventory {

  public static void main(String[] args) {

        try {
            String fileName = "facility_inventory.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(fileName);
            if (!xml.exists()) {
                System.err.println("**** XML File '" + fileName + "' cannot be found");
                System.exit(-1);
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList facilityEntries = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < facilityEntries.getLength(); i++) {
                if (facilityEntries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                
                String entryName = facilityEntries.item(i).getNodeName();
                if (!entryName.equals("Facility")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }
                
                // Get a node attribute
                NamedNodeMap aMap = facilityEntries.item(i).getAttributes();
                String storeId = aMap.getNamedItem("Name").getNodeValue();

                // Get a named nodes
                Element elem = (Element) facilityEntries.item(i);
                String fname = elem.getElementsByTagName("Name").item(0).getTextContent();
                String rateperd = elem.getElementsByTagName("Rate").item(0).getTextContent();
                String costpert = elem.getElementsByTagName("Cost").item(0).getTextContent();

                
                // Get all nodes named "Network" - there can be 0 or more
                ArrayList<String> networkDescriptions = new ArrayList<>();
                NodeList networkList = elem.getElementsByTagName("Network");
                for (int j = 0; j < networkList.getLength(); j++) {
                    if (networkList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = networkList.item(j).getNodeName();
                    if (!entryName.equals("Network")) {
                        System.err.println("Unexpected node found: " + entryName);
                        return;
                    }

                    // Get some named nodes
                    elem = (Element) networkList.item(j);
                    String placeName = elem.getElementsByTagName("Name").item(0).getTextContent();
                    String Distance = elem.getElementsByTagName("Distance").item(0).getTextContent();
                   // String bookDate = elem.getElementsByTagName("Date").item(0).getTextContent();
                    //String bookIsbn13 = elem.getElementsByTagName("ISBN13").item(0).getTextContent();
                    // Create a string summary of the book
                    networkDescriptions.add(placeName + " is " + Distance + " miles away" );
                    //+ bookDate + " [" + bookIsbn13 + "]");
                }
                
                // Get all nodes named "Inventory" - there can be 0 or more
                ArrayList<String> inventoryDescriptions = new ArrayList<>();
                NodeList inventoryList = elem.getElementsByTagName("Inventory");
                for (int j = 0; j < inventoryList.getLength(); j++) {
                    if (inventoryList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = inventoryList.item(j).getNodeName();
                    if (!entryName.equals("Inventory")) {
                        System.err.println("Unexpected node found: " + entryName);
                        return;
                    }

                    // Get some named nodes
                    elem = (Element) inventoryList.item(j);
                    String inventoryID = elem.getElementsByTagName("ID").item(0).getTextContent();
                    String inventoryQ = elem.getElementsByTagName("Quantity").item(0).getTextContent();
        
                    System.out.println( inventoryID);
                    // Create a string summary of the book
                    inventoryDescriptions.add("InventoryID: " + inventoryID + " Quantity: " + inventoryQ + ". " );
                    //+ bookDate + " [" + bookIsbn13 + "]");
                }

                // Here I would create a Store object using the data I just loaded from the XML
                System.out.println("Facility name: "+ fname +" [Rate /day:" + rateperd + " Cost: $" + costpert + "] \n" + networkDescriptions +"\n" + inventoryDescriptions + "\n");
                //+ storeAddress + "\n" + bookDescriptions + "\n");
                
            }

        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            e.printStackTrace();
        }
    }
    
}
