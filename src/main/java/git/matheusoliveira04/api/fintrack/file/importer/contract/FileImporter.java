package git.matheusoliveira04.api.fintrack.file.importer.contract;

import java.io.InputStream;
import java.util.List;

public interface FileImporter<T> {

    List<T> importFile(InputStream inputStream) throws Exception;
}
