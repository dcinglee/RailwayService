package com.railwayservice.common;

import com.railwayservice.application.util.JSonUtils;
import com.railwayservice.stationmanage.dao.LineStationDao;
import com.railwayservice.stationmanage.entity.LineStation;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.InputStream;
import java.util.*;

public class InitTrans {

    private static String[] trans = {"D932/D933", "G4901", "K817", "K818", "K819", "Z23", "G291/G294", "G4910", "K6522", "K6521", "Z162", "Z161", "G576/G577", "K9063/K9066", "K9084/K9085", "G6485/G6488", "Z49", "K4655/K4658", "K4302/K4303", "G2626/G2627", "K820", "K5212", "K5211", "G492/G493", "K5216", "Z50", "G1830", "Z54", "Z53", "Z4177", "G6330/G6331", "G1829", "G6322/G6323", "G6075/G6078", "Z76", "Z75", "Z78", "Z77", "G1952/G1953", "G26", "K630", "G25", "G279", "D2003", "D2002", "D2005", "D2004", "G9642/G9643", "D2001", "G6145/G6148", "D2006", "K629", "G6318/G6319", "Z96", "Z95", "G4987", "G4988", "K218/K219", "G6076/G6077", "T231", "T232", "G280", "G1261/G1264", "G818/G819", "G844/G845", "K401", "G286", "K402", "G285", "3010", "3009", "G6321/G6324", "G6313/G6316", "G821/G824", "Z230/Z231", "G66", "G65", "G68", "G6701", "G67", "G6703", "3022", "G69", "3021", "G6705", "G6704", "G6707", "G6706", "G6709", "G2625/G2628", "G71", "G6314/G6315", "G70", "G73", "G72", "G75", "G686/G687", "G74", "K598/K599", "G6710", "G77", "G76", "G79", "G6712", "G6711", "G78", "G6714", "G6713", "G6718", "T10", "G80", "G1262/G1263", "G82", "G81", "G84", "G83", "T287/T290", "G86", "G85", "G88", "G87", "G89", "G8906", "G8907", "G8905", "C7085", "C7084", "G8908", "C7087", "G8909", "C7086", "C7089", "C7088", "G1315/G1318", "G491/G494", "C7080", "G91", "C7083", "G90", "C7082", "G93", "G92", "G94", "G6305/G6308", "K7726/K7727", "G8910", "G6732", "G4553", "G6731", "G6734", "G6733", "G6736", "G6735", "G4559", "C7096", "C7095", "C7097", "C7099", "C7090", "C7092", "C7091", "C7094", "C7093", "K471", "G826/G827", "G4560", "G6741", "G6743", "K472", "G275/G278", "G6501", "G4565", "G6742", "G836/G837", "G4566", "G6745", "G6503", "G4567", "K479", "G6744", "C7059", "G6502", "G4568", "G6306/G6307", "G6746", "G6504", "G6507", "G6506", "G6071/G6074", "G6508", "C7063", "C7062", "G2811/G2814", "C7065", "C7064", "C7067", "C7066", "G257/G260", "C7068", "T49", "C7061", "C7060", "G6510", "G6512", "G2922/G2923", "G6511", "G6514", "G6513", "G6516", "G6515", "G6518", "G6517", "T50", "G6519", "C7074", "C7073", "C7076", "C7075", "C7078", "C7077", "C7079", "C7072", "C7071", "G6521", "G6520", "G6523", "G6522", "G6525", "C7038", "G288/G289", "C7037", "G6524", "G6527", "C7039", "G6526", "G6529", "G6528", "C7041", "C7040", "C7043", "C7042", "C7045", "C7044", "C7047", "C7046", "K238/K239", "G6486/G6487", "G6530", "K480", "G6532", "G6531", "G6534", "G6533", "C7049", "G6536", "G6535", "C7048", "G6538", "3098", "G6537", "3097", "G6539", "C7052", "C7051", "C7056", "G1145/G1148", "C7055", "C7058", "C7057", "G6146/G6147", "C7050", "K270", "G6541", "G6540", "G6543", "G6542", "G9646/G9647", "G6545", "G6544", "G6547", "G1289/G1292", "G6546", "G6549", "C7018", "K279", "C7017", "C7019", "C7021", "C7020", "C7023", "C7022", "C7025", "C7024", "G1162", "K280", "G1161", "K261", "K262", "T9", "K269", "C7027", "C7026", "K267", "C7029", "K268", "C7028", "C7030", "T96", "T95", "K9018", "C7032", "C7031", "K9017", "C7034", "C7033", "C7036", "C7035", "G6141/G6144", "G9641/G9644", "D942/D943", "C7001", "C7003", "C7002", "G1144", "G1143", "G1141", "G1140", "C7005", "C7004", "C7007", "C7006", "C7009", "C7008", "C7010", "G1139", "G1138", "C7012", "C7011", "G1136", "C7014", "G1135", "C7013", "G1134", "G1133", "G1132", "G1130", "G6101", "G6103", "G6102", "G6105", "G6302/G6303", "G6317/G6320", "G1129", "G1128", "G1127", "G1126", "G847/G850", "G1125", "G1124", "G1123", "G1122", "G1121", "G1120", "G6110", "G6112", "G6111", "G6116", "G6118", "G6117", "G6481/G6484", "G6119", "G6301/G6304", "G1117", "G1116", "G1115", "G1114", "K4602", "G1113", "K4601", "G1112", "G1111", "G1110", "G6121", "G6120", "G6122", "K9094", "K9093", "G311/G314", "G1109", "G1108", "G1107", "G1106", "G1105", "G1104", "G1103", "G1102", "Z1", "G1101", "Z2", "Z3", "Z4", "Z5", "Z6", "K1364", "K1363", "G6132", "G1286/G1287", "G6131", "K950/K951", "Z201", "Z202", "G1572", "G1571", "K9076", "K9075", "G1285/G1288", "G510", "G512", "G511", "G514", "G516", "G1566", "G518", "G517", "G1565", "G1564", "G519", "G1563", "T3040", "T3039", "T3038", "T3037", "G6163/G6166", "G9668", "G501", "G503", "G1559", "G840/G841", "G502", "G505", "G504", "G507", "G506", "G509", "G508", "G9672", "G9671", "G9673", "G9678", "G9677", "G530", "G532", "G531", "G1306", "G1305", "G535", "G1304", "G538", "G1303", "K6614", "G1302", "G1301", "G258/G259", "K6605", "G521", "G520", "G523", "G525", "G1282/G1283", "G524", "G527", "G1534", "G529", "G1533", "G528", "G1532", "G1531", "D936/D937", "G9694", "G9698", "G550", "K4218", "G310", "G552", "G551", "G554", "G553", "G556", "G6160/G6161", "G555", "G558", "G1526", "G557", "G1525", "G1524", "G1523", "G1522", "K9059/K9062", "G1521", "K4217", "G1281/G1284", "G1848/G1849", "G487/G490", "G541", "G542", "G545", "G544", "G547", "G546", "G307", "G549", "G548", "G309", "K4204", "G308", "G817/G820", "G570", "G572", "G571", "G574", "G573", "G1748", "G1747", "G1746", "G1504", "G1745", "G579", "G1744", "G1743", "G1742", "G1741", "G6329/G6332", "K4230", "G312/G313", "K585/K588", "G561", "K4229", "G563", "G562", "G565", "G564", "G567", "G566", "G569", "G1847/G1850", "G568", "Z285", "Z286", "G1545/G1548", "G640/G641", "G6164/G6165", "G1152/G1153", "G580", "G585", "G9645/G9648", "G587", "G586", "G588", "G1712", "G1711", "G1710", "K535/K538", "G1709", "G1294/G1295", "G832/G833", "G6151/G6154", "G1272/G1273", "K9060/K9061", "G1312/G1313", "G363", "G365", "G364", "G366", "K836/K837", "D6721", "G9063", "K4293", "K4294", "G9064", "G287/G290", "G4611", "G4612", "G4613", "G4614", "K508", "K507", "G2926/G2927", "K7725/K7728", "D909", "K485/K488", "D904", "D903", "D902", "D901", "G1032/G1033", "G839/G842", "G1293/G1296", "D935/D938", "D910", "G1031/G1034", "G2925/G2928", "G6142/G6143", "D928", "D927", "G4651", "G4652", "G4653", "D924", "G4654", "D923", "D922", "D921", "G1151/G1154", "D939", "G4663", "G4664", "G488/G489", "G4672", "G6072/G6073", "G4673", "G4674", "D940", "G6310/G6311", "K590", "G2921/G2924", "K117", "K118", "K597/K600", "K589", "K105", "K106", "T167", "T168", "C7159", "C7158", "G2812/G2813", "K9083/K9086", "K9064/K9065", "K486/K487", "K9122", "K9121", "G6309/G6312", "C7160", "G1290/G1291", "G1316/G1317", "G1560/G1561", "K835/K838", "C7137", "C7136", "K157", "K217/K220", "K158", "C7138", "K4301/K4304", "C7140", "C7142", "C7141", "C7144", "C7143", "C7146", "C7145", "G6152/G6153", "G4011", "G4012", "G1546/G1547", "G4013", "G4014", "G4015", "G4016", "T288/T289", "C7148", "G4017", "C7147", "G4018", "G4019", "C7149", "G822/G823", "C7153", "C7152", "C7155", "C7154", "C7157", "G692/G693", "C7156", "G831/G834", "G4020", "G6202", "G6201", "G6204", "G6203", "C7115", "G6205", "C7114", "K179", "C7117", "G6208", "G6207", "C7116", "C7119", "G6209", "C7118", "G6159/G6162", "C7120", "C7122", "G96/G97", "C7121", "G1951/G1954", "C7124", "C7123", "G1022", "G1021", "G1020", "K180", "G6211", "G6213", "G6212", "G6215", "G6214", "C7126", "C7125", "G6216", "G6219", "C7128", "C7127", "G6218", "C7129", "G1019", "C7131", "G1018", "G1017", "C7130", "C7133", "G1016", "G1015", "C7132", "C7135", "G1014", "G1013", "K536/K537", "C7134", "G1012", "G1011", "G1010", "G6222", "G6221", "G6224", "G6223", "G1271/G1274", "G6225", "G6227", "G6229", "G1009", "G1008", "G1007", "G1006", "G1005", "C7100", "G1004", "G1003", "C7102", "C7101", "G1002", "G1001", "G8891", "G6231", "G8894", "G8895", "G6230", "G4053", "G8892", "G4054", "G8893", "G8898", "G6234", "G6237", "G8896", "G8897", "G6236", "C7104", "C7103", "C7106", "C7105", "C7107", "C7109", "C7111", "C7110", "C7113", "C7112", "G1311/G1314", "G6240", "G6482", "G691/G694", "G6242", "G6241", "G6244", "G6002", "G6001", "G6246", "G6248", "G575/G578", "G6326/G6327", "G611", "G610", "G613", "G612", "G615", "G614", "K1164/K1165", "D931/D934", "G617", "G616", "G619", "G618", "K7751/K7754", "G6251", "G6250", "G6253", "G6011", "G6252", "G6255", "G6013", "Z229/Z232", "G6254", "G6012", "G6015", "G95/G98", "G6014", "G1955/G1958", "G6017", "G6258", "G6016", "G6019", "G6018", "G602", "G601", "G604", "G603", "G606", "G605", "G608", "G607", "G609", "G6260", "G1701/G1704", "G6020", "G6022", "G6021", "G6266", "G6024", "G1702/G1703", "G6023", "G6026", "G6025", "G6028", "G6027", "G1956/G1957", "G6029", "G631", "G633", "G2901", "G632", "G2902", "G635", "G2903", "G634", "G637", "G2904", "G2905", "G636", "G2906", "G638", "K949/K952", "K586/K587", "G6031", "G6030", "G6033", "G6032", "G6034", "G6325/G6328", "G620", "G622", "G825/G828", "G2911", "G621", "G2912", "G624", "G2913", "G623", "G2914", "G626", "G292/G293", "G2915", "G625", "G2916", "G2917", "K1163/K1166", "G2918", "G651", "G653", "G652", "G655", "G657", "G656", "G659", "G658", "D941/D944", "T55/T58", "G402", "G401", "G404", "G403", "G406", "G405", "G639/G642", "G276/G277", "K7752/K7753", "G671", "G670", "G673", "K22", "G431", "G672", "G430", "K21", "G433", "G674", "G685/G688", "G1408", "G432", "G1407", "G434", "G1406", "G835/G838", "G1405", "G1404", "G1403", "G1402", "G1401", "T56/T57", "G1275/G1278", "Z151", "Z150", "Z152", "K237/K240", "G660", "G662", "G661", "G422", "G664", "G421", "G663", "G666", "G424", "G665", "G423", "G668", "G426", "G425", "G669", "G429", "Z149", "G1276/G1277"};
    private LineStationDao lineStationDao;

    public InitTrans() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:springDataJpa.xml");
        lineStationDao = ac.getBean(LineStationDao.class);
    }

    /**
     * 发送 GET 请求（HTTP）带参数。
     */
    public static String httpGet(String url, Map<String, Object> params) {
        String apiUrl = url;
        String result = null;
        StringBuilder param = new StringBuilder();
        try {
            boolean first = true;
            for (String key : params.keySet()) {
                if (first && !apiUrl.contains("?")) {
                    param.append("?");
                    first = false;
                } else {
                    param.append("&");
                }
                param.append(key).append("=").append(params.get(key));
            }
            apiUrl += param.toString();
            HttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiUrl);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (Exception e) {
            System.out.println("Http访问异常：" + e.getMessage());
        }
        return result;
    }

    public static void main(String[] args) {
        InitTrans initTrans = new InitTrans();
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(trans));
        String[] transes = set.toArray(new String[0]);
        System.out.println(trans.length + "/" + set.size());
        for (int i = 0; i < transes.length; i++) {
            if (transes[i] != null && transes[i].startsWith("G")) {
                Map<String, Object> params = new HashMap<>();
                params.put("appkey", "3440d87e555c7cfd");
                params.put("trainno", trans[i]);
                String result = httpGet("http://api.jisuapi.com/train/line", params);
                initTrans.insertTrainLine(result);
            }
        }
    }

    private void insertTrainLine(String result) {
        Map map = JSonUtils.readValue(result, Map.class);
        if (!"0".equals(map.get("status"))) {
            System.out.println("not ok");
            return;
        }

        Map resultMap = (Map) map.get("result");
        if (resultMap == null || resultMap.size() == 0) {
            System.out.println("no result");
            return;
        }

        String trainno = (String) resultMap.get("trainno");
        String type = (String) resultMap.get("type");
        List<Map<String, String>> trains = (List<Map<String, String>>) resultMap.get("list");
        if (trains == null || trains.size() == 0) {
            System.out.println("no trains");
            return;
        }

        int count = 0;
        for (Map<String, String> train : trains) {
            try {
                LineStation lineStation = new LineStation();
                lineStation.setLineNo(trainno);
                lineStation.setType(type);
                lineStation.setStation(train.get("station"));
                lineStation.setSortNo(Integer.valueOf(train.get("sequenceno")));
                lineStation.setArriveTime(train.get("arrivaltime"));
                lineStation.setDepartTime(train.get("departuretime"));
                lineStation.setStopTime(train.get("stoptime"));
                lineStation.setCreateDate(new Date());
                lineStationDao.save(lineStation);
                count++;
            } catch (Exception e) {
                System.out.println("one fail");
            }
        }
        System.out.println("total:" + trains.size() + ",success:" + count);
    }

}