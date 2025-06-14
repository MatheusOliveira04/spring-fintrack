package git.matheusoliveira04.api.fintrack.file.exporter.contract;

import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter<T> {
    Resource exportFile(List<T> list) throws Exception;
}
