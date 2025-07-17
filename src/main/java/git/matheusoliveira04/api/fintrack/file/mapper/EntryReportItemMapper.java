package git.matheusoliveira04.api.fintrack.file.mapper;

import git.matheusoliveira04.api.fintrack.dto.request.EntryReportItemRequest;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class EntryReportItemMapper {

    private static final String UNDEFINED = "Undefined";
    private static final Double ZERO = 0.0;
    private static final Boolean FALSE = false;

    public EntryReportItemRequest toReportItem(Entry entry) {
        var entryOptional = Optional.ofNullable(entry);

       return EntryReportItemRequest.builder()
               .description(entryOptional.map(Entry::getDescription).orElse(UNDEFINED))
               .value(entryOptional.map(entryMap -> entryMap.getValue().doubleValue()).orElse(ZERO))
               .paid(entryOptional.map(Entry::getPaid).orElse(FALSE))
               .type(entryOptional.map(Entry::getCategory).map(category -> category.getType().getDescription()).orElse(UNDEFINED))
               .build();
    }

    public List<EntryReportItemRequest> toReportItems(List<Entry> entries) {
        return entries.stream().map(this::toReportItem).toList();
    }
}
