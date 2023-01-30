package com.ceyentra.anpr.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@CrossOrigin
@RequestMapping("api/v1")
public class FileReaderController {

    @PostMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void scheduleMeetingByEmployee(@RequestPart MultipartFile anprXml, @RequestPart("detectionPicture") MultipartFile detectionPicture, @RequestPart("licensePlatePicture") MultipartFile licensePlatePicture) {
        String projectPath = null;
        try {

            // add the images to a directory created in the project path
            projectPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getParentFile().getAbsolutePath();

            // System.out.println(projectPath); // /home/shinyT480/Ceyentra/Projects/08_ParkTags/read-anpr
            File assetsDir = new File(projectPath + "/src/main/resources/assets");
            assetsDir.mkdir();

            detectionPicture.transferTo(new File(assetsDir.getAbsolutePath() + "/" + detectionPicture.getOriginalFilename()));
            licensePlatePicture.transferTo(new File(assetsDir.getAbsolutePath() + "/" + licensePlatePicture.getOriginalFilename()));
            anprXml.transferTo(new File(assetsDir.getAbsolutePath() + "/" + anprXml.getOriginalFilename()));

            // read xml file
            readANPRXmlFile();

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void readANPRXmlFile() {
        File xmlFile = new File("src/main/resources/assets/anpr.xml");
        //System.out.println(xmlFile.getAbsolutePath());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        try {
            //  instantiate a new Document object from an XML file
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            // get the root node or element of an XML file
            // Element rootNode = doc.getDocumentElement();
            // System.out.println("rootNode : " + rootNode);

            // retrieve nodes by tag name

            String ipAddress = doc.getElementsByTagName("ipAddress").item(0).getTextContent();
            String dateTime = doc.getElementsByTagName("dateTime").item(0).getTextContent();
            String licensePlate = doc.getElementsByTagName("licensePlate").item(0).getTextContent();
            String vehicleType = doc.getElementsByTagName("vehicleType").item(0).getTextContent();
            System.out.println();
            System.out.println("ipAddress : " + ipAddress);
            System.out.println("dateTime : " + dateTime);
            System.out.println("licensePlate : " + licensePlate);
            System.out.println("vehicleType : " + vehicleType);


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
