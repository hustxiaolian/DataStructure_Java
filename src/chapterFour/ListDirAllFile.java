package chapterFour;

import java.io.File;

public class ListDirAllFile {

	public static void main(String[] args) {
		File file = new File("D:\\lammps\\Cu_melt");
		listDirAllFile(file);
	}
	
	public static void listDirAllFile(File dir) {
		if(!dir.exists())
			throw new IllegalArgumentException("���ļ���"+dir+"������");
		if(!dir.isDirectory())
			throw new IllegalArgumentException(dir+"�����ļ���");
		
		for(File file:dir.listFiles()) {
			if(file.isDirectory())
				listDirAllFile(file);
			else
				System.out.println(file.toString() + "\tsize:" + file.length() + " bytes");
				
		}
	}
}
