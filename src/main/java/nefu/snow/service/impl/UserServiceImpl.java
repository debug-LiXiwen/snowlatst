package nefu.snow.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import nefu.snow.common.SnowException;
import nefu.snow.common.util.JsonUtil;
import nefu.snow.common.util.TokenUtil;
import nefu.snow.core.mapper.CommunityMapper;
import nefu.snow.core.mapper.UserMapper;
import nefu.snow.core.model.*;
import nefu.snow.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by LiXiwen on 2019/5/9 22:37.
 **/

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserMapper userMapper;
    private final CommunityMapper communityMapper;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public UserServiceImpl(UserMapper userMapper , CommunityMapper communityMapper){
        this.userMapper = userMapper;
        this.communityMapper = communityMapper;
    }

    @Override
    public Map<String, Object> postLogin(User user) throws SnowException {
        Map<String, Object> rtv = null;
        List<User> users = userMapper.selectByCondition(user);
        if (null != users && 1 == users.size()) {
            user = users.get(0);
            String type = user.getUserType();
            user.setUserToken(TokenUtil.getToken());
            if (0 < userMapper.updateTokenBySystemId(user)) {
                user = users.get(0);
                rtv = new LinkedHashMap<>(3);
                rtv.put("code",0);
                rtv.put("type", user.getUserType());
                rtv.put("token", user.getUserToken());
            }
        } else {
            throw new SnowException("用户不存在!");
        }
        return rtv;
    }

    @Override
    public int signIn(User user) {
        int rtv =0;
        int a = userMapper.selectNum(user);
        if(0 == a ) {
            user.setUserToken(TokenUtil.getToken());
            user.setUserId(user.getUserId().substring(user.getUserId().length() - 6, user.getUserId().length()));
            if (0 < userMapper.insertUser(user)) {
                rtv = 0;
            } else {
                rtv = 1;
            }
        } else if(1 == a){
            if (0 < userMapper.updateUser(user)) {
                rtv = 0;
            } else {
                rtv = 1;
            }
        }else{
            rtv = 1;
        }
        return rtv;
    }

    public Map<String,Object> selectHonorList(int page) {
        Map<String,Object> rtv = new LinkedHashMap<>();
        Map<String,Object> map = new LinkedHashMap<>();
        PageInfo<Honor> pageInfo = null;

        PageHelper.startPage(page, 5);
        List<Honor> honors = userMapper.selectHonorList();

        logger.info("honors : "+JsonUtil.getJsonString(honors));
        if(null != honors && 0 != honors.size()) {
            pageInfo = new PageInfo<>(honors);
        }
        map.put("totalSize",pageInfo.getTotal());
        map.put("totalPage",pageInfo.getPages());
        map.put("eachPage",5);
        map.put("nowPage",pageInfo.getPageNum());

        rtv.put("honorList",honors);
        rtv.put("pages",map);

        return rtv;
    }

    @Override
    public Map<String, Object> vote(String fromUserId , String toUserId) {
        Map<String,Object> rtv = new LinkedHashMap<>();
        int voteNum = userMapper.selectVoteNumByUserId(toUserId);
        int status1 = userMapper.updateVote(voteNum+1,toUserId);
        int status2 = userMapper.updateVstate(1,fromUserId);
        if(1 == status1 && 1 == status2){
            rtv.put("code",0);
        }else{
            rtv.put("code",1);
        }
        return rtv;
    }

    @Override
    public Map<String, Object> voteState(String userId)  {
        Map<String,Object> rtv = new LinkedHashMap<>();
        int state = userMapper.voteState(userId);
        if(0 == state){
            rtv.put("code",0);
            rtv.put("state",0);
        } else if (1 == state){
            rtv.put("code",0);
            rtv.put("state",1);
        } else{
            rtv.put("code",1);
            rtv.put("message","数据库错误");
        }
        return rtv;
    }

    @Override
    public Map<String, Object> voteSysState() {
        Map<String,Object> rtv = new LinkedHashMap<>();
        int sysState = userMapper.voteSysState();
        if(0 == sysState){
            rtv.put("state",0);
        } else if (1 == sysState){
            rtv.put("state",1);
        } else{
            rtv.put("code",1);
            rtv.put("message","数据库错误");
        }
        return rtv;
    }

    @Override
    public Map<String, Object> newSnow(Article article) throws SnowException{
        Map<String,Object> rtv = null;
        if(null != article) {
            article.setAuthor(communityMapper.selectAuthorById(article.getUserId()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = null;
            try {
                time= sdf.parse(sdf.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            article.setTime(time);
            int flag = communityMapper.insertArticle(article);
            rtv = new HashMap<>();
            if(0 < flag){
                rtv.put("code",0);
            } else {
                rtv.put("code",1);
            }
        } else{
            throw new SnowException("插入文章失败，非法错误");
        }
        return rtv;
    }

    @Override
    public Map<String, Object> newComment(Comment comment) throws SnowException {
        Map<String,Object> rtv =  new LinkedHashMap<>();
        comment.setCommentAuthor(communityMapper.selectAuthorById(comment.getUserId()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = null;
        try {
            time= sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        comment.setCommentTime(time);
        int flag = communityMapper.insertComment(comment);
        if(0 != flag) {
            rtv.put("code",0);
        } else {
            throw new SnowException("插入评论失败，非法错误");
        }
        return rtv;
    }

    @Override
    public Map<String, Object> selectSnowList(int page) throws SnowException {


        Map<String,Object> rtv = null;
        Map<String,Object> map = null;
        Map<String,Object> data = null;
        PageInfo<PageArticle> pageInfo = null;

        PageHelper.startPage(page, 5);
        List<PageArticle> articles = communityMapper.selectSnowList();

        logger.info("articles : "+JsonUtil.getJsonString(articles));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null != articles && 0 != articles.size()) {

            pageInfo = new PageInfo<>(articles);

        }

        map = new LinkedHashMap<>();
        map.put("totalSize",pageInfo.getTotal());
        map.put("totalPage",pageInfo.getPages());
        map.put("eachPage",pageInfo.getPageSize());
        map.put("nowPage",pageInfo.getPageNum());

        data = new LinkedHashMap<>();

        data.put("snowList",articles);
        data.put("page",map);

        rtv = new LinkedHashMap<>();

        rtv.put("code",0);
        rtv.put("data",data);

        return rtv;
    }

    @Override
    public Map<String, Object> selectSnowCommentList(Integer snowId,int page) throws SnowException {
        Map<String,Object> rtv = null;
        Map<String,Object> data = null;
        Map<String,Object> map = new LinkedHashMap<>();
        PageInfo<PageComment> pageInfo = null;

        Comment comment = new Comment();
        comment.setSnowId(snowId);

        PageHelper.startPage(page,5);
        List<PageComment> comments = communityMapper.selectCommentList(comment);
       // List<Map<String,Object>> newComments = new ArrayList<>();


       // PageBean<Map<String,Object>> pageBean = new PageBean<>(page,5,comments.size());



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null != comments && 0 != comments.size()){

            pageInfo = new PageInfo<>(comments);

        }

        data = new LinkedHashMap<>();
        data.put("totalSize",pageInfo.getTotal());
        data.put("totalPage",pageInfo.getPages());
        data.put("eachPage",pageInfo.getPageSize());
        data.put("nowPage",pageInfo.getPageNum());

        map.put("commentList",comments);
        map.put("page",data);
        rtv = new LinkedHashMap<>();
        rtv.put("code",0);
        rtv.put("data",map);

        return rtv;
    }

    @Override
    public Map<String, Object> selectSnow(Integer snowId) throws SnowException {
        Map<String, Object> rtv =  new LinkedHashMap<>();
        Map<String,Object> map = new LinkedHashMap<>();
        Article data = new Article();
        Article article = new Article();
        article.setSnowId(snowId);
        data = communityMapper.selectSnowById(article);
        if(null != data){
            rtv.put("code",0);
            map.put("snowId",data.getSnowId());
            map.put("title",data.getTitle());
            map.put("content",data.getContent());
            map.put("time",format.format(data.getTime()));
            map.put("author",data.getAuthor());
            rtv.put("data",map);
        } else {
            throw new SnowException("data为空");
        }
        return rtv;
    }


    @Override
    public Map<String, Object> selectWork() throws SnowException {
        List<User> users = userMapper.selectOrder();
        List<Work> works = new ArrayList<>();

        List<Work> data = new ArrayList<>();

        List<Map<String,Object>> newdata = new ArrayList<>();

        Map<String,Object> rtv = null;
        Map<String,Object> map = null;
        Map<String,Object> map1 = new LinkedHashMap<>();
        Map<String,Object> map2 = new LinkedHashMap<>();

        if(null != users){
            for(int i=1 ; i<=0.3*users.size();i++){
                    Work work = new Work();
                    User user = users.get(i);
                    work.setUserId(user.getUserId());
                    work.setUserName(user.getUserName());
                    work.setScore(user.getUserScore());
                    work.setReward("有奖金");
                    works.add(work);
            }
            boolean flag = userMapper.deleteNewwork();
            boolean b = userMapper.insertNewWork(works);
            PageHelper.startPage(1,5);
            data = userMapper.selectNewWork();

            /*
            for(Work work : data){
                map2.put("userId",work.getUserId());
                map2.put("userName",work.getUserName());
                map2.put("score",work.getScore());
                map2.put("reward",work.getReward());
                newdata.add(map2);
            }*/

            PageInfo<Work> pageInfo =new PageInfo<>(data);

            map = new LinkedHashMap<>();
            map.put("totalSize",pageInfo.getTotal());
            map.put("totalPage",pageInfo.getPages());
            map.put("eachPage",pageInfo.getPageSize());
            map.put("nowPage",pageInfo.getPageNum());

            map1.put("workList",data);
            map1.put("page",map);

            rtv = new LinkedHashMap<>();
            rtv.put("code",0);
            rtv.put("data",map1);

        }
        return rtv;
    }

    @Override
    public Map<String, Object> updateCstate(User user) {
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;
        Map<String,Object> rtv = new HashMap<>();
        int flag = 0;
        long time = new Date().getTime();
        String time1 = String.valueOf(time);
        if(0 == user.getCstate()) {
            user.setUserStartTime(time);
            flag = userMapper.updateCstate(user);
        } else if(1 == user.getCstate()){
            user.setUserEndTime(time);
            flag = userMapper.updateCstateEndTime(user);
            long startTime  = userMapper.selectStartTime(user);
            logger.info("(time-startTime) : "+(time-startTime));

            long diff=(time-startTime);

            long olddiff = userMapper.selectDiff(user.getUserId());

            long time2 = diff+olddiff;
            long newhour = (time2)%(nd)/(nh);
            long newmin = (time2)%(nd)%(nh)/(nm);

            userMapper.updateDiff(time2,user.getUserId());
            userMapper.updateHour(newhour,user.getUserId());
            userMapper.updateMin(newmin,user.getUserId());


            user.setUserWorkTime(newhour+"小时"+newmin+"分");

            int workFlag = userMapper.updateWorkTime(user);
        }else{
            int flag1 = userMapper.updateCstate(user);
        }
        if(0 != flag){
            rtv.put("code",0);
        } else {
            rtv.put("code",1);
        }
        return rtv;
    }

    @Override
    public Map<String, Object> selectSnowClock(User user) {
        Map<String,Object> rtv = new LinkedHashMap<>();
        Map<String,Object> data = new LinkedHashMap<>();
        User user1 = userMapper.selectClockStatus(user);
        if(null != user1){
            data.put("workTime",user1.getUserWorkTime());
            data.put("state",user1.getCstate());
            data.put("task",user1.getUserTask());
            data.put("teamId",user1.getUserTeamId());

            rtv.put("code",0);
            rtv.put("data",data);
        } else{
            rtv.put("code",1);
        }
        return rtv;
    }

    @Override
    public Map<String, Object> selectTask(User user) {
        Map<String,Object> rtv = new LinkedHashMap<>();
        Map<String,Object> data = new LinkedHashMap<>();
        User user1 = userMapper.selectTask(user);
        if(null != user1){
            data.put("content",user1.getUserTask());
            data.put("time",format.format(user1.getUserTaskTime()));
            rtv.put("code",0);
            rtv.put("data",data);
        } else{
            rtv.put("code",1);
        }
        return rtv;
    }

    @Override
    public Map<String, Object> selectWorkTime(User user) {
        Map<String,Object> rtv = new LinkedHashMap<>();
        Map<String,Object> data = new LinkedHashMap<>();
        String workTime = userMapper.selectWorkTime(user);
        rtv.put("code",0);
        rtv.put("data",workTime);
        return rtv;
    }


}
