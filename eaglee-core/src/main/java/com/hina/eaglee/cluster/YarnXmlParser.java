package com.hina.eaglee.cluster;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class YarnXmlParser {
    /**
     * 将XML字符串解析为YarnApps对象
     */
    public static YarnApps parseXml(String xml) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(YarnApps.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (YarnApps) unmarshaller.unmarshal(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))
        );
    }

    // 测试方法
    public static void main(String[] args) throws Exception {
        String xml = """
                <apps>
                    <app>
                        <id>application_1744287866674_1089269</id>
                        <user>xy_app_spark</user>
                        <name>商户产品调用量实时统计</name>
                        <queue>root.xy_yarn_pool.production</queue>
                        <state>RUNNING</state>
                        <finalStatus>UNDEFINED</finalStatus>
                        <progress>100.0</progress>
                        <trackingUI>ApplicationMaster</trackingUI>
                        <trackingUrl>http://cdh85-39:8088/proxy/application_1744287866674_1089269/</trackingUrl>
                        <diagnostics/>
                        <clusterId>1744287866674</clusterId>
                        <applicationType>Apache Flink</applicationType>
                        <applicationTags/>
                        <priority>0</priority>
                        <startedTime>1757928664889</startedTime>
                        <finishedTime>0</finishedTime>
                        <elapsedTime>3959398121</elapsedTime>
                        <amContainerLogs>http://cdh85-181:8042/node/containerlogs/container_e3732_1744287866674_1089269_01_000001/xy_app_spark</amContainerLogs>
                        <amHostHttpAddress>cdh85-181:8042</amHostHttpAddress>
                        <amRPCAddress>cdh85-181:39755</amRPCAddress>
                        <allocatedMB>36864</allocatedMB>
                        <allocatedVCores>7</allocatedVCores>
                        <reservedMB>0</reservedMB>
                        <reservedVCores>0</reservedVCores>
                        <runningContainers>3</runningContainers>
                        <memorySeconds>145958611570</memorySeconds>
                        <vcoreSeconds>27715667</vcoreSeconds>
                        <queueUsagePercentage>0.10485061</queueUsagePercentage>
                        <clusterUsagePercentage>0.07129842</clusterUsagePercentage>
                        <resourceSecondsMap>
                            <entry>
                            <key>memory-mb</key>
                            <value>145958611570</value>
                            </entry>
                            <entry>
                            <key>vcores</key>
                            <value>27715667</value>
                            </entry>
                        </resourceSecondsMap>
                        <preemptedResourceMB>0</preemptedResourceMB>
                        <preemptedResourceVCores>0</preemptedResourceVCores>
                        <numNonAMContainerPreempted>0</numNonAMContainerPreempted>
                        <numAMContainerPreempted>0</numAMContainerPreempted>
                        <preemptedMemorySeconds>0</preemptedMemorySeconds>
                        <preemptedVcoreSeconds>0</preemptedVcoreSeconds>
                        <preemptedResourceSecondsMap/>
                        <logAggregationStatus>NOT_START</logAggregationStatus>
                        <unmanagedApplication>false</unmanagedApplication>
                        <amNodeLabelExpression/>
                        <timeouts>
                            <timeout>
                            <type>LIFETIME</type>
                            <expiryTime>UNLIMITED</expiryTime>
                            <remainingTimeInSeconds>-1</remainingTimeInSeconds>
                            </timeout>
                        </timeouts>
                    </app>
                </apps>
                """;
        YarnApps yarnApps = parseXml(xml);
        System.out.println("解析到的应用数量：" + yarnApps.getAppList().size());
    }
}