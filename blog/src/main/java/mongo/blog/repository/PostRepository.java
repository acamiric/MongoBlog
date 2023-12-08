package mongo.blog.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import mongo.blog.models.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String>{


//gets all comments from a single post
@Query(value="{'id': ?0}",fields = "{'id':1, 'userName':0, 'description':0,'noOfComments':0}")
Optional<Post> findCommentsByPostId(String id);

	

}
