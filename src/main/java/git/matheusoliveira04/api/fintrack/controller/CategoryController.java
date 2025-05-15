package git.matheusoliveira04.api.fintrack.controller;

import static git.matheusoliveira04.api.fintrack.util.TokenUtil.*;

import git.matheusoliveira04.api.fintrack.config.jwts.JwtUtil;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private CategoryService categoryService;
    private UserService userService;
    private JwtUtil jwtUtil;

    public CategoryController(CategoryService categoryService, UserService userService, JwtUtil jwtUtil) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PreAuthorize("hasRole('BASIC')")
    @PostMapping
    public ResponseEntity<Category> insert(
            @RequestBody Category category,
            @RequestHeader("Authorization") String token) {
        var username = jwtUtil.extractUsername(extractBearerCharacters(token));
        var user = userService.findByEmail(username);
        category.setUser(user);
        var categorySaved = categoryService.insert(category);
        return ResponseEntity.ok(categorySaved);
    }
}
