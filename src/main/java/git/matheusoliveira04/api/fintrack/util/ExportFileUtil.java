package git.matheusoliveira04.api.fintrack.util;

import git.matheusoliveira04.api.fintrack.file.exporter.MediaTypes;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Validated
@Component
public class ExportFileUtil {

    private static final Map<String, String> EXPORT_FILE_MAP = new HashMap<>();
    private static final String XLSX_VALUE = ".xlsx";
    private static final String CSV_VALUE = ".csv";
    private static final String PDF_VALUE = ".pdf";

    public static String resolveFileName(String baseName, String acceptHeader) {
        return baseName + "_" + LocalDateTime.now() + getExportFileType(acceptHeader);
    }

    private static String getExportFileType(@NotBlank String acceptHeader) {
        registerExportFileTypes();
        return Optional.ofNullable(EXPORT_FILE_MAP.get(acceptHeader)).orElse(PDF_VALUE);
    }

    private static void registerExportFileTypes() {
        EXPORT_FILE_MAP.put(MediaTypes.APPLICATION_XLSX, XLSX_VALUE);
        EXPORT_FILE_MAP.put(MediaTypes.TEXT_CSV, CSV_VALUE);
        EXPORT_FILE_MAP.put(MediaType.APPLICATION_PDF_VALUE, PDF_VALUE);
    }


}
