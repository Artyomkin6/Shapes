package bag;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import data.Ball;
import data.Cube;
import data.Cylinder;
import data.Shape;
import org.w3c.dom.*;

import java.io.File;
import java.io.IOException;

enum ShapeType {
    CUBE,
    BALL,
    CYLINDER,
    NULL
}

public class XmlHandler {

    private static Backpack bag;

    public static boolean xmlValidator(File file) {

        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("schema.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(file));
        } catch (IOException | SAXException exc) {
            return false;
        }

        return true;
    }

    public static Backpack xmlReader(File file) throws ParserConfigurationException, SAXException, IOException {

        bag = new Backpack();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        parser.parse(file, new DefaultHandler() {

            double len = 0;
            double radius = 0;
            ShapeType type = ShapeType.NULL;
            boolean isRadius = false;
            boolean isLength = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equals("Backpack")) {
                    double capacity = Double.parseDouble(attributes.getValue(0));
                    bag = new Backpack(capacity);
                } else if (qName.equals("Cube")) {
                    type = ShapeType.CUBE;
                } else if (qName.equals("Ball")) {
                    type = ShapeType.BALL;
                } else if (qName.equals("Cylinder")) {
                    type = ShapeType.CYLINDER;
                } else if (qName.equals("radius")) {
                    isRadius = true;
                } else if (qName.equals("length") || qName.equals("height")) {
                    isLength = true;
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) {
                if (qName.equals("Cube")) {
                    bag.put(new Cube(len));
                    type = ShapeType.NULL;
                }
                if (qName.equals("Ball")) {
                    bag.put(new Ball(radius));
                    type = ShapeType.NULL;
                }
                if (qName.equals("Cylinder")) {
                    bag.put(new Cylinder(radius, len));
                    type = ShapeType.NULL;
                }
                if (qName.equals("radius")) {
                    isRadius = false;
                }
                if (qName.equals("length") || qName.equals("height")) {
                    isLength = false;
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) {
                if (type != ShapeType.NULL) {
                    if (isRadius) {
                        radius = Double.parseDouble((new String(ch, start, length)).replace("\n", "").trim());
                    }
                    if (isLength) {
                        len = Double.parseDouble((new String(ch, start, length)).replace("\n", "").trim());
                    }
                }
            }
        });

        return bag;
    }

    public static void xmlWriter(Backpack shapes, File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();
            Element rootElement = doc.createElement("Backpack");
            doc.appendChild(rootElement);
            rootElement.setAttribute("capacity", String.valueOf(shapes.getCapacity()));

            for (Shape shape : shapes.getShapeList()) {
                if (shape instanceof Cube) {
                    rootElement.appendChild(getShapeNode(doc, (Cube) shape));
                }
                if (shape instanceof Ball) {
                    rootElement.appendChild(getShapeNode(doc, (Ball) shape));
                }
                if (shape instanceof Cylinder) {
                    rootElement.appendChild(getShapeNode(doc, (Cylinder) shape));
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            StreamResult fileStream = new StreamResult(file);

            transformer.transform(source, fileStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Node getShapeNode(Document doc, Cube cube) {
        Element shape = doc.createElement("Cube");

        Node length = doc.createElement("length");
        length.appendChild(doc.createTextNode(String.valueOf(cube.getLength())));
        shape.appendChild(length);

        return shape;
    }

    private static Node getShapeNode(Document doc, Ball ball) {
        Element shape = doc.createElement("Ball");

        Node radius = doc.createElement("radius");
        radius.appendChild(doc.createTextNode(String.valueOf(ball.getRadius())));
        shape.appendChild(radius);

        return shape;
    }

    private static Node getShapeNode(Document doc, Cylinder cylinder) {
        Element shape = doc.createElement("Cylinder");

        Node radius = doc.createElement("radius");
        radius.appendChild(doc.createTextNode(String.valueOf(cylinder.getRadius())));
        shape.appendChild(radius);

        Node height = doc.createElement("height");
        height.appendChild(doc.createTextNode(String.valueOf(cylinder.getHeight())));
        shape.appendChild(height);

        return shape;
    }

}
