import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class testIP {
    public static void main(String[] args) {
        IP("223.104.4.232");
    }

    public static Map IP(String ip){
        Map<String,String> map= new HashMap<>();
        File database = new File("D:\\HadoopProject\\ALL\\JavaLearn\\IP\\src\\main\\resources\\GeoLite2-City.mmdb");
        DatabaseReader reader = null;
        try {
            reader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        CityResponse response = null;
        try {
            response = reader.city(ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }

        Country country = response.getCountry();
        Subdivision subdivision = response.getMostSpecificSubdivision();
        City city = response.getCity();

        map.put("city",city.getNames().get("zh-CN"));
        map.put("country",country.getNames().get("zh-CN").toString());
        map.put("subdivision",subdivision.getNames().get("zh-CN"));
        System.out.println(map);
        return  map;
    }
}

