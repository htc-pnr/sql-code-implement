package exercise.model;

import exercise.ColumnType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Column {

    private String name;
    private Integer number;
    private ColumnType type;

}
