git config --global http.postBuffer 1048576000
startTime=$(date +%s)

WORKSPACERoot=$(cd `dirname $0`;cd ..; pwd)
echo ${WORKSPACERoot}
WORKSPACE=${WORKSPACERoot}/app
backup_Dir=${WORKSPACERoot}/apppackage/lastdebugapp
BuildLog=${WORKSPACERoot}/apppackage

#读取签名配置文件
configpath=${WORKSPACERoot}/local.properties
function prop {
    echo `cat $configpath | grep -w ^${1} | cut -d= -f2`
}

RELEASE_KEY_PASSWORD=$(prop "RELEASE_KEY_PASSWORD")
RELEASE_STOREFILE=$(prop "RELEASE_STOREFILE")
RELEASE_STOREPASSWORD=$(prop "RELEASE_STOREPASSWORD")
RELEASE_KEY_ALIAS=$(prop "RELEASE_KEY_ALIAS")
buildtools25=$(prop "sdk.dir")/build-tools/25.0.2

#这种方法不能处理key中包含.的情况
#while read line;
#do
#    eval "$line"
#    echo $line
#done < ${WORKSPACERoot}/local.properties


echo keypassword :${RELEASE_KEY_PASSWORD}
echo keyPath :${RELEASE_STOREFILE}
echo password :${RELEASE_STOREPASSWORD}
echo alias :${RELEASE_KEY_ALIAS}
echo sdkdir :${buildtools25}


#开始打包
cd ${WORKSPACERoot}
echo 开始打包
pwd
mkdir ${BuildLog}/
touch ${BuildLog}/newDebugbuildlog.txt
sh ./gradlew clean assembleDebug | grep -v 'newDebugbuildlog.txt' > ${BuildLog}/newDebugbuildlog.txt

#解析log看是否打包成功
while read channelsline
do
if echo "$channelsline"|grep -q -E "^:"
then
continue
elif [[ "$channelsline" = "BUILD SUCCESSFUL" ]];then
echo 'BUILD SUCCESSFUL'
break
elif [[ "$channelsline" = "BUILD FAILED" ]];then
echo 'BUILDFAILED'
cat ${BuildLog}/newDebugbuildlog.txt
exit
fi
done <${BuildLog}/newDebugbuildlog.txt


#开始对其
rm -rf ${WORKSPACE}/build/outputs/duiqi
mkdir ${WORKSPACE}/build/outputs/duiqi
echo 开始4k对齐
ls ${WORKSPACE}/build/outputs/apk/debug/ |grep -v 'apkduiqilist.txt' > ${WORKSPACE}/build/apkduiqilist.txt

duiqiisOk=false
while read lineduiqi
do
  if [[ "${lineduiqi##*.}"x = "apk"x ]];then
    echo ${WORKSPACE}/build/outputs/apk/debug/${lineduiqi}
    ${buildtools25}/zipalign -v 4   ${WORKSPACE}/build/outputs/apk/debug/${lineduiqi}  ${WORKSPACE}/build/outputs/duiqi/${lineduiqi}
        duiqiisOk=ok
  fi
done < ${WORKSPACE}/build/apkduiqilist.txt


#检查对其结果
if [ "$duiqiisOk" != ok ];then
echo 4k对齐失败
exit
echo 4k对齐成功
fi


#开始签名
echo startsign
ls ${WORKSPACE}/build/outputs/duiqi/ |grep -v 'apksign.txt' > ${WORKSPACE}/build/apksign.txt
while read linesign
do
  if [[ "${linesign##*.}"x = "apk"x ]];then
  echo ${WORKSPACE}/build/outputs/duiqi/${linesign}
  ${buildtools25}/apksigner sign --ks   ${RELEASE_STOREFILE} --ks-key-alias ${RELEASE_KEY_ALIAS} --ks-pass pass:${RELEASE_KEY_PASSWORD}  ${WORKSPACE}/build/outputs/duiqi/${linesign}
    uploadPath=${WORKSPACE}/build/outputs/duiqi/${linesign}

fi
done < ${WORKSPACE}/build/apksign.txt
echo signsuccess
echo ${uploadPath}


#开始上传蒲公英
curl -F "file=@"${uploadPath} -F "buildInstallType=2"  -F "buildPassword=603777"  -F "buildUpdateDescription=内测体验版(canary)" -F "uKey=ed639f2e5cac76e08c1eb24b775c2b69" -F "_api_key=30ddd93225add3ed02677a8ae9722d80" https://www.pgyer.com/apiv2/app/upload
#result=`git log --pretty=format:"%s" -n 10`
#curl -F "file=@"${uploadPath} -F "updateDescription=内测体验版 ${result}" -F "uKey=ed639f2e5cac76e08c1eb24b775c2b69"  -F "_api_key=30ddd93225add3ed02677a8ae9722d80"  https://qiniu-storage.pgyer.com/apiv1/app/upload
#curl -F "file=@/Users/mac/Desktop/backup/Android_lyf_mall1/app/build/outputs/channels/tiyanban_debug_7.0.59_1119_1045_com.umaman.laiyifen_7059_jiagu.apk" -F "buildInstallType=2"  -F "buildPassword=603777"  -F "updateDescription=内测体验版" -F "uKey=ed639f2e5cac76e08c1eb24b775c2b69" -F "_api_key=30ddd93225add3ed02677a8ae9722d80" https://www.pgyer.com/apiv2/app/upload


rm -rf ${backup_Dir}/
mkdir ${backup_Dir}/
#体验版都不备份
cp -R ${WORKSPACE}/build/* ${backup_Dir}/


echo
echo 打包完成,请打开链接下载
echo https://www.pgyer.com/6dX6

endTime=$(date +%s)
timeUsed=$(( $endTime - $startTime ))
echo "打包总用时："+$timeUsed