package br.uff.ic.provmonitor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class WorkspaceUtils {
	
	public static void copyDirectory(Path source, Path destiny) throws IOException{
		
	    
	    String sourceLocation = source.toString();
	    String targetLocation = destiny.toString();
	    
	    copyDirectory(sourceLocation, targetLocation);
	    
//	    final Path targetPath = destiny;
//	    final Path sourcePath = source;
	    
//	    Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
//        @Override
//        public FileVisitResult preVisitDirectory(final Path dir,
//                final BasicFileAttributes attrs) throws IOException {
//            Files.createDirectories(targetPath.resolve(sourcePath
//                    .relativize(dir)));
//            return FileVisitResult.CONTINUE;
//        }
//
//        @Override
//        public FileVisitResult visitFile(final Path file,
//                final BasicFileAttributes attrs) throws IOException {
//            Files.copy(file,
//                    targetPath.resolve(sourcePath.relativize(file)));
//            return FileVisitResult.CONTINUE;
//        }
//    });
	}

	public static void copyDirectory(String source, String destiny) throws IOException{
		
		File sourceLocation = new File (source);
		File targetLocation = new File (destiny);
	    
	    if (!targetLocation.exists() || !targetLocation.isDirectory()){
			//If path does not exists create it.
			if(!targetLocation.mkdirs()){
				throw new IOException("Workspace path does not exists and it was not possible create the path.");
			}
		}
	    
	    copyDirectory(sourceLocation, targetLocation);
	    
	    
	    
	}
	
	public static void copyDirectory(File sourceLocation , File targetLocation)
			 throws IOException {
			      
			     if (sourceLocation.isDirectory()) {
			         if (!targetLocation.exists()) {
			             targetLocation.mkdir();
			         }
			          
			         String[] children = sourceLocation.list();
			         for (int i=0; i<children.length; i++) {
			             copyDirectory(new File(sourceLocation, children[i]),
			                     new File(targetLocation, children[i]));
			         }
			     } else {
			          
			         InputStream in = new FileInputStream(sourceLocation);
			         OutputStream out = new FileOutputStream(targetLocation);
			          
			         // Copy the bits from instream to outstream
			         byte[] buf = new byte[1024];
			         int len;
			         while ((len = in.read(buf)) > 0) {
			             out.write(buf, 0, len);
			         }
			         in.close();
			         out.close();
			     }
			 }
}
