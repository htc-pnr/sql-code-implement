package exercise;

import exercise.model.Row;
import exercise.model.Table;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SqlTest {

    public static final String  PURCHASES_FILE_NAME = "src/main/resources/purchases.csv";
    public static final String  USERS_FILE_NAME = "src/main/resources/users.csv";

    private InputStream purchasesIS;
    private InputStream usersIS;
    Sql sql;

    @Before
    public void init() throws IOException {

        purchasesIS = new FileInputStream(PURCHASES_FILE_NAME);
        usersIS = new FileInputStream(USERS_FILE_NAME);
        sql = new Sql();
    }

    @Test
    public void usersInitTest(){

        Table table = sql.init(usersIS);

        assertEquals(5, table.getRows().size());

        assertEquals("user_id", table.getColumns().get(0).getName());
        assertEquals("name", table.getColumns().get(1).getName());
        assertEquals("email", table.getColumns().get(2).getName());

        List<String> rowData = table.getRows().get(0).getData();

        assertEquals("2", rowData.get(0));
        assertEquals("manuel", rowData.get(1));
        assertEquals("manuel@foo.de", rowData.get(2));

    }

    @Test
    public void purchasesInitTest(){

        Table table = sql.init(purchasesIS);

        assertEquals(8, table.getRows().size());

        assertEquals("ad_id", table.getColumns().get(0).getName());
        assertEquals("title", table.getColumns().get(1).getName());
        assertEquals("user_id", table.getColumns().get(2).getName());

        List<String> rowData = table.getRows().get(0).getData();

        assertEquals("1", rowData.get(0));
        assertEquals("car-1", rowData.get(1));
        assertEquals("1", rowData.get(2));

    }

    @Test
    public void usersOrderByDescTest()
    {
        Table table = sql.init(usersIS);

        table = sql.orderByDesc(table, "user_id");

        List<Row> rows = table.getRows();

        assertEquals("5", rows.get(0).getData().get(0));
        assertEquals("4", rows.get(1).getData().get(0));
        assertEquals("3", rows.get(2).getData().get(0));
        assertEquals("2", rows.get(3).getData().get(0));
        assertEquals("1", rows.get(4).getData().get(0));
    }

    @Test
    public void purchasesOrderByDescTest()
    {
        Table table = sql.init(purchasesIS);

        table = sql.orderByDesc(table, "title");

        List<Row> rows = table.getRows();

        assertEquals("table-2", rows.get(0).getData().get(1));
        assertEquals("table-1", rows.get(1).getData().get(1));
        assertEquals("guitar-2", rows.get(2).getData().get(1));
        assertEquals("guitar-1", rows.get(3).getData().get(1));
        assertEquals("chair-1", rows.get(4).getData().get(1));
        assertEquals("car-3", rows.get(5).getData().get(1));
        assertEquals("car-2", rows.get(6).getData().get(1));
        assertEquals("car-1", rows.get(7).getData().get(1));

    }

    @Test
    public void usersPurchasesJoinTest()
    {
        Table users = sql.init(usersIS);
        Table purchases = sql.init(purchasesIS);

        Table join = sql.join(users, purchases, "user_id", "user_id");

        assertEquals(8, join.getRows().size());

        assertEquals("user_id", join.getColumns().get(0).getName());
        assertEquals("name", join.getColumns().get(1).getName());
        assertEquals("email", join.getColumns().get(2).getName());
        assertEquals("ad_id", join.getColumns().get(3).getName());
        assertEquals("title", join.getColumns().get(4).getName());
        assertEquals("user_id", join.getColumns().get(5).getName());

        List<String> firstRow =  join.getRows().get(0).getData();

        assertEquals("2", firstRow.get(0));
        assertEquals("manuel",firstRow.get(1));
        assertEquals("manuel@foo.de",firstRow.get(2));
        assertEquals("4",firstRow.get(3));
        assertEquals("guitar-1",firstRow.get(4));
        assertEquals("2",firstRow.get(5));
    }



}
