package br.uff.ic.provmonitor.workspaceWatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class WorkspaceAccessReader {

	/**
	 * Method that recursively read the path structure looking for Path with access time greater than the start date parameterized.
	 * @param rootPath <code>Path</code> - Path to be used as root for the recursively read
	 * @param startDate <code>Date</code> - Date to be compared with. It will return all path entries with access time greater than this date 
	 * @param onlyFiles <code>boolean</code> - Flag to include Directory's Paths or only File's Paths
	 * @return AbsolutePathArray <code>ArrayList<String></code> - Array with the absolute path of all Entries with access time greater than the one informed on startDate param.
	 * @throws IOException - When any problem occurs accessing Path attributes.
	 * */
	public static final ArrayList<String> readAccessedPaths (Path rootPath, Date startDate, boolean onlyFiles) throws IOException{
		ArrayList<String> result = new ArrayList<String>();
		
		//If the rootPath is not a Directory, verify accessTime. Else iterate recursively inside it's structure. 
		if (!rootPath.toFile().isDirectory()){
			BasicFileAttributes attributes = Files.readAttributes(rootPath, BasicFileAttributes.class);
			FileTime fileTime = attributes.lastAccessTime();
			Date fileDate = new Date(fileTime.toMillis());
			//Date compare
			if (startDate.compareTo(fileDate) < 0){
				//System.out.println("Accessed Path: " + rootPath + " - Access date: " + fileDate);
				result.add(rootPath.toFile().getAbsolutePath());
			}
		}else{
			//Queue to help navigate recursively inside the Path.
			Queue<Path> pathQueue = new LinkedList<Path>();

			//First level access
			for (File childrenPassName: rootPath.toFile().listFiles()){
				//TODO: Ignore list support: using regular expression
				
				//BasicFileAttributes attributes = Files.readAttributes(Paths.get(childrenPassName.getAbsolutePath()), BasicFileAttributes.class);
				//Date fileDate = new Date(attributes.lastAccessTime().toMillis());
				//System.out.println("[Inicio do Loop] Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
				
				//Ignore Git pathes
				if (!childrenPassName.getAbsolutePath().contains(".git")){
					Path childrenPass = Paths.get(childrenPassName.getAbsolutePath());
					
					BasicFileAttributes attributes = Files.readAttributes(childrenPass, BasicFileAttributes.class);
					Date fileDate = new Date(attributes.lastAccessTime().toMillis());
					
					//If the path is a file put it on the Queue to recursively navigation.
					if (childrenPassName.isDirectory()){
						//Date compare
						if (startDate.compareTo(fileDate) < 0){
							pathQueue.add(childrenPass);
							if (!onlyFiles){
								//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
								result.add(childrenPassName.getAbsolutePath());
							}
						}
					}else{
						//Date compare
						if (startDate.compareTo(fileDate) < 0){
							//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
							result.add(childrenPassName.getAbsolutePath());
						}
					}
				}
			}
			
			//Recursively access through Path tree
			while (!pathQueue.isEmpty()){
				File childrenFile = pathQueue.poll().toFile();
				for (File childrenPassName: childrenFile.listFiles()){
					//TODO: Ignore list support: using regular expression
					
					//BasicFileAttributes attributes = Files.readAttributes(Paths.get(childrenPassName.getAbsolutePath()), BasicFileAttributes.class);
					//Date fileDate = new Date(attributes.lastAccessTime().toMillis());
					//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate + " [Inicio do Loop]");
					
					if (!childrenPassName.getAbsolutePath().contains(".git")){
						Path childrenPass = Paths.get(childrenPassName.getAbsolutePath());
						
						BasicFileAttributes attributes = Files.readAttributes(childrenPass, BasicFileAttributes.class);
						Date fileDate = new Date(attributes.lastAccessTime().toMillis());
						
						//If the path is a file put it on the Queue to recursively navigation.
						if (childrenPassName.isDirectory()){
							//Date compare
							if (startDate.compareTo(fileDate) < 0){
								pathQueue.add(childrenPass);
								if (!onlyFiles){
									//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
									result.add(childrenPassName.getAbsolutePath());
								}
							}
						}else{
							//Date compare
							if (startDate.compareTo(fileDate) < 0){
								//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
								result.add(childrenPassName.getAbsolutePath());
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method that recursively read the path structure looking for Path with access time greater than the start date parameterized.
	 * @param rootPath <code>Path</code> - Path to be used as root for the recursively read
	 * @param startDate <code>Date</code> - Date to be compared with. It will return all path entries with access time greater than this date 
	 * @param onlyFiles <code>boolean</code> - Flag to include Directory's Paths or only File's Paths
	 * @return <code>Collection<AccessedPath> - Array with the absolute path and access dateTime of all Entries with access time greater than the one informed on startDate parameter.
	 * @throws IOException - When any problem occurs accessing Path attributes.
	 * */
	public static final Collection<AccessedPath> readAccessedPathsAndAccessTime (Path rootPath, Date startDate, boolean onlyFiles) throws IOException{
		ArrayList<AccessedPath> result = new ArrayList<AccessedPath>();
		
		//If the rootPath is not a Directory, verify accessTime. Else iterate recursively inside it's structure. 
		if (!rootPath.toFile().isDirectory()){
			BasicFileAttributes attributes = Files.readAttributes(rootPath, BasicFileAttributes.class);
			FileTime fileTime = attributes.lastAccessTime();
			Date fileDate = new Date(fileTime.toMillis());
			//Date compare
			if (startDate.compareTo(fileDate) < 0){
				//System.out.println("Accessed Path: " + rootPath + " - Access date: " + fileDate);
				result.add(new AccessedPath(rootPath.toFile().getAbsolutePath(), fileDate));
			}
		}else{
			//Queue to help navigate recursively inside the Path.
			Queue<Path> pathQueue = new LinkedList<Path>();

			//First level access
			for (File childrenPassName: rootPath.toFile().listFiles()){
				//TODO: Ignore list support: using regular expression
				
				//BasicFileAttributes attributes = Files.readAttributes(Paths.get(childrenPassName.getAbsolutePath()), BasicFileAttributes.class);
				//Date fileDate = new Date(attributes.lastAccessTime().toMillis());
				//System.out.println("[Inicio do Loop] Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
				
				//Ignore Git pathes
				if (!childrenPassName.getAbsolutePath().contains(".git")){
					Path childrenPass = Paths.get(childrenPassName.getAbsolutePath());
					
					BasicFileAttributes attributes = Files.readAttributes(childrenPass, BasicFileAttributes.class);
					Date fileDate = new Date(attributes.lastAccessTime().toMillis());
					
					//If the path is a file put it on the Queue to recursively navigation.
					if (childrenPassName.isDirectory()){
						//Date compare
						if (startDate.compareTo(fileDate) < 0){
							pathQueue.add(childrenPass);
							if (!onlyFiles){
								//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
								result.add(new AccessedPath(childrenPassName.getAbsolutePath(), fileDate));
							}
						}
					}else{
						//Date compare
						if (startDate.compareTo(fileDate) < 0){
							//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
							result.add(new AccessedPath(childrenPassName.getAbsolutePath(), fileDate));
						}
					}
				}
			}
			
			//Recursively access through Path tree
			while (!pathQueue.isEmpty()){
				File childrenFile = pathQueue.poll().toFile();
				for (File childrenPassName: childrenFile.listFiles()){
					//TODO: Ignore list support: using regular expression
					
					//BasicFileAttributes attributes = Files.readAttributes(Paths.get(childrenPassName.getAbsolutePath()), BasicFileAttributes.class);
					//Date fileDate = new Date(attributes.lastAccessTime().toMillis());
					//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate + " [Inicio do Loop]");
					
					if (!childrenPassName.getAbsolutePath().contains(".git")){
						Path childrenPass = Paths.get(childrenPassName.getAbsolutePath());
						
						BasicFileAttributes attributes = Files.readAttributes(childrenPass, BasicFileAttributes.class);
						Date fileDate = new Date(attributes.lastAccessTime().toMillis());
						
						//If the path is a file put it on the Queue to recursively navigation.
						if (childrenPassName.isDirectory()){
							//Date compare
							if (startDate.compareTo(fileDate) < 0){
								pathQueue.add(childrenPass);
								if (!onlyFiles){
									//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
									result.add(new AccessedPath(childrenPassName.getAbsolutePath(), fileDate));
								}
							}
						}else{
							//Date compare
							if (startDate.compareTo(fileDate) < 0){
								//System.out.println("Accessed Path: " + childrenPassName.getAbsolutePath() + " - Access date: " + fileDate);
								result.add(new AccessedPath(childrenPassName.getAbsolutePath(), fileDate));
							}
						}
					}
				}
			}
		}
		return result;
	}
	
}
