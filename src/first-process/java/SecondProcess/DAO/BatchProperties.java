package SecondProcess.DAO;

class BatchProperties {
    private String tableName;
    private String preparedValue;
    private int batchMax;
    private int numVal;



    BatchProperties(String tableName, String preparedValue, int batchMax, int numVal) {
        this.tableName = tableName;
        this.preparedValue = preparedValue;
        this.batchMax = batchMax;
        this.numVal = numVal;

    }

    String getTableName() {
        return tableName;
    }

    String getPreparedValue() {
        return preparedValue;
    }

    int getBatchMax() {
        return batchMax;
    }
    public int getNumVal() {
        return numVal;
    }

    public void setNumVal(int numVal) {
        this.numVal = numVal;
    }
}
