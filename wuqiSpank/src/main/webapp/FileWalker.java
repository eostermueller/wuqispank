
import java.io.File;
public class FileWalker {


	public static void main(String args[]) {
		FileWalker fileWalker = new FileWalker();
		fileWalker.list( new File(args[0])	);
	
	}

	public void list(File file) {
    		System.out.println(file.getName());
	    File[] children = file.listFiles();
	    if (children!=null)
	    for (File child : children) {
       		 list(child);
    		}	
	}

}
