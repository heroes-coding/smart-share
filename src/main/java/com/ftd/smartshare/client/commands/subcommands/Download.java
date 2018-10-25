package com.ftd.smartshare.client.commands.subcommands;

import java.io.File;

import com.ftd.smartshare.client.api.Api;
import com.ftd.smartshare.dto.DownloadRequestDto;

import picocli.CommandLine;
import picocli.CommandLine.Option;

@CommandLine.Command(description = "Downloads a file", name = "download", aliases = "d", mixinStandardHelpOptions = true)
public class Download implements Runnable {

	@CommandLine.Parameters(arity = "1", index = "0", description = "Name of file to be downloaded")
	private String fileName;

	@CommandLine.Parameters(arity = "1", index = "1", description = "The password for the file")
	private String password;

	@Option(names = "-s", description = "Return only the summary information from the file (defaults to false)")
	private boolean summaryOnly = false;

	@Option(names = "-f", description = "Relative save folder to save file to if download was successful")
	private String relativeSaveFolder = "downloads";

	public void run() {
		
		this.fileName = (new File(fileName)).getName(); // make sure name is a file name, and not a path + file name
		System.out.println((this.summaryOnly ? "Getting file summary for " : "Trying to download ") + fileName + "...");
		DownloadRequestDto downloadRequestDto = new DownloadRequestDto(this.fileName, this.password, this.summaryOnly,
				this.relativeSaveFolder);

		if (this.summaryOnly) {
			Api.getFileSummary(downloadRequestDto);
		} else {
			Api.downloadAndSave(downloadRequestDto);
		}
	}

}
