package coling;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        getHelp();
        while (scanner.hasNext()) {
            switch (Integer.parseInt(scanner.next())) {
                case 1:
                    try {
                        getElasticInfo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        createIndexes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        linkRepo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        backup();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        getBackupsInfo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        deleteIndexes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        recoverFromBackup();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    getHelp();
                    break;
            }
        }
    }

    private static void recoverFromBackup() throws IOException{
        System.out.println(post("http://localhost:9200/_snapshot/my_backup/snapshot_1/_restore", ""));
    }

    private static void deleteIndexes() throws IOException{

    }

    private static void getBackupsInfo() throws IOException{
        System.out.println(get("http://localhost:9200/_snapshot/my_backup/_all"));
    }

    private static void backup() throws IOException {
        System.out.println(put("http://localhost:9200/_snapshot/my_backup/snapshot_1?wait_for_completion=true", ""));
    }

    private static void getHelp() {
        System.out.println("elasticsearch 测试系统");
        System.out.println("请输入您要执行的命令编号：");
        System.out.println("1: 获取elasticSearch信息");
        System.out.println("2: 创建一个索引");
        System.out.println("3: 指定仓库");
        System.out.println("4: 备份一个快照");
        System.out.println("5: 查询快照信息");
        System.out.println("6: 删除索引");
        System.out.println("7: 从快照中恢复");
        System.out.println("0: 获取帮助");
    }

    private static void linkRepo() throws IOException {
        System.out.println(put("http://localhost:9200/_snapshot/my_backup", "{\n" +
                "  \"type\": \"fs\",\n" +
                "  \"settings\": {\n" +
                "    \"location\": \"/backup/es_backup_origin\"\n" +
                "  }\n" +
                "}"));
    }

    private static void createIndexes() throws IOException {
        System.out.println(post("http://localhost:9200/megacorp/employee/1", "{\n" +
                "    \"_index\": \"megacorp\",\n" +
                "    \"_type\": \"employee\",\n" +
                "    \"_id\": \"1\",\n" +
                "    \"_version\": 1,\n" +
                "    \"result\": \"created\",\n" +
                "    \"_shards\": {\n" +
                "        \"total\": 2,\n" +
                "        \"successful\": 1,\n" +
                "        \"failed\": 0\n" +
                "    },\n" +
                "    \"_seq_no\": 0,\n" +
                "    \"_primary_term\": 1\n" +
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

    private static void getElasticInfo() throws IOException {
//        URL url = new URL("http://localhost:9200/");
//        URLConnection urlConnection = url.openConnection();                                                    // 打开连接
//        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8")); // 获取输入流
//        String line = null;
//        StringBuilder sb = new StringBuilder();
//        while ((line = br.readLine()) != null) {
//            sb.append(line + "\n");
//        }
//        System.out.println(sb.toString());
        System.out.println(get("http://localhost:9200/"));
    }

}