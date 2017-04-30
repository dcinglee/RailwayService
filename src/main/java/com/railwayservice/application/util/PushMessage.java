package com.railwayservice.application.util;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.*;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * 百度云推广
 * TODO
 *
 * @author lixs
 */
public class PushMessage {
    //ApiKey是应用标识，在SDK调用过程中唯一标识一个应用 SecretKey是调用API时的Token，用来验证请求的合法性，请注意保密
    private static final String serviceProviderApiKey = "OLzGspbMGAV3Td0hjxujPKkT";
    //secretKey与ApiKey配套使用
    private static final String serviceProviderSecretKey = "68z9liuCZwUbyRob1LXknX5jjMnZfGsB";
    PushKeyPair serviceProviderPair = new PushKeyPair(serviceProviderApiKey, serviceProviderSecretKey);
    BaiduPushClient serviceProviderPushClient = new BaiduPushClient(serviceProviderPair,
            BaiduPushConstants.CHANNEL_REST_URL);
    
    
  //ApiKey是应用标识，在SDK调用过程中唯一标识一个应用 SecretKey是调用API时的Token，用来验证请求的合法性，请注意保密
    private static final String merchantApiKey = "4f0nGqxuhEFvWtUPh4GSsGwm";
    //secretKey与ApiKey配套使用
    private static final String merchantSecretKey = "kSH3wg664E9i6m00MxhcetUorM7kQEHE";
    PushKeyPair merchantPair = new PushKeyPair(merchantApiKey, merchantSecretKey);
    BaiduPushClient merchantClient = new BaiduPushClient(merchantPair,
            BaiduPushConstants.CHANNEL_REST_URL);
    
    

    public static void main(String[] args) throws Exception {
    }

    /**
     * 创建标签分组tag
     *
     * @param tagName
     * @throws Exception
     */
    public void create(String tagName, Integer deviceType) throws Exception {
        serviceProviderPushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {
            // 4. specify request arguments
            CreateTagRequest request = new CreateTagRequest().addTagName(
                    "配送人员").addDeviceType(3);
            // 5. http request
            CreateTagResponse response = serviceProviderPushClient.createTag(request);
            System.out.println(String.format("tagName: %s, result: %d",
                    response.getTagName(), response.getResult()));
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }

    /**
     * 向tag组中添加设备
     *
     * @param channelId
     * @param tagName
     * @throws Exception
     */
    public void addDeviceToTag(String[] channelId, String tagName, Integer deviceType) throws Exception {
        serviceProviderPushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });
        try {
            // 4. specify request arguments
            String[] channelIds = {"4211379536044258627", "3786585465353975804"};
            AddDevicesToTagRequest request = new AddDevicesToTagRequest()
                    .addTagName("配送人员").addChannelIds(channelIds)
                    .addDeviceType(3);
            // 5. http request
            AddDevicesToTagResponse response = serviceProviderPushClient
                    .addDevicesToTag(request);
            // Http请求结果解析打印
            if (null != response) {
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("devicesInTag：{");
                List<?> devicesInfo = response.getDevicesInfoAfterAdded();
                for (int i = 0; i < devicesInfo.size(); i++) {
                    Object object = devicesInfo.get(i);
                    if (i != 0) {
                        strBuilder.append(",");
                    }
                    if (object instanceof DeviceInfo) {
                        DeviceInfo deviceInfo = (DeviceInfo) object;
                        strBuilder.append("{channelId:"
                                + deviceInfo.getChannelId() + ",result:"
                                + deviceInfo.getResult() + "}");
                    }
                }
                strBuilder.append("}");
                System.out.println(strBuilder.toString());
            }
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }

    /**
     * 删除tag组
     *
     * @param tagName
     * @throws Exception
     */
    public void deleteTag(String tagName) throws Exception {
        try {
            // 4. specify request arguments
            DeleteTagRequest request = new DeleteTagRequest().addTagName(
                    "xxxxx").addDeviceType(new Integer(3));
            // 5. http request
            DeleteTagResponse response = serviceProviderPushClient.deleteTag(request);
            // Http请求结果解析打印
            System.out.println(String.format("tagName: %s, result: %d",
                    response.getTagName(), response.getResult()));
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }

    /**
     * 从tag组中删除设备
     *
     * @param channelId
     * @param tagName
     * @throws Exception
     */
    public void deleteDeivicesFromTag(String[] channelId, String tagName, Integer deviceType) throws Exception {
        try {
            // 4. specify request arguments
            String[] channelIds = {"xxxxxxxxxxxxxxxxx"};
            DeleteDevicesFromTagRequest request = new DeleteDevicesFromTagRequest()
                    .addTagName("xxxxx").addChannelIds(channelIds)
                    .addDeviceType(3);
            // 5. http request
            DeleteDevicesFromTagResponse response = serviceProviderPushClient
                    .deleteDevicesFromTag(request);
            // Http请求结果解析打印
            if (null != response) {
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("devicesInfoAfterDel:{");
                List<?> list = response.getDevicesInfoAfterDel();
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        strBuilder.append(",");
                    }
                    Object object = list.get(i);
                    if (object instanceof DeviceInfo) {
                        DeviceInfo deviceInfo = (DeviceInfo) object;
                        strBuilder.append("{channelId: "
                                + deviceInfo.getChannelId() + ", result: "
                                + deviceInfo.getResult() + "}");
                    }
                }
                strBuilder.append("}");
                System.out.println(strBuilder.toString());
            }
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }

    }

    /**
     * 批量单播的推送
     *
     * @param channelId
     * @param deviceType 3 android; 4 
     * @throws Exception
     */
    //消息入参太多
    public void pushMessageToBatch4ServiceProvider(String[] channelId, Integer[] deviceType, String title, String description) throws Exception {
        try {
//            if ( deviceType == 3 ) {
                // 4. specify request arguments
                //创建Android通知
                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("description", description);
                notification.put("notification_builder_id", 0);
                notification.put("notification_basic_style", 4);
                notification.put("open_type", 1);
                notification.put("url", "http://push.baidu.com");
                JSONObject jsonCustormCont = new JSONObject();
                jsonCustormCont.put("key", "value"); //自定义内容，key-value
                notification.put("custom_content", jsonCustormCont);
//            }
            String[] channelIds = channelId;
//			String[] channelIds = { "4211379536044258627", "3786585465353975804" };
            PushBatchUniMsgRequest request = new PushBatchUniMsgRequest()
                    .addChannelIds(channelIds)
                    .addMsgExpires(new Integer(3600))
                    .addMessageType(1)
                    .addMessage("{\"title\":\"" + title + "\",\"description\":\"" + description + "\"}")
                    .addDeviceType(3)
                    .addTopicId("BaiduPush");// 设置类别主题
            // 5. http request
            PushBatchUniMsgResponse response = serviceProviderPushClient
                    .pushBatchUniMsg(request);
            // Http请求结果解析打印
            System.out.println(String.format("msgId: %s, sendTime: %d",
                    response.getMsgId(), response.getSendTime()));
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }

    
    /**
     * 批量单播的推送
     *
     * @param channelId
     * @param deviceType 3 android; 4 
     * @throws Exception
     */
    //消息入参太多
    public void pushMessageToBatch4Merchant(String channelId, Integer deviceType, String title, String description) throws Exception {
        try {
//            if ( deviceType == 3 ) {
                // 4. specify request arguments
                //创建Android通知
                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("description", description);
                notification.put("notification_builder_id", 0);
                notification.put("notification_basic_style", 4);
                notification.put("open_type", 1);
                notification.put("url", "http://push.baidu.com");
                JSONObject jsonCustormCont = new JSONObject();
                jsonCustormCont.put("key", "value"); //自定义内容，key-value
                notification.put("custom_content", jsonCustormCont);
//            }
//            String[] channelIds = channelId;
//			String[] channelIds = { "4211379536044258627", "3786585465353975804" };
//            PushBatchUniMsgRequest request = new PushBatchUniMsgRequest()
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest()
                    .addChannelId(channelId)
                    .addMsgExpires(new Integer(3600))
                    .addMessageType(1)
                    .addMessage("{\"title\":\"" + title + "\",\"description\":\"" + description + "\"}")
                    .addDeviceType(3)
                    .addTopicId("BaiduPush");// 设置类别主题
            // 5. http request
            
            PushMsgToSingleDeviceResponse response = merchantClient
					.pushMsgToSingleDevice(request);
//            PushBatchUniMsgResponse response = merchantClient
//                    .pushBatchUniMsg(request);
            // Http请求结果解析打印
            System.out.println(String.format("msgId: %s, sendTime: %d",
                    response.getMsgId(), response.getSendTime()));
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }
    
    /**
     * 分组推送，适用于某一群体(配送员)
     *
     * @param tagName
     * @throws Exception
     */
    public void pushMessageToTag(String tagName) throws Exception {
        try {
            // 4. specify request arguments
            // pushTagTpye = 1 for common tag pushing
            PushMsgToTagRequest request = new PushMsgToTagRequest()
                    .addTagName("配送人员")
                    .addMsgExpires(new Integer(3600))
                    .addMessageType(1)  // 添加透传消息
                    // .addSendTime(System.currentTimeMillis() / 1000 + 120) //设置定时任务
                    .addMessage("{\"title\":\"TEST\",\"description\":\"welcome!lixiangs\"}")
                    .addDeviceType(3);
            // 5. http request
            PushMsgToTagResponse response = serviceProviderPushClient.pushMsgToTag(request);
            // Http请求结果解析打印
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
                    + response.getSendTime() + ",timerId: "
                    + response.getTimerId());
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }

    /**
     * 全部推送，适用于版本升级
     *
     * @throws Exception
     */
    public void pushMessageToAll(Integer deviceType) throws Exception {
//		pushClient.setChannelLogHandler(new YunLogHandler() {
//			@Override
//			public void onHandle(YunLogEvent event) {
//				System.out.println(event.getMessage());
//			}
//		});
        try {
            // 4. specify request arguments
            PushMsgToAllRequest request = new PushMsgToAllRequest()
                    .addMsgExpires(new Integer(3600)).addMessageType(1)
                    .addMessage("{\"title\":\"TEST\",\"description\":\"helloevery\"}") //添加透传消息
//					.addSendTime(System.currentTimeMillis() / 1000 + 120) // 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例2分钟后推送
                    .addDeviceType(4);
            // 5. http request
            PushMsgToAllResponse response = serviceProviderPushClient.pushMsgToAll(request);
            // Http请求结果解析打印
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
                    + response.getSendTime() + ",timerId: "
                    + response.getTimerId());
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }

    public void queryTag() throws Exception {
        try {
            // 4. specify request arguments
            QueryTagsRequest request = new QueryTagsRequest()
                    .addTagName("配送人员").addStart(0).addLimit(10)
                    .addDeviceType(3);
            // 5. http request
            QueryTagsResponse response = serviceProviderPushClient.queryTags(request);
            // Http请求结果解析打印
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("totalNum: " + response.getTotalNum() + "\n");
            if (null != response) {
                List<?> list = response.getTagsInfo();
                for (int i = 0; i < list.size(); i++) {
                    Object object = list.get(i);
                    if (object instanceof TagInfo) {
                        TagInfo tagInfo = (TagInfo) object;
                        strBuilder.append("List[" + i + "]: " + "tagId="
                                + tagInfo.getTagId() + ",tag="
                                + tagInfo.getTagName() + ",info="
                                + tagInfo.getInfo() + ",type="
                                + tagInfo.getType() + ",creatTime="
                                + tagInfo.getCreateTime() + "\n");
                    }
                }
                System.out.println(strBuilder.toString());
            }
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }

    public void queryMsgStatus(String[] msgId) throws Exception {
        try {
            // 4. specify request arguments
            String[] msgIds = {"1178821526648164913"};
            QueryMsgStatusRequest request = new QueryMsgStatusRequest()
                    .addMsgIds(msgIds)
                    .addDeviceType(3);
            // 5. http request
            QueryMsgStatusResponse response = serviceProviderPushClient
                    .queryMsgStatus(request);
            // Http请求返回值解析
            System.out.println("totalNum: " + response.getTotalNum() + "\n"
                    + "result:");
            if (null != response) {
                List<?> list = response.getMsgSendInfos();
                for (int i = 0; i < list.size(); i++) {
                    Object object = list.get(i);
                    if (object instanceof MsgSendInfo) {
                        MsgSendInfo msgSendInfo = (MsgSendInfo) object;
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append("List[" + i + "]: {" + "msgId = "
                                + msgSendInfo.getMsgId() + ",status = "
                                + msgSendInfo.getMsgStatus() + ",sendTime = "
                                + msgSendInfo.getSendTime() + ",successCount = "
                                + msgSendInfo.getSuccessCount());
                        strBuilder.append("}\n");
                        System.out.println(strBuilder.toString());
                    }
                }
            }
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMsg: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }

    public void queryDeviceInTag(String tagName, Integer deviceType) throws Exception {
        try {
            // 4. specify request arguments
            QueryDeviceNumInTagRequest request = new QueryDeviceNumInTagRequest()
                    .addTagName("配送人员").addDeviceType(3);
            // 5. http request
            QueryDeviceNumInTagResponse response = serviceProviderPushClient
                    .queryDeviceNumInTag(request);
            if (null != response) {
                System.out.println(String.format("deviceNum: %d",
                        response.getDeviceNum()));
            }
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMessage: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }

    public JSONObject androidMessagePush(String title, String description) {

        JSONObject notification = new JSONObject();
        notification.put("title", title);
        notification.put("description", description);
        notification.put("notification_builder_id", 0);
        notification.put("notification_basic_style", 4);
        notification.put("open_type", 1);
        notification.put("url", "http://push.baidu.com");
        return notification;
    }

    public void iosMessagePush(String message) {
        /**
         * {
         "aps": {
         "alert":"Message From Baidu Cloud Push-Service",
         "sound":"",  //可选
         "badge":0,    //可选
         },
         "key1":"value1",
         "key2":"value2"
         }
         */
    }

}
