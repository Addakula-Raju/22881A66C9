package com.affordmed.assessment.service;

import com.affordmed.assessment.model.UrlMapping;
import com.affordmed.assessment.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    private final UrlMappingRepository repository;
    private final Random random = new Random();

    public UrlShortenerService(UrlMappingRepository repository) {
        this.repository = repository;
    }

    public String shortenUrl(String originalUrl) {
        String shortCode = generateRandomCode(6);
        while (repository.findByShortUrl(shortCode).isPresent()) {
            shortCode = generateRandomCode(6);
        }
        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortUrl(shortCode);
        repository.save(mapping);
        return shortCode;
    }

    public String getOriginalUrl(String shortCode) {
        Optional<UrlMapping> mapping = repository.findByShortUrl(shortCode);
        return mapping.map(UrlMapping::getOriginalUrl).orElse(null);
    }

    private String generateRandomCode(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
