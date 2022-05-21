package model.board;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.Wizard;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Assistant implements Serializable {

    Wizard wizard;
    String name;
    int value;
    int movements;

    /**
     * Assistant constructor.
     *
     * @param wizard    {@link Wizard} specify deck to use (changes card's back).
     * @param name      specify name of the assistant.
     * @param value     value used to choose Playing order during planning and action phase.
     * @param movements maximum movement of MotherNature
     */
    private Assistant(Wizard wizard, String name, int value, int movements) {
        this.wizard = wizard;
        this.name = name;
        this.value = value;
        this.movements = movements;
    }

    /**
     * Load assistant deck from UML.
     *
     * @param wizard {@link Wizard} specify deck to use (changes card's back).
     * @return Whole Assistant deck as a {@link List}
     */
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

    /**
     * Getter for Assistant name
     *
     * @return {@link String} Assistant's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for assistant value
     *
     * @return Assistant value
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter for allowed MotherNature movements
     *
     * @return maximum movement for MotherNature
     */
    public int getMovements() {
        return movements;
    }

    /**
     * Get assistant cards' back
     * @return wizard chosen at the beginning of the game
     */
    public Wizard getWizard() { return wizard; }

    /**
     * Compare two assistants by their {@link Assistant#name}
     *
     * @param o other object to compare
     * @return true if assistants have same {@link Assistant#name}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistant assistant = (Assistant) o;
        return Objects.equals(name, assistant.name);
    }

    /**
     * Convert to string to allow easy print
     *
     * @return a {@link String} containing all Assistant info
     */
    @Override
    public String toString() {
        return "(" + value + ") [" + movements + "] " + name;
    }
}
