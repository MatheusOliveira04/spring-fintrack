package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.dto.request.EntryRequest;
import git.matheusoliveira04.api.fintrack.entity.Category;
import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.entity.User;
import git.matheusoliveira04.api.fintrack.file.exception.FileProcessingException;
import git.matheusoliveira04.api.fintrack.file.importer.contract.FileImporter;
import git.matheusoliveira04.api.fintrack.file.importer.factory.EntryFileImporterFactory;
import git.matheusoliveira04.api.fintrack.mapper.EntryMapper;
import git.matheusoliveira04.api.fintrack.repository.EntryRepository;
import git.matheusoliveira04.api.fintrack.service.CategoryService;
import git.matheusoliveira04.api.fintrack.service.EntryService;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EntryServiceImpl implements EntryService {

    private CategoryService categoryService;
    private EntryFileImporterFactory importerFactory;
    private EntryRepository entryRepository;
    private EntryMapper entryMapper;

    public EntryServiceImpl(CategoryService categoryService, EntryFileImporterFactory importerFactory, EntryRepository entryRepository, EntryMapper entryMapper) {
        this.categoryService = categoryService;
        this.importerFactory = importerFactory;
        this.entryRepository = entryRepository;
        this.entryMapper = entryMapper;
    }

    @Override
    public Entry insert(Entry entry) {
        return entryRepository.save(entry);
    }

    @Override
    public Page<Entry> findAllByUserId(UUID userId, int page, int size) {
        Page<Entry> entries = entryRepository.findAllByUserId(userId, PageRequest.of(page, size));
        if (entries.isEmpty()) {
            throw new ObjectNotFoundException("No user entries found!");
        }
        return entries;
    }

    @Override
    public Entry findByIdAndUserId(UUID entryId, UUID userId) {
        return entryRepository.findByIdAndUserId(entryId, userId)
                .orElseThrow(() -> new ObjectNotFoundException("Entry not found with id: " + entryId));
    }

    @Override
    public Entry update(Entry entry) {
        findByIdAndUserId(entry.getId(), entry.getUser().getId());
        return entryRepository.save(entry);
    }

    @Override
    public void delete(UUID entryId, UUID userId) {
        entryRepository.delete(findByIdAndUserId(entryId, userId));
    }

    @Override
    public List<Entry> massInsertUsingImportFile(MultipartFile file, User user) {
        try(InputStream inputStream = file.getInputStream()) {
            String fileName = extractFileName(file);
            List<EntryRequest> entryRequests = importFileData(fileName, inputStream);
            return entryRepository.saveAll(mapToEntries(user, entryRequests));
        } catch (Exception e) {
            throw new FileProcessingException("Failed to process the import file. Please check the file format and content: " + e.getMessage());
        }
    }

    private static String extractFileName(MultipartFile file) {
        return Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new FileProcessingException("File name cannot be null"));
    }

    private List<EntryRequest> importFileData(String fileName, InputStream inputStream) throws Exception {
        FileImporter<EntryRequest> importer = importerFactory.getImporter(fileName);
        return importer.importFile(inputStream);
    }

    private List<Entry> mapToEntries(User user, List<EntryRequest> entryRequests) {
        return entryRequests.stream().map(entryRequest -> {
            Category category = categoryService
                    .findByIdAndUserId(UUID.fromString(entryRequest.getCategoryId()), user.getId());
            return entryMapper.toEntry(entryRequest, category, user);
        }).toList();
    }
}
