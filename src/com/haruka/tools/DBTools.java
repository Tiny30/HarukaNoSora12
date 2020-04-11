package com.haruka.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * ���ݿ⹤����
 * @author haru@lich
 *
 */
public class DBTools {
	
	private static DataSource DATASOURCE = null;
	
	/** ��ʼ������Դ
	 * ͬ����������֤����Դ����ı�
	 * @param ds ��������Դ
	 * @throws Exception ���ѳ�ʼ�������׳������ݿ��ѳ�ʼ�����쳣��
	 */
	public static synchronized void dataSourceInit(DataSource ds) throws Exception{
		if(DATASOURCE == null){
			DATASOURCE = ds;
		}else{
			throw new Exception("���ݳ��ѳ�ʼ��");
		}
	}
	



	/** ��ȡ����
	 * @return �������ݿ�����
	 */
	public static Connection getConncetion(){
		Connection conn = null;
		try {
			conn = DBTools.DATASOURCE.getConnection();
			
			//ȡ���Զ��ύ
			conn.setAutoCommit(false);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return conn;
		
	}
	

	/** �����ύ 
	 * @param conn  ����һ�����ݿ����� 
	 */
	public static void commit(Connection conn){
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**����Ļع�
	 * @param conn ����һ�����ݿ�����
	 */
	public static void roolback(Connection conn){
		try {
			conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** �ͷ����ݿ����� ����ر�
	 * @param rs �����
	 * @param stmt ������PreparedStatement����
	 * @param conn ����һ�����ݿ�����
	 */
	public static void release(ResultSet rs, Statement stmt, Connection conn){
		try {
			if(rs != null){
				rs.close();
			}
			if(stmt != null){
				stmt.close();
			}
			if(conn != null){
				conn.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
