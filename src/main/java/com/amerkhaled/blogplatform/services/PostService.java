package com.amerkhaled.blogplatform.services;

import com.amerkhaled.blogplatform.domain.CreatePostRequest;
import com.amerkhaled.blogplatform.domain.UpdatePostRequest;
import com.amerkhaled.blogplatform.domain.entities.Post;
import com.amerkhaled.blogplatform.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    void deletePost(UUID id);

}
