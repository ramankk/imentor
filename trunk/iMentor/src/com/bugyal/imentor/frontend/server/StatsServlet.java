package com.bugyal.imentor.frontend.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatsServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static interface Stat {
		public void inc();
		public void inc(long amount);
	}
	
	public static class CountingStat implements Stat {
		private String name;
		private long value;
		
		public CountingStat(String name) {
			this.name = name;
			this.value = 0;
		}
		
		public void inc() {
			inc(1);
		}
		
		public void inc(long amount) {
			value += amount;
		}
		
		public String toString() {
			return name + "\t\t" + value;
		}
	}
	
	public static class AverageStat implements Stat {
		private String name;
		private int count;
		private long value;
		
		public AverageStat(String name) {
			this.name = name;
			this.count = 0;
			this.value = 0;
		}
		
		public void inc() {
			inc(1);
		}
		
		public void inc(long amount) {
			count++;
			value += amount;
		}
		
		public String toString() {
			if (count == 0) {
			  return name + "\t\t" + value + " (" + count + ")";
			} else {
			  return name + "\t\t" + (value/count) + " (" + count + ")";
			}
		}
	}

	private static final List<Stat> allStats = new ArrayList<Stat>();

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		PrintWriter out = response.getWriter();
		for (Stat s : allStats) {
			out.println(s);
		}
		out.close();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		doPost(request, response);
	}
	
	public static CountingStat createCountingStat(String name) {
		CountingStat newStat = new CountingStat(name);
		allStats.add(newStat);
		return newStat;
	}
	
	public static AverageStat createAverageStat(String name) {
		AverageStat newStat = new AverageStat(name);
		allStats.add(newStat);
		return newStat;
	}
	
}
