package mongo.blog.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
	private String id;
	private String postId;
	private String userName;
	private String description;
	private int rating;

}
