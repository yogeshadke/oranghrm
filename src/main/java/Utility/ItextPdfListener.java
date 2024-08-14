package Utility;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.xml.XmlSuite;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class ItextPdfListener implements IReporter {

    private CreatePDFReport pdfReport = new CreatePDFReport();

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        for (ISuite suite : suites) {
            try {
                pdfReport.openPdf();
                suite.getResults().forEach((key, suiteResult) -> {
                    ITestContext context = suiteResult.getTestContext();
                    String suiteInfo = String.format(
                            "Suite Name: %s\nReport Output Directory: %s\nSuite Name: %s\nStart Date Time: %s\nEnd Date Time: %s",
                            context.getName(), context.getOutputDirectory(), context.getSuite().getName(),
                            context.getStartDate(), context.getEndDate());

                    try {
						pdfReport.addParagraph(suiteInfo);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    logTestResults(context.getFailedTests(), "Failed Test Cases");
                    logTestResults(context.getPassedTests(), "Passed Test Cases");
                });
                pdfReport.closePdf();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void logTestResults(IResultMap resultMap, String resultType) {
        System.out.println("------" + resultType + "-----");
        for (ITestNGMethod method : resultMap.getAllMethods()) {
            String testCaseInfo = String.format(
                    "Test Case Name: %s\nDescription: %s\nPriority: %d\nDate: %s",
                    method.getMethodName(), method.getDescription(), method.getPriority(), new Date(method.getDate()));

            System.out.println(testCaseInfo);
            try {
                pdfReport.addParagraph(testCaseInfo);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
}

class CreatePDFReport {

    private FileOutputStream fileOutputStream;
    private Path filePath;
    private Document document;

    public void openPdf() throws IOException, DocumentException {
        filePath = Paths.get(String.format("pdf-%d.pdf", System.currentTimeMillis()));
        fileOutputStream = new FileOutputStream(filePath.toFile());
        document = new Document();
        PdfWriter.getInstance(document, fileOutputStream);
        document.open();
    }

    public void addParagraph(String text) throws DocumentException {
        if (document != null) {
            document.add(new Paragraph(text));
        }
    }

    public void closePdf() {
        if (document != null) {
            document.close();
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
