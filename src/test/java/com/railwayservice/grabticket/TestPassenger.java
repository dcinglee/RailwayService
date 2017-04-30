package com.railwayservice.grabticket;

import com.railwayservice.application.util.JSonUtils;
import com.railwayservice.grabticket.dao.KyfwInUserRelaDao;
import com.railwayservice.grabticket.dao.KyfwUserDao;
import com.railwayservice.grabticket.dao.PassengerDao;
import com.railwayservice.grabticket.entity.KyfwInUserRela;
import com.railwayservice.grabticket.entity.KyfwUser;
import com.railwayservice.grabticket.entity.Passenger;
import com.railwayservice.grabticket.service.PassengerService;
import com.railwayservice.user.dao.UserDao;
import com.railwayservice.user.entity.User;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 乘车人服务测试类。
 *
 * @author Ewing
 * @date 2017/2/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestPassenger {
    private Logger logger = LoggerFactory.getLogger(TestPassenger.class);

    private PassengerService passengerService;

    private PassengerDao passengerDao;

    private KyfwInUserRelaDao kyfwInUserRelaDao;

    private KyfwUserDao kyfwUserDao;

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setKyfwUserDao(KyfwUserDao kyfwUserDao) {
        this.kyfwUserDao = kyfwUserDao;
    }

    @Autowired
    public void setKyfwInUserRelaDao(KyfwInUserRelaDao kyfwInUserRelaDao) {
        this.kyfwInUserRelaDao = kyfwInUserRelaDao;
    }

    @Autowired
    public void setPassengerDao(PassengerDao passengerDao) {
        this.passengerDao = passengerDao;
    }

    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setName("testNo");
        user.setPassWord("testPassword");
        user = userDao.save(user);

    }

    @After
    public void tearDown() {
        if (null != user) {
            userDao.delete(user);
        }
    }

    @Test
    public void getPassengersTest() {
        String str = "{\n" +
                "            \"data\": {\n" +
                "                \"dj_passengers\": [],\n" +
                "                \"exMsg\": \"\",\n" +
                "                \"isExist\": true,\n" +
                "                \"normal_passengers\": [\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"1970-01-01 00:00:00\",\n" +
                "                        \"code\": \"7\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"397808520@qq.com\",\n" +
                "                        \"first_letter\": \"LINING3216102\",\n" +
                "                        \"index_id\": \"0\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"李宁\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"M\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2014-01-07 10:27:48\",\n" +
                "                        \"code\": \"2\",\n" +
                "                        \"country_code\": \"\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"HLN\",\n" +
                "                        \"index_id\": \"1\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"侯娈妮\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2016-12-24 00:00:00\",\n" +
                "                        \"code\": \"5\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"LCX\",\n" +
                "                        \"index_id\": \"2\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"李丑湘\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2014-01-07 10:27:48\",\n" +
                "                        \"code\": \"6\",\n" +
                "                        \"country_code\": \"\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"LD\",\n" +
                "                        \"index_id\": \"3\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"李丹\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"97\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2015-11-27 00:00:00\",\n" +
                "                        \"code\": \"8\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"LW\",\n" +
                "                        \"index_id\": \"4\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"李文\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"F\",\n" +
                "                        \"sex_name\": \"女\",\n" +
                "                        \"total_times\": \"98\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2012-01-08 21:07:03\",\n" +
                "                        \"code\": \"1\",\n" +
                "                        \"country_code\": \"\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"RW\",\n" +
                "                        \"index_id\": \"5\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"任卫\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"1990-06-24 00:00:00\",\n" +
                "                        \"code\": \"3\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"SZY\",\n" +
                "                        \"index_id\": \"6\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"421123199006247240\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"史周颖\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"F\",\n" +
                "                        \"sex_name\": \"女\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"1983-01-03 00:00:00\",\n" +
                "                        \"code\": \"4\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"ZW\",\n" +
                "                        \"index_id\": \"7\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"周伟\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"M\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"other_isOpenClick\": [\n" +
                "                    \"91\",\n" +
                "                    \"93\",\n" +
                "                    \"98\",\n" +
                "                    \"99\",\n" +
                "                    \"95\",\n" +
                "                    \"97\"\n" +
                "                ],\n" +
                "                \"two_isOpenClick\": [\n" +
                "                    \"93\",\n" +
                "                    \"95\",\n" +
                "                    \"97\",\n" +
                "                    \"99\"\n" +
                "                ]\n" +
                "            },\n" +
                "            \"event_id\": \"1491967649384\",\n" +
                "            \"httpstatus\": 200,\n" +
                "            \"messages\": [],\n" +
                "            \"status\": true,\n" +
                "            \"task_type\": \"submit_order\",\n" +
                "            \"validateMessages\": {},\n" +
                "            \"validateMessagesShowId\": \"_validatorMessage\",\n" +
                "            \"userId\": \"8a9d4f085adb1441015adb2208600022\",\n" +
                "            \"kyfwUserId\":\"1d3s1f65sd4fsd31f56ds6f\"\n" +
                "\n" +
                "        }";

        Map<String, Object> stringObjectMap = JSonUtils.readValue(str, Map.class);

        List<Passenger> passengerList = passengerService.getPassengers(stringObjectMap);
        Assert.assertTrue(passengerList.size() > 0);

        String userId = stringObjectMap.get("userId").toString();

        List<Passenger> passengers = passengerDao.findByUserId(userId);

        Iterator<Passenger> it = passengers.iterator();
        while (it.hasNext()) {
            passengerDao.delete(it.next());
            it.remove();
        }

    }

    @Test
    public void addPassengerTest() {

        String str = "{\n" +
                "            \"data\": {\n" +
                "            \"dj_passengers\": [],\n" +
                "                    \"exMsg\": \"\",\n" +
                "                    \"isExist\": true,\n" +
                "                    \"normal_passengers\":   {\n" +
                "                \"address\": \"\",\n" +
                "                        \"born_date\": \"1970-01-01 00:00:00\",\n" +
                "                        \"code\": \"7\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"397808520@qq.com\",\n" +
                "                        \"first_letter\": \"LINING3216102\",\n" +
                "                        \"index_id\": \"0\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"李宁\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"M\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "            },\n" +
                "            \"other_isOpenClick\": [\n" +
                "                \"91\",\n" +
                "                \"93\",\n" +
                "                \"98\",\n" +
                "                \"99\",\n" +
                "                \"95\",\n" +
                "                \"97\"\n" +
                "            ],\n" +
                "                    \"two_isOpenClick\": [\n" +
                "                \"93\",\n" +
                "                \"95\",\n" +
                "                \"97\",\n" +
                "                \"99\"\n" +
                "            ]\n" +
                "        },\n" +
                "            \"event_id\": \"1491967649384\",\n" +
                "                \"httpstatus\": 200,\n" +
                "                \"messages\": [],\n" +
                "                \"status\": true,\n" +
                "                \"task_type\": \"submit_order\",\n" +
                "                \"validateMessages\": {},\n" +
                "            \"validateMessagesShowId\": \"_validatorMessage\",\n" +
                "            \"userId\": \"8a9d4f085adb1441015adb2208600022\",\n" +
                "            \"kyfwUserId\":\"1d3s1f65sd4fsd31f56ds6f\"\n" +
                "        }";

        JSONObject jasonObject = JSONObject.fromObject(str);
        Map<String, Object> stringObjectMap = jasonObject;
        Passenger passenger = passengerService.addPassenger(stringObjectMap);
        Assert.assertTrue(null != passenger);

        passengerDao.delete(passenger);
    }

    @Test
    public void freshPassengersTest() {
        String str = "{\n" +
                "            \"data\": {\n" +
                "                \"dj_passengers\": [],\n" +
                "                \"exMsg\": \"\",\n" +
                "                \"isExist\": true,\n" +
                "                \"normal_passengers\": [\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"1970-01-01 00:00:00\",\n" +
                "                        \"code\": \"7\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"397808520@qq.com\",\n" +
                "                        \"first_letter\": \"LINING3216102\",\n" +
                "                        \"index_id\": \"0\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"李宁\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"M\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2014-01-07 10:27:48\",\n" +
                "                        \"code\": \"2\",\n" +
                "                        \"country_code\": \"\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"HLN\",\n" +
                "                        \"index_id\": \"1\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"侯娈妮\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2016-12-24 00:00:00\",\n" +
                "                        \"code\": \"5\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"LCX\",\n" +
                "                        \"index_id\": \"2\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"李丑湘\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2014-01-07 10:27:48\",\n" +
                "                        \"code\": \"6\",\n" +
                "                        \"country_code\": \"\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"LD\",\n" +
                "                        \"index_id\": \"3\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"李丹\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"97\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2015-11-27 00:00:00\",\n" +
                "                        \"code\": \"8\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"LW\",\n" +
                "                        \"index_id\": \"4\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"李文\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"F\",\n" +
                "                        \"sex_name\": \"女\",\n" +
                "                        \"total_times\": \"98\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"2012-01-08 21:07:03\",\n" +
                "                        \"code\": \"1\",\n" +
                "                        \"country_code\": \"\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"RW\",\n" +
                "                        \"index_id\": \"5\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"任卫\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"1990-06-24 00:00:00\",\n" +
                "                        \"code\": \"3\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"SZY\",\n" +
                "                        \"index_id\": \"6\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"421123199006247240\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"史周颖\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"F\",\n" +
                "                        \"sex_name\": \"女\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"address\": \"\",\n" +
                "                        \"born_date\": \"1983-01-03 00:00:00\",\n" +
                "                        \"code\": \"4\",\n" +
                "                        \"country_code\": \"CN\",\n" +
                "                        \"email\": \"\",\n" +
                "                        \"first_letter\": \"ZW\",\n" +
                "                        \"index_id\": \"7\",\n" +
                "                        \"mobile_no\": \"111111111\",\n" +
                "                        \"passenger_flag\": \"0\",\n" +
                "                        \"passenger_id_no\": \"430621111111111111\",\n" +
                "                        \"passenger_id_type_code\": \"1\",\n" +
                "                        \"passenger_id_type_name\": \"二代身份证\",\n" +
                "                        \"passenger_name\": \"周伟\",\n" +
                "                        \"passenger_type\": \"1\",\n" +
                "                        \"passenger_type_name\": \"成人\",\n" +
                "                        \"phone_no\": \"\",\n" +
                "                        \"postalcode\": \"\",\n" +
                "                        \"recordCount\": \"8\",\n" +
                "                        \"sex_code\": \"M\",\n" +
                "                        \"sex_name\": \"男\",\n" +
                "                        \"total_times\": \"99\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"other_isOpenClick\": [\n" +
                "                    \"91\",\n" +
                "                    \"93\",\n" +
                "                    \"98\",\n" +
                "                    \"99\",\n" +
                "                    \"95\",\n" +
                "                    \"97\"\n" +
                "                ],\n" +
                "                \"two_isOpenClick\": [\n" +
                "                    \"93\",\n" +
                "                    \"95\",\n" +
                "                    \"97\",\n" +
                "                    \"99\"\n" +
                "                ]\n" +
                "            },\n" +
                "            \"event_id\": \"1491967649384\",\n" +
                "            \"httpstatus\": 200,\n" +
                "            \"messages\": [],\n" +
                "            \"status\": true,\n" +
                "            \"task_type\": \"submit_order\",\n" +
                "            \"validateMessages\": {},\n" +
                "            \"validateMessagesShowId\": \"_validatorMessage\",\n" +
                "            \"userId\": \"8a9d4f085adb1441015adb2208600022\",\n" +
                "            \"kyfwUserId\":\"1d3s1f65sd4fsd31f56ds6f\"\n" +
                "\n" +
                "        }";

        Map<String, Object> stringObjectMap = JSonUtils.readValue(str, Map.class);

        List<Passenger> passengerList = passengerService.freshPassengers(stringObjectMap);

        Assert.assertTrue(passengerList.size() > 0);

    }

    @Test
    public void bindingKyfwByUserTestAndGetKyfwByUser() {

        String userName = "12306_testNo";
        String passWord = "12306_testPassword";

        //绑定12306账号
        boolean bl = passengerService.bindingKyfwByUser(user.getUserId(), userName, passWord);
        Assert.assertTrue(bl);

        // 获取12306账号
        Map<String, Object> map = passengerService.getKyfwByUser(user.getUserId());
        Assert.assertTrue(map != null);

        KyfwInUserRela kyfwInUserRela = kyfwInUserRelaDao.findByUserId(user.getUserId());
        kyfwInUserRelaDao.delete(kyfwInUserRela);

        KyfwUser kyfwUser = kyfwUserDao.findByUserName(userName);
        kyfwUserDao.delete(kyfwUser);

        userDao.delete(user);
    }

}
