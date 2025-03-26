package finalProject.dishcoveryServer.models.exception;

import java.util.Date;

public class ApiError {
    
    private int statusCode;
    private String messsage;
    private Date timeStamp;
    private String url;


    public ApiError(){

    }

    public ApiError(int statusCode, String messsage, Date timeStamp) {
        this.statusCode = statusCode;
        this.messsage = messsage;
        this.timeStamp = timeStamp;
    }

    public ApiError(int statusCode, String messsage, Date timeStamp, String url) {
        this.statusCode = statusCode;
        this.messsage = messsage;
        this.timeStamp = timeStamp;
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getMesssage() {
        return messsage;
    }
    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }
    public Date getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



}

