package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.repository.EntryRepository;
import git.matheusoliveira04.api.fintrack.service.EntryService;
import git.matheusoliveira04.api.fintrack.service.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<Entry> findAllByUserId(UUID userId) {
        List<Entry> entries = entryRepository.findAllByUserId(userId);
        if (entries.isEmpty()) {
            throw new ObjectNotFoundException("No user entries found!");
        }
        return entries;
    }
}
