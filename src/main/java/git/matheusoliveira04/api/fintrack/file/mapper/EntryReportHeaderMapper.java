package git.matheusoliveira04.api.fintrack.file.mapper;

import git.matheusoliveira04.api.fintrack.dto.request.EntryReportHeaderRequest;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EntryReportHeaderMapper {

    private static final String UNDEFINED = "Undefined";

    public EntryReportHeaderRequest toReportHeader(Entry entry) {
        return EntryReportHeaderRequest.builder()
                .name(Optional.ofNullable(entry).map(Entry::getUser).map(User::getName).orElse(UNDEFINED))
                .email(Optional.ofNullable(entry).map(Entry::getUser).map(User::getEmail).orElse(UNDEFINED))
                .build();

    }

    public List<EntryReportHeaderRequest> toReportHeaders(List<Entry> entries) {
        return entries.stream().map(this::toReportHeader).toList();
    }

}
