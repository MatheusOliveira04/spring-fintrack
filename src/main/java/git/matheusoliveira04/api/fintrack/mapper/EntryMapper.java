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

import java.util.List;
import java.util.Objects;
import static java.util.Optional.*;

@Validated
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EntryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    Entry toEntry(EntryRequest entryRequest, @Context @NotNull @Valid Category category, @Context @NotNull @Valid User user);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "userId", ignore = true)
    EntryResponse toEntryResponse(@NotNull Entry entry, @Context CategoryMapper categoryMapper);

    List<EntryResponse> toEntryResponse(List<Entry> entries, @Context CategoryMapper categoryMapper);

    @AfterMapping
    default void mapToEntry(@MappingTarget Entry entry,
                            EntryRequest request,
                            @Context @NotNull @Valid Category category,
                            @Context @NotNull @Valid User user) {
        entry.setCategory(category);
        entry.setUser(user);
        paidDefaultValue(entry, request);
    }

    @AfterMapping
    default void mapToEntryResponse(
            @MappingTarget EntryResponse entryResponse,
            @NotNull @Valid Entry entry,
            @Context CategoryMapper categoryMapper) {
        ofNullable(entry)
                .map(Entry::getCategory)
                .ifPresent(
                        category -> entryResponse.setCategory(categoryMapper.toCategoryResponse(category))
                );
        ofNullable(entry)
                .map(Entry::getUser)
                        .ifPresent(
                                user -> entryResponse.setUserId(user.getId().toString())
                        );
    }

    private void paidDefaultValue(Entry entry, EntryRequest request) {
        if (Objects.isNull(request.getPaid())) {
            entry.setPaid(false);
        }
    }
}
