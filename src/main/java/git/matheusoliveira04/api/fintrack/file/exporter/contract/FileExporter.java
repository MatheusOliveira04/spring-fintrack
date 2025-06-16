package git.matheusoliveira04.api.fintrack.file.exporter.contract;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface FileExporter<T> {
    Resource exportFile(@NotNull List<T> list) throws Exception;
}
