package njnu.opengms.container.enums;

/**
 * Created by SongJie on 2019/5/15 14:53
 */
public enum FromWhereEnum {
    PORTAL(0, "portal"),
    MODELCONTAINER(1, "modelContainer"),
    ONLINESAGA(2, "onlineSaga"),
    COMPARISON(3,"comparison")
    ;

    private int code;
    private String type;

    FromWhereEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
