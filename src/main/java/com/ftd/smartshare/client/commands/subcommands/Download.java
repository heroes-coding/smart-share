package com.ftd.smartshare.client.commands.subcommands;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.ftd.smartshare.client.commands.SmartShare;
import com.ftd.smartshare.dto.DownloadRequestDto;
import com.ftd.smartshare.dto.UploadRequestDto;

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
        System.out.println("Downloading " + fileName);
        DownloadRequestDto downloadRequestDto = new DownloadRequestDto(
        		this.fileName,
        		this.password,
        		this.summaryOnly
        		);
        
		JAXBContext context;
		try (
				Socket socket = new Socket("localhost", 6770);
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			){
			context = JAXBContext.newInstance(DownloadRequestDto.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(downloadRequestDto, out);
			
			
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
    	CommandLine.run(new SmartShare(), "download", "test.txt", "passworde");

	}

}
