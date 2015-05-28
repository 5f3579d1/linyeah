package com.whiletime.linyeah;

import com.itextpdf.text.pdf.PdfReader;
import com.linyeah.erp.orderdescschema.OrderDesc;
import com.linyeah.erp.ordersubmittemp.SubmitOrdersDesc;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.awt.*;
import java.awt.event.KeyEvent;
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
public class Main extends JFrame {

    private static final String PREFIX = "WSG_";
    private JProgressBar pBar;
    private StyledDocument document;

    private final String userName = "YiEdJgfgRXU=";
    private final String password = "UQ3xBiJfxrJz5q5utMGx1A==";
    private final String authtokenCode = "123456";

    private boolean deleleXMLFile = false;

    private String log;
    Runnable updateLog = () -> {
        try {
            document.insertString(document.getLength(), "\n" + log, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    };

    public Main() {

        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        pBar = new JProgressBar();
        pBar.setStringPainted(true);
        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(450, 0));
        topPanel.add(pBar);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);
        topPanel.add(separator, BorderLayout.SOUTH);
        basic.add(topPanel);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JTextPane textPane = new JTextPane();
        textPane.setText("选择订单目录");
        document = textPane.getStyledDocument();
        textPane.setEditable(false);
        textPanel.add(new JScrollPane(textPane));

        basic.add(textPanel);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton close = new JButton("关闭");
        close.setMnemonic(KeyEvent.VK_C);
        close.addActionListener(event -> System.exit(0));

        JButton openButton = new JButton("选择目录");
        openButton.addActionListener(event -> selectRootDirector());
        bottom.add(openButton);
        bottom.add(close);
        basic.add(bottom);

        setTitle("提交订单");
        setSize(new Dimension(450, 350));
        setResizable(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    private void selectRootDirector() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getPath();
            new Thread(() -> {
                appendLog("当前目录：" + path);
                start(path);
            }).start();
        }
    }

    private void appendLog(String log) {
        this.log = log;
        SwingUtilities.invokeLater(updateLog);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }

    public void start(String path) {
        File root = new File(path);
        File[] files = root.listFiles();

        int i = 0;
        List<String> products = new ArrayList<>();
        if (files != null)
            for (File userFile : files) {

                if (isMultiple(userFile))
                    start(userFile);
                else if (userFile.isDirectory()) {
                    if (i <= 15) {
                        addProducts(products, userFile, false);
                        i++;
                    } else {
                        i = 0;
                        submit(products);
                    }
                }
            }

        if (products.size() > 0)
            submit(products);

        appendLog("完成");
    }

    private void start(File splitFile) {

        File[] files = splitFile.listFiles();

        int i = 0;
        List<String> orderCustSeqs = new ArrayList<>();
        if (files != null)
            for (File file : files) {
                if (file.isDirectory())
                    if (i <= 15) {
                        addProducts(orderCustSeqs, file, true);
                        i++;
                    } else {
                        i = 0;
                        submit(orderCustSeqs);
                    }
            }

        if (orderCustSeqs.size() > 0)
            submit(orderCustSeqs);

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

                appendLog("用户：" + filename);

                FileOperate operate = FileOperateFactory.getInstance(null, pBar);
                appendLog("上传：" + cover);
                boolean cc = operate.deal(userFile.getPath() + "/" + cover);
                appendLog("上传：" + inner);
                boolean ic = operate.deal(userFile.getPath() + "/" + inner);

                if (cc && ic) {

                    OrderDesc.Products.Product product = new OrderDesc.Products.Product();
                    product.setCoverFile(cover);
                    product.setCoverFileSize(coverSize + "");
                    product.setInnerFile(inner);
                    product.setInnerFileSize(innerSize + "");
                    product.setInnpageNumber(new BigInteger(pageNum + ""));

                    String orderDescName = genOrderDesc(product, filename.replace(" ", "_"));
                    appendLog("上传：" + orderDescName);
                    if (deleleXMLFile)
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

            appendLog("创建订单：" + orderCustSeqs);

            orderCustSeqs.clear();
            if (deleleXMLFile)
                new File(submitOrderDescName).deleteOnExit();

        } else {
            appendLog("该订单失败");
        }
    }

    private String genOrderDesc(OrderDesc.Products.Product product, String name) {

        String filename = PREFIX + name + ".xml";

        OrderDesc obj = new OrderDesc();

        obj.setAuthUserName(userName);
        obj.setAuthPassword(password);
        obj.setAuthtokenCode(authtokenCode);
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
        product.setCovMatriCode("ZZ0007");
        product.setCovSinOrDblSide("1");
        product.setCovpageColor("1");
        product.setCovPrtProcesse1("YH043-YHS0146");
        product.setCovPrtProcesse2("");
        product.setCovPrtProcesse3("");
        product.setInnMatriCode("ZZ0005");
        product.setInnSinOrDblSide(new BigInteger("1"));
        product.setInnpageColor(new BigInteger("1"));
        product.setInnPrtProcesse1("YH043-YHS0146");
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
        obj.setAuthtokenCode(authtokenCode);
        obj.setOperationCode("0003");
        obj.setPrintMarkOrder("加急");
        obj.setReciverName("运营部");
        obj.setReciverProvince("浙江省");
        obj.setReciverExpress("汇通");
        obj.setReciverAddress("浙江省杭州市");
        obj.setReciverMobile("18668006480");
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
