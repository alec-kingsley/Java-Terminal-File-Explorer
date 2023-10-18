import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Path {
    private String path = "";
    
    // empty initializer sets path to current directory
    public Path() {
        File doc = new File("");
        path = doc.getAbsolutePath() + "\\";   
    }
    public Path(String path) {
        this.path = path;
    }
    // goes back one file for every . found at start, then goes forward
    // by every /fileName after
    public void pathChange(String change, boolean printPath) {
        //goes back one file for every . found at start
        while (path.length() > 0 && change.charAt(0) == '.') {
            if (path.charAt(path.length()-1) == '\\')
                path = path.substring(0,path.length()-1);
            while (path.length() > 0 && path.charAt(path.length()-1) != '\\')
                path = path.substring(0,path.length()-1);
            if (change.length() > 1)
                change = change.substring(1,change.length());
            else change = "";
        }
        if (path.charAt(path.length()-1) == '\\')
            path = path.substring(0,path.length()-1);
        if (change.charAt(change.length()-1) == '\\')
            change = change.substring(0,change.length()-1);
        if (change.length() > 0 && change.charAt(0) == '\\') {
            if (change.length() > 1)
                change = change.substring(1);
            else
                change = "";
        }
        path = path + "\\" + change;
        if (change.length() > 0) path = path + "\\";
        if (printPath) System.out.println("New path: "+path);
    }
    public void pathChange(String change) {
        pathChange(change, true);
    }
    public void list() {
        String[] files = (new File(path)).list();
        System.out.print("There are "+files.length+" files in this directory: ");
        System.out.println(path);
        int folderCt = 0;
        for (String file : files)
            if (new File(path + file).isDirectory()) folderCt++;
        System.out.println("\nFolders ("+folderCt+"):");
        for (String file : files)
            if (new File(path + file).isDirectory()) System.out.println(file);
        System.out.println("\nFiles("+(files.length-folderCt)+"):");
        for (String file : files)
            if (!new File(path + file).isDirectory()) System.out.println(file);
    }
    
    //returns one of the paths where folder is found
    public void find(String fileName) {
        String found = "0";
        if (path.length() == 0 || path.charAt(path.length()-1) != '\\')
            path = path + "\\";
        
        List<String> paths = new ArrayList<String>();
        List<Integer> intPath = new ArrayList<Integer>();
        
        
        //do-while loop creates 0s indicating 0th folder within each folder
        int folderCt;
        String[] files;
        do {
            do { 
                files = new File(path).list();
                folderCt = 0;
                for (String file : files) {
                    if (new File(path + file).isDirectory()) folderCt++;
                    if (file.equals(fileName)) paths.add(path+fileName);
                }
                if (folderCt > 0)
                    intPath.add(0,0);
                
                for (String file : files)
                    if (new File(path + file).isDirectory()) {
                        this.pathChange("\\"+file,false);
                        break;
                    }
            } while (folderCt > 0);
            files = new File(path).list();
            this.pathChange(".\\",false);
            do {
                files = new File(path).list();
                folderCt = -1; // subtracted one for current folder
                for (String file : files)
                    if (new File(path + file).isDirectory()) folderCt++;
                if (intPath.get(0) < folderCt) {
                    intPath.set(0,intPath.get(0)+1);
                    //go to next folder in dir
                    folderCt = -1;
                    for (String file : files) {
                        if (new File(path + file).isDirectory()) folderCt++;
                        if (intPath.get(0) == folderCt) { 
                            this.pathChange("\\"+file,false);
                            break;
                        }
                    }
                    break;
                } else {
                    intPath.remove(0);
                    if (intPath.size() > 0) this.pathChange(".\\",false);
                }
            } while (intPath.size() > 0);
        } while (intPath.size() > 0);
        if (paths.size() == 0)
            System.out.println("File not found.");
        else if (paths.size() == 1)
            System.out.println("Found directory: " + paths.get(0));
        else {
            System.out.println("Found directories:");
            for (String foundPath : paths)
                System.out.println(foundPath);
        }
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
    
}