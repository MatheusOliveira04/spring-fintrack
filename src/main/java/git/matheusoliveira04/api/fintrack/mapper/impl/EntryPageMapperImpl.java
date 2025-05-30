package git.matheusoliveira04.api.fintrack.mapper.impl;

import git.matheusoliveira04.api.fintrack.dto.response.EntryPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.EntryResponse;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.mapper.EntryPageMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntryPageMapperImpl implements EntryPageMapper {

    @Override
    public EntryPageResponse toEntryPageResponse(List<EntryResponse> entryResponses, Page<Entry> entryPage) {
        return EntryPageResponse.builder()
                .entryResponses(entryResponses)
                .totalItems(entryPage.getTotalElements())
                .totalPages(entryPage.getTotalPages())
                .build();
    }
}
