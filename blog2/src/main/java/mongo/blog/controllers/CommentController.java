package mongo.blog.controllers;


import java.util.ArrayList;
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

import mongo.blog.models.Comment;
import mongo.blog.models.Post;
import mongo.blog.services.CommentService;
import mongo.blog.services.PostService;

@RestController
@RequestMapping("/comments")
public class CommentController {


	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;


	@PostMapping
	public @ResponseBody ResponseEntity<Object> saveComment(@RequestBody Comment comment) throws Exception {

		try {
			Optional<Post> c = postService.findById(comment.getPostId());

			if (c.isPresent()) {
				Post p = c.get();
				p.getCommentIds().add(comment.getId());
				p.setNoOfComments(p.getCommentIds().size());
				postService.update(p);
				commentService.addComment(comment);
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
	public List<Comment> getAllCommentsFromOnePost(@PathVariable String id) throws Exception {
		Optional<Post> p=postService.findById(id);
		List<Comment> comments=new ArrayList<Comment>();
		if(p.isEmpty()) {
			throw new Exception("The post with id: "+id+" does not exist.");
		}
		
		else {
			for(String commentId:p.get().getCommentIds()) {
				Comment c= commentService.findById(commentId).get();
				comments.add(c);
			}
			return comments;
		}
	}
	

	@DeleteMapping("{pid}/{cid}")
	public @ResponseBody ResponseEntity<Object> delete(@PathVariable String pid, @PathVariable String cid) {

		
		try {
			Optional<Post> po=postService.findById(pid);
			if(po.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can not comment on a non existing post!");
			}
			Post p=po.get();
			
			
			for(String commentId:p.getCommentIds()) {
				if(commentId.equals(cid)) {
					p.getCommentIds().remove(commentId);
					commentService.deleteComment(commentService.findById(cid).get());
					p.setNoOfComments(p.getCommentIds().size());
					postService.update(p);
					return ResponseEntity.status(HttpStatus.OK).body("Comment with id: "+commentId+" deleted succsessfully.");
					
				}
			}
			}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return null;
	}
	//get comment from post id and comment id
	@GetMapping("{pid}/{cid}")
	public @ResponseBody ResponseEntity<Object> findById(@PathVariable String pid, @PathVariable String cid)
			throws Exception {

		Optional<Post> p = postService.findById(pid);
		if (p.isPresent()) {
			Post post = p.get();

			for (String commentId : post.getCommentIds()) {
				if (commentId.equals(cid)) {
					Optional<Comment> comment=commentService.findById(cid);
					if(comment.isEmpty()) {
						return ResponseEntity.status(HttpStatus.NOT_FOUND)
								.body("Invalid search - Comment with id: " + cid + " does not exist! ");
					}
					else 
					return ResponseEntity.status(HttpStatus.OK).body(comment.get());
				}
			}

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("Invalid search - Post with id: " + pid + " does not exist! ");
	}

}
