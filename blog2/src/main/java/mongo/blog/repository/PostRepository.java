package mongo.blog.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mongo.blog.models.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String>{

}
