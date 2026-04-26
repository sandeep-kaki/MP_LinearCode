package Utilites;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtilites {
	public static void readExcel() {
		//create object
	    XSSFWorkbook ExcelWBook=null;
	    XSSFSheet ExcelWSheet;
	    //XSSFRow Row;
	    //XSSFCell Cell;
	
	    
	    //Read ExelFile
	    //file open
	    FileInputStream inputStream=null;
	try {
	File exFile=new File("C:\\Users\\2475070\\OneDrive - Cognizant\\Desktop\\Stage 2\\ProjectFile.xlsx");
	 inputStream=new FileInputStream(exFile);
	}catch(FileNotFoundException e){
		e.printStackTrace();
	}
	//readData Object
	try {
	ExcelWBook =new XSSFWorkbook(inputStream);
	}catch(IOException e) {
		//Handel file not found Exception
		e.printStackTrace();
	}
	ExcelWSheet=ExcelWBook.getSheetAt(0);
	
	int ttRows=ExcelWSheet.getLastRowNum()+1;
	int ttCell=ExcelWSheet.getRow(0).getLastCellNum();
	//System.out.println("Excel Data Read:");
	//use loop go to each row
	for(int currentRow=0;currentRow<ttRows;currentRow++) 
	{   //use loop go to each colum
		for(int currentCell=0;currentCell<ttCell;currentCell++) {
		System.out.print(ExcelWSheet.getRow(currentRow).getCell(currentCell).toString()+"\t");
	
		}
		System.out.println();
	}
	try {
	ExcelWBook.close();
	}catch(IOException e) {
		e.printStackTrace();
	}
	
	}
	
	
	public static String mobilenumber() {
		//Store Excel Value
		String value="";
	    XSSFWorkbook ExcelWBook=null;
	    XSSFSheet ExcelWSheet;
	    //XSSFRow Row;
	    //XSSFCell Cell;
	
	//file open
	    FileInputStream inputStream=null;
	try {
	File exFile=new File("C:\\Users\\2475070\\OneDrive - Cognizant\\Desktop\\Stage 2\\ProjectFile.xlsx");
	 inputStream=new FileInputStream(exFile);
	}catch(FileNotFoundException e){
		e.printStackTrace();
	}
	//Write Data Object
	try {
	ExcelWBook =new XSSFWorkbook(inputStream);
	}catch(IOException e) {
		e.printStackTrace();
	}
	ExcelWSheet=ExcelWBook.getSheetAt(0);
	
	int ttRows=ExcelWSheet.getLastRowNum()+1;
	//int ttCell=ExcelWSheet.getRow(0).getLastCellNum();
	System.out.println("");
	//System.out.println("Excel Data used:");
	for(int currentRow=0;currentRow<ttRows;currentRow++) {
		
		//Write Data
		value=ExcelWSheet.getRow(1).getCell(0).toString();
	}
	try {
	ExcelWBook.close();
	}catch(IOException e) {
		e.printStackTrace();
	}
	return value;
	}


	
}
