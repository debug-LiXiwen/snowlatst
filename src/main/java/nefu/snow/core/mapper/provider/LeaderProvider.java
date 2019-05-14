package nefu.snow.core.mapper.provider;

import nefu.snow.core.model.User;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by LiXiwen on 2019/5/13 10:10.
 **/
public class LeaderProvider {

    public String selectSystemByCondition(User user) {
        return new SQL() {
            {
                SELECT("system_id AS systemId,user_id AS userId,user_name AS userName, user_password AS userPassword,user_token AS userToken," +
                        "user_type AS userType,user_votenum AS userVoteNum,user_vstate AS userVstate ,user_cstate AS cstate,user_score AS userScore," +
                        "user_workage AS userWorkAge,user_address AS userAddress,user_reward AS userReward,user_sex AS userSex,user_worktime as userWorkTime," +
                        "user_starttime AS userStartTime,user_endtime AS userEndTime,user_tasktime AS userTaskTime,num,astate");
                FROM("user");
                if (null != user.getUserTeamId()) {
                    WHERE("user_teamid=#{userTeamId}");
                }
            }
        }.toString();
    }

    public String selectUserList() {
        return new SQL() {
            {
                SELECT("system_id AS systemId,user_id AS userId,user_name AS userName, user_password AS userPassword,user_token AS userToken," +
                        "user_type AS userType,user_votenum AS userVoteNum,user_vstate AS userVstate ,user_cstate AS cstate,user_score AS userScore," +
                        "user_workage AS userWorkAge,user_address AS userAddress,user_reward AS userReward,user_sex AS userSex,user_worktime as userWorkTime," +
                        "user_starttime AS userStartTime,user_endtime AS userEndTime,user_tasktime AS userTaskTime,num,astate");
                FROM("user");
            }
        }.toString();
    }
}
