package com.whiletime.linyeah;

import com.itextpdf.text.pdf.PdfReader;
import com.linyeah.erp.orderdescschema.OrderDesc;
import com.linyeah.erp.ordersubmittemp.SubmitOrdersDesc;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Main class
 * TODO: 套数选择
 * Created by k on 5/25/15.
 */
public class Main {

    public static final String PREFIX = "WSG_";

    private final String userName = "YiEdJgfgRXU=";
    private final String password = "UQ3xBiJfxrJz5q5utMGx1A==";
    private final String authTokenCode = "123456";

    private boolean deleteXMLFile = false;

    private static PaperType paper = PaperType.EGGSHELL;

    public static void main(String[] args) {
        if (args.length != 0)
            paper = PaperType.KRAFT;

        new Main().start(".");
    }

    List<String> products = new ArrayList<>();

    public void start(String path) {
        File root = new File(path);
        File[] files = root.listFiles();
        if (files != null)
            for (File userFile : files) {
                if (isMultiple(userFile))
                    splitBook(userFile);
                else if (userFile.isDirectory())
                    addProducts(products, userFile, false);
            }

        if (products.size() > 0)
            submit(products);
    }

    private void splitBook(File splitFile) {
        File[] files = splitFile.listFiles();
        if (files != null)
            for (File file : files)
                if (file.isDirectory())
                    addProducts(products, file, true);
    }

    private boolean isMultiple(File directory) {
        File[] files = directory.listFiles();
        if (files != null)
            for (File file : files)
                if (file.isDirectory())
                    return true;
        return false;
    }

    private List<String> addProducts(List<String> orderCustSeqs, File userFile, boolean split) {

        if (orderCustSeqs.size() >= 15) {
            submit(orderCustSeqs);
            orderCustSeqs.clear();
        }

        String cover = null;
        String inner = null;
        int pageNum = 0;
        long coverSize = 0;
        long innerSize = 0;

        File[] files = userFile.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (file.isFile() && name.endsWith(".pdf"))
                    if (name.contains("封面")) {
                        coverSize = file.length();
                        cover = name;
                    } else {
                        inner = name;
                        try {
                            PdfReader reader = new PdfReader(file.getPath());
                            pageNum = reader.getNumberOfPages();
                            innerSize = reader.getFileLength();
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }

            if (cover != null && inner != null) {

                String filename;
                if (split)
                    filename = userFile.getParentFile().getName() + userFile.getName();
                else
                    filename = userFile.getName();


                boolean cc = CopyToLocal.getInstance().deal(userFile.getPath() + "/" + cover);
                boolean ic = CopyToLocal.getInstance().deal(userFile.getPath() + "/" + inner);

                if (cc && ic) {

                    OrderDesc.Products.Product product = new OrderDesc.Products.Product();
                    product.setCoverFile(PREFIX + cover);
                    product.setCoverFileSize(coverSize + "");
                    product.setInnerFile(PREFIX + inner);
                    product.setInnerFileSize(innerSize + "");
                    product.setInnpageNumber(new BigInteger(pageNum + ""));

                    String orderDescName = genOrderDesc(product, filename.replace(" ", "_"));
                    if (deleteXMLFile)
                        new File(orderDescName).deleteOnExit();

                    orderCustSeqs.add(orderDescName.substring(0, orderDescName.length() - 4));
                }
            }
        }
        return orderCustSeqs;
    }

    private void submit(List<String> orderCustSeqs) {

        if (orderCustSeqs.size() > 0) {

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
            String baseName = format.format(new Date());
            String submitOrderDescName = genSubmitOrderDesc(baseName, orderCustSeqs);
            orderCustSeqs.clear();
            if (deleteXMLFile)
                new File(submitOrderDescName).deleteOnExit();
        }
    }

    private String genOrderDesc(OrderDesc.Products.Product product, String name) {

        String filename = PREFIX + name + ".xml";

        OrderDesc obj = new OrderDesc();

        obj.setAuthUserName(userName);
        obj.setAuthPassword(password);
        obj.setAuthtokenCode(authTokenCode);
        obj.setProductName("胶装书");
        obj.setOperationCode("0002");
        obj.setCustSequence(filename.substring(0, filename.length() - 4));

        product.setSequence(new BigInteger("1"));
        product.setProdCode("0218");
        product.setProdName("胶装书");
        product.setProdWidth(new BigInteger("210"));
        product.setProdHeight(new BigInteger("148"));
        product.setProdNumber(new BigInteger("1"));
        product.setPrintMark("胶装书");
        product.setBleeding(new BigInteger("0"));
        product.setCustSizeFlag("N");
        product.setHasCover("Y");
        product.setCovMatriCode(paper.getField());
        product.setCovSinOrDblSide("0");

        if (PaperType.KRAFT.equals(paper))
            product.setCovpageColor("0");
        else
            product.setCovpageColor("1");

        product.setCovPrtProcesse1("");
        product.setCovPrtProcesse2("");
        product.setCovPrtProcesse3("");
        product.setInnMatriCode("ZZ0002");
        product.setInnSinOrDblSide(new BigInteger("1"));
        product.setInnpageColor(new BigInteger("1"));
        product.setInnPrtProcesse1("");
        product.setInnPrtProcesse2("");
        product.setInnPrtProcesse3("");

        OrderDesc.Products products = new OrderDesc.Products();
        products.getOrderItemGroup().add(product);
        obj.setProducts(products);

        try {
            JAXBContext context = JAXBContext.newInstance(OrderDesc.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(obj, new File(filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String genSubmitOrderDesc(String baseName, List<String> orderCustSeqs) {

        SubmitOrdersDesc obj = new SubmitOrdersDesc();

        obj.setAuthUserName(userName);
        obj.setAuthPassword(password);
        obj.setAuthtokenCode(authTokenCode);
        obj.setOperationCode("0003");
        obj.setPrintMarkOrder("加急");
        obj.setReciverName("丁缙东");
        obj.setReciverProvince("浙江省");
        obj.setReciverExpress("汇通");
        obj.setReciverAddress("浙江省杭州市余杭区仓前街道良睦路梦想小镇互联网村23号2楼时光书");
        obj.setReciverMobile("13735462890");
        obj.setReciverTime("1");

        obj.setOrderName(baseName);
        String custSequence = PREFIX + baseName + ".xml";
        obj.setCustSequence(custSequence.substring(0, custSequence.length() - 4));

        StringBuilder custSeqsStr = new StringBuilder();
        int size = orderCustSeqs.size();
        for (int i = 0; i < size; i++)
            if (i != size - 1)
                custSeqsStr.append(orderCustSeqs.get(i)).append(",");
            else
                custSeqsStr.append(orderCustSeqs.get(i));
        obj.setOrderCustSeqs(custSeqsStr.toString());

        try {

            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(new Date());
            XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            obj.setReciverdate(calendar);

            JAXBContext context = JAXBContext.newInstance(SubmitOrdersDesc.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, new File(custSequence));
        } catch (JAXBException | DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return custSequence;
    }

}
