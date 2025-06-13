    package git.matheusoliveira04.api.fintrack.file.importer.factory;

    import git.matheusoliveira04.api.fintrack.dto.request.EntryRequest;
    import git.matheusoliveira04.api.fintrack.file.exception.FileProcessingException;
    import git.matheusoliveira04.api.fintrack.file.importer.contract.FileImporter;
    import git.matheusoliveira04.api.fintrack.file.importer.impl.EntryCsvImporter;
    import git.matheusoliveira04.api.fintrack.file.importer.impl.EntryXlsxImporter;
    import jakarta.validation.constraints.NotBlank;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.context.ApplicationContext;
    import org.springframework.stereotype.Component;
    import org.springframework.validation.annotation.Validated;

    import java.util.HashMap;
    import java.util.Map;
    import java.util.Objects;

    @Validated
    @Component
    public class EntryFileImporterFactory {

        private static final Logger logger = LoggerFactory.getLogger(EntryFileImporterFactory.class);
        private final Map<String, FileImporter<EntryRequest>> fileImporterMap = new HashMap<>();

        public EntryFileImporterFactory(ApplicationContext context) {
            registerImporters(context);
        }

        private void registerImporters(ApplicationContext context) {
            this.fileImporterMap.put("xlsx", context.getBean(EntryXlsxImporter.class));
            this.fileImporterMap.put("csv", context.getBean(EntryCsvImporter.class));
        }

        public FileImporter<EntryRequest> getImporter(@NotBlank String fileName) {
            var fileImporter = fileImporterMap.get(extractExtension(fileName));
            if (Objects.isNull(fileImporter)) {
                logger.error("Invalid File Format");
                throw new FileProcessingException("Invalid File Format");
            }
                return fileImporter;
        }

        private static String extractExtension(@NotBlank String fileName) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        }
    }
