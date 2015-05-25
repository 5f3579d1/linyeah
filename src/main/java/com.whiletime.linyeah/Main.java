package com.whiletime.linyeah;

import com.itextpdf.text.pdf.PdfReader;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Main class
 * Created by k on 5/25/15.
 */
public class Main extends JFrame {

    //TODO: 套数选择
    public Main() {

        setTitle("提交订单");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton openButton = new JButton("选择目录");
        createLayout(openButton);

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                selectRootDirector();
            }
        });
    }

    private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup().addComponent(arg[0]));

        gl.setVerticalGroup(gl.createSequentialGroup().addComponent(arg[0]));
    }


    private void selectRootDirector() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            start(fileChooser.getSelectedFile().getPath());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                Main main = new Main();
                main.setVisible(true);
            }
        });
    }

    public void start(String path) {
        File root = new File(path);
        File[] files = root.listFiles();

        int i = 0;
        List<Product> products = new ArrayList<>();
        if (files != null)
            for (File userFile : files) {

                if (isMultiple(userFile))
                    start(userFile);
                else {
                    if (i <= 15) {
                        addProducts(products, userFile);
                        i++;
                    } else {
                        i = 0;
                        submit(products);
                        products = new ArrayList<>();
                    }
                }
            }

        if (products.size() > 0)
            submit(products);

    }

    private void start(File userFile) {

        File[] files = userFile.listFiles();

        int i = 0;
        List<Product> products = new ArrayList<>();
        if (files != null)
            for (File file : files) {
                if (i <= 15) {
                    addProducts(products, file);
                    i++;
                } else {
                    i = 0;
                    submit(products);
                    products = new ArrayList<>();
                }
            }

        if (products.size() > 0)
            submit(products);

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

    private List<Product> addProducts(List<Product> products, File userFile) {
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

            Product product = new Product();
            product.setSequence(products.size() + 1);
            product.setCoverFile(cover);
            product.setCoverFileSize(coverSize);
            product.setInnerFile(inner);
            product.setInnerFileSize(innerSize);
            product.setInnpageNumber(pageNum);
            products.add(product);

            FTPHelper ftpHelper = FTPHelper.getInstance();
            ftpHelper.connect();
            ftpHelper.upload(userFile.getPath() + "/" + cover);
            ftpHelper.upload(userFile.getPath() + "/" + inner);
            ftpHelper.disconnect();

        }
        return products;
    }

    private void submit(List<Product> products) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
        String baseName = format.format(new Date());
        String orderDescName = genOrderDesc(products, baseName);
        String submitOrderDescName = genSubmitOrderDesc(baseName, orderDescName);
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FTPHelper ftpHelper = FTPHelper.getInstance();
        ftpHelper.connect();
        ftpHelper.upload(orderDescName);
        ftpHelper.upload(submitOrderDescName);
        ftpHelper.disconnect();

        new File(orderDescName).deleteOnExit();
        new File(submitOrderDescName).deleteOnExit();
    }

    private String genOrderDesc(List<Product> products, String baseName) {
        String filename = OrderDesc.PREFIX + "order_desc_" + baseName + ".xml";

        OrderDesc orderDesc = new OrderDesc();
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

    public String genSubmitOrderDesc(String baseName, String orderCustSeqs) {

        SubmitOrdersDesc obj = new SubmitOrdersDesc();
        obj.setOrderName(OrderDesc.PREFIX + baseName);
        String custSequence = OrderDesc.PREFIX + "submit_orders_desc_" + baseName + ".xml";
        obj.setCustSequence(OrderDesc.PREFIX + custSequence);
        obj.setOrderCustSeqs(orderCustSeqs);
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
