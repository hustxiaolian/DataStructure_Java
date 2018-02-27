package chapterFour;

import java.io.File;

public class ListDirAllFile {

	public static void main(String[] args) {
		File file = new File("D:\\lammps\\Cu_melt");
		listDirAllFile(file);
	}
	
	public static void listDirAllFile(File dir) {
		if(!dir.exists())
			throw new IllegalArgumentException("该文件夹"+dir+"不存在");
		if(!dir.isDirectory())
			throw new IllegalArgumentException(dir+"不是文件夹");
		
		for(File file:dir.listFiles()) {
			if(file.isDirectory())
				listDirAllFile(file);
			else
				System.out.println(file.toString() + "\tsize:" + file.length() + " bytes");
				
		}
	}
}
