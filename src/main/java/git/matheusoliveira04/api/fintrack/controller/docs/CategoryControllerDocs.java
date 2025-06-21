package git.matheusoliveira04.api.fintrack.controller.docs;

import git.matheusoliveira04.api.fintrack.dto.request.CategoryRequest;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.CategoryResponse;
import git.matheusoliveira04.api.fintrack.validation.annotation.ValidUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public interface CategoryControllerDocs {

    @Operation(
            summary = "Insert a new category",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<CategoryResponse> insert(
            @RequestBody @Valid CategoryRequest categoryRequest,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token,
            UriComponentsBuilder uriBuilder);

    @Operation(
            summary = "Find category by id and user",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<CategoryResponse> findById(
            @PathVariable @NotBlank @ValidUUID String id,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token);


    @Operation(
            summary = "Find all category by user",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<CategoryPageResponse> findAllByUser(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token);

    @Operation(
            summary = "Update category",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<CategoryResponse> update(
            @RequestBody @Valid CategoryRequest categoryRequest,
            @PathVariable @NotBlank @ValidUUID String id,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token);

    @Operation(
            summary = "Delete category",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<Void> delete(
            @PathVariable @NotBlank @ValidUUID String id,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token);
}
