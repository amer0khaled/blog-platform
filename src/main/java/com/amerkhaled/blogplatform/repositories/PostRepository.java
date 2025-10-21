package com.amerkhaled.blogplatform.repositories;

import com.amerkhaled.blogplatform.domain.PostStatus;
import com.amerkhaled.blogplatform.domain.entities.Category;
import com.amerkhaled.blogplatform.domain.entities.Post;
import com.amerkhaled.blogplatform.domain.entities.Tag;
import com.amerkhaled.blogplatform.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByTags(Set<Tag> tags);
    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tags);
    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);
    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tags);
    List<Post> findAllByStatus(PostStatus status);
    List<Post> findByAuthorAndStatus(User user, PostStatus status);
}
