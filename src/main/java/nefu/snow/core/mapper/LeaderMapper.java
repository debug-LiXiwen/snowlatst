package nefu.snow.core.mapper;

import nefu.snow.core.mapper.provider.LeaderProvider;
import nefu.snow.core.model.Car;
import nefu.snow.core.model.Team;
import nefu.snow.core.model.Tool;
import nefu.snow.core.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by LiXiwen on 2019/5/13 9:21.
 **/
@Repository
@Mapper
public interface LeaderMapper {

    @Select("SELECT team_task_content AS teamTaskContent,team_task_publishtime AS teamTaskPublishTime from team WHERE team_id=#{teamId}")
    Team selectTeamTaskByTeamId(String teamId);

    @Update("UPDATE user SET num=#{num} WHERE user_id=#{userId}")
    int updateProgress(@Param("userId") String userId,@Param("num") int num);

    @SelectProvider(type = LeaderProvider.class, method = "selectSystemByCondition")
    List<User> selectSystem(User user);

    @Select("SELECT count(system_id) from user")
    int countNum();

    @Select("SELECT SUM(num) from user")
    int selectTotalNum();

    @Update("UPDATE user SET user_score=#{userScore} WHERE user_id=#{userId}")
    int updateScore(User user);

    @Update("UPDATE user set user_task=#{userTask},user_tasktime=#{userTaskTime} WHERE user_id=#{userId}")
    int updateUserTask(User user);

    @Update("UPDATE tool SET state=#{state} WHERE tool_id=#{toolId}")
    int updateToolState(Tool tool);

    @Update("UPDATE car SET state=#{state} WHERE car_id=#{carId}")
    int updateCarState(Car car);

    @Select("SELECT car_id AS carId,num,state,teamId from car")
    List<Car> selectCars();

    @Select("SELECT tool_id AS toolId,tool_name AS toolName , state ,teamId from tool")
    List<Tool> selectTools();

    @SelectProvider(type = LeaderProvider.class , method = "selectUserList")
    List<User> selectStarfs();

    @Select("SELECT team_id AS teamId,team_name AS teamName from team")
    List<Team> selectTeams();

    @Select("SELECT team_id AS teamId,team_name AS teamName,astate from team")
    List<Team> selectApplications();

    @Insert("INSERT INTO car(num,state) VALUES(#{num},#{state})")
    int insertCar(Car car);

    @Insert("INSERT INTO tool(tool_name,state) VALUES(#{toolName},#{state})")
    int insertTool(Tool tool);

    @Update("UPDATE user SET user_teamid=#{userTeamId} WHERE user_id=#{userId}")
    int changeTeamUser(User user);

    @Update("UPDATE car SET teamId=#{teamId} WHERE car_id=#{carId}")
    int changeCars(Car car);

    @Update("UPDATE tool SET teamId=#{teamId} WHERE tool_id=#{toolId}")
    int changeTools(Tool tool);

    @Update("UPDATE team SET team_task_content=#{teamTaskContent} WHERE team_id=#{teamId}")
    int changeTeamTasks(Team team);

    @Update("UPDATE team SET astate=#{astate} WHERE team_id=#{teamId}")
    int solveTeamApply(Team team);

    @Delete("DELETE from team WHERE team_id=#{teamId}")
    int deleteTeamApplyById(Team team);

    @Update("UPDATE performsystem SET state=#{state}")
    int updateSysState(Integer state);

    @Update("UPDATE system SET system_state=#{state}")
    int updateVoteState(Integer state);


    @Select("SELECT state from performsystem")
    int performState();

    @Update("UPDATE team SET score=#{score} WHERE team_id=#{teamId}")
    int updateTeamScore(Team team);

    @Update("UPDATE team SET applyContent=#{applyContent} WHERE team_id=#{teamId}")
    int updateTeamApplyContent(Team team);

    @Select("SELECT team_id AS teamId,applyContent  from team WHERE team_id=#{teamId}")
    Team selectTeamApplyContent(Team team);





}
