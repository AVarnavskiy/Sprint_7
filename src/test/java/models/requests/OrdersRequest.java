package models.requests;

public class OrdersRequest {
    private int courierId;
    private String nearStation;
    private String limit;
    private int page;

    public OrdersRequest() {
    }

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }

    public String getNearStation() {
        return nearStation;
    }

    public void setNearStation(String nearStation) {
        this.nearStation = nearStation;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
