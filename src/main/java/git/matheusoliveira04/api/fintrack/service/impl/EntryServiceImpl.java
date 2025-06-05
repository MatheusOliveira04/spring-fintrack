package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.repository.EntryRepository;
import git.matheusoliveira04.api.fintrack.service.EntryService;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EntryServiceImpl implements EntryService {

    private EntryRepository entryRepository;

    public EntryServiceImpl(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
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
}
