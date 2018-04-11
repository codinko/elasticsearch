package coling;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.Scanner;
/**
 * Created by colin on 2018/3/22.
 */
public class ElasticSearch {
    static String port ="2345";
    static String host;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        getHelp();
        while (scanner.hasNext()) {
            switch (Integer.parseInt(scanner.next())) {
                case 1:
                        getElasticInfo();
                    break;
                case 2:
                    createIndexes();
                    break;
                case 3:
                    linkRepo();
                    break;
                case 4:
                    backup();
                    break;
                case 5:
                    getBackupsInfo();
                    break;
                case 51:
                    deleteBackup();
                    break;
                case 6:
                    deleteIndexes();
                    break;
                case 61:
                    getIndexes();
                    break;
                case 7:
                    recoverFromBackup();
                    break;
                case 8:
                    port = scanner.next();
                    System.out.println(port);
                    break;
                case 9:
                    host = scanner.next();
                    System.out.println("当前主机"+host);
                    break;
                case 0:
                    getHelp();
                    break;
            }
        }
    }
    private static void getIndexes() {
        System.out.println(get("http://"+host+":"+ port +"/megacorp/employee/1"));
    }
    private static void deleteBackup() {
        System.out.println(delete("http://"+host+":" + port + "/_snapshot/my_backup/snapshot_1"));
    }
    private static void recoverFromBackup() {
        System.out.println(post("http://"+host+":"+ port +"/_snapshot/my_backup/snapshot_1/_restore", ""));
    }
    private static void deleteIndexes() {
        System.out.println(delete("http://"+host+":"+ port +"/megacorp"));
    }
    private static void getBackupsInfo() {
        System.out.println(get("http://"+host+":"+ port +"/_snapshot/my_backup/_all"));
    }
    private static void backup() {
        System.out.println(put("http://"+host+":"+ port +"/_snapshot/my_backup/snapshot_1?wait_for_completion=true", ""));
    }
    private static void getHelp() {
        System.out.println("elasticsearch 测试系统");
        System.out.println("请输入您要执行的命令编号：");
        System.out.println("1: 获取elasticSearch信息");
        System.out.println("2: 创建一个索引");
        System.out.println("3: 指定仓库");
        System.out.println("4: 备份一个快照");
        System.out.println("5: 查询快照信息,51: 删除快照");
        System.out.println("6: 删除索引 ,61: 查询索引");
        System.out.println("7: 从快照中恢复");
        System.out.println("8: 指定port");
        System.out.println("9: 指定host");
        System.out.println("0: 获取帮助");
        System.out.println("请指定端口");
    }
    private static void linkRepo() {
        String s = "\\\\\\\\AGENT-55\\\\\\\\backup";
        if (host.equals("172.30.1.53")) {
            s = "\\\\\\\\AGENT-53\\\\backup";
        }
        System.out.println(put("http://"+host+":"+ port +"/_snapshot/my_backup", "{\n" +
                "  \"type\": \"fs\",\n" +
                "  \"settings\": {\n" +
                "    \"location\": \""+s+"\"\n" +
                "  }\n" +
                "}"));
        System.out.println("{\n" +
                "  \"type\": \"fs\",\n" +
                        "  \"settings\": {\n" +
                        "    \"location\": \""+s+"\"\n" +
                        "  }\n" +
                        "}");
    }
    private static void createIndexes() {
        System.out.println(post("http://"+host+":"+ port +"/megacorp/employee/1", "{\n" +
                "    \"first_name\" : \"John\",\n" +
                "    \"last_name\" :  \"Smith\",\n" +
                "    \"age\" :        25,\n" +
                "    \"about\" :      \"I love to go rock climbing\",\n" +
                "    \"interests\": [ \"sports\", \"music\" ]\n" +
                "}"));

    }
    public static String post(String completeUrl, String body) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(completeUrl);
        httpPost.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);
            HttpResponse response = httpClient.execute(httpPost);
            return getResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String delete(String completeUrl) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(completeUrl);
        try {
            httpDelete.getRequestLine();
            HttpResponse response = httpClient.execute(httpDelete);
            return getResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String put(String completeUrl, String body) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(completeUrl);
        httpPut.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPut.getRequestLine();
            httpPut.setEntity(stringEntity);
            HttpResponse response = httpClient.execute(httpPut);
            return getResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String get(String completeUrl) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(completeUrl);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            return getResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getResponse(HttpResponse response) throws IOException {
//        HttpEntity httpEntity = response.getEntity();
//        BufferedReader bufferedReader = null;
//        StringBuilder entityStringBuilder = new StringBuilder();
//        if (httpEntity!=null) {
//            try {
//                bufferedReader=new BufferedReader
//                        (new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8*1024);
//                String line=null;
//                while ((line=bufferedReader.readLine())!=null) {
//                    entityStringBuilder.append(line+"\n");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return entityStringBuilder.toString();
        String json = EntityUtils.toString(response.getEntity());
        return json;
    }
    private static void getElasticInfo()  {
//        URL url = new URL("http://"+host+":"+port+"/");
//        URLConnection urlConnection = url.openConnection();                                                    // 打开连接
//        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8")); // 获取输入流
//        String line = null;
//        StringBuilder sb = new StringBuilder();
//        while ((line = br.readLine()) != null) {
//            sb.append(line + "\n");
//        }
//        System.out.println(sb.toString());
        System.out.println(get("http://"+host+":"+ port +"/"));
    }

}