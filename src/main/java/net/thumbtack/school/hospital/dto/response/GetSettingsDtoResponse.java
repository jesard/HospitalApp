package net.thumbtack.school.hospital.dto.response;

public class GetSettingsDtoResponse {

    private int maxNameLength;
    private int minPasswordLength;

    public GetSettingsDtoResponse(int maxNameLength, int minPasswordLength) {
        this.maxNameLength = maxNameLength;
        this.minPasswordLength = minPasswordLength;
    }

    public GetSettingsDtoResponse() {
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }
}
