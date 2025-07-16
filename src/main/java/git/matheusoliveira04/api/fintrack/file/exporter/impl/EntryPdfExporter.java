package git.matheusoliveira04.api.fintrack.file.exporter.impl;

import git.matheusoliveira04.api.fintrack.dto.request.EntryReportHeaderRequest;
import git.matheusoliveira04.api.fintrack.dto.request.EntryReportItemRequest;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.file.exporter.contract.FileExporter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

@Component
public class EntryPdfExporter implements FileExporter<Entry> {

    private static final String TEMPLATE_PATH = "/templates/EntryReport.jrxml";

    @Override
    public Resource exportFile(List<Entry> list) throws Exception {
        JasperReport report = compileReport();

        List<EntryReportHeaderRequest> reportHeader = list.stream()
                .map(entry -> new EntryReportHeaderRequest(entry.getUser().getName(), entry.getUser().getEmail()))
                .toList();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportHeader);

        List<EntryReportItemRequest> reportItem = list.stream()
                .map(entry -> new EntryReportItemRequest(entry.getDescription(),
                        entry.getValue().doubleValue(),
                        entry.getPaid(),
                        entry.getCategory().getType().getDescription()))
                .toList();

        JRBeanCollectionDataSource tableDataSource = new JRBeanCollectionDataSource(reportItem);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("TABLE_DATA_SOURCE", tableDataSource);

        JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

        byte[] pdfBytes = exportToPdf(print);
        return new ByteArrayResource(pdfBytes);
    }

    private JasperReport compileReport() throws Exception {
        InputStream input = getClass().getResourceAsStream(TEMPLATE_PATH);
        if (Objects.isNull(input)) throw new FileNotFoundException("Template not found: " + TEMPLATE_PATH);
        return JasperCompileManager.compileReport(input);
    }

    private byte[] exportToPdf(JasperPrint jasperPrint) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error while exporting PDF: " + e.getMessage());
        }
    }
}
