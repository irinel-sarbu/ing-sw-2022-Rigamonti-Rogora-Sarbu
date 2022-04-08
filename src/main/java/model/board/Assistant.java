package model.board;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import util.Logger;
import util.Wizard;

public class Assistant {

    Wizard wizard;
    String name;
    int value;
    int movements;

    private Assistant(Wizard wizard, String name, int value, int movements) {
        this.wizard = wizard;
        this.name = name;
        this.value = value;
        this.movements = movements;
    }

    public static List<Assistant> getWizardDeck(Wizard wizard) {
        List<Assistant> deck = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File("src/main/resources/assistants.xml"));

            // get <assistant>
            NodeList list = doc.getElementsByTagName("assistant");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    // get values
                    NodeList nameElement = element.getElementsByTagName("name");
                    String name = nameElement.item(0).getTextContent();

                    NodeList valueElement = element.getElementsByTagName("value");
                    int value = Integer.parseInt(valueElement.item(0).getTextContent());

                    NodeList movementsElement = element.getElementsByTagName("movements");
                    int movements = Integer.parseInt(movementsElement.item(0).getTextContent());

                    deck.add(new Assistant(wizard, name, value, movements));

                    //Logger.debug("New assistant:", "Name: " + name, "Value: " + value, "Mother nature movements: " + movements);
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return deck;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getMovements() {
        return movements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistant assistant = (Assistant) o;
        return Objects.equals(name, assistant.name);
    }

    @Override
    public String toString() {
        return "(" + value + ") [" + movements + "] " + name;
    }
}
