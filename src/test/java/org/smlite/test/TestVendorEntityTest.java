package org.smlite.test;

import org.smlite.entity.VendorEntity;
import org.smlite.util.SMLiteUtil;

import junit.framework.TestCase;

public class TestVendorEntityTest extends TestCase {

	public void testTestSetName() {
		String nextId = SMLiteUtil.nextId();
		String ciscoStr = "Cisco";
		VendorEntity entity = new VendorEntity(nextId, ciscoStr, true, 257846, "Communicator Hands Free", 45000,"monthly","Staff_Augmentation");
		
		entity.setName("Apple");
		assertFalse(entity.getName().equals(ciscoStr));
	}

	public void testTestSetPoDetails() {
		String nextId = SMLiteUtil.nextId();
		String poDetailsStr = "Communicator Hands Free";
		VendorEntity entity = new VendorEntity(nextId, "Cisco", true, 257846, poDetailsStr,55000,"monthly", "Staff_Augmentation");
		
		entity.setPoDetails("Communicator Wi-fi");
		assertFalse(entity.getPoDetails().equals(poDetailsStr));
	}

	public void testTestCopyTo() {
		VendorEntity entity = new VendorEntity(SMLiteUtil.nextId(), "Cisco", true, 257846, "Communicator Hands Free",65000,"monthly", "Staff_Augmentation");
		
		VendorEntity entity2 = new VendorEntity();
		entity.copyTo(entity2);
		entity.setId(SMLiteUtil.nextId());
		
		assertTrue(entity.getPoDetails().equals(entity2.getPoDetails()));
		assertFalse(entity.getId().equals(entity2.getId()));
	}

}
