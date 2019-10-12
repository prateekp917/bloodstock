package com.virtusa.bloodbank;

import java.util.List;

public interface BloodStockInterface {

	BloodStock findByID(int stock_id);
	List<BloodStock> findAll();
	BloodStock add(BloodStock bloodstock);
	void update(String allergy, int donor_id);
	boolean delete(int stock_id);

}
