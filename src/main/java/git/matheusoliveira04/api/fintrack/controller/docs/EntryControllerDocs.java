package git.matheusoliveira04.api.fintrack.controller.docs;

import git.matheusoliveira04.api.fintrack.dto.request.EntryRequest;
import git.matheusoliveira04.api.fintrack.dto.response.EntryPageResponse;
import git.matheusoliveira04.api.fintrack.dto.response.EntryResponse;
import git.matheusoliveira04.api.fintrack.file.exporter.MediaTypes;
import git.matheusoliveira04.api.fintrack.validation.annotation.ValidUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public interface EntryControllerDocs {

    @Operation(
            summary = "insert new entry",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<EntryResponse> insert(
            @RequestBody @Valid EntryRequest entryRequest,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token,
            UriComponentsBuilder uriBuilder
    );

    @Operation(
            summary = "find all user entries",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<EntryPageResponse> findAllByUser(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token
    );

    @Operation(
            summary = "find entry by id and by user",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<EntryResponse> findByIdAndUser(
            @PathVariable @NotBlank @ValidUUID String id,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token);

    @Operation(
            summary = "update entry",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<EntryResponse> update(
            @PathVariable @NotBlank @ValidUUID String id,
            @RequestBody @Valid EntryRequest entryRequest,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token
    );

    @Operation(
            summary = "delete entry",
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

    @Operation(
            summary = "import entry file",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {@Content(schema = @Schema(implementation = EntryRequest.class)) }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)

            }
    )
    ResponseEntity<List<EntryResponse>> importFile(
            @RequestParam MultipartFile file,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token);


    @Operation(
            summary = "Export entries data",
            security = {@SecurityRequirement(name = "bearer-key")},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MediaTypes.APPLICATION_XLSX),
                                    @Content(mediaType = MediaTypes.TEXT_CSV),
                                    @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)
                            }
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content)
            }
    )
    ResponseEntity<Resource> exportFileData(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(value = 100) int size,
            @Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token,
            HttpServletRequest request
    );

}
