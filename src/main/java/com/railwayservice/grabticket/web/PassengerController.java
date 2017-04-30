package com.railwayservice.grabticket.web;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.grabticket.entity.Passenger;
import com.railwayservice.grabticket.service.PassengerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 乘客模块请求控制器。
 *
 * @author lid
 * @date 2017.4.11
 */

@Api(value = "乘客模块请求控制器", description = "乘客模块的相关操作")
@Controller
@RequestMapping(value = "/passenger", produces = {"application/json;charset=UTF-8"})
public class PassengerController {

    private final Logger logger = LoggerFactory.getLogger(PassengerController.class);

    private PassengerService passengerService;

    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * 获取用户乘车人信息(由python端调用)
     *
     * @param stringObjectMap
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("/getPassengers")
    @ApiOperation(notes = "获取用户乘车人信息(由python端调用)", value = "获取用户乘车人信息(由python端调用)")
    public ResultMessage getPassengers(@RequestBody Map<String, Object> stringObjectMap) {
        logger.info("获取用户乘车人信息：" + ((Map<String, Object>) stringObjectMap.get("data")).get("normal_passengers"));
        try {
            passengerService.getPassengers(stringObjectMap);
            return ResultMessage.newSuccess("获取用户乘车人信息成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取用户乘车人信息异常：", e);
            return ResultMessage.newFailure("获取用户乘车人信息异常！");
        }
    }

    /**
     * 添加用户乘车人信息(由python端调用)
     *
     * @param stringObjectMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/addPassenger")
    @ApiOperation(notes = "添加用户乘车人信息(由python端调用)", value = "添加用户乘车人信息(由python端调用)")
    public ResultMessage addPassenger(@RequestBody Map<String, Object> stringObjectMap) {
        logger.info("添加用户乘车人信息：" + stringObjectMap);
        try {
            Passenger passengers = passengerService.addPassenger(stringObjectMap);
            return ResultMessage.newSuccess("添加用户乘车人信息成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("添加用户乘车人信息异常：", e);
            return ResultMessage.newFailure("添加用户乘车人信息异常！");
        }
    }

    /**
     * 刷新用户乘车人信息(由python端调用)
     *
     * @param stringObjectMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/freshPassengers")
    @ApiOperation(notes = "刷新用户乘车人信息(由python端调用)", value = "刷新用户乘车人信息(由python端调用)")
    public ResultMessage freshPassengers(@RequestBody Map<String, Object> stringObjectMap) {
        logger.info("刷新用户乘车人信息：" + stringObjectMap);
        try {
            List<Passenger> freshPassengers = passengerService.freshPassengers(stringObjectMap);
            return ResultMessage.newSuccess("刷新用户乘车人信息成功！").setData(freshPassengers);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("刷新用户乘车人信息异常：", e);
            return ResultMessage.newFailure("刷新用户乘车人信息异常！");
        }
    }

    /**
     * 绑定12306账号(每个微信用户只有一个12306账号，如果绑定新账号，则将原账号绑定关系删除)(由python端调用)
     *
     * @param userId
     * @param userName
     * @param passWord
     * @return
     */
    @ResponseBody
    @RequestMapping("/bindingKyfwByUser")
    @ApiOperation(notes = "(每个微信用户只有一个12306账号，如果绑定新账号，则将原账号绑定关系删除)", value = "绑定12306账号(由python端调用)")
    public ResultMessage bindingKyfwByUser(String userId, String userName, String passWord) {
        logger.info("绑定12306账号：用户ID:" + userId + ",12306账号:" + userId + ",密码:" + passWord);
        try {
            boolean bl = passengerService.bindingKyfwByUser(userId, userName, passWord);
            return ResultMessage.newSuccess("绑定12306账号成功！").setData(bl);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("绑定12306账号异常：", e);
            return ResultMessage.newFailure("绑定12306账号异常！");
        }
    }


    /**
     * 获取12306账号及乘客信息
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getKyfwByUser")
    @ApiOperation(notes = "获取12306账号及乘客信息", value = "获取12306账号及乘客信息")
    public ResultMessage getKyfwByUser(String userId) {
        logger.info("获取12306账号及乘客信息：用户ID:");
        try {
            Map<String, Object> objectMap = passengerService.getKyfwByUser(userId);
            return ResultMessage.newSuccess("获取12306账号及乘客信息成功！").setData(objectMap);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取12306账号及乘客信息异常：", e);
            return ResultMessage.newFailure("获取12306账号及乘客信息异常！");
        }
    }
}	
