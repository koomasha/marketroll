/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataService.mrktr.dal.dbconnect;

import java.util.HashMap;
import org.neo4j.driver.v1.*;

/**
 *
 * @author KooMasha
 */
public class GraphConnection {

    private final String _dbAdress = "104.154.226.78";
    private final String _dbUser = "neo4j";
    private final String _dbPassword = "root";
    private final Config _config = Config.build().withEncryptionLevel( Config.EncryptionLevel.NONE ).toConfig();
    private final AuthToken _auth = AuthTokens.basic(_dbUser, _dbPassword);
    private final Driver _driver = GraphDatabase.driver("bolt://" + _dbAdress,_auth ,_config);
    private final Session _session;
    private Transaction _transaction;

    public GraphConnection() {
        _session = _driver.session();
    }

    public void beginTransaction() {
        if (_transaction == null || !_transaction.isOpen()) {
            _transaction = _session.beginTransaction();
        }
    }

    public void commit() {
        _transaction.success();
        _transaction.close();
        _transaction = null;
    }

    public void rollback() {
        _transaction.failure();
        _transaction.close();
        _transaction = null;
    }

    public void close() {
        _session.close();
    }

    public StatementResult run(String query, HashMap<String, Object> params) {
        if (_transaction != null) {
            return _transaction.run(query, params);
        }
        return _session.run(query, params);
    }
}
