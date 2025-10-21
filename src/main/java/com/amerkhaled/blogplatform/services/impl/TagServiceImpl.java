package com.amerkhaled.blogplatform.services.impl;

import com.amerkhaled.blogplatform.domain.entities.Tag;
import com.amerkhaled.blogplatform.repositories.TagRepository;
import com.amerkhaled.blogplatform.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        log.info("[getTags] Fetching all tags with post count");
        List<Tag> tags = tagRepository.findAllWithPostCount();
        log.debug("[getTags] Retrieved {} tags", tags.size());
        return tags;
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        log.info("[createTags] Start creating tags. Requested names: {}", tagNames);

        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        log.debug("[createTags] Found {} existing tags: {}", existingTagNames.size(), existingTagNames);

        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();

        log.debug("[createTags] {} new tags to save: {}", newTags.size(), newTags.stream().map(Tag::getName).toList());

        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
            log.info("[createTags] Saved {} new tags", savedTags.size());
        }

        savedTags.addAll(existingTags);

        log.info("[createTags] Returning total {} tags ({} new, {} existing)",
                savedTags.size(), newTags.size(), existingTags.size());
        return savedTags;
    }

    @Transactional
    @Override
    public void deleteTag(UUID id) {
        log.info("[deleteTag] Attempting to delete tag with ID: {}", id);

        tagRepository.findById(id).ifPresentOrElse(tag -> {
            if (!tag.getPosts().isEmpty()) {
                log.warn("[deleteTag] Cannot delete tag with ID {} because it has {} associated posts", id, tag.getPosts().size());
                throw new IllegalStateException("Cannot delete tag with posts");
            }
            tagRepository.deleteById(id);
            log.info("[deleteTag] Successfully deleted tag with ID {}", id);
        }, () -> {
            log.warn("[deleteTag] Tag not found with ID {}", id);
        });
    }

    @Override
    public Tag getTagById(UUID id) {
        log.info("[getTagById] Fetching tag by ID: {}", id);
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[getTagById] Tag not found with ID {}", id);
                    return new EntityNotFoundException("Tag not found with ID " + id);
                });
        log.debug("[getTagById] Found tag: {}", tag.getName());
        return tag;
    }

    @Override
    public List<Tag> getTagByIds(Set<UUID> ids) {
        log.info("[getTagByIds] Fetching tags by IDs: {}", ids);
        List<Tag> foundTags = tagRepository.findAllById(ids);
        if (foundTags.size() != ids.size()) {
            log.error("[getTagByIds] Tag count mismatch: requested={}, found={}", ids.size(), foundTags.size());
            throw new EntityNotFoundException("Not all specified tag IDs exist");
        }
        log.debug("[getTagByIds] Successfully found {} tags", foundTags.size());
        return foundTags;
    }
}
