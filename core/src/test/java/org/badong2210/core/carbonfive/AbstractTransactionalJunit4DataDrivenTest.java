package org.badong2210.core.carbonfive;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.IMetadataHandler;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "carbonFiveTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public abstract class AbstractTransactionalJunit4DataDrivenTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource(name = "&sessionFactory")
    protected LocalSessionFactoryBean hibernateSessionFactory;
    @Resource(name = "hibernateTemplate")
    protected HibernateTemplate hibernateTemplate;
    @Resource(name = "jdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    protected IDataTypeFactory dbUnitDataTypeFactory;
    protected IMetadataHandler dbUnitMetadataHandler;

    @Before
    public void before() throws Exception {
        dbUnitDataTypeFactory = (IDataTypeFactory) applicationContext.getBean("dbUnitDataTypeFactory");
        dbUnitMetadataHandler = (IMetadataHandler) applicationContext.getBean("dbUnitMetadataHandler");

        setupDbUnitData();
    }

    protected void setupDbUnitData() throws DatabaseUnitException, Exception {
        Connection conn = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
        IDatabaseConnection dbUnitConn = new DatabaseConnection(conn);

        populateData(dbUnitConn);

        DataSourceUtils.releaseConnection(conn, jdbcTemplate.getDataSource());
    }


    @After
    public void after() throws Exception {
    }

    protected void populateData(IDatabaseConnection dbUnitConn) throws Exception {
        IDataSet dataSet = getDBUnitDataSet();

        if (dataSet != null) {
            dbUnitConn.getConfig().setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, dbUnitMetadataHandler);
            dbUnitConn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, dbUnitDataTypeFactory);
            getDatabaseOperation().execute(dbUnitConn, dataSet);
        }
    }

    protected DatabaseOperation getDatabaseOperation() {
        return DatabaseOperation.CLEAN_INSERT;
    }

    protected IDataSet getDBUnitDataSet() {
        List<org.springframework.core.io.Resource> datasets = getDatasetResources();

        List<IDataSet> dbUnitDataSets = new ArrayList<IDataSet>(datasets.size());
        for (org.springframework.core.io.Resource dataSetLocation : datasets) {
            try {
                File processedInputData = dataSetLocation.getFile();
                XmlDataSet xml = new XmlDataSet(new FileInputStream(processedInputData));
                dbUnitDataSets.add(xml);
            } catch (Exception e) {
                logger.warn("Could not load dataset " + dataSetLocation.getFilename() + ", ignoring", e);
            }
        }

        List<org.springframework.core.io.Resource> flatXMLDatasets = getFlatXMLDatasetResources();

        List<IDataSet> dbUnitDataSets2 = new ArrayList<IDataSet>(flatXMLDatasets.size());
        for (org.springframework.core.io.Resource dataSetLocation : flatXMLDatasets) {
            try {
                FlatXmlDataSet xml = new FlatXmlDataSet(dataSetLocation.getFile(), false, true);
                dbUnitDataSets2.add(xml);
            } catch (Exception e) {
                logger.warn("Could not load flat xml dataset " + dataSetLocation.getFilename() + ", ignoring", e);
            }
        }
        try {
            dbUnitDataSets2.addAll(dbUnitDataSets);

            return new CompositeDataSet(dbUnitDataSets2.toArray(new IDataSet[dbUnitDataSets2.size()]));
        } catch (DataSetException e) {
            logger.warn("Could not create composite dataset");
            throw new RuntimeException("Could not create composite dataset", e);
        }
    }

    private List<org.springframework.core.io.Resource> getDatasetResources() {
        List<org.springframework.core.io.Resource> datasetLocations = new ArrayList<org.springframework.core.io.Resource>();
        DataSet dataSet = getClass().getAnnotation(DataSet.class);

        if (dataSet != null && dataSet.locations().length > 0) {
            for (String location : dataSet.locations()) {
                datasetLocations.add(new org.springframework.core.io.ClassPathResource(location));
            }
        }
        return datasetLocations;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface DataSet {
        public String[] locations();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface FlatXMLDataSet {
        public String[] locations();
    }

    private List<org.springframework.core.io.Resource> getFlatXMLDatasetResources() {
        List<org.springframework.core.io.Resource> datasetLocations = new ArrayList<org.springframework.core.io.Resource>();
        FlatXMLDataSet dataSet = getClass().getAnnotation(FlatXMLDataSet.class);

        if (dataSet != null && dataSet.locations().length > 0) {
            for (String location : dataSet.locations()) {
                datasetLocations.add(new org.springframework.core.io.ClassPathResource(location));
            }
        }
        return datasetLocations;
    }

}