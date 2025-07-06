package git.matheusoliveira04.api.fintrack.file.exporter.impl;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.file.exporter.contract.FileExporter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntryPdfExporter implements FileExporter<Entry> {

    @Override
    public Resource exportFile(List<Entry> list) throws Exception {
        return null;
    }
}
