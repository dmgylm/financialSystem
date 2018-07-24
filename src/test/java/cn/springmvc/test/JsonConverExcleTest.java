package cn.springmvc.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.asm.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.financial.model.BusinessData;
import cn.financial.model.BusinessDataInfo;
import cn.financial.model.DataModule;
import cn.financial.service.BusinessDataInfoService;
import cn.financial.service.BusinessDataService;
import cn.financial.service.DataModuleService;
import cn.financial.service.OrganizationService;
import cn.financial.service.impl.BusinessDataInfoServiceImpl;
import cn.financial.service.impl.BusinessDataServiceImpl;
import cn.financial.service.impl.DataModuleServiceImpl;
import cn.financial.util.FormulaUtil;
import cn.financial.util.HtmlGenerate;
import cn.financial.util.JsonConvertProcess;
/*import net.sf.json.JSONArray;
import net.sf.json.JSONObject;*/
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-mybatis.xml", "classpath:spring/mybatis-config.xml", "classpath:spring/spring-cache.xml",
        "classpath:spring/spring-shiro.xml","classpath:spring/spring-redis.xml"})
public class JsonConverExcleTest {
	@Autowired
	private DataModuleService dataModuleService;
	@Autowired
	private BusinessDataService businessDataService;
	@Autowired
	private BusinessDataInfoService businessDataInfoService;
	@Autowired
	private OrganizationService organizationService;
	private static Map<Integer, Map<Integer, JSONObject>> rowMap = new HashMap<Integer, Map<Integer,JSONObject>>();

	/*public static void main(String[] args) {
        parseJsonToExcel("C:/Users/admin/Desktop/jsonToExcel-demo.xls", "data", mavenInfos);
    }*/

/*	 
    static void parseJsonToExcel(String saveFileName, String rootNodeName, String sourceJson){
        try{
            File filewrite=new File(saveFileName);
            filewrite.createNewFile();
            OutputStream os = new FileOutputStream(filewrite);
            JSONObject jsonObject = JSONObject.fromObject(sourceJson);
            createExcel(os, jsonObject, rootNodeName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
 
    public static void createExcel(OutputStream os, JSONObject apiJarInfos, String rootNodeName) throws WriteException,IOException {
        //创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(os);
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("First Sheet",0);
        JSONArray jsonArray = (JSONArray)apiJarInfos.get(rootNodeName);
        JSONObject jsonObjectHeader = jsonArray.getJSONObject(0);
        String[] headers = createTableHeader(jsonObjectHeader, sheet);
        int size =   jsonArray.size()+1;
        for(int i=1; i<size; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i-1);
            int j = 0;
            for (String key : headers){
                Label cellValue = new Label(j, i, jsonObject.get(key).toString());
                sheet.addCell(cellValue);
                j++;
            }
        }
        //把创建的内容写入到输出流中，并关闭输出流
        workbook.write();
        workbook.close();
        os.close();
    }
 
    static String[] createTableHeader(JSONObject jsonObjectHeader, WritableSheet sheet) throws WriteException {
        //遍历JSONObject中的key
        Iterator iterable = jsonObjectHeader.keys();
        String[] headers = new String[jsonObjectHeader.size()];
        int i = 0;
        while (iterable.hasNext()){
            //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
            String headerName = iterable.next().toString();
            Label cell = new Label(i, 0, headerName);
            sheet.addCell(cell);
            headers[i] = headerName;
            i++;
        }
        return headers;
    }
    private static final String mavenInfos ="{  \n" +
            "  \"data\": [\n" +
            "        {\n" +
            "            \"groupId\": \"com.test.demo\",\n" +
            "            \"artifactId\": \"demo-api\",\n" +
            "            \"version\": \"1.0.0-release\",\n" +
            "            \"latestSnapshot\": \"2.0.0-SNAPSHOT\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"groupId\": \"com.test.demo.core\",\n" +
            "            \"artifactId\": \"demo-core\",\n" +
            "            \"version\": \"1.1.3\",\n" +
            "            \"latestSnapshot\": \"1.3.0-SNAPSHOT\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";*/
@Test
public void dy() {
	/*	String id="c1afe7ef4e9e41cd94785f4067f5bcf4";
	  BusinessData  businessData=businessDataService.selectBusinessDataById(id);
      DataModule dm=dataModuleService.getDataModule(businessData.getDataModuleId());
      BusinessDataInfo busInfo=businessDataInfoService.selectBusinessDataById(id);
      JSONObject joTemp=JSONObject.parseObject(dm.getModuleData());
      JSONObject joInfo=JSONObject.parseObject(busInfo.getInfo());
      JsonConvertProcess.mergeJson(joTemp, joInfo);
      //HtmlGenerate htmlGenerate=new HtmlGenerate();
      String html=JsonConvertProcess.mergeJson(joTemp, joInfo).toString();
      System.out.println(html);*/
  	  String paths="C:/Users/admin/Desktop/htmlTest.txt";
  	  JSONObject json=JSONObject.parseObject(JsonConvertProcess.readFileContent(paths));
  	  JsonConversionExcel(json,"表格");
}
    public void JsonConversionExcel(JSONObject jsonObj,String sheetName) {
    	XSSFWorkbook wb = new XSSFWorkbook();//创建一个workbook
		XSSFSheet sheet = wb.createSheet("报表");//创建一个sheet
		XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);//// 创建一个居中格式  
        Label label; // 单元格对象
        int column = 0; // 列数计数
    	
    }


    @Test
    public void con( ) throws IOException {
    	String paths="C:/Users/admin/Desktop/test.txt";
    	  JSONObject json=JSONObject.parseObject(JsonConvertProcess.readFileContent(paths));
    	  System.out.println(json);
    	Map<Integer,Map<Integer,JSONObject>> trMap = assembleData(json);
		trMap = sortTableRowMap(trMap);
		assembleTable(trMap);
    }
    
	private Map<Integer, Map<Integer, JSONObject>> assembleData(
			JSONObject array) {
		for(Iterator<String> iter = array.keySet().iterator();iter.hasNext();) {
			String key = iter.next();
			Object obj = array.get(key);
			if(obj instanceof JSONArray) {
				JSONArray ja = (JSONArray)obj;
				for(int i=0;i<ja.size();i++) {
					putRowToMap(rowMap,ja.getJSONObject(i));
				}
			} else if(obj instanceof JSONObject) {
				assembleData((JSONObject) obj);
			}
		}
		
		return rowMap;
	}
	
    
	/**
	 * 将该Map通过Key进行排序
	 * @param map
	 * @return
	 */
	private Map<Integer, Map<Integer, JSONObject>> sortTableRowMap(
			Map<Integer, Map<Integer, JSONObject>> map) {
		Map<Integer, Map<Integer, JSONObject>> sortMap = new TreeMap<Integer, Map<Integer, JSONObject>>(
				new MapKeyComparator());

		sortMap.putAll(map);
		return sortMap;
	}

    /**
	 * 
	 * @param trMap
	 * @param obj
	 */
	private void putRowToMap(Map<Integer, Map<Integer, JSONObject>> trMap,
			JSONObject obj) {
		if(!obj.containsKey("row")) {
			return;
		}
		Integer rowNum = obj.getInteger("row");
		Integer colNum = obj.getInteger("col");
		Map<Integer, JSONObject> row = trMap.get(rowNum);
		if(row==null) {
			row = new HashMap<Integer, JSONObject>();
			trMap.put(rowNum, row);
		}
		row.put(colNum, obj);
	}
	static class MapKeyComparator implements Comparator<Integer>{
	    public int compare(Integer value1, Integer value2) {
	        return value1-value2;
	    }
	}
	/**
	 * 组装Table
	 * @param trMap
	 * @param table
	 * @throws IOException 
	 */
	private void assembleTable(Map<Integer, Map<Integer, JSONObject>> trMap) throws IOException {
		Workbook wb = new XSSFWorkbook();
		 Sheet  sheet = wb.createSheet("sheet1");
		 XSSFCellStyle ztStyle = (XSSFCellStyle) wb.createCellStyle();   
		 
		 ztStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
		 ztStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
		 ztStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
		 ztStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		 ztStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中  
		 
         // 创建字体对象   
         Font ztFont = wb.createFont();   
         ztFont.setFontName("宋体");    
         ztFont.setFontHeightInPoints((short)10);    // 将字体大小设置为18px   
         ztStyle.setFont(ztFont);
         
		 Row  row1 ;
		 List<CellRangeAddress>lc=new ArrayList<CellRangeAddress>();
		for(Iterator<Integer> rowIter = trMap.keySet().iterator();rowIter.hasNext();) {
			Integer rowNum = rowIter.next();
			Map<Integer, JSONObject> row = trMap.get(rowNum);
			
			row1 = sheet.createRow(rowNum); 
			for(Iterator<Integer> colIter = row.keySet().iterator();colIter.hasNext();) {
				Integer colNum = colIter.next();
				JSONObject col = row.get(colNum);
				
				int firstRow=Integer.parseInt(col.get("row").toString());
				int lastRow=firstRow;
				int firstCol=Integer.parseInt(col.get("col").toString());
				int lastCol=firstCol;
				if(Integer.parseInt(col.get("rowspan").toString())!=1) {
					lastRow=firstRow+Integer.parseInt(col.get("rowspan").toString())-1;
				}
				if(Integer.parseInt(col.get("colspan").toString())!=1) {
					lastCol=firstCol+Integer.parseInt(col.get("colspan").toString())-1;
				}
				CellRangeAddress cellr =new CellRangeAddress(firstRow,lastRow,firstCol,lastCol);
				lc.add(cellr);
				    Cell cell = row1.createCell(colNum);
				    boolean display= (boolean) col.get("display");
				    if(col.get("name").equals("")&&col.containsKey("value")&&display==true) {
				    	if(Integer.parseInt(col.get("type").toString())==11||Integer.parseInt(col.get("type").toString())==12||Integer.parseInt(col.get("type").toString())==13||Integer.parseInt(col.get("type").toString())==14||Integer.parseInt(col.get("type").toString())==15) {
				    		cell.setCellValue(col.get("name").toString());
				    	}else {
				    		 cell.setCellValue(col.get("value").toString());
				    	}
				    }else {
				    	 cell.setCellValue(col.get("name").toString());
				    }
				    cell.setCellStyle(ztStyle);
				    if(Integer.parseInt(col.get("type").toString())==3) {
				    	 XSSFCellStyle ztStyles = (XSSFCellStyle) wb.createCellStyle();  
				    	 ztStyles.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框    
						 ztStyles.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框    
						 ztStyles.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
						 ztStyles.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
						 ztStyles.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中  
						 ztStyles.setFillPattern(CellStyle.SOLID_FOREGROUND); 
						 ztStyles.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex()); // 前景色;
						 ztStyles.setFont(ztFont);
						 cell.setCellStyle(ztStyle);               // 样式应用到该单元格上  
						 cell.setCellStyle(ztStyles);
				    	
				    }
			}
		}
		for (int i = 0; i < lc.size(); i++) {
			 sheet.addMergedRegion(lc.get(i));
			
		}
		FileOutputStream fileOut = new FileOutputStream("C:/Users/admin/Desktop/workbook.xlsx");
		//  sheet=getCell(trMap,sheet);
		wb.write(fileOut);
	    fileOut.close();
	}
}
