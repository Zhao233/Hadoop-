# mapreduce程序中打log的方式

## 直接使用System.out.println
1. 日志输出地址: <hadoop安装目录>下的logs/userLog/application——******<任务号>/container_******<分块>/stdout
2. 缺点: 无法将指定的log输出到指定的文件，并且有的container不一定执行map和reduce过程，导致有的container不输出stdout，并且查找日志困难
3. 注意: 输出格式要明确，列如: <title>: <content>。 title要有区别度，content不适宜过长。 