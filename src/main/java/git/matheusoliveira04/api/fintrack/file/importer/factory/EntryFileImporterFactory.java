package git.matheusoliveira04.api.fintrack.file.importer.factory;

import git.matheusoliveira04.api.fintrack.dto.request.EntryRequest;
import git.matheusoliveira04.api.fintrack.file.exception.FileProcessingException;
import git.matheusoliveira04.api.fintrack.file.importer.contract.FileImporter;
import git.matheusoliveira04.api.fintrack.file.importer.impl.EntryCsvImporter;
import git.matheusoliveira04.api.fintrack.file.importer.impl.EntryXlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EntryFileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(EntryFileImporterFactory.class);

    private ApplicationContext applicationContext;

    public EntryFileImporterFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public FileImporter<EntryRequest> getImporter(String fileName) throws Exception {
        if (fileName.endsWith(".xlsx")) {
            logger.info("Importing from .xlsx");
            return applicationContext.getBean(EntryXlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            logger.info("Importing from .csv");
            return applicationContext.getBean(EntryCsvImporter.class);
        } else {
            logger.error("Invalid File Format");
            throw new FileProcessingException("Invalid File Format");
        }

    }
}
