package git.matheusoliveira04.api.fintrack.file.exporter.factory;

import git.matheusoliveira04.api.fintrack.dto.response.EntryResponse;
import git.matheusoliveira04.api.fintrack.file.exception.FileProcessingException;
import git.matheusoliveira04.api.fintrack.file.exporter.contract.FileExporter;
import git.matheusoliveira04.api.fintrack.file.exporter.impl.EntryCsvExporter;
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

@Component
public class EntryFileExporterFactory {

    private final Logger logger = LoggerFactory.getLogger(EntryFileExporterFactory.class);
    private final Map<String, FileExporter<EntryResponse>> fileExporterMap = new HashMap<>();

    public EntryFileExporterFactory(ApplicationContext context) {
        registerExporters(context);
    }

    private void registerExporters(ApplicationContext context) {
        fileExporterMap.put(APPLICATION_XLSX, context.getBean(EntryXlsxExporter.class));
        fileExporterMap.put(TEXT_CSV, context.getBean(EntryCsvExporter.class));
    }

    public FileExporter<EntryResponse> getExporter(String acceptHeader) {
        var fileExporter = fileExporterMap.get(acceptHeader);
        if (Objects.isNull(fileExporter)) {
            logger.error("Invalid File Format");
            throw new FileProcessingException("Invalid File Format");
        }
        return fileExporter;
    }
}
