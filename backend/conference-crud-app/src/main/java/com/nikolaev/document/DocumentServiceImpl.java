package com.nikolaev.document;

import com.nikolaev.document.dto.DocumentDto;
import com.nikolaev.document.dto.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public DocumentDto getDocument(Long id) {
        return DocumentMapper.toDto(documentRepository.getOne(id));
    }

    @Override
    public Document downloadDocument(Long id) {
        return documentRepository.getOne(id);
    }

    @Override
    public void deleteDocument(Long documentId) {
        if (!documentRepository.getOne(documentId).getSubmission().isReviewable()) {
            documentRepository.deleteById(documentId);
        }
    }
}
