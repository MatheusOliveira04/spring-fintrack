package git.matheusoliveira04.api.fintrack.util;

import git.matheusoliveira04.api.fintrack.entity.enums.ExportFileType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
@Component
public class ExportFileUtil {

    public static String resolveFileName(String baseName, String acceptHeader) {
        ExportFileType fileType = ExportFileType.fromMediaType(acceptHeader);
        return baseName + "_" + LocalDateTime.now() + fileType.getExtension();
    }
}
