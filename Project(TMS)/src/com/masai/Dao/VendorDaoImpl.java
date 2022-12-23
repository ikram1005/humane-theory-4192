package com.masai.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.masai.Exception.VendorException;
import com.masai.Utility.JdbcUtil;
import com.masai.model.Vendor;

public class VendorDaoImpl implements VendorDao {

	@Override
	public String RegisterVendor(Vendor v) {
		String m = "Registration failed";
		Connection connection = JdbcUtil.provideConnection();

		PreparedStatement pst1 = null;
		PreparedStatement ps = null;

		try {
			pst1 = connection.prepareStatement("select * from vendor where email=?");
			pst1.setString(1, v.getEmail());
			ResultSet rs = pst1.executeQuery();

			if (rs.next()) {
				m = "Registeration Declined email id already exist ";

			} else {

				try {
					ps = connection.prepareStatement("insert into vendor values(?,?,?,?,?,?,?)");

					ps.setInt(1, v.getVendorid());
					ps.setString(2, v.getPassword());
					ps.setString(3, v.getName());
					ps.setString(4, v.getMobile());
					ps.setString(5, v.getEmail());
					ps.setString(6, v.getCompany());
					ps.setString(7, v.getAddress());

					int x = ps.executeUpdate();
					if (x > 0) {
						m = "registered successfully";
					}
				} catch (SQLException e) {
					e.printStackTrace();
					m = e.getMessage();
				}

			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			m = e1.getMessage();
		}

		return m;
	}

	@Override
	public Vendor loginVendor(String email, String pass) throws VendorException {
		Vendor vr = null;

		try (Connection conn = JdbcUtil.provideConnection()) {
			PreparedStatement ps = conn.prepareStatement("Select * from Vendor where email=? and password=?");

			ps.setString(1, email);
			ps.setString(2, pass);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int v = rs.getInt("vid");
				String p = rs.getString("password");
				String vn = rs.getString("vname");
				String m = rs.getString("vmob");
				String e = rs.getString("vemail");
				String c = rs.getString("company");
				String a = rs.getString("address");

				vr = new Vendor(v, p, vn, m, e, c, a);
			} else {
				throw new VendorException("invalid email and password");
			}

		} catch (SQLException e) {
			throw new VendorException(e.getMessage());
		}

		return vr;
	}

}
