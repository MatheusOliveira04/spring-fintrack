package git.matheusoliveira04.api.fintrack.entity.enums;

import git.matheusoliveira04.api.fintrack.file.exporter.MediaTypes;
import org.springframework.http.MediaType;

import java.util.Arrays;

public enum ExportFileType {

    XLSX(".xlsx", MediaTypes.APPLICATION_XLSX),
    CSV(".csv", MediaTypes.TEXT_CSV),
    PDF(".pdf", MediaType.APPLICATION_PDF_VALUE);

    private final String extension;
    private final String mediaType;

    ExportFileType(String extension, String mediaType) {
        this.extension = extension;
        this.mediaType = mediaType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMediaType() {
        return mediaType;
    }

    public static ExportFileType fromMediaType(String mediaType) {
        return Arrays
                .stream(ExportFileType.values())
                .filter(type -> type.getMediaType().equals(mediaType))
                .findFirst()
                .orElse(PDF);
    }
}
