package com.whiletime.linyeah;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by k on 5/22/15.
 */
@XmlRootElement(name = "SubmitOrdersDesc")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubmitOrdersDesc {

    @XmlElement(name = "AuthUserName")
    private String authUserName = OrderDesc.authUserName;

    @XmlElement(name = "AuthPassword")
    private String authPassword = OrderDesc.authPassword;

    @XmlElement(name = "AuthtokenCode")
    private String authtokenCode = OrderDesc.authtokenCode;

    /**
     * 操作类型 0002：商品信息;0003：生成订单
     */
    @XmlElement(name = "OperationCode")
    private String operationCode = "0003";

    @XmlElement(name = "OrderName")
    private String orderName;
    /**
     * 同文件名
     */
    @XmlElement(name = "CustSequence")
    private String custSequence;

    @XmlElement(name = "OrderCustSeqs")
    private String orderCustSeqs;

    @XmlElement(name = "PrintMarkOrder")
    private String printMarkOrder = "加急";

    @XmlElement(name = "ReciverName")
    private String reciverName = "运营部";

    @XmlElement(name = "ReciverExpress")
    private String reciverExpress = "汇通";

    @XmlElement(name = "reciverAddress")
    private String reciverAddress = "浙江省杭州市";

    @XmlElement(name = "reciverMobile")
    private String reciverMobile = "18668006480";

    /**
     * 要求收货日期(最早为下单日后延一天)
     */
    @XmlElement(name = "Reciverdate")
    private String reciverdate;
    /**
     * 要求时间
     * 1:(0am-12am)
     * 2:(13pm-18pm)
     * 3:(19pm-24pm)
     */
    @XmlElement(name = "ReciverTime")
    private String reciverTime = "1";

    public void setOrderName(String orderName) {
        this.orderName = OrderDesc.PREFIX + orderName;
    }

    public void setCustSequence(String custSequence) {
        this.custSequence = custSequence;
    }

    public void setOrderCustSeqs(String orderCustSeqs) {
        this.orderCustSeqs = orderCustSeqs;
    }

    public void setReciverdate(String reciverdate) {
        this.reciverdate = reciverdate;
    }
}
