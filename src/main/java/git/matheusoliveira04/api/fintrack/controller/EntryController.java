package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.controller.docs.EntryControllerDocs;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static git.matheusoliveira04.api.fintrack.file.exporter.MediaTypes.APPLICATION_XLSX;
import static org.springframework.http.HttpHeaders.*;

@Validated
@RestController
@RequestMapping("/api/v1/entry")
public class EntryController implements EntryControllerDocs {

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

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @PostMapping
    public ResponseEntity<EntryResponse> insert(
            @RequestBody @Valid EntryRequest entryRequest,
            @RequestHeader(AUTHORIZATION) String token,
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

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @GetMapping("/user")
    public ResponseEntity<EntryPageResponse> findAllByUser(
            @PageableDefault(size = 10, page = 0, sort = "description") Pageable pageable,
            @RequestHeader(AUTHORIZATION) String token
            ) {
        UUID userId = tokenUtil.getUserIdByToken(token);
        Page<Entry> allByUserId = entryService.findAllByUserId(userId, pageable);
        List<EntryResponse> entryResponse = entryMapper.toEntryResponse(allByUserId.toList(), categoryMapper);

        return ResponseEntity.ok(entryPageMapper.toEntryPageResponse(entryResponse, allByUserId));
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @GetMapping("/{id}")
    public ResponseEntity<EntryResponse> findByIdAndUser(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestHeader(AUTHORIZATION) String token) {
        UUID userId = tokenUtil.getUserIdByToken(token);
        Entry entryFound = entryService.findByIdAndUserId(UUID.fromString(id), userId);
        return ResponseEntity.ok(entryMapper.toEntryResponse(entryFound, categoryMapper));
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @PutMapping("/{id}")
    public ResponseEntity<EntryResponse> update(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestBody @Valid EntryRequest entryRequest,
            @RequestHeader(AUTHORIZATION) String token
    ) {
        User user = tokenUtil.getUserByToken(token);
        Category category = categoryService.findByIdAndUserId(UUID.fromString(entryRequest.getCategoryId()), user.getId());
        Entry entry = entryMapper.toEntry(entryRequest, category, user);
        entry.setId(UUID.fromString(id));
        return ResponseEntity.ok(entryMapper.toEntryResponse(entryService.update(entry), categoryMapper));
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestHeader(AUTHORIZATION) String token) {
        UUID userId = tokenUtil.getUserIdByToken(token);
        UUID entryId = UUID.fromString(id);
        entryService.delete(entryId, userId);
      return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @PostMapping(value = "/importFile", consumes = "multipart/form-data")
    public ResponseEntity<List<EntryResponse>> importFile(
            @RequestParam MultipartFile file,
            @RequestHeader(AUTHORIZATION) String token) {
        User user = tokenUtil.getUserByToken(token);
        List<Entry> entries = entryService.importFile(file, user);
        return ResponseEntity.ok(entryMapper.toEntryResponse(entries, categoryMapper));
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @GetMapping("/export")
    public ResponseEntity<Resource> exportFileData(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 100) int size,
            @RequestHeader(AUTHORIZATION) String token,
            HttpServletRequest request
    ) {
        var userId = tokenUtil.getUserIdByToken(token);

        String acceptHeader = request.getHeader(ACCEPT);
        Resource file = entryService.exportData(userId, PageRequest.of(page, size), acceptHeader);

        var contentType = Optional.ofNullable(acceptHeader).orElse("application/octet-stream");
        var fileExtension = APPLICATION_XLSX.equalsIgnoreCase(acceptHeader) ? ".xlsx" : ".csv";
        var fileName = "entries_exported_" + LocalDateTime.now() + fileExtension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }




}
