package com.batsoft.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2017/10/16.
 */
public class YamlUtils {
    private static final Logger logger = LogManager.getLogger(YamlUtils.class);

    public static Map<String, Object> yaml2Map(String yamlSource) {
        try {
            YamlMapFactoryBean yaml = new YamlMapFactoryBean();
            yaml.setResources(new ClassPathResource(yamlSource));
            return yaml.getObject();
        } catch (Exception e) {
            logger.error("Cannot read yaml", e);
            return null;
        }
    }
    public static Map<String, Object> yaml2Map(Resource resource) {
        try {
            YamlMapFactoryBean yaml = new YamlMapFactoryBean();
            yaml.setResources(resource);
            return yaml.getObject();
        } catch (Exception e) {
            logger.error("Cannot read yaml", e);
            return null;
        }
    }

    public static Properties yaml2Properties(String yamlSource) {
        try {
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new ClassPathResource(yamlSource));
            return yaml.getObject();
        } catch (Exception e) {
            logger.error("Cannot read yaml", e);
            return null;
        }
    }

    public static Map loadYaml(String path) {
        try {
            String yamlString = preLoad(path);
            if (yamlString == null || yamlString.length() <= 0) {
                return null;
            }
            //初始化Yaml解析器
            Yaml yaml = new Yaml();
            //读入文件
            Object result = yaml.load(yamlString);

            if (result instanceof Map) {
                return (Map) result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成yml 文件
     * @param yamlMap
     * @param outPath 输出路径
     */
    public static void dumpYaml(Map yamlMap, String outPath) {
        PrintWriter writer=null;
        String result="";
        try {
            //初始化Yaml解析器
            Yaml yaml = new Yaml();
             result = yaml.dumpAsMap(yamlMap);
            System.out.println(result);
            File newFile = new File(outPath);
             writer = new PrintWriter(newFile);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            writer.print(result);
            writer.flush();
            writer.close();
        }
    }

    //这个方法主要作用是因为文件中有!!的一行，yaml无法进行解析，所以将其去掉
    private static String preLoad(String path) throws IOException {
        File yamlFile = new File(path);
        String result="";
        BufferedReader fileReader = new BufferedReader(new FileReader(yamlFile));
        try {
            String temp;
            StringBuilder stringBuilder = new StringBuilder();
            while ((temp = fileReader.readLine()) != null) {
                if (temp.trim().startsWith("!!")) {
                    continue;
                }
                stringBuilder.append(temp + "\n");
            }
             result = stringBuilder.toString();
            System.out.println("YAML 内容-->" + result);
        }catch (Exception e){

        }finally {
            fileReader.close();
        }


        return result;
    }

}
