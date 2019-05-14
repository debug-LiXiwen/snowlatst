package nefu.snow.core.model;

import lombok.Data;


/**
 * Created by LiXiwen on 2019/5/14 20:12.
 *
 **/
@Data
public class PageComment {
    private Integer commentId;
    private String content;
    private String time;
    private String author;

}
