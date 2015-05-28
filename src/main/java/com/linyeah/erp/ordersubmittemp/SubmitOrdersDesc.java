//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.28 at 06:57:47 PM CST 
//


package com.linyeah.erp.ordersubmittemp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuthUserName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="10"/>
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AuthPassword">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="10"/>
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AuthtokenCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="5"/>
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OperationCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="0001"/>
 *               &lt;enumeration value="0002"/>
 *               &lt;enumeration value="0003"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OrderName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CustSequence">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OrderCustSeqs">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PrintMarkOrder" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReciverName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReciverProvince">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReciverExpress">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReciverAddress">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReciverMobile">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="^1[3578][0-9]{9}$"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Reciverdate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ReciverTime">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "authUserName",
    "authPassword",
    "authtokenCode",
    "operationCode",
    "orderName",
    "custSequence",
    "orderCustSeqs",
    "printMarkOrder",
    "reciverName",
    "reciverProvince",
    "reciverExpress",
    "reciverAddress",
    "reciverMobile",
    "reciverdate",
    "reciverTime"
})
@XmlRootElement(name = "SubmitOrdersDesc")
public class SubmitOrdersDesc {

    @XmlElement(name = "AuthUserName", required = true)
    protected String authUserName;
    @XmlElement(name = "AuthPassword", required = true)
    protected String authPassword;
    @XmlElement(name = "AuthtokenCode", required = true)
    protected String authtokenCode;
    @XmlElement(name = "OperationCode", required = true)
    protected String operationCode;
    @XmlElement(name = "OrderName", required = true)
    protected String orderName;
    @XmlElement(name = "CustSequence", required = true)
    protected String custSequence;
    @XmlElement(name = "OrderCustSeqs", required = true)
    protected String orderCustSeqs;
    @XmlElement(name = "PrintMarkOrder", required = true)
    protected String printMarkOrder;
    @XmlElement(name = "ReciverName", required = true)
    protected String reciverName;
    @XmlElement(name = "ReciverProvince", required = true)
    protected String reciverProvince;
    @XmlElement(name = "ReciverExpress", required = true)
    protected String reciverExpress;
    @XmlElement(name = "ReciverAddress", required = true)
    protected String reciverAddress;
    @XmlElement(name = "ReciverMobile", required = true)
    protected String reciverMobile;
    @XmlElement(name = "Reciverdate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar reciverdate;
    @XmlElement(name = "ReciverTime", required = true)
    protected String reciverTime;

    /**
     * Gets the value of the authUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthUserName() {
        return authUserName;
    }

    /**
     * Sets the value of the authUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthUserName(String value) {
        this.authUserName = value;
    }

    /**
     * Gets the value of the authPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthPassword() {
        return authPassword;
    }

    /**
     * Sets the value of the authPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthPassword(String value) {
        this.authPassword = value;
    }

    /**
     * Gets the value of the authtokenCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthtokenCode() {
        return authtokenCode;
    }

    /**
     * Sets the value of the authtokenCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthtokenCode(String value) {
        this.authtokenCode = value;
    }

    /**
     * Gets the value of the operationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperationCode() {
        return operationCode;
    }

    /**
     * Sets the value of the operationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperationCode(String value) {
        this.operationCode = value;
    }

    /**
     * Gets the value of the orderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderName() {
        return orderName;
    }

    /**
     * Sets the value of the orderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderName(String value) {
        this.orderName = value;
    }

    /**
     * Gets the value of the custSequence property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustSequence() {
        return custSequence;
    }

    /**
     * Sets the value of the custSequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustSequence(String value) {
        this.custSequence = value;
    }

    /**
     * Gets the value of the orderCustSeqs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderCustSeqs() {
        return orderCustSeqs;
    }

    /**
     * Sets the value of the orderCustSeqs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderCustSeqs(String value) {
        this.orderCustSeqs = value;
    }

    /**
     * Gets the value of the printMarkOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrintMarkOrder() {
        return printMarkOrder;
    }

    /**
     * Sets the value of the printMarkOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrintMarkOrder(String value) {
        this.printMarkOrder = value;
    }

    /**
     * Gets the value of the reciverName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReciverName() {
        return reciverName;
    }

    /**
     * Sets the value of the reciverName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReciverName(String value) {
        this.reciverName = value;
    }

    /**
     * Gets the value of the reciverProvince property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReciverProvince() {
        return reciverProvince;
    }

    /**
     * Sets the value of the reciverProvince property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReciverProvince(String value) {
        this.reciverProvince = value;
    }

    /**
     * Gets the value of the reciverExpress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReciverExpress() {
        return reciverExpress;
    }

    /**
     * Sets the value of the reciverExpress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReciverExpress(String value) {
        this.reciverExpress = value;
    }

    /**
     * Gets the value of the reciverAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReciverAddress() {
        return reciverAddress;
    }

    /**
     * Sets the value of the reciverAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReciverAddress(String value) {
        this.reciverAddress = value;
    }

    /**
     * Gets the value of the reciverMobile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReciverMobile() {
        return reciverMobile;
    }

    /**
     * Sets the value of the reciverMobile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReciverMobile(String value) {
        this.reciverMobile = value;
    }

    /**
     * Gets the value of the reciverdate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReciverdate() {
        return reciverdate;
    }

    /**
     * Sets the value of the reciverdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReciverdate(XMLGregorianCalendar value) {
        this.reciverdate = value;
    }

    /**
     * Gets the value of the reciverTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReciverTime() {
        return reciverTime;
    }

    /**
     * Sets the value of the reciverTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReciverTime(String value) {
        this.reciverTime = value;
    }

}
