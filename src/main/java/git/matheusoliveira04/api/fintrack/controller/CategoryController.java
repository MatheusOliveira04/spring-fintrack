package git.matheusoliveira04.api.fintrack.controller;

import static git.matheusoliveira04.api.fintrack.util.TokenUtil.*;

import git.matheusoliveira04.api.fintrack.config.jwts.JwtUtil;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.mapper.CategoryMapper;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private CategoryService categoryService;
    private UserService userService;
    private JwtUtil jwtUtil;
    private CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, UserService userService, JwtUtil jwtUtil, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.categoryMapper = categoryMapper;
    }

    @PreAuthorize("hasRole('BASIC')")
    @PostMapping
    public ResponseEntity<CategoryResponse> insert(
            @RequestBody Category category,
            @RequestHeader("Authorization") String token,
            UriComponentsBuilder uriBuilder) {
        var username = jwtUtil.extractUsername(extractBearerCharacters(token));
        var user = userService.findByEmail(username);
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
}
