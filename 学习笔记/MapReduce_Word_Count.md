# MapReduce Word Count 例子的学习笔记

## 流程: 
1. 输入(Input): 输入文件后，由被指定的TextInputFormat，通过map方法，一次处理一行。然后这一行被StringTokenizer通过空格分为tokens，并且向context中输出一个键对值< <Word>, 1 >。  
第一个map:  
< Hello, 1>  
< World, 1>  
< Bye, 1>  
< World, 1>  
第二个map:  
< Hello, 1>  
< Hadoop, 1>  
< Goodbye, 1>  
< Hadoop, 1>  

2. 合并(Combine): WordCount同样指定了一个combiner。因此，在通过key们分类合并过后，每一个map的输出会通过一个为了本地合并的本地combiner(Reducer)。也就是将key相同的健队值合并(如果key相同，将它们的value合并)。合并对象为每一个map。  
合并后的第一个map:  
< Bye, 1>  
< Hello, 1>  
< World, 2>  
合并后的第二个map:  
< Goodbye, 1>  
< Hadoop, 2>  
< Hello, 1>  

3. Reduce: Reducer类通过reduce方法将每一个相同的key出现过的次数(value)相加在一起。将不同的map中，相同的key的value合并在一起。  
输出: 
< Bye, 1>  
< Goodbye, 1>  
< Hadoop, 2>  
< Hello, 2>  
< World, 2>  

## 包说明
1. org.apache.hadoop.io.Text
    + 解释: 用于存储text字符串，使用标准的UTF-8编码。它在内部提供了序列化，反序列化和在字节(byte)级的文本比较。它的长度单位是Integer，并且使用无损压缩的格式。
    + 参考资料  
    与String的区别: https://www.cnblogs.com/huiAlex/p/8182586.html
2. Context  
    + 解释: 它是Mapper的一个内部类它记录了上下文信息(传递参数), 作为了map和reduce执行中各个函数的一个桥梁
    + 参考资料  
    https://blog.csdn.net/nagisaclz/article/details/47254645

3. IntWritable
    + 解释: 它是一个实现了WritableComparable接口的类，目的是为了实现在网络传输中的所需要的序列化和反序列化方法。它的作用实际上就是一个可以序列化，反序列化，可比较的一个int包装类。
    + 参考资料: https://blog.csdn.net/ghuilee/article/details/45705169