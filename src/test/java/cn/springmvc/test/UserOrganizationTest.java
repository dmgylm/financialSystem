package cn.springmvc.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.financial.model.Organization;
import cn.financial.model.UserOrganization;
import cn.financial.service.OrganizationService;
import cn.financial.service.UserOrganizationService;
import cn.financial.util.HttpClient3;
import cn.financial.util.TreeNode;
import cn.financial.util.UuidUtil;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml", "classpath:spring/spring-redis.xml"})
/**
 * 用户组织结构关联表
 * @author gs
 * 2018/3/16
 */
public class UserOrganizationTest {
    @Autowired
    private UserOrganizationService service;
    @Autowired
    private OrganizationService organizationService;
    
    HttpClient3 http = new HttpClient3();
    
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //新增
    @Test
    public void insertTest() {
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/role/userOrganizationInsert?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "orgId=[{\"orgId\":\"cced74c59a9846b5b0a81c0baf235c17\"}]&uId=404ed3a5442c4ed78331d6c77077958f");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*//Map<Object, Object> map = new HashMap<>();
        //String codeStr = "[{\"code\":\"0101\"},{\"code\":\"0102\"},{\"code\":\"010101\"},{\"code\":\"010102\"}]";
        //String orgIdStr = "[{\"orgId\":\"cced74c59a9846b5b0a81c0baf235c17\"},{\"orgId\":\"e71064dc0fc443fa8893ce489aed8c38\"},{\"orgId\":\"22ee8c554c2a4f5383038c3f11ae02ed\"},{\"orgId\":\"96a2cd07161b42699c9028ae935a2033\"},{\"orgId\":\"2c39d78cc4a044509bb0ad37cc79a43e\"},{\"orgId\":\"e03f9ccb52184af3adc9681b6fc6067b\"},{\"orgId\":\"edaa035e69b84a2394e94684867f782c\"},{\"orgId\":\"016f4ab3da324d06aca8aa8068d7dda1\"},{\"orgId\":\"21dd76dc119248eca574659d2f213524\"},{\"orgId\":\"313a14372fa044c6bebaa1add330e423\"},{\"orgId\":\"36f7cb0d4f0f4cf3a3308b506f71d032\"},{\"orgId\":\"f925b112c09844b9bb12a0a0aed4ca74\"},{\"orgId\":\"b934d530f6764d2f82972d218669989c\"},{\"orgId\":\"4bde49515e7e479f9d831d92ca848703\"},{\"orgId\":\"1353f7c56c3d4e6e9a3a7e77625039f9\"},{\"orgId\":\"3aaab550a4864961847e6569b9b05e05\"},{\"orgId\":\"531eb41fca2a405bad57779b7ae5c425\"},{\"orgId\":\"e31e79a4dd19432cb07654394829450a\"},{\"orgId\":\"f070fc62cf1d4b34896bc3ac95fe04d9\"},{\"orgId\":\"5b5e434f5db14516b7cb26d47eb873a7\"},{\"orgId\":\"2c95cceaee084092b3112f99972fab4d\"},{\"orgId\":\"4f0b63b25ba14000878a8edb94499cfb\"},{\"orgId\":\"819a5a9703df4e63a1f1852b2cac0d75\"},{\"orgId\":\"8300e4f044ac4f43923e29166b0687f0\"},{\"orgId\":\"e0c29353f2214731824f946d8798846d\"},{\"orgId\":\"bab6018885e749efb1684c7c90127353\"},{\"orgId\":\"1086074fe707462e89368202955b7c44\"},{\"orgId\":\"3e060cb5361d437ab3f12130240447a6\"},{\"orgId\":\"9e11ed9d4bff489b8a616c57c198a77b\"},{\"orgId\":\"db15647416a243649631fb37d898b12c\"},{\"orgId\":\"eb1636d505b642c5a7e95a7ee4fd9da7\"},{\"orgId\":\"ce7decdce3e24990a9bef34763afa7bd\"},{\"orgId\":\"13691fa3eb10484ca739aec088b6bb52\"},{\"orgId\":\"478dd662af6f4aa0a3e26042e966d7eb\"},{\"orgId\":\"4c77449ca2ae4ed6b127d4fc859e81ed\"},{\"orgId\":\"8e96e28bec6b4ebcab626f357f8209a6\"},{\"orgId\":\"eb3587f1af424aed96e7f5febc975276\"},{\"orgId\":\"fd8c1a59b1784f35b503f57e2ce69e57\"},{\"orgId\":\"03728e83b8dd4e7aa665cc6c10856cf2\"},{\"orgId\":\"878c06a5ef4849e087dca635f99bbe00\"},{\"orgId\":\"8a02e75a25f345808df9ab5dd5c0da54\"},{\"orgId\":\"91f4fc70a0a44567a6d633f64d0334f0\"},{\"orgId\":\"af84c52f1413425ab26f723b10f45403\"},{\"orgId\":\"ffc40f3a5a0b4dc082d979892b3b35cd\"},{\"orgId\":\"ff98d81a143949b4a8bce555a0b6b65b\"},{\"orgId\":\"4e6ed42c9af94d77ad2e843413e17757\"},{\"orgId\":\"00a0b00003f7457ab1d52bf085f8ce14\"},{\"orgId\":\"1c4a8128d351414e9ad36f9ce5393b12\"},{\"orgId\":\"29c57609c2344b07a560621785e13b21\"},{\"orgId\":\"5d28eb1f3501465fb4acb6ea4068e682\"},{\"orgId\":\"c34310260afe4034931809803fa40c60\"},{\"orgId\":\"f3efffad7e36405a813ba9d550e5e29e\"},{\"orgId\":\"a7f1f0c620b24e05955cb0c7dc2d6a60\"},{\"orgId\":\"b06b982fa273491b8eec0c4ef5351702\"},{\"orgId\":\"039bca93a9534d99876db9343d52a06f\"},{\"orgId\":\"0ec59a92745f4a05a7337b2e4eb2180b\"},{\"orgId\":\"493836355884471ab0eef6d384ec48a4\"},{\"orgId\":\"4ecb3946e8b3448d8326605067f3c35e\"},{\"orgId\":\"7d6c2fd9e10e4715874f351e9a0a1b26\"},{\"orgId\":\"3be4774252e54e38870483b2906c77b8\"},{\"orgId\":\"5b743604fd6a4300867c05b36f310c93\"},{\"orgId\":\"e5d78de7c6ee4769b8b96841d6fbb239\"},{\"orgId\":\"1c73c41a7a7b442b901a0330e40bd7d6\"},{\"orgId\":\"46a5f8cdc94a43aa866fd627ac50ce03\"},{\"orgId\":\"4442e356fba94704a4fbf040cf9465a9\"},{\"orgId\":\"19d90fcbb05b49ef80f3a979c31c0230\"},{\"orgId\":\"d363c21ff85b454f84db3390d83ab217\"},{\"orgId\":\"1e0ba1612ff349019e33a197622dd8c1\"},{\"orgId\":\"292ef9fc66c94be9b0c323aad514197b\"},{\"orgId\":\"21e10ead202d469f96c11c649217ca1c\"},{\"orgId\":\"39f88d3dd05a49fb916a9aac43c7175f\"},{\"orgId\":\"2c17d102735d4473a061b5308028b407\"},{\"orgId\":\"ff012d2bf2f74d6dac190f08b72709da\"},{\"orgId\":\"4d692c60743a447c95f7abbfeb888a9e\"},{\"orgId\":\"6f1f18558ba948c09f08bd063613283a\"},{\"orgId\":\"d51210f487f840a5a1387bd5321b4a08\"},{\"orgId\":\"37dee02571284bbd89c4ae3e92e0547b\"},{\"orgId\":\"7392593f61f844659cc2b8d1a2ef8133\"},{\"orgId\":\"0c8e3e3ca0ec44378756f1d1dec8a30f\"},{\"orgId\":\"6e16ca5b549d495cb09fa77b5da7d648\"},{\"orgId\":\"088053224bfe404ab97b45efba15d6c0\"},{\"orgId\":\"fbf9a1e58dad48e3a47be5a9d2445c1f\"},{\"orgId\":\"a0982aed7639460d844bc446c93712f4\"},{\"orgId\":\"114aec7421b640809cf3d5b5758b7400\"},{\"orgId\":\"7322b9f60cc34d829e7f1308d385e64b\"},{\"orgId\":\"04668940dfa947cd8a1d95e32e503fc8\"},{\"orgId\":\"a8aea093058b4e9894f8f7330cf6ff77\"},{\"orgId\":\"a968b23cb8984bbd84c63b064d9e7c13\"},{\"orgId\":\"e0f99c0c5bc8477799110b00f81eb483\"},{\"orgId\":\"d1601173c36f4e318d5ecf325fc725fb\"},{\"orgId\":\"d2d7ddb28f194cd4b60cdb79373e6b05\"},{\"orgId\":\"0c6170f81aa4426494a02a2d62671452\"},{\"orgId\":\"25afd11c196a4c46a84d72d360dfe6c2\"},{\"orgId\":\"956e2cbd085c4618b123f8f188d84f04\"},{\"orgId\":\"de4dfb19ecda4af8a291511f36fc00c6\"},{\"orgId\":\"8e1ba712e705492fa256d37f3c2b07a0\"},{\"orgId\":\"0d25e4317898409bb35a6e29c864e6fd\"},{\"orgId\":\"9bb986cf360540b8bba0df8671033051\"},{\"orgId\":\"f946a081eda949228537a8746200c3d6\"},{\"orgId\":\"303d8eae5e424f69a3806b64942836e6\"},{\"orgId\":\"2afedf1a3c1f459d940059bb91bd7556\"},{\"orgId\":\"8baae719135d4da9a8bdc2a4a6c84d5a\"},{\"orgId\":\"1befa8a7ec8e47b0bf5a2af3322906a1\"},{\"orgId\":\"f8483e1c85e84323853aeee27b4e8c91\"},{\"orgId\":\"49c207c40797490eb1e6a8f45b63175f\"},{\"orgId\":\"add3c54094704e7da1cda947bbfd771a\"},{\"orgId\":\"758e39ccf7a94c31a698e12342421e36\"},{\"orgId\":\"e57a4fbad7b74c218f01f571b6f765a8\"},{\"orgId\":\"e5c832f988984528a4440f823df0bf58\"},{\"orgId\":\"634c24cf93c64665aca409b54bfb3f6e\"},{\"orgId\":\"2ad6b32752e24a929742d4e71648dc88\"},{\"orgId\":\"47a78c1f686a4fa5bd064ec837147fec\"},{\"orgId\":\"bbafedb88413495bb50f6f6b52aa97d4\"},{\"orgId\":\"a82279806239498ba717e1818aaabaa8\"},{\"orgId\":\"99a900fa36ff489185cf662ae949937b\"},{\"orgId\":\"e335e57f23e840788c4c88038006b1af\"},{\"orgId\":\"4e77c8778fe44cc88e8480755c89021c\"},{\"orgId\":\"cdaa72678c954e128a9b1cd7d57ad87a\"},{\"orgId\":\"2561ecc3fdd04ca188242b391b023ce0\"},{\"orgId\":\"9bc9e57d7e4e481c944d2539a0d0b7a2\"},{\"orgId\":\"90e59ddec3eb46339e918d2d20b3dd6c\"},{\"orgId\":\"f19a40b11dcb46778d27f40774683be5\"},{\"orgId\":\"da11a0fced594ee6afa51d0b1415791e\"},{\"orgId\":\"a4378c2ec7354453b54c2e5eb2847acf\"},{\"orgId\":\"17dfc21b54494c07a160e1cadd742063\"},{\"orgId\":\"63c1b9e8e5f74e63b912fd45a8563760\"},{\"orgId\":\"dc55b6b4c66f4471ac597f0608893e25\"},{\"orgId\":\"63a919e5b3ea4bf7815f1ee12479ca52\"},{\"orgId\":\"b38ef79c1dfc4a5fb6ee472962401693\"},{\"orgId\":\"ebd63278c548480a871589308becf9eb\"},{\"orgId\":\"6f1f5d048c8d47c68f78a0fc69a5d1e4\"},{\"orgId\":\"6a3f9ea134374f299a30d2c5ac289c44\"},{\"orgId\":\"fef092ad443546aca122c0616f069089\"},{\"orgId\":\"cff5d41750974a8e93b72803a2b9d5a5\"},{\"orgId\":\"fd1e30d84cea4c07af2fd9ec6e68a4b6\"},{\"orgId\":\"cc58fafcbfdf47a38884111a036e7147\"},{\"orgId\":\"d4f82275891e45c0b32bd2d4182856e2\"},{\"orgId\":\"7913a04ddbf84116a2a5d2ca29178469\"},{\"orgId\":\"d557dbf2a21f494b965c7ff33b77185a\"},{\"orgId\":\"f627078472574953988febef3b167ca5\"},{\"orgId\":\"fa770c62c60e4abea4b1176ba47a9cdf\"},{\"orgId\":\"78567576c9cd46028bd9eb78dd8e6310\"},{\"orgId\":\"eda5abfa51e743808dda1fb112ddfaa8\"},{\"orgId\":\"0ad72cfb850b4063a992bc2b0909c05a\"},{\"orgId\":\"d51b6e1fb11144a9b9a8ba4f5ca36fc4\"},{\"orgId\":\"80e28611c1a84b78afb57721734ca0cb\"},{\"orgId\":\"b8acccb91fb84147b5144f7a503a5efb\"},{\"orgId\":\"42e7e225061844518c4a6705ea8e90bd\"},{\"orgId\":\"d068f5466da04a65a0aa2dac575d39f4\"},{\"orgId\":\"f30c496cf5ac462aab0954c818e60ff2\"},{\"orgId\":\"8216e65c544f42cfa157c2380360c270\"},{\"orgId\":\"e63f71d83537425f959a7a4318db2a60\"},{\"orgId\":\"e57f2dd42d254dabaa9bd03a91ad38b5\"},{\"orgId\":\"9506e4e787f642cfb40120f3643e0d37\"},{\"orgId\":\"5b5f6bb033c247c7b8faa46a325704a9\"},{\"orgId\":\"28c9b1924e9b4112896771c99d6635a5\"},{\"orgId\":\"b0e3f17ea6a14cc0bc64901a8b4be9ff\"},{\"orgId\":\"9b5aa5b8667e4922b20bbf65b1296ae1\"},{\"orgId\":\"e831ef9fb3af42aeb0eaf92ab592ba85\"},{\"orgId\":\"da9de9b795cc470b91251e199f64f750\"},{\"orgId\":\"6183d281d18248cf8a6ddcbb78066d01\"},{\"orgId\":\"3c2287dbe960483ebcfd0303dc7e5172\"},{\"orgId\":\"92aaf16c788f4797a0512e155c15f83c\"},{\"orgId\":\"0303e625211c47ee86efb8007053186b\"},{\"orgId\":\"9c9ec2104aff4de185b1930a660d3098\"},{\"orgId\":\"44ded634502140b194c3770bf27b8a5d\"},{\"orgId\":\"09bae04ba0a0491fa6bbfae71197bab8\"},{\"orgId\":\"76fb579f1496407c88b286b629a2f71a\"},{\"orgId\":\"9555ad0a824844918fde6de2650b917a\"},{\"orgId\":\"3542109f99e2492e881d82d61ea4cdab\"},{\"orgId\":\"590b6db9b5624e73ac07303386c8689d\"},{\"orgId\":\"856fb4f378254e1dabe379d8a930fd97\"},{\"orgId\":\"cceb6da40c7449359a9b0ecdc3dd7848\"},{\"orgId\":\"a2e95b1905d441b58c4508c5fef3e429\"},{\"orgId\":\"bd4001208ae248bbb95ec4d0d4a70a0a\"},{\"orgId\":\"d89b71e7c9bb492f954ff60ed3c3f53c\"},{\"orgId\":\"cb4ed25b13a0417fa30a4db67fb5d6d8\"},{\"orgId\":\"10c0ee4147f84e3d9aa045065fc67620\"},{\"orgId\":\"0b91f9e34d784ba49f48eafea2eba761\"},{\"orgId\":\"da006f643bfe42889b0d266242442d27\"},{\"orgId\":\"e368393b5cf24643be4977f556472f60\"},{\"orgId\":\"1bb56148c4f54e7eb3fb9a57b0e788dd\"},{\"orgId\":\"57682961777a4890bed992369ec02d10\"},{\"orgId\":\"7036f5db671c48ef8d74968b29375952\"},{\"orgId\":\"879b0e02c007483d81cb90f5219d2788\"},{\"orgId\":\"3857a59f5d144a708d90605f09610073\"},{\"orgId\":\"572b920a64364c0cb9d9259b9668e8dc\"},{\"orgId\":\"d0f956cb5f5546dca91509ccde2a6e74\"},{\"orgId\":\"2a38648b34134859a018d080ae720573\"},{\"orgId\":\"3567311b574d4cbd87297eedcb3c07f2\"},{\"orgId\":\"6bd2a260ddc04319a188783bdbc52d2d\"},{\"orgId\":\"9416bfb8dfd7400c8b521ab7cd97cfca\"},{\"orgId\":\"afcfa757ff7a46fbbfc5f60d1ce51b18\"},{\"orgId\":\"7916b254cb0b4309ba1422e63bab4934\"},{\"orgId\":\"f48d55ec27ec41c99c7be64ead65a9ef\"},{\"orgId\":\"f61298513e0d484db319f8cae8c485f4\"},{\"orgId\":\"0d1874ebc5384a46aa3708ad816e90af\"},{\"orgId\":\"fdc05069255c40f0aa38ba8f943ec04f\"},{\"orgId\":\"dde019565c1a4d9ba6f56e799cfcdfee\"},{\"orgId\":\"6c46000ac7854f17b5bac31cc0dc76b4\"},{\"orgId\":\"11f141db4ea14b8884e839fc54bfbff3\"},{\"orgId\":\"706ae766e92d46e3bc81e641d612b7d8\"},{\"orgId\":\"f1a0f7ad058b47caaac46c1b3c2d9dca\"},{\"orgId\":\"1fd97e60c4a44ba3abdcfc49fa91b824\"},{\"orgId\":\"8a69325eb5914f23a45d6593e112e3f0\"},{\"orgId\":\"5b5681d4844742c6a444fef8922b11e5\"},{\"orgId\":\"6d070064cc42469ea1583cef9a45ce0d\"},{\"orgId\":\"8bfe6776929545d4ba5ad9293ef57c00\"},{\"orgId\":\"8d65e06cfb0e4852a98409922317180f\"},{\"orgId\":\"1cee63f5d2fb4f0a8e8f233e08e49dc2\"},{\"orgId\":\"434a44afb2d34ac1be4128284ce0175d\"},{\"orgId\":\"47aff414a8f84d74a45317e6f77746a7\"},{\"orgId\":\"5558cafc71e342e0a7d1c8d3072308bd\"},{\"orgId\":\"6579e2eb481940dab642682ec812e0c8\"},{\"orgId\":\"8b862fd1104d474c82b18ceabcfd0a30\"},{\"orgId\":\"bef37da9e4ed4fa898b477cdd4307aa1\"},{\"orgId\":\"e3f8f4e83522446d8fca10018e7ba0a1\"},{\"orgId\":\"bb029c9478d34ddaa23736c24fa0a262\"},{\"orgId\":\"a22f38453a5c41a3872413a81655d7d4\"},{\"orgId\":\"f4710f76469940b1bdc2e76eead3b6e1\"},{\"orgId\":\"e964f5060b164bcfb6084e225ac0597a\"},{\"orgId\":\"a6a1e9f39ff44d83aa0555e43ea84844\"},{\"orgId\":\"fcd318919a0248f18ade1bd6d750810a\"},{\"orgId\":\"7aca048645d947119aadaa425ac5caee\"},{\"orgId\":\"57fdfd91204b4ffe93046d7af946e0f9\"},{\"orgId\":\"a3256f0fde8b47b79cfa7fe15f55c5b0\"},{\"orgId\":\"85aaff4835b44aa8b3b21e72c567fcf5\"},{\"orgId\":\"ca5198e6779c4ee59249e2af498a2e86\"},{\"orgId\":\"f780fd36e92a4af8ac088dd8bef843a4\"},{\"orgId\":\"b4bebdf24eac4912b5df7932260c90d6\"},{\"orgId\":\"febf50d33b784821a2d19902dc5bbc38\"},{\"orgId\":\"b0d108e8f394474fad8c16e1833a08f0\"},{\"orgId\":\"372a3a8385b14b198da4961bcfe5e955\"},{\"orgId\":\"05e5d7213b274e86b48fea35e3ad874c\"},{\"orgId\":\"1acf769879084495b2197fb12e6acaca\"},{\"orgId\":\"4c3579ceb8b843f9a082741b1fe07661\"},{\"orgId\":\"f2003fcbfb3c437d9a1a67c3445c8c5f\"},{\"orgId\":\"6c5680c7d68a4bb6b3bbcd15c3b819da\"},{\"orgId\":\"bf60b944376649aea47be2ce5adc4336\"},{\"orgId\":\"d1ecff605d5b47df96878af617afe381\"},{\"orgId\":\"605ea3e587a04222983e016196d5556b\"},{\"orgId\":\"b199ac5d5b874a2cb4512b353e8c3930\"},{\"orgId\":\"d3ded16c6e9442f69745dfa184b9e5f3\"},{\"orgId\":\"603f3e39305443ca97de1f6b8819946b\"},{\"orgId\":\"85754e59f1e343cb881d2ea1f6af0182\"},{\"orgId\":\"2618d5a5e39a484d8ae3f7f73a5fc748\"},{\"orgId\":\"9b05f7076a5a41059d0bf414e33e8558\"},{\"orgId\":\"e87adf5fe9c34a628ffa832936ee5a47\"},{\"orgId\":\"4b3e7d1525cb47398852cdc4fbf88f75\"},{\"orgId\":\"6615589e4b00484481610e6d9acdc1f4\"},{\"orgId\":\"691b42fb0b3b44a986c98aaad47f4d45\"},{\"orgId\":\"b7693cad9e0c410a85a2827bc203dc0c\"},{\"orgId\":\"399ab9d8268e44a1b213b6add4fd933f\"},{\"orgId\":\"7d3233285e1847e893ec1874c0113c52\"},{\"orgId\":\"b8183034ebb94cb18478af0feb33ea2c\"},{\"orgId\":\"309238b4b99a4459b549a8f947383ca6\"},{\"orgId\":\"862c3d44410940068134b947aae440d3\"},{\"orgId\":\"32aa9084b85740869082488fe08431fc\"},{\"orgId\":\"3d5eca8e63914300900bf05262172025\"},{\"orgId\":\"68df67b1e34f4da483d9cd7c469f5f46\"},{\"orgId\":\"95a1ee04a7014f62b219de54213e5f9b\"},{\"orgId\":\"aa51be1c906d4033b5446bbb249d6b57\"},{\"orgId\":\"b57325bf85274fdcba141dc970bfcdb3\"},{\"orgId\":\"9e69663a296f4b879a370340652e3db3\"},{\"orgId\":\"adb5e799d8584471a2f0a1842ea2addf\"},{\"orgId\":\"db46fdff84b54eb2b9fe87c9e89ed2cf\"},{\"orgId\":\"b24bf9d6d57e4eddb59b32f82e44c34b\"},{\"orgId\":\"46ced48a884045d0af42335952a42bc7\"},{\"orgId\":\"499bcfc565864d21b77897387d09a183\"},{\"orgId\":\"035b820ea1da43a78de32045424b58f2\"},{\"orgId\":\"047f1d714e254b419e86de708a43e175\"},{\"orgId\":\"6676b0b3d9274d39ae54e271c4c5820e\"},{\"orgId\":\"cfa9a81b3cf14bafb5e86577500bc9bd\"},{\"orgId\":\"2a35c60757ef4c3c97ccf06c0a7a602c\"},{\"orgId\":\"6b95aae4a73b4082986943f5c2dfa1e7\"},{\"orgId\":\"525b617dbc394ed68457169873915874\"},{\"orgId\":\"71dc9e3fca4a4f8a876e526fb2ee35e0\"}]";
        //String orgIdStr = "[{\"orgId\":\"00bd51930b7e4550a8d78823917f2972\"},{\"orgId\":\"fef092ad443546aca122c0616f069089\"}]";
        String orgIdStr = "[{\"orgId\":\"00a1f4a699c945638e4c6114e9a8448d\"},{\"orgId\":\"00bd51930b7e4550a8d78823917f2972\"},{\"orgId\":\"016f4ab3da324d06aca8aa8068d7dda1\"},{\"orgId\":\"017c2e23ca6645698d4aebda254e280e\"}]";

        JSONArray sArray = JSON.parseArray(orgIdStr);
        UserOrganization userOrganization = null;
        int number=0;
        for (int i = 0; i < sArray.size(); i++) {
            JSONObject object = (JSONObject) sArray.get(i);
            String orgId =(String)object.get("orgId");
            //map.put("code", code);// 根据组织架构code查询id
            //List<Organization> list = organizationService.listOrganizationBy(map);
            //if(list.size()>0){
                //for(Organization orgList:list){
                    userOrganization = new UserOrganization();
                    userOrganization.setId(UuidUtil.getUUID());
                    userOrganization.setoId(orgId);
                    System.out.println("organizationId:==="+orgId);
                    userOrganization.setuId("b7632238905a48a0b221264b4087ebf8");
                    number = service.insertUserOrganization(userOrganization);
                //}
            //}
        }
        
        try {
            System.out.println(number);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }
    //根据用户id查询
    @Test
    public void ListUserOrganizationTest() {
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/role/userOrganizationIndex?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "uId=404ed3a5442c4ed78331d6c77077958f");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*Map<Object, Object> map = new HashMap<Object, Object>();
        List<Organization> listre = new ArrayList<>();
        List<UserOrganization> orgList = service.listUserOrganization("8a02320b8b1c4a999235df9b4f5ccbf3");
        List<TreeNode<Organization>> nodes = new ArrayList<>();
        for (int i = 0; i < orgList.size(); i++) {
            String orgId = orgList.get(i).getoId();
            //当前id信息
            map.put("id", orgId);
            List<Organization> userOrgId = organizationService.listOrganizationBy(null, null, null, orgId, null, null, null, null);
            listre.addAll(userOrgId);
            //根据当前id查询该节点的所有父节点
            List<Organization> orgIdList = organizationService.listTreeByIdForParent(orgId);
            listre.addAll(orgIdList);
        }
        
        if(!CollectionUtils.isEmpty(listre)){
            for (int i = 0; i < listre.size() - 1; i++) {
                for (int j = listre.size() - 1; j > i; j--) {
                    if (listre.get(j).getId().equals(listre.get(i).getId())) {
                        listre.remove(j);
                    }
                }
            } 
            for (Organization organization : listre) {
                TreeNode<Organization> node = new TreeNode<>();
                node.setId(organization.getCode());
                node.setParentId(organization.getParentId());
                node.setName(organization.getOrgName());
                node.setPid(organization.getId());
                nodes.add(node);
            }
        }
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(TreeNode.buildTree(nodes));
        System.out.println(jsonObject);
        System.out.println("总条数："+orgList.size());
        for(UserOrganization list:orgList){
            System.out.println(" oId: "+list.getoId() +" uId: "+list.getuId() +" jobNumber: "+list.getJobNumber()+
                    " userName: "+list.getName()+" realName: "+list.getRealName()+" code: "+list.getCode()+
                    " ParentId: "+list.getParentId()+" His_permission: "+list.getHis_permission()+
                    " orgName: "+list.getOrgName()+" createTime: "+list.getCreateTime() +"\n");
        }*/
        
    }
    
    //修改
    @Test
    public void updateUserOrganizationTest(){
        try {
            String url = http.doPost("http://192.168.113.135:8080/financialSys/role/userOrganizationUpdate?meta.session.id=a92875db-1d3a-42cb-adb7-0952e74dcf37", "orgId=[{\"orgId\":\"cced74c59a9846b5b0a81c0baf235c17\"}]&uId=404ed3a5442c4ed78331d6c77077958f");
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*UserOrganization userOrganization = null;
        int deleteNumber = service.deleteUserOrganization("404ed3a5442c4ed78331d6c77077958f");
        if(deleteNumber>0){
            String orgIdStr = "[{\"orgId\":\"cced74c59a9846b5b0a81c0baf235c17\"}]";
            JSONArray sArray = JSON.parseArray(orgIdStr);
            int number=0;
            for (int i = 0; i < sArray.size(); i++) {
                JSONObject object = (JSONObject) sArray.get(i);
                String orgId =(String)object.get("orgId");
                //map.put("code", code);// 根据组织架构code查询id
                //List<Organization> list = organizationService.listOrganizationBy(map);
                //if(list.size()>0){
                    //for(Organization orgList:list){
                        userOrganization = new UserOrganization();
                        userOrganization.setuId("404ed3a5442c4ed78331d6c77077958f");
                        //userOrganization.setId("1beb41326553418f9fc9b45d037a0925");
                        userOrganization.setId(UuidUtil.getUUID());
                        userOrganization.setoId(orgId);
                        System.out.println("organizationId:==="+orgId);
                        number = service.updateUserOrganization(userOrganization);
                    //}
                //}
            }
            
            try {
                System.out.println("修改成功："+number);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }else{
            System.out.println("删除失败");
        }*/
        
    }
    
}
