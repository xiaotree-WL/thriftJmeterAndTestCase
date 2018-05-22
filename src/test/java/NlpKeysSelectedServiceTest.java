import com.techwolf.oceanus.listwordhighlight.idl.*;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 测试传入job对象和geek对象的List，返回每个geek对象的标签词
 * wanglin01@kanzhun.com 2018.04.27
 */
public class NlpKeysSelectedServiceTest {

    private TTransport transport;

    @Before
    public void init() throws TTransportException {
        // ip为启动服务的ip地址,端口号是默认的25000
        transport=new TFramedTransport(new TSocket("172.16.24.122", 25000));
        transport.open();


    }

    @Test
    public void geeksTagsSelected() throws TException {
        // 设置传输协议为 TBinaryProtocol
        TProtocol protocol=new TBinaryProtocol(transport);//new TMultiplexedProtocol(new TCompactProtocol(transport),NlpKeysSelectedService.class.getCanonicalName()+"$Iface");
        NlpKeysSelectedService.Client searchService=new NlpKeysSelectedService.Client(protocol);

        //构造入参和出参
        // 构造job对象
        long jobId = 120107;
        String jobTitle = "性能测试工程师";
        int jobPosition = 100304;
        String jobDesc = "优秀的你可以有美术设计功底且热爱影视；优秀的你可以有电影电视剧以及新媒体等影视海报物料等设计经验；你也可以有独到的设计理念…有以上任何一点 我们都期待见到优秀的你！";
        String[] jobSkills = {"CNN", "平面设计", "编程", "研发"};
        List<String> jobSkillArray = Arrays.asList(jobSkills);
        NlpKhJobInfo jobInfo = new NlpKhJobInfo(jobId, jobTitle, jobPosition, jobDesc, jobSkillArray);
        // 构造geek对象
        long geekId = 320656533;
        String geekDesc = "基本的 shell、 Loadrunner、 Jmeter、基本的T-sqJorofler、Nmon、QTP、 monkeyrunner等，曾独立负责过国家级机场航站楼弱电类三方验收检测和军工类应用管理系统的综合检测。六年的性能测试，让我不但收获了丰富的经验，而且还让我养成了怀疑一切的性格，让我知道了在数据和结果面前，只有优化和完善才是唯一的正途。";
        long workId1 = 1;
        String workContent1 = "电商平台性能测试，接口测试等。利用测试场景及不同的负载压力为系统优化和系统评估提供数据支撑，分析定位潜在问题和风险，预估系统数据增长瓶颈。并兼顾系统稳定性分析评估和健壮性分析评估";
        String workPerf1 = null;
        String[] workSkills1 = {"性能测试", "软件测试", "sever端测试"};
        NlpKhWorkExpInfo workExpInfo1 = new NlpKhWorkExpInfo(workId1, workContent1, workPerf1, Arrays.asList(workSkills1));
        long workId2 = 2;
        String workContent2 = "1，公司产品基础平台的整体性能测试，并分析瓶颈原因，出具测试报告。    2，公司产品架构优化测试，配置测试，接口测试。分布式集群优化测试和稳定性测试。     3，性能测试技术培训和知识难点分享。    4，各分公司项目系统性能测试及技术支持。     5,功能测试及兼容性测试。    6,测试需求提取和测试方案的制定，测试脚本开发，测试用例执行，测试问题定位，测试结果分析和测试报告编写。";
        String workPerf2 = "平台整体性问题定位和分析，提供优化方向和瓶颈原因，优化平台配置，提高并行处理能力，提高软件可靠性。    在部门内部教授性能测试技术，并培养了3-5名初级性能测试工程师，分享性能测试经验。";
        String[] workSkills2 = {};
        NlpKhWorkExpInfo workExpInfo2 = new NlpKhWorkExpInfo(workId2, workContent2, workPerf2, Arrays.asList(workSkills2));
        List<NlpKhWorkExpInfo> workExpInfoList = Arrays.asList(workExpInfo1, workExpInfo2);
        long projId1 = 1;
        String projDesc1 = "民航弱点类信息软件的性能和功能测试。包括测试需求提取，测试方案制定，测试脚本开发和维护，测试执行，结果分析，问题定位和出具测试报告。";
        String projPerf1 = "深圳宝安机场弱点类软件测试";
        NlpKhProjExpInfo projExpInfo1 = new NlpKhProjExpInfo(projId1, projDesc1, projPerf1);
        List<NlpKhProjExpInfo> projExpInfoList = Arrays.asList(projExpInfo1);
        final NlpKhGeekInfo geekInfo = new NlpKhGeekInfo(geekId, geekDesc, workExpInfoList, projExpInfoList);
        final List<NlpKhGeekInfo> geekInfos = new ArrayList<NlpKhGeekInfo>() {{
            for (int i = 0; i < 10; i++)
                this.add(geekInfo);
        }};
        for (int i = 0; i <1000 ; i++) {
            Map<Long,List<String>> result = searchService.geeksTagsSelected(jobInfo, geekInfos);
            System.out.println(result);
        }

    }
}