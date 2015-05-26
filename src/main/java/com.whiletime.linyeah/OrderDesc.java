package com.whiletime.linyeah;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by k on 5/22/15.
 */
@XmlRootElement(name = "OrderDesc")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDesc {

    static final public String PREFIX = "WSG_DJD_";

    @XmlElement(name = "AuthUserName")
    public static String authUserName = "YiEdJgfgRXU";

    @XmlElement(name = "AuthPassword")
    public static String authPassword = "UQ3xBiJfxrJz5q5utMGx1A==";

    @XmlElement(name = "AuthtokenCode")
    public static String authtokenCode = "123456";

    @XmlElement(name = "ProductName")
    private String productName = "胶装书";

    /**
     * 操作类型
     * 0001：询价
     * 0002：下单
     */
    @XmlElement(name = "OperationCode")
    private String operationCode = "0002";

    /**
     * 同文件名
     */
    @XmlElement(name = "CustSequence")
    private String custSequence;

    @XmlElementWrapper(name = "Products")
    @XmlElement(name = "Product")
    private List<Product> products;

    public void setCustSequence(String custSequence) {
        this.custSequence = custSequence;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}

class Product {

    private String name;

    @XmlElement(name = "Sequence")
    private int sequence;

    @XmlElement(name = "ProdCode")
    private int prodCode = 2018;

    @XmlElement(name = "ProdName")
    private String prodName = "胶装书";

    @XmlElement(name = "ProdWidth")
    private int prodWidth = 210;

    @XmlElement(name = "ProdHeight")
    private int prodHeight = 148;

    /**
     * 套数
     */
    @XmlElement(name = "ProdNumber")
    private int prodNumber;

    @XmlElement(name = "PrintMark")
    private String printMark = "胶装书";

    @XmlElement(name = "Bleeding")
    private String bleeding = "";

    @XmlElement(name = "CustSizeFlag")
    private String custSizeFlag = "N";

    @XmlElement(name = "HasCover")
    private String hasCover = "Y";

    @XmlElement(name = "CovMatriCode")
    private String covMatriCode = "ZZ0007";

    /**
     * 封面单面或者双面印刷
     * 单面:0
     * 双面:1
     */
    @XmlElement(name = "CovSinOrDblSide")
    private String covSinOrDblSide = "1";

    @XmlElement(name = "CovpageColor")
    private String covpageColor = "1";
    /**
     * 封面印后工艺
     */
    @XmlElement(name = "CovPrtProcesse1")
    private String covPrtProcesse1 = "YH043-YHS0146";

    @XmlElement(name = "CovPrtProcesse2")
    private String covPrtProcesse2 = "";

    @XmlElement(name = "CovPrtProcesse3")
    private String covPrtProcesse3 = "";

    @XmlElement(name = "InnMatriCode")
    private String innMatriCode = "ZZ0004";

    @XmlElement(name = "InnSinOrDblSide")
    private String innSinOrDblSide = "1";

    /**
     * 内页印刷类型:
     * 黑白：0
     * 彩色：1
     */
    @XmlElement(name = "InnpageColor")
    private String innpageColor = "1";

    @XmlElement(name = "InnpageNumber")
    private int innpageNumber;

    /**
     * 内页印后工艺
     */
    @XmlElement(name = "InnPrtProcesse1")
    private String innPrtProcesse1 = "YH043-YHS0146";

    @XmlElement(name = "InnPrtProcesse2")
    private String innPrtProcesse2 = "";

    @XmlElement(name = "InnPrtProcesse3")
    private String innPrtProcesse3 = "";

    @XmlElement(name = "CoverFile")
    private String coverFile;

    @XmlElement(name = "CoverFileSize")
    private long coverFileSize;

    @XmlElement(name = "InnerFile")
    private String innerFile;

    @XmlElement(name = "InnerFileSize")
    private long innerFileSize;

    public void setName(String name) {
        this.name = name;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setInnpageNumber(int innpageNumber) {
        this.innpageNumber = innpageNumber;
    }

    public void setCoverFile(String coverFile) {
        this.coverFile = OrderDesc.PREFIX + coverFile.substring(0, coverFile.length() - 4) + "_cover.pdf";
    }

    public void setCoverFileSize(long coverFileSize) {
        this.coverFileSize = coverFileSize;
    }

    public void setInnerFile(String innerFile) {
        this.innerFile = OrderDesc.PREFIX + innerFile.substring(0, innerFile.length() - 4) + "_inner.pdf";
    }

    public void setInnerFileSize(long innerFileSize) {
        this.innerFileSize = innerFileSize;
    }

    @Override
    public String toString() {
        return sequence + "：" + name;
    }

}
