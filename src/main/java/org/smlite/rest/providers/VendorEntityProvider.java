
package org.smlite.rest.providers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sakaiproject.entitybus.EntityReference;
import org.sakaiproject.entitybus.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybus.entityprovider.EntityProviderManager;
import org.sakaiproject.entitybus.entityprovider.capabilities.RESTful;
import org.sakaiproject.entitybus.entityprovider.extension.Formats;
import org.sakaiproject.entitybus.entityprovider.search.Search;
import org.sakaiproject.entitybus.util.AbstractAutoRegisteringProvider;
import org.smlite.entity.VendorEntity;
import org.smlite.util.SMLiteUtil;



public class VendorEntityProvider extends AbstractAutoRegisteringProvider implements CoreEntityProvider, RESTful {

	public Connection conn = null; 
	public Statement stmt=null;
	 private static final String VENDOR_TABLE = "create table IF NOT EXISTS SMLite.Vendor ( "
		    + "   id VARCHAR(20) PRIMARY KEY, name VARCHAR(20) not null, poCreated BOOLEAN, "
		    + "   poNumber BIGINT(20), poDetails VARCHAR(20), poAmount BIGINT(15), poDuration VARCHAR(15), type VARCHAR(20))";
	public static Connection getConnection() throws Exception {
	    String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://localhost:3306";
	    String username = "root";
	    String password = "admin";
	    Class.forName(driver);
	    Connection conn = DriverManager.getConnection(url, username, password);
	    return conn;
	 }
	public VendorEntityProvider(EntityProviderManager entityProviderManager) {
		super(entityProviderManager);
		
		
		try {
			
			conn = getConnection(); 
			stmt=conn.createStatement();

			stmt.execute("CREATE DATABASE IF NOT EXISTS SMLite");
			boolean test= stmt.execute(VENDOR_TABLE);
			
			System.out.println(" database Done "+test);
			

		} catch (ClassNotFoundException e) {
		      System.out.println("error: failed to load MySQL driver.");
		      e.printStackTrace();
		    } catch (SQLException e) {
		      System.out.println("error: failed to create a connection object.");
		      e.printStackTrace();
		    } catch (Exception e) {
		      System.out.println("other error:");
		      e.printStackTrace();
		    } finally {
		      try {
		        stmt.close();
		      //  conn.close();        
		      } catch (SQLException e) {
		        e.printStackTrace();
		      }
		    }
	}

	public String getEntityPrefix() {
		return "vendor";
	}

	public String createEntity(EntityReference ref, Object entity, Map<String, Object> params) {
		String nextId = SMLiteUtil.nextId();
		VendorEntity passedEntity = (VendorEntity) entity;
		
		passedEntity.setId(nextId); // overwrite passed ID
		System.out.println(" createEntity "+passedEntity);

		try {
			stmt=conn.createStatement();

			stmt.execute("INSERT INTO SMLite.Vendor (id, name, poCreated ,poNumber, poDetails, poAmount,poDuration,type) " +
					"VALUES('"+nextId+"','"+passedEntity.getName()+"',"+passedEntity.isPoCreated()
					+","+passedEntity.getPoNumber()+",'"+passedEntity.getPoDetails()+"',"+passedEntity.getPoAmount()+",'"+passedEntity.getPoDuration()+"','"+passedEntity.getType()
					+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				if(stmt!=null)
				stmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return nextId;
	}

	public Object getSampleEntity() {
		return new VendorEntity();
	}

	public void updateEntity(EntityReference ref, Object entity, Map<String, Object> params) {
		VendorEntity passedEntity = (VendorEntity) entity;
		ResultSet rs = null;
		System.out.println(" updateEntity "+passedEntity);

		try {
			stmt=conn.createStatement();

			rs = stmt.executeQuery("Select id, name, poCreated ,poNumber, poDetails, type from SMLite.Vendor where id="+ref.getId());
			while (rs.next()) {
				String queryStr= "UPDATE SMLite.Vendor set name='"+passedEntity.getName()+"', " +
				"poCreated="+passedEntity.isPoCreated()+", poDetails='"+passedEntity.getPoDetails()+"', poNumber='"+passedEntity.getPoNumber()+"', " +
				"type='"+passedEntity.getType() +"', poAmount='"+passedEntity.getPoAmount() +"' where id="+ref.getId();
				System.out.println("queryStr "+queryStr);
				stmt.executeUpdate(queryStr);
				
				break;
			}
		} catch (SQLException e) {
			try {
				if(stmt!=null)
				stmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		
	}

	public Object getEntity(EntityReference ref) {
		if (ref.getId() == null) {
			return new VendorEntity();
		}
		System.out.println(" getEntity "+ref.getId());

		ResultSet rs = null;
		try {
			stmt=conn.createStatement();

			rs = stmt.executeQuery("Select * from SMLite.Vendor where id="+ref.getId());
			while (rs.next()) {
				String id = (String) rs.getString(VendorEntity.ID);
				String name = (String) rs.getString(VendorEntity.NAME);
				boolean poCreated = (Boolean) rs.getBoolean(VendorEntity.PO_CREATED);
				long poNumber = (Long) rs.getLong(VendorEntity.PO_NUMBER);
				String poDetails = (String) rs.getString(VendorEntity.PO_DETAILS);
				int poAmount = (Integer) rs.getInt(VendorEntity.PO_AMOUNT);
				String poDuration = (String) rs.getString(VendorEntity.PO_DURATION);
				String type = (String) rs.getString(VendorEntity.TYPE);
				
				VendorEntity e = new VendorEntity(id,name,poCreated, poNumber, poDetails,poAmount,poDuration,type);
				return e;
			}
		} catch (SQLException e) {
			try {
				if(stmt!=null)
				stmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			e.printStackTrace();
		}

		return null;
	}

	public void deleteEntity(EntityReference ref, Map<String, Object> params) {
		System.out.println("############to delete########  : "+ref.getId());
		if (ref.getId() == null) {
			throw new IllegalArgumentException("Invalid entity id, cannot find entity to remove: " + ref);
		}
		// search query
		ResultSet rs = null;
		
		try {
			stmt=conn.createStatement();

			rs = stmt.executeQuery("Select * from SMLite.Vendor where id="+ref.getId());
			while (rs.next()) {
				
				stmt.executeUpdate("DELETE FROM SMLite.Vendor where id="+ref.getId());
				break;
			}
		} catch (SQLException e) {
			try {
				if(stmt!=null)
				stmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}

	public List<?> getEntities(EntityReference ref, Search search) {
		List<VendorEntity> entities = new ArrayList<VendorEntity>();

		if (search.isEmpty()) {
			// return all
			ResultSet rs = null;
			System.out.println(" getEntities "+ref.getId());

			try {
				stmt=conn.createStatement();

				rs = stmt.executeQuery("Select * from SMLite.Vendor");
				while (rs.next()) {
					String id = (String) rs.getString(VendorEntity.ID);
					String name = (String) rs.getString(VendorEntity.NAME);
					boolean poCreated = (Boolean) rs.getBoolean(VendorEntity.PO_CREATED);
					long poNumber = (Long) rs.getLong(VendorEntity.PO_NUMBER);
					String poDetails = (String) rs.getString(VendorEntity.PO_DETAILS);
					int poAmount = (Integer) rs.getInt(VendorEntity.PO_AMOUNT);
					String poDuration = (String) rs.getString(VendorEntity.PO_DURATION);
					String type = (String) rs.getString(VendorEntity.TYPE);
	    			
	    			VendorEntity e = new VendorEntity(id,name,poCreated, poNumber, poDetails,poAmount,poDuration,type);
	    			entities.add(e);
	            }
	        }catch (SQLException e) {
	        	try {
					if(stmt!=null)
					stmt.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		} else {
			// restrict based on search param
			if (search.getRestrictionByProperty("name") != null) {
				String sMatch = search.getRestrictionByProperty("name").value.toString();
				
			
				ResultSet rs = null;
				System.out.println(" getEntities else "+ref.getId());

				try {
					stmt=conn.createStatement();

					rs = stmt.executeQuery("Select * from SMLite.Vendor where id="+ref.getId() +" and name="+sMatch);
					while (rs.next()) {
						String id = (String) rs.getString(VendorEntity.ID);
						String name = (String) rs.getString(VendorEntity.NAME);
						boolean poCreated = (Boolean) rs.getBoolean(VendorEntity.PO_CREATED);
						long poNumber = (Long) rs.getLong(VendorEntity.PO_NUMBER);
						String poDetails = (String) rs.getString(VendorEntity.PO_DETAILS);
						int poAmount = (Integer) rs.getInt(VendorEntity.PO_AMOUNT);
						String poDuration = (String) rs.getString(VendorEntity.PO_DURATION);
						String type = (String) rs.getString(VendorEntity.TYPE);
		    			
		    			VendorEntity e = new VendorEntity(id,name,poCreated, poNumber, poDetails,poAmount,poDuration,type);
		    			entities.add(e);
					}
				} catch (SQLException e) {
					try {
						if(stmt!=null)
						stmt.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		}
		return entities;
	}

	public String[] getHandledOutputFormats() {
		return new String[] { Formats.HTML, Formats.JSON, Formats.XML, Formats.FORM };
	}

	public String[] getHandledInputFormats() {
		return new String[] { Formats.HTML, Formats.JSON, Formats.XML };
	}

	public boolean entityExists(String id) {
		boolean retValue = false;
		
		System.out.println(" entityExists "+id);

		ResultSet rs = null;
		
		try {
			stmt=conn.createStatement();
			rs = stmt.executeQuery("Select * from SMLite.Vendor where id="+id);
			while (rs.next()) {
				retValue = true;
			}
		}catch (SQLException e) {
			try {
				if(stmt!=null)
				stmt.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return retValue;
	}

}
