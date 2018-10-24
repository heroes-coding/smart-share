package com.ftd.smartshare.client.commands.subcommands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.ftd.smartshare.client.commands.SmartShare;
import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.FileDto;
import com.ftd.smartshare.dto.UploadRequestDto;

import entity.File;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@CommandLine.Command(
        description = "Downloads a file",
        name = "download",
        aliases = "d",
        mixinStandardHelpOptions = true
)
public class Download implements Runnable {

    @CommandLine.Parameters(arity="1", index = "0", description = "Name of file to be downloaded")
    private String fileName;

    @CommandLine.Parameters(arity="1", index = "1", description = "The password for the file")
    private String password;

	@Option(names = "-s", description = "Return only the summary information from the file (defaults to false)")
	private boolean summaryOnly = false;
    
    public void run() {
        System.out.println((this.summaryOnly ? "Getting file summary for " : "Trying to download ") + fileName + "...");
        DownloadRequestDto downloadRequestDto = new DownloadRequestDto(
        		this.fileName,
        		this.password,
        		this.summaryOnly
        		);
        
		JAXBContext context;
		try (
				Socket server = new Socket("localhost", 6770);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
				BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			){
			context = JAXBContext.newInstance(DownloadRequestDto.class, FileDto.class);
			
			Marshaller marshaller = context.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(downloadRequestDto, stringWriter);
			out.write(stringWriter.toString());
			out.newLine();
			out.flush();
			

			Unmarshaller unmarshaller = context.createUnmarshaller();
			FileDto fileDto = (FileDto) unmarshaller.unmarshal(in);
			
			if (fileDto.getFileName() == null) {
				System.out.println("Could not get file.");
			} else if (this.summaryOnly) {
				System.out.printf("It was created at %s, has %d remaining downloads, and expires in %d minutes\n", 
					new Timestamp(fileDto.getTimeCreated()), fileDto.getRemainingDownloads(), fileDto.getTimeUntilExpiration());
			} else {
				File file = new File(fileDto.getFileName(),fileDto.getFile());
				file.saveFile("downloads");
			}
			



		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        
    }
    
    public static void main(String[] args) {
    	CommandLine.run(new SmartShare(), "download", "test.txt", "poobear");

	}

}
