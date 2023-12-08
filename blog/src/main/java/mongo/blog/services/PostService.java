package mongo.blog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import mongo.blog.models.Post;
import mongo.blog.repository.PostRepository;

@Service
public class PostService {
	
@Autowired
private PostRepository repository;

public Post addPost(Post p) {
	return repository.insert(p);
}

public String deletePost(Post p) {
	repository.delete(p);
	return "Post with id"+ p.getId() +"successfully removed";
}

public List<Post> getAllPosts(){
	return repository.findAll();
}

public Optional<Post> findById(String id) throws Exception {
	Optional<Post> s=repository.findById(id);
	if(s.isPresent()) {
		return Optional.of(s.get());
	}
	return Optional.empty();
}


public Post update(Post s) throws Exception {
	Optional<Post> existingPost=repository.findById(s.getId());
	if(existingPost.isPresent()) {
		return repository.save(s);
	}
	throw new Exception("Non existing post");
}

public Optional<Post> getAllCommentsFromOnePost(String id){
	return repository.findCommentsByPostId(id);
}



}
