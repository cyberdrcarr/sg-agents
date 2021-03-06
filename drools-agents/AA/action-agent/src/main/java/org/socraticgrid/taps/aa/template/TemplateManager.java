/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.taps.aa.template;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author esteban
 */
public class TemplateManager implements Serializable{

    private Map<String, TemplateEntry> templateEntries = new HashMap<>();

    public TemplateManager(List<String> resources) {
        try {
            this.parseTemplatesSource(resources);
        } catch (Exception ex) {
            throw new RuntimeException("Error parsing templates file", ex);
        }
    }

    public TemplateEntry getTemplate(String id) {
        TemplateEntry result = this.templateEntries.get(id);

        if (result == null) {
            throw new IllegalArgumentException("No template with id '" + id + "' is defined in templates.xml!");
        }

        return result;
    }

    private void parseTemplatesSource(List<String> resources) throws ParserConfigurationException, SAXException, IOException {

        for (String resource : resources) {

            InputStream templatesStream = TemplateManager.class.getResourceAsStream(resource);

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(templatesStream);

            doc.getDocumentElement().normalize();

            NodeList templates = doc.getElementsByTagName("template");

            for (int i = 0; i < templates.getLength(); i++) {
                Node template = templates.item(i);

                String id = template.getAttributes().getNamedItem("id").getNodeValue();
                String header = null;
                String body = null;

                NodeList childNodes = template.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node child = childNodes.item(j);

                    if ("header".equalsIgnoreCase(child.getNodeName())) {
                        header = child.getTextContent().trim();
                    } else if ("body".equalsIgnoreCase(child.getNodeName())) {
                        body = child.getTextContent().trim();
                    }
                }

                templateEntries.put(id, new TemplateEntry(id, header, body));

            }
        }

    }
}
