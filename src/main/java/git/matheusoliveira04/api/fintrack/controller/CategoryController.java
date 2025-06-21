package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.controller.docs.CategoryControllerDocs;
import git.matheusoliveira04.api.fintrack.dto.request.CategoryRequest;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.mapper.CategoryMapper;
import git.matheusoliveira04.api.fintrack.mapper.CategoryPageMapper;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.util.TokenUtil;
import git.matheusoliveira04.api.fintrack.validation.annotation.ValidUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Validated
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController implements CategoryControllerDocs {

    private CategoryMapper categoryMapper;
    private CategoryPageMapper categoryPageMapper;
    private CategoryService categoryService;
    private TokenUtil tokenUtil;

    public CategoryController(CategoryMapper categoryMapper, CategoryPageMapper categoryPageMapper, CategoryService categoryService, TokenUtil tokenUtil) {
        this.categoryMapper = categoryMapper;
        this.categoryPageMapper = categoryPageMapper;
        this.categoryService = categoryService;
        this.tokenUtil = tokenUtil;
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @PostMapping
    public ResponseEntity<CategoryResponse> insert(
            @RequestBody @Valid CategoryRequest categoryRequest,
            @RequestHeader(AUTHORIZATION) String token,
            UriComponentsBuilder uriBuilder) {
        Category category = categoryMapper.toCategory(categoryRequest, tokenUtil.getUserByToken(token));
        Category categoryInserted = categoryService.insert(category);
        return ResponseEntity
                .created(uriBuilder.path("/api/v1/category/{id}").buildAndExpand(categoryInserted.getId()).toUri())
                .body(categoryMapper.toCategoryResponse(categoryInserted));
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestHeader(AUTHORIZATION) String token) {
        UUID userId = tokenUtil.getUserByToken(token).getId();
        Category category = categoryService.findByIdAndUserId(UUID.fromString(id), userId);
        return ResponseEntity.ok(categoryMapper.toCategoryResponse(category));
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @GetMapping("/user")
    public ResponseEntity<CategoryPageResponse> findAllByUser(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size,
            @RequestHeader(AUTHORIZATION) String token) {
        Page<Category> categoryPage = categoryService.findAllByUserId(tokenUtil.getUserIdByToken(token), page, size);
        List<CategoryResponse> categoryResponseList = categoryMapper.toCategoryResponse(categoryPage.toList());
        CategoryPageResponse categoryPageResponse = categoryPageMapper.toCategoryPageResponse(categoryResponseList, categoryPage);
        return ResponseEntity.ok(categoryPageResponse);
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @RequestBody @Valid CategoryRequest categoryRequest,
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestHeader(AUTHORIZATION) String token) {
        Category category = categoryMapper.toCategory(categoryRequest, tokenUtil.getUserByToken(token));
        category.setId(UUID.fromString(id));
        return ResponseEntity.ok(categoryMapper.toCategoryResponse(categoryService.update(category)));
    }

    @Override
    @PreAuthorize("hasRole('BASIC')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestHeader(AUTHORIZATION) String token
    ) {
        UUID categoryId = UUID.fromString(id);
        UUID userId = tokenUtil.getUserIdByToken(token);
        categoryService.delete(categoryId, userId);
        return ResponseEntity.noContent().build();
    }
}
