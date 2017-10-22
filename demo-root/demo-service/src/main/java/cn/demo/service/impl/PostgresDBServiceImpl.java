package cn.demo.service.impl;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.slf4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.Statement;

import java.sql.PreparedStatement;

import cn.demo.service.PostgresDBService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;

public class PostgresDBServiceImpl implements PostgresDBService {
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PostgresDBServiceImpl.class);

	private ComboPooledDataSource comboPooledDataSource;

	private PreparedStatement insertKeyStatement;

	public PostgresDBServiceImpl(String dbUrl, String dbUser, String dbPw, String driver)
			throws PropertyVetoException, SQLException {
		comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setDriverClass(driver);
		comboPooledDataSource.setJdbcUrl(dbUrl);
		comboPooledDataSource.setUser(dbUser);
		comboPooledDataSource.setPassword(dbPw);

		createDatabase();
	}

	private void createDatabase() throws SQLException {

		/// XXX should be done via DDL script
//		comboPooledDataSource.getConnection().createStatement().executeUpdate(
//				"create table id_master_seq(sequence_id int) ENGINE = MYISAM;", Statement.RETURN_GENERATED_KEYS);
		/*comboPooledDataSource.getConnection().createStatement()
				.executeUpdate("CREATE TABLE `novelpage` (" + "`id`  bigint NOT NULL AUTO_INCREMENT ,"
						+ "`html`  mediumtext NULL ," + "`title` varchar(50)  NULL ," + "`url`  varchar(100) NULL ,"
						+ "`seen`  timestamp NULL ON UPDATE CURRENT_TIMESTAMP ," + "PRIMARY KEY (`id`)"
						+ ")" + ";");*/

		insertKeyStatement = comboPooledDataSource.getConnection()
				.prepareStatement("insert into novelpage (html,title,url,seen) values " + "(?,?,?,?)");

	}

	@Override
	public void store(Page page,Object params) {

		if (page.getParseData() instanceof HtmlParseData) {
			try {

				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();

				insertKeyStatement.setString(1, htmlParseData.getText());
				insertKeyStatement.setString(2, params.toString());
				insertKeyStatement.setString(3, page.getWebURL().getURL());
				insertKeyStatement.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
				insertKeyStatement.executeUpdate();
			} catch (SQLException e) {
				logger.error("SQL Exception while storing webpage for url'{}'", page.getWebURL().getURL(), e);
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void close() {
		if (comboPooledDataSource != null) {
			comboPooledDataSource.close();
		}
	}
}
