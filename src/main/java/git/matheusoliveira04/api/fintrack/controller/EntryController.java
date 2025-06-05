package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.dto.request.EntryRequest;
import git.matheusoliveira04.api.fintrack.dto.response.EntryPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.EntryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.mapper.CategoryMapper;
import git.matheusoliveira04.api.fintrack.mapper.EntryMapper;
import git.matheusoliveira04.api.fintrack.mapper.EntryPageMapper;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.EntryService;
import git.matheusoliveira04.api.fintrack.util.TokenUtil;
import git.matheusoliveira04.api.fintrack.validation.annotation.ValidUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/entry")
public class EntryController {

    private CategoryService categoryService;
    private CategoryMapper categoryMapper;
    private EntryMapper entryMapper;
    private EntryPageMapper entryPageMapper;
    private EntryService entryService;
    private TokenUtil tokenUtil;

    public EntryController(CategoryService categoryService, CategoryMapper categoryMapper, EntryMapper entryMapper, EntryPageMapper entryPageMapper, EntryService entryService, TokenUtil tokenUtil) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.entryMapper = entryMapper;
        this.entryPageMapper = entryPageMapper;
        this.entryService = entryService;
        this.tokenUtil = tokenUtil;
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('BASIC')")
    @PostMapping
    public ResponseEntity<EntryResponse> insert(
            @RequestBody @Valid EntryRequest entryRequest,
            @RequestHeader("Authorization") String token,
            UriComponentsBuilder uriBuilder
    ) {
        User user = tokenUtil.getUserByToken(token);
        Category category = categoryService.findByIdAndUserId(UUID.fromString(entryRequest.getCategoryId()), user.getId());
        Entry entry = entryMapper.toEntry(entryRequest, category, user);
        Entry entryInserted = entryService.insert(entry);

        return ResponseEntity
                .created(uriBuilder.path("/api/v1/entry/{id}").buildAndExpand(entryInserted.getId()).toUri())
                .body(entryMapper.toEntryResponse(entryInserted, categoryMapper));
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('BASIC')")
    @GetMapping("/user")
    public ResponseEntity<EntryPageResponse> findAllByUser(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size,
            @RequestHeader("Authorization") String token
            ) {
        UUID userId = tokenUtil.getUserIdByToken(token);
        Page<Entry> allByUserId = entryService.findAllByUserId(userId, page, size);
        List<EntryResponse> entryResponse = entryMapper.toEntryResponse(allByUserId.toList(), categoryMapper);

        return ResponseEntity.ok(entryPageMapper.toEntryPageResponse(entryResponse, allByUserId));
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('BASIC')")
    @GetMapping("/{id}")
    public ResponseEntity<EntryResponse> findByIdAndUser(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestHeader("Authorization") String token) {
        UUID userId = tokenUtil.getUserIdByToken(token);
        Entry entryFound = entryService.findByIdAndUserId(UUID.fromString(id), userId);
        return ResponseEntity.ok(entryMapper.toEntryResponse(entryFound, categoryMapper));
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('BASIC')")
    @PutMapping("/{id}")
    public ResponseEntity<EntryResponse> update(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestBody @Valid EntryRequest entryRequest,
            @RequestHeader("Authorization") String token
    ) {
        User user = tokenUtil.getUserByToken(token);
        Category category = categoryService.findByIdAndUserId(UUID.fromString(entryRequest.getCategoryId()), user.getId());
        Entry entry = entryMapper.toEntry(entryRequest, category, user);
        entry.setId(UUID.fromString(id));
        return ResponseEntity.ok(entryMapper.toEntryResponse(entryService.update(entry), categoryMapper));
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PreAuthorize("hasRole('BASIC')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestHeader("Authorization") String token) {
        UUID userId = tokenUtil.getUserIdByToken(token);
        UUID entryId = UUID.fromString(id);
        entryService.delete(entryId, userId);
      return ResponseEntity.noContent().build();
    }


}
