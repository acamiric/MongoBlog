package mongo.blog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mongo.blog.models.Comment;
import mongo.blog.repository.CommentRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository repository;
	
	public Comment addComment(Comment p) {
		return repository.insert(p);
	}

	public void deleteComment(Comment p) {
		repository.delete(p);
		//return "Comment with id"+ p.getId() +"successfully removed";
	}

	public List<Comment> getAllComments(){
		return repository.findAll();
	}

	public Optional<Comment> findById(String id) throws Exception {
		Optional<Comment> s=repository.findById(id);
		if(s.isPresent()) {
			return Optional.of(s.get());
		}
		return Optional.empty();
	}


	public Comment update(Comment s) throws Exception {
		Optional<Comment> existingPost=repository.findById(s.getId());
		if(existingPost.isPresent()) {
			return repository.save(s);
		}
		throw new Exception("Non existing comment");
	}
}
