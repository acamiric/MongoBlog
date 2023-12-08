package mongo.blog.controllers;


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

import mongo.blog.models.Comment;
import mongo.blog.models.Post;
import mongo.blog.services.PostService;

@RestController
@RequestMapping("/comments")
public class CommentController {


	@Autowired
	private PostService service;


	@PostMapping
	public @ResponseBody ResponseEntity<Object> saveComment(@RequestBody Comment comment) throws Exception {

		try {
			Optional<Post> c = service.findById(comment.getPostId());

			if (c.isPresent()) {
				Post p = c.get();
				p.getUserComments().add(comment);
				p.setNoOfComments(p.getUserComments().size());
				service.update(p);
				return ResponseEntity.status(HttpStatus.OK).body(p);

			}

			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can not comment on a non existing post");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong: " + e.getMessage());

		}

	}


	// all comments from a single post
	@GetMapping("{id}")
	public Optional<Post> getAllCommentsFromOnePost(@PathVariable String id) {
		return service.getAllCommentsFromOnePost(id);
	}

	@DeleteMapping("{pid}/{cid}")
	public @ResponseBody ResponseEntity<Object> delete(@PathVariable String pid, @PathVariable String cid) {
		try {
			Optional<Post> c = service.findById(pid);
			if (c.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Can not remove a comment from a non existing post!");
			}

			if (c.isPresent()) {
				Post p = c.get();
				for (Comment com : p.getUserComments()) {
					if (com.getId().equals(cid)) {
						p.getUserComments().remove(com);
						p.setNoOfComments(p.getUserComments().size());
						service.update(p);
						return ResponseEntity.status(HttpStatus.OK)
								.body("Successfully deleted Comment with id: " + cid);
					}
				}

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Can not delete a non existing comment!");

			}

			else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can not remove a non existing comment");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	//get comment from post id and comment id
	@GetMapping("{pid}/{cid}")
	public @ResponseBody ResponseEntity<Object> findById(@PathVariable String pid, @PathVariable String cid)
			throws Exception {

		Optional<Post> p = service.findById(pid);
		if (p.isPresent()) {
			Post post = p.get();

			for (Comment com : post.getUserComments()) {
				if (com.getId().equals(cid)) {
					return ResponseEntity.status(HttpStatus.OK).body(com);
				}
			}

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("Invalid search - Comment with id: " + cid + " does not exist! ");
	}

}
