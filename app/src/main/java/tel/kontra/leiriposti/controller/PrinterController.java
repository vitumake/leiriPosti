package tel.kontra.leiriposti.controller;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import tel.kontra.leiriposti.model.Message;
import tel.kontra.leiriposti.model.PrintableMessage;
import tel.kontra.leiriposti.model.PrintersNotFoundException;

/**
 * PrinterController class is responsible for managing print services and sending data to the printer.
 * It provides methods to get available print services, set the default print service, and send data to the printer.
 * 
 * This class is used to handle printing tasks in the application.
 * It allows the user to select a printer and send data to it for printing.
 * 
 * @version 1.0
 * @since 0.1
 * 
 */
public class PrinterController {

    private static final Logger LOGGER = LogManager.getLogger(); // Logger for debugging

    private PrintService[] printServices; // List of available print services
    private PrintService defaultPrintService; // Service in use

    /**
     * Constructor for PrinterController class.
     * It initializes the available print services and sets the default print service.
     * 
     * @throws PrintersNotFoundException If no printers are found.
     * @throws PrinterException If an error occurs while initializing the printer job.
     * 
     */
    public PrinterController() {
        // Get print services and default print service
        printServices = PrinterJob.lookupPrintServices(); // Get all available print services

        // Log to console for debugging
        LOGGER.info("Available print services:");
        for (PrintService service : printServices) {
            LOGGER.info(" - " + service.getName()); // Log the name of each service
        }

        // Get the default print service if available
        if (printServices.length > 0) {
            defaultPrintService = printServices[0]; // Set the first service as default
            System.out.println("Default print service: " + defaultPrintService.getName()); // Print the name of the default service
        } else {
            LOGGER.warn("No print services found!"); // Log a warning message
        }
    }

    /**
     * Get the available print services.
     * 
     * @return The array of available print services.
     */
    public PrintService[] getPrintServices() {
        return printServices;
    }

    /**
     * Get the default printer name.
     * 
     * @return The name of the default print service.
     *         Returns null if no default print service is set.
     */
    public String getDefaultPrintServiceName() {
        if (defaultPrintService != null) {
            return defaultPrintService.getName(); // Return the name of the default print service
        } else {
            return null; // No default print service set
        }
    }

    /**
     * Set the default print service.
     * 
     * @param defaultPrintService The default print service to set.
     */
    public void setDefaultPrintService(PrintService defaultPrintService) {
        this.defaultPrintService = defaultPrintService; // Set the default print service
    }

    /**
     * Set the default print service by name.
     * 
     * @param name The name of the print service to set as default.
     */
    public void setPrintServiceByName(String name) {
        for (PrintService service : printServices) {
            if (service.getName().equals(name)) {
                defaultPrintService = service; // Set the default print service by name
                break;
            }
        }
    }

    /**
     * Print the information of the default print service.
     * 
     * This method prints the name and attributes of the default print service.
     */
    public void printServiceInfo() {
        if (defaultPrintService == null) {
            System.out.println("No default print service set!"); // No default print service set
            return;
        } else {
            System.out.println("Default print service: " + defaultPrintService.getName()); // Print the name of the default print service
            for( Attribute a : defaultPrintService.getAttributes().toArray() ) {
                System.out.println("* "+a.getName()+": "+a);
            }
        }
    }

    /**
     * Print the given data using the printable service.
     * 
     * @param data The data to print.
     * @throws PrinterException If an error occurs while printing.
     * @throws PrintersNotFoundException If no printers are found.
     */
    public void sendToPrinter(Message data) throws PrintersNotFoundException, PrinterException {

        // Set attributes for the print job
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(2)); // Set the number of copies to 2
        pras.add(Sides.DUPLEX);

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new PrintableMessage(data));

        // Set the print service to the default one
        if (defaultPrintService != null) {
            job.setPrintService(defaultPrintService); // Set the print service to the default one
        } else {
            throw new PrintersNotFoundException("No printer found!"); // No default print service set
        }

        try {
            job.print(pras); // Print the job with the specified attributes
            System.out.println("Printing job: " + job.getJobName()); // Print the name of the print job
            System.out.println("Printing to: " + defaultPrintService.getName()); // Print the name of the print service used

        } catch (Exception e) {
            System.out.println("Error printing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
