package nefu.snow.api;

import nefu.snow.core.model.Car;
import nefu.snow.core.model.Team;
import nefu.snow.core.model.Tool;
import nefu.snow.core.model.User;
import nefu.snow.service.LeaderService;
import nefu.snow.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by LiXiwen on 2019/5/13 9:17.
 **/
@CrossOrigin(origins = "*" , allowCredentials = "true" , allowedHeaders = "*")
@RestController
public class LeaderApi {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final LeaderService leaderService;

    @Autowired
    public LeaderApi(UserService userService,LeaderService leaderService) {
        this.userService = userService;
        this.leaderService = leaderService;
    }

    @RequestMapping(value = "/snow/teamTask/{teamId}" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> selectTeamTaskByTeamId (@PathVariable(value = "teamId") String teamId){
        logger.info("GET teamTask : ");

        Map<String,Object> rtv = leaderService.selectTeamTaskByTeamId(teamId);
        return rtv;
    }


    @RequestMapping(value = "/snow/needs" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> snowNeeds(@RequestBody Team team){
        logger.info("applyContent : "+team.getApplyContent());

        return leaderService.updateTeamApplyContent(team);

    }

    @RequestMapping(value = "/snow/progress" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> updateProgress (@RequestParam("userId") String userId,@RequestParam("num") int num){
        logger.info("GET updateProgress : ");

        Map<String,Object> rtv = leaderService.updateProgress(userId,num);
        return rtv;
    }

    @RequestMapping(value = "/snow/progress/{teamId}" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> selectSystem (@PathVariable(value = "teamId") String teamId){
        logger.info("GET updateProgress : ");
        Map<String,Object> rtv = leaderService.selectSystem(teamId);
        return rtv;

    }


    @RequestMapping(value = "/snow/performSysState" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> performSysState (){
        logger.info("GET performSysState : ");
        return leaderService.performState();
    }

    @RequestMapping(value = "/snow/perform" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> performScore (@RequestParam("userId") String userId,@RequestParam("score") int score){
        logger.info("GET performScore : ");
        User user= new User();
        user.setUserId(userId);
        user.setUserScore(score);
        Map<String,Object> rtv = leaderService.updateScore(user);
        return rtv;
    }


    @RequestMapping(value = "/snow/task" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> performTask (@RequestBody User user){
        logger.info("POST performScore : "+ user.getUserId()+","+user.getUserTask());

        Map<String,Object> rtv = leaderService.updateUserTask(user);
        return rtv;
    }


    @RequestMapping(value = "/snow/select/tools" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> selectTools(){
        logger.info("GET selectTools : ");

        return leaderService.selectTools();
    }

    @RequestMapping(value = "/snow/select/cars" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> selectCars(){
        logger.info("GET selectCars : ");

        return leaderService.selectCars();
    }

    @RequestMapping(value = "/snow/update/tools" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> updateToolState (@RequestParam("toolId") Integer toolId,@RequestParam("state") String state){
        logger.info("GET updateToolState : ");

        Tool tool = new Tool();
        tool.setToolId(toolId);
        tool.setState(state);
        Map<String,Object> rtv = leaderService.updateToolState(tool);
        return rtv;
    }

    @RequestMapping(value = "/snow/update/cars" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> updateCarState (@RequestParam("carId") Integer carId,@RequestParam("state") String state){
        logger.info("GET updateCarState : ");

        Car car = new Car();
        car.setCarId(carId);
        car.setState(state);
        Map<String,Object> rtv = leaderService.updateCarState(car);
        return rtv;
    }

    @RequestMapping(value = "/snow/starfs" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> selectStarfs (){
        logger.info("GET updateCarState : ");

        Map<String,Object> rtv = leaderService.selectStarfs();
        return rtv;
    }

    @RequestMapping(value = "/snow/teams" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> selectTeams (){
        logger.info("GET selectTeams : ");

        Map<String,Object> rtv = leaderService.selectTeams();
        return rtv;
    }

    @RequestMapping(value = "/snow/applications" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> selectApplications (){
        logger.info("GET selectApplications : ");

        Map<String,Object> rtv = leaderService.selectApplications();
        return rtv;
    }


    @RequestMapping(value = "/snow/tools" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> postAddTool(@RequestBody Tool tool){
        Map<String,Object> rtv = new LinkedHashMap<>();
        tool.setState("未使用");
        rtv = leaderService.insertTool(tool);
        return rtv;
    }


    @RequestMapping(value = "/snow/cars" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> postAddCar(@RequestBody Car car){
        Map<String,Object> rtv = new LinkedHashMap<>();
        car.setState("未使用");
        rtv = leaderService.insertCar(car);
        return rtv;
    }

    @RequestMapping(value = "/snow/changeTeam" , method = RequestMethod.PUT)
    @ResponseBody
    public Map<String,Object> changeTeamUser(@RequestParam(value = "userId") String userId,@RequestParam(value = "teamId") String teamId){
        Map<String,Object> rtv = new LinkedHashMap<>();
        User user = new User();
        user.setUserId(userId);
        user.setUserTeamId(teamId);
        rtv = leaderService.changeTeamUser(user);

        return rtv;

    }

    @RequestMapping(value = "/snow/changeCars" , method = RequestMethod.PUT)
    @ResponseBody
    public Map<String,Object> changeCars(@RequestParam(value = "carId") Integer carId,@RequestParam(value = "teamId") String teamId){
        Map<String,Object> rtv = new LinkedHashMap<>();
        Car car = new Car();
        car.setCarId(carId);
        car.setTeamId(teamId);
        rtv = leaderService.changeCar(car);
        return rtv;
    }

    @RequestMapping(value = "/snow/changeTools" , method = RequestMethod.PUT)
    @ResponseBody
    public Map<String,Object> changeTools(@RequestParam(value = "toolId") Integer toolId,@RequestParam(value = "teamId") String teamId){
        Map<String,Object> rtv = new LinkedHashMap<>();
        Tool tool = new Tool();
        tool.setToolId(toolId);
        tool.setTeamId(teamId);
        rtv = leaderService.changeTool(tool);
        return rtv;
    }

    @RequestMapping(value = "/snow/changeTeamTask" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> changeTeamTasks(@RequestBody Team team){
        Map<String,Object> rtv = new LinkedHashMap<>();
        rtv = leaderService.changeTeamTask(team);
        return rtv;
    }

    @RequestMapping(value = "/snow/apply" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> solveApplys(@RequestParam(value = "teamId") String teamId,@RequestParam(value = "apply") Integer apply){
        Map<String,Object> rtv = new LinkedHashMap<>();
        Team team = new Team();
        team.setTeamId(teamId);
        team.setAstate(apply);
        rtv = leaderService.solveApply(team);
        return rtv;
    }

    @RequestMapping(value = "/snow/managePerformState" , method = RequestMethod.PUT)
    @ResponseBody
    public Map<String,Object> putSysState(@RequestParam("state") Integer state){
        Map<String,Object> rtv = new LinkedHashMap<>();
        rtv = leaderService.putSysState(state);
        return rtv;
    }


    @RequestMapping(value = "/snow/perform/team" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> performTeamScore (@RequestParam("teamId") String teamId,@RequestParam("score") int score){
        logger.info("GET performScore : ");
        Team team = new Team();
        team.setTeamId(teamId);
        team.setScore(score);
        Map<String,Object> rtv = leaderService.updateTeamScore(team);
        return rtv;
    }


    @RequestMapping(value = "/snow/manageVoteState" , method = RequestMethod.PUT)
    @ResponseBody
    public Map<String,Object> putVoteState(@RequestParam("state") Integer state){
        Map<String,Object> rtv = new LinkedHashMap<>();
        rtv = leaderService.putVoteState(state);
        return rtv;
    }






}
