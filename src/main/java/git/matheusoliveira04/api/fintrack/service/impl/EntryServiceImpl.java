package git.matheusoliveira04.api.fintrack.service.impl;

import git.matheusoliveira04.api.fintrack.entity.Entry;
import git.matheusoliveira04.api.fintrack.repository.EntryRepository;
import git.matheusoliveira04.api.fintrack.service.EntryService;
import org.springframework.stereotype.Service;

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
}
