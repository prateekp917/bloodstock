package com.virtusa.bloodbank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.virtusa.bloodbank.*;
import java.util.Scanner;
import org.apache.log4j.Logger;
//import org.eclipse.jdt.internal.compiler.parser.Scanner;

public class BloodstockDao implements BloodStockInterface {
	private static final Logger log=Logger.getRootLogger();
	private Connection connection=DBConnection.getConnection();
	private final String FINDBLDTYPE ="select blood_type, count(blood_type) as quantity from blood_stock where blood_type =?";
	private final String UPDATEALG="update Blood_Stock set Allergies=? where donor_id =?";
	private final String DELETEBLD="delete from Blood_Stock where stock_id=?";
	private final String ADDBLD="insert into Blood_Stock(blood_type, expiry_date, HGB, RBC, WBC, Platelets, MCV, Donor_id, Allergies) values (?,?,?,?,?,?,?,?,?)";
	private final String FINDID="select stock_id, blood_type, expiry_date, HGB, RBC, WBC, Platelets, MCV from Blood_Stock where stock_id=?";
	private final String FINDALL="select stock_id, blood_type, expiry_date, HGB, RBC, WBC, Platelets, MCV, Donor_id, Allergies from Blood_Stock";
	
	 
	

	@Override
	public BloodStock findByID(int stock_id) {
		// TODO Auto-generated method stub
		PreparedStatement pst=null;
		BloodStock bloodstock=null;
		try {
			pst=connection.prepareStatement(FINDID);
			pst.setInt(1,stock_id);
			ResultSet resultset=pst.executeQuery();
			if(resultset.next())
			{
				int sid=resultset.getInt("stock_id");
				String bldtype=resultset.getString("blood_type");
				String edate=resultset.getString("expiry_date");
				double hgb=resultset.getDouble("HGB");
				double rbc=resultset.getDouble("RBC");
				double wbc=resultset.getDouble("WBC");
				double plts=resultset.getDouble("Platelets");
				double mcv=resultset.getDouble("MCV");

					
				bloodstock=new BloodStock(sid, bldtype, edate, hgb, rbc, wbc, plts, mcv);
				log.trace(bloodstock);
				
			}
		}catch(SQLException e)
		{
			System.out.println(e.getMessage());
			log.error(e);
		}
		finally {
			if(pst!=null)
				try {
					pst.close();
				}catch(SQLException e)
			{
					log.error(e);
			}
		}
		return bloodstock;
	}
	
	@Override
	public List<BloodStock> findAll() {
		PreparedStatement pst=null;
		List<BloodStock> bloodstock=new ArrayList<>();
		try
		{
			pst=connection.prepareStatement(FINDALL);
			ResultSet resultset=pst.executeQuery();
			while(resultset.next())
			{
				int sid=resultset.getInt("stock_id");
				String bldtype=resultset.getString("blood_type");
				String edate=resultset.getString("expiry_date");
				double hgb=resultset.getDouble("HGB");
				double rbc=resultset.getDouble("RBC");
				double wbc=resultset.getDouble("WBC");
				double plts=resultset.getDouble("Platelets");
				double mcv=resultset.getDouble("MCV");
				int did=resultset.getInt("Donor_id");
				String allergies=resultset.getString("Allergies");
				BloodStock b=new BloodStock(sid, bldtype, edate, hgb, rbc, wbc, plts, mcv, did, allergies);
				bloodstock.add(b);
			}
		}
		catch(SQLException e)
		{
			log.error(e);
			System.out.println(e.getMessage());
			//return null;
		}
		finally 
		{
			if(pst!=null)
				try {
					pst.close();
				}catch(SQLException e)
			{
					log.error(e);
			}
		}
		return bloodstock;
	}

	

	@Override
	public BloodStock add(BloodStock bloodstock) {
		PreparedStatement pst=null;
		try {
			pst=connection.prepareStatement(ADDBLD);
			//pst.setInt(1, bloodstock.getStock_id());
			pst.setString(1, bloodstock.getBloodtype());
			pst.setString(2, bloodstock.getExpdate());
			pst.setDouble(3, bloodstock.getHgb());
			pst.setDouble(4, bloodstock.getRbc());
			pst.setDouble(5, bloodstock.getWbc());
			pst.setDouble(6, bloodstock.getPlatelets());
			pst.setDouble(7, bloodstock.getMcv());
			pst.setInt(8, bloodstock.getDonor_id());
			pst.setString(9, bloodstock.getAllergies());
			
			
			pst.executeUpdate();
			//connection.commit();
		}
			/*String keysql="select seq.currval from dual";
			Statement stmt=connection.createStatement();
			boolean rsavailable=stmt.execute(keysql);
			long idgen=0;
			if(rsavailable) {
				ResultSet rs=stmt.getGeneratedKeys();
				rs.next();
				idgen=rs.getLong(1);
				log.debug("id generated: "+idgen);
				bloodstock.setStock_id(id);
			}
			else
			{
				log.debug("cant find key");
			}
			
			return bloodstock;
		}*/
		catch (SQLException e) {
			log.error(e);
			System.out.println(e.getMessage());
		}
		catch(Exception e)
		{
			log.error(e);
			System.out.println(e.getMessage());
			//return null;
		}
		finally 
		{
			if(pst!=null)
				try {
					pst.close();
				}catch(SQLException e)
			{
					log.error(e);
			}
		}
		
			
		 
		return bloodstock;
	}
	
	public void update(String allergy, int did) {
		PreparedStatement pst=null;
		try {
			pst=connection.prepareStatement(UPDATEALG);
			pst.setString(1,allergy);
			pst.setInt(2, did);
			pst.executeUpdate();
			//connection.commit();
			
		}
		catch(Exception e)
		{
			log.error(e);
			System.out.println(e.getMessage());
			//return null;
		}
		finally 
		{
			if(pst!=null)
				try {
					pst.close();
				}catch(SQLException e)
			{
					log.error(e);
					System.out.println(e.getMessage());

			}
		}
		
	}

	@Override
	public boolean delete(int stock_id) {
		PreparedStatement pst=null;
		try 
		{
			pst=connection.prepareStatement(DELETEBLD);
			pst.setInt(1, stock_id);
			int rows=pst.executeUpdate();
			//connection.commit();
			if(rows!=1)return false;
			else return true;
		}
		catch(SQLException e)
		{
			log.error(e);
			return false;
		}
		finally 
		{
			if(pst!=null)
				try {
					pst.close();
				}catch(SQLException e)
			{
					log.error(e);
			}
		}
		
		@Override
		public BloodStock findBldtype(String bloodtype) {
			PreparedStatement pst=null;
			try 
			{
				pst=connection.prepareStatement(FINDBLDTYPE);
				pst.setInt(1, bloodtype);
				ResultSet resultset=pst.executeQuery();
				if(resultset.next())
				{
					String btype = rs.getString("bloodtype");
					int count = rs.getInt("quantity");
					
				BloodStock bbtype =  new BloodStock(btype, count);
				log.trace(bbtype);
					
				}
			}
			catch(SQLException e)
			{
				log.error(e);
				return false;
			}
			finally 
			{
				if(pst!=null)
					try {
						pst.close();
					}catch(SQLException e)
				{
						log.error(e);
				}
			}
	}
	
	public static void main(String[] args) {
		BloodStockInterface bldstkdao=new BloodstockDao();
		
		Scanner sc= new Scanner(System.in);
		//to find blood details by id
		BloodStock b = null;
		System.out.println("Enter the ID whose details you want to search: ");
		int id = sc.nextInt();
		
		
		b= bldstkdao.findByID(id);
		System.out.println(b);
		sc.nextLine();
		
		System.out.println("\n");
		List<BloodStock> list=bldstkdao.findAll();
		list.forEach((n)->System.out.println(n));   // to display all record(FindAll)

		
		//to add new blood details
		System.out.println("Enter bloodtype you want to add: ");
		String bldtype = sc.nextLine();
		System.out.println("Enter Donation date(yyyy-mm-dd) you want to add: ");
		String dondate = sc.nextLine();
		System.out.println("Enter Haemoglobin count: ");
		double hgb = sc.nextDouble();
		System.out.println("Enter RBC count: ");
		double rbc = sc.nextDouble();
		System.out.println("Enter WBC count: ");
		double wbc = sc.nextDouble();
		System.out.println("Enter Platelets count: ");
		double plts = sc.nextDouble();
		System.out.println("Enter MCV count: ");
		double mcv = sc.nextDouble();
		System.out.println("Enter Donor ID: ");
		int donid = sc.nextInt();
		sc.nextLine();
		System.out.println("Enter Allergies: ");
		String alrg = sc.nextLine();

		BloodStock bloodstock=new BloodStock(bldtype, dondate, hgb, rbc, wbc, plts, mcv, donid, alrg);
		bloodstock=bldstkdao.add(bloodstock);
		if(bloodstock!=null) {
			System.out.println("Details added successfully!! \n");
		}
		
		//to update allergy details for particular donor
		System.out.println("update allergy details for particular donor!!! ");

		System.out.println("Enter Donor ID to update : ");
		int donupdate = sc.nextInt();
		sc.nextLine();
		
		System.out.println("Enter Allergies: ");
		String algupdate = sc.nextLine();
		
		bldstkdao.update(algupdate, donupdate);  //to update
		System.out.println("Details updated successfully!! \n");

		// to delete particular record
		System.out.println("Delete particular record\n");
		System.out.println("Enter Stock ID to delete: ");
		int siddelete= sc.nextInt();

		boolean bs=bldstkdao.delete(siddelete);
		if(bs==true)
			System.out.println("row deletion successful!!!");
		//log.debug(bldstkdao);
	   
		}

	

	}

