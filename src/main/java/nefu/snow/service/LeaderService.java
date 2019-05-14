package nefu.snow.service;


import nefu.snow.core.model.Car;
import nefu.snow.core.model.Team;
import nefu.snow.core.model.Tool;
import nefu.snow.core.model.User;

import java.util.Map;

/**
 * Created by LiXiwen on 2019/5/13 9:22.
 **/

public interface LeaderService {

    Map<String,Object> selectTeamTaskByTeamId(String teamId);

    Map<String,Object> updateProgress(String userId,int num);

    Map<String,Object> selectSystem(String teamId);

    Map<String,Object> performState();

    Map<String,Object> updateScore(User user);

    Map<String,Object> updateUserTask(User user);

    Map<String,Object> updateToolState(Tool tool);

    Map<String,Object> updateCarState(Car car);

    Map<String,Object> selectTools();

    Map<String,Object> selectCars();

    Map<String,Object> selectStarfs();

    Map<String,Object> selectTeams();

    Map<String,Object> selectApplications();

    Map<String,Object> insertTool(Tool tool);

    Map<String,Object> insertCar(Car car);

    Map<String,Object> changeTeamUser(User user);

    Map<String,Object> changeCar(Car car);

    Map<String,Object> changeTool(Tool tool);

    Map<String,Object> changeTeamTask(Team team);

    Map<String,Object> solveApply(Team team);

    Map<String,Object> putSysState(Integer state);

    Map<String,Object> putVoteState(Integer state);

    Map<String, Object> updateTeamScore(Team team);


    Map<String,Object> updateTeamApplyContent(Team team);
}
