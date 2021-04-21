#!/usr/bin/env bash

echo "`mvn clean install -N`"
clear
init_dir=`pwd`
#服务名
SERVICE_NAME=""

#获取服务名函数
getServiceName(){
cur_dir=`pwd`
if [ -f $cur_dir ];then
	exit 1
fi
file_list=`ls -d nacos-*/ | cat -n`
echo "服务列表:"
echo "$file_list"
echo -n "请选择要执行的目录序号: "
read sequence
#判空
if [ -z "$sequence" ]; then
    echo "请选择序号!"
	exit 1
fi
#先设置分隔符为换行符
IFS=$'\n'
for i in $file_list
	do
	    t_sequence=`echo $i | awk -F ' ' '{print $1}'`
		  if test $t_sequence == $sequence;then
		  	_NAME=`echo $i | awk -F ' ' '{print $2}'`
			SERVICE_NAME=$cur_dir/$_NAME
		    if [ -f $SERVICE_NAME ];then
				break;
			fi
			cd $SERVICE_NAME
			#echo $SERVICE_NAME
			_parent=`find . -name .git`
			#echo $_parent
			if [ -n "$_parent" ]; then
		        getServiceName
			fi
			_pom=`find . -name pom*`
			#echo $_pom
			if [ -n "$_pom" ]; then
		        break;
			fi
		  fi
	done
}


while [ -z $SERVICE_NAME  ]
do
	    getServiceName
done

cd $SERVICE_NAME
echo ""
echo "------所选服务名------"
echo $SERVICE_NAME
echo "------拉取最新代码------"
echo "`git pull`"
echo "------开始clean install-----"
#执行生成命令
echo "`mvn clean install -Dmaven.test.skip=true`"

#“$?”来显示上一条命令执行的返回值，如果为0则代表执行成功
if [ $? -eq 0 ]; then
  cd $init_dir
  #exit
fi
exec /bin/bash

