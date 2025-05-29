package git.matheusoliveira04.api.fintrack.mapper;

import git.matheusoliveira04.api.fintrack.dto.request.EntryRequest;
import git.matheusoliveira04.api.fintrack.dto.response.EntryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.service.exception.IntegrityViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.*;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.Optional;

@Validated
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EntryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    Entry mapToEntry(EntryRequest entryRequest);

    default Entry toEntry(
            EntryRequest entryRequest,
            @Context @NotNull @Valid Category category,
            @Context @NotNull @Valid User user) {
        Entry entry = mapToEntry(entryRequest);
        entry.setCategory(category);
        entry.setUser(user);
        return entry;
    }

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "userId", ignore = true)
    EntryResponse toEntryResponse(@NotNull Entry entry, @Context CategoryMapper categoryMapper);

    @AfterMapping
    default void mapToEntryResponse(
            @MappingTarget EntryResponse entryResponse,
            @NotNull @Valid Entry entry,
            @Context CategoryMapper categoryMapper) {
        Category category = Optional.ofNullable(entry.getCategory())
                .orElseThrow(() -> new IntegrityViolationException("Category is missing from entry"));
        User user = Optional.ofNullable(entry.getUser())
                .orElseThrow(() -> new IntegrityViolationException("User is missing from entry"));

        entryResponse.setCategory(categoryMapper.toCategoryResponse(category));
        entryResponse.setUserId(user.getId().toString());
    }

    @AfterMapping
    default void applyDefaults(@MappingTarget Entry entry, EntryRequest request) {
        paidDefaultValue(entry, request);
    }

    private void paidDefaultValue(Entry entry, EntryRequest request) {
        if (Objects.isNull(request.getPaid())) {
            entry.setPaid(false);
        }
    }
}
