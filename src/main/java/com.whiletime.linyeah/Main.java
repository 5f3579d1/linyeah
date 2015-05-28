package com.whiletime.linyeah;

import com.itextpdf.text.pdf.PdfReader;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Main class
 * TODO: 套数选择
 * Created by k on 5/25/15.
 */
public class Main extends JFrame {

    private JProgressBar pBar;
    private StyledDocument document;

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
        Main main = new Main();
        main.setVisible(true);
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
                    appendLog("用户：" + userFile.getName());
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
        if (files != null) {
            for (File file : files)
                if (file.isDirectory())
                    return true;
        }
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

                FTPHelper ftpHelper = FTPHelper.getInstance(pBar);
                appendLog("上传：" + cover);
                boolean cc = ftpHelper.upload(userFile.getPath() + "/" + cover);
                appendLog("上传：" + inner);
                boolean ic = ftpHelper.upload(userFile.getPath() + "/" + inner);

                if (cc && ic) {

                    Product product = new Product();
                    if (split)
                        product.setName(userFile.getParentFile().getName());
                    else
                        product.setName(userFile.getName());

                    product.setCoverFile(cover);
                    product.setCoverFileSize(coverSize);
                    product.setInnerFile(inner);
                    product.setInnerFileSize(innerSize);
                    product.setInnpageNumber(pageNum);

                    String orderDescName = genOrderDesc(product, userFile.getName().replace(" ", "_"));
                    appendLog("上传：" + orderDescName);
                    ftpHelper.upload(orderDescName);
                    if (deleleXMLFile)
                        new File(orderDescName).deleteOnExit();

                    orderCustSeqs.add(orderDescName);
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

            FTPHelper ftpHelper = FTPHelper.getInstance(pBar);
            appendLog("上传：" + submitOrderDescName);
            ftpHelper.upload(submitOrderDescName);

            appendLog("创建订单：" + orderCustSeqs);

            orderCustSeqs.clear();
            if (deleleXMLFile)
                new File(submitOrderDescName).deleteOnExit();

        } else {
            appendLog("该订单失败");
        }
    }

    private String genOrderDesc(Product product, String name) {
        String filename = OrderDesc.PREFIX + name + ".xml";

        OrderDesc orderDesc = new OrderDesc();
        List<Product> products = new ArrayList<>();
        products.add(product);
        orderDesc.setProducts(products);

        try {
            JAXBContext context = JAXBContext.newInstance(OrderDesc.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

            OrderDesc obj = new OrderDesc();
            obj.setCustSequence(filename);
            obj.setProducts(products);

            marshaller.marshal(obj, new File(filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String genSubmitOrderDesc(String baseName, List<String> orderCustSeqs) {

        SubmitOrdersDesc obj = new SubmitOrdersDesc();
        obj.setOrderName(baseName);
        String custSequence = OrderDesc.PREFIX + baseName + ".xml";
        obj.setCustSequence(custSequence);

        StringBuilder custSeqsStr = new StringBuilder();
        int size = orderCustSeqs.size();
        for (int i = 0; i < size; i++)
            if (i != size - 1)
                custSeqsStr.append(orderCustSeqs.get(i) + ",");
            else
                custSeqsStr.append(orderCustSeqs.get(i));
        obj.setOrderCustSeqs(custSeqsStr.toString());

        obj.setReciverdate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        try {
            JAXBContext context = JAXBContext.newInstance(SubmitOrdersDesc.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
            marshaller.marshal(obj, new File(custSequence));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return custSequence;
    }

}
