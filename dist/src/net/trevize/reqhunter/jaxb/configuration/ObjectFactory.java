//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.30 at 11:39:10 AM CEST 
//


package net.trevize.reqhunter.jaxb.configuration;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.trevize.reqhunter.jaxb.configuration package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Configuration_QNAME = new QName("http://trevize.net/ReqHunter", "configuration");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.trevize.reqhunter.jaxb.configuration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConfigurationT }
     * 
     */
    public ConfigurationT createConfigurationT() {
        return new ConfigurationT();
    }

    /**
     * Create an instance of {@link ConfigurationT.Sessions }
     * 
     */
    public ConfigurationT.Sessions createConfigurationTSessions() {
        return new ConfigurationT.Sessions();
    }

    /**
     * Create an instance of {@link ReqDatabaseT }
     * 
     */
    public ReqDatabaseT createReqDatabaseT() {
        return new ReqDatabaseT();
    }

    /**
     * Create an instance of {@link WorkbenchT }
     * 
     */
    public WorkbenchT createWorkbenchT() {
        return new WorkbenchT();
    }

    /**
     * Create an instance of {@link SessionT }
     * 
     */
    public SessionT createSessionT() {
        return new SessionT();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfigurationT }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://trevize.net/ReqHunter", name = "configuration")
    public JAXBElement<ConfigurationT> createConfiguration(ConfigurationT value) {
        return new JAXBElement<ConfigurationT>(_Configuration_QNAME, ConfigurationT.class, null, value);
    }

}
