package com.railwayservice.application.util;

import java.io.IOException; 
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;  
import java.util.Collection;  
import java.util.Date;  
import java.util.Iterator;  

  
import org.apache.poi.hssf.usermodel.HSSFCell;  
import org.apache.poi.hssf.usermodel.HSSFCellStyle;  
import org.apache.poi.hssf.usermodel.HSSFPatriarch;  
import org.apache.poi.hssf.usermodel.HSSFRichTextString;  
import org.apache.poi.hssf.usermodel.HSSFRow;  
import org.apache.poi.hssf.usermodel.HSSFSheet;  
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  
  
/** 
 * 利用开源组件poi3.0.2动态导出excel 文档 转载时请保留以下信息，注明出处！ 
 *  
 * @author leno 
 * @version v1.0 
 * @param <t> 
 *            应用开发泛型，代表任意一个符合javabean风格的类 
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getxxx(),而不是isxxx() 
 *            byte[]表jpg格式的图片数据 
 */  
public class ExcelExportUtil <t>  
{  
  
    
    /** 
     * 这是一个通用的开发方法 ，利用了java的反射机制，可以将放置在java集合中并且符号一定条件的数据以excel  的形式输出到指定io设备上 
     *  
     * @param title 
     *            表格标题名 
     * @param titles 
     *            表格属性列名数组
     * @param values
     * 			     需要显示的属性            
     * @param dataset 
     *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此开发方法 支持的 
     *            javabean属性的数据类型有基本数据类型及string,Date,byte[](图片数据) 
     * @param out 
     *            与输出设备关联的流对象，可以将excel 文档导出到本地文件或者网络中 
     */  
    @SuppressWarnings("unchecked")  
    public void exportExcel ( String[] titles, String[] values, int[] cellWidths,
            Collection<t> dataset, OutputStream out)  
    {  
        // 声明一个工作薄   
        @SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();  
        // 生成一个表格   
        HSSFSheet sheet = workbook.createSheet();//createSheet(title);
        // 设置表格默认列宽度为15个字节   
        sheet.setDefaultColumnWidth((short) 150);  
        // 生成一个样式   
        HSSFCellStyle style = workbook.createCellStyle();  
        // 设置这些样式   
//        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);  
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
//        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
//        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
//        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
//        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
        // 生成一个字体   
//        HSSFFont font = workbook.createFont();  
//        font.setColor(HSSFColor.VIOLET.index);  
//        font.setFontHeightInPoints((short) 12);  
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        // 把字体应用开发到当前的样式   
//        style.setFont(font);  
        // 生成并设置另一个样式   
//        HSSFCellStyle style2 = workbook.createCellStyle();  
//        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);  
//        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
//        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
//        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
//        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);  
//        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);  
//        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
//        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
        // 生成另一个字体   
//        HSSFFont font2 = workbook.createFont();  
//        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);  
        // 把字体应用开发到当前的样式   
//        style2.setFont(font2);  
  
        HSSFCellStyle leftStyle = workbook.createCellStyle();
        leftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        
        HSSFCellStyle rightStyle = workbook.createCellStyle();
        rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        
        // 声明一个画图的顶级管理器   
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();  
        // 定义注释的大小和位置,详见文档   
//        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,  
//                0, 0, 0, (short) 4, 2, (short) 6, 5));  
        // 设置注释内容   
//        comment.setString(new HSSFRichTextString("可以在poi中添加注释！"));  
        // 设置注释作者，当鼠标移动开发其他 到单元格上是可以在

   
//        comment.setAuthor("xuyu");  
  
        // 产生表格标题行   
        HSSFRow row = sheet.createRow(0);  
        for (short i = 0; i < titles.length; i++)  
        {  
            HSSFCell cell = row.createCell(i);
//            cell.set
            cell.setCellStyle(style);  
            HSSFRichTextString text = new HSSFRichTextString(titles[i]);  
            cell.setCellValue(text);  
        }  
        
        //设置宽度
        for( int i = 0; cellWidths != null && i < cellWidths.length; i++ ){
        	sheet.setColumnWidth(i,  (int)(35.7 * cellWidths[i]));  
        }
  
        // 遍历集合数据，产生数据行   
        Iterator<t> it = dataset.iterator();  
        int index = 0;  
        
        
      	 
        while (it.hasNext())  
        {  
            index++;  
            row = sheet.createRow(index);  
            t t = (t) it.next();  
            // 利用反射，根据javabean属性的先后顺序，动态调用getxxx()开发方法 得到属性值   
            Field[] fields = t.getClass().getDeclaredFields();  
//            System.out.println("fields="+fields);
            
//            attrs
//            for (short i = 0; i < fields.length; i++)  
            	
            for (short i = 0; values != null && i < values.length; i++)	
            {
                HSSFCell cell = row.createCell(i); 
                Field Field = fields[i];  
                String fieldname = values[i];
                String getmethodname = "get"  
                        + fieldname.substring(0, 1).toUpperCase()  
                        + fieldname.substring(1);  
                try  
                {  
					Class tcls = t.getClass();
					Method getmethod = tcls.getMethod(getmethodname, new Class[] {});
					Object value = getmethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textvalue = null;
					if (value instanceof String) {
						HSSFRichTextString text = new HSSFRichTextString((String) value);
						cell.setCellStyle(leftStyle);
						cell.setCellValue(text);
					} else if (value instanceof BigDecimal) {
						BigDecimal intvalue = (BigDecimal) value;
						intvalue = intvalue.setScale(2, BigDecimal.ROUND_HALF_UP);
						cell.setCellStyle(rightStyle);
						cell.setCellValue(intvalue.toString());
					} else if (value instanceof Integer) {
						Integer Date = (Integer) value;
						cell.setCellStyle(rightStyle);
						cell.setCellValue(Date);
					} else if (value instanceof Boolean) {
						textvalue = "" + value;
						cell.setCellValue(Double.parseDouble(textvalue));
					} else if (value instanceof Date) {
						Date Date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
						textvalue = sdf.format(Date);
						HSSFRichTextString richstring = new HSSFRichTextString(textvalue);
						cell.setCellStyle(leftStyle);
						cell.setCellValue(richstring);
					} else {
						// 其它数据类型都当作字符串简单处理
						textvalue = value != null ? value.toString() : null;
						HSSFRichTextString richstring = new HSSFRichTextString(textvalue);
						cell.setCellStyle(leftStyle);
						cell.setCellValue(richstring);
					}
                    
                    // 如果不是图片数据，就利用正则表达式判断textvalue是否全部由数字组成   
//                    if (textvalue != null)  
//                    {  
//                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");  
//                        Matcher Matcher = p.matcher(textvalue);  
//                        if (Matcher.matches())  
//                        {  
//                            // 是数字当作double处理   
//                            cell.setCellValue(Double.parseDouble(textvalue));  
//                        }  
//                        else  
//                        {  
//                            HSSFRichTextString richstring = new HSSFRichTextString(  
//                                    textvalue);  
//                            cell.setCellValue(richstring);  
//                        }  
//                    }  
                }  
                catch (SecurityException e)  
                {  
                    e.printStackTrace();  
                }  
                catch (NoSuchMethodException e)  
                {  
                    e.printStackTrace();  
                }  
                catch (IllegalArgumentException e)  
                {  
                    e.printStackTrace();  
                }  
                catch (IllegalAccessException e)  
                {  
                    e.printStackTrace();  
                }  
                catch (InvocationTargetException e)  
                {  
                    e.printStackTrace();  
                }  
                finally  
                {  
                    // 清理资源  
                }  
            }  
        }  
        try  
        {  
            workbook.write(out);  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
    }  
  
}

