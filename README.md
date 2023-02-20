# Error Code Generator  

Yaml 형태의 메시지 파일을 입력으로 받아 Java Enum Class 생성한다.  


## 사용 방법  
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.mobigen.platform</groupId>
            <artifactId>error-codegen</artifactId>
            <version>1.0.0</version>
            <configuration>
                <input>src/resources/message.yml</input> 
                <output>${project.basedir}/com/mobigen/platform/errcodegen/ResponseCode.java</output>
            </configuration>
        </plugin>
    </plugins>
</build>
```
