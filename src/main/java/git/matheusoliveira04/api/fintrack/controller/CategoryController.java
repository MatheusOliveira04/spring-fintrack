package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.dto.request.CategoryRequest;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
import git.matheusoliveira04.api.fintrack.mapper.CategoryMapper;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.util.TokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private CategoryService categoryService;
    private CategoryMapper categoryMapper;
    private TokenUtil tokenUtil;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper, TokenUtil tokenUtil) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.tokenUtil = tokenUtil;
    }

    @PreAuthorize("hasRole('BASIC')")
    @PostMapping
    public ResponseEntity<CategoryResponse> insert(
            @RequestBody CategoryRequest categoryRequest,
            @RequestHeader("Authorization") String token,
            UriComponentsBuilder uriBuilder) {
        var category = categoryMapper.toCategory(categoryRequest, tokenUtil.getUser(token));
        var categoryInserted = categoryService.insert(category);
        return ResponseEntity
                .created(uriBuilder.path("/api/v1/category/{id}").buildAndExpand(categoryInserted.getId()).toUri())
                .body(categoryMapper.toCategoryResponse(categoryInserted));
    }

    @PreAuthorize("hasRole('BASIC')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(categoryMapper.toCategoryResponse(categoryService.findById(UUID.fromString(id))));
    }

    @PreAuthorize("hasRole('BASIC')")
    @GetMapping("/user")
    public ResponseEntity<List<CategoryResponse>> findAllOfUser(
            @RequestHeader("Authorization") String token) {
        var categoryList = categoryService.findAllByUserId(tokenUtil.getUser(token).getId());
        return ResponseEntity.ok(categoryMapper.toCategoryResponse(categoryList));
    }
}
