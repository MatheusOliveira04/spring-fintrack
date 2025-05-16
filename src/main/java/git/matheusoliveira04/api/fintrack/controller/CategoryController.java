package git.matheusoliveira04.api.fintrack.controller;

import git.matheusoliveira04.api.fintrack.config.jwts.JwtUtil;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.mapper.CategoryMapper;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.UserService;
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
            @RequestBody Category category,
            @RequestHeader("Authorization") String token,
            UriComponentsBuilder uriBuilder) {
        var user = tokenUtil.getUser(token);
        category.setUser(user);
        var categorySaved = categoryService.insert(category);
        return ResponseEntity
                .created(uriBuilder.path("/api/v1/category/{id}").buildAndExpand(category.getId()).toUri())
                .body(categoryMapper.toCategoryResponse(categorySaved));
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
        var user = tokenUtil.getUser(token);
        var categoryList = categoryService.findAllByUserId(user.getId());
        List<CategoryResponse> categoryResponses = categoryList
                .stream()
                .map(category -> categoryMapper.toCategoryResponse(category))
                .toList();
        return ResponseEntity.ok(categoryResponses);
    }
}
