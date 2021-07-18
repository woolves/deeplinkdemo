'''
根據美團多渠道打包方式,自動生成多渠道目錄
要求apk是使用V1加密方式打包的;
python3 multi_channel -s srcApkPath -v versionCode
python3 multi_channel -srcFile=srcApkPath --version=versionCode
'''
import os
import shutil
import zipfile
import time
import sys
import getopt

startTime = time.time()

prefixInfo = "debug"
srcApk = "./app-debug.apk"
version = ""  # 版本號
channelFilePath = "./channel"  # 渠道配置檔案路徑,每行定義一個渠道

toolInfo = '''參考美團多渠道打包方案1的打包工具;
預設使用當前目錄下 "channel" 檔案中定義的渠道資訊,每行一個渠道名稱,可通過 -c 引數來指定渠道檔案路徑;
要求apk使用的是v1簽名方式,若使用v2則本工具無效;
python3 multi_channel -s srcApkPath -v 1.7 -p demo -c ./channel
-s --srcFile : 新增一個源apk,會依據該apk生成多渠道apk,並儲存於 "./channelApk/" 中;
-v --version : 可選, 給生成的apk名稱新增一個版本號,會自動新增字首 _v{version},如 demo_v1.7.apk;
-p --prefix  : 可選, 給生成的apk名稱新增一個字首資訊,預設為"release"
-c --channel : 定義要生成的渠道包資訊,每行定義一個渠道名稱,會依次生成對應的渠道包'''

opts, args = getopt.getopt(sys.argv[1:], "hs:v:p:c:", ["help", "srcFile=", "version=", "prefix=", "channel="])
for name, value in opts:
    if name in ("-s", "--srcFile"):  # 原始檔名稱
        srcApk = value
    elif name in ("-v", "--version"):  # 版本號
        version = "_v%s" % value
    elif name in ("-p", "--prefix"):  # apk名稱字首資訊
        prefixInfo = value
    elif name in ("-c", "--channel"):  # 多渠道配置檔案
        channelFilePath = value
    elif name in ("-h", "--help"):
        print(toolInfo)
        exit()

print("srcApk = %s , version = %s" % (srcApk, version))

isApkExist = os.path.exists(srcApk)
if not isApkExist or not os.path.isfile(srcApk):
    print("%s 源apk檔案不存在,請重試" % srcApk)
    exit()

if not os.path.exists(channelFilePath) or not os.path.isfile(channelFilePath):
    print("%s channel渠道檔案不存在或者不是有效的file,請檢查後重試" % channelFilePath)
    exit()

pkgPath = os.path.join(os.getcwd(), "channelApk")  # 生成的多渠道apk存放的目錄
print("生成的apk會被存放於 %s" % pkgPath)

isPathExist = os.path.exists(pkgPath)
isDir = os.path.isdir(pkgPath)
if not isPathExist or not isDir:
    os.makedirs(pkgPath)

f = open(channelFilePath, 'r', encoding='utf-8')
for line in f:
    channel_name = line.strip('\n')
    # print("當前正在生成渠道包: %s" % channel_name)
    channelPath = pkgPath + "/{prefix}_{channel}{version}.apk".format(prefix=prefixInfo, channel=channel_name,
                                                                      version=version)
    shutil.copy(srcApk, channelPath)
    zipped = zipfile.ZipFile(channelPath, 'a', zipfile.ZIP_DEFLATED)
    empty_channel_file = "META-INF/tdchannel_{channel}".format(channel=channel_name)
    # zipped.write("empty", empty_channel_file) # 使用這種方式需要在當前目錄下存在empty檔案
    zipped.writestr(empty_channel_file, data=channel_name)
diff = time.time() - startTime
print("耗時: %s" % diff)