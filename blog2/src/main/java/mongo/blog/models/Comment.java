package mongo.blog.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection  = "comments")
public class Comment {
	@Id
	private String id;
	private String postId;
	private String userName;
	private String description;
	private int rating;

}
