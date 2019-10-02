package script;

public final class SqlScripts {

    public static final String sDbSchema =
            "CREATE TABLE IF NOT EXISTS Currency " +
                    "( " +
                    "    id   INT PRIMARY KEY, " +
                    "    name VARCHAR2(30) " +
                    "); " +
                    " " +
                    "CREATE TABLE IF NOT EXISTS Bank_account " +
                    "( " +
                    "    id             IDENTITY, " +
                    "    owner_name     VARCHAR(256)   NOT NULL, " +
                    "    balance        DECIMAL(19, 4) NOT NULL, " +
                    "    currency_id    INT            NOT NULL, " +
                    "    FOREIGN KEY (currency_id) REFERENCES currency (id) " +
                    "); " +
                    " " +
                    "CREATE TABLE IF NOT EXISTS Transaction_status " +
                    "( " +
                    "    id   INT PRIMARY KEY, " +
                    "    name VARCHAR(30) " +
                    "); " +
                    " " +
                    "CREATE TABLE IF NOT EXISTS Transaction_type " +
                    "( " +
                    "    id   INT PRIMARY KEY, " +
                    "    name VARCHAR(30) " +
                    "); " +
                    " " +
                    "CREATE TABLE IF NOT EXISTS Transaction " +
                    "( " +
                    "    id              IDENTITY, " +
                    "    from_account_id number         NOT NULL, " +
                    "    to_account_id   number         NOT NULL, " +
                    "    amount          DECIMAL(19, 4) NOT NULL, " +
                    "    currency_id     INT            NOT NULL, " +
                    "    creation_date   TIMESTAMP      NOT NULL, " +
                    "    update_date     TIMESTAMP, " +
                    "    status_id       VARCHAR(256)   NOT NULL, " +
                    "    type_id         int, " +
                    " " +
                    "    FOREIGN KEY (from_account_id) REFERENCES bank_account (id), " +
                    "    FOREIGN KEY (to_account_id) REFERENCES bank_account (id), " +
                    "    FOREIGN KEY (currency_id) REFERENCES currency (id) " +
                    ")";

    public final static String initialData =
            "INSERT INTO currency (id, name) " +
            "VALUES " +
            "  (1, 'USD'), " +
            "  (2, 'EUR'), " +
            "  (3, 'GBP'); " +
            " " +
            "INSERT INTO Transaction_status (id, name) " +
            "VALUES " +
            "  (1, 'DEPOSIT'), " +
            "  (2, 'WITHDRAWAL'); " +
            " " +
            "INSERT INTO bank_account (owner_name, balance, currency_id) " +
            "VALUES " +
            "  ('Elias Spanos', 1000, 3), " +
            "  ('Nicos Michael', 1000, 2), " +
            "  ('Marios Andreou', 1000, 1); ";
}
