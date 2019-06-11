https://bitbucket.org/azazar/bitcoin-json-rpc-client/overview


#将Jar包直接install到本地 maven repository 中去

#具体命令如下
mvn install:install-file -Dfile=bitcoin-json-rpc-client-1.1.jar -DgroupId=com.azazar -DartifactId=bitcoin-json-rpc-client -Dversion=1.1 -Dpackaging=jar

#具体解释
mvn install:install-file #固定格式

-Dfile=bitcoin-json-rpc-client-1.1.jar #指定包名

-DgroupId=com.azazar  		   #指定groupId, 通常在项目的pom.xml文件中找到

-DartifactId=bitcoin-json-rpc-client #指定artifactId, 通常在项目的pom.xml文件中找到


-Dversion=1.1 #版本号，对应项目pom.xml 文件中的version元素节点


-Dpackaging=jar #打包类型，对应pom.xml文件中的packaging
 



