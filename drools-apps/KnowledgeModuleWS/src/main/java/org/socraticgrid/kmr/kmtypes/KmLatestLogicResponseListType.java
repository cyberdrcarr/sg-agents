
package org.socraticgrid.kmr.kmtypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KmLatestLogicResponseListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KmLatestLogicResponseListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="km" type="{urn:org:socraticgrid:kmr:kmtypes}KmLatestLogicResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KmLatestLogicResponseListType", propOrder = {
    "km"
})
public class KmLatestLogicResponseListType {

    @XmlElement(namespace = "urn:org:socraticgrid:kmr:kmtypes")
    protected List<KmLatestLogicResponse> km;

    /**
     * Gets the value of the km property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the km property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KmLatestLogicResponse }
     * 
     * 
     */
    public List<KmLatestLogicResponse> getKm() {
        if (km == null) {
            km = new ArrayList<KmLatestLogicResponse>();
        }
        return this.km;
    }

}
