package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.response.EntryPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.EntryResponse;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EntryPageMapper {

    EntryPageResponse toEntryPageResponse(List<EntryResponse> entryResponses, Page<Entry> entryPage);
}
