package exercise.exception;

public class ColumnNotFoundException extends  RuntimeException{

    public ColumnNotFoundException(String columnName)
    {
        super("Column not found : "+ columnName);
    }
}
