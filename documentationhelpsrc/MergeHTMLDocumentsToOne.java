import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Scanner;





/**
 * 
 */

/**
 * @author kjellw
 *
 */
public class MergeHTMLDocumentsToOne {


	/**
	 * The program expects input to standard in that is file names of files to
	 * merge into the document. When the program has got the end character it
	 * ends. The merged document is printed to standard out.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 0)
			printInformationExit();

		
		writeHeader();
		scanStandardInForFilesToInclude();
		writeFooter();

	}

	private static void scanStandardInForFilesToInclude() {
		Scanner fileScan = new Scanner(System.in);
		while(fileScan.hasNext()){
			String file = fileScan.next();
			File inputFile = new File(file);
			Scanner inputFileScanner = null;
			try {
				inputFileScanner = new Scanner(inputFile);
				inputFileScanner.useDelimiter("<body>");
				inputFileScanner.next();//Throw away header
				inputFileScanner.useDelimiter("</body>");
				String content = inputFileScanner.next();//get content
				if(content==null){
					System.err.println("Not a proper html file");
					System.exit(0);
				}
				String content2 = content.substring("<body>".length());
				System.out.println(content2);
					
			} catch (FileNotFoundException e) {
				System.err.println("Could not find file: " + file);
				System.exit(1);
			}finally{
				if(inputFileScanner!=null)
					inputFileScanner.close();
			}
			
		}
		
	}

	private static void writeFooter() {
		System.out.println("</body>");
		System.out.println("</html>");
;

		
	}

	private static void writeHeader() {
		System.out.println("<html>");
		System.out.println("<head>");
		System.out.println("</head>");
		System.out.println("<body>");
	}

	private static void printInformationExit() {
		System.out.println("The program expects input to standard in that is file names of files to");
		System.out.println("merge into the document. When the program has got the end character it");
		System.out.println("ends. The merged document is printed to standard out.");
		System.exit(0);
	}

}
