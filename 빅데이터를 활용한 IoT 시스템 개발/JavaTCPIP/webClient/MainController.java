package com.tcpip;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chat.Client;

@Controller
public class MainController {
	
	Client client;
	
	public MainController() {
		client = new Client("192.168.0.4", 5555, "[ny_web]");
		try {
			client.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/main.mc")
	public ModelAndView main() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("main");
		return mv;
	}
//	@RequestMapping("/iot.mc")
//	public void send_iot() {
//		System.out.println("IoT Send Start ...");
//		client.sendTarget("/192.168.0.4", "100");
//	}
	@RequestMapping("/iot.mc")
	public void send_iot(HttpServletRequest req, HttpServletResponse res) throws IOException {
		System.out.println("IoT Send Start ...");

		String ip = req.getParameter("ip");
//		System.out.println(ip);
		client.sendTarget(ip, "100");
		PrintWriter out = res.getWriter();
		out.print("ok");
		out.close();
	}
	@RequestMapping("/phone.mc")
	public void send_phone() {
		System.out.println("Phone Send Start ...");
	}
}
