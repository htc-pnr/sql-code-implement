package exercise;

import exercise.exception.ColumnNotFoundException;
import exercise.model.Column;
import exercise.model.Row;
import exercise.model.Table;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Sql {

    public  static final String COMMA_DELIMITER = ",";

    public Table init(InputStream csvContent) {

        Table table = null;

        try (final Reader reader = new InputStreamReader(csvContent)) {

            BufferedReader br = new BufferedReader(reader);
            AtomicInteger rowCount = new AtomicInteger(1);
            AtomicInteger columnCount = new AtomicInteger(0);

            List<Column> columns = Arrays.stream(br.readLine().split(COMMA_DELIMITER))
                                          .map(value ->
                                                  Column.builder()
                                                  .name(value.toLowerCase(Locale.ENGLISH))
                                                  .number(columnCount.getAndIncrement())
                                                  .build())
                                          .collect(Collectors.toList());

            List<Row> rows = br.lines()
                                        .skip(0)
                                        .map(line ->
                                                {
                                                    List<String> data = Arrays.stream(line.split(COMMA_DELIMITER))
                                                                                 .collect(Collectors.toList());
                                                    Integer key = Integer.parseInt(data.get(0));

                                                    return Row.builder()
                                                            .number(rowCount.getAndIncrement())
                                                            .data(data)
                                                            .key(key)
                                                            .build();
                                                })
                                        .collect(Collectors.toList());

            table = Table.builder()
                         .columns(columns)
                         .rows(rows)
                         .build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return table;
    }	

    public Table orderByDesc(Table table, String columnName){

        this.orderByDesc(table, getColumnNumberByName(table, columnName));

        return table;
    }

    private void orderByDesc(Table table, Integer columnNumber)
    {
          table.getRows().sort(
                  (row1, row2) -> {

                      String field1 = row1.getData().get(columnNumber);
                      String field2 = row2.getData().get(columnNumber);

                      return field2.compareTo(field1);
                  });
    }

    public Table join(Table left, Table right, String joinColumnTableLeft, String joinColumnTableRight) {

        List<Column> joinColumns = left.getColumns();
        joinColumns.addAll(right.getColumns());

        Integer columnLeft = getColumnNumberByName(left, joinColumnTableLeft);
        Integer columnRight = getColumnNumberByName(right, joinColumnTableRight);

        List<Row> joinRows = new ArrayList<>();
        left.getRows().stream().forEach(leftRow -> {

                  joinRows.addAll(right.getRows()
                                            .stream()
                                            .filter(rightRow -> {

                                                        String fieldLeft = leftRow.getData().get(columnLeft);
                                                        String fieldRight = rightRow.getData().get(columnRight);

                                                        return fieldLeft.equals(fieldRight);
                                                    })
                                            .map(rightRow -> {

                                                        List<String> joinRowData = leftRow.getData();
                                                        joinRowData.addAll(rightRow.getData());
                                                        return Row.builder()
                                                                  .data(joinRowData).build();
                                                    })
                                            .collect(Collectors.toList()));
                });

        return Table.builder()
                .columns(joinColumns)
                .rows(joinRows).build();
    }

    private Integer getColumnNumberByName(Table table, String columnName) {

        Optional<Column> columnOpt = table.getColumns().stream()
                                    .filter(column -> column.getName().equals(columnName.toLowerCase(Locale.ENGLISH)))
                                    .findFirst();

        return columnOpt.map(Column::getNumber)
                        .orElseThrow(() -> new ColumnNotFoundException(columnName));
    }
}
