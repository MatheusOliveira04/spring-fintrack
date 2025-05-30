package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.dto.request.EntryRequest;
import git.matheusoliveira04.api.fintrack.dto.response.EntryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.mapper.CategoryMapper;
import git.matheusoliveira04.api.fintrack.mapper.EntryMapper;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.EntryService;
import git.matheusoliveira04.api.fintrack.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
    private EntryService entryService;
    private EntryMapper entryMapper;
    private TokenUtil tokenUtil;

    public EntryController(CategoryService categoryService, CategoryMapper categoryMapper, EntryService entryService, EntryMapper entryMapper, TokenUtil tokenUtil) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.entryService = entryService;
        this.entryMapper = entryMapper;
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
    public ResponseEntity<List<EntryResponse>> findAllByUser(@RequestHeader("Authorization") String token) {
        UUID userId = tokenUtil.getUserIdByToken(token);
        List<EntryResponse> entryResponses = entryMapper
                .toEntryResponse(entryService.findAllByUserId(userId), categoryMapper);
        return ResponseEntity.ok(entryResponses);
    }


}
