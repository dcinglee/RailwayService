package com.railwayservice.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import com.railwayservice.application.util.ExcelExportUtil;
//import com.railwayservice.application.util.Arrayist;
//import com.railwayservice.application.util.book;
//import com.railwayservice.application.util.student;
import com.railwayservice.serviceprovider.entity.ServiceType;
import com.railwayservice.serviceprovider.service.ServiceTypeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestExcelExportUtil {

	private Logger logger = LoggerFactory.getLogger(TestExcelExportUtil.class);

	@Autowired
    private ServiceTypeService serviceTypeService;

    public ServiceTypeService getServiceTypeService() {
		return serviceTypeService;
	}

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}


    @Test
    public void exportTest() {
    	
    	List<ServiceType> services = serviceTypeService.findAllServiceType("desc");
    	// 测试学生   
    	ExcelExportUtil <ServiceType> ex = new ExcelExportUtil <ServiceType>();  
        String[] headers =  
        { "学号", "姓名", "年龄" };  
        String[] attr={"introduction","name","distributionCosts"};
//        List<ServiceType> dataset = new ArrayList<ServiceType>();  
//        dataset.add(new student(10000001, "张三", 20, true, new Date()));  
//        dataset.add(new student(20000002, "李四", 24, false, new Date()));  
//        dataset.add(new student(30000003, "王五", 22, true, new Date()));  
        // 测试图书   
//        ExcelExportUtil <book> ex2 = new ExcelExportUtil <book>();  
        String[] headers2 =  
        { "图书编号", "图书名称", "图书作者" };  
        
        int[] widths = {200,600,300};
//        List<book> dataset2 = new arraylist<book>();  
        try  
        {  
//            BufferedInputStream bis = new BufferedInputStream(  
//                    new FileInputStream("v://book.bmp"));  
//            byte[] buf = new byte[bis.available()];  
//            while ((bis.read(buf)) != -1)  
//            {  
//                //   
//            }  
//            dataset2.add(new book(1, "js p", "leno", 300.33f, "1234567",  
//                    "清华出版社", buf));  
//            dataset2.add(new book(2, "java编程思想", "brucl", 300.33f, "1234567",  
//                    "阳光出版社", buf));  
//            dataset2.add(new book(3, "dom艺术", "lenotang", 300.33f, "1234567",  
//                    "清华出版社", buf));  
//            dataset2.add(new book(4, "c++经典", "leno", 400.33f, "1234567",  
//                    "清华出版社", buf));  
//            dataset2.add(new book(5, "c#入门", "leno", 300.33f, "1234567",  
//                    "汤春秀出版社", buf));  
  
            OutputStream out = new FileOutputStream("e://a.xls");  
//            OutputStream out2 = new FileOutputStream("e://b.xls");  
            ex.exportExcel (headers, attr,widths,services, out);  
//            ex2.exportExcel (headers2, dataset2, out2);  
            out.close();  
//            JOptionPane.showMessageDialog(null, "导出成功!");  
            System.out.println("excel 导出成功！");  
        }  
        catch (FileNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    }

    
}
