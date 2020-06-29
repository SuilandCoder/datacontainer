package njnu.opengms.container.enums;

/**
 * @EnumName DataResourceTypeEnum
 * @Description todo
 * @Author sun_liber
 * @Date 2019/3/7
 * @Version 1.0.0
 */
public enum DataResourceTypeEnum {
    SHAPEFILE(0, "shapefile"),
    GEOTIFF(1, "geotiff"),
    UDX(2, "udx"),
    OTHER(3, "other"),
    SDAT(4,"sdat"),
    SHAPEFILE_LIST(5,"shapefile_list"),
    GEOTIFF_LIST(6,"geotiff_list"),
    SDAT_LIST(7,"sdat_list"),
    NETCDF(8,"netcdf"),
    CSV(9,"csv");

    private int code;
    private String type;

    DataResourceTypeEnum(int code, String type) {
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
