package com.railwayservice.serviceprovider.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.serviceprovider.entity.ServiceType;
import com.railwayservice.serviceprovider.service.ServiceTypeService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author lixs
 * @date 2017年2月17日
 * @describe 服务管理控制器。
 */

@Controller
@RequestMapping(value = "/serviceType", produces = {"application/json;charset=UTF-8"})
@Api(value = "服务管理控制器", description = "服务管理模块的相关操作")
@Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_SERVICE_SERVICE_MANAGE"})
public class ServiceTypeController {
    private final Logger logger = LoggerFactory.getLogger(ServiceTypeController.class);
    private ServiceTypeService serviceTypeService;

    @Autowired
    public void setServiceTypeService(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    /**
     * 服务添加
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addService(ServiceType serviceType) {
        logger.info("服务管理层->添加服务->服务名称：" + serviceType.getName());
        try {
            serviceTypeService.addServiceType(serviceType);
            return ResultMessage.newSuccess("添加服务完成！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("添加服务异常：", e);
            return ResultMessage.newFailure("添加服务异常！");
        }
    }

    /**
     * 服务删除
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultMessage deleteService(String typeId) {
        logger.info("服务管理层->服务删除->服务id：" + typeId);
        try {
            serviceTypeService.deleteServiceType(typeId);
            return ResultMessage.newSuccess("服务删除完成！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("服务删除异常：", e);
            return ResultMessage.newFailure("服务删除异常！");
        }
    }

    /**
     * 服务更新
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateService(ServiceType serviceType) {
        logger.info("服务管理层->服务更新->服务名称：" + serviceType.getName());
        try {
            serviceTypeService.updateServiceType(serviceType);
            return ResultMessage.newSuccess("服务更新完成！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("服务更新异常：", e);
            return ResultMessage.newFailure("服务更新异常！");
        }
    }

    /**
     * 服务查询(根据id查询)
     */

    @ResponseBody
    @RequestMapping("/find")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage findService(@RequestParam String id) {
        logger.info("服务管理层->服务查询->服务id：" + id);
        try {
            ServiceType serviceType = serviceTypeService.findServiceType(id);
            return ResultMessage.newSuccess().setData(serviceType);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("服务查询异常：", e);
            return ResultMessage.newFailure("服务查询异常！");
        }
    }

    /**
     * 服务查询(根据name查询)
     */

    @ResponseBody
    @RequestMapping("/findServiceByName")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage findServiceByName(@RequestParam String name) {
        logger.info("服务管理层->服务查询->服务名称：" + name);
        try {
            List<ServiceType> serviceType = serviceTypeService.findByName(name);
            return ResultMessage.newSuccess().setData(serviceType);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("服务查询异常：", e);
            return ResultMessage.newFailure("服务查询异常！");
        }
    }

    /**
     * 服务查询(查询所有)
     */
    @ResponseBody
    @RequestMapping("/findAll")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage findAllService(String order) {
        logger.info("服务管理层->服务查询");
        List<ServiceType> serviceType = serviceTypeService.findAllServiceType(order);
        return ResultMessage.newSuccess().setData(serviceType);
    }
}


