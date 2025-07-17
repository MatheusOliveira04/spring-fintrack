package git.matheusoliveira04.api.fintrack.file.exporter.impl;

import git.matheusoliveira04.api.fintrack.dto.request.EntryReportHeaderRequest;
import git.matheusoliveira04.api.fintrack.dto.request.EntryReportItemRequest;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.file.exporter.contract.FileExporter;
import git.matheusoliveira04.api.fintrack.file.mapper.EntryReportHeaderMapper;
import git.matheusoliveira04.api.fintrack.file.mapper.EntryReportItemMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class EntryPdfExporter implements FileExporter<Entry> {

    private static final String TEMPLATE_PATH = "/templates/EntryReport.jrxml";

    private EntryReportHeaderMapper entryReportHeaderMapper;
    private EntryReportItemMapper entryReportItemMapper;

    public EntryPdfExporter(EntryReportHeaderMapper entryReportHeaderMapper, EntryReportItemMapper entryReportItemMapper) {
        this.entryReportHeaderMapper = entryReportHeaderMapper;
        this.entryReportItemMapper = entryReportItemMapper;
    }

    @Override
    public Resource exportFile(List<Entry> list) throws Exception {
        JasperReport report = compileReport();

        JRBeanCollectionDataSource dataSource = createHeaderDataSource(list);
        Map<String, Object> parameters = setParameters(list);

        JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);

        byte[] pdfBytes = generatePdfBytes(print);
        return new ByteArrayResource(pdfBytes);
    }

    private JasperReport compileReport() throws Exception {
        InputStream input = getClass().getResourceAsStream(TEMPLATE_PATH);
        if (Objects.isNull(input)) throw new FileNotFoundException("Template not found: " + TEMPLATE_PATH);
        return JasperCompileManager.compileReport(input);
    }

    private JRBeanCollectionDataSource createHeaderDataSource(List<Entry> list) {
        List<EntryReportHeaderRequest> reportHeader = entryReportHeaderMapper.toReportHeaders(list);
        return new JRBeanCollectionDataSource(reportHeader);
    }

    private Map<String, Object> setParameters(List<Entry> entries) {
        JRBeanCollectionDataSource tableDataSource = getJrBeanCollectionTableDataSource(entries);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("TABLE_DATA_SOURCE", tableDataSource);
        return parameters;
    }

    private JRBeanCollectionDataSource getJrBeanCollectionTableDataSource(List<Entry> list) {
        List<EntryReportItemRequest> reportItem = entryReportItemMapper.toReportItems(list);
        return new JRBeanCollectionDataSource(reportItem);
    }

    private byte[] generatePdfBytes(JasperPrint jasperPrint) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error while exporting PDF: " + e.getMessage());
        }
    }
}
