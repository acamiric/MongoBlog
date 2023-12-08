package mongo.blog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mongo.blog.models.Comment;
@Repository
public interface CommentRepository extends MongoRepository<Comment, String>{

}
