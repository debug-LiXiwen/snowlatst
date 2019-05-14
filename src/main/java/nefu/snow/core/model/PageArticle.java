package nefu.snow.core.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by LiXiwen on 2019/5/14 20:13.
 **/
@Data
public class PageArticle {
    private Integer snowId;
    private String title;
    private String content;
    private String time;
    private String author;

}
