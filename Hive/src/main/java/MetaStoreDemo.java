import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;

import java.util.List;

public class MetaStoreDemo {
    public static void main(String[] args) throws TException {

        HiveConf hiveConf = new HiveConf();
        hiveConf.addResource("hive-site.xml");

        HiveMetaStoreClient client = new HiveMetaStoreClient(hiveConf);

        //获取数据库信息
        List<String> tablesList = client.getAllTables("test");
        System.out.print("test数据所有的表:  ");
        for (String s : tablesList) {
            System.out.print(s + "\t");
        }
        System.out.println();

        //获取表信息
        System.out.println("test.t3 表信息: ");
        Table table= client.getTable("test","t3");
        List<FieldSchema> fieldSchemaList= table.getSd().getCols();
        for (FieldSchema schema: fieldSchemaList) {
            System.out.println("字段: " + schema.getName() + ", 类型: " + schema.getType());
        }

        client.close();
    }
}
