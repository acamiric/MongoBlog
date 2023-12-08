package mongo.blog.controllers;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import mongo.blog.models.Post;
import mongo.blog.services.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
	@Autowired
	private PostService service;

	
	@PostMapping
	public Post savePost(@RequestBody Post post) {
		return service.addPost(post);
	}

	@GetMapping
	private List<Post> getPosts() {
		return service.getAllPosts();
	}
	
	@DeleteMapping("{id}")
	public @ResponseBody ResponseEntity<String> delete(@PathVariable String id) {
		try {
			Optional<Post> s = service.findById(id);
			
			if (s.isEmpty()) {
				throw new Exception("Can not delete non existing post!");
			}
			
			service.deletePost(s.get());

			return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted Post with id: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Object> findById(@PathVariable String id) throws Exception {
		Optional<Post> s = service.findById(id);
		if (s.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(s.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("Invalid search - Post with id: " + id + " does not exist! ");
	}
	

}
