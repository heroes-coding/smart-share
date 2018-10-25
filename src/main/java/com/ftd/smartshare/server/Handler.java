package com.ftd.smartshare.server;

import java.io.IOException;
import java.net.Socket;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public abstract class Handler {
	private Socket client;
	private Marshaller marshaller;

	public Handler(Socket client, Marshaller marshaller) {
		this.client = client;
		this.marshaller = marshaller;
	}

	public void sendResponseToClient(Object response) {
		try {
			marshaller.marshal(response, client.getOutputStream());
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
