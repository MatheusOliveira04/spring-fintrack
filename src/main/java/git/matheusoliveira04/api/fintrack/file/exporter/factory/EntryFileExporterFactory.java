package git.matheusoliveira04.api.fintrack.file.exporter.factory;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.file.exception.FileProcessingException;
import git.matheusoliveira04.api.fintrack.file.exporter.contract.FileExporter;
import git.matheusoliveira04.api.fintrack.file.exporter.impl.EntryCsvExporter;
import git.matheusoliveira04.api.fintrack.file.exporter.impl.EntryPdfExporter;
import git.matheusoliveira04.api.fintrack.file.exporter.impl.EntryXlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static git.matheusoliveira04.api.fintrack.file.exporter.MediaTypes.APPLICATION_XLSX;
import static git.matheusoliveira04.api.fintrack.file.exporter.MediaTypes.TEXT_CSV;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Component
public class EntryFileExporterFactory {

    private final Logger logger = LoggerFactory.getLogger(EntryFileExporterFactory.class);
    private final Map<String, FileExporter<Entry>> fileExporterMap = new HashMap<>();

    public EntryFileExporterFactory(ApplicationContext context) {
        registerExporters(context);
    }

    private void registerExporters(ApplicationContext context) {
        fileExporterMap.put(APPLICATION_XLSX, context.getBean(EntryXlsxExporter.class));
        fileExporterMap.put(TEXT_CSV, context.getBean(EntryCsvExporter.class));
        fileExporterMap.put(APPLICATION_PDF_VALUE, context.getBean(EntryPdfExporter.class));
    }

    public FileExporter<Entry> getExporter(String acceptHeader) {
        FileExporter<Entry> fileExporter = fileExporterMap.get(acceptHeader);
        if (Objects.isNull(fileExporter)) {
            logger.error("Invalid File Format");
            throw new FileProcessingException("Invalid File Format");
        }
        return fileExporter;
    }
}
