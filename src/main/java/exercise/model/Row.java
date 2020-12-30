package exercise.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Row {

    Integer number;
    Integer key;
    List<String> data;
}
