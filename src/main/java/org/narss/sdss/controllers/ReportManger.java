package org.narss.sdss.controllers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import static org.apache.commons.io.IOUtils.copy;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import static org.narss.sdss.controllers.Templates.boldCenteredStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 */
public class ReportManger {

    private static List<Double> colum = new ArrayList<Double>();

    public ReportManger() {

    }

    public static void build(String inputFilepath, String OutputFilepath, String pdfname,String performfile, String title, String description,String chartType) throws ParserConfigurationException, SAXException, Exception {
        try {
           colum.clear();
            // performfile = Properties.MCDA_INPUT + "//attributes.xml";
            copyXml(inputFilepath, performfile);
           List<String> creiterais = tabledatasource(performfile);
           // title = "SDSS Report";
           String headertitle = "The Following tables show the result before and after analysis";
            String pdfpath = OutputFilepath + pdfname+".pdf";
            JasperPdfExporterBuilder pdfExporter = export.pdfExporter(pdfpath);
            FieldBuilder<String> itemField = field("alternativeID", type.stringType());
            FieldBuilder<Integer> quantityField = field("real", type.integerType());
            JRXmlDataSource perdataSource = new JRXmlDataSource(performfile, "XMCDA/performanceTable/alternativePerformances");
           JFreeChart chart = null;
            if(chartType.equals("Bar_Chart")){
              chart = createBarChart(createBarDataset(inputFilepath));
           }
           else if(chartType.equals("Pie_Chart")){
              chart = createPieChart(createPieDataset(inputFilepath));
           }
         
            BufferedImage image = chart.createBufferedImage(1000, 1000);
            JasperReportBuilder report1 = report()
                    .setTemplate(Templates.reportTemplate)
                    .columns(
                            col.column("Alternative", itemField)            
                    )
                    .title(Templates.createTitleComponent(title))
                    .pageHeader(createTextField(headertitle))
                      .addTitle(Components.text(description))
                            //  "\n NARSS decision support system final report, soil suitability analysis for several crops to determine the overall suitability of a given area or a farm,With  higher frequency of dry periods, efficient land utilisation for agricultural production systems is required for the survival of most farms in El Alamein. Therefore land evaluation techniques and their resulting soil or land suitability maps must address the economic viability and provide information for management decisions at field or farm scale. Modern precision agricultural practices require across-farm and/or within-field soil variability which should be accounted for in the suitability assessment for it to be an effective tool for management decisions. Soil function under a number of land uses should also be assessed as it provides different options to the farmer and thus reduces the farmer’s dependency on a single land use. \n"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(perdataSource);

            for (int i = 0; i < creiterais.size(); i++) {

                FieldBuilder<String> real = field("performance/real_" + creiterais.get(i), type.stringType());
                TextColumnBuilder<String> Column = col.column(creiterais.get(i), real);
                report1.addColumn(Column);

            }
             TextColumnBuilder<Integer> Columnreal = col.column("REAL (After MCDA)", quantityField);
             report1.addColumn(Columnreal);
             report1.summary(cmp.horizontalFlowList(cmp.image(image).setDimension(575, 450)));
            report1.toPdf(pdfExporter);
        } catch (DRException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();

        }
    }

     static List<String> tabledatasource(String filepath) throws Exception {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(filepath);
        List<String> criteriaName = new ArrayList<String>();
        Node mainnode = doc.getElementsByTagName("alternativePerformances").item(0);
        NodeList list = mainnode.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if ("performance".equals(node.getNodeName())) {
                NodeList list2 = node.getChildNodes();
                for (int j = 0; j < list2.getLength(); j++) {
                    Node node2 = list2.item(j);
                    if ("criterionID".equals(node2.getNodeName())) {
                        criteriaName.add(node2.getTextContent());
                    }
                }

            }
        }
        return criteriaName;
    }

    
   

    private static void copyXml(String source, String target){
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document srcdoc = dBuilder.parse(source);
            Document doctarget = dBuilder.parse(target);
            List<String> reals = new ArrayList<String>();
            NodeList performancelist = doctarget.getElementsByTagName("alternativePerformances");
            NodeList n3List = srcdoc.getElementsByTagName("alternativeValue");
            for (int x = 0; x < n3List.getLength(); x++) {
                Node nNode = n3List.item(x);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                }
                Element eElement = (Element) nNode;
                String line1 = eElement.getElementsByTagName("real").item(0).getTextContent();
                reals.add(line1);
            }
            for (int i = 0; i < performancelist.getLength(); i++) {

                Element real = doctarget.createElement("real");
                real.appendChild(doctarget.createTextNode(reals.get(i)));
                performancelist.item(i).appendChild(real);
            }
            DOMSource source1 = new DOMSource(doctarget);
            StreamResult result = new StreamResult(new File(target));
            TransformerFactory.newInstance().newTransformer().transform(source1, result);
        } catch (SAXException sax) {
            sax.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ReportManger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ReportManger.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static TextFieldBuilder<String> createTextField(String label) {

        return cmp.text(label).setStyle(boldCenteredStyle);

    }
    //--------------------------------------------------------------------------
    public static void buildwithoutchart(String inputFilepath, String OutputFilepath, String pdfname, String title, String headertitle) throws ParserConfigurationException, SAXException, Exception {
        try {
           
            String performfile = Properties.MCDA_INPUT + "attributes.xml";
            copyXml(inputFilepath, performfile);
           List<String> creiterais = tabledatasource(performfile);
            title = "SDSS Report";
            headertitle = " The Following tables show the result before and after analysis";
            String pdfpath = OutputFilepath + pdfname;
            JasperPdfExporterBuilder pdfExporter = export.pdfExporter(pdfpath);
            FieldBuilder<String> itemField = field("alternativeID", type.stringType());
            FieldBuilder<Integer> quantityField = field("real", type.integerType());
            JRXmlDataSource perdataSource = new JRXmlDataSource(performfile, "XMCDA/performanceTable/alternativePerformances");
           
            JasperReportBuilder report1 = report()
                    .setTemplate(Templates.reportTemplate)
                    .columns(
                            col.column("Alternative", itemField)            
                    )
                    .title(Templates.createTitleComponent(title))
                    .pageHeader(createTextField(headertitle))
                    .addTitle(Components.text("\n NARSS decision support system final report, soil suitability analysis for several crops to determine the overall suitability of a given area or a farm,With  higher frequency of dry periods, efficient land utilisation for agricultural production systems is required for the survival of most farms in El Alamein. Therefore land evaluation techniques and their resulting soil or land suitability maps must address the economic viability and provide information for management decisions at field or farm scale. Modern precision agricultural practices require across-farm and/or within-field soil variability which should be accounted for in the suitability assessment for it to be an effective tool for management decisions. Soil function under a number of land uses should also be assessed as it provides different options to the farmer and thus reduces the farmer’s dependency on a single land use. \n"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(perdataSource);

            for (int i = 0; i < creiterais.size(); i++) {

                FieldBuilder<String> real = field("performance/real_" + creiterais.get(i), type.stringType());
                TextColumnBuilder<String> Column = col.column(creiterais.get(i), real);
                report1.addColumn(Column);

            }
             //TextColumnBuilder<Integer> Columnreal = col.column("REAL (After MCDA)", quantityField);
            // report1.addColumn(Columnreal);
            report1.toPdf(pdfExporter);
        } catch (DRException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();

        }
    }
////////////// charts draw 
     static JFreeChart createBarChart(CategoryDataset dataset) {
double avg =0.0;
        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart3D("Chart Results",
                "Alternative Names",
                "Suitablity VALUES", (CategoryDataset) dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

       double sum = 0;
       //colum.clear();
        // double totalevent = colum.stream().mapToDouble(f -> f.doubleValue()).sum();
        for (int i = 0; i < colum.size(); i++) {
            sum += colum.get(i);
        }
        avg = sum / dataset.getColumnCount();
        for (int j = 0; j < colum.size(); j++) {
            double maxi = Collections.max(colum);
            if (colum.get(j) == maxi) {
                renderer.setSeriesPaint(j, Color.GREEN);
            }
            if (colum.get(j) > avg && maxi != colum.get(j)) {
                renderer.setSeriesPaint(j, Color.YELLOW);
            }
            if (colum.get(j) < avg) {
                renderer.setSeriesPaint(j, Color.RED);
            }
       }

        return chart;
    }
private static JFreeChart createPieChart( PieDataset dataset ) {
      JFreeChart chart = ChartFactory.createPieChart(      
         "Chart Results",   // chart title 
         dataset,          // data    
         true,             // include legend   
         true, 
         false); 
      
    //  chart.setBackgroundPaint(Color.white);

        PiePlot plot = (PiePlot) chart.getPlot();
plot.setBackgroundPaint(Color.white);
       // plot.setSectionPaint("J+1", Color.black);

       // plot.setSectionPaint("J-1", new Color(120, 0, 120));
// or do this, if you are using an older version of JFreeChart:
//plot.setSectionPaint(1, Color.black);
//plot.setSectionPaint(3, new Color(120, 0, 120));
        double sum = 0;
        // double totalevent = colum.stream().mapToDouble(f -> f.doubleValue()).sum();
        for (int i = 0; i < colum.size(); i++) {
            sum += colum.get(i);
        }
        double avg = sum / dataset.getItemCount();
        for (int j = 0; j < colum.size(); j++) {
            double maxi = Collections.max(colum);
            if (colum.get(j) == maxi) {
                //renderer.setSeriesPaint(j, Color.GREEN);
                plot.setSectionPaint(j, Color.GREEN);
            }
            if (colum.get(j) > avg && maxi != colum.get(j)) {
               // renderer.setSeriesPaint(j, Color.YELLOW);
                plot.setSectionPaint(j, Color.YELLOW);
            }
            if (colum.get(j) < avg) {
              //  renderer.setSeriesPaint(j, Color.RED);
                plot.setSectionPaint(j,  Color.RED);
            }
       }
      return chart;
   }
//////////////////////DATASETS
 @SuppressWarnings("empty-statement")
    private static CategoryDataset createBarDataset(String filepath) throws Exception {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        File fXmlFile = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        NodeList n3List = doc.getElementsByTagName("alternativeValue");
        for (int x = n3List.getLength() - 1; x >= 0; x--) {
            Node nNode = n3List.item(x);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            }
            Element eElement = (Element) nNode;
            String line1 = eElement.getElementsByTagName("real").item(0).getTextContent();
            String line2 = line1.replace("\"", "");
            Double yaxis = Double.parseDouble(line2);
            colum.add(yaxis);
            //  System.out.print("values"+yaxis);
            dataset.addValue(yaxis, eElement.getElementsByTagName("alternativeID").item(0).getTextContent(), eElement.getElementsByTagName("alternativeID").item(0).getTextContent());
//row.add();
        }
return dataset;

    }
     @SuppressWarnings("empty-statement")
    private static PieDataset  createPieDataset(String filepath) throws Exception {
        final DefaultPieDataset  dataset = new DefaultPieDataset ();
        File fXmlFile = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        NodeList n3List = doc.getElementsByTagName("alternativeValue");
        for (int x = n3List.getLength() - 1; x >= 0; x--) {
            Node nNode = n3List.item(x);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            }
            Element eElement = (Element) nNode;
            String line1 = eElement.getElementsByTagName("real").item(0).getTextContent();
            String line2 = line1.replace("\"", "");
            Double yaxis = Double.parseDouble(line2);
            colum.add(yaxis);
            //  System.out.print("values"+yaxis);
             
//row.add();
dataset.setValue(eElement.getElementsByTagName("alternativeID").item(0).getTextContent(), yaxis);
        }
        return dataset;

    }
    
void viewreportfile(HttpServletResponse response,String reportname) throws SAXException, Exception{
   try {
         
                String reportDestination = Properties.Reports + reportname+".pdf";
                FileInputStream fis = new FileInputStream(new File(reportDestination));

                copy(fis, response.getOutputStream());
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=" + reportDestination);
                File my_file = new File(reportDestination);

                //  send the file to browser
                OutputStream out = response.getOutputStream();
                FileInputStream in = new FileInputStream(my_file);
                byte[] buffer = new byte[4096];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.flush();
                response.flushBuffer();
            } catch (FileNotFoundException fnf) {
                fnf.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

    }

}
