/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.export;
import static net.sf.dynamicreports.report.builder.DynamicReports.field;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.narss.sdss.controllers.Properties;
import org.narss.sdss.controllers.Templates;
import static org.narss.sdss.controllers.Templates.boldCenteredStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class WaterReport {
        static double[][] results = null;
    public static void build(double[][] results,String OutputFilepath, String pdfname, String title, String headertitle) throws ParserConfigurationException, SAXException, Exception {
       
      try {
          
           
            String performfile =Properties.SAMPLES1;
            title = "SDSS Report";
            headertitle = " The Following tables show the result before and after analysis";
            String pdfpath = OutputFilepath + pdfname;
            JasperPdfExporterBuilder pdfExporter = export.pdfExporter(pdfpath);
            
            JRXmlDataSource perdataSource = new JRXmlDataSource(performfile, "Points/Point");
            FieldBuilder<String> id = field("ID", type.stringType());
            FieldBuilder<String> SampleSite = field("SampleSite", type.stringType());
            FieldBuilder<String> Chlorophyll = field("XOfChlorophyll", type.stringType());
            FieldBuilder<String> SSC = field("XOfSSC", type.stringType());
            FieldBuilder<String> TDS = field("XOfTDS", type.stringType());
            FieldBuilder<String> TSS = field("XOfTSS", type.stringType());
            FieldBuilder<String> Turbidity = field("XOfTurbidity", type.stringType());
            FieldBuilder<String> YChlorophyll = field("YOfChlorophyll", type.stringType());
            FieldBuilder<String> YSSC = field("YOfSSC", type.stringType());
            FieldBuilder<String> YTDS = field("YOfTDS", type.stringType());
            FieldBuilder<String> YTSS = field("YOfTSS", type.stringType());
            FieldBuilder<String> YTurbidity = field("YOfTurbidity", type.stringType());
            FieldBuilder<String> resChlorophyll = field("ResultOfChlorophyll", type.stringType());
            FieldBuilder<String> resSSC = field("ResultOfSSC", type.stringType());
            FieldBuilder<String> resTDS = field("ResultOfTDS", type.stringType());
            FieldBuilder<String> resTSS = field("ResultOfTSS", type.stringType());
            FieldBuilder<String> resTurbidity = field("ResultOfTurbidity", type.stringType());
          JasperReportBuilder report1 = report()
                    .setTemplate(Templates.reportTemplate)
           
                    .columns(
                             col.column("ID", id),
                            col.column("SampleSite", SampleSite),
                            col.column("XOfChlorophyll", Chlorophyll) ,
                       //   col.column("YOfChlorophyll", YChlorophyll) ,
                          col.column("ResultOfChlorophyll", resChlorophyll) ,
                          col.column("XOfSSC", SSC) , 
                         //  col.column("YOfSSC", YSSC) , 
                           col.column("ResultOfSSC", resSSC) ,
                            col.column("XOfTDS", TDS) ,
                          //  col.column("YOfTDS", YTDS) , 
                            col.column("ResultOfTDS", resTDS) ,
                            col.column("XOfTSS", TSS) ,
                          //  col.column("YOfTSS", YTSS) ,
                            col.column("ResultOfTSS", resTSS),
                            col.column("XOfTurbidity", Turbidity),
                            col.column("ResultOfTurbidity", resTurbidity)
                          //  col.column("YOfTurbidity", YTurbidity)    
                    )
                    .title(Templates.createTitleComponent(title))
                    .pageHeader(createTextField(headertitle))
                    .addTitle(Components.text("\n Nile River is the only source of surface water for the nearby communities. Due to increased pollution levels, water quality of Nile River is being continuously deteriorated. Water quality refers to the physical, chemical and biological properties of water. It may be degraded by the presence of wastes, nutrients, microorganisms, pesticides, heavy metals and sediments. This Message to discuss the application of remote sensing in monitoring the water quality, depending on some of those crucial parameters, which can be recognized in hyper spectral data. Therefore, the main purpose of this study is to assess the advantages and accuracy of using some remote sensing techniques in monitoring the water quality. On other words, the first purpose of this research is to integrate remote sensing data with a water quality model comprising more accurate initial inputs. The second purpose is to enhance the contribution of geospatial modeling in the process of monitoring and assessment of sewage and agricultural pollutants in the Nile water in Egypt and Sudan.\n"))
                    .pageFooter(Templates.footerComponent)
                    .setDataSource(perdataSource);

           /* for (int i = 0; i < creiterais.size(); i++) {

               
            }*/
           //  TextColumnBuilder<Integer> Columnreal = col.column("REAL (After MCDA)", quantityField); aa7ot hna valid or not 
          //   report1.addColumn(Columnreal);
           /*for (int i=0;i<16;i++)
           {for(int j=0 ;j<5;j++)
           {  
               String  r =Double.toString(result[i][j]);
                FieldBuilder<String> real = field("y", type.stringType());
                TextColumnBuilder<String> Column = col.column("", r);
                report1.addColumn(Column);
             
           }
           }*/
            report1.toPdf(pdfExporter);
        } catch (DRException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();

        }
    }
   
     private static TextFieldBuilder<String> createTextField(String label) {

        return cmp.text(label).setStyle(boldCenteredStyle);

    }
    /* public static void main(String[] args) throws SAXException, Exception{
        build(results,"E:/", "WATER1.pdf", "", "");
        editonpointXml(Properties.SAMPLES1,results);
    }*/
    
    public static void editonpointXml(String source,double[][] results) throws TransformerException{
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document srcdoc = dBuilder.parse(source);
        // Node models = doc.getElementsByTagName("Models").item(0);
           // List<String> reals = new ArrayList<String>();
            NodeList n3List = srcdoc.getElementsByTagName("Point");
           for (int i = 0; i < n3List.getLength(); i++)
           {
               String yofc=Double.toString(results[i][0]);
           Element YOfChlorophyll = srcdoc.createElement("YOfChlorophyll");
            YOfChlorophyll.appendChild(srcdoc.createTextNode(yofc));
            n3List.item(i).appendChild(YOfChlorophyll);
            
            
             Element ResultOfChlorophyll = srcdoc.createElement("ResultOfChlorophyll");
            ResultOfChlorophyll.appendChild(srcdoc.createTextNode("VALID"));
            n3List.item(i).appendChild(ResultOfChlorophyll);
            String yofSSC=Double.toString(results[i][1]);
             Element YOfSSC = srcdoc.createElement("YOfSSC");
            YOfSSC.appendChild(srcdoc.createTextNode(yofSSC));
            n3List.item(i).appendChild(YOfSSC);
            
             Element ResultOfSSC = srcdoc.createElement("ResultOfSSC");
            ResultOfSSC.appendChild(srcdoc.createTextNode("InVALID"));
            n3List.item(i).appendChild(ResultOfSSC);
            String yofTDS=Double.toString(results[i][2]);
             Element YOfTDS = srcdoc.createElement("YOfTDS");
            YOfTDS.appendChild(srcdoc.createTextNode(yofTDS));
            n3List.item(i).appendChild(YOfTDS);
            
            
             Element ResultOfTDS = srcdoc.createElement("ResultOfTDS");
            ResultOfTDS.appendChild(srcdoc.createTextNode("InVALID"));
            n3List.item(i).appendChild(ResultOfTDS);
            String yofTSS=Double.toString(results[i][3]);
             Element YOfTSS = srcdoc.createElement("YOfTSS");
            YOfTSS.appendChild(srcdoc.createTextNode(yofTSS));
            n3List.item(i).appendChild(YOfTSS);
            
             
            
             Element ResultOfTSS = srcdoc.createElement("ResultOfTSS");
            ResultOfTSS.appendChild(srcdoc.createTextNode("InVALID"));
            n3List.item(i).appendChild(ResultOfTSS);
              String yofTurbidity=Double.toString(results[i][4]);
           Element YOfTurbidity = srcdoc.createElement("YOfTurbidity");
            YOfTurbidity.appendChild(srcdoc.createTextNode(yofTurbidity));
            n3List.item(i).appendChild(YOfTurbidity);

               Element ResultOfTurbidity = srcdoc.createElement("ResultOfTurbidity");
            ResultOfTurbidity.appendChild(srcdoc.createTextNode("VALID"));
            n3List.item(i).appendChild(ResultOfTurbidity);
          
           }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source1 = new DOMSource(srcdoc);
		StreamResult result = new StreamResult(new File(source));
		transformer.transform(source1, result);
        } catch (SAXException sax) {
            sax.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }// catch (TransformerConfigurationException ex) {
          //  Logger.getLogger(XmlDatasourceReport.class.getName()).log(Level.SEVERE, null, ex);
        //} //catch (TransformerException ex) {
           // Logger.getLogger(XmlDatasourceReport.class.getName()).log(Level.SEVERE, null, ex);
       // }

    }
     
    
   
 

}
