package mongo.blog.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="posts")
public class Post {
	@Id
	private String id;
	private String userName;
	private String description;
	
	private List<String> commentIds=new ArrayList<String>();
	private int noOfComments=commentIds.size();
}
