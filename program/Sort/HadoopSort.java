import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class HadoopSort {
    //private static final Log mylog =LogFactory.getLog("MyLog");
    /**
     * 快速排序算法
     * 由小到大
     * */
    public static List<Integer> Sort(List<Integer> list, int startPos, int endPos){//有小到大
        if(startPos == endPos){
            return null;
        }

        //System.out.println("startPos: "+startPos);
        //System.out.println("endPos: "+endPos);

        int index_key = startPos;

        for( int index = startPos; index < endPos; index++ ){
            if(list.get(index) < list.get(index_key)){
                moveItemOverKey(list, index, index_key);
                index_key++;
            }
        }

        //System.out.println("index_key: "+index_key);
        //System.out.println("=============================================================");

        if(startPos != index_key){
            Sort(list, startPos, index_key-1);
        }

        if(endPos != index_key){
            Sort(list, index_key+1, endPos);
        }

        System.out.println("start: "+ startPos + "  end: "+endPos+"  sort result : "+list+"\n");

        if(startPos == 0 && endPos == list.size() ){
            return list;
        } else {
            return null;
        }
    }

    public static void moveItemOverKey(List<Integer> list, int pos, int keyPos){//将比key值小的元素移到key的右边
        int temp_value = list.get(pos);

        for(int i = keyPos; i >=0 ; i--){
            if(list.get(i) <= temp_value){//遇到比自己小的
                list.remove(pos);
                list.add(i+1, temp_value);

                break;
            }

            if(keyPos == 0 || i == 0){//key在开头位置,或者已经移动到了队列尽头
                list.remove(pos);
                list.add(i, temp_value);

                break;
            }


        }
    }

    public static class TokenMapper extends Mapper< Object, Text, Text, Text> {// KeyIn, valueIn, keyOut, valueOut
        private final static Text name = new Text("result");

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            if(value.toString().equals("")){
                return;
            }

            System.out.println("map-input-key: " + key.toString() + " || map-input-value: "+value.toString()+"\n");

            StringTokenizer itr = new StringTokenizer(value.toString());

            List<Integer> list = new LinkedList<>();

            while( itr.hasMoreTokens() ){
                String string_value = itr.nextToken();

                list.add(Integer.parseInt(string_value));
            }

            Sort(list,0,list.size());

            System.out.println("sortComplete result : " + list.toString() +"\n");

            String arrayString = "";

            for(Integer temp_int : list){
                arrayString+= String.valueOf(temp_int)+" ";
            }

            System.out.println("map-output-key: " + key.toString() + " || map-output-value: "+arrayString+"\n");

            context.write(name, new Text(arrayString));
        }
    }

    public static class SortReducer extends Reducer<Text, Text, Text, Text>{// KeyIn, valueIn, keyOut, valueOut
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            System.out.println("reduce-input-key: " + key.toString() + " || reduce-input-value: "+values.toString());
            //mylog.info("reduce-input-key: " + key.toString() + " || reduce-input-value: "+values.toString());

            List<Integer> list = new LinkedList<>();


            for(Text array : values){
                StringTokenizer itr = new StringTokenizer(array.toString());

                while( itr.hasMoreTokens() ){
                    list.add(Integer.valueOf(itr.nextToken()));
                }
            }

            list = Sort(list,0,list.size());

            System.out.println("sortComplete result : " + list.toString() +"\n");

            String arrayString = "";

            for(Integer temp_int : list){
                arrayString+= String.valueOf(temp_int)+" ";
            }

            System.out.println("reduce-output-key: " + key.toString() + " || reduce-output-value: "+arrayString);

            context.write(key, new Text(arrayString));
        }
    }

    public static void main(String[] args) throws Exception {
        //mylog.info("program start");

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "hadoop sort test");

        job.setJarByClass(HadoopSort.class);

        job.setMapperClass(HadoopSort.TokenMapper.class);
        job.setCombinerClass(HadoopSort.SortReducer.class);
        job.setReducerClass(HadoopSort.SortReducer.class);

        //mylog.info("set mapper combiner reducer complete");

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //mylog.info("set InputFormat OutFormat complete");

        FileInputFormat.addInputPath(job, new Path(args[0]));

        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //mylog.info("ready to go");


        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
